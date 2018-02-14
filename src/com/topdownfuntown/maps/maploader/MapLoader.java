package com.topdownfuntown.maps.maploader;


import com.google.gson.Gson;

import java.io.*;
import java.nio.file.Files;

public class MapLoader {

    public static void loadMapJson(String name) throws FileNotFoundException {
        File f = new File("./assets/maps/" + name + ".json");
        String s = "NULL";
        try {
            s = new String(Files.readAllBytes(f.toPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Gson gs = new Gson();
        MapDOA stuff = gs.fromJson(s, MapDOA.class);
        System.out.println(stuff.Name);
        System.out.println(stuff.Enemies.get(2).Type);



    }

    public static void main(String[] args){
        try {
            loadMapJson("Default");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}