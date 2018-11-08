package skills;

import main.Bot;

public abstract class Skill extends Thread {
	
	protected final String NAME;
	protected final Bot bot;
	
	private boolean receivedInput = false;
	private String lastInput = null;
	
	Skill(Bot bot, String name) {
		this.NAME = name;
		this.bot = bot;
	}
	
	@Override
	public void start() {
		super.start();
	}
	
	@Override
	public void run() {
		super.run();
		bot.getSpeechRecognizer().setUseActivationWord(false);
		setup();
	}
	
	protected abstract void setup();
	
	protected abstract void loop();
	
	protected final String waitForInput() {
		receivedInput = false;
		while(true) {
			if(receivedInput) {
				return lastInput;
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void sendInput(String input) {
		this.receivedInput = true;
		this.lastInput = input;
	}
	
	public String getSkillName() {
		return NAME;
	}
	
}
