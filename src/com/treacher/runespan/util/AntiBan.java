package com.treacher.runespan.util;

import com.treacher.runespan.Runespan;
import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt6.ClientContext;

/**
 * Created by Michael Treacher
 */

public class AntiBan {

    private long lastTimeAFKing = System.currentTimeMillis();
    private long lastTimeMovingMouse = System.currentTimeMillis();

    private final ClientContext ctx;

    public AntiBan(ClientContext ctx) {
        this.ctx = ctx;
    }

    public void execute() {
        if(timeForMouseMovement()) triggerMouseEvent();
        if(timeForAFK()) goAFK();
    }

    // AFK every 60 minutes
    private boolean timeForAFK() {
        return (System.currentTimeMillis() - lastTimeAFKing) > (Random.nextInt(50000,70000) * 60);
    }

    private boolean timeForMouseMovement() {
        return (System.currentTimeMillis() - lastTimeMovingMouse) > (Random.nextInt(50000,70000) * 2.5);
    }

    private void triggerMouseEvent() {
        Runespan.STATE = "Anti Ban: Moving Mouse";

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
        Runespan.STATE = "Anti Ban: AFK";
        lastTimeAFKing = System.currentTimeMillis();
        Condition.sleep(Random.nextInt(60000, 120000));
    }

    private void moveMouse() {
        ctx.input.move(Random.nextInt(100, 400), Random.nextInt(100, 400));
    }

    public void mouseOffScreen() {
        ctx.input.move(getOffScreenX(), getOffScreenY());
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