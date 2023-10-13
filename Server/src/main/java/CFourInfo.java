import java.io.Serializable;
import javafx.util.Pair;
import java.util.ArrayList;

public class CFourInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	String name;
	Pair<Integer,Integer> move;
	boolean invalid, first, disconnected, tie;
	ArrayList<Pair<Integer,Integer>> four;
}