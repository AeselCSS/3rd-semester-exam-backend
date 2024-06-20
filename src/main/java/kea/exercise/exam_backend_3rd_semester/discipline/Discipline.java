package kea.exercise.exam_backend_3rd_semester.discipline;

import jakarta.persistence.*;
import kea.exercise.exam_backend_3rd_semester.participant.Participant;
import kea.exercise.exam_backend_3rd_semester.resultType.ResultType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Discipline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private DisciplineType disciplineType;

    @Enumerated(EnumType.STRING)
    private ResultType resultType;

    @ManyToMany(mappedBy = "disciplines")
    private Set<Participant> participants = new HashSet<>();

    public Discipline(String name, DisciplineType disciplineType, ResultType resultType) {
        this.name = name;
        this.disciplineType = disciplineType;
        this.resultType = resultType;
    }
}
