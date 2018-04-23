package com.rominntrenger.stateHandling;

import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

public class StateHandling {

        public static void save(String path)throws Exception{
            int score = 100;
            String[] items;

            File f = new File(path);
            System.out.println(f.exists());
            if(f.exists() && !f.isDirectory()){
                /* filen eksisterer. overwrite */
                try(ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get(String.valueOf(f))))){
                    oos.writeObject("\r" +new Date() + ": Score = " + score);
                    oos.close();
                    System.out.println("fantes. skrev over");
                }
            }else{

                System.out.println("HELLO");
                /* eksisterte IKKE, lag ny fil. */

                boolean fileCheck = f.createNewFile();
                if(fileCheck) System.out.println("fil lagd");
                else System.out.println("kunne ikke lage fil");
                try(ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get(String.valueOf(f))))){
                    oos.writeObject("Score: " + score);
                    oos.close();
                }
            }
        }

        public static Object load(String filename) throws Exception{
            try(ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Paths.get(filename)))){
                return ois.readObject();
            }
        }


}
