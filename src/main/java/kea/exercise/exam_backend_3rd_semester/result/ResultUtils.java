package kea.exercise.exam_backend_3rd_semester.result;

import kea.exercise.exam_backend_3rd_semester.exception.BadRequestException;
import kea.exercise.exam_backend_3rd_semester.resultType.ResultType;

public class ResultUtils {
    public static String formatValue(ResultType resultType, int resultValue) {
        if (resultType == null) {
            throw new BadRequestException("Result type cannot be null");
        }

        switch (resultType) {
            case TIME:
                int hours = resultValue / 360000;
                int minutes = (resultValue / 6000) % 60;
                int seconds = (resultValue / 100) % 60;
                int hundredths = resultValue % 100;
                return String.format("%02d:%02d:%02d.%02d", hours, minutes, seconds, hundredths);
            case DISTANCE:
                int meters = resultValue / 100;
                int centimeters = resultValue % 100;
                return String.format("%d.%02d", meters, centimeters);
            case POINTS:
                return String.valueOf(resultValue);
            default:
                throw new BadRequestException("Invalid result type: " + resultType);
        }
    }

    public static int parseFormattedValue(ResultType resultType, String formattedValue) {
        if (resultType == null) {
            throw new BadRequestException("Result type cannot be null");
        }

        switch (resultType) {
            case TIME:
                String[] timeParts = formattedValue.split("[:.]");
                int hours = Integer.parseInt(timeParts[0]);
                int minutes = Integer.parseInt(timeParts[1]);
                int seconds = Integer.parseInt(timeParts[2]);
                int hundredths = Integer.parseInt(timeParts[3]);
                return (hours * 3600 + minutes * 60 + seconds) * 100 + hundredths;
            case DISTANCE:
                String[] distanceParts = formattedValue.split("\\.");
                int meters = Integer.parseInt(distanceParts[0]);
                int centimeters = Integer.parseInt(distanceParts[1]);
                return meters * 100 + centimeters;
            case POINTS:
                return Integer.parseInt(formattedValue);
            default:
                throw new BadRequestException("Invalid result type: " + resultType);
        }
    }
}
