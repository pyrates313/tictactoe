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
		
		if(currentTurn==0) {
			turn = 1;
			fieldMatrix[row][column] = 1;
		}
		else {
			turn = 0;
			fieldMatrix[row][column] = 2;
		}
	}
	
	public static void resetLogic() {
		fieldMatrix = new int[3][3];
	}
}
