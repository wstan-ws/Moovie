package vttp.iss.privatemoviebooking.model;

public class Movies {
    
    private String title;

    private Integer year;

    private String poster;

    private String imdbID;

    private String rated;

    private String runtime;

    private String plot;

    private String rating;

    public Movies() {
    }

    public Movies(String title, Integer year, String poster, String imdbID) {
        this.title = title;
        this.year = year;
        this.poster = poster;
        this.imdbID = imdbID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getRated() {
        return rated;
    }

    public void setRated(String rated) {
        this.rated = rated;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getImdbID() {
        return imdbID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    @Override
    public String toString() {
        return "Movies [title=" + title + ", year=" + year + ", poster=" + poster + ", imdbID=" + imdbID + ", rated="
                + rated + ", runtime=" + runtime + ", plot=" + plot + ", rating=" + rating + "]";
    }
}
