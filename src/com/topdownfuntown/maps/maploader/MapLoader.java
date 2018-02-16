package com.topdownfuntown.maps.maploader;


import com.bluebook.audio.AudioLoader;
import com.bluebook.audio.AudioPlayer;
import com.bluebook.graphics.Sprite;
import com.bluebook.graphics.SpriteLoader;
import com.bluebook.util.Vector2;
import com.google.gson.Gson;
import com.topdownfuntown.maps.GameMap;

import java.io.*;
import java.nio.file.Files;

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



        return ret;
    }
}
