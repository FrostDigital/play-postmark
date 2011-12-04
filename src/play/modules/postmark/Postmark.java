package play.modules.postmark;

import play.Logger;
import play.Play;

/**
 * @author Joel Soderstrom
 *
 * TODO: Add support for sending mail with HTML content 
 * TODO: Add support for attachments
 */
public class Postmark {
	
	public static Postmark ctx = null;

	private static final String PROP_API_KEY = "postmark.apiKey";
	private static final String PROP_FROM = "postmark.from";
	
	private boolean isTest = false;
	
	/**
	 * API-key needed to authenticate at Postmarkapp.com
	 * http://developer.postmarkapp.com/developer-build.html#authentication-headers
	 */
	private final String apiKey;

	/**
	 * E-mail address that will populate "from" field in mails.
	 * This address will be overridden if "from" is set in 
	 * {@link #sendMail(String, String, String)}.
	 * 
	 * Note that this address must be a valid "sender signature" registered 
	 * in your account @ postmarkapp.com.
	 */
	private final String defaultFrom;
	
	public static void init() {
		ctx = new Postmark(
			Play.configuration.getProperty(PROP_FROM), 
			Play.configuration.getProperty(PROP_API_KEY)
		);
	}
	
	private Postmark(String defaultFrom, String apiKey) {
		this.defaultFrom = defaultFrom;
		this.apiKey = apiKey;

		if(apiKey != null) {
			/*
			 * Key set to POSTMARK_API_TEST indicates that we only test run. 
			 * In that case enable logging for mail response from server. 
			 * 
			 * Source:
			 * http://developer.postmarkapp.com/developer-build.html#authentication-headers
			 */
			this.isTest = apiKey.equals("POSTMARK_API_TEST");
		} 
		else {
			Logger.warn("No API-key for postmark specified, please set property '%s'", PROP_API_KEY);
		}
	}
	
	public String getDefaultFrom() {
		return defaultFrom;
	}
	
	public String getApiKey() {
		return apiKey;
	}
	
	public boolean isTest() {
		return isTest;
	}
}
