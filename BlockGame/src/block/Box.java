package block;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import main.Main;

public class Box {
	// boxの縦横サイズ。描画時に必要。
	private final static Dimension boxSize = new Dimension(Main.BOX_SIZE, Main.BOX_SIZE);

	// このboxを含むブロック
	private Block parent;
	
	// boxの相対座標x
	private int x;
	
	// boxの相対座標y
	private int y;
	
	// boxの色
	private Color color;
	
	/**
	 * テスト用。blockなしで使うときのため。
	 * @param x
	 * @param y
	 * @param color
	 */
	public Box(int x, int y, Color color) {
		this.x = x;
		this.y = y;
		this.color = color;
	}
	
	public Box(Block parent, int x, int y, Color color) {
		this.parent = parent;
		this.x = x;
		this.y = y;
		this.color = color;
	}
	
	public Block getParent() {
		return parent;
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Color getColor() {
		return color;
	}
	
	public void rotateRight() {
		int tmp = x;
		x = -y;
		y = tmp;
	}
	
	public void rotateLeft() {
		int tmp = x;
		x = y;
		y = -tmp;
	}
	
	/**
	 * 描画のため
	 * @param g2
	 * @param centerX
	 * @param centerY
	 */
	public void draw(Graphics2D g2, int centerX, int centerY) {
		double w = boxSize.getWidth();
		double h = boxSize.getHeight();
		int xx = centerX + getX();
		int yy = centerY + getY();
		g2.setPaint(getColor());
		g2.fill(new Rectangle2D.Double(xx * w, yy * h, w, h));
		g2.setPaint(Color.BLACK);
		g2.draw(new Rectangle2D.Double(xx * w, yy * h, w, h));
	}
	

	/**
	 * デバッグ用。boxの相対座標を表示。
	 */
	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}
}
