package application;
	
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.Parent;


public class Main extends Application {
	private static int fieldMatrix[][] = new int[3][3];
	private static int turn = 0;
	private static final int cross = 0;
	private static final int circle = 1;
	public static int winner = 0;
	public static int difficulty = 0;
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
	
	public static void invertTurn() {
		//changes the current active player to the other, fix for turn after win
		turn = (turn==cross)? circle : cross;
	}
	
	public static int[][] getMatrix(){
		return fieldMatrix;
	}
	//0 means cross turn -> Matrix with 1
	//1 means circle turn -> Matrix with 2
	public static int update(int row, int column, int currentTurn) {
		//update turn and matrix	
		if(currentTurn==cross) {
			turn = circle;
			fieldMatrix[row][column] = 1;
		}
		else if(currentTurn==circle) {
			turn = cross;
			fieldMatrix[row][column] = 2;
		}
		//check if the game has ended
		if(gameDone()) {
			//whenever a game is done, can return 1 for p1 wins, 2 for p2 wins, 3 for field full - noone wins
			return winner;
			
		}
		//standard return if game is not done
		return 0;
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
			winner = checkWin();
			done = true;
		}
		
		else { //check if a wincondition happened for either player
			winner = checkWin();
			if(winner==1 || winner==2) {
				done = true;
			}
		}
		return done;
	}
	
	public static int checkWin() {
		//returns 0 if noone won, game not done yet, 1 if player1 wins and 2 if player2
		for (int x = 1; x <= 2; x++) {
			for (int i = 0; i < fieldMatrix.length; i++) {
				//check if a column win happened
				if(fieldMatrix[i][0] == x && fieldMatrix[i][1] == x && fieldMatrix[i][2] == x) {
					return x;
				}
				//check if a row win happened
				else if(fieldMatrix[0][i] == x && fieldMatrix[1][i] == x && fieldMatrix[2][i] == x) {
					return x;
				}
			}
			//check if diagonal win happened
			if(fieldMatrix[0][0] == x && fieldMatrix[1][1] == x && fieldMatrix[2][2] == x) {
				return x;
			}
			else if(fieldMatrix[0][2] == x && fieldMatrix[1][1] == x && fieldMatrix[2][0] == x) {
				return x;
			}
		}
		
		//if no win happened
		return 3;
	}
	
	public static void resetLogic() {
		fieldMatrix = new int[3][3];
		turn = 0;
		winner = 0;
	}
	
	public static int[] calculateTurn() {
		/*calculates the computers turn. Easy will check if the computer (circle) or the player is on an immediate win
		 * (2 out of 3 placed) and block that, or just complete to win itself.
		 * Hard uses the mathematical strategies to deliver an optimal game without any random placements.
		 */
		int[] randompair = new int[2];
		//easy difficulty
		if(difficulty == 0) {
			//pairs of enemy
			List<Integer> pairs = getPairs(cross+1);
			List<Integer> circlePairs = getPairs(circle+1);
			//prints the possible winpositions present
			//System.out.println(Arrays.toString(pairs.toArray()));
			//check if computer could win, if so assign those values
			if(circlePairs.size()>0) {
				randompair[0] = circlePairs.get(0);
				randompair[1] = circlePairs.get(1);
			}
			else if(pairs.size()>0) {
				//assigns the first wincondition to our pair that is placed by the computer
				randompair[0] = pairs.get(0);
				randompair[1] = pairs.get(1);
			}
			else {
				//if no enemy turn is to be blocked, the computer just calculates a random postition to fill
				randompair = calculateRandom();
			}
		}
		//hard difficulty
		if(difficulty == 1) {
			
		}
		//updating Matrix and returning the coordinates
		fieldMatrix[randompair[0]][randompair[1]] = 2;
		return randompair;
	}
	//calculates a random value pair that is used as the coordinates of the computers turn
	//if field is already filled, move to the right until empty/start new row
	public static int[] calculateRandom() {
		int counter = 0;
		int rowcounter = 0;
		Random random = new Random();
		int[] randompair = new int[2];
		randompair[0] = random.nextInt(3);
		randompair[1] = random.nextInt(3);
		//counter to prevent infinite loop when error would occur
		while(fieldMatrix[randompair[0]][randompair[1]]!=0 && counter <10) {
			if(rowcounter == 3) {
				rowcounter = 0;
				randompair[1] = (randompair[1]+1)%3;
			}
			else {
				randompair[0] = (randompair[0]+1)%3;
			}
			rowcounter++;
			counter++;
		}
		if(fieldMatrix[randompair[0]][randompair[1]]!=0) {
			throw new IllegalArgumentException("FieldMatrix must have empty spaces!");
		}
		return randompair;
	}
	
	public static List<Integer> getPairs(int type) {
		/*Pairs are if in a given row/column/diagonal 2 out of the 3 fields are occupied by the same type and the third is empty,
		 * making it a potential win for the next turn. This function returns the position of the open field that is
		 * critical for the win.
		 */
		List<Integer> pairs = new ArrayList<>();
		int[] zeroPosition = {-1,-1};
		int counter = 0;
		//check all rows for potential winpairs
		for (int i = 0; i < fieldMatrix.length; i++) {
			for (int j = 0; j < fieldMatrix.length; j++) {
				if(fieldMatrix[i][j]==type) {
					counter++;
				}
				else if(fieldMatrix[i][j]==0) {
					zeroPosition[0]=i;
					zeroPosition[1]=j;
				}
			}
			if(counter==2 && zeroPosition[0]!=-1) {
				for (int k = 0; k < 2; k++) {
					pairs.add(zeroPosition[k]);
				}
			}
			//reset pairspecific variables
			counter = 0;
			zeroPosition[0] = -1;
			zeroPosition[1] = -1;
		}
		//check all columns for potential winpairs
		for (int j = 0; j < fieldMatrix.length; j++) {
			for (int i = 0; i < fieldMatrix.length; i++) {
				if(fieldMatrix[i][j]==type) {
					counter++;
				}
				else if(fieldMatrix[i][j]==0) {
					zeroPosition[0]=i;
					zeroPosition[1]=j;
				}
			}
			if(counter==2 && zeroPosition[0]!=-1) {
				for (int k = 0; k < 2; k++) {
					pairs.add(zeroPosition[k]);
				}
			}
			//reset pairspecific variables
			counter = 0;
			zeroPosition[0] = -1;
			zeroPosition[1] = -1;
		}
		//test for potential diagonal downright winpair
		for (int i = 0; i < fieldMatrix.length; i++) {
			if(fieldMatrix[i][i]==type) {
				counter++;
			}
			else if(fieldMatrix[i][i]==0) {
				zeroPosition[0]=i;
				zeroPosition[1]=i;
			}
		}
		if(counter==2 && zeroPosition[0]!=-1) {
			for (int k = 0; k < 2; k++) {
				pairs.add(zeroPosition[k]);
			}
		}
		//reset pairspecific variables
		counter = 0;
		zeroPosition[0] = -1;
		zeroPosition[1] = -1;
		//test for potential diagonal upright winpair
		for (int i = 0; i < fieldMatrix.length; i++) {
			if(fieldMatrix[i][fieldMatrix.length-1-i]==type) {
				counter++;
			}
			else if(fieldMatrix[i][fieldMatrix.length-1-i]==0) {
				zeroPosition[0]=i;
				zeroPosition[1]=fieldMatrix.length-1-i;
			}
		}
		if(counter==2 && zeroPosition[0]!=-1) {
			for (int k = 0; k < 2; k++) {
				pairs.add(zeroPosition[k]);
			}
		}
		//reset pairspecific variables
		counter = 0;
		zeroPosition[0] = -1;
		zeroPosition[1] = -1;
		
		return pairs;
	}
}
