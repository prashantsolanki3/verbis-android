package com.blackshift.verbis.utils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.sql.Timestamp;

/**
 * Package com.blackshift.verbis.utils
 * <p>
 * Created by Prashant on 3/20/2016.
 * <p>
 * Email: solankisrp2@gmail.com
 * Github: @prashantsolanki3
 */
//TODO: complete this
public class DateUtils {

    public static DateTime getDateTimeUTC(){
        return DateTime.now(DateTimeZone.UTC);
    }

    public static long getTimestampUTC(){
        return new Timestamp(DateUtils.getDateTimeUTC().getMillis()).getTime();
    }

    public static DateTime getDateTimeLocal(){
        return DateTime.now(DateTimeZone.getDefault());
    }

    public static DateTime convertUTCToLocal(String dateTime){
        DateTime dT = new DateTime(dateTime,DateTimeZone.UTC);
        return convertUTCToLocal(dT);
    }

    public static DateTime convertUTCToLocal(DateTime dateTime){

        return dateTime;
    }



    //TODO: localToUTC,Convert into timestamp, pass timestamp to firebase, convert to DateTime from timestamp keeping in mind the TimeZone.


}
