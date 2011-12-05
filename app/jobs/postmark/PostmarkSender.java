package jobs.postmark;

import com.postmark.java.PostmarkClient;
import com.postmark.java.PostmarkException;
import com.postmark.java.PostmarkMessage;
import com.postmark.java.PostmarkResponse;
import com.postmark.java.PostmarkStatus;

import play.Logger;
import play.Play;
import play.i18n.Messages;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.modules.postmark.Postmark;
import play.test.Fixtures;

public class PostmarkSender extends Job {
	
	/**
	 * Client that communicates with Postmark's REST API.
	 */
	private static PostmarkClient postmarkClient;
	
	/**
	 * The message *this* sender will send
	 */
	private PostmarkMessage message;
	
	/**
	 * Private, use {@link #sendMail(...)} instead!
	 * @param message
	 */
	private PostmarkSender(PostmarkMessage message) {
		this.message = message;
	}
	
    public void doJob() {
    	try {
    		if(Postmark.ctx.isTest()) {
    			logMessage(message);
    		}
    		PostmarkResponse resp = postmarkClient.sendMessage(message);
    		
    		if(Postmark.ctx.isTest() || !PostmarkStatus.SUCCESS.equals(resp.getStatus()) ) {
    			logResponse(resp);
    		}
    	} catch (PostmarkException e) {
    		Logger.warn("Could not send mail to '%s':", message.getToAddress(), e.getMessage());
    	}
    }
    
    /**
     * Send mail given recipient from given sender.
     * @param from - sender, must be a valid "sender signature" registered @ postmarkapp.com
     * @param to
     * @param subject
     * @param body - message content
     */
    public static void sendMail(String from, String to, String subject, String body) {
    	if(from == null && Postmark.ctx.getDefaultFrom() != null) {
    		from = Postmark.ctx.getDefaultFrom();
    	}
    	sendMail(new PostmarkMessage(from, to, null, null, subject, body, false, null));
    }
    
    /**
     * Send mail to given recipient.
     * @param to
     * @param subject
     * @param body - message content
     */
    public static void sendMail(String to, String subject, String body) {
    	sendMail(null, to, subject, body);
    }
    
    /**
     * Creates a new AsyncMailJob and runs it.
     * @param message
     * @param isTest
     */
    public static void sendMail(PostmarkMessage message) {
    	assert Postmark.ctx != null : "Postmark module has not been initialized";
		assert message.getFromAddress() != null : "Cannot send mail, 'from' is null";

    	if(postmarkClient == null) {
    		postmarkClient = new PostmarkClient(Postmark.ctx.getApiKey());
    	}
    	new PostmarkSender(message).doJob();
    }
    
    
	private static void logMessage(PostmarkMessage message) {
		Logger.info(
			new StringBuilder()
			.append("\n------- Postmark will send message ------")
			.append("\nTo: ").append(message.getToAddress())
			.append("\nFrom: ").append(message.getFromAddress())
			.append("\nSubject: ").append(message.getSubject())
			.append("\nBody:\n").append( message.getTextBody() == null ? message.getHtmlBody() : message.getTextBody() )
			.toString()
		);
	}

	private static void logResponse(PostmarkResponse resp) {
		Logger.info(
			new StringBuilder()
			.append("\n------- Postmark response ------")
			.append("\nStatus: ").append(resp.getStatus())
			.append("\nStatus message: ").append(resp.getMessage())
			.append("\nError code: ").append(resp.getErrorCode())
			.append("\nTo: ").append(resp.getTo())
			.toString()
		);
	}
}
