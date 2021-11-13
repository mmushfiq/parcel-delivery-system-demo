package az.mm.delivery.identity.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public final class FormatterUtil {

    private FormatterUtil() {
    }

    public static Date convertToUtilDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

}
