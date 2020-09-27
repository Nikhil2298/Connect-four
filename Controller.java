package com.internsahala.game.connect4;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Controller implements Initializable {

	private static final int COLUMN =7;
	private static final int ROW=6;
	private static final int CIRCLE_DIAMETER =80;
	private static final String DISK1_COLOUR = "#7ae334";
	private static final String DISK2_COLOUR = "#ed32e1";

	private static String player1;
	private static String player2;

	private Boolean isPlayer1Turn = true;
	private Boolean isAllowedToInset = true;

	//Structural changes
	Disc[][] insertDiscArray = new Disc[ROW][COLUMN];

	@FXML
	public GridPane rootGridPane;

	@FXML
	public Pane insertedDiskPane;

	@FXML
	public Label playerLabel;

	@FXML
	public TextField playerOneTextField,playerTwoTextField;

	@FXML
	public Button setNamesButton;


	public void createPlayground1(){
		Shape rectangleWithHole = gameStructure();

		rootGridPane.add(rectangleWithHole, 0 , 1);
		setNamesButton.setOnAction(event ->{
			player1 = playerOneTextField.getText();
			player2 = playerTwoTextField.getText();

		});

		List<Rectangle> rectangleList = createClickColumn();
		for(Rectangle rectangle: rectangleList) {
			rootGridPane.add(rectangle, 0, 1);
		}
	}

	public Shape gameStructure(){
		Shape rectangleWithHole = new Rectangle((COLUMN+1)*CIRCLE_DIAMETER, (ROW+1)*CIRCLE_DIAMETER);
		for(int row=0;row<ROW;row++){
			for(int column=0;column<COLUMN;column++){
				Circle newCircle = new Circle();
				newCircle.setRadius(CIRCLE_DIAMETER/2);
				newCircle.setCenterX(CIRCLE_DIAMETER/2);
				newCircle.setCenterY(CIRCLE_DIAMETER/2);

				newCircle.setSmooth(true);



				newCircle.setTranslateX(column*(CIRCLE_DIAMETER+5) +(CIRCLE_DIAMETER/4));
				newCircle.setTranslateY(row*(CIRCLE_DIAMETER+5) +(CIRCLE_DIAMETER/4));
				rectangleWithHole = Shape.subtract(rectangleWithHole,newCircle);
			}
		}

		rectangleWithHole.setFill(Color.WHITE);

		return rectangleWithHole;
	}

	private ArrayList<Rectangle> createClickColumn(){

		List<Rectangle> rectangleList = new ArrayList<Rectangle>();
		for(int col=0;col<COLUMN;col++) {
			Rectangle rectangle = new Rectangle(CIRCLE_DIAMETER, (ROW + 1) * CIRCLE_DIAMETER);
			rectangle.setFill(Color.TRANSPARENT);
			rectangle.setTranslateX(col*(CIRCLE_DIAMETER+5) +CIRCLE_DIAMETER / 4);

			rectangle.setOnMouseEntered(event ->  rectangle.setFill(Color.valueOf("#eeeeee26")));
			rectangle.setOnMouseExited(event ->  rectangle.setFill(Color.TRANSPARENT));

			final int column = col;

			rectangle.setOnMouseClicked(event -> {
				if(player1 != null && player2 != null) {
					if (isAllowedToInset) {
						isAllowedToInset = false;
						insertDisc(new Disc(isPlayer1Turn), column);
					}
				}else {
					nameNotEnter();
				}
			});
			rectangleList.add(rectangle);
		}
		return (ArrayList<Rectangle>) rectangleList;
	}

	private void nameNotEnter() {
		Alert notEntered = new Alert(Alert.AlertType.WARNING);
		notEntered.setTitle("Connect 4");
		notEntered.setHeaderText("Please Enter the Name");
		notEntered.setContentText("You must set the Name First");
		notEntered.show();

	}

	private void insertDisc(Disc disc,int col){
		int row = ROW-1;
		while(row>=0){
			if(getIfDiscPresent(row,col)==null){
				break;
			}
			row--;
		}
		if(row<0){
			return;
		}


		insertDiscArray[row][col] = disc;
		insertedDiskPane.getChildren().add(disc);

		int currentRow = row;
		disc.setTranslateX(col*(CIRCLE_DIAMETER+5) +CIRCLE_DIAMETER / 4);
		TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5),disc);
		translateTransition.setToY(row*(CIRCLE_DIAMETER+5) +CIRCLE_DIAMETER / 4);
		translateTransition.setOnFinished(event -> {
			isAllowedToInset = true;
			if(gameEnded(currentRow,col)){
				gameOver();

			}
			isPlayer1Turn = !isPlayer1Turn;

			playerLabel.setText(isPlayer1Turn ? player1 : player2);

		});
		translateTransition.play();
	}

	private void gameOver() {

		String winner = isPlayer1Turn ? player1 : player2;
		System.out.println("Winner is" + winner);

		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Connect 4");
		alert.setHeaderText("The Winner is "+winner);
		alert.setContentText("Do you want to play again");
		ButtonType yesButton = new ButtonType("Yes");
		ButtonType noButton = new ButtonType("No and Exit");
		alert.getButtonTypes().setAll(yesButton,noButton);

		Platform.runLater( () -> {
			Optional<ButtonType> clickButton = alert.showAndWait();
			if(clickButton.isPresent() &&  clickButton.get() == yesButton){
				resetGame();
			}else {
				Platform.exit();
				System.exit(0);
			}
		});


	}

	public void resetGame() {

		insertedDiskPane.getChildren().clear();
		for(int row =0 ; row < ROW ; row++){
			for(int col =0; col < COLUMN ;col++){
				insertDiscArray[row][col]=null;
			}
		}
		isPlayer1Turn=true;
		player2=null;
		player1 =null;
		playerOneTextField.setText(null);
		playerTwoTextField.setText(null);
		playerLabel.setText("Player One");


		createPlayground1(); //create new
	}

	private boolean gameEnded(int row, int col) {

		List<Point2D> verticalPoints = IntStream.rangeClosed(row-3 ,row +3)
				.mapToObj(r-> new Point2D(r,col))
				.collect(Collectors.toList());

		List<Point2D> horizontalPoints = IntStream.rangeClosed(col-3 ,col +3)
				.mapToObj(c -> new Point2D(row,c))
				.collect(Collectors.toList());

		Point2D startPoint1 = new Point2D(row-3,col+3);
		List<Point2D> diagonal1 = IntStream.rangeClosed(0,6)
				.mapToObj(i -> startPoint1.add( i, -i))
				.collect(Collectors.toList());

		Point2D startPoint2 = new Point2D(row-3,col-3);
		List<Point2D> diagonal2 = IntStream.rangeClosed(0,6)
				.mapToObj(i -> startPoint2.add( i, i)).
						collect(Collectors.toList());

		Boolean isEnded = checkCombination(verticalPoints) || checkCombination(horizontalPoints) || checkCombination(diagonal1) || checkCombination(diagonal2);


		return isEnded;
	}

	private Boolean checkCombination(List<Point2D> points) {

		int chain = 0;
		for (Point2D point : points) {
			int indexOfRow = (int) point.getX();
			int indexOfColumn = (int) point.getY();
			Disc disc = getIfDiscPresent(indexOfRow,indexOfColumn);

			if(disc != null && disc.isPlayerOneMove==isPlayer1Turn){
				chain++;
				if(chain==4){
					return true;
				}
			}else{
				chain =0;
			}
		}
		return false;
	}


	public Disc getIfDiscPresent(int row,int col){
		if(row>=ROW || row<0 || col>=COLUMN || col<0){
			return null;
		}
		return insertDiscArray[row][col];
	}

	private static class Disc extends Circle {
		private final Boolean isPlayerOneMove;

		Disc(Boolean isPlayerOneMove){
			this.isPlayerOneMove = isPlayerOneMove;
			setRadius(CIRCLE_DIAMETER/2);
			setFill(isPlayerOneMove ? Color.valueOf(DISK1_COLOUR): Color.valueOf(DISK2_COLOUR));
			setCenterX(CIRCLE_DIAMETER/2);
			setCenterY(CIRCLE_DIAMETER/2);
		}
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}
}

