package com.rominntrenger;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

public class Randomizer<E> {
    private NavigableMap<Integer, E> map = new TreeMap<>();
    private Random random;
    private int total = 0;

    public Randomizer() {
        this(new Random());
    }

    public Randomizer(Random random) {
        this.random = random;
    }

    /**
     * Adds a specific GameObject {@Link:GameObject} with a weight value.
     * Adds the weight to the total weight of the list.
     * @param weight
     * @param object
     */
    public void addElement(int weight, E object) {
        if (weight <= 0) return;
        total += weight;
        map.put(total, object);
    }

    /**
     * Returns a random object from the list. Will be more likely to
     * return an object of a higher weight.
     * @return
     */
    public E randomElement() {
        int value = random.nextInt(total) + 1;
        return map.ceilingEntry(value).getValue();
    }

}