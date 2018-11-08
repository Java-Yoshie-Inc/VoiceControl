package skills;

import java.util.ArrayList;

import main.Bot;

public class Skills extends ArrayList<Skill> {
	
	private static final long serialVersionUID = 1L;
	
	public Skills(Bot bot) {
		add(new Skill1(bot));
	}
	
}
