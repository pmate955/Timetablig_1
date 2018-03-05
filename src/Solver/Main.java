package Solver;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;

import Datatypes.Combo;

import Visualization.VisualTimetable;

public class Main {

	public static void main(String[] args) {
		GreedySolve g = new GreedySolve("time2.txt");
		List<Combo> solved = new ArrayList();
		List<Combo> bad = new ArrayList();
		if(g.solveBackTrack2(g.courses,solved,bad,g.teachers,0,0,0))System.out.println("yuhú");;
		for(Combo c: solved){
			c.print();
		}
		g.setCombo(solved);
	
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
