package com.rominntrenger.stateHandling.DAO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MetaDAO implements Serializable {

    public List<PlayerDAO> players = new ArrayList<>();
    public WaveManagerDAO waveManager;

}
