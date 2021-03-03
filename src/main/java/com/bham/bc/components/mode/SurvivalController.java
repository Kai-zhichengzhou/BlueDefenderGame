package com.bham.bc.components.mode;

import com.bham.bc.components.CenterController;
import com.bham.bc.components.armory.Bullet;
import com.bham.bc.components.characters.HomeTank;
import com.bham.bc.components.characters.enemies.Enemy;
import com.bham.bc.components.environment.GameMap;
import com.bham.bc.entity.Direction;
import com.bham.bc.entity.physics.BombTank;
import javafx.scene.canvas.GraphicsContext;

public class SurvivalController extends CenterController {
    /** Initialize an Object Of GameMap*/
    private GameMap gameMap;

    public SurvivalController(){
        super();
        gameMap = new GameMap();
        homeTank = new HomeTank(250, 560, Direction.STOP);
        initEnemies();
    }

    /**
     * A method to generate certain number of Enemy Tanks
     * Adding created Objects into enemyTanks(list)
     */
    private void initEnemies() {
        for (int i = 0; i < 20; i++) {
            if (i < 9){
                enemyTanks.add(new Enemy(150 + 70 * i, 40,  Direction.D));
            } else if (i < 15){
                enemyTanks.add(new Enemy(700, 140 + 50 * (i - 6), Direction.D));
            } else{
                enemyTanks.add(new Enemy(10, 50 * (i - 12),  Direction.D));
            }
        }
    }

    /**
     /** Overriding Method to indicates Game Logic \
     */

    @Override
    public void update() {

        //move-----------------
        homeTank.update();
        for (Enemy e : enemyTanks) {
            e.update();
        }
        //move-----------------

        //map----------------------------------
        gameMap.update(homeTank);
        for (Enemy e : enemyTanks) {
            gameMap.update(e);
        }
        //map----------------------------------

        //tanks----------------------------------
        homeTank.collideWithTanks(enemyTanks);
        for (Enemy e : enemyTanks) {
            e.collideWithTanks(enemyTanks);
        }
        //tanks----------------------------------
        /**
         * Use nested For Loop to update game state
         * Keep Tracking Bullets to see if Bullets hit Bullets, and Updates game state (Inner Loop)
         * Keep Tracking Bullets to see(Outer Loop):
         * 1.If Bullets hit enemy Tanks(Updates game state)
         * 2.If Bullets hit Player (Updates game state)
         * 3.If Bullets hits environment Objects e.g Home and Wall (Updates game state)
         */
        for (int i = 0; i < bullets.size(); i++) {
            Bullet m = bullets.get(i);
            m.update();
            m.hitTanks(enemyTanks);
            m.hitTank(homeTank);
            for(int j=0;j<bullets.size();j++){
                if (i==j) continue;
                Bullet bts=bullets.get(j);
                m.hitBullet(bts);
            }
            gameMap.update(m);
        }
        gameMap.updateObstacles();
    }

    @Override
    public void render(GraphicsContext gc) {
        /**
         *  The order of Render does matter
         *  The latter render will cover the previous render
         *  For example,rending Tree at the end leads to successfully Shading
         */

        for (int i = 0; i < bullets.size(); i++) {
            Bullet t = bullets.get(i);
            t.render(gc);
        }

        //the blood bar is here. But it's covered currently
        homeTank.render(gc);
        for (int i = 0; i < enemyTanks.size(); i++) {
            Enemy t = enemyTanks.get(i);
            t.render(gc);
        }
        for (int i = 0; i < bombTanks.size(); i++) {
            BombTank bt = bombTanks.get(i);
            bt.render(gc);
        }
        gameMap.renderAll(gc);


    }

    /**
     * Method to determine if player wins the game
     * @return
     */
    @Override
    public boolean isWin(){
        return enemyTanks.isEmpty() && gameMap.isHomeLive() && homeTank.isLive();
    }

    /**
     * Status to indicates defeat
     * Home destroyed OR Player Tank dead
     * @return
     */
    @Override
    public boolean isLoss(){
        return !gameMap.isHomeLive() || !homeTank.isLive();
    }

    /**
     * Clear all objects on tht map
     */
    public void clear(){
        super.clear();
        gameMap.clearAll();
    }
}
