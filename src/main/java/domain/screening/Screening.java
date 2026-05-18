package domain.screening;

public class Screening {

	private int screenId;
	private int movieId;
	private String movieTitle;
	private int theaterId;
	private String theaterName;
	private String showtime;
	private int price;
	private int remainSeats;

	public Screening() {}

	public int getScreenId()              { return screenId; }
	public void setScreenId(int v)        { this.screenId = v; }
	public int getMovieId()               { return movieId; }
	public void setMovieId(int v)         { this.movieId = v; }
	public String getMovieTitle()         { return movieTitle; }
	public void setMovieTitle(String v)   { this.movieTitle = v; }
	public int getTheaterId()             { return theaterId; }
	public void setTheaterId(int v)       { this.theaterId = v; }
	public String getTheaterName()        { return theaterName; }
	public void setTheaterName(String v)  { this.theaterName = v; }
	public String getShowtime()           { return showtime; }
	public void setShowtime(String v)     { this.showtime = v; }
	public int getPrice()                 { return price; }
	public void setPrice(int v)           { this.price = v; }
	public int getRemainSeats()           { return remainSeats; }
	public void setRemainSeats(int v)     { this.remainSeats = v; }

}
