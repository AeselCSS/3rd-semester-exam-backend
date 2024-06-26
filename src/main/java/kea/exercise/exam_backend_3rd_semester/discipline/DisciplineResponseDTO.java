package kea.exercise.exam_backend_3rd_semester.discipline;

import kea.exercise.exam_backend_3rd_semester.resultType.ResultType;

import java.util.List;

public record DisciplineResponseDTO(
        Long id,
        String name,
        DisciplineType disciplineType,
        ResultType resultType,
        List<String> participants
) {
}
