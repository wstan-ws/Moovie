package vttp.iss.privatemoviebooking.services;

import java.io.StringReader;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp.iss.privatemoviebooking.model.FormDetails;
import vttp.iss.privatemoviebooking.model.Movies;
import vttp.iss.privatemoviebooking.repository.RedisRepository;

@Service
public class MovieService {

    @Autowired
    RedisRepository redisRepo;
    
    @Value("${newsapi.key}")
    private String apiKey;

    private List<Movies> movieList = null;

    public List<Movies> getMovies() {

        String url = UriComponentsBuilder
            .fromUriString("https://www.omdbapi.com")
            // apikey needs to be header instead
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
                return new Movies(title, year, poster); 
            })
            .sorted((c0, c1) -> c0.getTitle().compareTo(c1.getTitle()))
            .toList();

        return movieList;
    }

    public void saveDetails(FormDetails details) {

        redisRepo.saveDetails(details);
    }

    public FormDetails getDetails(String uniqueKey) {

        FormDetails retrievedDetails = redisRepo.getDetails(uniqueKey);

        return retrievedDetails;
    }

    public Integer remainingOccupancy(FormDetails details) {

        Integer remaining = redisRepo.remainingOccupancy(details);

        return remaining;
    }

    public void deleteBooking(String uniqueKey, String key) {

        redisRepo.deleteBooking(uniqueKey, key);
    }

    public void editBooking(String uniqueKey, FormDetails formDetails, String key) {

        redisRepo.editBooking(uniqueKey, formDetails, key);
    }
}
