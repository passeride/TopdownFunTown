package com.rominntrenger.objects.enemy;

import com.bluebook.graphics.Sprite;
import com.bluebook.physics.Collider;
import com.bluebook.physics.RayCast;
import com.bluebook.physics.RayCastHit;
import com.bluebook.physics.listeners.OnCollisionListener;
import com.bluebook.util.GameObject;
import com.bluebook.util.Vec2;
import com.rominntrenger.objects.Explotion;
import com.rominntrenger.objects.Projectile;
import com.rominntrenger.objects.player.Player;
import java.util.ArrayList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;

public class AlienTurret extends Enemy {

    private int rayCastResolution = 90;
    private ArrayList<RayCast> raycasts = new ArrayList<>();
    private Sprite turretHead;

    private double osscilation = 2.5;
    private boolean angleGrowing = true;

    private double timeTest = 0.0;
    private double progress = 0.0;
    private int timesChanged = 0;

    private double angleRotate = 90;

    private boolean playerSeen = false;
    private boolean enemySeen = false;
    GameObject player;
    GameObject enemy;

    /**
     * Constructor for GameObject given position rotation and sprite
     */
    public AlienTurret(Vec2 position, Vec2 direction) {
        super(position, direction, new Sprite("enemies/turret"));
        turretHead = new Sprite("enemies/turret_arm_r");
        collider.setTag("Turret");
        setUpRayCast();
    }

    void debugDrawRays(GraphicsContext gc){
        gc.setStroke(Color.CYAN);
        for(RayCast r : raycasts){
            RayCastHit rch = r.getHit();
            if(rch !=  null)
                gc.strokeLine(r.getRay().x1, r.getRay().y1, r.getRay().x2, r.getHit().ray.y2);
        }
    }

    /**
     * Goes over {@link #raycasts} and returns a double array with positions first array is X
     * cooridnates second is Y coordinates
     *
     * @return [x/y][position]
     */
    private double[][] getPolygon() {
        double[][] ret = new double[2][];
        double[] xs = new double[raycasts.size() + 1];
        double[] ys = new double[raycasts.size() + 1];

        boolean playerNotSeen = true;
        boolean enemyNotSeen = true;

        for (int i = 0; i < raycasts.size(); i++) {
            RayCastHit rch = raycasts.get(i).getHit();
            if (rch != null) {
                if (rch.isHit) {
//                    if (rch.colliderHit.getTag() == "UnHittable"
//                        || rch.colliderHit.getTag() == "Walk") {
//                        player = rch.colliderHit.getGameObject();
//                        playerSeen = true;
//                        playerNotSeen = false;
//                    } else if (rch.colliderHit.getTag() == "Hittable") {
//                        enemy = rch.colliderHit.getGameObject();
//                        enemySeen = true;
//                        enemyNotSeen = false;
//                    }
                }
//                System.out.println(rch.ray.x2);
                xs[i] = rch.ray.x2;
                ys[i] = rch.ray.y2;
            }
        }
        Vec2 pos = transform.getGlobalPosition();
        xs[xs.length - 1] = pos.getX();
        ys[ys.length - 1] = pos.getY();
        ret[0] = xs;
        ret[1] = ys;

        if (playerNotSeen) {
            playerSeen = false;
        }

        if (enemyNotSeen) {
            enemySeen = false;
        }

        return ret;
    }

    private void setUpRayCast() {
        for (int i = 0; i < rayCastResolution; i++) {
            //double dir = (Math.PI / 2) + ((Math.PI * 2) * (progress + 1.0)) * ((double) i / rayCastResolution);
            double dir = (Math.PI / 2) + ((double) i / rayCastResolution);
            RayCast r = new RayCast(dir, this, 6000);
            r.addInteractionLayer("Block");
            r.addInteractionLayer("UnHittable");
            r.addInteractionLayer("Hittable");
            r.addInteractionLayer("Walk");

            raycasts.add(r);
        }
    }

