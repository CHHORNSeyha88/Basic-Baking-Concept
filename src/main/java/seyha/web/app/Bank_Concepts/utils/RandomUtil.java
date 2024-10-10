package seyha.web.app.Bank_Concepts.utils;

public class RandomUtil {
    public Long generateRandomString(int length) {

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int digit = (int) (Math.random()*10);
            sb.append(digit);
        }
        return Long.parseLong(sb.toString());

    }
}
