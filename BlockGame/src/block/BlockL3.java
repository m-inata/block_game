package block;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import util.Point;


/**
 * 隣接するL字状の3つのboxから構成されるブロック
 * [0][1]
 * [2]
 */
public class BlockL3 extends Block {

	public BlockL3(int x, int y, Color color) {
		super(x, y, new ArrayList<Box>());
		
		boxes.add(new Box(this, 0, 0, color));
		boxes.add(new Box(this, 1, 0, color));
		boxes.add(new Box(this, 0, -1, color));
	}

	/**
	 * 他のブロックが部分的に消去後、1個のブロックが必要になったときのため。
	 * @param x
	 * @param y
	 * @param color
	 * @param points このブロックに含まれるboxの相対座標のリスト(要素が3個であると想定)
	 */
	public BlockL3(int x, int y, Color color, List<Point<Integer>> points) {
		super(x, y, new ArrayList<Box>());
		
		for (Point<Integer> pt : points) {
			boxes.add(new Box(this, pt.x(), pt.y(), color));
		}
	}

	
	/**
	 * コピーコンストラクタ
	 * @param other
	 */
	public BlockL3(BlockL3 other) {
		super(other);
		
		this.x = other.x;
		this.y = other.y;
	}

	
	/**
	 * 指定された消去される位置をもとに、分解後のブロックのリストを構築する
	 */
	@Override
	protected List<Block> getErased(Set<Integer> iBoxes) {
		List<Block> ret = new ArrayList<>();
				
		if (iBoxes.containsAll(Set.of(0, 1, 2))) {
			;
			
		} else if (iBoxes.containsAll(Set.of(0, 1))) {
			Box b = boxes.get(2);
			List<Point<Integer>> points = List.of(new Point<Integer>(b.getX(), b.getY()));
			ret.add(new Block1(x, y, b.getColor(), points));
			
		} else if (iBoxes.containsAll(Set.of(0, 2))) {
			Box b = boxes.get(1);
			List<Point<Integer>> points = List.of(new Point<Integer>(b.getX(), b.getY()));
			ret.add(new Block1(x, y, b.getColor(), points));

		} else if (iBoxes.containsAll(Set.of(1, 2))) {
			Box b = boxes.get(0);
			List<Point<Integer>> points = List.of(new Point<Integer>(b.getX(), b.getY()));
			ret.add(new Block1(x, y, b.getColor(), points));

		} else if (iBoxes.containsAll(Set.of(0))) {
			Box b = boxes.get(1);
			List<Point<Integer>> points = List.of(new Point<Integer>(b.getX(), b.getY()));
			ret.add(new Block1(x, y, b.getColor(), points));
			Box b2 = boxes.get(2);
			List<Point<Integer>> points2 = List.of(new Point<Integer>(b2.getX(), b2.getY()));
			ret.add(new Block1(x, y, b.getColor(), points2));

		} else if (iBoxes.containsAll(Set.of(1))) {
			Box b = boxes.get(0);
			Box b2 = boxes.get(2);
			List<Point<Integer>> points = List.of(new Point<Integer>(b.getX(), b.getY()),
					new Point<Integer>(b2.getX(), b2.getY()));
			ret.add(new Block2(x, y, b.getColor(), points));

		} else if (iBoxes.containsAll(Set.of(2))) {
			Box b = boxes.get(0);
			Box b2 = boxes.get(1);
			List<Point<Integer>> points = List.of(new Point<Integer>(b.getX(), b.getY()),
					new Point<Integer>(b2.getX(), b2.getY()));
			ret.add(new Block2(x, y, b.getColor(), points));

		} else {
			ret.add(new BlockL3(this));
		}
		
		return ret;
	}

}
