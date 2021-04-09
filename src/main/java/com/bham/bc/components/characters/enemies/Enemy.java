package com.bham.bc.components.characters.enemies;

import com.bham.bc.components.shooting.BulletType;
import com.bham.bc.components.shooting.Gun;
import com.bham.bc.components.characters.Side;
import com.bham.bc.entity.ai.navigation.ItemType;
import com.bham.bc.entity.ai.navigation.NavigationService;
import com.bham.bc.entity.ai.navigation.SearchStatus;
import com.bham.bc.entity.ai.navigation.impl.PathEdge;
import com.bham.bc.entity.ai.navigation.impl.PathPlanner;
import com.bham.bc.entity.ai.behavior.StateMachine;
import com.bham.bc.entity.graph.edge.GraphEdge;
import com.bham.bc.utils.GeometryEnhanced;
import com.bham.bc.utils.messaging.Telegram;
import com.bham.bc.components.characters.GameCharacter;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

import java.util.LinkedList;
import java.util.List;

import static com.bham.bc.components.CenterController.backendServices;

/**
 * <h1>Generic enemy bot</h1>
 *
 * <p>This class defines all the common behavior an enemy character can show. All the method calls are
 * determined by the state machine each enemy has.</p>
 */
public abstract class Enemy extends GameCharacter {
    protected NavigationService navigationService;
    protected LinkedList<PathEdge> pathEdges;
    protected int edgeBehavior;
    private Point2D destination;
    private int timeTillSearch;

    protected final Gun GUN;

    /**
     * Constructs a character instance with directionSet initialized to empty
     *
     * @param x     top left x coordinate of the enemy
     * @param y     top left y coordinate of the enemy
     * @param speed value which defines the initial velocity
     * @param hp    health points the enemy should have
     */
    protected Enemy(double x, double y, double speed, double hp) {
        super(x, y, speed, hp, Side.ENEMY);
        navigationService = new PathPlanner(this, backendServices.getGraph());
        pathEdges = new LinkedList<>();
        destination = new Point2D(0, 0);
        timeTillSearch = 20;
        edgeBehavior = GraphEdge.normal;
        GUN = new Gun(this, BulletType.DEFAULT);
    }


    /**
     * Finds path to a specified item type if it has not already been found
     * @param itemType type of item that should be navigated
     */
    public void navigate(ItemType itemType) {
        // If we don't have any points to follow, we need to navigate (it may return 0 edges if it's close)
        // Otherwise, if we have points to follow, we still need to update the search if the target is dynamic
        if(pathEdges.isEmpty()) {
            if (navigationService.peekRequestStatus() != SearchStatus.search_incomplete) {
                switch (itemType) {
                    case HEALTH:
                        navigationService.createRequest(ItemType.HEALTH);
                        break;
                    case HOME:
                        navigationService.createRequest(backendServices.getClosestCenter(getCenterPosition(), ItemType.HOME));
                        break;
                    case ENEMY_AREA:
                        navigationService.createRequest(backendServices.getClosestCenter(getCenterPosition(), ItemType.ENEMY_AREA));
                        break;
                    case ALLY:
                        navigationService.createRequest(backendServices.getClosestCenter(getCenterPosition(), ItemType.ALLY));
                        break;
                }
            }
        } else if(itemType == ItemType.ALLY && (--timeTillSearch <= 0)) {
            navigationService.createRequest(backendServices.getClosestCenter(getCenterPosition(), ItemType.ALLY));
        }

        //-----test
        if(itemType == ItemType.ALLY && timeTillSearch % 5 == 0) System.out.println("Time left till new navigation request for ally: " + timeTillSearch);
        //---------

        // Due to checks on each frame of whether the search is complete or not we always need get the list of points if it is empty
        // If the search status is completed, we fill our list with points to follow (if it is empty or we need to update the search for dynamic target)
        if((pathEdges.isEmpty() || (itemType == ItemType.ALLY && timeTillSearch <= 0)) && navigationService.peekRequestStatus() == SearchStatus.target_found) {
            pathEdges = navigationService.getPath();

            // If the target is very close we might have no path edges to follow
            // Otherwise if we have path edges, do not remove the last edge to keep the list not empty for proper search() functionality
            destination = pathEdges.isEmpty() ? getCenterPosition() : pathEdges.getFirst().getDestination();
            steering.setTarget(destination);

            timeTillSearch = 20;
            navigationService.resetTaskStatus();
        }
    }

