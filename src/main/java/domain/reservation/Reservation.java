package domain.reservation;

public class Reservation {

	private int reservId;
	private int custId;
	private String custName;
	private int screenId;
	private int movieId;
	private String movieTitle;
	private String theaterName;
	private String showtime;
	private String seatNo;
	private int price;
	private String reservDate;

	public Reservation() {}

	public int getReservId()              { return reservId; }
	public void setReservId(int v)        { this.reservId = v; }
	public int getCustId()                { return custId; }
	public void setCustId(int v)          { this.custId = v; }
	public String getCustName()           { return custName; }
	public void setCustName(String v)     { this.custName = v; }
	public int getScreenId()              { return screenId; }
	public void setScreenId(int v)        { this.screenId = v; }
	public int getMovieId()               { return movieId; }
	public void setMovieId(int v)         { this.movieId = v; }
	public String getMovieTitle()         { return movieTitle; }
	public void setMovieTitle(String v)   { this.movieTitle = v; }
	public String getTheaterName()        { return theaterName; }
	public void setTheaterName(String v)  { this.theaterName = v; }
	public String getShowtime()           { return showtime; }
	public void setShowtime(String v)     { this.showtime = v; }
	public String getSeatNo()             { return seatNo; }
	public void setSeatNo(String v)       { this.seatNo = v; }
	public int getPrice()                 { return price; }
	public void setPrice(int v)           { this.price = v; }
	public String getReservDate()         { return reservDate; }
	public void setReservDate(String v)   { this.reservDate = v; }

}
