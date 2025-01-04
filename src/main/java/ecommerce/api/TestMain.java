package ecommerce.api;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestMain {
    public static void main(String[] args) throws ParseException {
        String dateTimeString = "2022-07-28T13:14:15.123+01:00";
        String str = "2025-01-04T16:22:48.082Z";
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        Date date = isoFormat.parse(str);
        System.out.println(date);
    }
}
