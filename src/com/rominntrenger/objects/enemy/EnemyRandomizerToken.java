package com.rominntrenger.objects.enemy;

import com.bluebook.util.Vec2;

/**
 * Used with {@link com.rominntrenger.Randomizer} to represent different types of aliens
 */
public class EnemyRandomizerToken {

    private EnemyType entity;

    public enum EnemyType {
        ALIEN_GREEN, ALIEN_PURPLE, AlIEN_ZOMBIE, ALIEN_EXPLODE, ALIEN_EYE, ALIEN_GLOW, ALIEN_WORM
    }

    public EnemyRandomizerToken(EnemyType entity) {
        this.entity = entity;
    }

    /**
     * This function will create a new alien of type corresponding to {@link EnemyType}
     *
     * @param pos initial position for the enemy
     */
    public void spawn(Vec2 pos) {
        switch (entity) {
            case ALIEN_GREEN:
                new AlienGreen(pos);
                break;
            case ALIEN_PURPLE:
                new AlienPurple(pos);
                break;
            case AlIEN_ZOMBIE:
                new AlienPurple(pos);
                break;
            case ALIEN_EXPLODE:
                new AlienExplode(pos);
                break;
            case ALIEN_EYE:
                new AlienEye(pos);
                break;
            case ALIEN_GLOW:
                new AlienGlow(pos);
                break;
            case ALIEN_WORM:
                new AlienWorm(pos);
                break;
            default:
                break;
        }
    }

}
