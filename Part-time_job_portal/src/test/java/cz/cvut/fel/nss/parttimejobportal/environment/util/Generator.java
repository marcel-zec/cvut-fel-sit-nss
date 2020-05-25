package cz.cvut.fel.nss.parttimejobportal.environment.util;

import cz.cvut.fel.nss.parttimejobportal.model.*;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Generator {

    private static final Random RAND = new Random();

    public static int randomInt() {
        return RAND.nextInt();
    }

    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFHIJKLMNOPQRSTUVWXYZ0123456789";

    public static boolean randomBoolean() {
        return RAND.nextBoolean();
    }

    private static String generatePassword(){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            sb.append(ALPHABET.charAt(RAND.nextInt(ALPHABET.length())));
        }
        return sb.toString();
    }

    private static Address generateAddress(AbstractUser user){
        return new Address("City " + randomInt(), "Street " + randomInt(), randomInt(), "Z71" + randomInt(), "Country " + randomInt(), user);
    }


    /**
     * Generates user
     * @return user
     */
    public static User generateUser() {
        String username =  "UserrNo"+ randomInt();
        String email = username + "@imejl.cz";
        final User user = new User(generatePassword(), "Tester", "Testovany", email);
        user.setAddress(generateAddress(user));
        List<TripReview> tripReviews = new ArrayList<>();
        user.setTripReviews(tripReviews);
        //TODO: vygenerovat prazdny list reviewů ?
        return user;
    }


    /**
     * Generates trip
     * @return generated trip
     */
    public static Offer generateTrip() {
       return new Offer("trip no." + randomInt(), randomInt(), "a really good trip","trip" , randomInt(), "GB", randomInt(),null);
   }


    /**
     * Generates date
     * @return date
     */
    public static Date generateDate(){
        long ms;
        ms = 1609459200 + (Math.abs(RAND.nextLong()) % (70L * 365 * 24 * 60 * 60 * 1000));
        return new Date(ms);
    }


    /**
     * Generates earliest date than date in parameter
     * @param latestDate
     * @return date
     */
    public static Date generateDate(Date latestDate){
        return new Date(ThreadLocalRandom.current().nextLong(1609459200, latestDate.getTime()));
    }

    /**
     * Generates random date between earliest and latest dates
     * @param earliestDate
     * @param latestDate
     * @return date
     */
    public static Date generateDate(Date earliestDate, Date latestDate){
        return new Date(ThreadLocalRandom.current().nextLong(earliestDate.getTime(), latestDate.getTime()));
    }

    /**
     * Generates time
     * @return time
     */
    public static Time generateTime(){
        final int millisInDay = 24*60*60*1000;
        return new Time((long)RAND.nextInt(millisInDay));
    }

    /**
     * Generates Achievement
     */
//    public static Achievement generateAchievement(){
//        //TODO - opravit ak je potrebne
////        return new Achievement("Achievement no." + randomInt(), "Really good achiev.", "icon");
//    }

    /**
     * Generates Category
     */
    public static Category generateCategory(){
        return new Category("Category no." + randomInt());
    }

}