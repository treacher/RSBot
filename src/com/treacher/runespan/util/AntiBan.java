package com.treacher.runespan.util;

import com.treacher.runespan.RuneSpan;
import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Hud.Window;

import java.util.concurrent.Callable;

/**
 * Created by Michael Treacher
 */

public class AntiBan {

    private long lastTimeAFKing = System.currentTimeMillis();
    private long lastTimeCheckingSkill = System.currentTimeMillis();
    private long lastTimeMovingMouse = System.currentTimeMillis();

    private final ClientContext ctx;

    public AntiBan(ClientContext ctx) {
        this.ctx = ctx;
    }

    public void execute() {
        if(timeForMouseMovement()) triggerMouseEvent();
//        if(timeForCheckSkill()) checkSkill();
        if(timeForAFK()) goAFK();
    }

    // AFK every 60 minutes
    private boolean timeForAFK() {
        return (System.currentTimeMillis() - lastTimeAFKing) > (Random.nextInt(50000,70000) * 60);
    }

    private boolean timeForCheckSkill() {
        return (System.currentTimeMillis() - lastTimeCheckingSkill) > (Random.nextInt(50000,70000) * 30);
    }

    private boolean timeForMouseMovement() {
        return (System.currentTimeMillis() - lastTimeMovingMouse) > (Random.nextInt(50000,70000) * 2.5);
    }

    private void triggerMouseEvent() {
        RuneSpan.STATE = "Anti Ban: Moving Mouse";

        lastTimeMovingMouse = System.currentTimeMillis();

        int randomNumber = Random.nextInt(1,3);

        switch(randomNumber) {
            case 1:
                moveMouse();
                break;
            case 2:
                mouseOffScreen();
                break;
            default:
                break;
        }
    }

    // Don't really want to go AFK all that often.
    private void goAFK(){
        RuneSpan.STATE = "Anti Ban: AFK";
        lastTimeAFKing = System.currentTimeMillis();
        Condition.sleep(Random.nextInt(60000, 120000));
    }

    /*
     * Opens up skill list and randomly selects a few skills to check the experience of. Once finished checking it goes back to your backpack.
     */
//    private void checkSkill() {
//        lastTimeCheckingSkill = System.currentTimeMillis();
//
//        ctx.hud.open(Window.SKILLS);
//
//        Condition.wait(new Callable<Boolean>() {
//            @Override
//            public Boolean call() throws Exception {
//                return ctx.hud.opened(Window.SKILLS);
//            }
//        }, 1000, 4);
//
//        final int skillId = Constants.SKILLS_RUNECRAFTING;
//
//        final Component skillsWindowComponent = ctx.widgets.component(1466, 11);
//        final Component randomSkillComponent = skillsWindowComponent.component(skillId);
//
//        ctx.input.move(randomSkillComponent.nextPoint());
//        Condition.sleep(Random.nextInt(1500, 2000));
//
//        backToBackpack();
//    }

    private void moveMouse() {
        ctx.input.move(Random.nextInt(100, 400), Random.nextInt(100, 400));
    }

    public void mouseOffScreen() {
        ctx.input.move(getOffScreenX(), getOffScreenY());
    }

    /*
     * Sets the HUD to backpack and moves the mouse away from the backpack
     */
    private void backToBackpack() {
        ctx.hud.open(Window.BACKPACK);
        Condition.sleep();
        moveMouse();
    }

    private int getOffScreenX() {
        if (Random.nextBoolean()) {
            final int width = ctx.client().getCanvas().getWidth();
            return width + Random.nextInt(20, 50);
        } else {
            return 0 - Random.nextInt(20, 50);
        }
    }

    private int getOffScreenY() {
        if (Random.nextBoolean()) {
            final int height = ctx.client().getCanvas().getHeight();
            return height + Random.nextInt(20, 50);
        } else {
            return 0 - Random.nextInt(20, 50);
        }
    }

}