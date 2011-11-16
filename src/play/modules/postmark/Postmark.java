package play.modules.postmark;

import com.postmark.java.PostmarkClient;
import com.postmark.java.PostmarkException;
import com.postmark.java.PostmarkMessage;
import com.postmark.java.PostmarkResponse;

import play.Logger;
import play.Play;

public class Postmark {
	
	private static final String PROP_API_KEY = "play.postmark.apiKey";
	private static final String PROP_FROM = "play.postmark.from";
//	private static final String PROP_LOG_MESSAGES = "play.postmark.log-messages";
	
	private static boolean isTest = false;
	
	/**
	 * API-key needed to authenticate at Postmarkapp.com
	 * http://developer.postmarkapp.com/developer-build.html#authentication-headers
	 */
	private static String apiKey;

	/**
	 * E-mail address that will populate "from" field in mails.
	 * Note that this address will be overridden if "from" is set in {@link #sendMail(String, String, String)}
	 */
	private static String from;
	
//	/**
//	 * If sent messages should be logged to default logger (debug)
//	 */
//	private static boolean logMessages = false;
	
	/**
	 * Client that communicates with Postmark's REST API.
	 */
	private static PostmarkClient postmarkClient;
	
	
	public static void init() {
		apiKey = Play.configuration.getProperty(PROP_API_KEY);
		from = Play.configuration.getProperty(PROP_FROM);
//		logMessages= Play.configuration.getProperty(PROP_LOG_MESSAGES);

		if(apiKey != null) {
			postmarkClient = new PostmarkClient(apiKey);
			
			/*
			 * Key set to POSTMARK_API_TEST indicates that we only test run. 
			 * In that case enable logging for mail response from server. 
			 * 
			 * Source:
			 * http://developer.postmarkapp.com/developer-build.html#authentication-headers
			 */
			isTest = apiKey.equals("POSTMARK_API_TEST");
		} 
		else {
			Logger.warn("No API-key for postmark specified, please set property '%s'", PROP_API_KEY);
		}
	}
	
    public static void sendMail(String from, String to, String subject, String body) {
    	sendMail(new PostmarkMessage(from, to, null, null, subject, body, false, null));
    }
    
    public static void sendMail(String to, String subject, String body) {
    	sendMail(null, to, subject, body);
    }

    public static void sendMail(PostmarkMessage message) {
    	if(message.getFromAddress() == null && from != null) {
    		message.setFromAddress(from);
    	}
    	
    	try {
    		if(isTest) {
    			logMessage(message);
    		}
    		PostmarkResponse resp = postmarkClient.sendMessage(message);
    		
    		if(isTest) {
    			logResponse(resp);
    		}
    	} catch (PostmarkException e) {
    		Logger.warn("Could not send mail to '%s':", message.getToAddress(), e.getMessage());
    	}
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
