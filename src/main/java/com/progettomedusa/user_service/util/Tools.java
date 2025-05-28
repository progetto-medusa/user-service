package com.progettomedusa.user_service.util;

import org.springframework.stereotype.Component;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

@Component
public class Tools {

    public String getInstant() {
        DateTimeFormatter dateFormatter = ISODateTimeFormat.dateTime();
        DateTime dt = DateTime.now(DateTimeZone.UTC);
        return dt.toString(dateFormatter);
    }

    public boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailRegex);
    }

}
