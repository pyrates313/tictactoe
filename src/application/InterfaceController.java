package application;

import java.io.IOException;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class InterfaceController {
	private static final int cross = 0;
	private static final int circle = 1;
	private static final int pvp = 0;
	private static final int pvc = 1;
	private int wincon = 0;
	private String player1 = "PLAYER1";
	private String player2 = "PLAYER2";
	private String turnText = "Current turn:";
	private int gamemode = pvp;
	
	@FXML
	private ToggleButton b_easy;
	
	@FXML
	private ToggleButton b_medium;
	
	@FXML
	private ToggleButton b_hard;
	
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
		ToggleGroup group = new ToggleGroup();
		b_easy.setToggleGroup(group);
		b_medium.setToggleGroup(group);
		b_hard.setToggleGroup(group);
		p1_name.textProperty().addListener((obs, oldText, newText)->{
			if(newText.length()==0) {
				player1 = "PLAYER1";
			}
			else {
				player1 = newText;
			}
			String player = (Main.getTurn()==cross)? player1 : player2;
			updateTurnText(turnText, player);
			//turn_text.setText("Current turn: "+player);
		});
		p2_name.textProperty().addListener((obs, oldText, newText)->{
			if(newText.length()==0) {
				player2 = "PLAYER2";
			}
			else {
				player2 = newText;
			}
			String player = (Main.getTurn()==cross)? player1 : player2;
			updateTurnText(turnText, player);
			//turn_text.setText("Current turn: "+player);
		});
		b_easy.setVisible(false);
		b_medium.setVisible(false);
		b_hard.setVisible(false);
	}
	
	@FXML //needed to be recognized by fxml file
	public void fieldImageAction(MouseEvent event) {
		int[][] fieldMatrix = Main.getMatrix();
		int idrow = Character.getNumericValue(((Node) event.getSource()).getId().charAt(1));
		int idcolumn = Character.getNumericValue(((Node) event.getSource()).getId().charAt(2));
		int turn = Main.getTurn(); //0 means cross, 1 means circle
		//circle's turn (Player2)
		if(turn==circle && fieldMatrix[idrow][idcolumn]==0 && wincon==0 && gamemode==pvp) {
			Image image = new Image(this.getClass().getResourceAsStream("/res/tile_circle.png")); //sets the new image
			//gets the current image that is clicked on and performs the desired action on it
			((ImageView) event.getSource()).setImage(image);
			//update current turn indicator to next player
			turnText = "Current turn:";
			updateTurnText(turnText, player1);
			wincon = Main.update(idrow, idcolumn, turn);
		}

		//cross' turn (Player1)
		else if(turn == cross && fieldMatrix[idrow][idcolumn]==0 && wincon==0) {
			Image image = new Image(this.getClass().getResourceAsStream("/res/tile_cross.png")); //sets the new image
			//gets the current image that is clicked on and performs the desired action on it
			((ImageView) event.getSource()).setImage(image);
			//update current turn indicator to next player
			turnText = "Current turn:";
			updateTurnText(turnText, player2);
			wincon = Main.update(idrow, idcolumn, turn);
			//simulate computer move
			if(gamemode==pvc && wincon==0) {
				Image circleImage = new Image(this.getClass().getResourceAsStream("/res/tile_circle.png"));
				int[] x = Main.calculateTurn(idrow, idcolumn);
				String id = "i"+x[0]+x[1];
				for(Node node: anchor.getChildren()) {
					if(node instanceof ImageView) {
						if(node.getId().equals(id)) {
							((ImageView) node).setImage(circleImage);
						}
					}
				}
				Main.invertTurn();
				String player = (Main.getTurn()==cross)? player1 : player2;
				updateTurnText(turnText, player);
				if(Main.gameDone()) {
					wincon = Main.winner;
				}
			}
		}
		//check if game is done
		if(wincon==3) {
			turnText = "Game over - Draw!";
			updateTurnText(turnText, "");
		}
		else if(wincon==circle+1) {
			//turn inverted to undo turnchange of update function, so namechange is correct
			Main.invertTurn();
			turnText = "Congratulations, wins!";
			if(gamemode==pvp) {
				updateTurnText(turnText, player2);
			}
			else {
				updateTurnText("You lost, better luck next time!", "");
			}
		}
		else if(wincon==cross+1) {
			//turn inverted to undo turnchange of update function, so namechange is correct
			Main.invertTurn();
			turnText = "Congratulations, wins!";
			updateTurnText(turnText, player1);
		}
	}
	
	public void resetField(ActionEvent event) {
		Main.resetLogic();
		wincon = 0;
		turnText = "Current turn:";
		updateTurnText(turnText, player1);
		Image resimg = new Image(this.getClass().getResourceAsStream("/res/tile.png"));
		//Image resimg = new Image("file:src/application/tile.png");
		for(Node node : anchor.getChildren()) {
			if(node instanceof ImageView) {
				((ImageView) node).setImage(resimg);
			}
		}
	}
	
	private void updateTurnText(String text, String name) {
		/*function that handles all the text updates displaying who won, who is at turn etc.
		 * needed to process dynamic name updates.*/
		if(text.contains("Congratulations, wins")) {
			turn_text.setText("Congratulations, "+name+" wins!");
		}
		else if(text.contains("Game over - Draw!")) {
			turn_text.setText(text);
		}
		else {
			turn_text.setText(text+" "+name);
		}
	}
	
	public void setGamemodePvP(ActionEvent event) {
		//updates Gamemode to PvP and disables the difficulty buttons of PvC. Resets the current game
		gamemode = pvp;
		b_easy.setVisible(false);
		b_medium.setVisible(false);
		b_hard.setVisible(false);
		resetField(null);
	}
	
	public void setGamemodePvC(ActionEvent event) {
		/*updates Gamemode to PvC and enables the difficulty buttons. Resets the current game
		 * Also sets the default difficulty to easy.
		 */
		gamemode = pvc;
		b_easy.setVisible(true);
		b_medium.setVisible(true);
		b_hard.setVisible(true);
		difficulty_easy();
		b_easy.setSelected(true);
		resetField(null);
	}
	
	public void difficulty_easy() {
		Main.difficulty = 0;
		resetField(null);
	}
	
	public void difficulty_medium() {
		Main.difficulty = 1;
		resetField(null);
	}
	
	public void difficulty_hard() {
		Main.difficulty = 2;
		resetField(null);
	}
	
	public void about_window() {
		AboutController about = new AboutController();
		about.new_window();
	}
}
