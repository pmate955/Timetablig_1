package Solver;

import java.util.ArrayList;
import java.util.List;

import Datatypes.Course;
//import Datatypes.Curriculum;
import Datatypes.Room;
import Datatypes.Teacher;
import Datatypes.TimeSlot;
import Datatypes.Topic;
import Datatypes.Combo;

public class GreedySolve {
	private Reader r;
	private boolean readyToSolve;
	private int days;
	private int slots;
	public List<Room> rooms;
	public List<Teacher> teachers;
	public List<Course> courses;
	public List<Topic> topics;
	public List<TimeSlot> timeslots;
	
	public GreedySolve(String filename){
		this.slots = 4;
		this.days = 4;
		this.r = new Reader(filename);
		if(!r.readFile()){
			this.readyToSolve = false;
		} else this.readyToSolve = true;
		this.rooms = r.rooms;
		this.teachers = r.teachers;
		this.courses = r.courses;
		this.topics = r.topics;	
		this.timeslots = new ArrayList();
		for(int day = 0; day < 5; day++){
			for(int slot = 0; slot < 4; slot++){
				TimeSlot t = new TimeSlot(day,slot);
				this.timeslots.add(t);
			}
		}
	}
	
	
	
	
	public void printSolution(){
		System.out.println("Solution: ");
		for(Room r: rooms) r.print();
	}
	
	
	
	public boolean solveBackTrack2(List<Course> cs, List<Combo> solved, List<Combo> bad, List<Teacher> teachers, int timeSlotIndex, int roomIndex, int teacherIndex){
		if(cs.isEmpty()) return true;			//Ha minden kurzust felhasználtunk, akkor vége
		Course c = cs.get(0);					//Kiveszem az elsõ kurzust
		if(timeSlotIndex >= timeslots.size()){	//Ha nincs több idõpont, megyünk a következõ teremre
			timeSlotIndex = 0;
			roomIndex++;
		}
		if(roomIndex >= rooms.size()) return false;			//Ha nincs több választásunk, akkor visszalépünk
		TimeSlot t = timeslots.get(timeSlotIndex);
		Room r = rooms.get(roomIndex);
		boolean good = true;
		for(int i = 0; i < c.getSlots(); i++){				//Megnézzük, hogy minden slotba illik-e az óra
			Combo cbn = new Combo(c,new TimeSlot(t.getDay(),t.getSlot()+i),r);
			if(erroneus(solved,cbn)){
				good = false;
				break;
			}
		}
		boolean foundTeacher = false;						//Megpróbálunk ráérõ tanárt keresni
		for(int j = 0; j < teachers.size();j++){
			boolean found = true;
			Teacher tc = teachers.get(j);
			if(!tc.contains(c.getTopicname())) continue;
			for(int i = 0; i < c.getSlots(); i++){
				TimeSlot tts = new TimeSlot(t.getDay(),t.getSlot()+i);
				if(!tc.isAvailable(tts)){
					found = false;
					break;
				}
			}
			if(found){
				teacherIndex = j;
				foundTeacher = true;
				break;
			}
			
		}
		if(good && foundTeacher){

			teachers.get(teacherIndex).addUnavailablePeriod(t, c.getSlots());		//Foglalttá tesszük az adott tanárt
			for(int i = 0; i < c.getSlots(); i++){
				Combo cbn = new Combo(c,new TimeSlot(t.getDay(),t.getSlot()+i),r);
				cbn.getCourse().setT(teachers.get(teacherIndex));					//Elmentjük a megoldást
				solved.add(cbn);
			}
			cs.remove(c);
				return solveBackTrack2(cs,solved,bad,teachers,0,0,0);		//Ha sikerült lerakni a kombót, megyünk tovább
		}
		return solveBackTrack2(cs,solved,bad,teachers,++timeSlotIndex,roomIndex,teacherIndex);		//Ha nem, akkor tovább próbálkozunk
	}
	
	public void setCombo(List<Combo> l){
		for(Combo cmb: l){
			Room r = null;
			for(Room ro : rooms){
				if(ro.equals(cmb.getR())){
					ro.addCourse(cmb.getCourse(), cmb.getT());
				}
			}
		}
	}
	
	private boolean erroneus(List<Combo> good, Combo c){		//TRue, ha nem jó a megoldás
		if(c.getT().getSlot()>=4) return true;		//Ha "túllóg" az óra
		for(Combo gd: good){
			if(gd.getR().equals(c.getR()) && gd.getT().equals(c.getT()))	return true;		//Ha ütközik
		}
		return false;
	}
	
	
	
	
}
