package kea.exercise.exam_backend_3rd_semester.result;

import kea.exercise.exam_backend_3rd_semester.resultType.ResultType;

import java.time.LocalDate;

public record ResultResponseDTO(
        Long id,
        ResultType resultType,
        LocalDate date,
        String formattedValue,
        Long participantId,
        Long disciplineId,
        String participantName,
        String disciplineName
) {
    public ResultResponseDTO(Result result) {
        this(
                result.getId(),
                result.getResultType(),
                result.getDate(),
                ResultUtils.formatValue(result.getResultType(), result.getResultValue()),
                result.getParticipant().getId(),
                result.getDiscipline().getId(),
                result.getParticipant().getFullName(),
                result.getDiscipline().getName()
        );
    }
}