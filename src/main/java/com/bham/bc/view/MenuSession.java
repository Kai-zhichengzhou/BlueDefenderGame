package com.bham.bc.view;

import com.bham.bc.components.mode.MODE;
import com.bham.bc.view.menu.EndMenu;
import com.bham.bc.view.menu.MainMenu;
import com.bham.bc.view.menu.PauseMenu;
import com.bham.bc.view.model.MenuBackground;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Class managing the behavior of the main menu
 */
public class MenuSession {

    private static final int HEIGHT = 768;
    private static final int WIDTH = 1024;
    private AnchorPane mainPane;
    private Scene mainScene;
    private Stage mainStage;

    private MainMenu mainMenu;
    private PauseMenu pauseMenu;

    /**
     * Constructs the menu view manager
     */
    public MenuSession() {
        mainPane = new AnchorPane();
        mainScene = new Scene(mainPane, WIDTH, HEIGHT);
        mainStage = new Stage();
        mainStage.setScene(mainScene);

        mainMenu = new MainMenu(this, WIDTH, HEIGHT);
        pauseMenu = new PauseMenu();

        initMainMenu();
        initPauseMenu();
    }

    /**
     * Creates the main menu from where the user can start a new game session
     */
    private void initMainMenu() {
        MenuBackground menuBackground = new MenuBackground(WIDTH, HEIGHT);
        mainPane.getChildren().addAll(menuBackground, mainMenu);
    }

    private void initPauseMenu() {}

    private MainMenu getMainMenu() { return mainMenu; }
    private PauseMenu getPauseMenu() { return pauseMenu; }
    private EndMenu getEndMenu(double score) { return new EndMenu(); }


    /**
     * Creates a single Game Session based on a chosen MODE
     * @param mode SURVIVAL or CHALLENGE mode to be set in Controller
     */
    public void createGameSession(MODE mode) {
        GameSession gameSession = new GameSession(mode);
        gameSession.createNewGame(mainStage);
    }

    /**
     * returns the main stage used for the menu
     * @return the menu stage
     */
    public Stage getMainStage() {
        return mainStage;
    }
}
