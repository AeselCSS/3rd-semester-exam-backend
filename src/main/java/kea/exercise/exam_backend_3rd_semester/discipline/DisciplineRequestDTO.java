package kea.exercise.exam_backend_3rd_semester.discipline;

import kea.exercise.exam_backend_3rd_semester.resultType.ResultType;

public record DisciplineRequestDTO(
        Long id,
        String name,
        DisciplineType disciplineType,
        ResultType resultType
) {
}
