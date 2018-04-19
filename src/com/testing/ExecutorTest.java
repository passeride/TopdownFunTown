package com.testing;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorTest{

    public static void Test(){
        ExecutorService executorService = Executors.newFixedThreadPool(100);

        executorService.execute(() -> {
            for(int i = 0;  i < 10; i ++){
                System.out.println("HEY " + i);
            }
        });

        executorService.shutdown();

    }

    public static void main(String[] args){
        ExecutorTest.Test();
    }
}