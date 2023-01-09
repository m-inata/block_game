package main;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JPanel;

import block.Block;
import block.Block1;
import block.Block2;
import block.BlockBar;
import block.BlockBar3;
import block.BlockL;
import block.BlockL3;
import block.BlockSq;

public class Main {
	// boxの一辺の長さ
	public static final int BOX_SIZE = 30;
	
	// フィールドにboxを横に並べられる個数
	private static final int FIELD_WIDTH = 10;
	
	// フィールドにboxを縦に積み上げられる個数
	private static final int FIELD_HEIGHT = 20;
	
	// タイマーの最大値
	private static int MAX_COUNT = 50;
	
	// 乱数の種
	private static int RANDOM_SEED = 0;
	
	// ブロックの初期x座標
	private static int INIT_X = 4;
	
	// ブロックの初期y座標
	private static int INIT_Y = 0;	

	// フィールドを描画するときの横幅と縦の長さ
	private static Dimension mainPanelSize = new Dimension(FIELD_WIDTH * BOX_SIZE, FIELD_HEIGHT * BOX_SIZE);

	
	// フィールド
	private static Field field;
	
	// タイマーのカウンター
	private static int counter = MAX_COUNT;
	
	// 乱数発生器
	private static Random random = new Random(RANDOM_SEED);

	
	
	/**
	 * 新しいブロックを作るメソッド
	 * @return
	 */
	public static Block createBlock() {
		Block ret = null;
		int n = random.nextInt(7);
		
		if (n == 0) {
			ret = new Block1(INIT_X, INIT_Y, Color.BLUE);
			
		} else if (n == 1) {
			ret = new Block2(INIT_X, INIT_Y, Color.PINK);

		} else if (n == 2) {
			ret = new BlockBar3(INIT_X, INIT_Y, Color.GRAY);

		} else if (n == 3) {
			ret = new BlockL3(INIT_X, INIT_Y, Color.ORANGE);

		} else if (n == 4) {
			ret = new BlockBar(INIT_X, INIT_Y, Color.CYAN);

		} else if (n == 5) {
			ret = new BlockSq(INIT_X, INIT_Y, Color.RED);

		} else if (n == 6) {
			ret = new BlockL(INIT_X, INIT_Y, Color.YELLOW);
		}
		
		return ret;
	}
	
	
	/**
	 * アプリケーションのエントリーポイント
	 * @param args
	 */
	public static void main(String[] args) {

		field = new Field(FIELD_WIDTH, FIELD_HEIGHT);
				
		//-----------------------------
		// ウインドウの構築
		//-----------------------------
		JFrame frame = new JFrame("Block Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        int w = mainPanelSize.width;
        int h = mainPanelSize.height;
        frame.setSize(w + 20, h + 50);
        final JPanel mainPanel = new MainPanel(w, h, field);
        frame.add(mainPanel);
        frame.setResizable(true);
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
        
        
        //-----------------------------
        // 入力された時の処理設定
        //-----------------------------
        InputManager input = new InputManager(mainPanel);
        input.setOnFall(() -> {
        	System.out.println("↓が押された");
        	Block activeBlock = field.getActiveBlock();
        	activeBlock.moveDown(field);
        	mainPanel.repaint();
        });
        
        input.setOnLeft(() -> {
        	System.out.println("←が押された");
        	Block activeBlock = field.getActiveBlock();
        	activeBlock.moveLeft(field);
        	mainPanel.repaint();
        });
        
        input.setOnRight(() -> {
        	System.out.println("→が押された");
        	Block activeBlock = field.getActiveBlock();
        	activeBlock.moveRight(field);
        	mainPanel.repaint();
        });
        input.setOnHardDrop(() -> {
        	System.out.println("↑が押された");
        	Block activeBlock = field.getActiveBlock();
        	activeBlock.hardDrop(field);
        	mainPanel.repaint();
        });
        
        input.setOnRotateLeft(() -> {
        	System.out.println("Zが押された");
        	Block activeBlock = field.getActiveBlock();
        	activeBlock.rotateLeft(field);
        	mainPanel.repaint();
        });
        
        input.setOnRotateRight(() -> {
        	System.out.println("Xが押された");
        	Block activeBlock = field.getActiveBlock();
        	activeBlock.rotateRight(field);
        	mainPanel.repaint();
        });
        
        
		//-----------------------------
        // 時間経過毎のタスク
		//-----------------------------
        final Timer timer = new Timer();
        
        timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if (counter == 0) {
					Block activeBlock = field.getActiveBlock();
					boolean fell = activeBlock.moveDown(field);
					// これ以上ブロックを落とせなかった場合
					if (!fell) {
						// ブロックをフィールドに固定
						field.register(activeBlock);
						
						int ren = 0;  // 連鎖の回数
						while (true) {
							// 揃った行を削除
							boolean erased = field.eraseAll(mainPanel);
							System.out.println(field);

							// ブロックを落とす
							if (erased) {
								field.fallBlocks(mainPanel);
								ren++;
								System.out.println(field);
							} else {
								break;
							}
						}
						
						// 新しいブロックを生成してアクティブブロックとする
						field.setActiveBlock(createBlock());
						
						// 再度描画
						mainPanel.repaint();
					}
					// ブロックを落とせた場合はカウンターをリセット
					else {
						counter = MAX_COUNT;
					}
					
					mainPanel.repaint();
					
				} else {
					counter--;
				}
			}
        	
        }, 1000, 20);
	}

}
