package Solver;

import java.awt.EventQueue;
import java.awt.Point;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import Datatypes.Combo;
import Datatypes.IndexCombo;
import Datatypes.Room;
import Visualization.VisualTimetable;

public class Main {

	public static void main(String[] args) {
		GreedySolve g = new GreedySolve("time2.txt");		
		Instant start = Instant.now();
		solveNew(g);
		Instant end = Instant.now();				
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
	
	public static void solveOld(GreedySolve g){
		List<Combo> solved = new ArrayList<Combo>();
		List<Point> bad = new ArrayList<Point>();
		List<Room> roomSave = new ArrayList<Room>();
		List<Combo> solvedSave = new ArrayList<Combo>();
		if(g.solveBackTrack2(g.courses,solved,bad,g.teachers,0,0)){
			System.out.println("Success, trying to solve without Friday!");
			g.copyListR(roomSave, g.rooms);
			for(Combo c:solved) solvedSave.add(c);
			solved.clear();
			bad.clear();
			g.clearData();			
			g.NoFridaySlot();
			System.out.println(g.courses.size());
			if(g.solveBackTrack2(g.courses,solved,bad,g.teachers,0,0)){
				System.out.println("Solved without friday :)");				
			} else {
				System.out.println("Solved with friday only :(");				
				g.rooms.clear();
				g.copyListR(g.rooms, roomSave);
				solved.clear();
				for(Combo c:solvedSave) solved.add(c);
			}
		}
		else System.out.println("Failed!!!!");
		g.setCombo(solved);	
		g.printSolution();	
	}

	public static void solveNew(GreedySolve g){
		List<Combo> solved = new ArrayList<Combo>();
		List<IndexCombo> bad = new ArrayList<IndexCombo>();
		List<Room> roomSave = new ArrayList<Room>();
		List<Combo> solvedSave = new ArrayList<Combo>();
		if(g.solveBackTrackHard(g.courses,solved,bad,g.teachers,0,0,0)){
			System.out.println("Success, trying to solve without Friday!");
			g.copyListR(roomSave, g.rooms);
			for(Combo c:solved) solvedSave.add(c);
			solved.clear();
			bad.clear();
			g.clearData();			
			g.NoFridaySlot();
			System.out.println(g.courses.size());
			if(g.solveBackTrackHard(g.courses,solved,bad,g.teachers,0,0,0)){
				System.out.println("Solved without friday :)");				
			} else {
				System.out.println("Solved with friday only :(");				
				g.rooms.clear();
				g.copyListR(g.rooms, roomSave);
				solved.clear();
				for(Combo c:solvedSave) solved.add(c);
			}
		}
		else System.out.println("Failed!!!!");
		g.setCombo(solved);	
		g.printSolution();	
	}
	
}
