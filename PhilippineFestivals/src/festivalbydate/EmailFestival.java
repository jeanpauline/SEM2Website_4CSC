package festivalbydate;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;
 
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * Servlet implementation class FestivalByDate
 */
@WebServlet("/EmailFestival")
public class EmailFestival extends HttpServlet {
	private static final long serialVersionUID = 1L;
    final String sourceEmail = "sem2website@gmail.com"; // requires valid Gmail id
    final String password = "sem2project"; // correct password for Gmail id
    String toEmail = ""; // any destination email id   
    
    public EmailFestival() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		toEmail = request.getParameter("emailadd");
		try {
			sendEmail();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static Properties mailServerProperties;
    static Session getMailSession;
    static MimeMessage msg;
    
    public void sendEmail() throws AddressException, MessagingException {
 
        System.out.println("\n1st ===> setup Mail Server Properties..");
 
 
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
 
        System.out
                .println("\n2nd ===> create Authenticator object to pass in Session.getInstance argument..");
 
        Authenticator authentication = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sourceEmail, password);
            }
        };
        Session session = Session.getInstance(props, authentication);
        generateAndSendEmail(
                session,
                toEmail,
                "Crunchify's JavaMail API example with Image Attachment",
                "Greetings, <br><br>Test email by Crunchify.com JavaMail API example. Please find here attached Image."
                        + "<br><br> Regards, <br>Crunchify Admin");
 
    }
 
    public static void generateAndSendEmail(Session session, String toEmail, String subject,
            String body) {
        try {
            System.out.println("\n3rd ===> generateAndSendEmail() starts..");
 
            MimeMessage crunchifyMessage = new MimeMessage(session);
            crunchifyMessage.addHeader("Content-type", "text/HTML; charset=UTF-8");
            crunchifyMessage.addHeader("format", "flowed");
            crunchifyMessage.addHeader("Content-Transfer-Encoding", "8bit");
 
            crunchifyMessage.setFrom(new InternetAddress("noreply@crunchify.com",
                    "NoReply-Crunchify"));
            crunchifyMessage.setReplyTo(InternetAddress.parse("noreply@crunchify.com", false));
            crunchifyMessage.setSubject(subject, "UTF-8");
            crunchifyMessage.setSentDate(new Date());
            crunchifyMessage.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(toEmail, false));
 
            // Create the message body part
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(body, "text/html");
 
            // Create a multipart message for attachment
            Multipart multipart = new MimeMultipart();
 
            // Set text message part
            multipart.addBodyPart(messageBodyPart);
 
            messageBodyPart = new MimeBodyPart();
 
            // Valid file location
            String filename = "/PhilippineFestivals/WebContent/img/decc.jpg";
            DataSource source = new FileDataSource(filename);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(filename);
            // Trick is to add the content-id header here
            messageBodyPart.setHeader("Content-ID", "image_id");
            multipart.addBodyPart(messageBodyPart);
 
            System.out.println("\n4th ===> third part for displaying image in the email body..");
            messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent("<br><h3>Find below attached image</h3>"
                    + "<img src='cid:image_id'>", "text/html");
            multipart.addBodyPart(messageBodyPart);
            crunchifyMessage.setContent(multipart);
 
            System.out.println("\n5th ===> Finally Send message..");
 
            // Finally Send message
            Transport.send(crunchifyMessage);
 
            System.out
                    .println("\n6th ===> Email Sent Successfully With Image Attachment. Check your email now..");
            System.out.println("\n7th ===> generateAndSendEmail() ends..");
 
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
