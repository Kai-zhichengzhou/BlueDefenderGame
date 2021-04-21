package com.bham.bc.view.menu;

import com.bham.bc.view.GameSession;
import com.bham.bc.view.MenuSession;
import com.bham.bc.view.model.GameFlowEvent;
import com.bham.bc.view.model.MenuButton;
import com.bham.bc.view.model.RecordsHandler;
import com.bham.bc.view.model.SubMenu;
import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.json.JSONObject;


/**
 * <h1>End Menu</h1>
 *
 * <p>Represents the end screen for a finished game session. End menu is observed every time
 * a player finishes a game. It asks the player to enter their name and saves their score to
 * a JSON file to be loaded to the leaderboard.</p>
 *
 * <b>Note:</b> the Menu asks for a name and allows to submit the score <i>only</i> if it makes
 * to the leaderboard of up to 10 highest all-time scores
 */
public class EndMenu extends AnchorPane {

    public Rectangle dim;
    public static Stage primaryStage;
    private SubMenu subMenuEnd;

    private JSONObject jsObject;
    private double score;
    private final GameFlowEvent LEAVE_GAME_EVENT;

    public EndMenu() {
        LEAVE_GAME_EVENT = new GameFlowEvent(GameFlowEvent.LEAVE_GAME);

        setMinWidth(GameSession.WIDTH);
        setMinHeight(GameSession.HEIGHT);

        initBgDim();
    }

    /**
     * Adds background dim to the menu
     */
    private void initBgDim() {
        dim = new Rectangle(GameSession.WIDTH, GameSession.HEIGHT);
        dim.setFill(Color.NAVY);
        dim.setOpacity(0.4);
        getChildren().add(dim);
    }

    /**
     * show when user get high socres
     */
    private void showWhenHigh(double score) {
        // Main label to show on the end menu
        Label endMenuLabel = new Label("Congratulations!\nYou made it to the top!");
        endMenuLabel.getStyleClass().add("leaderboard-label");
        //endMenuLabel.setTextAlignment(TextAlignment.CENTER);

        // Label to ask user's name
        Label nameLabel = new Label("Enter your name:");
        nameLabel.getStyleClass().add("slider-label");

        // Input field to get user's name
        TextField nameInput = new TextField();
        nameInput.setMaxWidth(GameSession.WIDTH/3.0);

        // TODO: replace the above 2 lines with this 1 line:
        // TextField nameInput = new MenuTextField();

        // Label and input field for user's name
        VBox labelAndInput = new VBox();
        labelAndInput.setSpacing(15);
        labelAndInput.setAlignment(Pos.CENTER);
        labelAndInput.getChildren().addAll(nameLabel,nameInput);

        // Submit button to push the record to the leaderboard
        MenuButton btnSubmit = new MenuButton("SUBMIT");
        btnSubmit.setOnMouseClicked(e -> {
            LEAVE_GAME_EVENT.setScore(score);
            LEAVE_GAME_EVENT.setName(nameInput.getText());
            btnSubmit.fireEvent(LEAVE_GAME_EVENT);


            MainMenu.recordsHandler.createRecord(new RecordsHandler.Records("13",nameInput.getText(),score+"",RecordsHandler.getDate()));
            MainMenu.tableView.setItems(MainMenu.recordsHandler.sortAndGetData());
        });

        // Container to store all the elements of this end menu
        VBox container = new VBox();
        container.setSpacing(45);
        container.setAlignment(Pos.CENTER);
        container.getChildren().addAll(endMenuLabel,labelAndInput, btnSubmit);

        subMenuEnd = new SubMenu(this);
        subMenuEnd.getChildren().addAll(container);

    }

    /**
     * show when user get high socres
     */
    private void showWhenLow(){
        VBox vBox=new VBox();

        Label endMenuLabel = new Label("Your score didn't make it\nto the leaderboard this time...");
        endMenuLabel.getStyleClass().add("leaderboard-label");
        endMenuLabel.setTextAlignment(TextAlignment.CENTER);

        MenuButton btnReturn = new MenuButton("RETURN TO MENU");
        btnReturn.setOnMouseClicked(e -> { GameSession.gameStage.hide();
            MenuSession manager = new MenuSession();
            primaryStage = manager.getMainStage();
            primaryStage.show();});
        vBox.getChildren().addAll(endMenuLabel, btnReturn);
        vBox.setSpacing(30);
        vBox.setAlignment(Pos.CENTER);
        subMenuEnd = new SubMenu(this);
        subMenuEnd.getChildren().addAll(vBox);


    }

    /**
     * Shows end menu with fade in transition
     * @param gamePane game pane menu will be attached to
     */
    public void show(AnchorPane gamePane, double sco) {
        gamePane.getChildren().add(this);
        score=sco;

        if (RecordsHandler.jsonArrayToFile.length()==0){
            //System.out.println("high");
            showWhenHigh(sco);
        }else {

        jsObject= (JSONObject) RecordsHandler.jsonArrayToFile.get(RecordsHandler.jsonArrayToFile.length()-1);
        if (Double.parseDouble(jsObject.getString("score"))>score){
            //System.out.println("low");
            showWhenLow();
        }else {
            //System.out.println("high");
            showWhenHigh(sco);
        }}

        FadeTransition ft = new FadeTransition(Duration.millis(300), dim);
        ft.setFromValue(0);
        ft.setToValue(0.7);

        ft.play();
        subMenuEnd.show();
    }
}
