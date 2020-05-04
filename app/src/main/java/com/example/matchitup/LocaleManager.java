package com.example.matchitup;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;

import java.util.Locale;

public class LocaleManager {

    /**
     * Sets the locale of the application.
     * @param context
     * @param localeCode
     */
    public static void setLocale(Context context, String localeCode) {
        Locale locale = new Locale(localeCode);
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            conf.setLocale(locale);
        }
        else {
            conf.locale = locale;
        }

        res.updateConfiguration(conf, dm);
    }
}
