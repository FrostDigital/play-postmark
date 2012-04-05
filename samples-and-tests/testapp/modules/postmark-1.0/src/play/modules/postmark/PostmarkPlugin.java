package play.modules.postmark;

import play.Logger;
import play.PlayPlugin;

public class PostmarkPlugin extends PlayPlugin {
	
	@Override
	public void onApplicationStart() {
		Postmark.init();
	}
}
