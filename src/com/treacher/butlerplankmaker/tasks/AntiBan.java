package com.treacher.butlerplankmaker.tasks;

import java.awt.Point;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.treacher.butlerplankmaker.PlankMaker;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Component;
import org.powerbot.script.rt6.Hud.Window;

public class AntiBan extends Task<ClientContext> {

	long antiBanLastRunTime = System.currentTimeMillis();
	final int maxFriendComponents = 5;

	public AntiBan(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public boolean activate() {
		if (System.currentTimeMillis() - antiBanLastRunTime > Random.nextInt(100000, 200000)) {
			antiBanLastRunTime = System.currentTimeMillis();
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void execute() {
		int randomNumber = Random.nextInt(1,8);
		
		switch(randomNumber) {
			case 1:
				PlankMaker.STATE = "AntiBan: Move camera";
				moveCamera();
				break;
			case 2:
				PlankMaker.STATE = "AntiBan: Check friends list";
				checkFriendsList();
				break;
			case 3:
				PlankMaker.STATE = "AntiBan: Check skills";
				checkSkills();
				break;
			case 4:
				PlankMaker.STATE = "AntiBan: Move mouse";
				moveMouse();
				break;
			default:
				break;
		}
	}
	
	private void moveCamera() {
		int randomNumber = Random.nextInt(0,1);
		
		switch(randomNumber) {
			case 0:
				ctx.camera.angle(Random.nextInt(300, 380));
				break;
			case 1:
				ctx.camera.angle(Random.nextInt(40, -40));
				break;
			default:
				break;
		}
	}
	
	/*
	 * Opens up friends list and hovers over a random amount of your friends. The more friends the better but it has 
	 * a upper limit of checking 5 friends because I think any more over that starts to look a bit strange. Once finished looking
	 * it goes back to your backpack.
	 */
	private void checkFriendsList() {
		ctx.hud.open(Window.FRIENDS);
		
		final Component friendsListComponent = ctx.widgets.component(550, 6);
		final List<Component> friendsComponents =  Arrays.asList(friendsListComponent.components());
		
		//Restrict it to only look at a max of 5 friends
		final int friendsToHoverCount = friendsComponents.size() > maxFriendComponents ? maxFriendComponents : friendsComponents.size();
		
		// Shuffle up the list to give some randomness
		Collections.shuffle(friendsComponents);
		
		Point firstPoint = friendsComponents.get(0).nextPoint();
		
		//Always move to the first point because if you don't the social hover widget will cover the social menu.
		ctx.input.move(firstPoint);
		
		//Start from the second element
		for(int i=1; i < friendsToHoverCount; i++) {
			if(Random.nextInt(0, 2) == 0) {
				// Only slightly vary the y coordinate
				final int yVariance = friendsComponents.get(i).centerPoint().y += Random.nextInt(-2, 2);
				ctx.input.move(firstPoint.x, yVariance);
				Condition.sleep(Random.nextInt(800, 1200));
			}
		}

		backToBackpack();
	}
	
	/*
	 * Opens up skill list and randomly selects a few skills to check the experience of. Once finished checking it goes back to your backpack.
	 */
	private void checkSkills() {
		ctx.hud.open(Window.SKILLS);
		
		final int skillCountToCheck = Random.nextInt(1,3);
		
		final Component skillsWindowComponent = ctx.widgets.component(1466, 11);
		// Loop through a few random skills
		for(int i = 0; i < skillCountToCheck; i++){
			final int skillId = Random.nextInt(0, 23);
			final Component randomSkillComponent = skillsWindowComponent.component(skillId);
			ctx.input.move(randomSkillComponent.nextPoint());
			Condition.sleep(Random.nextInt(1500, 2000));
		}
		backToBackpack();
	}
	
	private void moveMouse() {
		ctx.input.move(Random.nextInt(100, 400), Random.nextInt(100, 400));
	}
	
	/*
	 * Sets the HUD to backpack and moves the mouse away from the backpack
	 */
	private void backToBackpack() {
		ctx.hud.open(Window.BACKPACK);
		Condition.sleep();
		moveMouse();
	}

}
