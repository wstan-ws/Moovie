package vttp.iss.privatemoviebooking.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import vttp.iss.privatemoviebooking.model.FormDetails;
import vttp.iss.privatemoviebooking.model.Movies;
import vttp.iss.privatemoviebooking.model.UniqueKey;
import vttp.iss.privatemoviebooking.services.MovieService;

@Controller
@RequestMapping
public class MovieController {

    @Autowired
    MovieService movieSvc;

    @Autowired
    APIRestController apiController;

    private List<Movies> movieList = null;

    private String uniqueKey = "";

    @GetMapping(path = {"/", "/home"})
    public String getHome(Model model) {

        movieList = apiController.getMovies();
        model.addAttribute("movieList", movieList);

        return "home";
    }

    @GetMapping(path = "/movie")
    public String getMoviesSelection(Model model) {

        movieList = apiController.getMovies();
        model.addAttribute("movieList", movieList);

        return "movie";
    }

    @GetMapping(path = "/movie/{title}")
    public String getMovieSelection(@PathVariable("title") String title, Model model, HttpSession session) {

        movieList = apiController.getMovies();
        String poster = "";
        for (Movies movie : movieList) {
            if (movie.getTitle().equals(title)) {
                poster = movie.getPoster();
            }
        }
        model.addAttribute("poster", poster);
        model.addAttribute("title", title);
        model.addAttribute("formDetails", new FormDetails());

        session.setAttribute("title", title);

        return "detailform";
    }

    @PostMapping(path = "/success")
    public String postMovie(@Valid @ModelAttribute FormDetails formDetails, BindingResult result, Model model, HttpSession session) {

        String title = session.getAttribute("title").toString();
        movieList = apiController.getMovies();
        String poster = "";
        for (Movies movie : movieList) {
            if (movie.getTitle().equals(title)) {
                poster = movie.getPoster();
            }
        }
        model.addAttribute("poster", poster);
        model.addAttribute("title", title);

        if (result.hasErrors()) {
            return "detailform";
        }

        if (movieSvc.remainingOccupancy(formDetails) >= 2) {
            FieldError err = new FieldError("formDetails", "date", "Max Occupancy Reached. Please select another date/time or room");
            result.addError(err);
            model.addAttribute("error", err.getDefaultMessage());
            return "errorbooking";
        }

        formDetails.setSpecialKey(UUID.randomUUID().toString());
        uniqueKey = formDetails.getSpecialKey();
        formDetails.setMovie(title);
        movieSvc.saveDetails(formDetails);

        return "success";
    }
    
    @GetMapping(path = "/booking")
    public String getBooking(Model model) {

        FormDetails retrievedDetails = movieSvc.getDetails(uniqueKey);

        model.addAttribute("retrievedDetails", retrievedDetails);

        return "booking";
    }

    @GetMapping(path = "/retrievebooking")
    public String retrieveBooking(Model model) {

        model.addAttribute("uniqueKey", new UniqueKey());

        return "retrievebooking";
    }

    @PostMapping(path = "/booking")
    public String postBooking(@ModelAttribute UniqueKey enteredKey, BindingResult result, Model model, HttpSession session) {

        if (movieSvc.getDetails(enteredKey.getUniqueKey()) == null) {
            FieldError err = new FieldError("uniqueKey", "uniqueKey", "Invalid Booking Reference No.");
            result.addError(err);
            model.addAttribute("error", err.getDefaultMessage());
            return "errorbooking";
        }

        uniqueKey = enteredKey.getUniqueKey();
        FormDetails retrievedDetails = movieSvc.getDetails(uniqueKey);

        model.addAttribute("retrievedDetails", retrievedDetails);
        session.setAttribute("uniquekey", uniqueKey);

        String key = retrievedDetails.getDate() + retrievedDetails.getTime() + retrievedDetails.getRoom();
        session.setAttribute("key", key);

        session.setAttribute("name", retrievedDetails.getName());
        session.setAttribute("email", retrievedDetails.getEmail());

        return "yourbooking";
    }

    @GetMapping(path = "/delete")
    public String deleteBooking(HttpSession session) {

        uniqueKey = session.getAttribute("uniquekey").toString();
        FormDetails details = movieSvc.getDetails(uniqueKey);
        String key = details.getDate() + details.getTime() + details.getRoom();

        movieSvc.deleteBooking(uniqueKey, key);

        return "/delete";
    }

    @GetMapping(path = "/edit")
    public String editBooking(Model model, HttpSession session) {

        model.addAttribute("formDetails", new FormDetails());
        String name = session.getAttribute("name").toString();
        String email = session.getAttribute("email").toString();
        model.addAttribute("email", email);
        model.addAttribute("name", name);

        movieList = apiController.getMovies();
        model.addAttribute("movieList", movieList);

        return "edit";
    }

    @PostMapping(path = "/editsuccess")
    public String editSuccess(@Valid @ModelAttribute FormDetails formDetails, BindingResult result, HttpSession session, Model model) {

        String name = session.getAttribute("name").toString();
        String email = session.getAttribute("email").toString();
        model.addAttribute("email", email);
        model.addAttribute("name", name);
        formDetails.setName(name);
        formDetails.setEmail(email);

        movieList = apiController.getMovies();
        model.addAttribute("movieList", movieList);

        if (result.hasErrors()) {
            System.out.println("Errors Detected");
            System.out.println(result.getAllErrors());
            return "edit";
        } 

        uniqueKey = session.getAttribute("uniquekey").toString();
        formDetails.setSpecialKey(uniqueKey);

        String key = session.getAttribute("key").toString();

        movieSvc.editBooking(uniqueKey, formDetails, key);

        return "editsuccess";

    }
}
