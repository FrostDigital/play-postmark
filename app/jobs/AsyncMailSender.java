package jobs;

import play.Logger;
import play.Play;
import play.i18n.Messages;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.modules.postmark.Postmark;
import play.test.Fixtures;

@OnApplicationStart
public class AsyncMailSender extends Job {
	
	private String email;
	private String subject;
	private String body;
	
	public AsyncMailSender(String email, String subject, String body) {
		this.email = email;
		this.subject = subject;
		this.body = body;
	}
	
    public void doJob() {
		Postmark.sendMail(email, subject, body);
    }
    
}
