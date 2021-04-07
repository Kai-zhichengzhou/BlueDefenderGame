/**
 * Desc: If a bot runs over an instance of this class its health will never decrease for few seconds only.
 */
package com.bham.bc.components.triggers.powerups;

import com.bham.bc.components.environment.Obstacle;
import com.bham.bc.entity.ai.navigation.ItemType;
import com.bham.bc.entity.BaseGameEntity;
import com.bham.bc.utils.Constants;
import com.bham.bc.components.triggers.RespawnTrigger;
import com.bham.bc.components.characters.GameCharacter;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import static com.bham.bc.utils.Constants.FRAME_RATE;

public class Immune extends RespawnTrigger {

    public static int width = Constants.TILE_WIDTH;
    public static int length = Constants.TILE_WIDTH;

	private int activationTime;
	
    public Immune(int x,int y, int activationTime, int respawnCooldown) {

        super(x, y);
        this.activationTime = activationTime;

        //create this trigger's region of fluence
        addRectangularTriggerRegion(new Point2D(x, y), new Point2D(width, length));
        setRespawnDelay(respawnCooldown * FRAME_RATE);
    }

    protected Image[] getDefaultImage() {
        return new Image[] {new Image("file:src/main/resources/img/tiles/triggers/green_heart.png"), };
    }

    //if triggered, the bot's health will be never decrease for few seconds only
    @Override
    public void tryTriggerC(GameCharacter gameCharacter) {
        if (isActive() && rectIsTouchingTrigger(gameCharacter.getPosition(), gameCharacter.getRadius())) {
            gameCharacter.toImmune(activationTime * FRAME_RATE);
            deactivate();
        }
    }
    
    @Override
    public void tryTriggerO(Obstacle entity) {

    }

    //draws a box with a red cross at the trigger's location
    @Override
    public void render(GraphicsContext gc) {
        if (isActive()) {
            gc.drawImage(entityImages[0], this.x, this.y);
        }
    }

    @Override
    public ItemType getItemType() {
        return null;
    }
}