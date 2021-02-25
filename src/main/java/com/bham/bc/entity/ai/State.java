package com.bham.bc.entity.ai;

public class State{

    private final Action[] actions;
    private final Action[] entryActions;
    private final Action[] exitActions;
    private final Transition[] transitions;

    /**
     * Constructor for a State
     * @param actions List of actions to be performed while the State is active
     * @param entryActions List of actions to be performed when the State is entered
     * @param exitActions List of actions to be performed when the State is exited
     * @param transitions List of transitions that can occur from this state
     */
    public State(Action[] actions, Action[] entryActions, Action[] exitActions, Transition[] transitions){
        this.actions = actions;
        this.entryActions = entryActions;
        this.exitActions = exitActions;
        this.transitions = transitions;
    }

    /**
     * Returns the actions of the State
     * @return
     */
    public Action[] getActions(){
        return actions;
    }

    /**
     * Returns the actions to start the State
     * @return The entry actions
     */
    public Action[] getEntryActions(){
        return entryActions;
    }

    /**
     * Returns the actions to leave the State with
     * @return The exit actions
     */
    public Action[] getExitActions(){
        return exitActions;
    }

    /**
     * Returns the transitions of the State
     * @return the transitions
     */
    public Transition[] getTransitions(){
        return transitions;
    }
}