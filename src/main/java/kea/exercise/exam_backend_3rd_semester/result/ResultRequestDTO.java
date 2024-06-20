package kea.exercise.exam_backend_3rd_semester.result;

import kea.exercise.exam_backend_3rd_semester.resultType.ResultType;

import java.time.LocalDate;

public record ResultRequestDTO(
        Long participantId,
        Long disciplineId,
        ResultType resultType,
        LocalDate date,
        String resultValue
) {}
