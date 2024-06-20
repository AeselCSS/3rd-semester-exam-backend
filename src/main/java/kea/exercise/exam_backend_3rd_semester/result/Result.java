package kea.exercise.exam_backend_3rd_semester.result;

import jakarta.persistence.*;
import kea.exercise.exam_backend_3rd_semester.discipline.Discipline;
import kea.exercise.exam_backend_3rd_semester.participant.Participant;
import kea.exercise.exam_backend_3rd_semester.resultType.ResultType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ResultType resultType;

    private LocalDate date;

    private Integer resultValue;

    @ManyToOne
    private Participant participant;

    @ManyToOne
    private Discipline discipline;

    public Result(ResultType resultType, LocalDate date, int resultValue, Participant participant, Discipline discipline) {
        this.resultType = resultType;
        this.date = date;
        this.resultValue = resultValue;
        this.participant = participant;
        this.discipline = discipline;
    }
}
