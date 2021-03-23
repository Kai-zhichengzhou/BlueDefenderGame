package com.bham.bc.view.menu;

import com.bham.bc.components.environment.MapType;
import com.bham.bc.components.mode.MODE;
import com.bham.bc.view.MenuSession;
import com.bham.bc.view.model.MenuButton;
import com.bham.bc.view.model.NewGameEvent;
import com.bham.bc.view.model.SubMenu;
import javafx.geometry.Pos;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * <h1>Main Menu</h1>
 *
 * <p>Represents the primary menu of the game. All the buttons, sub-menus and their functionalities
 * are defined here. The class is created only once by {@link MenuSession} and is observed whenever
 * a game session is not active.</p>
 */
public class MainMenu extends AnchorPane {

    private SubMenu subMenuMain;
    private SubMenu subMenuMode;
    private SubMenu subMenuScores;
    private SubMenu subMenuSettings;

    private NewGameEvent newGameEvent;

    /**
     * Constructs an AnchorPane layout as the Main Menu
     *
     * @param width  menu window's length
     * @param height menu window's height
     */
    public MainMenu(double width, double height) {
        newGameEvent = new NewGameEvent(NewGameEvent.START_GAME);
        setWidth(width);
        setHeight(height);

        initBgDim();
        initTitle();

        createSubMenuMain();
        createSubMenuMode();
        createSubMenuScores();
        createSubMenuSettings();

        getChildren().addAll(subMenuMain);
        subMenuMain.show();
    }

    /**
     * Adds background dim to the menu
     */
    private void initBgDim() {
        Rectangle bg = new Rectangle(getWidth(), getHeight());
        bg.setFill(Color.GRAY);
        bg.setOpacity(0.2);

        getChildren().add(bg);
    }

    /**
     * Adds title to the menu
     */
    private void initTitle() {
        Title title = new Title("T A N K 1 G A M E");
        title.setTranslateY(100);
        title.setTranslateX(getWidth()/2 - title.getWidth()/2);

        getChildren().add(title);
    }

    /**
     * Creates the primary sub-menu for the main menu. This defines the behavior of all the
     * necessary buttons to control the GUI actions and create corresponding sub-menus.
     */
    private void createSubMenuMain() {
        MenuButton btnSolo = new MenuButton("SOLO");
        MenuButton btnCoop = new MenuButton("CO-OP");
        MenuButton btnScores = new MenuButton("HIGH-SCORES");
        MenuButton btnSettings = new MenuButton("SETTINGS");
        MenuButton btnQuit = new MenuButton("QUIT");

        btnSolo.setOnMouseClicked(e -> { newGameEvent.setNumPlayers(1); subMenuMain.hide(); subMenuMode.show(); });
        btnCoop.setOnMouseClicked(e -> { newGameEvent.setNumPlayers(2); subMenuMain.hide(); subMenuMode.show(); });
        btnScores.setOnMouseClicked(e -> { subMenuMain.hide(); subMenuScores.show(); });
        btnSettings.setOnMouseClicked(e -> { subMenuMain.hide(); subMenuSettings.show(); });
        btnQuit.setOnMouseClicked(e -> System.exit(0));

        subMenuMain = new SubMenu(this);
        subMenuMain.getChildren().addAll(btnSolo, btnCoop, btnScores, btnSettings, btnQuit);
    }

    /**
     * Creates the sub-menu for mode selection. This menu is observed whenever "SOLO" or
     * "CO-OP" is clicked and asks {@link com.bham.bc.view.MenuSession} to initiate a
     * single {@link com.bham.bc.view.GameSession} based on the selected parameters
     */
    private void createSubMenuMode() {
        MenuButton btnBack = new MenuButton("BACK");
        MenuButton btnSurvival = new MenuButton("SURVIVAL");
        MenuButton btnChallenge = new MenuButton("CHALLENGE");

        btnBack.setOnMouseClicked(e -> { subMenuMode.hide(); subMenuMain.show(); });
        btnSurvival.setOnMouseClicked(e -> { newGameEvent.setMode(MODE.SURVIVAL); newGameEvent.setMapType(MapType.Map1); btnSurvival.fireEvent(newGameEvent); });
        btnChallenge.setOnMouseClicked(e -> { newGameEvent.setMode(MODE.CHALLENGE); newGameEvent.setMapType(MapType.EmptyMap); btnChallenge.fireEvent(newGameEvent);});

        subMenuMode = new SubMenu(this);
        subMenuMode.getChildren().addAll(btnBack, btnSurvival, btnChallenge);
    }

    /**
     * Creates a sub-menu to view high-scores of both modes. This menu is observed whenever
     * "HIGH-SCORES" is clicked and shows top 10 scores.
     */
    private void createSubMenuScores() {
        subMenuScores = new SubMenu(this);
    }

    /**
     * Creates a sub-menu for settings. This menu is observed whenever "SETTINGS" is clicked
     * and allows the user to configure UI parameters, such as SFX or MUSIC volume
     */
    private void createSubMenuSettings() {
        MenuButton btnBack = new MenuButton("BACK");
        MenuButton btnSFX = new MenuButton("SFX VOLUME");
        MenuButton btnMusic = new MenuButton("MUSIC VOLUME");

        btnBack.setOnMouseClicked(e -> { subMenuSettings.hide(); subMenuMain.show(); });

        subMenuSettings = new SubMenu(this);
        subMenuSettings.getChildren().addAll(btnBack, btnSFX, btnMusic);
    }


    /**
     *  <h1>Title of the game</h1>
     *
     *  <p>This is a unique component provided by a parent {@link com.bham.bc.view.menu.MainMenu}.
     *  This is because the Title should always be part of the Main Menu node.
     */
    private static class Title extends StackPane {

        private static final double WIDTH = 475;
        private static final double HEIGHT = 60;

        /**
         * Constructs a title used in the Main Menu layout
         * @param name the title of the game
         * TODO: look for fancier/more game-like fonts
         */
        public Title(String name) {
            Rectangle bg = new Rectangle(WIDTH, HEIGHT);
            setWidth(WIDTH);
            setHeight(HEIGHT);
            bg.setStroke(Color.WHITE);
            bg.setStrokeWidth(3);
            bg.setFill(null);


            Text text = new Text(name);
            text.setFill(Color.WHITE);
            text.setFont(Font.font("Times New Roman", FontWeight.SEMI_BOLD, 50));

            setAlignment(Pos.TOP_CENTER);
            getChildren().addAll(bg,text);
        }
    }
}
