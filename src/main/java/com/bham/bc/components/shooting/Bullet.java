package com.bham.bc.components.shooting;

import com.bham.bc.components.characters.GameCharacter;
import com.bham.bc.components.characters.Side;
import com.bham.bc.components.environment.Obstacle;
import com.bham.bc.components.environment.Attribute;
import com.bham.bc.entity.BaseGameEntity;
import com.bham.bc.utils.messaging.Telegram;
import com.bham.bc.entity.MovingEntity;
import javafx.scene.image.Image;
import javafx.geometry.Point2D;

import java.util.List;


/**
 * Represents any bullet and defines common bullet properties
 * <p> TODO: add constrains / asserts / throw exceptions in setters
 */
abstract public class Bullet extends MovingEntity {
    private final BulletType TYPE;
    private Side side;
    private double damage;

    /**
     * <p>Constructs a bullet using with geometrical properties to determine the position of a bullet on each frame.
     * SIDE property is used to indicate which team the bullet can damage. </p>
     *
     * @param x       top left position in x axis
     * @param y       top left position in y axis
     * @param speed   velocity value at which the bullet will move
     * @param heading a normalized vector indicating the direction the bullet is moving
     * @param type    bullet type (e.g., DEFAULT, EXPLOSIVE)
     * @param side    ALLY or ENEMY side the bullet belongs to
     * @param damage  amount of hp the bullet can take from an entity
     */
    public Bullet(double x, double y, double speed, Point2D heading, BulletType type, Side side, double damage) {
        super(x, y, speed, heading);
        this.TYPE = type;
        this.side = side;
        this.damage = damage;

        entityImages = new Image[] { TYPE.getImage() };
    }

    /**
     * Gets bullet's speed
     * @return velocity at which the bullet is moving
     */
    public double getMaxSpeed() { return maxSpeed; }

    /**
     * Gets bullet's type
     * @return DEFAULT of EXPLOSIVE type the bullet belongs to
     * TODO: add more types
     */
    public BulletType getType() {
        return TYPE;
    }

    /**
     * Gets bullet's side
     * @return ALLY or ENEMY side the bullet belongs to
     */
    public Side getSide() {
        return side;
    }

    /**
     * Gets bullet's damage
     * @return amount of damage the bullet deals
     */
    public double getDamage() {
        return damage;
    }

    /**
     * Sets bullet's speed
     * <br><b>Note:</b> the speed must be in range [1, 20]
     * <br>TODO: assert range
     */
    public void setSpeed(double speed) {
        this.maxSpeed = speed;
    }

    /**
     * Sets bullet's damage
     *
     * <br><b>Note:</b> the damage must be in range [-500, 500]
     * <br>TODO: assert range
     */
    public void setDamage(double damage) {
        this.damage = damage;
    }

    /**
     * Unregisters and prepares to remove the bullet. Also runs any destruction effects
     */
    public abstract void destroy();


    public double getHitboxRadius() {
        Point2D p1 = super.getRadius();
        double n1 = Math.sqrt(p1.getX()/2*p1.getX()/2 + p1.getY()/2*p1.getY()/2);
        if(n1<42.0) return 42.0;                // 20.0 means the mini check hitbox radius
        return n1;
    }

    @Override
    public boolean handleMessage(Telegram msg) { return false; }

    @Override
    public String toString() { return "Bullet"; }

    public void handle(List<BaseGameEntity> entities) {
        entities.forEach(this::handle);
    }

    public void handle(BaseGameEntity entity) {
        if(entity instanceof GameCharacter && intersects(entity) && getSide() != ((GameCharacter) entity).getSide() && ((GameCharacter) entity).getImmuneTicks() == 0) {
            ((GameCharacter) entity).changeHp(-damage);
            destroy();
        } else if(entity instanceof Obstacle && ((Obstacle) entity).getAttributes().contains(Attribute.WALL) && intersects(entity)) {
            destroy();
            if(((Obstacle) entity).getAttributes().contains(Attribute.BREAKABLE)) {
                ((Obstacle) entity).changeHp(-damage);
            }
        }
    }


}