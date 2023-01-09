package block;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import main.Field;
import util.Point;


/**
 * 1個のboxから成るブロック
 * [0]
 */
public class Block1 extends Block {
	
	public Block1(int x, int y, Color color) {
		super(x, y, new ArrayList<Box>());
		
		boxes.add(new Box(this, 0, 0, color));
	}
	
	/**
	 * 他のブロックが部分的に消去後、1個のブロックが必要になったときのため。
	 * @param x
	 * @param y
	 * @param color
	 * @param points このブロックに含まれるboxの相対座標のリスト(要素が1個であると想定)
	 */
	public Block1(int x, int y, Color color, List<Point<Integer>> points) {
		super(x, y, new ArrayList<Box>());
		
		for (Point<Integer> pt : points) {
			boxes.add(new Box(this, pt.x(), pt.y(), color));
		}
	}
	
	/**
	 * コピーコンストラクタ
	 * @param other
	 */
	public Block1(Block1 other) {
		super(other);
		
		this.x = other.x;
		this.y = other.y;
	}

	
	@Override
	public boolean rotateRight(Field field) {
		return true;
	}

	@Override
	public boolean rotateLeft(Field field) {
		return true;
	}
	
	
	/**
	 * 消去された位置に従って、分解後のブロックのリストを構築する
	 */
	@Override
	protected List<Block> getErased(Set<Integer> iBoxes) {
		List<Block> ret = new ArrayList<>();
		
		if (iBoxes.contains(0)) {
			;

		} else {
			ret.add(new Block1(this));
		}
		
		return ret;
	}
}
