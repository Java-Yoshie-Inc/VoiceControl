package skills;

import main.Bot;

public abstract class Skill {
	
	private Thread THREAD;
	
	protected final String NAME;
	protected final Bot bot;
	
	private boolean receivedInput = false;
	private String lastInput = null;
	
	protected Skill(Bot bot, String name) {
		this.NAME = name;
		this.bot = bot;
	}
	
	public void start() {
		bot.getSpeechRecognizer().setUseActivationWord(false);
		stop();
		setup();
		this.THREAD = new Thread(new Runnable() {
			@Override
			public void run() {
				loop();
			}
		});
		THREAD.start();
	}
	
	public void stop() {
		if(THREAD != null) {
			THREAD.interrupt();
		}
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
			} catch (InterruptedException e) {}
		}
	}
	
	public void sendInput(String input) {
		this.receivedInput = true;
		this.lastInput = input;
	}
	
	public String getName() {
		return NAME;
	}
	
}
