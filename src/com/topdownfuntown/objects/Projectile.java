package com.topdownfuntown.objects;

import com.bluebook.engine.GameApplication;
import com.bluebook.engine.GameEngine;
import com.bluebook.physics.Collider;
import com.bluebook.physics.listeners.OnCollisionListener;
import com.bluebook.util.GameObject;
import com.bluebook.graphics.Sprite;
import com.bluebook.util.GameSettings;
import com.bluebook.util.Vector2;
import javafx.scene.canvas.GraphicsContext;

public class Projectile extends GameObject{

    private double speed = 800.0;
    float period = 0.5f;
    float frequency;
    float angularFrequency;
    float elapsedTime = 0.0f;
    float amplitude = 15.0f;
    float phase = 500f;

    private Vector2 startPosition;
    boolean isSine = false;
    boolean isBouncy = true;



    /**
     * Constructor for GameObject given position rotation and sprite
     *
     * @param position
     * @param direction
     * @param sprite
     */
    public Projectile(Vector2 position, Vector2 direction, Sprite sprite) {
        super(position, direction, sprite);
        this.startPosition = position;
        size = new Vector2(100, 30);
        this.setCollider(new Collider(this));
        collider.setName("Bullet");
        collider.setTag("DMG");
    }

    public void setOnCollisionListener(OnCollisionListener listener){
        this.collider.setOnCollisionListener( listener);
    }

    @Override
    public void update(double delta){
        if(isSine)
            translate(Vector2.add(rotateVectorAroundPoint(SmoothSineWave(delta), direction.getAngleInDegrees()), Vector2.multiply(direction, speed * delta)));
        else
            translate(Vector2.multiply(direction, speed * delta));


        //position = sinVector(position,  t);
    }

    /**
     * Override to create a 8 % margin for movement
     * @param moveVector
     */
    @Override
    public void translate(Vector2 moveVector){
        Vector2 newValue = Vector2.add(position, moveVector);

        double screenWidth = GameApplication.getInstance().getScreenWidth();
        double screenHeihgt = GameApplication.getInstance().getScreenHeight();
        double boudMarginX = screenWidth * GameSettings.getDouble("map_movement_padding_X");
        double boudMarginY = screenHeihgt * GameSettings.getDouble("map_movement_padding_Y");


        if(newValue.getX() <= screenWidth - boudMarginX
                && newValue.getX() > boudMarginX
                && newValue.getY() <= screenHeihgt - boudMarginY
                && newValue.getY() > boudMarginY){
            position = newValue;
        }else{
            if(!isBouncy)
                destroy();
            else{
                if(newValue.getX() >= screenWidth - boudMarginX || newValue.getX() <= boudMarginX){
                    direction.setX(-direction.getX());
                }
                if(newValue.getY() >= screenHeihgt - boudMarginY || newValue.getY() <= boudMarginY){
                    direction.setY(-direction.getY());
                }
                position = newValue;
            }
        }
    }

    private Vector2 rotateVectorAroundPoint(Vector2 vec, double angle){
        double x = vec.getX() * Math.cos(angle) - vec.getY() * Math.sin(angle);
        double y = vec.getX() * Math.sin(angle) + vec.getY() * Math.cos(angle);

        return new Vector2(x, y);
    }

    private Vector2 SmoothSineWave (double deltaTime) {
        // y(t) = A * sin(ωt + θ) [Basic Sine Wave Equation]
        // [A = amplitude | ω = AngularFrequency ((2*PI)f) | f = 1/T | T = [period (s)] | θ = phase | t = elapsedTime]
        // Public/Serialized Variables: amplitude, period, phase
        // Private/Non-serialized Variables: frequency, angularFrequency, elapsedTime
        // Local Variables: omegaProduct, y

        // If the value of period has altered last known frequency...
        if (1 / (period) != frequency) {
            // Recalculate frequency & omega.
            frequency = 1 / (period);
            angularFrequency = (float)(2f * Math.PI) * frequency;
        }
        // Update elapsed time.
        elapsedTime += deltaTime;
        // Calculate new omega-time product.
        float omegaProduct = (angularFrequency * elapsedTime);
        // Plug in all calculated variables into the complete Sine wave equation.
        float y = (amplitude * (float)Math.sin (omegaProduct + phase));
        //
        return new Vector2 (0, y);
    }

    public double getLengthTraveled(){
        return startPosition.distance(position);
    }

    @Override
    public void setSize(Vector2 vec){
        this.size = vec;
        collider.updateRect();
    }

    @Override
    public void draw(GraphicsContext gc){

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
