package com.leesh.quiz.global.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public interface DateTimeUtils {

    static LocalDateTime convertToLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

}
