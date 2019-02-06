package application;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class AboutController {
	@FXML
	private Label about_text;
	private Scene test;
	
	/*@FXML
	public void initialize() {
		about_text.setWrapText(true);
		System.out.println("h");
		about_text.prefWidthProperty().bind(test.widthProperty());
	}*/
	
	public void new_window() {
		Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/About.fxml"));
            Stage stage = new Stage();
            stage.setTitle("How to Play");
            Scene scene = new Scene(root, 500,500);
            stage.setScene(scene);
            stage.show();
            test = scene;
            //System.out.println("i");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
	}
}
