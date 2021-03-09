package Connect4;

import java.io.FileNotFoundException;

public class GameBoard {
	
	public static void main (String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable(){
			
			@Override
			public void run() {
				try {
					@SuppressWarnings("unused")
					GuiForConnect4 gui = new GuiForConnect4();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
			
		});
	}

}
