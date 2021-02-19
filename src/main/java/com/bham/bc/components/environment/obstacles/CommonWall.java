package com.bham.bc.components.environment.obstacles;

import com.bham.bc.entity.BaseGameEntity;
import com.bham.bc.utils.messaging.Telegram;
import com.bham.bc.components.environment.MapObject2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

public class CommonWall extends MapObject2D {
	/**
	 * Size OF CommonWall (Common Wall can be destroyed )
	 */
	public static int width = 22;
	public static int length = 21;

	public CommonWall(int x, int y) {
		super(x,y);
		initImages();
	}

	private void initImages() {
		entityImages = new Image[] {new Image("file:src/main/resources/img/commonWall.gif"), };
	}

	@Override
	public void render(GraphicsContext gc) {
		gc.drawImage(entityImages[0], x, y);
	}

	@Override
	public Rectangle getHitBox() {
		return new Rectangle(x, y, width, length);
	}

	@Override
	public void update() {

	}

	@Override
	public boolean handleMessage(Telegram msg) {
		return false;
	}

	@Override
	public boolean isIntersect(BaseGameEntity b) {
		Rectangle t = new Rectangle(x, y, width, length);
		return t.intersects(b.getHitBox().getBoundsInLocal());
	}
}
