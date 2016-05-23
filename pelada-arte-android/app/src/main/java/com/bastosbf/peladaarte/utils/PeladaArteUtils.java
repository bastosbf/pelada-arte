package com.bastosbf.peladaarte.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

/**
 * Created by bastosbf on 18/05/16.
 */
public class PeladaArteUtils {

    public static final int ADD_PELADA_RESULT = 100;
    public static final int ADD_PLAYER_RESULT = 101;

    private static Map<Integer, String> daysOfTheWeek = new HashMap<>();

    static {
        daysOfTheWeek.put(1, "Domingo");
        daysOfTheWeek.put(2, "Segunda");
        daysOfTheWeek.put(3, "Terça");
        daysOfTheWeek.put(4, "Quarta");
        daysOfTheWeek.put(5, "Quinta");
        daysOfTheWeek.put(6, "Sexta");
        daysOfTheWeek.put(7, "Sábado");
    }

    private static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
    static {
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
    }
    public static String getDayOfTheWeek(int day) {
        return daysOfTheWeek.get(day);
    }

    public static int getDayOfTheWeek(String day) {
        Set<Map.Entry<Integer, String>> entries = daysOfTheWeek.entrySet();
        Iterator<Map.Entry<Integer, String>> iterator = entries.iterator();
        while(iterator.hasNext()) {
            Map.Entry<Integer, String> entry = iterator.next();
            if(entry.getValue().equalsIgnoreCase(day)) {
                return entry.getKey();
            }
        }
        return -1;
    }

    public static Date getDate(String time) {
        try {
            return sdf.parse(time);
        } catch (ParseException e) {
            return null;
        }
    }


}
