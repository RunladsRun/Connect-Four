import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.api.BeforeEach;
import javafx.util.Pair;
import java.util.ArrayList;

class GameGridTest {
	
	GameGrid grid;
	
	@BeforeEach
	void setEmptyGrid() {
		grid = new GameGrid();
	}
	
	@ParameterizedTest
	@ValueSource(ints = {0,1,2,3,4,5,6})
	void vaildTest(int col) {
		assertTrue(grid.vaildGrid(5,col), "Wrong value");
	}
	
	@ParameterizedTest
	@ValueSource(ints = {0,1,2,3,4,5,6})
	void vaildTest2(int col) {
		for(int i = 0; i < 7; i++) {
			grid.setGrid(5, i, 'O');
		}
		assertTrue(grid.vaildGrid(4,col), "Wrong value");
	}
	
	@ParameterizedTest
	@ValueSource(ints = {0,1,2,3,4,5,6})
	void vaildTest3(int col) {
		assertFalse(grid.vaildGrid(0,col), "Wrong value");
	}
	
	@ParameterizedTest
	@ValueSource(ints = {0,1,2,3,4,5,6})
	void checkHTest(int index) {
		ArrayList<Pair<Integer,Integer>> arr;
		arr = grid.checkH(0,index);
		assertEquals(7, arr.size(), "Wrong value");
	}
	
	@ParameterizedTest
	@ValueSource(ints = {2,3,4})
	void checkHTest2(int index) {
		for(int i = 2; i < 5;i++) {
			grid.setGrid(3, i, 'O');
		}
		ArrayList<Pair<Integer,Integer>> arr;
		arr = grid.checkH(3,index);
		assertEquals(3, arr.size(), "Wrong value");
	}
	
	@ParameterizedTest
	@ValueSource(ints = {0,1,2,3,4,5})
	void checkHTest3(int index) {
		for(int i = 0; i < 6;i++) {
			grid.setGrid(5, i, 'O');
		}
		ArrayList<Pair<Integer,Integer>> arr;
		arr = grid.checkH(5,index);
		assertEquals(6, arr.size(), "Wrong value");
	}
	
	@Test
	void checkHTest4() {
		for(int i = 2; i < 7;i++) {
			grid.setGrid(4, i, 'O');
		}
		ArrayList<Pair<Integer,Integer>> arr;
		arr = grid.checkH(4,0);
		assertEquals(2, arr.size(), "Wrong value");
	}
	
	
	@ParameterizedTest
	@ValueSource(ints = {0,1,2,3,4,5})
	void checkVTest(int index) {
		ArrayList<Pair<Integer,Integer>> arr;
		arr = grid.checkV(index,0);
		assertEquals(6, arr.size(), "Wrong value");
	}
	
	@ParameterizedTest
	@ValueSource(ints = {2,3,4,5})
	void checkVTest2(int index) {
		for(int i = 2; i < 6;i++) {
			grid.setGrid(i, 4, 'O');
		}
		ArrayList<Pair<Integer,Integer>> arr;
		arr = grid.checkV(index,4);
		assertEquals(4, arr.size(), "Wrong value");
	}
	
	@Test
	void checkVTest3() {
		grid.setGrid(4, 6, 'O');
		ArrayList<Pair<Integer,Integer>> arr;
		arr = grid.checkV(1,6);
		assertEquals(4, arr.size(), "Wrong value");
	}
	
	@Test
	void checkD1Test() {
		ArrayList<Pair<Integer,Integer>> arr;
		arr = grid.checkD1(0,0);
		assertEquals(6, arr.size(), "Wrong value");
	}
	
	@Test
	void checkD1Test2() {
		ArrayList<Pair<Integer,Integer>> arr;
		arr = grid.checkD1(0,2);
		assertEquals(5, arr.size(), "Wrong value");
	}
	
	@Test
	void checkD1Test3() {
		ArrayList<Pair<Integer,Integer>> arr;
		arr = grid.checkD1(4,2);
		assertEquals(4, arr.size(), "Wrong value");
	}
	
	@ParameterizedTest
	@ValueSource(ints = {2,3,4,5})
	void checkD1Test4(int index) {
		for(int i = 2; i < 6;i++) {
			grid.setGrid(i, i, 'O');
		}
		ArrayList<Pair<Integer,Integer>> arr;
		arr = grid.checkD1(index,index);
		assertEquals(4, arr.size(), "Wrong value");
	}
	
	@Test
	void checkD2Test() {
		ArrayList<Pair<Integer,Integer>> arr;
		arr = grid.checkD2(0,0);
		assertEquals(1, arr.size(), "Wrong value");
	}
	
	@Test
	void checkD2Test2() {
		ArrayList<Pair<Integer,Integer>> arr;
		arr = grid.checkD2(4,5);
		assertEquals(3, arr.size(), "Wrong value");
	}
	
	@Test
	void checkD2Test3() {
		ArrayList<Pair<Integer,Integer>> arr;
		arr = grid.checkD2(1,6);
		assertEquals(5, arr.size(), "Wrong value");
	}
	
	@ParameterizedTest
	@ValueSource(ints = {0,1,2,3,4,5})
	void checkD2Test4(int index) {
		grid.setGrid(5, 1, 'O');
		grid.setGrid(4, 2, 'O');
		grid.setGrid(3, 3, 'O');
		ArrayList<Pair<Integer,Integer>> arr;
		arr = grid.checkD2(index,6-index);
		assertEquals(3, arr.size(), "Wrong value");
	}
	
	@Test
	void checkTieTest1() {
		assertFalse(grid.checkTie(), "Wrong value");
	}
	
	@Test
	void checkTieTest2() {
		grid.setGrid(0, 0, 'O');
		grid.setGrid(5, 1, 'O');
		grid.setGrid(3, 4, 'O');
		assertFalse(grid.checkTie(), "Wrong value");
	}
	
	@Test
	void checkTieTest3() {
		for(int i = 0; i < 6; i ++) {
			for (int j = 0; j < 7; j++) {
				grid.setGrid(i, j, 'O');
			}
		}
		assertTrue(grid.checkTie(), "Wrong value");
	}

}