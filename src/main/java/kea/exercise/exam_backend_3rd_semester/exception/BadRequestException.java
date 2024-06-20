package kea.exercise.exam_backend_3rd_semester.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}