package application;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class InterfaceController {
	private static final int cross = 0;
	private static final int circle = 1;
	private int wincon = 0;
	private String player1 = "PLAYER1";
	private String player2 = "PLAYER2";
	
	@FXML
	private TextField p1_name;
	
	@FXML
	private TextField p2_name;
	
	@FXML
	private AnchorPane anchor;
	
	@FXML
	private Text turn_text;
	
	@FXML
	public void initialize() {
		p1_name.textProperty().addListener((obs, oldText, newText)->{
			if(newText.length()==0) {
				player1 = "PLAYER1";
			}
			else {
				player1 = newText;
			}
			String player = (Main.getTurn()==cross)? player1 : player2;
			turn_text.setText("Current turn: "+player);
		});
		p2_name.textProperty().addListener((obs, oldText, newText)->{
			if(newText.length()==0) {
				player2 = "PLAYER2";
			}
			else {
				player2 = newText;
			}
			String player = (Main.getTurn()==cross)? player1 : player2;
			turn_text.setText("Current turn: "+player);
		});
	}
	
	@FXML //needed to be recognized by fxml file
	private void fieldImageAction(MouseEvent event) {
		int[][] fieldMatrix = Main.getMatrix();
		int idrow = Character.getNumericValue(((Node) event.getSource()).getId().charAt(1));
		int idcolumn = Character.getNumericValue(((Node) event.getSource()).getId().charAt(2));
		int turn = Main.getTurn(); //0 means cross, 1 means circle
		//circle's turn (Player2)
		if(turn==circle && fieldMatrix[idrow][idcolumn]==0 && wincon==0) {
			Image image = new Image("file:tile_circle.png"); //sets the new image
			//gets the current image that is clicked on and performs the desired action on it
			((ImageView) event.getSource()).setImage(image);
			//update current turn indicator to next player
			turn_text.setText("Current turn: "+player1);
			wincon = Main.update(idrow, idcolumn, turn);
		}
		//cross' turn (Player1)
		else if(turn == cross && fieldMatrix[idrow][idcolumn]==0 && wincon==0) {
			Image image = new Image("file:tile_cross.png"); //sets the new image
			//gets the current image that is clicked on and performs the desired action on it
			((ImageView) event.getSource()).setImage(image);
			//update current turn indicator to next player
			turn_text.setText("Current turn: "+player2);
			wincon = Main.update(idrow, idcolumn, turn);
		}
		//check if game is done
		if(wincon==3) {
			turn_text.setText("Game over - Draw!");
		}
		else if(wincon==circle+1) {
			turn_text.setText("Congratulations, "+player2+" wins!");
		}
		else if(wincon==cross+1) {
			turn_text.setText("Congratulations, "+player1+" wins!");
		}
	}
	
	public void resetField(ActionEvent event) {
		Main.resetLogic();
		wincon = 0;
		turn_text.setText("Current turn: "+player1);
		Image resimg = new Image("file:tile.png");
		for(Node node : anchor.getChildren()) {
			if(node instanceof ImageView) {
				((ImageView) node).setImage(resimg);
			}
		}
	}
}
