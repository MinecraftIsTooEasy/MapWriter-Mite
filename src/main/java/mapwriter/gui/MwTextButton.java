package mapwriter.gui;

public record MwTextButton(int id, String text, int x, int y, int width, int height) {
	
	public boolean over(int mx, int my) {
		return mx >= this.x && mx < this.x + this.width && my >= this.y && my < this.y + this.height;
	}
}
