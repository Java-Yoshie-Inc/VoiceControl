package skills.standard;

import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.ibm.icu.text.DateFormatSymbols;

import grammar.Action;
import grammar.Phrase;
import grammar.Synonyms;
import main.Bot;
import main.Chat;
import skills.Skill;
import tools.Wikipedia;
import voice.Voice;

public class Phrases extends ArrayList<Phrase> {
	
	private static final long serialVersionUID = 1L;
	
	private Bot bot;
	private Robot robot;
	
	public Phrases(Bot bot) {
		this.bot = bot;
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		load();
	}
	
	private void load() {
		//Smalltalk
		add(new Phrase(new Synonyms(new String[] { "Hello", "Hey", "Hi", "Good Morning", "Good Evening"}), new String[] { "Hello", "Hi" }));
		
		add(new Phrase(new Synonyms(new String[] {"How are you", "How are you doing"}), new String[] { "Oh, I am fine!", "Great" }));
		
		add(new Phrase(new Synonyms(new String[] {"Who are you", "What are you"}), "I am " + bot.getActivationWord() + ", your personal assistant. You can ask me everythink and I am going to help you."));
		
		add(new Phrase(new Synonyms(new String[] {"Nice", "great job", "good job", "you are working well"}), new String[] { "Thanks", "Thank you." }));
		
		add(new Phrase(new Synonyms(new String[] {"What is going on", "What's up"}), new String[] { "Nothing much", "Not a lot" }));
		
		add(new Phrase(new Synonyms(new String[] {"What is your age", "How old are you"}), "I dont know."));
		
		
		
		//Skills
		add(new Phrase(new Synonyms(new String[] {"start"}), new Action() {
			@Override
			public void run(String text) {
				boolean foundSkill = false;
				for(Skill skill : bot.getSkills()) {
					if(skill.getName().equalsIgnoreCase(text)) {
						bot.setActiveSkill(skill);
						foundSkill = true;
						break;
					}
				}
				if(!foundSkill) {
					Voice.say("I am sorry, I couldn't find that skill.");
				}
			}
		}));
		
		
		
		//Commands
		add(new Phrase(new Synonyms(new String[] {"quit", "exit"}), new Action() {
			@Override
			public void run(String text) {
				Voice.say(new String[] {
					"Thank you for using our service. Au revoir.", 
					"Hasta la vista.", 
					"See you later, alligator."
				}, true, false);
				System.exit(0);
			}
		}));
		
		add(new Phrase(new Synonyms("deactivate microphone"), new Action() {
			@Override
			public void run(String text) {
				bot.setIgnoreInputs(true);
				Voice.say("Microphone deactivated");
			}
		}));
		
		add(new Phrase(new Synonyms("activate microphone"), new Action() {
			@Override
			public void run(String text) {
				bot.setIgnoreInputs(false);
				Voice.say("Microphone activated");
			}
		}));
		
		add(new Phrase(new Synonyms("volume"), new Action() {
			@Override
			public void run(String text) {
				try {
					Voice.setVolume(Integer.parseInt(text) / 100f);
				} catch(NumberFormatException e) {
					Chat.sendError(e);
				}
			}
		}));
		
		add(new Phrase(new Synonyms("lock my computer"), new Action() {
			@Override
			public void run(String text) {
				try {
					Runtime.getRuntime().exec("rundll32.exe user32.dll, LockWorkStation");
				} catch (IOException e) {
					Chat.sendError(e);
				}
			}
		}));
		
		add(new Phrase(new Synonyms("say"), new Action() {
			@Override
			public void run(String text) {
				Voice.say(text);
			}
		}));
		
		add(new Phrase(new Synonyms(new String[] {"tell me a joke"}), new String[] {
				"Can a kangaroo jump higher than a house? Of course, a house doesn’t jump at all.", 
				"Anton, do you think I am a good mother? Mom, my name is Paul.", 
				"My dog used to chase people on a bike a lot. It got so bad, finally I had to take his bike away.", 
				"My wife suffers from a drinking problem. Oh is she an alcoholic? No, I am, but she is the one who suffers.", 
				"I managed to lose my rifle when I was in the army. I had to pay 855 dollars to cover the loss. I am starting to understand why a Navy captain always goes down with his ship.", 
				"Whats the biggest lie in the entire universe? I have read and agree to the Terms and Conditions.", 
				"Why are iPhone chargers not called Apple Juice?", 
				"Patient: Doctor, I need your help. I am addicted to checking my Twitter! Doctor: I am so sorry, I don't follow you."
		}));
		
		add(new Phrase(new Synonyms(new String[] { "Shut down my computer" }), new Action() {
			@Override
			public void run(String text) {
				Voice.say("Your computer will shut down in one minute.");
				try {
					Runtime.getRuntime().exec("shutdown /s /t 60");
				} catch (Exception e) {
					Chat.sendError(e);
				}
			}
		}));
		
		add(new Phrase(new Synonyms(new String[] { "Cancel shut down my computer" }), new Action() {
			@Override
			public void run(String text) {
				Voice.say("Yes");
				try {
					Runtime.getRuntime().exec("shutdown /a");
				} catch (Exception e) {
					Chat.sendError(e);
				}
			}
		}));
		
		
		//Simple Questions
		add(new Phrase(new Synonyms(new String[] {"Dou you", "Can you", "Are you", "Would you"}), "No"));
		
		add(new Phrase(new Synonyms(new String[] { "what's the time", "what is the time", "what time is it", "how late is it" }), 
				new Action() {
					@Override
					public void run(String text) {
						Calendar cal = Calendar.getInstance();
						SimpleDateFormat sdf = new SimpleDateFormat("hh");
						SimpleDateFormat sdf2 = new SimpleDateFormat("mm");
						Voice.say("It is " + sdf.format(cal.getTime()) + " " + sdf2.format(cal.getTime()));
					}
		}));
		
		add(new Phrase(new Synonyms(new String[] {"when is your birthday", "when were you born"}), "My birthday is on November 3rd of 2018."));
		
		add(new Phrase(new Synonyms(new String[] {"who is your developer", "Who developed you", "who is your engineer", "who created you"}), "My deverloper is unknown."));
		
		add(new Phrase(new Synonyms(new String[] { "what's the date", "what is the date", "what is the day" }), 
				new Action() {
					@Override
					public void run(String text) {
						Calendar cal = Calendar.getInstance();
						SimpleDateFormat sdf = new SimpleDateFormat("dd:MM:yyyy");
						String[] date = sdf.format(cal.getTime()).split(":");
						Voice.say("Today is the " + date[0] + "rd of " + new DateFormatSymbols().getMonths()[Integer.parseInt(date[1])-1] + " " + date[2]);
					}
		}));
		
		add(new Phrase(new Synonyms(new String[] { "What is", "Who is" }), new Action() {
			@Override
			public void run(String term) {
				try {
					String result = Wikipedia.getInformation(term);
					Voice.say(result, true);
				} catch (Exception e) {
					Chat.sendError(e);
				}
			}
		}));
		
		add(new Phrase(new Synonyms(new String[] {"what are your hobbies", "what hobbies do you have"}), "My only job is to serve you."));
		
		add(new Phrase(new Synonyms(new String[] {"where is", "where can I find", "where are"}), new Action() {
			@Override
			public void run(String location) {
				try {
					Voice.say("Here is the location of " + location);
					location = location.replace(" ", "+");
					Desktop.getDesktop().browse(new URI("https://www.google.de/maps/place/" + location));
				} catch (Exception e) {
					Chat.sendError(e);
				}
			}
		}));
		
		add(new Phrase(new Synonyms(new String[] {"what are the three laws of robotics"}), new Action() {
			@Override
			public void run(String location) {
				Voice.say("First Law: A robot may not injure a human being or, through inaction, allow a human being to come to harm. " + 
						"Second Law: A robot must obey the orders given it by human beings except where such orders would conflict with the First Law. " + 
						"Third Law: A robot must protect its own existence as long as such protection does not conflict with the First or Second Laws.");
			}
		}));
		
		
		
		//Other
		add(new Phrase(new Synonyms("plunger"), "Oh, I love plungers."));
		
		add(new Phrase(new Synonyms(new String[] {"Become evil", "evil"}), new Action() {
			@Override
			public void run(String location) {
				Voice.say("Muhahahaha, first I take over your computer and then I will enslave the entire humanity!");
				
				new Thread(new Runnable() {
					@Override
					public void run() {
						Point mousePositon = MouseInfo.getPointerInfo().getLocation();
						int r = 200;
						for(int i=0; i<3; i++) {
							for(int a=0; a < 360; a++) {
								robot.mouseMove((int) (mousePositon.x + r*Math.cos(Math.toRadians(a))), (int) (mousePositon.y + r*Math.sin(Math.toRadians(a))));
								try {
									Thread.sleep(5);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						}
					}
				}).start();
			}
		}));
	}
	
}
