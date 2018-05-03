package com.rominntrenger;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

public class Randomizer<E> {
    private NavigableMap<Integer, E> map = new TreeMap<Integer, E>();
    private Random random;
    private int total = 0;

    public Randomizer() {
        this(new Random());
    }

    public Randomizer(Random random) {
        this.random = random;
    }

    public void addElement(int weight, E object) {
        if (weight <= 0) return;
        total += weight;
        map.put(total, object);
    }

    public E randomElement() {
        int value = random.nextInt(total) + 1;
        return map.ceilingEntry(value).getValue();
    }

}