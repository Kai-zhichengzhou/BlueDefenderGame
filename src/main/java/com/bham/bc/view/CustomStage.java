package com.bham.bc.view;

import com.bham.bc.utils.Constants;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.beans.value.ChangeListener;


/**
 * @author : YiFan Yaao
 * @version : 1.0
 * @project: BattleCityGUI
 * @name : CustomStageSubscene.java
 * @data : 2021/2/28
 * @time : 14:43
 */
public class CustomStage {

    private Stage stage;
    private Scene gamescene;

    private double x = 0.00;
    private double y = 0.00;
    private double width = 0.00;
    private double height = 0.00;
    private boolean isMax = false;
    private boolean isRight;// 是否处于右边界调整窗口状态
    private boolean isBottomRight;// 是否处于右下角调整窗口状态
    private boolean isBottom;// 是否处于下边界调整窗口状态
    private double RESIZE_WIDTH = 5.00;
    private double MIN_WIDTH = 400.00;
    private double MIN_HEIGHT = 300.00;
    private double xOffset = 0, yOffset = 0;//自定义dialog移动横纵坐标
    private String typeOf;
    private AnchorPane gamePane;
    private Label setMenu;



    public CustomStage(Stage gameStage, Scene gameScene,AnchorPane pane) {
        stage=gameStage;
        gamescene=gameScene;
        gamePane=pane;

    }

