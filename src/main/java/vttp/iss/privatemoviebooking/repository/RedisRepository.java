package vttp.iss.privatemoviebooking.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import vttp.iss.privatemoviebooking.model.FormDetails;

@Repository
public class RedisRepository {

    @Autowired @Qualifier("database")
    private RedisTemplate<String, FormDetails> template;

    @Autowired @Qualifier("occupancy")
    private RedisTemplate<String, Integer> oTemplate;

    private ValueOperations<String, FormDetails> value;

    private ValueOperations<String, Integer> oValue;

    public void saveDetails(FormDetails details) {

        value = template.opsForValue();
        value.set(details.getSpecialKey(), details);
        
        oValue = oTemplate.opsForValue();
        String key = details.getDate() + details.getTime() + details.getRoom();
        if (oValue.get(key) == null) {
            oValue.set(key, 1);
        } else {
            oValue.increment(key);
        }
        
    }

    public FormDetails getDetails(String uniqueKey) {

        value = template.opsForValue();
        FormDetails retrievedDetails = value.get(uniqueKey);

        return retrievedDetails;
    }

    public Integer remainingOccupancy(FormDetails details) {

        oValue = oTemplate.opsForValue();
        String key = details.getDate() + details.getTime() + details.getRoom();
        
        Integer remaining = 0;
        if (oValue.get(key) == null) {
            remaining = 0;
        } else {
            remaining = oValue.get(key);
        }

        return remaining;
    }

    public void deleteBooking(String uniqueKey, String key) {

        oValue = oTemplate.opsForValue();
        oValue.decrement(key);

        template.delete(uniqueKey);
    }

    public void editBooking(String uniqueKey, FormDetails formDetails, String key) {

        value = template.opsForValue();
        value.getAndSet(uniqueKey, formDetails);

        oTemplate.delete(key);
        String newKey = formDetails.getDate() + formDetails.getTime() + formDetails.getRoom();
        if (oValue.get(newKey) == null) {
            oValue.set(newKey, 1);
        } else {
            oValue.increment(newKey);
        }
    }
    
}
