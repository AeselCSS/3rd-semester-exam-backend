package kea.exercise.exam_backend_3rd_semester.result;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResultRepository extends JpaRepository<Result, Long>{
    List<Result> findAllByDisciplineName(String disciplineName);

    void deleteByParticipantId(Long participantId);

    void deleteByParticipantIdAndDisciplineId(Long participantId, Long disciplineId);
}