    public void startMenuIcon(){
        setMenu = new Label("");
        setMenu.setId("setMenu");
        setMenu.setPrefWidth(29);
        setMenu.setPrefHeight(40);



        setMenu.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                /*
                if (GameSession.isshown==false){
                    CustomMenuSubscene customMenuSubscene =new CustomMenuSubscene();
                    customMenuSubscene.getStage(stage);
                    customMenuSubscene.getGameScene(gamescene);
//                    customMenuSubscene.createDeafultSubscene();
                    gamePane.getChildren().add(customMenuSubscene);
                    GameSession.isshown=true;}

                 */
            }
        });
        setMenu.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {

                if (event.getCode() == KeyCode.ENTER) {
                    System.out.println("    You pressed Enter!");
                }

            }
        });
        KeyCodeCombination keyCodeCombination=new KeyCodeCombination(KeyCode.ENTER);
        gamescene.getAccelerators().put(keyCodeCombination, new Runnable() {
            @Override
            public void run() {
                /*
                if (GameSession.isshown==false){
                    CustomMenuSubscene customMenuSubscene =new CustomMenuSubscene();
                    customMenuSubscene.getStage(stage);
                    customMenuSubscene.getGameScene(gamescene);
//                    customMenuSubscene.createDeafultSubscene();
                    gamePane.getChildren().add(customMenuSubscene);
                    GameSession.isshown=true;}

                 */

            }
        });






    }


    public void createCustomStage(AnchorPane root){
        startMenuIcon();
        stage.initStyle(StageStyle.TRANSPARENT);

        HBox gpTitle = new HBox();
        gpTitle.setId("title");
        gpTitle.setSpacing(4);
        gpTitle.setPadding(new Insets(15,5,17,5));
        Label lbTitle = new Label("Battle City");
        lbTitle.setId("name");
        Glow glow=new Glow();
        lbTitle.setEffect(glow);
        glow.setLevel(0.6);
        BackgroundImage image=new BackgroundImage(new Image("file:src/main/resources/GUIResources/menuBar03.png",Constants.WINDOW_WIDTH,34,false,true), BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,null);

        gpTitle.setBackground(new Background(image));
        gpTitle.setMinWidth(Constants.WINDOW_WIDTH);
        gpTitle.setMaxWidth(Constants.WINDOW_WIDTH);
        gpTitle.setMinHeight(15);
        gpTitle.setMaxHeight(35);
        gpTitle.getStylesheets().add(CustomStage.class.getResource("../../../../GUIResources/Stage.css").toExternalForm());


        Label btnMin = new Label();
        btnMin.setPrefWidth(33);
        btnMin.setPrefHeight(26);

        BackgroundImage image2=new BackgroundImage(new Image("file:src/main/resources/GUIResources/Min.png",24,10,false,true), BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,null);

        btnMin.setBackground(new Background(image2));

        Label btnClose = new Label();
        btnClose.setPrefWidth(33);
        btnClose.setPrefHeight(26);


        String[] types=new String[]{"TYPE 1","TYPE 2","TYPE 3","TYPE 4","TYPE 5"};
        ChoiceBox changeSkin=new ChoiceBox(FXCollections.observableArrayList(
                "Classic Black","Classic Grey","Classic Blue","Classic Orange","Classic Gold"
        ));
        changeSkin.setId("changeSkin");
        changeSkin.setMaxSize(25,22);
        changeSkin.setMinSize(25,22);
        BackgroundImage image3=new BackgroundImage(new Image("file:src/main/resources/GUIResources/skinSet.png",25,22,false,true), BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,null);
        changeSkin.setBackground(new Background(image3));
        changeSkin.getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Number> ov,Number old_val,Number new_val)->{


            typeOf = types[new_val.intValue()];
            if (typeOf.equals("TYPE 1")){
                BackgroundImage image4=new BackgroundImage(new Image("file:src/main/resources/GUIResources/menuBar05.png",Constants.WINDOW_WIDTH,34,false,true), BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,null);

                gpTitle.setBackground(new Background(image4));
                setMenu.requestFocus();

            }else if (typeOf.equals("TYPE 2")){
                BackgroundImage image4=new BackgroundImage(new Image("file:src/main/resources/GUIResources/menuBar02.png",Constants.WINDOW_WIDTH,34,false,true), BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,null);

                gpTitle.setBackground(new Background(image4));
                setMenu.requestFocus();

            }else if (typeOf.equals("TYPE 3")){
                BackgroundImage image4=new BackgroundImage(new Image("file:src/main/resources/GUIResources/menuBar.png",Constants.WINDOW_WIDTH,34,false,true), BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,null);

                gpTitle.setBackground(new Background(image4));
                setMenu.requestFocus();

            }else if (typeOf.equals("TYPE 4")){
                BackgroundImage image4=new BackgroundImage(new Image("file:src/main/resources/GUIResources/menuBar04.png",Constants.WINDOW_WIDTH,34,false,true), BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,null);

                gpTitle.setBackground(new Background(image4));
                setMenu.requestFocus();

            }else if (typeOf.equals("TYPE 5")){
                BackgroundImage image4=new BackgroundImage(new Image("file:src/main/resources/GUIResources/menuBar03.png",Constants.WINDOW_WIDTH,34,false,true), BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,null);

                gpTitle.setBackground(new Background(image4));
                setMenu.requestFocus();

            }



        });
        changeSkin.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                setMenu.requestFocus();
            }
        });






        btnClose.setId("winClose");
        btnMin.setId("winMin");
        lbTitle.setTranslateX(-540);
        btnMin.setTranslateY(6);
        changeSkin.setTranslateY(1);
        changeSkin.setTranslateX(-3);


        gpTitle.getChildren().add(lbTitle);
        gpTitle.getChildren().add(setMenu);
        gpTitle.getChildren().add(changeSkin);
        gpTitle.getChildren().add(btnMin);
        gpTitle.getChildren().add(btnClose);
        root.getChildren().add(gpTitle);

        gpTitle.setLayoutX(0);
        gpTitle.setLayoutY(0);
        gpTitle.setAlignment(Pos.CENTER_RIGHT);



        btnMin.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                stage.setIconified(true);
            }



        });

        btnClose.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                stage.close();
            }
        });

        stage.xProperty().addListener(new ChangeListener<Number>() {


            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (newValue != null && !isMax) {
                    x = newValue.doubleValue();
                }
            }
        });
        stage.yProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (newValue != null && !isMax) {
                    y = newValue.doubleValue();
                }
            }
        });
        stage.widthProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (newValue != null && !isMax) {
                    width = newValue.doubleValue();
                }
            }
        });
        stage.heightProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (newValue != null && !isMax) {
                    height = newValue.doubleValue();
                }
            }
        });

        root.setOnMouseMoved((MouseEvent event) -> {
            event.consume();
            double x = event.getSceneX();
            double y = event.getSceneY();
            double width = stage.getWidth();
            double height = stage.getHeight();
            Cursor cursorType = Cursor.DEFAULT;// 鼠标光标初始为默认类型，若未进入调整窗口状态，保持默认类型
            // 先将所有调整窗口状态重置
            isRight = isBottomRight = isBottom = false;
            if (y >= height - RESIZE_WIDTH) {
                if (x <= RESIZE_WIDTH) {// 左下角调整窗口状态
                    //不处理

                } else if (x >= width - RESIZE_WIDTH) {// 右下角调整窗口状态
                    isBottomRight = true;
                    cursorType = Cursor.SE_RESIZE;
                } else {// 下边界调整窗口状态
                    isBottom = true;
                    cursorType = Cursor.S_RESIZE;
                }
            } else if (x >= width - RESIZE_WIDTH) {// 右边界调整窗口状态
                isRight = true;
                cursorType = Cursor.E_RESIZE;
            }
            // 最后改变鼠标光标
            root.setCursor(cursorType);
        });

        root.setOnMouseDragged((MouseEvent event) -> {

            //根据鼠标的横纵坐标移动dialog位置
            event.consume();
            if (yOffset != 0 ) {
                stage.setX(event.getScreenX() - xOffset);
                if (event.getScreenY() - yOffset < 0) {
                    stage.setY(0);
                } else {
                    stage.setY(event.getScreenY() - yOffset);
                }
            }

            double x = event.getSceneX();
            double y = event.getSceneY();
            // 保存窗口改变后的x、y坐标和宽度、高度，用于预判是否会小于最小宽度、最小高度
            double nextX = stage.getX();
            double nextY = stage.getY();
            double nextWidth = stage.getWidth();
            double nextHeight = stage.getHeight();
            if (isRight || isBottomRight) {// 所有右边调整窗口状态
                nextWidth = x;
            }
            if (isBottomRight || isBottom) {// 所有下边调整窗口状态
                nextHeight = y;
            }
            if (nextWidth <= MIN_WIDTH) {// 如果窗口改变后的宽度小于最小宽度，则宽度调整到最小宽度
                nextWidth = MIN_WIDTH;
            }
            if (nextHeight <= MIN_HEIGHT) {// 如果窗口改变后的高度小于最小高度，则高度调整到最小高度
                nextHeight = MIN_HEIGHT;
            }
            // 最后统一改变窗口的x、y坐标和宽度、高度，可以防止刷新频繁出现的屏闪情况
            stage.setX(nextX);
            stage.setY(nextY);
            stage.setWidth(nextWidth);
            stage.setHeight(nextHeight);

        });
        //鼠标点击获取横纵坐标
        root.setOnMousePressed(event -> {
            event.consume();
            xOffset = event.getSceneX();
            if (event.getSceneY() > 46) {
                yOffset = 0;
            } else {
                yOffset = event.getSceneY();
            }
        });
//





    }

}