    private void updateRayCast() {
        for (int i = 0; i < rayCastResolution; i++) {

            double dir =
                getDirection().getAngleInRadians() + (Math.PI * 1.25) + ((Math.PI / 2) * ((double) i
                    / rayCastResolution));
            RayCast r = raycasts.get(i);
            r.updatePosition();

            r.setAngle(dir);

        }
    }

    @Override
    public void destroy() {
        super.destroy();
        for(RayCast r : raycasts){
            r.destroy();
        }
        new Explotion(getPosition());
    }

    @Override
    public void update(double delta) {
        super.update(delta);
        updateRayCast();

        if (playerSeen) {
            setDirection(Vec2.Vector2FromAngleInDegrees(
                Vec2.getAngleBetweenInDegrees(getPosition(), player.getPosition()) + 90));
            shoot();
        } else if (enemySeen) {
            setDirection(Vec2.Vector2FromAngleInDegrees(
                Vec2.getAngleBetweenInDegrees(getPosition(), enemy.getPosition()) + 90));
            shoot();
        } else {
            timeTest += delta;
            progress = (timeTest % osscilation) / osscilation;
            if (timesChanged < (int) (timeTest / osscilation)) {
                angleGrowing = !angleGrowing;
            }
            if (!angleGrowing) {
                progress = 1 - progress;
            }
            setDirection(Vec2.Vector2FromAngleInDegrees(Math.sin(progress) * 90));

            timesChanged = (int) (timeTest / osscilation);
        }
    }

    @Override
    public void draw(GraphicsContext gc) {

        double[][] polygon = getPolygon();

        gc.save();

        //gc.applyEffect(new ColorAdjust(0, 0, -0.8, 0));

        if (playerSeen) {
            gc.setFill(new RadialGradient(0, 0, 0.5, 0.5, 0., true,
                CycleMethod.NO_CYCLE,
                new Stop(0.0, new Color(1, 0, 0, 0.3)),
                new Stop(1.0, Color.TRANSPARENT)));
        } else if (enemySeen) {
            gc.setFill(new RadialGradient(0, 0, 0.5, 0.5, 0., true,
                CycleMethod.NO_CYCLE,
                new Stop(0.0, new Color(0.5, 0.5, 1, 0.6)),
                new Stop(1.0, Color.TRANSPARENT)));
        } else {
            gc.setFill(new RadialGradient(0, 0, 0.5, 0.5, 0., true,
                CycleMethod.NO_CYCLE,
                new Stop(0.0, new Color(0.5, 1, 0.5, 0.6)),
                new Stop(1.0, Color.TRANSPARENT)));
        }


        gc.restore();

        sprite.draw(gc, getPosition());
        sprite.rotate(Vec2.ZERO);
        turretHead.rotate(getDirection());
        turretHead.draw(gc, transform.getGlobalPosition());

        debugDrawRays(gc);
    }

    public void shoot() {
        Projectile p = new Projectile(Vec2
            .rotateVectorAroundPoint(Vec2.add(getPosition(), new Vec2(0, 80)), getPosition(),
                getDirection().getAngleInDegrees()),
            Vec2.Vector2FromAngleInDegrees(getDirection().getAngleInDegrees() - 90),
            new Sprite("/projectiles/projectile_c_00"));

        p.setPeriod(.2f);
        p.setAmplitude(8f);
        p.setPhase(2000f);
        p.setSpeed(800);
        p.setSine(true);

        // Adding colliders layers
        p.getCollider().addInteractionLayer("UnHittable");
        p.getCollider().addInteractionLayer("Hittable");
        p.getCollider().addInteractionLayer("Block");

        p.setOnCollisionListener(new OnCollisionListener() {
            @Override
            public void onCollision(Collider other) {
                if (other.getGameObject() instanceof Player) {
                    Player pl = (Player) other.getGameObject();
                    pl.hit(10);

                } else if (other.getGameObject() instanceof Enemy) {
                    other.getGameObject().destroy();
                }
                p.destroy();

            }
        });
    }
}
