package vttp.iss.privatemoviebooking.services;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import vttp.iss.privatemoviebooking.model.FormDetails;
import vttp.iss.privatemoviebooking.repository.RedisRepository;

@Service
public class MovieService {

    @Autowired
    RedisRepository redisRepo;

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
