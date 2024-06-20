package kea.exercise.exam_backend_3rd_semester.participant;

import java.time.LocalDate;
import java.util.List;

public record ParticipantRequestDTO(
        Long id,
        String fullName,
        Gender gender,
        LocalDate dateOfBirth,
        String club,
        List<String> disciplines
) {
}
