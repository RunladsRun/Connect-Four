import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ServerGUI extends Application {
	
	
	TextField port;
	Button open, close;
	Scene main, server;
	ListView<String> list;
	Server serverConnection;
	
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Connect Four Server");
		port = new TextField();
		port.setPromptText("Enter the port number");
		open = new Button("Open");
		close = new Button("Close");
		list = new ListView<String>();
		
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });
		
		close.setOnAction(e -> {
			Platform.exit();
            System.exit(0);
		});
		
		createMain();
		
		open.setOnAction(e-> {
			serverConnection = new Server(data-> {
				Platform.runLater(()->{
					if(data == "0") {
						port.clear();
						port.setPromptText("Invaild port Number!");
					} else {
						createServer();
						primaryStage.setScene(server);
					}
				});
			}, data-> {
				Platform.runLater(()->{
					int index = list.getItems().size();
					list.getItems().add(data.toString());
					list.scrollTo(index);
					});
				}, port.getText());
			
		});
	    
		primaryStage.setScene(main);
		primaryStage.setResizable(false);
		primaryStage.show();
	}
	
	public void createMain() {
		
		Text title = new Text("Connect Four Server");
		VBox vbox = new VBox(port, open);
		vbox.setId("vbox");
		
		title.setFill(Color.AZURE);
		title.setStroke(Color.CYAN);
		
		vbox.setPadding(new Insets(25, 100, 25, 100));
		vbox.setSpacing(15);
		vbox.setAlignment(Pos.CENTER);
		
		title.setFont(Font.font("Verdana", 60));
		port.setAlignment(Pos.CENTER);
		port.setFont(Font.font(15));
		
		VBox root = new VBox(title, vbox);
		root.setSpacing(50);
		root.setAlignment(Pos.CENTER);
		
		root.setPadding(new Insets(20, 100, 50, 100));
		root.getStylesheets().add("main.css");  
		main = new Scene(root, 700, 500);
		
	}
	
	public void createServer() {
		VBox root = new VBox(list, close);
		root.setPadding(new Insets(0, 25, 0, 25));
		root.setSpacing(15);
		root.setAlignment(Pos.CENTER);
		
		close.setFont(Font.font(15));
		close.setTextFill(Color.CORNFLOWERBLUE);
		close.setStyle("-fx-background-color: AQUAMARINE");
		
		root.setStyle("-fx-background-color: ALICEBLUE; -fx-border-color: AQUA;");
		
		
		server = new Scene(root, 400, 500);
	}

}
