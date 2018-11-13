package skills;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import main.Bot;

public abstract class Skill {
	
	protected ScheduledExecutorService service;
	
	protected final String NAME;
	protected final Bot bot;
	
	private boolean receivedInput = false;
	private String lastInput = null;
	
	protected Skill(Bot bot, String name) {
		this.NAME = name;
		this.bot = bot;
	}
	
	public void start() {
		bot.setUseActivationWord(false);
		stop();
		setup();
		service = Executors.newSingleThreadScheduledExecutor();
		service.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				System.out.println(this);
				loop();
			}
		}, 0, 10, TimeUnit.MILLISECONDS);
	}
	
	public synchronized void stop() {
		if(service != null) {
			service.shutdownNow();
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
