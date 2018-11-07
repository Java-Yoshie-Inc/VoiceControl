package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Port;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.result.WordResult;
import grammar.ActivationWord;
import voice.Voice;

public class SpeechRecognizer {
	
	private LiveSpeechRecognizer recognizer;
	private Logger logger = Logger.getLogger(getClass().getName());
	
	private final ActivationWord NAME;
	private final SpeechRecognizeEvent EVENT;
	private ArrayList<String> blacklist = new ArrayList<String>();
	
	private boolean ignoreSpeechRecognitionResults = false;
	private boolean speechRecognizerThreadRunning = false;
	private boolean resourcesThreadRunning;
	private ExecutorService eventsExecutorService = Executors.newFixedThreadPool(2); //This executor service is used in order the playerState events to be executed in an order
	
	private boolean useActivationWord = true;
	private boolean isEnabled = false;
	
	public SpeechRecognizer(ActivationWord name, SpeechRecognizeEvent event) {
		this.NAME = name;
		this.EVENT = event;
		
		// FORMAT
		System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tF %1$tT] [%4$-7s] %5$s %n");
		
		// Loading Message
		logger.log(Level.INFO, "Loading Speech Recognizer...");
		
		// Configuration
		Configuration configuration = new Configuration();
		configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
		configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
		
		//Uncomment this line of code if you want the recognizer to recognize every word of the language 
		configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin");
		
		// Grammar
		/*configuration.setGrammarPath("resource:/grammars");
		configuration.setGrammarName("grammar");
		configuration.setUseGrammar(true);*/
		
		try {
			recognizer = new LiveSpeechRecognizer(configuration);
		} catch (IOException e) {
			logger.log(Level.SEVERE, null, e);
		}
		
		// Start recognition process pruning previously cached data.
		// recognizer.startRecognition(true);
		
		//Check if needed resources are available
		startResourcesThread();
		//Start speech recognition thread
		startSpeechRecognition();
	}
	
	//-----------------------------------------------------------------------------------------------
	
	public synchronized void startSpeechRecognition() {
		//Check lock
		if (speechRecognizerThreadRunning) {
			logger.log(Level.INFO, "Speech Recognition Thread already running...");
		} else {
			//Submit to ExecutorService
			eventsExecutorService.submit(() -> {
				//locks
				speechRecognizerThreadRunning = true;
				ignoreSpeechRecognitionResults = false;
				
				//Start Recognition
				recognizer.startRecognition(true);
				
				//Information
				Voice.say("Speech recognition is running...", false);
				logger.log(Level.INFO, "Speach recognition running...");
				
				try {
					while (speechRecognizerThreadRunning) {
						SpeechResult speechResult = recognizer.getResult();
						
						//Check if we ignore the speech recognition results
						if (!ignoreSpeechRecognitionResults) {
							
							//Check the result
							if (speechResult == null) {
								logger.log(Level.INFO, "I can't understand what you said.");
							} else {
								//Get the hypothesis
								String speechRecognitionResult = speechResult.getHypothesis();
								
								//Call the appropriate method 
								makeDecision(speechRecognitionResult, speechResult.getWords());
							}
						} else
							logger.log(Level.INFO, "Ingoring Speech Recognition Results...");
						
					}
				} catch (Exception ex) {
					logger.log(Level.WARNING, null, ex);
					speechRecognizerThreadRunning = false;
				}
				
				logger.log(Level.INFO, "SpeechThread has exited...");
			});
		}
	}
	
	public void startResourcesThread() {
		//Check lock
		if (resourcesThreadRunning) {
			logger.log(Level.INFO, "Resources Thread already running...");
		} else {
			//Submit to ExecutorService
			eventsExecutorService.submit(() -> {
				try {
					//Lock
					resourcesThreadRunning = true;
					
					// Detect if the microphone is available
					while (true) {
						//Is the Microphone Available
						if (!AudioSystem.isLineSupported(Port.Info.MICROPHONE))
							logger.log(Level.INFO, "Microphone is not available.");
						
						// Sleep some period
						Thread.sleep(350);
					}
					
				} catch (InterruptedException e) {
					logger.log(Level.WARNING, null, e);
					resourcesThreadRunning = false;
				}
			});
		}
	}
	
	public void makeDecision(String speech , List<WordResult> speechWords) {
		//System.out.println(speech + " " + Arrays.deepToString(speechWords.toArray(new WordResult[0])));
		System.out.println(speech + useActivationWord);
		
		if(!useActivationWord || NAME.getSynonyms().equals(speech)) {
			isEnabled = true;
			if(useActivationWord) {
				Voice.say("Yes", false, true);
				return;
			}
		}
		
		for(String string : blacklist) {
			if(string.equals(speech)) {
				return;
			}
		}
		
		if(isEnabled) {
			isEnabled = false;
			EVENT.say(speech);
		} else {
			System.out.println("Cant understand: " + speech);
		}
	}
	
	public synchronized void setIgnoreSpeechRecognitionResults(boolean ignore) {
		this.ignoreSpeechRecognitionResults = ignore;
	}
	public boolean getIgnoreSpeechRecognitionResults() {
		return ignoreSpeechRecognitionResults;
	}
	public boolean getSpeechRecognizerThreadRunning() {
		return speechRecognizerThreadRunning;
	}
	public void askQuestion(boolean question) {
		this.hasNextQuestion = question;
	}
	public boolean useActivationWord() {
		return useActivationWord;
	}
	public void setUseActivationWord(boolean useActivationWord) {
		this.useActivationWord = useActivationWord;
	}
	public ArrayList<String> getBlacklist() {
		return blacklist;
	}
	public void setBlacklist(ArrayList<String> blacklist) {
		this.blacklist = blacklist;
	}
	
}
