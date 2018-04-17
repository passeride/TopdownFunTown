package com.rominntrenger.main.objects.enemy;


import com.bluebook.engine.GameApplication;
import com.bluebook.graphics.AnimationSprite;
import com.bluebook.util.Vector2;
import com.rominntrenger.main.RomInntrenger;
import com.rominntrenger.main.objects.Explotion;

import java.util.Random;

public class AlienExplode extends Enemy {
    private double shootInterval = 1.8;
    private long prevShot = 0;

    public AlienExplode(Vector2 position) {
        super(position, Vector2.ZERO, new AnimationSprite("/enemies/alienExplode",6));
        Random r = new Random();
        prevShot = System.currentTimeMillis() + r.nextInt((int) (shootInterval * 1000));
       // setTarget(((Topdownfuntown)GameApplication.getInstance()).getPlayer());
        speed = 500;
    }


    @Override
    public void destroy() {
        new Explotion(getPosition());
        super.destroy();
    }

    public void update(double delta) {
        super.update(delta);
        setTarget(((RomInntrenger)GameApplication.getInstance()).player);
    }
}
