package Datatypes;

import java.util.ArrayList;
import java.util.List;

public class Teacher {
	private String name;
	private List<String> specialities;
	private List<TimeSlot> availability;
	
	public Teacher(String name){
		this.name = name;
		this.specialities = new ArrayList<String>();
		this.availability = new ArrayList<TimeSlot>();
	}

	public String getName() {
		return name;
	}

	public void setName(String _name) {
		this.name = _name;
	}
	
	public void addSpeciality(String spec){
		if(!specialities.contains(spec)) specialities.add(spec);
	}
	
	public boolean contains(String spec){
		return specialities.contains(spec);
	}
	
	public void print(){
		System.out.println("Name: " + this.name);
		for(String s: specialities) System.out.println("Spec: " + s);
		System.out.println("---------------------------------");
	}
	
	public void addUnavailablePeriod(TimeSlot t, int slots){
		for(int i = 0; i < slots; i++){
			TimeSlot ts = new TimeSlot(t.getDay(),t.getSlot()+i);
			if(!availability.contains(ts)) availability.add(ts);
		}
	}
	
	public boolean isAvailable(TimeSlot t){
		return !availability.contains(t);			
	}
	
	public void clearAvailability(){
		this.availability.clear();
	}
}
