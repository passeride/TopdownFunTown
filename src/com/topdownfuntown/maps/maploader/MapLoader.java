package com.topdownfuntown.maps.maploader;


import com.bluebook.audio.AudioLoader;
import com.bluebook.audio.AudioPlayer;
import com.bluebook.graphics.Sprite;
import com.bluebook.graphics.SpriteLoader;
import com.bluebook.util.Vector2;
import com.google.gson.Gson;
import com.topdownfuntown.maps.GameMap;
import com.topdownfuntown.objects.Crate;
import com.topdownfuntown.objects.Enemy;
import com.topdownfuntown.objects.GreenAlien;
import com.topdownfuntown.objects.PurpleAlien;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MapLoader {

    public static GameMap loadMapJson(String name){
        File f = new File("./assets/maps/" + name + ".json");
        String s = "NULL";
        try {
            s = new String(Files.readAllBytes(f.toPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Gson gs = new Gson();
        MapDOA stuff = gs.fromJson(s, MapDOA.class);

        return convertToGameMap(stuff);
    }

    private static GameMap convertToGameMap(MapDOA mapdoa){
        GameMap ret = new GameMap(new Sprite(mapdoa.BackgroundImage));
        ret.setName(mapdoa.Name);
        ret.setSafeRoom(mapdoa.isSafeRoome);
        if(mapdoa.SoundTrack != null)
            ret.setSoundTrack(new AudioPlayer(mapdoa.SoundTrack));

        // Enemies
        ret.addEnemyList(populateEnemies(mapdoa.Enemies));

        for(BlockDOA b : mapdoa.Blocks){
            ret.addObstale(new Crate(new Vector2(b.x_pos, b.y_pos)));
        }


        return ret;
    }

    private  static List<Enemy> populateEnemies(List<EnemyDOA> edoa){
        ArrayList<Enemy> ret = new ArrayList<>();
        for(EnemyDOA e : edoa){
            switch(e.Type){
                case "AlienGreen":
                    ret.add(new GreenAlien(new Vector2(e.x_pos, e.y_pos)));
                    break;
                case "AlienPurple":
                    ret.add(new PurpleAlien(new Vector2(e.x_pos, e.y_pos)));
                    break;
            }
        }

        if(ret.size() > 0) {
            Random r = new Random();
            int getNumber = r.nextInt(ret.size());
            System.out.println("Next stufa;sldkkfj  " + getNumber);
            ret.get(getNumber).isKeyHolder = true;
        }

        return ret;
    }
}
