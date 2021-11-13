package az.mm.delivery.identity.util;

import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class FormatterUtilTest {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Test
    void convertToUtilDate_Should_Return_True() throws ParseException {
        LocalDateTime ldt = LocalDateTime.of(2021, 4, 22, 11, 30);
        Date expectedResult = sdf.parse("2021-04-22 11:30");

        assertEquals(expectedResult, FormatterUtil.convertToUtilDate(ldt));
    }

    @Test
    void convertToUtilDate_Should_Return_False() throws ParseException {
        LocalDateTime ldt = LocalDateTime.of(2021, 4, 22, 11, 30);
        Date expectedResult = sdf.parse("2021-04-20 11:30");

        assertNotEquals(expectedResult, FormatterUtil.convertToUtilDate(ldt));
    }

}