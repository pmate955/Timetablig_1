package Solver;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Datatypes.Combo;
import Datatypes.Course;
import Datatypes.IndexCombo;
//import Datatypes.Curriculum;
import Datatypes.Room;
import Datatypes.Teacher;
import Datatypes.TimeSlot;
import Datatypes.Topic;

public class GreedySolve {
	private Reader r;
	private boolean readyToSolve;
	public List<Room> rooms;
	public List<Teacher> teachers;
	public List<Course> courses;
	public List<Topic> topics;
	public List<TimeSlot> timeslots;
	public Map<String,List<Integer>> courseTeacher;
	public static int runCount = 0;
	
	public GreedySolve(String filename){
		this.r = new Reader(filename);
		if(!r.readFile()){
			this.readyToSolve = false;
		} else this.readyToSolve = true;
		this.rooms = new ArrayList<Room>();
		this.courses = new ArrayList<Course>();
		this.teachers = new ArrayList<Teacher>();
		this.topics = new ArrayList<Topic>();
		this.copyListR(this.rooms, r.rooms);
		this.copyListT(this.teachers, r.teachers);
		this.copyListC(this.courses, r.courses);
		this.copyListTp(this.topics, r.topics);
		this.courseTeacher = new HashMap<String,List<Integer>>();
		for(Topic to:topics){
			List<Integer> tIndexes = new ArrayList<Integer>();
			for(Teacher t:teachers){
				if(t.contains(to.getName())) tIndexes.add(teachers.indexOf(t));
			}
			courseTeacher.put(to.getName(), tIndexes);
		}
		this.timeslots = new ArrayList<TimeSlot>();
		for(int day = 0; day < 5; day++){
			for(int slot = 0; slot < 4; slot++){
				TimeSlot t = new TimeSlot(day,slot);
				this.timeslots.add(t);
			}
		}
	}
	
	public void clearData(){
		for(Room ro:rooms) ro.clearRoom();
		for(Teacher t:teachers) t.clearAvailability();
		this.copyListC(this.courses, r.courses);
	}
	
