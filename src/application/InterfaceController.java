package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class InterfaceController {
	@FXML
	private AnchorPane anchor;
	
	@FXML //needed to be recognized by fxml file
	private void fieldImageAction(MouseEvent event) {
		int turn = Main.getTurn(); //0 means cross, 1 means circle
		if(turn==1) {
			Image image = new Image("file:tile_circle.png"); //sets the new image
			//gets the current image that is clicked on and performs the desired action on it
			((ImageView) event.getSource()).setImage(image);
		}
		else {
			Image image = new Image("file:tile_cross.png"); //sets the new image
			//gets the current image that is clicked on and performs the desired action on it
			((ImageView) event.getSource()).setImage(image);
		}
	}
	
	public void resetField(ActionEvent event) {
		Image resimg = new Image("file:tile.png");
		for(Node node : anchor.getChildren()) {
			if(node instanceof ImageView) {
				((ImageView) node).setImage(resimg);
			}
		}
	}
}
