package vttp.iss.privatemoviebooking.model;

public class Movies {
    
    private String title;

    private Integer year;

    private String poster;

    public Movies() {
    }

    public Movies(String title, Integer year, String poster) {
        this.title = title;
        this.year = year;
        this.poster = poster;
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

    @Override
    public String toString() {
        return "Movies [title=" + title + ", year=" + year + ", poster=" + poster + "]";
    }
}
