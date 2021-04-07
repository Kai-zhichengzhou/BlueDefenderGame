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

public class ArmorTrigger extends  RespawnTrigger{
    public static int width = Constants.TILE_WIDTH;
    public static int height = Constants.TILE_HEIGHT;

    private int HP;

    public ArmorTrigger(int x, int y, int HP, int Respawn){
        super(x,y);
        this.HP = HP;
        addRectangularTriggerRegion(new Point2D(x,y), new Point2D(width,height));
        setRespawnDelay(Respawn*FRAME_RATE);
    }


    @Override
    protected Image[] getDefaultImage() {
        return new Image[]{ new Image("file:src/main/resources/img/tiles/triggers/armor.png")};
    }

    @Override
    public void tryTriggerC(GameCharacter entity) {
        if(isActive()&&rectIsTouchingTrigger(entity.getPosition(), entity.getRadius())){
            //entity.armorUP(HP);
            deactivate();
        }

    }




    @Override
    public Shape getHitBox() {
        return null;
    }

    @Override
    public void render(GraphicsContext gc) {
        if(isActive()){
            gc.drawImage(entityImages[0],this.x,this.y);
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



    @Override
    public void tryTriggerO(Obstacle entity) {

    }

    @Override
    public ItemType getItemType() {
        return null;
    }
}
