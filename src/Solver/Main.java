package Solver;

import java.awt.EventQueue;

import Visualization.VisualTimetable;

public class Main {

	public static void main(String[] args) {
		GreedySolve g = new GreedySolve("time2.txt");
		g.solve();
		g.printSolution();		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VisualTimetable frame = new VisualTimetable(g);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