	public void NoFridaySlot(){
		this.timeslots.clear();
		for(int day = 0; day < 4; day++){
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
	
	
					// List of unfixed courses, solution Course/Room/Time/Teacher combo, already used Time/Room, list of teachers
	public boolean solveBackTrack2(List<Course> cs, List<Combo> solved, List<Point> used, List<Teacher> teachers, int timeSlotIndex, int roomIndex){
		runCount++;		
		if(cs.isEmpty()) return true;							//If there's no more unfixed course, end of the recursion
		Course c = cs.get(0);									//Else we get the first unfixed course, and trying to fix
		if(timeSlotIndex >= timeslots.size()){					//If "timeIndex" > maximum time, we're going to next room
			timeSlotIndex = 0;
			roomIndex++;
		}
		if(roomIndex >= rooms.size()) return false;				//If there is no more room/time, we're failed
		Point p = new Point(timeSlotIndex, roomIndex);			//We're checking the given time/room combo
		while(used.contains(p)){								//If it's already used, no need recursion, while we don't find an available slot
			timeSlotIndex++;		
			if(timeSlotIndex >= timeslots.size()){	
				timeSlotIndex = 0;
				roomIndex++;
			}
			if(roomIndex >= rooms.size()) return false;			
			p = new Point(timeSlotIndex, roomIndex);
		}
		TimeSlot t = timeslots.get(timeSlotIndex);				//We get the time slot
		Room r = rooms.get(roomIndex);							//and the room
		boolean good = true;
		for(int i = 0; i < c.getSlots(); i++){					//Check the course/Slot/room combo, has collision with already fixed combos?
			Combo cbn = new Combo(c,new TimeSlot(t.getDay(),t.getSlot()+i),r);
			if(erroneus(solved,cbn)){
				good = false;
				break;
			}
		}
		boolean foundTeacher = false;
		int teacherIndex = 0;
		if(good){										//We're trying to find a teacher to combo with the minimal courses (soft constraint)
			int minimalSlot = Integer.MAX_VALUE;
			for(int j = 0; j < teachers.size();j++){				//We go through teachers
				boolean found = true;
				Teacher tc = teachers.get(j);
				if(!tc.contains(c.getTopicname())) continue;		//If the teacher is not specialized for the course, we get the next one
				for(int i = 0; i < c.getSlots(); i++){
					TimeSlot tts = new TimeSlot(t.getDay(),t.getSlot()+i);
					if(!tc.isAvailable(tts)){						//We have to check the teacher availability 
						found = false;
						break;
					}
				}
				if(found){											//If teacher has time we found the one we need :)
					if(tc.getUnavailabelCount()<minimalSlot){
						teacherIndex = j;
						minimalSlot = tc.getUnavailabelCount();
					}					
					foundTeacher = true;					
				}
				
			}
		}
		if(good && foundTeacher){								//If the course has time/room/teacher, we can add to our solved list

			teachers.get(teacherIndex).addUnavailablePeriod(t, c.getSlots());		//set the teacher unavailable for him course
			for(int i = 0; i < c.getSlots(); i++){
				Combo cbn = new Combo(c,new TimeSlot(t.getDay(),t.getSlot()+i),r);
				cbn.getCourse().setT(teachers.get(teacherIndex));					//Save the combos
				solved.add(cbn);
				used.add(new Point(timeSlotIndex+i, roomIndex));
			}
			cs.remove(c);
				return solveBackTrack2(cs,solved,used,teachers,0,0);		//We going down the tree with the next course
		}
		return solveBackTrack2(cs,solved,used,teachers,++timeSlotIndex,roomIndex);		//If we didn't find something, we have to check the next time slots/rooms
	}
	
	public boolean solveBackTrackHard(List<Course> cs, List<Combo> solved, List<IndexCombo> used, List<Teacher> teachers, int timeSlotIndex, int roomIndex, int teacherIndex){	
		runCount++;
		if(cs.isEmpty()) return true;							//If there's no more unfixed course, end of the recursion
		Course c = cs.get(0);									//Else we get the first unfixed course, and trying to fix
		List<Integer> teacherIndexes = this.getTeacherByCourse(c.getTopicname());
		if(timeSlotIndex >= timeslots.size()){					//If "timeIndex" > maximum time, we're going to next room
			timeSlotIndex = 0;
			roomIndex++;
		}
		if(roomIndex >= rooms.size()){
			roomIndex = 0;									//If there is no more room/time, next teacher
			teacherIndex++;
		}
		if(teacherIndex>=teacherIndexes.size()) return false;
		IndexCombo p = new IndexCombo(timeSlotIndex, roomIndex, teacherIndex);			//We're checking the given time/room combo
		while(used.contains(p)){								//If it's already used, no need recursion, while we don't find an available slot
			timeSlotIndex++;		
			if(timeSlotIndex >= timeslots.size()){					//If "timeIndex" > maximum time, we're going to next room
				timeSlotIndex = 0;
				roomIndex++;
			}
			if(roomIndex >= rooms.size()){
				roomIndex = 0;									//If there is no more room/time, next teacher
				teacherIndex++;
			}
			if(teacherIndex>=teacherIndexes.size()) return false;	
			p = new IndexCombo(timeSlotIndex, roomIndex, teacherIndex);
		}
		TimeSlot t = timeslots.get(timeSlotIndex);				//We get the time slot
		Room r = rooms.get(roomIndex);							//and the room
		boolean good = true;
		for(int i = 0; i < c.getSlots(); i++){					//Check the course/Slot/room combo, has collision with already fixed combos?
			Combo cbn = new Combo(c,new TimeSlot(t.getDay(),t.getSlot()+i),r);
			if(erroneus(solved,cbn)){
				good = false;
				break;
			}
		}
		boolean foundTeacher = false;
		
		if(good){										//We're trying to find a teacher to combo 
					
			boolean found = true;
			Teacher tc = teachers.get(teacherIndexes.get(teacherIndex));
			if(!tc.contains(c.getTopicname())) foundTeacher = false;	//If the teacher is not specialized for the course, we get the next one
			for(int i = 0; i < c.getSlots(); i++){
				TimeSlot tts = new TimeSlot(t.getDay(),t.getSlot()+i);
				if(!tc.isAvailable(tts)){						//We have to check the teacher availability 
					found = false;
					break;
				}
			}
			if(found){													
				foundTeacher = true;					
			}				
			
		}
		if(good && foundTeacher){								//If the course has time/room/teacher, we can add to our solved list

			teachers.get(teacherIndex).addUnavailablePeriod(t, c.getSlots());		//set the teacher unavailable for him course
			for(int i = 0; i < c.getSlots(); i++){
				Combo cbn = new Combo(c,new TimeSlot(t.getDay(),t.getSlot()+i),r);
				cbn.getCourse().setT(teachers.get(teacherIndex));					//Save the combos
				solved.add(cbn);
				used.add(new IndexCombo(timeSlotIndex+i, roomIndex,teacherIndex));
			}
			cs.remove(c);
				return solveBackTrackHard(cs,solved,used,teachers,0,0,0);		//We going down the tree with the next course
		}
		return solveBackTrackHard(cs,solved,used,teachers,++timeSlotIndex,roomIndex,teacherIndex);		//If we didn't find something, we have to check the next time slots/rooms
	}
	
	public void HillClimbFriday(){
		for(int slotIndex = 0; slotIndex < 4; slotIndex++){
			HashMap<IndexCombo,Integer> map = this.getEmptyComboList();
			for(Room room: rooms){
				Course c = room.getCourseByPos(4, slotIndex);
				if(c!=null){
					
				}
			}
		}
		
	}
	
	
	
	public HashMap<IndexCombo,Integer> getEmptyComboList(){				//Integer stores the size of slots, Indexcombo(day,slot,roomIndex)
		HashMap<IndexCombo,Integer> output = new HashMap<IndexCombo,Integer>();
		for(int roomIndex = 0; roomIndex<rooms.size();roomIndex++){
			Room r = rooms.get(roomIndex);
			for(int day = 0; day < 5; day++){
				for(int slot = 0; slot < 4; slot++){
					if(r.getCourseByPos(day, slot)==null){
						int size = 0;
						for(int i = 0; i<4;i++){
							if(slot+i<4 && r.getCourseByPos(day, slot+i)==null){
								size++;
							} else break;
						}
						output.put(new IndexCombo(day,slot,roomIndex), size);
						slot+=size;
					}
				}
			}
		}
		return output;		
	}
	
	public void setCombo(List<Combo> l){
		for(Combo cmb: l){
			for(Room ro : rooms){
				if(ro.equals(cmb.getR())){
					ro.addCourse(cmb.getCourse(), cmb.getT());
				}
			}
		}
	}
	
	private List<Integer> getTeacherByCourse(String topicName){
		return courseTeacher.get(topicName);
	}
	
	private boolean erroneus(List<Combo> good, Combo c){					//True, if the solution is not correct
		if(c.getT().getSlot()>=4) return true;								//Not enough timeslot for the given day
		for(Combo gd: good){
			if(gd.getR().equals(c.getR()) && gd.getT().equals(c.getT()))	return true;		//If it collides with another course, which is already in list
		}
		return false;														//Otherwise, it's good :)
	}
	
	private void copyListC(List<Course> dest, List<Course> src){
		for(int i = 0; i < src.size();i++){
			dest.add(src.get(i));
		}
	}
	public void copyListR(List<Room> dest, List<Room> src){
		for(int i = 0; i < src.size();i++){
			dest.add(src.get(i));
		}
	}
	
	private void copyListT(List<Teacher> dest, List<Teacher> src){
		for(int i = 0; i < src.size();i++){
			dest.add(src.get(i));
		}
	}
	
	private void copyListTp(List<Topic> dest, List<Topic> src){
		for(int i = 0; i < src.size();i++){
			dest.add(src.get(i));
		}
	}
}
