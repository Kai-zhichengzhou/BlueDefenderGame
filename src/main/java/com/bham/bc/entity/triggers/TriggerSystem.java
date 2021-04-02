/**
 * Desc: Class to manage a collection of triggers. Triggers may be registered
 * with an instance of this class. The instance then takes care of updating
 * those triggers and of removing them from the system if their lifetime has
 * expired.
 */
package com.bham.bc.entity.triggers;


import com.bham.bc.components.characters.GameCharacter;
import com.bham.bc.components.characters.Player;
import com.bham.bc.components.environment.GenericObstacle;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TriggerSystem {

    private ArrayList<Trigger> m_Triggers = new ArrayList<Trigger>();

    /**
     * this method iterates through all the triggers present in the system and
     * calls their Update method in order that their internal state can be
     * updated if necessary. It also removes any triggers from the system that
     * have their m_bRemoveFromGame field set to true.
     */
    private void updateTriggers() {
        Iterator<Trigger> it = m_Triggers.iterator();
        while (it.hasNext()) {
            Trigger curTrg = it.next();
            if (curTrg.isToBeRemoved()) {
                it.remove();
            } else {
                curTrg.update();
            }
        }
    }

    private void tryTriggers(List<GameCharacter> gameCharacters, List<GenericObstacle> obstacles) {
        //test each entity against the triggers

        //an entity must be ready for its next trigger update and it must be
        //alive before it is tested against each trigger.
//      if (curEnt.isReadyForTriggerUpdate() && curEnt.isAlive())
        for (GameCharacter curEnt : gameCharacters) {
            for (Trigger curTrg : m_Triggers) {
                curTrg.tryTriggerC(curEnt);
            }
        }

        for (GenericObstacle curEnt : obstacles) {
            for (Trigger curTrg : m_Triggers) {
                curTrg.tryTriggerO(curEnt);
            }
        }

    }
    //-----------------
    private void tryTriggersPlayer(Player player){

            for(Trigger curTrg : m_Triggers){
                curTrg.tryTriggerC(player);
            }

    }

    /**
     * this deletes any current triggers and empties the trigger list
     */
    public void clear() {
        m_Triggers.clear();
    }

    /**
     * This method should be called each update-step of the game. It will first
     * update the internal state odf the triggers and then try each entity
     * against each active trigger to test if any should be triggered.
     */
    public void handleAll(List<GameCharacter> gameCharacters, List<GenericObstacle> obstacles){
        updateTriggers();
        tryTriggers(gameCharacters,obstacles);
    }

    /**
     * this is used to register triggers with the TriggerSystem (the
     * TriggerSystem will take care of tidying up memory used by a trigger)
     */
    public void register(Trigger trigger) {
        m_Triggers.add(trigger);
    }

    /**
     * some triggers are required to be rendered (like giver-triggers for
     * example)
     */
    public void render(GraphicsContext gc) {
        for (Trigger curTrg : m_Triggers) {
            curTrg.render(gc);
        }
    }

    public ArrayList<Trigger> getTriggers() {
        return m_Triggers;
    }
}