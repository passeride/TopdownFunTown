package com.rominntrenger.main.objects;

import com.bluebook.graphics.Sprite;
import com.bluebook.physics.BoxCollider;
import com.bluebook.physics.CircleCollider;
import com.bluebook.physics.listeners.OnCollisionListener;
import com.bluebook.renderer.RenderLayer;
import com.bluebook.util.GameObject;
import com.bluebook.util.Vector2;
import java.util.ArrayList;
import javafx.scene.canvas.GraphicsContext;

/**
 * Projectile is a used by Enemy and Player to shoot stuff It has multiple paramter to change speed
 * and Possibly add a sine wave pattern to it
 */
public class Projectile extends GameObject {

    private double speed = 800.0;
    private double TTL = 1.2;
    float period = 0.5f;
    float frequency;
    float angularFrequency;
    float elapsedTime = 0.0f;
    float amplitude = 15.0f;
    float phase = 500f;

    private long startTime = 0;


    private static ArrayList<Projectile> allProjectilse = new ArrayList<>();

    private Vector2 startPosition;
    boolean isSine = false;
    boolean isBouncy = true;
    boolean isTimeDecay = true;

    double squareWithStart;
    double squareHeightStart;

    private Vector2 startSize = size;

    /**
     * Constructor for Projectile given position rotation and sprite
     */
    public Projectile(Vector2 position, Vector2 direction, Sprite sprite) {
        super(position, direction, sprite);
        allProjectilse.add(this);
        this.startPosition = transform.getGlobalPosition();
        this.setCollider(new CircleCollider(this, 30));
        setRenderLayer(RenderLayer.RenderLayerName.HIGH_BLOCKS);
        collider.setName("Bullet");
        collider.setTag("DMG");
        startTime = System.currentTimeMillis();
        startSize = new Vector2(1, 1);
        squareHeightStart = 1;
        squareWithStart = 1;
    }

    public static void clearAllProjectiles() {
        for (Projectile p : allProjectilse) {
            p.destroy();
        }
        allProjectilse.clear();
    }

    public void setOnCollisionListener(OnCollisionListener listener) {
        this.collider.setOnCollisionListener(listener);
    }

    @Override
    public void update(double delta) {
        if (isSine) {
            translate(Vector2.add(Vector2
                    .rotateVectorAroundPoint(SmoothSineWave(delta), Vector2.ZERO,
                        getDirection().getAngleInDegrees()),
                Vector2.multiply(getDirection(), speed * delta)));
        } else {
            translate(Vector2.multiply(getDirection(), speed * delta));
        }

        if (isTimeDecay) {

            double elapseInSeconds = (System.currentTimeMillis() - startTime) / 1000.0;
            double TTLAdjusted = TTL * 1.2;
            double elapseProgress = (TTLAdjusted - elapseInSeconds) / TTLAdjusted;

            if (elapseProgress > 0.1) {
                elapseProgress = elapseProgress / 2 + 0.5;
                transform.setLocalScale(Vector2.multiply(startSize, elapseProgress));
            } else {
                destroy();
            }
        }
    }

    double lerp(double point1, double point2, double alpha) {
        return point1 + alpha * (point2 - point1);
    }


    @Override
    public void translate(Vector2 moveVector) {
        setPosition(Vector2.add(getPosition(), moveVector));
    }

    private Vector2 SmoothSineWave(double deltaTime) {
        // y(t) = A * sin(ωt + θ) [Basic Sine Wave Equation]
        // [A = amplitude | ω = AngularFrequency ((2*PI)f) | f = 1/T | T = [period (s)] | θ = phase | t = elapsedTime]
        // Public/Serialized Variables: amplitude, period, phase
        // Private/Non-serialized Variables: frequency, angularFrequency, elapsedTime
        // Local Variables: omegaProduct, y

        // If the value of period has altered last known frequency...
        if (1 / (period) != frequency) {
            // Recalculate frequency & omega.
            frequency = 1 / (period);
            angularFrequency = (float) (2f * Math.PI) * frequency;
        }
        // Update elapsed time.
        elapsedTime += deltaTime;
        // Calculate new omega-time product.
        float omegaProduct = (angularFrequency * elapsedTime);
        // Plug in all calculated variables into the complete Sine wave equation.
        float y = (amplitude * (float) Math.sin(omegaProduct + phase));
        //
        return new Vector2(0, y);
    }

    public double getLengthTraveled() {
        return startPosition.distance(position);
    }

    @Override
    public void setSize(Vector2 vec) {
        super.setSize(vec);
        if (collider != null && collider instanceof BoxCollider) {
            ((BoxCollider) collider).updateRect();
        }
    }

    @Override
    public void draw(GraphicsContext gc) {

        sprite.draw(gc, position, direction);

    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public float getPeriod() {
        return period;
    }

    public void setPeriod(float period) {
        this.period = period;
    }

    public float getAmplitude() {
        return amplitude;
    }

    public void setAmplitude(float amplitude) {
        this.amplitude = amplitude;
    }

    public float getPhase() {
        return phase;
    }

    public void setPhase(float phase) {
        this.phase = phase;
    }

    public boolean isSine() {
        return isSine;
    }

    public void setSine(boolean sine) {
        isSine = sine;
    }
}