package com.topdownfuntown.objects;

import com.bluebook.graphics.Sprite;
import com.bluebook.physics.Collider;
import com.bluebook.physics.RayCast;
import com.bluebook.physics.RayCastHit;
import com.bluebook.physics.listeners.OnCollisionListener;
import com.bluebook.util.GameObject;
import com.bluebook.util.Vector2;
import com.sun.javafx.geom.Line2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;

import java.util.ArrayList;

public class Turret extends Enemy {

    private int rayCastResolution = 180;
    private ArrayList<RayCast> raycasts = new ArrayList<>();
    private Sprite turretHead;

    private double osscilation = 2.5;
    private boolean angleGrowing = true;

    private double timeTest = 0.0;
    private double progress = 0.0;
    private int timesChanged = 0;

    private double angleRotate = 90;

    private boolean playerSeen = false;
    GameObject player;

    /**
     * Constructor for GameObject given position rotation and sprite
     *
     * @param position
     * @param direction
     */
    public Turret(Vector2 position, Vector2 direction) {
        super(position, direction, new Sprite("enemies/turret"));
        turretHead = new Sprite("enemies/turret_arm_r");
        setUpRayCast();
    }

    /**
     * Goes over {@link #raycasts} and returns a double array with positions first array is X cooridnates
     * second is Y coordinates
     * @return [x/y][position]
     */
    private double[][] getPolygon(){
        double[][] ret = new double[2][];
        double[] xs = new double[raycasts.size() + 1];
        double[] ys = new double[raycasts.size() + 1];

        boolean playerNotSeen = true;

        for(int i = 0; i < raycasts.size(); i++){
            RayCastHit rch = raycasts.get(i).getHit();
            if(rch != null) {
                if (rch.isHit) {
                    if(rch.colliderHit.getTag() == "UnHittable"){
                        player = rch.colliderHit.getGameObject();
                        playerSeen = true;
                        playerNotSeen = false;
                    }
                    xs[i] = rch.ray.x2;
                    ys[i] = rch.ray.y2;
                } else {
                    xs[i] = rch.ray.x2;
                    ys[i] = rch.ray.y2;
                }
            }
        }
        xs[xs.length - 1] = position.getX();
        ys[ys.length - 1] = position.getY();
        ret[0] = xs;
        ret[1] = ys;

        if(playerNotSeen){
            playerSeen = false;
        }

        return ret;
    }

    private void setUpRayCast(){
        for(int i = 0; i < rayCastResolution; i++){
            //double dir = (Math.PI / 2) + ((Math.PI * 2) * (progress + 1.0)) * ((double) i / rayCastResolution);
            double dir = (Math.PI / 2) + ((double) i / rayCastResolution);
            RayCast r = new RayCast(dir, this, 600);
            r.addInteractionLayer("Block");
            r.addInteractionLayer("UnHittable");
            raycasts.add(r);
        }
    }

    private void updateRayCast(){
        for(int i = 0; i < rayCastResolution; i++){

            double dir = direction.getAngleInRadians() + (Math.PI * 1.25) + ((Math.PI / 2) * ((double) i / rayCastResolution));
            RayCast r = raycasts.get(i);
            r.setAngle(dir);

        }
    }


    @Override
    public void update(double detla) {
        super.update(detla);
        if(!playerSeen){
            timeTest += detla;
            progress = (timeTest % osscilation) / osscilation;
//        if((progress > .04 && progress > .96)) {
            if (timesChanged < (int) (timeTest / osscilation))
                angleGrowing = !angleGrowing;
            if (!angleGrowing) {
                progress = 1 - progress;
            }
            direction = Vector2.Vector2FromAngleInDegrees(Math.sin(progress) * 90);

            timesChanged = (int) (timeTest / osscilation);
            updateRayCast();
        }else{
            direction = Vector2.Vector2FromAngleInDegrees(Vector2.getAngleBetweenInDegrees(position, player.getPosition()) + 90);
            shoot();
        }
//        }
    }

    @Override
    public void draw(GraphicsContext gc) {

        double[][] polygon = getPolygon();

        gc.save();

        //gc.applyEffect(new ColorAdjust(0, 0, -0.8, 0));

        if(playerSeen) {
            gc.setFill(new RadialGradient(0, 0, 0.5, 0.5, 0., true,
                    CycleMethod.NO_CYCLE,
                    new Stop(0.0, new Color(1, 0, 0, 0.3)),
                    new Stop(1.0, Color.TRANSPARENT)));
        }else{
            gc.setFill(new RadialGradient(0, 0, 0.5, 0.5, 0., true,
                    CycleMethod.NO_CYCLE,
                    new Stop(0.0, new Color(0.5, 1, 0.5, 0.6)),
                    new Stop(1.0, Color.TRANSPARENT)));
        }
        gc.setGlobalBlendMode(BlendMode.OVERLAY);

        gc.fillPolygon(polygon[0], polygon[1], polygon[0].length);

        gc.restore();

        sprite.draw(gc, position);
        sprite.rotate(Vector2.ZERO);
        turretHead.rotate(direction);
        turretHead.draw(gc, position);
    }

    public void shoot() {
        Projectile p = new Projectile(position, Vector2.Vector2FromAngleInDegrees(direction.getAngleInDegrees() - 90), new Sprite("/projectiles/balltest"));
        p.setSize(new Vector2(32, 32));

        p.getSprite().setSquareHeight(32);
        p.getSprite().setSquareWidth(32);
        p.setPeriod(.2f);
        p.setAmplitude(0f);
        p.setPhase(2000f);
        p.setSpeed(800);
        p.setSine(true);

        // Adding colliders layers
        p.getCollider().addInteractionLayer("UnHittable");
        p.getCollider().addInteractionLayer("Block");

        p.setOnCollisionListener(new OnCollisionListener() {
            @Override
            public void onCollision(Collider other) {
                if (other.getGameObject() instanceof Player) {
                    Player pl = (Player) other.getGameObject();
                    pl.hit();

                }
                p.destroy();

            }
        });
    }
}
