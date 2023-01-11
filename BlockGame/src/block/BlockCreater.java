package block;

import java.util.ArrayList;
import java.util.List;

import util.Point;

public class BlockCreater {
	
	public static List<Block> create1(int x, int y, List<Box> boxes, int i) {
		List<Block> ret = new ArrayList<>();
		Box b = boxes.get(i);
		List<Point<Integer>> points = List.of(new Point<Integer>(b.getX(), b.getY()));
		ret.add(new Block1(x, y, b.getColor(), points));
		return ret;
	}
	
	
	public static List<Block> create2(int x, int y, List<Box> boxes, int i0, int i1) {
		List<Block> ret = new ArrayList<>();
		Box b = boxes.get(i0);
		Box b2 = boxes.get(i1);
		List<Point<Integer>> points = List.of(new Point<Integer>(b.getX(), b.getY()),
				new Point<Integer>(b2.getX(), b2.getY()));
		ret.add(new Block2(x, y, b.getColor(), points));
		return ret;
	}


	public static List<Block> create1_1(int x, int y, List<Box> boxes, int i0, int i1) {
		List<Block> ret = new ArrayList<>();
		Box b = boxes.get(i0);
		List<Point<Integer>> points = List.of(new Point<Integer>(b.getX(), b.getY()));
		ret.add(new Block1(x, y, b.getColor(), points));
		Box b2 = boxes.get(i1);
		List<Point<Integer>> points2 = List.of(new Point<Integer>(b2.getX(), b2.getY()));
		ret.add(new Block1(x, y, b.getColor(), points2));
		return ret;
	}


	public static List<Block> create3(int x, int y, List<Box> boxes, int i0, int i1, int i2) {
		List<Block> ret = new ArrayList<>();
		Box b = boxes.get(i0);
		Box b2 = boxes.get(i1);
		Box b3 = boxes.get(i2);
		List<Point<Integer>> points = List.of(new Point<Integer>(b.getX(), b.getY()),
				new Point<Integer>(b2.getX(), b2.getY()),
				new Point<Integer>(b3.getX(), b3.getY()));
		ret.add(new BlockBar3(x, y, b.getColor(), points));
		return ret;
	}


	public static List<Block> create1_2(int x, int y, List<Box> boxes, int i0, int i1, int i2) {
		List<Block> ret = new ArrayList<>();
		Box b = boxes.get(i0);
		List<Point<Integer>> points = List.of(new Point<Integer>(b.getX(), b.getY()));
		ret.add(new Block1(x, y, b.getColor(), points));
		
		Box b2 = boxes.get(i1);
		Box b3 = boxes.get(i2);
		List<Point<Integer>> points2 = List.of(new Point<Integer>(b2.getX(), b2.getY()),
				new Point<Integer>(b3.getX(), b3.getY()));
		ret.add(new Block2(x, y, b.getColor(), points2));
		return ret;
	}


}
