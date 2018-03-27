package Solver;

import java.awt.EventQueue;
import java.awt.Point;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import Datatypes.Combo;
import Visualization.VisualTimetable;

public class Main {

	public static void main(String[] args) {
		GreedySolve g = new GreedySolve("time2.txt");
		List<Combo> solved = new ArrayList();
		List<Point> bad = new ArrayList();
		Instant start = Instant.now();
		if(g.solveBackTrack2(g.courses,solved,bad,g.teachers,0,0))System.out.println("Success!");
		else System.out.println("Failed!!!!");
		Instant end = Instant.now();
		g.setCombo(solved);	
		g.printSolution();	
		System.out.println();
		System.out.println("==========Optimization info============");
		System.out.println(g.runCount + " times started the method");
		System.out.println("Time needed: " + Duration.between(start, end)); 
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
