package kea.exercise.exam_backend_3rd_semester.result;

import kea.exercise.exam_backend_3rd_semester.exception.BadRequestException;
import kea.exercise.exam_backend_3rd_semester.resultType.ResultType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResultUtilsTest {

    @Test
    void testFormatTimeValue() {
        int timeValue = 62345; // 00:10:23.45
        String formattedValue = ResultUtils.formatValue(ResultType.TIME, timeValue);
        assertEquals("00:10:23.45", formattedValue);
    }

    @Test
    void testParseTimeValue() {
        String formattedValue = "00:10:23.45";
        int parsedValue = ResultUtils.parseFormattedValue(ResultType.TIME, formattedValue);
        assertEquals(62345, parsedValue);
    }

    @Test
    void testFormatDistanceValue() {
        int distanceValue = 735; // 7.35 meters
        String formattedValue = ResultUtils.formatValue(ResultType.DISTANCE, distanceValue);
        assertEquals("7.35", formattedValue);
    }

    @Test
    void testParseDistanceValue() {
        String formattedValue = "7.35";
        int parsedValue = ResultUtils.parseFormattedValue(ResultType.DISTANCE, formattedValue);
        assertEquals(735, parsedValue);
    }

    @Test
    void testFormatPointsValue() {
        int pointsValue = 9000;
        String formattedValue = ResultUtils.formatValue(ResultType.POINTS, pointsValue);
        assertEquals("9000", formattedValue);
    }

    @Test
    void testParsePointsValue() {
        String formattedValue = "9000";
        int parsedValue = ResultUtils.parseFormattedValue(ResultType.POINTS, formattedValue);
        assertEquals(9000, parsedValue);
    }

    @Test
    void testInvalidResultTypeForFormatting() {
        int invalidValue = 0;
        assertThrows(BadRequestException.class, () -> ResultUtils.formatValue(null, invalidValue));
    }

    @Test
    void testInvalidResultTypeForParsing() {
        String invalidValue = "0";
        assertThrows(BadRequestException.class, () -> ResultUtils.parseFormattedValue(null, invalidValue));
    }
}
