package com.rominntrenger.objects.weapon;

import java.io.Serializable;

public class WeaponBase implements Serializable {

    public char character = 'N';

    public double dmgMultiplier = 1.2;
    public double shootInterval = 0.3;
    public double speedMultiplier = 0.7;
    public double angularDampeningMultiplier = 0.5;

}
