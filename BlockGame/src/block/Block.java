package block;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

import main.Field;


/**
 * ブロックを表すクラス
 * これの子クラスを定義して使う
 */
public abstract class Block {
	// 回転の中心のx
	protected int x;
	
	// 回転の中心のy
	protected int y;
	
	// このブロックに含まれるbox
	protected ArrayList<Box> boxes;
	
	/**
	 * コピーコンストラクタ。子クラスのコピーコンストラクタでこれに委譲すること。
	 * @param other
	 */
	protected Block(Block other) {
		boxes = new ArrayList<>();
		for (Box b : other.boxes) {
			boxes.add(new Box(this, b.getX(), b.getY(), b.getColor()));
		}
	}
	
	/**
	 * 子クラスのコンストラクタでboxの位置を決定すること。
	 * @param x
	 * @param y
	 * @param boxes
	 */
	protected Block(int x, int y, ArrayList<Box> boxes) {
		this.x = x;
		this.y = y;
		this.boxes = boxes;
	}
	
	
	/**
	 * 回転の中心のxのgetter
	 * @return
	 */
	public int getX() {
		return x;
	}


	/**
	 * 回転の中心のyのgetter
	 * @return
	 */
	public int getY() {
		return y;
	}


	/**
	 * ブロックの描画
	 * @param g2
	 * @param field
	 */
	public void draw(Graphics2D g2) {
		for (Box b : boxes) {
			b.draw(g2, x, y);
		}
	}

	
	/**
	 * すべてのboxについての絶対座標をfnに適用する。
	 * @param fn
	 */
	public void eachBoxPosition(BiConsumer<Integer, Integer> fn) {
		for (Box b : boxes) {
			int xx = x + b.getX();
			int yy = y + b.getY();
			fn.accept(xx, yy);
		}
	}
	
	/**
	 * すべてのboxについてfnを適用する。
	 * @param fn
	 */
	public void eachBox(Consumer<Box> fn) {
		for (Box b : boxes) {
			fn.accept(b);
		}
	}
	
	/**
	 * すべてのboxについてpredがtrueであれば、trueを返す。
	 * @param pred
	 * @return
	 */
	public boolean trueForAll(BiPredicate<Integer, Integer> pred) {
		boolean trueAll = true;
		for (Box b : boxes) {
			int xx = x + b.getX();
			int yy = y + b.getY();
			trueAll = trueAll && pred.test(xx, yy);
		}
		return trueAll;
	}
	
	
	
	/**
	 * yCoordsの行が消去された場合の残りのブロックを生成して返す。
	 * @param yCoords
	 * @return
	 */
	public List<Block> ifErased(Set<Integer> yCoords) {
		Set<Integer> indices = getBoxIndices(yCoords);
		return getErased(indices);
	}
	
	/**
	 * 消去対象となるboxのインデックスのリストを返す。
	 * @param yCoords
	 * @return
	 */
	private Set<Integer> getBoxIndices(Set<Integer> yCoords) {
		Set<Integer> ret = new HashSet<>();
		
		for (int yy : yCoords) {
			for (int i = 0; i < boxes.size(); i++) {
				int y0 = this.y + boxes.get(i).getY();
				if (y0 == yy) {
					ret.add(i);
				}
			}
		}
		
		return ret;
	}

	/**
	 * 消去された位置に従って、分解後のブロックのリストを構築する
	 */
	protected abstract List<Block> getErased(Set<Integer> iBoxes);
	
	
	/**
	 * このブロックが指定されたいずれかの行に存在するならtrue
	 * @param yCoords
	 * @return
	 */
	public boolean contains(Set<Integer> yCoords) {
		
		for (int yy : yCoords) {
			for (int i = 0; i < boxes.size(); i++) {
				Box b = boxes.get(i);
				if (yy == y + b.getY()) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * 1つ右に移動させる。
	 * 移動させることができればtrueを返す。
	 * @param field
	 * @return
	 */
	public boolean moveRight(Field field) {
		x++;
	    if (!field.isIn(this) || !field.isVacant(this)) {
	        x--;
	        return false;
	    }
	    return true;
	}
	
	/**
	 * 1つ左に移動させる。
	 * 移動させることができればtrueを返す。
	 * @param field
	 * @return
	 */
	public boolean moveLeft(Field field) {
		this.x--;
	    if (!field.isIn(this) || !field.isVacant(this)) {
	        this.x++;
	        return false;
	    }
	    return true;
	}
	
	/**
	 * 1つ上に移動させる。
	 * 移動させることができればtrueを返す。
	 * @param field
	 * @return
	 */
	public boolean moveUp(Field field) {
		this.y--;
	    if (!field.isIn(this) || !field.isVacant(this)) {
	        this.y++;
	        return false;
	    }
	    return true;
	}
	
	/**
	 * 1つ下に移動させる。
	 * 移動させることができればtrueを返す。
	 * @param field
	 * @return
	 */
	public boolean moveDown(Field field) {
		this.y++;
	    if (!field.isIn(this) || !field.isVacant(this)) {
	        this.y--;
	        return false;
	    }
	    return true;
	}
	
	public boolean isFallable(Field field) {
		this.y++;
	    if (!field.isIn(this) || !field.isVacant(this)) {
	        this.y--;
	        return false;
	    }
	    this.y--;
	    return true;
	}

	/**
	 * 一気に下まで落とす。
	 * @param field
	 * @return 移動させることができた場合true
	 */
	public boolean hardDrop(Field field) {
		int fallCount = 0;
		while (true) {
			boolean fallen = moveDown(field);
			if (fallen) {
				fallCount++;
			} else {
				break;
			}
		}
		return fallCount != 0;
	}
	
	/**
	 * 右回転のデフォルト実装。
	 * 必要であれば子クラスでオーバーライドすること。
	 * @param field
	 * @return
	 */
	public boolean rotateRight(Field field) {
		for (Box b : boxes) {
			b.rotateRight();
		}

		if (!field.isIn(this) || !field.isVacant(this)) {
			for (Box b : boxes) {
				b.rotateLeft();
			}
			return false;
		}

		return true;

	}
	
	/**
	 * 左回転のデフォルト実装。
	 * 必要であれば子クラスでオーバーライドすること。
	 * @param field
	 * @return
	 */
	public boolean rotateLeft(Field field) {
		for (Box b : boxes) {
			b.rotateLeft();
		}

		if (!field.isIn(this) || !field.isVacant(this)) {
			for (Box b : boxes) {
				b.rotateRight();
			}
			return false;
		}

		return true;
	}


	@Override
	public String toString() {
		return "Block [x=" + x + ", y=" + y + ", boxes=" + boxes + "]";
	}
}
