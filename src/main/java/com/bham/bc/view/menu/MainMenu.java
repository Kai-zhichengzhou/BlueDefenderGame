package com.bham.bc.view.menu;

import com.bham.bc.audio.SFX;
import com.bham.bc.components.environment.MapType;
import com.bham.bc.components.mode.MODE;
import com.bham.bc.view.MenuSession;
import com.bham.bc.view.model.MenuButton;
import com.bham.bc.view.model.NewGameEvent;
import com.bham.bc.view.model.SubMenu;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import static com.bham.bc.audio.AudioManager.audioManager;

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
    private VBox vBox;
    private VBox vBox2;

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
        subMenuScores=new SubMenu(this);
        subMenuScores.setMinHeight(430);
        subMenuScores.setMinWidth(850);
        BackgroundImage image=new BackgroundImage(new Image("file:src/main/resources/GUIResources/img_3.png",430,850,false,true), BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,null);
        subMenuScores.setBackground(new Background(image));
        subMenuScores.setLayoutX(200);
        subMenuScores.setLayoutY(-80);
        Text text2 = new Text();
        text2.setText("Scores:");
        text2.setStyle(" -fx-font-size: 25px;\n" +
                "-fx-text-fill: gold;\n"+
                "    -fx-font-family: \"Arial Narrow\";\n" +
                "    -fx-font-weight: bold;\n" +
                "\n" +
                "    -fx-stroke: gold;");
        Glow glow1=new Glow();
        text2.setEffect(glow1);
        glow1.setLevel(0.7);
        text2.setTranslateX(90);
        text2.setTranslateY(40);
        subMenuScores.getChildren().add(text2);
        getChildren().add(subMenuScores);
        subMenuScores.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                    System.out.println("    You pressed ESCAPE!");
                    subMenuScores.hide();
                    subMenuMain.show();
            }
        });

    }

    /**
     * Creates a sub-menu for settings. This menu is observed whenever "SETTINGS" is clicked
     * and allows the user to configure UI parameters, such as SFX or MUSIC volume
     */
    private void createSubMenuSettings() {
        volumeSlider();
        SFXVolumeSlider();
        MenuButton btnBack = new MenuButton("BACK");

        btnBack.setOnMouseClicked(e -> { subMenuSettings.hide(); subMenuMain.show(); });

        subMenuSettings = new SubMenu(this);
        subMenuSettings.getChildren().addAll(vBox,vBox2,btnBack);
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

    public void volumeSlider(){
        Slider volumeSlider = new Slider();
        volumeSlider.setValue(100);

        Label num=new Label((int)volumeSlider.valueProperty().getValue().doubleValue()+"");
        num.setStyle(" -fx-font-size: 15px;\n" +
                "    -fx-text-fill: white;\n" +
                "    -fx-font-family: \"Arial Narrow\";\n" +
                "    -fx-font-weight: bold;");

        volumeSlider.valueProperty().addListener((obsVal, oldVal, newVal) -> {
            audioManager.setMusicVolume(newVal.doubleValue()/100);
            num.setText(newVal.intValue() + "");
        });

        HBox HBox=new HBox(volumeSlider,num);

        Label volume=new Label("Volume:");

        volume.setStyle(" -fx-font-size: 25px;\n" +
                "    -fx-text-fill: white;\n" +
                "    -fx-font-family: \"Arial Narrow\";\n" +
                "    -fx-font-weight: bold;");
        vBox=new VBox(volume,HBox);
    }

    public void SFXVolumeSlider(){
        Slider volumeSlider = new Slider();
        volumeSlider.setValue(100);

        Label num=new Label((int)volumeSlider.valueProperty().getValue().doubleValue()+"");
        num.setStyle(" -fx-font-size: 15px;\n" +
                "    -fx-text-fill: white;\n" +
                "    -fx-font-family: \"Arial Narrow\";\n" +
                "    -fx-font-weight: bold;");

        volumeSlider.valueProperty().addListener((obsVal, oldVal, newVal) -> {
            //SFX
            SFX[] sfxs=SFX.values();
            for (SFX v:sfxs){
                v.setVolume(newVal.doubleValue()/100);
            }

            num.setText(newVal.intValue() + "");
        });

        HBox HBox=new HBox(volumeSlider,num);

        Label volume=new Label("SFX Volume:");

        volume.setStyle(" -fx-font-size: 25px;\n" +
                "    -fx-text-fill: white;\n" +
                "    -fx-font-family: \"Arial Narrow\";\n" +
                "    -fx-font-weight: bold;");
        vBox2=new VBox(volume,HBox);

    }





}
