package skills;

import main.Bot;
import voice.Voice;

public class Skill1 extends Skill {
	
	public Skill1(Bot bot) {
		super(bot, "Fun");
	}
	
	@Override
	public void setup() {
		Voice.say("Welcome to this skill!", false);
		loop();
	}
	
	@Override
	protected void loop() {
		while(true) {
			Voice.say("How are you?", false);
			String input = waitForInput();
			Voice.say("You are " + input, false);
		}	
	}
	
}
