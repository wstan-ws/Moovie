package vttp.iss.privatemoviebooking.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class FormDetails {
    
    @NotEmpty(message = "Name is a Mandatory Field")
    @Size(max = 50, message = "Exceeded maximum of 50 characters")
    private String name;

    @NotEmpty(message = "Email is a Mandatory Field")
    @Email(message = "Must be in email format")
    @Size(max = 50, message = "Exceeded maximum of 50 characters")
    private String email;

    @NotNull(message = "Date is a Mandatory Field")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Future(message = "Invalid Date Selection")
    private Date date;

    private String time;

    private String room;

    private String movie;

    private String specialKey;

    public FormDetails() {
    }

    public FormDetails(String name, String email, Date date, String time, String room) {
        this.name = name;
        this.email = email;
        this.date = date;
        this.time = time;
        this.room = room;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getMovie() {
        return movie;
    }

    public void setMovie(String movie) {
        this.movie = movie;
    }

    public String getSpecialKey() {
        return specialKey;
    }

    public void setSpecialKey(String specialKey) {
        this.specialKey = specialKey;
    }

    @Override
    public String toString() {
        return "FormDetails [name=" + name + ", email=" + email + ", date=" + date + ", time=" + time + ", room=" + room
                + "]";
    }

}
