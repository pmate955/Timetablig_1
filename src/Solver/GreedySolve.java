package Solver;

import java.util.List;

import Datatypes.Course;
//import Datatypes.Curriculum;
import Datatypes.Room;
import Datatypes.Teacher;
import Datatypes.TimeSlot;
import Datatypes.Topic;

public class GreedySolve {
	private Reader r;
	private boolean readyToSolve;
	private int days;
	private int slots;
	public List<Room> rooms;
	public List<Teacher> teachers;
	public List<Course> courses;
	public List<Topic> topics;
	
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
	}
	
	public void solve(){										//Courses to Rooms
		if(!readyToSolve){
			System.out.println("Can not solve because no input data! ");
			return;
		}
		int courseIndex = 0;
		int counter = 0;
		while(!isAllTopicFixed() || counter < 20){
			for(int day = 0; day < days; day++ ){
				for(int slot = 0; slot < slots; slot++){			//Fixed periods
					for(Topic tp:topics){							//Go through topics
						Course c = tp.getFirstUnfixed();
						if(c==null) continue;						//If all course in topic is fixed, go to next topic
						TimeSlot t = new TimeSlot("T ", day, slot);
						for(Room r : rooms) if(!r.isUsed(t) && slot <= slots-c.getSlots()){		//Go through rooms, and if we find an eligible period/room combination 
							boolean noSameTopicInPeriod = true;
							for(Room r2 :rooms){												//Check the same topics in period
								if(r2.isUsed(t) && tp.contains(r2.getCourse(t))){
									noSameTopicInPeriod = false;
									break;
								}
							}
							if(noSameTopicInPeriod){
								r.addCourse(c, t);													//Add the course to room
								c.setFixed();
								courseIndex++;
								break;
							}
						}
						if(courseIndex>=courses.size()){					
							this.addTeachers();
							System.out.println("___________SOLVED_________");								
							return;
						}
					}					
				}
			}
			counter++;
		}		
		this.addTeachers();
		System.out.println("Not solved in time :( ");
	}
	
	private void addTeachers(){
		for(int day = 0; day < days; day++){
			for(int slot = 0; slot < slots; slot++){
				TimeSlot t = new TimeSlot("",day,slot);
				for(Room r: rooms){
					Course c = r.getCourse(t);
					if(c == null) continue;
					String topicName = "";
					for(Topic topic:topics){					//Optimize later !!!
						if(topic.contains(c)){
							topicName = topic.getName();
							break;
						}
					}
					boolean noTeacher = true;
					for(Teacher te:teachers){
						if(te.contains(topicName) && te.isAvailable(t)){
							te.addUnavailablePeriod(t);
							c.setT(te);
							noTeacher = false;
							break;
						}
					}
					if(noTeacher) System.out.println("Problem, no teacher : " + topicName);
				}
			}
		}
	}
	
	private boolean isAllTopicFixed(){
		for(Topic t:topics) if(!t.isAllFixed()) return false;
		return true;
	}
	
	public void printSolution(){
		System.out.println("Solution: ");
		for(Room r: rooms) r.print();
	}
	
	
	
}
