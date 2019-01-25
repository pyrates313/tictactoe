package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.Parent;


public class Main extends Application {
	private static int fieldMatrix[][] = new int[3][3];
	private static int turn = 0;
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/Interface.fxml")); //loads the fxml file and creates the window
			Scene scene = new Scene(root,800,800);
			
			primaryStage.setTitle("Tic Tac Toe");
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public static int getTurn() {
		return turn;
	}
	
	public static int[][] getMatrix(){
		return fieldMatrix;
	}
	//0 means cross turn -> Matrix with 1
	//1 means circle turn -> Matrix with 2
	public static void update(int row, int column, int currentTurn) {
		//update turn and matrix	
		if(currentTurn==0) {
			turn = 1;
			fieldMatrix[row][column] = 1;
		}
		else if(currentTurn==1) {
			turn = 0;
			fieldMatrix[row][column] = 2;
		}
		//check if the game has ended
		if(gameDone()) {
			System.out.println("The game is over");
			//InterfaceController.resetField();
		}
	}
	
	public static boolean gameDone(){
		//check if Matrix is full
		boolean done = false;
		int zeroCount = 0;
		for(int i=0; i<fieldMatrix.length; i++){
			for (int j = 0; j < fieldMatrix[i].length; j++) {
				if (fieldMatrix[i][j] == 0) {
					zeroCount += 1;
				}
			}
		}
		
		if(zeroCount == 0) { //no 0's -> All fields have been filled out, game over
			done = true;
		}
		
		else { //check if a wincondition happened for either player
			int winner = checkWin();
			if(winner==1 || winner==2) {
				done = true;
			}
		}
		return done;
	}
	
	public static int checkWin() {
		for (int x = 1; x <= 2; x++) {
			for (int i = 0; i < fieldMatrix.length; i++) {
				//check if a column win happened
				if(fieldMatrix[i][0] == x && fieldMatrix[i][1] == x && fieldMatrix[i][2] == x) {
					return 1;
				}
				//check if a row win happened
				else if(fieldMatrix[0][i] == x && fieldMatrix[1][i] == x && fieldMatrix[2][i] == x) {
					return 1;
				}
			}
			//check if diagonal win happened
			if(fieldMatrix[0][0] == x && fieldMatrix[1][1] == x && fieldMatrix[2][2] == x) {
				return 1;
			}
			else if(fieldMatrix[0][2] == x && fieldMatrix[1][1] == x && fieldMatrix[2][0] == x) {
				return 1;
			}
		}
		
		//if no win happened
		return 0;
	}
	
	public static void resetLogic() {
		fieldMatrix = new int[3][3];
		turn=0;
	}
}
