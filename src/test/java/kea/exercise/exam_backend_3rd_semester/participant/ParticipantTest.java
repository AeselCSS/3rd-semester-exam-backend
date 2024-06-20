package kea.exercise.exam_backend_3rd_semester.participant;

import kea.exercise.exam_backend_3rd_semester.discipline.Discipline;
import kea.exercise.exam_backend_3rd_semester.discipline.DisciplineType;
import kea.exercise.exam_backend_3rd_semester.resultType.ResultType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
class ParticipantTest {

    private Participant participant;

    @BeforeEach
    void setUp() {
        participant = new Participant("John Doe", Gender.MALE, LocalDate.of(2000, 1, 1), "Club A");
    }

    @Test
void ageGroupIsSetCorrectlyForChildEdgeCase() {
    participant = new Participant("John Doe", Gender.MALE, LocalDate.now().minusYears(6), "Club A");
    assertEquals(AgeGroup.CHILD, participant.getAgeGroup());
}

@Test
void ageGroupIsSetCorrectlyForYouthEdgeCase() {
    participant = new Participant("John Doe", Gender.MALE, LocalDate.now().minusYears(10), "Club A");
    assertEquals(AgeGroup.YOUTH, participant.getAgeGroup());
}

@Test
void ageGroupIsSetCorrectlyForJuniorEdgeCase() {
    participant = new Participant("John Doe", Gender.MALE, LocalDate.now().minusYears(14), "Club A");
    assertEquals(AgeGroup.JUNIOR, participant.getAgeGroup());
}

@Test
void ageGroupIsSetCorrectlyForAdultEdgeCase() {
    participant = new Participant("John Doe", Gender.MALE, LocalDate.now().minusYears(23), "Club A");
    assertEquals(AgeGroup.ADULT, participant.getAgeGroup());
}

@Test
void ageGroupIsSetCorrectlyForSeniorEdgeCase() {
    participant = new Participant("John Doe", Gender.MALE, LocalDate.now().minusYears(41), "Club A");
    assertEquals(AgeGroup.SENIOR, participant.getAgeGroup());
}

@Test
void ageGroupIsSetCorrectlyForNotEligibleEdgeCase() {
    participant = new Participant("John Doe", Gender.MALE, LocalDate.now().minusYears(5), "Club A");
    assertEquals(AgeGroup.NOT_ELIGIBLE, participant.getAgeGroup());
}

@Test
void disciplineIsAddedCorrectly() {
    Discipline discipline = new Discipline("Discipline A", DisciplineType.RUNNING, ResultType.TIME);
    participant.addDiscipline(discipline);
    assertTrue(participant.getDisciplines().contains(discipline));
    assertTrue(discipline.getParticipants().contains(participant));
}
}