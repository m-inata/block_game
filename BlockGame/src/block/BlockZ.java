package block;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import util.Point;

/**
 * 隣接する4つのboxから構成されるブロック
 * [3][2]
 *    [0][1]
 */
public class BlockZ extends Block {

	public BlockZ(int x, int y, Color color) {
		super(x, y, new ArrayList<Box>());
		
		boxes.add(new Box(this, 0, 0, color));
		boxes.add(new Box(this, 1, 0, color));
		boxes.add(new Box(this, 0, -1, color));
		boxes.add(new Box(this, -1, -1, color));

	}
	
	
	/**
	 * 他のブロックが部分的に消去後、1個のブロックが必要になったときのため。
	 * @param x
	 * @param y
	 * @param color
	 * @param points このブロックに含まれるboxの相対座標のリスト(要素が4個であると想定)
	 */
	public BlockZ(int x, int y, Color color, List<Point<Integer>> points) {
		super(x, y, new ArrayList<Box>());
		
		for (Point<Integer> pt : points) {
			boxes.add(new Box(this, pt.x(), pt.y(), color));
		}
	}

	
	/**
	 * コピーコンストラクタ
	 * @param other
	 */
	public BlockZ(BlockZ other) {
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
				
		if (iBoxes.containsAll(Set.of(0, 1, 2, 3))) {
			;
			
		} else if (iBoxes.containsAll(Set.of(0, 1, 2))) {
			ret = BlockCreater.create1(x, y, boxes, 3);
			
		} else if (iBoxes.containsAll(Set.of(0, 2, 3))) {
			ret = BlockCreater.create1(x, y, boxes, 1);

		} else if (iBoxes.containsAll(Set.of(0, 1))) {
			ret = BlockCreater.create2(x, y, boxes, 2, 3);

		} else if (iBoxes.containsAll(Set.of(0, 2))) {
			ret = BlockCreater.create1_1(x, y, boxes, 1, 3);

		} else if (iBoxes.containsAll(Set.of(1, 3))) {
			ret = BlockCreater.create2(x, y, boxes, 0, 2);

		} else if (iBoxes.containsAll(Set.of(2, 3))) {
			ret = BlockCreater.create2(x, y, boxes, 0, 1);

		} else if (iBoxes.containsAll(Set.of(1))) {
			ret = BlockCreater.create3(x, y, boxes, 0, 2, 3);

		} else if (iBoxes.containsAll(Set.of(3))) {
			ret = BlockCreater.create3(x, y, boxes, 0, 1, 2);

		} else {
			ret.add(new BlockZ(this));
		}
		
		return ret;
	}

}
