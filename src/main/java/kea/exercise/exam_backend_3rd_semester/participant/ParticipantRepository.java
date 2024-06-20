package kea.exercise.exam_backend_3rd_semester.participant;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    Optional<Participant> findByFullNameIgnoreCase(String name);
}
