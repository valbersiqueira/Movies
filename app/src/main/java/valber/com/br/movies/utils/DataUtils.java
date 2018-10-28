package valber.com.br.movies.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataUtils {

    public static String stringToLocale(String data) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = format.parse(data);
        } catch (ParseException e) {
            Log.e(DataUtils.class.getSimpleName(), e.getMessage());
        }
        return format2.format(date);
    }

    public static String dateToString(){
        SimpleDateFormat format  = new SimpleDateFormat("dd/MM/yyyy");

        return format.format(new Date());
    }

    public static Date stringToDate(String data){
        SimpleDateFormat format  = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = format.parse(data);
        } catch (ParseException e) {
            Log.e(DataUtils.class.getSimpleName(), e.getMessage());
        }
        return date;
    }

}
