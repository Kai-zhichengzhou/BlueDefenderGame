package com.bham.bc.view.menu;

import com.bham.bc.view.GameSession;
import com.bham.bc.view.tools.GameFlowEvent;
import com.bham.bc.view.model.MenuButton;
import com.bham.bc.view.model.MenuSlider;
import com.bham.bc.view.model.SubMenu;
import javafx.animation.FadeTransition;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import static com.bham.bc.audio.AudioManager.audioManager;

/**
 * <h1>Pause Menu</h1>
 *
 * <p>Represents an in-game menu that is observed whenever a game session asks to pause the game.
 * The menu allows to resume, change the sound settings, and return to the main menu.</p>
 *
 * <b>Note:</b> neither state, nor the score of the game is saved when returning to the main menu.
 */
public class PauseMenu extends AnchorPane {


    /**
     * use custom {@link SubMenu} to create Pause Menu
     */
    private SubMenu subMenuPause;
    /**
     * use custom {@link SubMenu} to create Settings Menu
     */
    public SubMenu subMenuSettings;
    /**
     * background dim to the menu
     */
    public Rectangle dim;

    /**
     * create pause_game_event
     */
    private final GameFlowEvent PAUSE_GAME_EVENT;
    /**
     * create leave_game_event
     */
    private final GameFlowEvent LEAVE_GAME_EVENT;

    public static MenuSlider musicVolume;
    public static MenuSlider sfxVolume;


    /**
     * Constructs an {@link AnchorPane} layout as the Pause Menu and initialize Pause Menu
     */
    public PauseMenu() {
        PAUSE_GAME_EVENT = new GameFlowEvent(GameFlowEvent.PAUSE_GAME);
        LEAVE_GAME_EVENT = new GameFlowEvent(GameFlowEvent.LEAVE_GAME);
        setMinSize(GameSession.WIDTH, GameSession.HEIGHT);
        initBgDim();
        createSubMenuPause();
        createSubMenuSettings();
    }


    /**
     * Adds background dim to the menu
     */
    private void initBgDim() {
        dim = new Rectangle(GameSession.WIDTH, GameSession.HEIGHT);
        dim.setFill(Color.BLACK);
        dim.setOpacity(0.5);
        getChildren().add(dim);
    }

    /**
     * <p>Creates layout for primary view for pause menu.</p>
     * <p>use custom menu button ({@link MenuButton}).</p>
     * <p>Pause Menu has three buttons, e.g. resume button, setting button and return button. </p>
     */
    private void createSubMenuPause() {
        MenuButton btnResume = new MenuButton("RESUME");
        MenuButton btnSettings = new MenuButton("SETTINGS");
        MenuButton btnReturn = new MenuButton("RETURN TO MENU");

        btnResume.setOnMouseClicked(e -> btnResume.fireEvent(PAUSE_GAME_EVENT));
        btnSettings.setOnMouseClicked(e -> { subMenuPause.hide(); subMenuSettings.show(); });
        btnReturn.setOnMouseClicked(e -> btnReturn.fireEvent(LEAVE_GAME_EVENT));

        subMenuPause = new SubMenu(this);
        subMenuPause.getChildren().addAll(btnResume, btnSettings, btnReturn);
    }

    /**
     * <p>Creates layout for settings in the pause menu.</p>
     * <p>settings have  {@link MenuSlider} for control of volume.</p>
     */
    private void createSubMenuSettings() {
        musicVolume = new MenuSlider("MUSIC", (int) (audioManager.getMusicVolume() * 100));
        sfxVolume = new MenuSlider("EFFECTS", (int) (audioManager.getEffectsVolume() * 100));
        MenuButton btnBack = new MenuButton("BACK");

        musicVolume.getValueProperty().addListener((obsVal, oldVal, newVal) -> audioManager.setMusicVolume(newVal.doubleValue()/100));
        sfxVolume.getValueProperty().addListener((obsVal, oldVal, newVal) -> audioManager.setEffectsVolume(newVal.doubleValue()/100));
        btnBack.setOnMouseClicked(e -> { subMenuSettings.hide(); subMenuPause.show(); });

        subMenuSettings = new SubMenu(this);
        subMenuSettings.getChildren().addAll(musicVolume, sfxVolume, btnBack);
    }

    /**
     * Shows pause menu with fade in transition
     * @param gamePane game pane menu will be attached to
     */
    public void show(AnchorPane gamePane) {
        gamePane.getChildren().add(this);

        FadeTransition ft = new FadeTransition(Duration.millis(300), dim);
        ft.setFromValue(0);
        ft.setToValue(0.7);

        ft.play();
        subMenuPause.show();
    }

    /**
     * Hides pause menu with fade out transition
     * @param gamePane game pane menu will be detached from
     */
    public void hide(AnchorPane gamePane) {
        FadeTransition ft = new FadeTransition(Duration.millis(300), dim);
        ft.setFromValue(0.7);
        ft.setToValue(0);

        ft.play();
        subMenuSettings.hide();
        subMenuPause.hide();

        ft.setOnFinished(e -> gamePane.getChildren().remove(this));
    }
}
