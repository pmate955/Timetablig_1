package Datatypes;

public class Room {
	private String name;
	private Course[][] used;
	private int capacity;
	private int days;
	private int slots;
	
	public Room(String name, int days, int times, int capacity){
		this.name = name;
		this.used = new Course[days][times];
		this.capacity = capacity;
		this.days = days;
		this.slots = times;
	}
	
	public String getName(){return this.name;}
	public int getCapacity(){return this.capacity;}
	public int getDays(){return this.days;}
	public int getSlots(){return this.slots;}
	public Course getCourseByPos(int day, int slot){return used[day][slot];};
	
	public boolean isUsed(TimeSlot t){
		return used[t.getDay()][t.getSlot()]!=null;
	}
	
	public void addCourse(Course c, TimeSlot t){
		for(int i = 0; i < c.getSlots(); i++){
			this.used[t.getDay()][t.getSlot()+i] = c;			
		}
	}
	
	public Course getCourse(TimeSlot t){
		return used[t.getDay()][t.getSlot()];
	}

	public void print(){
		System.out.println("Room: " + name);
		System.out.println("=============================================================");
		for(int slot = 0; slot < slots; slot++ ){
			for(int day = 0; day < days; day++){
				if(used[day][slot]==null) System.out.print("day: " + day + " slot: " + slot + " is empty || ");
				else System.out.print("day: " + day + " slot: " + slot + " " + used[day][slot].toString() + " || ");
			}
			System.out.println();
		}
		System.out.println("=============================================================");
	}
}
