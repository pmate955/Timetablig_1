package Datatypes;

public class TimeSlot {
	private String _name;
	private int day;
	private int slot;
	
	public TimeSlot(String _name, int day, int slot) {
		this._name = _name;
		this.day = day;
		this.slot = slot;
	}

	public String get_name() {
		return _name;
	}

	public int getDay() {
		return day;
	}

	public int getSlot() {
		return slot;
	}
	
	
	

}
