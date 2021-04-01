package com.bham.bc.entity;

import com.bham.bc.components.characters.Steering;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.transform.Rotate;

/**
 * Represents any entity that can move in any angle
 */
public abstract class MovingEntity extends BaseGameEntity {
    protected double speed;
    //could be zero
    protected Point2D velocity;
    //non-zero, normalized vector for direction, must be updated once the velocity was updated
    protected Point2D heading;

    public double getAngle() {
        return Math.toDegrees(Math.atan2(heading.getY(), heading.getX())) + 90;
    }

    public void setAngle(double angle) {
        this.angle = angle;
        this.heading = new Point2D(Math.sin(Math.toRadians(angle)),Math.cos(Math.toRadians(angle)));
    }

    protected double angle;
    protected boolean exists;



    /**
     * Constructs a single moving entity by default facing up (angle is set to 0) and generates a new valid ID for it
     *
     * @param x top left x coordinate of the entity and its image
     * @param y top left y coordinate of the entity and its image
     * @param speed value which defines the initial velocity
     */
    protected MovingEntity(double x, double y, double speed) {
        super(GetNextValidID(), x, y);
        this.speed = speed;
        this.velocity = new Point2D(0,0);
        this.heading = new Point2D(0,-1);
        setAngle(0);
        exists = true;
    }

    protected MovingEntity(double x, double y, double speed, double angle) {
        super(GetNextValidID(), x, y);
        this.speed = speed;

        //        x += Math.sin(Math.toRadians(angle));
//        y -= Math.cos(Math.toRadians(angle));
        this.velocity = new Point2D(0,0);
        this.heading = new Point2D(Math.sin(Math.toRadians(angle)),Math.cos(Math.toRadians(angle)));
        setAngle(angle);
        exists = true;
    }

    /**
     * Draws an image on a graphics context
     *
     * <p>The image is drawn at (x, y) rotated by angle pivoted around the point (centerX, centerY).
     * It uses Rotate class form JavaFX which applies rotation using transform matrix.</p>
     *
     * @param gc graphics context the image is to be drawn on
     * @param angle rotation angle
     *
     * @see <a href="https://stackoverflow.com/questions/18260421/how-to-draw-image-rotated-on-javafx-canvas">stackoverflow.com</a>
     * @see <a href="https://docs.oracle.com/javase/8/javafx/api/javafx/scene/transform/Rotate.html">docs.oracle.com</a>
     */
    protected void drawRotatedImage(GraphicsContext gc, Image image, double angle) {
        gc.save();
        Rotate r = new Rotate(angle, x + image.getWidth() / 2, y + image.getHeight() / 2);
        gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
        gc.drawImage(image, x, y);
        gc.restore();
    }

    /**
     * Checks if this entity exists
     * @return true if it exists and false otherwise
     */
    public boolean exists() { return exists; }

    /**
     * Defines how the position of the entity is updated on each frame
     */
    public abstract void move();

    public double getMaxSpeed() {
        return speed;
    }

    public Point2D getVelocity() {
        return velocity;
    }

    public double getMaxForce() {
        return 0;
    }

    public double getSpeed() {
//        return m_vVelocity.Length();
        return velocity.magnitude();
    }

    public Point2D getHeading() {
        return heading;
    }
}
