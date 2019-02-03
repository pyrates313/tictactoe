package application;

import java.util.ArrayList;
import java.util.List;

public class fieldPair {
	/*stores the x and y coordinates of the playing field as well as the numerical representatio of
	 * that field from 1 to 9, aka
	 * 1 2 3
	 * 4 5 6
	 * 7 8 9
	 * That value is accessible though the command getField()
	 */
	private final int x;
	private final int y;
	private int field;
	private int rotator = 0;
	private final List<Integer> corners = new ArrayList<Integer>();
	private final List<Integer> edges = new ArrayList<Integer>();
	
	public fieldPair(int x, int y) {
		corners.add(1);
		corners.add(3);
		corners.add(9);
		corners.add(7);
		edges.add(2);
		edges.add(6);
		edges.add(8);
		edges.add(4);
		this.x = x;
		this.y = y;
		this.field = (x*3)+y+1;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getField() {
		return field;
	}
	
	public int getRotator() {
		return rotator;
	}
	
	public int[] getCoordinates(int newField) {
		//maps the fieldvalue to actual Matrixcoordinates
		int[] coordinates = new int[2];
		if(newField<=3) {
			coordinates[0] = 0;
			if(newField == 1) {
				coordinates[1] = 0;
			}
			else {
				coordinates[1] = (newField == 2)? 1:2;
			}
		}
		else if(newField<=6){
			coordinates[0] = 1;
			if(newField == 4) {
				coordinates[1] = 0;
			}
			else {
				coordinates[1] = (newField == 5)? 1:2;
			}
		}
		else {
			coordinates[0] = 2;
			if(newField == 7) {
				coordinates[1] = 0;
			}
			else {
				coordinates[1] = (newField == 8)? 1:2;
			}
		}
		return coordinates;
	}
	
	public int rotateField() {
		int index = 0;
		/*rotates the entire playing field by 90°, as tactics are mirrored. 4 mirrors return the field to the original position.
		 * 1 2 3 -> 3 6 9
		 * 4 5 6 -> 2 5 8
		 * 7 8 9 -> 1 4 7
		 */
		if(field == 5) {
			return field;
		}
		else if(corners.contains(field)) {
			index = corners.indexOf(field);
			field = corners.get((index+1)%4);
		}
		else if(edges.contains(field)) {
			index = edges.indexOf(field);
			field = edges.get((index+1)%4);
		}
		rotator = (rotator+1)%4;
		return field;
	}
	
	public int rotateBack(int newField) {
		/*difference states how many rotations are needed in order to return to the original
		 * state. Then the function checks if it is a corner or edge and adds the difference
		 * to the index, getting the original value back for a specific newField.
		 */
		int index = 0;
		int difference = 4-rotator;
		//if not rotation has been performed, return original state
		if(rotator == 0 || newField == 5) {
			return newField;
		}
		else {
			if(corners.contains(newField)) {
				index = corners.indexOf(newField);
				newField = corners.get((index+difference)%4);
			}
			else if(edges.contains(newField)) {
				index = edges.indexOf(newField);
				newField = edges.get((index+difference)%4);
			}
			return newField;
		}
	}
}
