package com.bham.bc.components.triggers.powerups;

import com.bham.bc.components.environment.Obstacle;
import com.bham.bc.entity.ai.navigation.ItemType;
import com.bham.bc.utils.Constants;
import com.bham.bc.utils.messaging.Telegram;
import com.bham.bc.components.triggers.RespawnTrigger;
import com.bham.bc.components.characters.GameCharacter;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.shape.Shape;

import static com.bham.bc.utils.Constants.FRAME_RATE;

public class SpeedTrigger extends RespawnTrigger {
    public static int width = Constants.TILE_WIDTH;
    public static int length = Constants.TILE_WIDTH;

    private int speed;


    public SpeedTrigger(int x, int y, int respawnCooldown, int speed) {
        super(x,y);
        this.speed = speed;
        addRectangularTriggerRegion(new Point2D(x,y), new Point2D(width,length));
        setRespawnDelay(respawnCooldown*FRAME_RATE);
    }


    @Override
    protected Image[] getDefaultImage() {
        return new Image[]{new Image("file:src/main/resources/img/tiles/triggers/flash.png")};
    }



    @Override
    public Shape getHitBox() {
        return null;
    }

    @Override
    public void render(GraphicsContext gc) {
        if (isActive()) {
            gc.drawImage(entityImages[0], this.x, this.y);
            renderRegion(gc);
        }

    }

    @Override
    public boolean handleMessage(Telegram msg) {
        return false;
    }

    @Override
    public String toString() {
        return null;
    }


    /**
     * If the speedBuff triggered, the tank will change its speed to new state
     * @return
     */
    @Override
    public void tryTriggerC(GameCharacter gameCharacter) {
        if(isActive()&& rectIsTouchingTrigger(gameCharacter.getPosition(),gameCharacter.getRadius())){
            //gameCharacter.speedUp(speed);
            deactivate();
        }

    }



    @Override
    public void tryTriggerO(Obstacle entity) {

    }

    @Override
    public ItemType getItemType() {
        return null;
    }
}
