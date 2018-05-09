package com.rominntrenger.objects.weapon;

import java.io.Serializable;

public class WeaponClip implements Serializable {

    public char character = 'S';
    public int ammoCap = 20;
    public int dmg = 20;
    public double heatSeeking = 0.0;
    public double reloadTime = 1.2;

}
