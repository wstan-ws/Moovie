package vttp.iss.privatemoviebooking.controllers;

import java.io.StringReader;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp.iss.privatemoviebooking.model.Movies;

@RestController
public class APIRestController {
    
    @Value("${movieapi.key}")
    private String apiKey;

    private List<Movies> movieList = null;

    public List<Movies> getMovies() {

        String url = UriComponentsBuilder
            .fromUriString("https://www.omdbapi.com")
            .queryParam("apikey", apiKey)
            .queryParam("s", "storm")
            .queryParam("type", "movie")
            .toUriString();

        RequestEntity<Void> request = RequestEntity.get(url).build();

        RestTemplate template = new RestTemplate();

        ResponseEntity<String> response = template.exchange(request, String.class);
        String payload = response.getBody();

        JsonReader reader = Json.createReader(new StringReader(payload));
        JsonObject object = reader.readObject();
        JsonArray array = object.getJsonArray("Search");

        movieList = array.stream()
            .map(j -> j.asJsonObject())
            .map(o -> {
                String title = o.getString("Title");
                Integer year = Integer.parseInt(o.getString("Year"));
                String poster = o.getString("Poster");
                String imdbID = o.getString("imdbID");
                return new Movies(title, year, poster, imdbID); 
            })
            .sorted((c0, c1) -> c0.getTitle().compareTo(c1.getTitle()))
            .toList();

        return movieList;
    }

    public List<Movies> getMovieDetails() {

        movieList = getMovies();

        for (Movies movie : movieList) {
            String id = movie.getImdbID();

            String url = UriComponentsBuilder
            .fromUriString("https://www.omdbapi.com")
            .queryParam("apikey", apiKey)
            .queryParam("i", id)
            .queryParam("plot", "full")
            .toUriString();

            RequestEntity<Void> request = RequestEntity.get(url).build();

            RestTemplate template = new RestTemplate();

            ResponseEntity<String> response = template.exchange(request, String.class);
            String payload = response.getBody();

            JsonReader reader = Json.createReader(new StringReader(payload));
            JsonObject object = reader.readObject();
            
            String rated = object.getString("Rated");
            String runtime = object.getString("Runtime");
            String plot = object.getString("Plot");
            String rating = object.getString("imdbRating");
            rating = rating + Character.toString(0x2B50);

            movie.setRated(rated);
            movie.setRuntime(runtime);
            movie.setPlot(plot);
            movie.setRating(rating);
        }

        return movieList;
    }

    public List<Movies> sortedYear() {

        movieList = movieList.stream()
            .sorted((c0, c1) -> c1.getYear().compareTo(c0.getYear()))
            .toList();

        return movieList;
    }

    public List<Movies> sortedRating() {

        movieList = movieList.stream()
            .sorted((c0, c1) -> c1.getRating().compareTo(c0.getRating()))
            .toList();

        return movieList;
    }
}