    /**
     * Navigates to a specific item type (if needed) and moves towards that direction
     * @param itemType type of item that should be searched
     */
    public void search(ItemType itemType) {
        navigate(itemType);

        // Checks if the enemy intersects the point in the list of path edges and gets the next target point if so
        if(intersects(new Circle(destination.getX(), destination.getY(), 3))) {
            if(!pathEdges.isEmpty()) {
                PathEdge nextEdge = pathEdges.removeFirst();
                edgeBehavior = nextEdge.getBehavior();
                destination = nextEdge.getDestination();
                steering.setTarget(destination);
            }
        }

        // If the list of pathEdges is not empty we need to move forward
        if(!pathEdges.isEmpty()) {
            steering.seekOn();
            move();
        }
    }

    /**
     * Changes angle to align with a specified point
     * @param toward position to face
     */
    protected void face(Point2D toward) {
        Point2D direction = toward.subtract(getCenterPosition());
        if (!GeometryEnhanced.isZero(direction)){
            heading = direction.normalize();
        }
    }

    /**
     * Changes angle to face the nearest ally
     *
     * <p><b>Note:</b> if some ally target was followed and found but there is another ally standing closer but behind an
     * obstacle, this method will make the enemy aim at that ally. So free path condition must be checked. Alternatively,
     * in <i>getClosestCenter()</i> only those distances could be filtered if they don't intersect any obstacles.</p>
     */
    protected void aim() {
        face(backendServices.getClosestCenter(getCenterPosition(), ItemType.ALLY));
    }

    /**
     * Shoots the specified bullet(-s) at the current angle with a random probability
     * @param threshold value between 0 and 1 above which the shoot() method would be run
     */
    protected void shoot(double threshold) {
        if(Math.random() > threshold) GUN.shoot();
    }


    /**
     * AI behaviour to idly patrol around the map.
     * They will move a certain distance, then randomly switch direction
     */
    protected void patrol() {
        /*
        TODO: fix this method
        DIRECTION[] directions = DIRECTION.values();
        if (step == 0){
            direction = directions[r.nextInt(directions.length)];
            step = r.nextInt(10)+ 5;
        } else {
            step--;
            move();
        }
         */
    }

    /**
     * In this behaviour the AI tries to get closer to the player.
     * Once it has gotten close to the player it will try and shoot at the player
     * It will use a pathfinding algorithm to navigate to the player
     * This algorithm will not be used after each update as this could cause strain on the game
     * Instead every so often the tank will update it's location of the player, and pathfind to it
     */
    protected void approachPlayer() {
        //TODO
    }

    /**
     * In this behaviour the AI will try to obtain a power-up close to it
     * It will make use of pathfinding algorithm to obtain it.
     * Since the power-up is a stationary item, it will not need continually re-update the pathfinding
     */
    protected void findPowerup(){
        //TODO
    }

    /**
     * In this behaviour the AI will try to maximise it's distance away from the player rather than get close to it
     * It can do this by selecting a point on the map and pathfinding to that point.
     * Very rarely it could update with the players new position to alter it's path if need be
     */
    protected void flee(){
        //TODO
    }

    /**
     * In this behaviour the AI will pathfind to the home base and try to take it over
     */
    protected void takeOver() {
        //backendServices.occupyHome(-1);
        //TODO
    }

    /**
     * Abstract method which all child classes must fill with their own unique Finite State Machine
     * @return The StateMachine for that specific enemy
     */
    protected abstract StateMachine createFSM();

    @Override
    public void render(GraphicsContext gc) {
        if (navigationService!=null) navigationService.render(gc);
        drawRotatedImage(gc, entityImages[0], getAngle());

        gc.setStroke(Color.GOLD);
        gc.setLineWidth(2.0);

        gc.strokeLine(x, y, x+velocity.getX()*10, y+velocity.getY()*10);

        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2.0);

        gc.strokeLine(x, y, x+acceleration.getX()*10,x+acceleration.getY()*10 );

        gc.setFill(Color.WHITE);
        gc.fillRoundRect(destination.getX(),destination.getY(),4,4,1,1);
    }

    public List<Shape> getSmoothingBoxes(){
        return navigationService.getSmoothingBoxes();
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        switch (msg.Msg.id){
            case 0:
                System.out.println("you are deafeated by id "+ msg.Sender);
                return true;
            case 3:
                System.out.println("chang the direction");
                return true;
            default:
                System.out.println("no match");
                return false;
        }
    }

    @Override
    public String toString() {
        return "Enemy";
    }
}
