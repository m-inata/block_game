package main;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import block.Block;


/**
 * ブロックが移動できる領域
 */
public class MainPanel extends JPanel {

	int w;
	int h;
	Field field;


	public MainPanel(int w, int h, Field field) {
		this.w = w;
		this.h = h;
		this.field = field;
		setBounds(0, 0, w, h);
	}

	
	/**
	 * 描画時に呼ばれる
	 */
	@Override
	public void paint(Graphics g) {
		
		Graphics2D g2 = (Graphics2D)g;
		
		g2.clearRect(0, 0, w, h);
		
		// 下に積まれているブロックの描画
		for (Block block : field.getFalledBlocks()) {
			block.draw(g2);
		}
		
		// アクティブブロックの描画
		Block activeBlock = field.getActiveBlock();
		activeBlock.draw(g2);
	}
}
