package main;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;


public class InputManager {
	private static final String HARD_DROP = "HARD DROP";
	private static final String FALL = "FALL";
	private static final String RIGHT = "RIGHT";
	private static final String LEFT = "LEFT";
	private static final String ROTATE_RIGHT = "ROTATE RIGHT";
	private static final String ROTATE_LEFT = "ROTATE LEFT";

	private ActionMap actionMap;

	
	public InputManager(JComponent c) {
		this.actionMap = c.getActionMap();
		
		
		int condition = JComponent.WHEN_IN_FOCUSED_WINDOW;
	    InputMap inputMap = c.getInputMap(condition);
	      
		KeyStroke upKey = KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, false);
		inputMap.put(upKey, HARD_DROP);
		
		KeyStroke downKey = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, false);
		inputMap.put(downKey, FALL);
		
		KeyStroke rightKey = KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false);
		inputMap.put(rightKey, RIGHT);
		
		KeyStroke leftKey = KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false);
		inputMap.put(leftKey, LEFT);
		
		KeyStroke xKey = KeyStroke.getKeyStroke(KeyEvent.VK_X, 0, false);
		inputMap.put(xKey, ROTATE_RIGHT);
		
		KeyStroke zKey = KeyStroke.getKeyStroke(KeyEvent.VK_Z, 0, false);
		inputMap.put(zKey, ROTATE_LEFT);
	}
	
	
	public void setOnHardDrop(Runnable fn) {
		actionMap.put(HARD_DROP, new KeyAction(fn));
	}

	public void setOnFall(Runnable fn) {
		actionMap.put(FALL, new KeyAction(fn));
	}
   
	public void setOnRight(Runnable fn) {
		actionMap.put(RIGHT, new KeyAction(fn));
	}

	public void setOnLeft(Runnable fn) {
		actionMap.put(LEFT, new KeyAction(fn));
	}

	public void setOnRotateRight(Runnable fn) {
		actionMap.put(ROTATE_RIGHT, new KeyAction(fn));
	}

	public void setOnRotateLeft(Runnable fn) {
		actionMap.put(ROTATE_LEFT, new KeyAction(fn));
	}

	
	private class KeyAction extends AbstractAction {
		private Runnable proc;
		
		public KeyAction(Runnable proc) {
			this.proc = proc;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			proc.run();
		}
	}
}
