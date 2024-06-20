package kea.exercise.exam_backend_3rd_semester.participant;

import java.util.List;

public record ParticipantResponseDTO(
        Long id,
        String fullName,
        Gender gender,
        Integer age,
        AgeGroup ageGroup,
        String club,
        List<String> disciplines
) {

}
