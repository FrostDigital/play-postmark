package controllers;

import jobs.postmark.PostmarkSender;
import play.mvc.Controller;

public class Application extends Controller {

    public static void index() {
    	PostmarkSender.sendMail(
    			// To
    			"luke@space.com", 
    			// Subject
    			"Hello my son", 
    			// Body
    			"How are you? I am fine, although a bit lonely :|"
    	);
        renderText("Mail sent!");
    }

}