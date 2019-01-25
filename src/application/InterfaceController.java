package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class InterfaceController {
	@FXML
	private AnchorPane anchor;
	
	@FXML
	private Text turn_text;
	
	@FXML //needed to be recognized by fxml file
	private void fieldImageAction(MouseEvent event) {
		int[][] fieldMatrix = Main.getMatrix();
		int idrow = Character.getNumericValue(((Node) event.getSource()).getId().charAt(1));
		int idcolumn = Character.getNumericValue(((Node) event.getSource()).getId().charAt(2));
		int turn = Main.getTurn(); //0 means cross, 1 means circle
		//circle's turn (Player2)
		if(turn==1 && fieldMatrix[idrow][idcolumn]==0) {
			Image image = new Image("file:tile_circle.png"); //sets the new image
			//gets the current image that is clicked on and performs the desired action on it
			((ImageView) event.getSource()).setImage(image);
			//update current turn indicator to next player
			turn_text.setText("Current turn: PLAYER1");
			Main.update(idrow, idcolumn, turn);
		}
		//cross' turn (Player1)
		else if(turn == 0 && fieldMatrix[idrow][idcolumn]==0) {
			Image image = new Image("file:tile_cross.png"); //sets the new image
			//gets the current image that is clicked on and performs the desired action on it
			((ImageView) event.getSource()).setImage(image);
			//update current turn indicator to next player
			turn_text.setText("Current turn: PLAYER2");
			Main.update(idrow, idcolumn, turn);
		}
	}
	
	public void resetField(ActionEvent event) {
		Main.resetLogic();
		turn_text.setText("Current turn: PLAYER1");
		Image resimg = new Image("file:tile.png");
		for(Node node : anchor.getChildren()) {
			if(node instanceof ImageView) {
				((ImageView) node).setImage(resimg);
			}
		}
	}
}
