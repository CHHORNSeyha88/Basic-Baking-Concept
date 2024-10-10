package seyha.web.app.Bank_Concepts.utils;

public class RandomUtil {
//    public Long generateRandomString(int length) {
//
//    }

    public long generateRandom(int length) {
        StringBuilder sb = new StringBuilder("00"); // Start with "00"
        for (int i = 0; i < length; i++) { // Generate 'length' random digits
            int digit = (int) (Math.random() * 10);
            sb.append(digit);
        }
        return Long.parseLong(sb.toString()); // Return the result as a String to preserve leading zeros
    }

}
