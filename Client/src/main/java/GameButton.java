import javafx.scene.control.Button;

public class GameButton extends Button {
	int row;
	int col;
	
	public GameButton(int r, int c) {
		this.row = r;
		this.col = c;
		this.setStyle("-fx-background-color: GAINSBORO; "
				+ "-fx-background-radius: 40px;");
		
		this.setMinSize(80, 80);
	}
	
	public void clicked(boolean first) {
		if(first) {
			this.setStyle("-fx-background-color: red; "
					+ "-fx-background-radius: 40px;");
		} else {
			this.setStyle("-fx-background-color: yellow; "
					+ "-fx-background-radius: 40px;");
		}
	}
	
	public void unclicked() {
		this.setStyle("-fx-background-color: GAINSBORO; "
				+ "-fx-background-radius: 40px;");
	}
	
	public void setWinning(boolean first) {
		if(first) {
			this.setStyle("-fx-background-color: red; "
					+ "-fx-background-radius: 40px;"
					+ "-fx-border-color: AQUA; "
					+ "-fx-border-radius: 40px;"
					+ "-fx-border-width: 7");
		} else {
			this.setStyle("-fx-background-color: yellow; "
					+ "-fx-background-radius: 40px;"
					+ "-fx-border-color: AQUA; "
					+ "-fx-border-radius: 40px;"
					+ "-fx-border-width: 7");
		}
		
	}
	
	public int getRow() {
		return this.row;
	}
	
	public int getCol() {
		return this.col;
	}
}