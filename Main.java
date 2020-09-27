package com.internsahala.game.connect4;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {

        launch(args);
    }

    private Controller controller;
    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("game.fxml"));
        GridPane rootGridPane = loader.load();
        controller= loader.getController();
        controller.createPlayground1();


        MenuBar menuBar = createMenu();
        menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
        Pane menuPane = (Pane) rootGridPane.getChildren().get(0);
        menuPane.getChildren().add(menuBar);


        Scene scene = new Scene(rootGridPane);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Connect Four");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private MenuBar createMenu(){
        //New Menu

        Menu fileMenu = new Menu("File");


        // New Menu Item
        MenuItem newMenuItem = new MenuItem("New");
        newMenuItem.setOnAction(event -> controller.resetGame());

        //Reset Menu Item
        MenuItem resetMenuItem = new MenuItem("Reset");
        resetMenuItem.setOnAction(event -> controller.resetGame());

        SeparatorMenuItem separatorMenuItem = new SeparatorMenuItem();
        //Exit Menu Item
        MenuItem exitMenuItem = new MenuItem("Exit");

        exitMenuItem.setOnAction(event -> exitGame());

        fileMenu.getItems().addAll(newMenuItem,resetMenuItem,separatorMenuItem,exitMenuItem);

        //Help Menu
        Menu helpMenu = new Menu("Help");

        //About Game Menu
        MenuItem aboutGame = new MenuItem("About Connect4");
        aboutGame.setOnAction(event -> aboutConnect4Game());

        SeparatorMenuItem separator = new SeparatorMenuItem();
        //About me

        MenuItem aboutMe = new MenuItem("About Me");
        aboutMe.setOnAction(event -> aboutDeveloper());

        helpMenu.getItems().addAll(aboutGame, separator, aboutMe);


        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu,helpMenu);

        return menuBar;
    }

    private void aboutDeveloper() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About The Developer");
        alert.setHeaderText("Nikhil Dogra");
        alert.setContentText("I Love to create new Games . I  am learning core java to make such games.");
        alert.show();

    }

    private void aboutConnect4Game() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About Connect4 Game");
        alert.setHeaderText("How to Play?");
        alert.setContentText("Connect Four is a two-player connection game in which the" +
                " players first choose a color and then take turns dropping colored discs " +
                "from the top into a seven-column, six-row vertically suspended grid. " +
                "The pieces fall straight down, occupying the next available space within the column. " +
                "The objective of the game is to be the first to form a horizontal, vertical, or" +
                " diagonal line of four of one's own discs. Connect Four is a solved game. " +
                "The first player can always win by playing the right moves.");
        alert.show();

    }

    private void exitGame() {

        Platform.exit();
        System.exit(0);
    }


}

