package com.comcast.xideo.viewmodel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.text.SpannableStringBuilder;
import gueei.binding.Converter;
import gueei.binding.IObservable;

public class DATE_FORMAT extends Converter<CharSequence> {

    public DATE_FORMAT(IObservable<?>[] dependents) {
            super(CharSequence.class, dependents);
    }

    @Override
    public CharSequence calculateValue(Object... args) throws Exception {
            int len = args.length;
            String format = "HH:mm";
            SpannableStringBuilder result = new SpannableStringBuilder("");
            for(int i=0; i<len; i++){
                    if (args[i]==null) continue;
                    if (args[i] instanceof CharSequence)
                            format = ((CharSequence)args[i]).toString();
                    else
                    	if(args[i] instanceof Number){
                    		long duration = ((Long) args[i]).longValue() * 60 * 60;
                    		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
                    		String s = sdf.format(new Date(duration - TimeZone.getDefault().getRawOffset()));
                    		result.append(s);
                    	}
            }
            return result.toString();
    }
}