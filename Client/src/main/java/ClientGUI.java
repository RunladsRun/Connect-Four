import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Pair;
import javafx.util.Duration;
import javafx.animation.PauseTransition;

import java.util.ArrayList;

public class ClientGUI extends Application {
	
	boolean gameOn, isTurn, isFirst; 
	int gameStatus;
	TextField name, port, ip, name1, name2, message1, message2;
	Text win, lose, tie;
	Button play, exit, no;
	GridPane gameBoard;
	ArrayList<ArrayList<GameButton>> buttonGrid;
	Scene main, game;
	EventHandler<ActionEvent> buttonAction;
	Client clientConnection;
	GameGrid gameGrid;
	PauseTransition pause = new PauseTransition(Duration.seconds(4));
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Connect Four!");
		
		gameOn = false;
		
		play = new Button("Play");
		exit = new Button("Exit");
		no = new Button("No");
		win = new Text("You Win :)");
		lose = new Text("You Lose :(");
		tie = new Text("It's tie :|");
		name = new TextField();
		port = new TextField();
		ip = new TextField();
		name1 = new TextField();
		name2 = new TextField();
		message1 = new TextField();
		message2 = new TextField();
		name1.setEditable(false);
		name2.setEditable(false);
		message1.setEditable(false);
		message2.setEditable(false);
		
		
		
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });
		
		createMain();
		
		buttonAction = new EventHandler<ActionEvent>(){
	 		public void handle(ActionEvent event){
	 			if(!gameOn) {
	 				message1.setText("The Game is not Ready!");
	 			} else if (gameStatus > 0){
	 				message1.setText("The Game is Over, plase wait!");
	 			} else if (!isTurn){
	 				message1.setText("It's not your turn");
	 			} else {
	 				GameButton gb = (GameButton) event.getSource();
	 				int row = gb.getRow();
	 				int col = gb.getCol();
	 				if(gameGrid.vaildGrid(row, col)) {
	 					gameGrid.setGrid(row, col, 'o');
	 					ArrayList<Pair<Integer,Integer>> arr = gameGrid.checkWinning(row,col);
	 					if(arr.size() == 4) {
	 						clientConnection.sendInfo(winningInfo(row,col,arr));
	 						int winRow;
	 						int winCol;
	 						for(int i = 0; i < 4; i++) {
	 							winRow = arr.get(i).getKey();
	 							winCol = arr.get(i).getValue();
	 							buttonGrid.get(winRow).get(winCol).setWinning(isFirst);
	 						}
 							message1.setText("You win!");
 							gameStatus = 1;
 							pause.play();
	 					} else if (gameGrid.checkTie()) {
	 						gb.clicked(isFirst);
	 						message1.setText("It's tie!");
	 						clientConnection.sendInfo(tieInfo(row,col));
 							gameStatus = 3;
 							pause.play();
	 					} else {
	 						gb.clicked(isFirst);
		 					clientConnection.sendInfo(makeInfo(row,col,false));
		 					isTurn = false;
		 					message1.setText("Now It's opponent's turn");
	 					}
	 				} else {
	 					message1.setText("It's not vaild place to move");
	 					clientConnection.sendInfo(makeInfo(row,col,true));
	 				}
	 			}
	 		}
		};
		
		exit.setOnAction(e-> {
			Platform.exit();
            System.exit(0);
		});
		
		no.setOnAction(e-> {
			Platform.exit();
            System.exit(0);
		});
		
		play.setOnAction(e-> {
			clientConnection = new Client(data -> {
				Platform.runLater(()->{
					if(data == "0") {
						port.clear();
						ip.clear();
					} else {
						makeBoard();
						createGame();
						primaryStage.setScene(game);
					}
				});
			}, 
			data-> {
				Platform.runLater(()-> {
					CFourInfo info = (CFourInfo) data;
					if(!gameOn) {
						gameOn = true;
						gameGrid = new GameGrid();
						name2.setText(info.name);
						isFirst = info.first;
						isTurn = isFirst;
						if(isFirst) {
							message1.setText("It's your turn");
							message2.setText("A new player joined! The game begins");
						} else {
							message1.setText("It's opponent's turn");
							message2.setText("The game begins!");
						}
					} else if (info.disconnected) {
						for(int i = 0; i < 6; i++) {
							for (int j = 0; j < 7; j++) {
								buttonGrid.get(i).get(j).unclicked();
							}
						}
						message1.setText("Opponent left, waiting for another player to come...");
						name2.clear();
						message2.clear();
						gameOn = false;
						
					} else {
						int row = info.move.getKey();
						int col = info.move.getValue();
						if(info.tie) {
							buttonGrid.get(row).get(col).clicked(!isFirst);
							message1.setText("It's tie!");
							clientConnection.sendInfo(makeInfo(0,0,false));
							gameStatus = 3;
							pause.play();
							
						} else if (info.four.size() == 4) {
							int winRow;
	 						int winCol;
	 						for(int i = 0; i < 4; i++) {
	 							winRow = info.four.get(i).getKey();
	 							winCol = info.four.get(i).getValue();
	 							buttonGrid.get(winRow).get(winCol).setWinning(!isFirst);
	 						}
							message1.setText("You lose!");
							clientConnection.sendInfo(makeInfo(0,0,false));
							gameStatus = 2;
							pause.play();
						} else {
							gameGrid.setGrid(row, col, 'x');
							buttonGrid.get(row).get(col).clicked(!isFirst);
							isTurn = true;
							message1.setText("Now It's your turn");
						}
						message2.setText(info.name + " made a move on ("
									+ row + ", " + col + ")");
					}
				});
			}, name.getText(), ip.getText(), port.getText());
			clientConnection.start();
		});
		
		pause.setOnFinished(e-> {
			play.setText("Yes");
			primaryStage.setScene(createResult());
			gameOn = false;
			gameStatus = 0;
			name2.clear();
			message2.clear();
		});
	    
		
		primaryStage.setScene(main);
		primaryStage.setResizable(false);
		primaryStage.show();
	}
	
	public void createMain() {
		Text title = new Text("Connect Four");
		Text nameText = new Text("Name: ");
		Text portText = new Text("Port: ");
		Text ipText = new Text("IP: ");
		
		nameText.setFont(Font.font(30));
		portText.setFont(Font.font(30));
		ipText.setFont(Font.font(30));
		
		GridPane info = new GridPane();
		info.add(nameText, 0,0);
		info.add(name, 1, 0);
		info.add(portText, 0,1);
		info.add(port, 1, 1);
		info.add(ipText, 0,2);
		info.add(ip, 1, 2);
		info.add(play, 1, 3);
		
		info.setPadding(new Insets(20, 15, 15, 10));
		
		info.setVgap(20);
		info.setHgap(20);
		
		GridPane.setMargin(play, new Insets(0,0,0,140));
		
		info.setStyle("-fx-border-color: AQUA;"
				+ "	-fx-background-color: ALICEBLUE;"
				+ "	-fx-border-width: 5;"
				+ "	-fx-background-radius: 18 18 18 18;"
				+ "	-fx-border-radius: 18 18 18 18;");
		
		name.setFont(Font.font(15));
 		port.setFont(Font.font(15));
 		ip.setFont(Font.font(15));
		
		Image image = new Image("mainClient.jpg", 350, 275, false, true);
		ImageView iv = new ImageView(image);
		
		GridPane root = new GridPane();
		
		root.add(title, 0, 0, 2, 1);
		root.add(iv, 0, 1);
		root.add(info, 1, 1);
		root.add(exit, 1, 2);
		
		play.setFont(Font.font(17));
		exit.setFont(Font.font(13));
		
		play.setStyle("-fx-background-color: AQUAMARINE;"
				+ "	-fx-text-fill: CORNFLOWERBLUE;");
		exit.setStyle("-fx-background-color: AQUAMARINE;"
				+ "	-fx-text-fill: CORNFLOWERBLUE;");
		
		title.setFont(Font.font(80));
		root.setAlignment(Pos.CENTER);
		root.setPadding(new Insets(20, 20, 20, 20));
		root.setVgap(20);
		root.setHgap(30);
		
		GridPane.setMargin(exit, new Insets(0,0,0,320));
		
		title.setFill(Color.YELLOW);
		title.setStroke(Color.RED);
		title.setStrokeWidth(3);
		
		root.setStyle("-fx-background-color: LIGHTCYAN;"
				+ "-fx-border-color: AQUAMARINE;"
				+ "	-fx-border-width: 8;");
		
		main = new Scene(root, 800, 500);
	}
	
	public void createGame() {
		
		
		VBox player1 = new VBox(name1, message1);
		VBox player2 = new VBox(name2, message2);
		
		player1.setSpacing(30);
		player2.setSpacing(30);
		player1.setAlignment(Pos.CENTER);
		player2.setAlignment(Pos.CENTER);
		player1.setPadding(new Insets(40,40,40,40));
		player2.setPadding(new Insets(40,40,40,40));
		GridPane.setMargin(player1, new Insets(30,0,0,0));
		
		name1.setText(name.getText());
		message1.setText("Waiting for other player to join");
		
		name1.setMinSize(200, 50);
		name1.setFont(Font.font(20));
		name1.setAlignment(Pos.CENTER);
		
		name2.setMinSize(200, 50);
		name2.setFont(Font.font(20));
		name2.setAlignment(Pos.CENTER);
		
		VBox.setMargin(name1, new Insets(0,20,0,20));
		VBox.setMargin(name2, new Insets(0,20,0,20));
		
		message1.setMinSize(200, 30);
		message2.setMinSize(200, 30);
		
		exit.setFont(Font.font(24));
		exit.setAlignment(Pos.TOP_RIGHT);
		
		player1.setStyle("-fx-border-color: AQUA;"
				+ "	-fx-background-color: ALICEBLUE;"
				+ "	-fx-border-width: 5;"
				+ "	-fx-background-radius: 18 18 18 18;"
				+ "	-fx-border-radius: 18 18 18 18;");
		
		player2.setStyle("-fx-border-color: AQUA;"
				+ "	-fx-background-color: ALICEBLUE;"
				+ "	-fx-border-width: 5;"
				+ "	-fx-background-radius: 18 18 18 18;"
				+ "	-fx-border-radius: 18 18 18 18;");
		
		GridPane root = new GridPane();
		
		root.add(gameBoard, 0, 0, 1, 4);
		root.add(player1, 1, 0);
		root.add(player2, 1, 1);
		root.add(exit, 1, 2);
		
		gameBoard.setHgap(10);
		gameBoard.setVgap(10);
		
		gameBoard.setStyle("-fx-background-color: MEDIUMBLUE;"
				+ "	-fx-border-color: NAVY;"
				+ "	-fx-border-width: 5;"
				+ "	-fx-background-radius: 18 18 18 18;"
				+ "	-fx-border-radius: 18 18 18 18;");
		gameBoard.setPadding(new Insets(20,20,20,20));
		
		root.setPadding(new Insets(20, 20, 20, 20));
		root.setHgap(20);
		root.setVgap(35);
		
		root.setStyle("-fx-background-color: LIGHTCYAN;"
				+ "-fx-border-color: AQUAMARINE;"
				+ "	-fx-border-width: 5;");
		
		game = new Scene(root, 1150, 650);
	}
	
	public Scene createResult() {
		HBox hbox = new HBox(play, no);
		Text text = new Text("Do you want to keep playing?");
		Text result;
		
		hbox.setSpacing(150);
		
		if (gameStatus == 1) {
			result = win;
		} else if (gameStatus == 2){
			result = lose;
		} else {
			result = tie;
		}
		VBox root = new VBox(result, text, hbox);
		
		result.setFont(Font.font(30));
		text.setFont(Font.font(24));
		result.setFill(Color.AQUA);
		text.setFill(Color.AQUA);
		
		no.setFont(Font.font(17));
		no.setStyle("-fx-background-color: AQUAMARINE;"
				+ "	-fx-text-fill: CORNFLOWERBLUE;");
		root.setStyle("-fx-background-color: LIGHTCYAN;"
				+ "-fx-border-color: AQUAMARINE;"
				+ "	-fx-border-width: 7;");
		
		hbox.setAlignment(Pos.CENTER);
		root.setSpacing(30);
		root.setAlignment(Pos.CENTER);
		
		return new Scene(root, 600, 400);
	}
	
	public void makeBoard() {
		gameBoard = new GridPane();
		buttonGrid = new ArrayList<ArrayList<GameButton>>();
		
		for (int i = 0; i < 6; i++) {
			ArrayList<GameButton> arr = new ArrayList<GameButton>();
			for (int j = 0; j < 7; j++) {
				arr.add(new GameButton(i, j));
				arr.get(j).setOnAction(buttonAction);
				gameBoard.add(arr.get(j), j, i);
			}
			buttonGrid.add(arr);
		}
	}
	
	public CFourInfo makeInfo(int row, int col, boolean invalid) {
		CFourInfo info = new CFourInfo();
		info.move = new Pair<Integer,Integer>(row, col);
		info.invalid = invalid;
		info.disconnected = false;
		info.tie = false;
		info.name = name.getText();
		info.four = new ArrayList<Pair<Integer,Integer>>();
		return info;
	}
	
	public CFourInfo winningInfo(int row, int col, ArrayList<Pair<Integer,Integer>> arr) {
		CFourInfo info = new CFourInfo();
		info.move = new Pair<Integer,Integer>(row, col);
		info.invalid = false;
		info.tie = false;
		info.disconnected = false;
		info.name = name.getText();
		info.four = arr;
		return info;
	}
	
	public CFourInfo tieInfo(int row, int col) {
		CFourInfo info = new CFourInfo();
		info.move = new Pair<Integer,Integer>(row, col);
		info.tie = true;
		return info;
	}


}
