package application;


import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.Stage;

public class Main extends Application {

    

    @Override
    public void start(Stage primaryStage) throws IOException {
    	try {
			Parent content = FXMLLoader.load(getClass().getResource("interface_utilisateur.fxml"));
			primaryStage.setTitle("OBIS 3D");
			primaryStage.setScene(new Scene(content));
			primaryStage.show();
			primaryStage.setResizable(false);
		}
		catch(IOException e){
			e.printStackTrace();
		}
    }

    public static void main(String[] args) {
        launch(args);
    }

    
    
}