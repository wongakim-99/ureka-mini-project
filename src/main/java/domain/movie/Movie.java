package domain.movie;

public class Movie {

	private int movieId;
	private String title;
	private String genre;
	private String director;
	private String rating;

	public Movie() {}

	public Movie(int movieId, String title, String genre, String director, String rating) {
		this.movieId = movieId;
		this.title = title;
		this.genre = genre;
		this.director = director;
		this.rating = rating;
	}

	public int getMovieId()          { return movieId; }
	public void setMovieId(int v)    { this.movieId = v; }
	public String getTitle()         { return title; }
	public void setTitle(String v)   { this.title = v; }
	public String getGenre()         { return genre; }
	public void setGenre(String v)   { this.genre = v; }
	public String getDirector()      { return director; }
	public void setDirector(String v){ this.director = v; }
	public String getRating()        { return rating; }
	public void setRating(String v)  { this.rating = v; }

}
