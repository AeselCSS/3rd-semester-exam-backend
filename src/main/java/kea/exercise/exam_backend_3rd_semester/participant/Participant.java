package kea.exercise.exam_backend_3rd_semester.participant;

import jakarta.persistence.*;
import kea.exercise.exam_backend_3rd_semester.discipline.Discipline;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate dateOfBirth;

    private String club;

    @ManyToMany
    @JoinTable(
            name = "participant_discipline",
            joinColumns = @JoinColumn(name = "participant_id"),
            inverseJoinColumns = @JoinColumn(name = "discipline_id"))
    private Set<Discipline> disciplines = new HashSet<>();

    public Participant(String fullName, Gender gender, LocalDate dateOfBirth, String club) {
        this.fullName = fullName;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.club = club;
    }

    public int getAge() {
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
}

    public AgeGroup getAgeGroup() {
        int age = getAge();
    switch (age) {
        case 6, 7, 8, 9 -> {
            return AgeGroup.CHILD;
        }
        case 10, 11, 12, 13 -> {
            return AgeGroup.YOUTH;
        }
        case 14, 15, 16, 17, 18, 19, 20, 21, 22 -> {
            return AgeGroup.JUNIOR;
        }
        case 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40 -> {
            return AgeGroup.ADULT;
        }
        default -> {
            return age >= 41 ? AgeGroup.SENIOR : AgeGroup.NOT_ELIGIBLE;
        }
    }
}

    public void addDiscipline(Discipline discipline) {
        disciplines.add(discipline);
        discipline.getParticipants().add(this);
    }

    public void removeDiscipline(Discipline discipline) {
        disciplines.remove(discipline);
        discipline.getParticipants().remove(this);

    }
}
