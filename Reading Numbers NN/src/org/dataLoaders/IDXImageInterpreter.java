package org.dataLoaders;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class IDXImageInterpreter {
	
	public static void showImage(int[] data, int pxPerRow) {
		int scale = 4;
		
		
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
		
		
		JPanel pMain = new JPanel() {
			public void paintComponent(Graphics g) {
				//draw number to buffered image				
				for(int i = 0; i < data.length; i++) {			
					
					g.setColor(new Color(0, 0, 0, data[i]));
					g.fillRect((i % pxPerRow) * scale, (i / pxPerRow) * scale, scale, scale);
					
				}				
			}
		};
		pMain.setPreferredSize(new Dimension(pxPerRow * scale, data.length / pxPerRow * scale));
		frame.add(pMain);
		
//		g.dispose();
		
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
//	private static class DrawPanel extends JComponent{
//		public paintComponent(Graphics g) {
//			
//		}
//	}
}
