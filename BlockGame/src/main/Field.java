package main;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JPanel;

import block.Block;
import block.Block2;
import block.Box;



public class Field {

	// boxが横に並べられる個数
	private int width;
	
	// boxが縦に積み上げられる個数
	private int height;

	// その位置がboxで埋まっていれば、そのboxを指す。そうでなければnull。
	private Box[][] f;
	
	
	// 下に落ちたブロックを管理するリスト
	private List<Block> falledBlocks = new ArrayList<>();
	
	
	// アクティブブロック(プレイヤーが動かせるブロック)
	private Block activeBlock = new Block2(1, 0, Color.PINK);

	
	/**
	 * 全体がnullで初期化される
	 * @param width
	 * @param height
	 */
	public Field(int width, int height) {
		this.width = width;
		this.height = height;
		
		f = new Box[height][width];
	}
	
	/**
	 * 絶対座標(x, y)のboxがnullかどうか調べるため。
	 * @param x
	 * @param y
	 * @return
	 */
	public Box get(int x, int y) {
		return f[y][x];
	}
	
	
	/**
	 * 幅のgetter
	 * @return
	 */
	public int getWidth() {
		return width;
	}


	/**
	 * 高さのgetter
	 * @return
	 */
	public int getHeight() {
		return height;
	}
	

	public List<Block> getFalledBlocks() {
		return falledBlocks;
	}

	public Block getActiveBlock() {
		return activeBlock;
	}

	public void setActiveBlock(Block activeBlock) {
		this.activeBlock = activeBlock;
	}

	/**
	 * ブロックがフィールドの範囲内にあればtrueを返す。
	 * @param block
	 * @return
	 */
	public boolean isIn(Block block) {
		return block.trueForAll((x, y) -> 0 <= x && x < width && 0 <= y && y < height);
	}

	
	/**
	 * blockの位置が他のブロックで埋まっていなければtrueを返す。
	 * @param block
	 * @return
	 */
	public boolean isVacant(Block block) {
		return block.trueForAll((x, y) -> f[y][x] == null || f[y][x].getParent() == block);
	}

	
	/**
	 * yで表される行がブロックでいっぱいであればtrueを返す。
	 * @param y
	 * @return
	 */
	public boolean isFilled(int y) {
		for (int x = 0; x < width; x++) {
			if (this.f[y][x] == null) {
	            return false;
	        }
	    }
	    return true;
	}

	
	/**
	 * ブロックをフィールドとfalledBlocksに登録する
	 * @param block
	 */
	public void register(Block block) {
		block.eachBox(box -> {
			int x = block.getX() + box.getX();
			int y = block.getY() + box.getY();
			f[y][x] = box;
		});
		
		falledBlocks.add(block);
	}

	/**
	 * ブロックをfalledBlocksから除く。
	 * それがうまくいけばフィールドからも消去する。
	 * @param block
	 * @return 除くことができればtrue。
	 */
	public boolean unregister(Block block) {
		boolean erased = falledBlocks.remove(block);
		
		if (erased) {
			block.eachBox(box -> {
				int x = block.getX() + box.getX();
				int y = block.getY() + box.getY();
				f[y][x] = null;
			});
		}
		
		return erased;
	}

	/**
	 * ブロックを消去する
	 * @return 消去した行があればtrue
	 */
	public boolean eraseAll(JPanel mainPanel) {
		// boxで埋まった行を抽出
		Set<Integer> yCoords = new HashSet<>();
		
		for (int y = height - 1; y > 0; y--) {
			if (isFilled(y)) {
				yCoords.add(y);
			}
		}
		
		// 消去する行が無い場合
		if (yCoords.isEmpty()) {
			return false;
		}
		
		// 消去対象のブロックの集合
		Set<Block> beforeBlocks = getBlocks(yCoords);
		
		// 消去後のブロックを構築
		List<Block> afterBlocks = new ArrayList<>();
		for (Block block : beforeBlocks) {
			List<Block> tmp = block.ifErased(yCoords);
			afterBlocks.addAll(tmp);
		}
		
		// 消去対象のブロックを、実際に消去
		for (Block block : beforeBlocks) {
			unregister(block);
		}
		
		// 消去された状態を一瞬報じするため
		mainPanel.repaint();
		try {
			Thread.sleep(200);
		} catch (InterruptedException ignore) {
		}
		
		// 消去後のブロックに差し替え
		for (Block block : afterBlocks) {
			register(block);
		}
		
		return true;
	}
	
	
	/**
	 * 指定した行に含まれるブロックの集合を得るメソッド
	 * @param yCoords
	 * @return
	 */
	public Set<Block> getBlocks(Set<Integer> yCoords) {
		Set<Block> ret = new HashSet<>();

		for (int i = 0; i < falledBlocks.size(); i++) {
			Block block = falledBlocks.get(i);
			if (block.contains(yCoords)) {
				ret.add(block);
			}
		}

		return ret;
	}
	
	
	/**
	 * ブロックをすべて落下させる
	 * @param mainPanel
	 */
	public void fallBlocks(JPanel mainPanel) {
		boolean  isContinue = true;
		
		while (isContinue) {
			boolean existFallenBlock = false;  //1つでもブロックを落とせたらtrueになる
			for (Block block : falledBlocks) {
				boolean fallable1 = block.isFallable(this);
						
				if (fallable1) {
					// フィールド上からブロック登録を消去
					block.eachBox(box -> {
						int x = block.getX() + box.getX();
						int y = block.getY() + box.getY();
						f[y][x] = null;
					});
					
					// ブロックを落下
					block.hardDrop(this);
					
					// 落下させた位置にブロックを登録
					block.eachBox(box -> {
						int x = block.getX() + box.getX();
						int y = block.getY() + box.getY();
						f[y][x] = box;
					});
				}
				existFallenBlock = existFallenBlock || fallable1;
			}
			if (!existFallenBlock) {
				isContinue = false;
			}
			mainPanel.repaint();
			try {
				Thread.sleep(10);
			} catch (InterruptedException ignore) {
			}
		}
	}

	/**
	 * デバッグ用。フィールドのブロックが積まれている様子を文字列で表示
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("--Field--\n");
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				sb.append(f[y][x] == null ? "□" : "■");
			}
			sb.append("\n");
		}
		sb.append("---------");
		return sb.toString();
	}
	
	
}
