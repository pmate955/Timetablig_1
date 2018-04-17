package Datatypes;

public class Combo {		//Represents a combination of TimeSlot, Course and Room {later teacher}
	
	private Course c;
	private TimeSlot t;
	private Room r;
	
	public Combo(Course c, TimeSlot t, Room r){
		this.c = c;
		this.r = r;
		this.t = t;
	}
	
	public Course getCourse(){
		return this.c;
	}

	public TimeSlot getT() {
		return t;
	}

	public Room getR() {
		return r;
	}
	
	public void setC(Course c) {
		this.c = c;
	}

	public void setT(TimeSlot t) {
		this.t = t;
	}

	public void setR(Room r) {
		this.r = r;
	}

	public void print(){
		System.out.println(t.toString() + " | " + (c==null?"_":c.toString()) + " " + r.getName());
	}
	
	public void setFixed(){
		this.c.setFixed();
	}
	
	public boolean equals(Object o){
		if(!(o instanceof Combo)) return false;
		if(((Combo)o).getCourse().equals(this.getCourse()) && ((Combo)o).getR().equals(this.getR()) 
			&& ((Combo)o).getT().equals(this.getT())) return true;
		return false;
	}
}
