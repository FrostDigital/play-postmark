package controllers;

import play.*;
import play.modules.postmark.Postmark;
import play.mvc.*;

import java.util.*;

import models.*;

public class Application extends Controller {

    public static void index() {
    	Postmark.sendMail("luke.skywalker@space.com", "Something I want to tell you", "I've googled this, and it seems I might be your father :|");
    	renderText("OK");
    }

}