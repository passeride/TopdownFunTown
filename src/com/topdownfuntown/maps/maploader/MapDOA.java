package com.topdownfuntown.maps.maploader;


import java.util.ArrayList;

public class MapDOA {

    /*
      "Name":"StartRoom",
              "isSafeRoom":false,
              "BackgroundImage":"bakgrunn",
              "SoundTrack":null,
*/
    public String Name;
    public boolean isSafeRoome;
    public String BackgroundImage;
    public String SoundTrack;

    public ArrayList<EnemyDOA> Enemies;
    public ArrayList<BlockDOA> Blocks;
}
