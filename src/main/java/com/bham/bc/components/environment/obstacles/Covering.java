package com.bham.bc.components.environment.obstacles;

import com.bham.bc.components.environment.Obstacle;
import com.bham.bc.components.environment.Tileset;
import javafx.scene.image.Image;

import java.util.EnumSet;

/**
 * Tile which covers anything that passes through it
 */
public class Covering extends Obstacle {
    /**
     * Constructs an obstacle
     *
     * @param x       position in x axis
     * @param y       position in y axis
     * @param tileset type of tileset
     * @param tileIDs IDs of tiles in case the obstacle is animated
     */
    public Covering(int x, int y, Tileset tileset, int... tileIDs) {
        super(x, y, tileset, tileIDs);
    }

    @Override
    public EnumSet<Attribute> getAttributes() {
        return EnumSet.of(Attribute.RENDER_TOP, Attribute.WALKABLE);
    }

    @Override
    protected Image[] getDefaultImage() {
        return new Image[] { new Image("file:src/main/resources/img/tiles/DefaultTiles/tree.bmp") };
    }

//    @Override
//    public void interactWith(int ID, int indexOfNode , Rectangle r1) { }
}
