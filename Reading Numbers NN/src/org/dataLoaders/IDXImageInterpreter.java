package org.dataLoaders;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class IDXImageInterpreter {
	
	public static void showImage(double[] data, int pxPerRow, boolean exitOnClose) {
		int scale = 4;
		
		
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(exitOnClose ? JFrame.EXIT_ON_CLOSE : JFrame.HIDE_ON_CLOSE);
				
		
		
		JPanel pMain = new JPanel() {
			public void paintComponent(Graphics g) {
				//draw number to buffered image
				for(int i = 0; i < data.length; i++) {
//					System.out.println(data[i]);
					g.setColor(new Color(0, 0, 0, (int)(data[i] * 255)));
					g.fillRect((i % pxPerRow) * scale, (i / pxPerRow) * scale, scale, scale);
				}
			}
		};
		pMain.setPreferredSize(new Dimension(pxPerRow * scale, data.length / pxPerRow * scale));
		frame.add(pMain);
		
//		g.dispose();
		
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
//	private static class DrawPanel extends JComponent{
//		public paintComponent(Graphics g) {
//			
//		}
//	}
}
