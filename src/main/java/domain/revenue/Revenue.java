package domain.revenue;

public class Revenue {
    private int movieId;
    private String movieTitle;
    private int totalRevenue;

    public Revenue(int movieid, String movieTitle, String totalRevenue) {
        this.movieId = movieid;
        this.movieTitle = movieTitle;
        this.totalRevenue = Integer.parseInt(totalRevenue);
    }

    public int getMovieId() {
        return movieId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public int getTotalRevenue() {
        return totalRevenue;
    }
}
