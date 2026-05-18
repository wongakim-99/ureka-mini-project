package domain.theater;

public class Theater {

	private int theaterId;
	private String name;
	private int totalSeats;

	public Theater() {}

	public Theater(int theaterId, String name, int totalSeats) {
		this.theaterId = theaterId;
		this.name = name;
		this.totalSeats = totalSeats;
	}

	public int getTheaterId()           { return theaterId; }
	public void setTheaterId(int v)     { this.theaterId = v; }
	public String getName()             { return name; }
	public void setName(String v)       { this.name = v; }
	public int getTotalSeats()          { return totalSeats; }
	public void setTotalSeats(int v)    { this.totalSeats = v; }

}
