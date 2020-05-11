package com.terryyessfung.whatsins;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Helper {
    private static Helper sHelper;
    public Helper(){ }

    public static Helper getInstance(){
        if(sHelper == null){
            sHelper = new Helper();
        }
        return sHelper;
    }

    /**
     * Change ISODate which is from mongoDB to simple format date
     * eg: 2020-05-07T03:52:12.502Z to 2020-05-07
     * **/
    public String formatDate(String date){
            String result = "";
            try{
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.CHINA);
                SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
                Date d = format.parse(date);
                result = format2.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean isShaking(float[] values, int shakeValue){
        float x = values[0], y = values[1], z = values[2];
        if(Math.abs(x) > shakeValue || Math.abs(y) > shakeValue || Math.abs(z) > shakeValue){
            return true;
        }
        return false;
    }
}
