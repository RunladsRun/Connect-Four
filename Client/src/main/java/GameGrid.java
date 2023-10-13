import java.util.ArrayList;
import javafx.util.Pair;

public class GameGrid {
	private char[][] grid;
	
	public GameGrid() {
		this.grid = new char[][] { 	{'-','-','-','-','-','-','-'},
									{'-','-','-','-','-','-','-'},
									{'-','-','-','-','-','-','-'},
									{'-','-','-','-','-','-','-'},
									{'-','-','-','-','-','-','-'},
									{'-','-','-','-','-','-','-'} };
	}
	
	public boolean vaildGrid(int row, int col) {
		
		if(this.grid[row][col] != '-') {
			return false;
		}
		if(row == 5 || this.grid[row+1][col] != '-') {
			return true;
		} else {
			return false;
		}
	}
	
	public void setGrid(int row, int col, char c) {
		this.grid[row][col] = c;
	}
	
	public ArrayList<Pair<Integer,Integer>> checkH(int row, int col) {
		ArrayList<Pair<Integer,Integer>> arr = new ArrayList<Pair<Integer,Integer>>();
		arr.add(new Pair<>(row,col));
		char val = grid[row][col];
		int c = col+1;
		while(c <= 6 && val == grid[row][c]) {
			arr.add(new Pair<>(row, c));
			c++;
		}
		c = col-1;
		while(c >= 0 && val == grid[row][c]) {
			arr.add(new Pair<>(row, c));
			c--;
		}
		return arr;
	}
	
	public ArrayList<Pair<Integer,Integer>> checkV(int row, int col) {
		ArrayList<Pair<Integer,Integer>> arr = new ArrayList<Pair<Integer,Integer>>();
		arr.add(new Pair<>(row,col));
		char val = grid[row][col];
		int r = row+1;
		while(r <= 5 && val == grid[r][col]) {
			arr.add(new Pair<>(r, col));
			r++;
		}
		r = row-1;
		while(r >= 0 && val == grid[r][col]) {
			arr.add(new Pair<>(r, col));
			r--;
		}
		return arr;
	}
	
	public ArrayList<Pair<Integer,Integer>> checkD1(int row, int col) {
		ArrayList<Pair<Integer,Integer>> arr = new ArrayList<Pair<Integer,Integer>>();
		arr.add(new Pair<>(row,col));
		char val = grid[row][col];
		int r = row+1;
		int c = col+1;
		while(r <= 5 && c <= 6 && val == grid[r][c]) {
			arr.add(new Pair<>(r, c));
			r++;
			c++;
		}
		r = row-1;
		c = col-1;
		while(r >= 0 && c >= 0 && val == grid[r][c]) {
			arr.add(new Pair<>(r, c));
			r--;
			c--;
		}
		return arr;
	}
	
	public ArrayList<Pair<Integer,Integer>> checkD2(int row, int col) {
		ArrayList<Pair<Integer,Integer>> arr = new ArrayList<Pair<Integer,Integer>>();
		arr.add(new Pair<>(row,col));
		char val = grid[row][col];
		int r = row+1;
		int c = col-1;
		while(r <= 5 && c >= 0 && val == grid[r][c]) {
			arr.add(new Pair<>(r, c));
			r++;
			c--;
		}
		r = row-1;
		c = col+1;
		while(r >= 0 && c <= 6 && val == grid[r][c]) {
			arr.add(new Pair<>(r, c));
			r--;
			c++;
		}
		return arr;
	}
	
	public ArrayList<Pair<Integer,Integer>> checkWinning(int row, int col) {
		ArrayList<Pair<Integer,Integer>> arr = new ArrayList<Pair<Integer,Integer>>();
		
		arr = checkH(row, col);
		if(arr.size() == 4) {
			return arr;
		}
		
		arr = checkV(row, col);
		if(arr.size() == 4) {
			return arr;
		}
		
		arr = checkD1(row, col);
		if(arr.size() == 4) {
			return arr;
		}
		
		arr = checkD2(row, col);
		if(arr.size() == 4) {
			return arr;
		}
		
		return arr;
	}
	
	public boolean checkTie() {
		for(int i = 0; i < 6; i++) {
			for (int j = 0; j < 7; j++) {
				if(this.grid[i][j] == '-') {
					return false;
				}
			}
		}
		return true;
	}
	
}
















