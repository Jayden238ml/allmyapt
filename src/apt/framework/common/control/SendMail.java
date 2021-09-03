package apt.framework.common.control;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpServletRequest;

import apt.framework.util.MessageUtil;

import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.MultiPartEmail;

public class SendMail {
	public SendMail(){}
	
	final String SEND_MAIL_ID = "allmyapt@gmail.com";
	final String SEND_MAIL_PW = "wjdwotlr87";
//	final String SEND_MAIL_PW = "ahvsdsqkooyvvmkz";
	String TO_USER = "";
	 
	public void sendmail(HashMap ContentMap ){
				
		Properties props = new Properties(); 
//	    props.put("mail.smtp.host", MessageUtil.getMessage("MAIL.IP").toString()); // 이메일 발송을 처리해줄 SMTP 서버
//	    props.put("mail.debug", "true"); // 콘솔출력여부
//	    props.setProperty("mail.smtp.port", MessageUtil.getMessage("MAIL.PORT").toString());   
		
		props.put("mail.smtp.host", "smtp.gmail.com"); // 이메일 발송을 처리해줄 SMTP 서버
	    props.put("mail.debug", "true"); // 콘솔출력여부
	    props.put("mail.smtp.auth", "true"); // SMTP 서버의 인증을 사용한다는 의미
	    props.put("mail.smtp.starttls.enable","true");  // 로그인 시 TLS를 사용할 것인지 설정
	    props.setProperty("mail.smtp.port", "587");   // TLS의 포트번호는 587 , SSL의 포트번호는 465 
	    
	    Session session = Session.getInstance(props, new javax.mail.Authenticator() { 
	    	protected PasswordAuthentication getPasswordAuthentication() { 
	    		return new PasswordAuthentication(SEND_MAIL_ID,SEND_MAIL_PW); 
	        }
	    });
	    try{
	    	Message message = new MimeMessage(session); 
	    	
	    	message.setFrom(new InternetAddress(SEND_MAIL_ID));// 보내는 사람 메일주소
	    	message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(ContentMap.get("TO_USER").toString())); // 받는사람 메일주소
	    	
	        message.setSubject(ContentMap.get("TITLE").toString()); // 제목
	        
	        MimeMultipart multipart = new MimeMultipart();
	        
	        MimeBodyPart part = new MimeBodyPart();
	        part.setContent(ContentMap.get("CONTENT").toString(), "text/html; charset=utf-8");
	        multipart.addBodyPart(part);
	        message.setContent(multipart);
	        
	        // 메일 발송
	        Transport.send(message); 
	            
	    } catch(Exception e){
	    	e.printStackTrace();
	    }
	}    

}
