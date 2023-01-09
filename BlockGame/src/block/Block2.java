package block;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import util.Point;


/**
 * 隣接する二つのboxから構成されるブロック
 * [0][1]
 */
public class Block2 extends Block {

	public Block2(int x, int y, Color color) {
		super(x, y, new ArrayList<Box>());
		
		boxes.add(new Box(this, 0, 0, color));
		boxes.add(new Box(this, 1, 0, color));
	}
	
	/**
	 * 他のブロックが部分的に消去後、1個のブロックが必要になったときのため。
	 * @param x
	 * @param y
	 * @param color
	 * @param points このブロックに含まれるboxの相対座標のリスト(要素が2個であると想定)
	 */
	public Block2(int x, int y, Color color, List<Point<Integer>> points) {
		super(x, y, new ArrayList<Box>());
		
		for (Point<Integer> pt : points) {
			boxes.add(new Box(this, pt.x(), pt.y(), color));
		}
	}

	/**
	 * コピーコンストラクタ
	 * @param other
	 */
	public Block2(Block2 other) {
		super(other);
		
		this.x = other.x;
		this.y = other.y;
	}


	/**
	 * 消去された位置に従って、分解後のブロックのリストを構築する
	 */
	@Override
	protected List<Block> getErased(Set<Integer> iBoxes) {
		List<Block> ret = new ArrayList<>();
				
		if (iBoxes.containsAll(Set.of(0, 1))) {
			;
			
		} else if (iBoxes.contains(0)) {
			Box b = boxes.get(1);
			List<Point<Integer>> points = List.of(new Point<Integer>(b.getX(), b.getY()));
			ret.add(new Block1(x, y, b.getColor(), points));
			
		} else if (iBoxes.contains(1)) {
			Box b = boxes.get(0);
			List<Point<Integer>> points = List.of(new Point<Integer>(b.getX(), b.getY()));
			ret.add(new Block1(x, y, b.getColor(), points));

		} else {
			ret.add(new Block2(this));
		}
		return ret;
	}
}
