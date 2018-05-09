package com.rominntrenger.stateHandling.DAO;

import com.rominntrenger.objects.WaveManager;
import com.rominntrenger.objects.WaveManager.WaveSate;

import java.io.Serializable;

public class WaveManagerDAO implements Serializable {

    public int waveNumber;

    public WaveManagerDAO(WaveManager wm) {
        if (wm.getState() != WaveSate.PAUSE)
            waveNumber = wm.getWaveNumber() - 1;
    }
}
