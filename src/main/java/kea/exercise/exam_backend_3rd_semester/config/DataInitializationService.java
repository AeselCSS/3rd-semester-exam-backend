package kea.exercise.exam_backend_3rd_semester.config;

import jakarta.transaction.Transactional;
import kea.exercise.exam_backend_3rd_semester.discipline.Discipline;
import kea.exercise.exam_backend_3rd_semester.discipline.DisciplineRepository;
import kea.exercise.exam_backend_3rd_semester.discipline.DisciplineType;
import kea.exercise.exam_backend_3rd_semester.exception.ResourceNotFoundException;
import kea.exercise.exam_backend_3rd_semester.participant.Gender;
import kea.exercise.exam_backend_3rd_semester.participant.Participant;
import kea.exercise.exam_backend_3rd_semester.participant.ParticipantRepository;
import kea.exercise.exam_backend_3rd_semester.resultType.ResultType;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DataInitializationService {
    private final DisciplineRepository disciplineRepository;
    private final ParticipantRepository participantRepository;


    public DataInitializationService(DisciplineRepository disciplineRepository, ParticipantRepository participantRepository) {
        this.disciplineRepository = disciplineRepository;
        this.participantRepository = participantRepository;
    }

    @Transactional
    public void initializeData() {
        if (disciplineRepository.count() == 0) {
            createDisciplines();
        }
        if (participantRepository.count() == 0) {
            createParticipants();
        }
    }

    private void createDisciplines() {
        // create disciplines
        List<Discipline> disciplines = List.of(
                // Running disciplines
                new Discipline("100m", DisciplineType.RUNNING, ResultType.TIME),
                new Discipline("200m", DisciplineType.RUNNING, ResultType.TIME),
                new Discipline("400m", DisciplineType.RUNNING, ResultType.TIME),
                new Discipline("800m", DisciplineType.RUNNING, ResultType.TIME),
                new Discipline("1500m", DisciplineType.RUNNING, ResultType.TIME),
                new Discipline("5000m", DisciplineType.RUNNING, ResultType.TIME),
                new Discipline("half marathon", DisciplineType.RUNNING, ResultType.TIME),
                new Discipline("marathon", DisciplineType.RUNNING, ResultType.TIME),
                new Discipline("100m hurdles", DisciplineType.RUNNING, ResultType.TIME),
                new Discipline("400m hurdles", DisciplineType.RUNNING, ResultType.TIME),
                new Discipline("3000m steeplechase", DisciplineType.RUNNING, ResultType.TIME),
                new Discipline("4x100m relay", DisciplineType.RUNNING, ResultType.TIME),
                new Discipline("4x400m relay", DisciplineType.RUNNING, ResultType.TIME),
                // Jumping disciplines
                new Discipline("high jump", DisciplineType.JUMPING, ResultType.DISTANCE),
                new Discipline("pole vault", DisciplineType.JUMPING, ResultType.DISTANCE),
                new Discipline("long jump", DisciplineType.JUMPING, ResultType.DISTANCE),
                new Discipline("triple jump", DisciplineType.JUMPING, ResultType.DISTANCE),
                // Throwing disciplines
                new Discipline("shot put", DisciplineType.THROWING, ResultType.DISTANCE),
                new Discipline("discus throw", DisciplineType.THROWING, ResultType.DISTANCE),
                new Discipline("hammer throw", DisciplineType.THROWING, ResultType.DISTANCE),
                new Discipline("javelin throw", DisciplineType.THROWING, ResultType.DISTANCE),
                // Combined events
                new Discipline("heptathlon", DisciplineType.COMBINED_EVENTS, ResultType.POINTS),
                new Discipline("decathlon", DisciplineType.COMBINED_EVENTS, ResultType.POINTS)
        );

        disciplineRepository.saveAll(disciplines);
    }

    private void createParticipants() {
        // create participants
        Participant johnDoe = new Participant("John Michael Doe", Gender.MALE, LocalDate.of(1990, 5, 15), "Copenhagen Athletics Club");
        Participant janeDoe = new Participant("Jane Marie Doe", Gender.FEMALE, LocalDate.of(1992, 8, 25), "Copenhagen Athletics Club");
        Participant jackSmith = new Participant("Jack William Smith", Gender.MALE, LocalDate.of(1995, 3, 10), "Roskilde Athletics Club");
        Participant jillSmith = new Participant("Jill Elizabeth Smith", Gender.FEMALE, LocalDate.of(1997, 11, 20), "Roskilde Athletics Club");
        Participant jamesBrown = new Participant("James Robert Brown", Gender.MALE, LocalDate.of(1988, 7, 5), "Odense Athletics Club");
        Participant jenniferBrown = new Participant("Jennifer Anne Brown", Gender.FEMALE, LocalDate.of(1991, 9, 30), "Odense Athletics Club");
        Participant joshuaJohnson = new Participant("Josh James Johnson", Gender.MALE, LocalDate.of(1993, 1, 1), "Aarhus Athletics Club");
        Participant jessicaJohnson = new Participant("Jessica Mary Johnson", Gender.FEMALE, LocalDate.of(1996, 4, 15), "Aarhus Athletics Club");

        // add disciplines to participants
        johnDoe.addDiscipline(disciplineRepository.findByName("100m").orElseThrow(() -> new ResourceNotFoundException("Discipline not found")));
        johnDoe.addDiscipline(disciplineRepository.findByName("200m").orElseThrow(() -> new ResourceNotFoundException("Discipline not found")));
        johnDoe.addDiscipline(disciplineRepository.findByName("400m").orElseThrow(() -> new ResourceNotFoundException("Discipline not found")));
        johnDoe.addDiscipline(disciplineRepository.findByName("800m").orElseThrow(() -> new ResourceNotFoundException("Discipline not found")));

        janeDoe.addDiscipline(disciplineRepository.findByName("100m").orElseThrow(() -> new ResourceNotFoundException("Discipline not found")));
        janeDoe.addDiscipline(disciplineRepository.findByName("200m").orElseThrow(() -> new ResourceNotFoundException("Discipline not found")));
        janeDoe.addDiscipline(disciplineRepository.findByName("100m hurdles").orElseThrow(() -> new ResourceNotFoundException("Discipline not found")));
        janeDoe.addDiscipline(disciplineRepository.findByName("400m hurdles").orElseThrow(() -> new ResourceNotFoundException("Discipline not found")));

        jackSmith.addDiscipline(disciplineRepository.findByName("long jump").orElseThrow(() -> new ResourceNotFoundException("Discipline not found")));
        jackSmith.addDiscipline(disciplineRepository.findByName("triple jump").orElseThrow(() -> new ResourceNotFoundException("Discipline not found")));

        jillSmith.addDiscipline(disciplineRepository.findByName("high jump").orElseThrow(() -> new ResourceNotFoundException("Discipline not found")));
        jillSmith.addDiscipline(disciplineRepository.findByName("pole vault").orElseThrow(() -> new ResourceNotFoundException("Discipline not found")));

        jamesBrown.addDiscipline(disciplineRepository.findByName("shot put").orElseThrow(() -> new ResourceNotFoundException("Discipline not found")));
        jamesBrown.addDiscipline(disciplineRepository.findByName("discus throw").orElseThrow(() -> new ResourceNotFoundException("Discipline not found")));
        jamesBrown.addDiscipline(disciplineRepository.findByName("hammer throw").orElseThrow(() -> new ResourceNotFoundException("Discipline not found")));
        jamesBrown.addDiscipline(disciplineRepository.findByName("javelin throw").orElseThrow(() -> new ResourceNotFoundException("Discipline not found")));

        jenniferBrown.addDiscipline(disciplineRepository.findByName("heptathlon").orElseThrow(() -> new ResourceNotFoundException("Discipline not found")));
        jenniferBrown.addDiscipline(disciplineRepository.findByName("3000m steeplechase").orElseThrow(() -> new ResourceNotFoundException("Discipline not found")));

        joshuaJohnson.addDiscipline(disciplineRepository.findByName("decathlon").orElseThrow(() -> new ResourceNotFoundException("Discipline not found")));

        jessicaJohnson.addDiscipline(disciplineRepository.findByName("5000m").orElseThrow(() -> new ResourceNotFoundException("Discipline not found")));
        jessicaJohnson.addDiscipline(disciplineRepository.findByName("half marathon").orElseThrow(() -> new ResourceNotFoundException("Discipline not found")));
        jessicaJohnson.addDiscipline(disciplineRepository.findByName("marathon").orElseThrow(() -> new ResourceNotFoundException("Discipline not found")));

        participantRepository.saveAll(List.of(johnDoe, janeDoe, jackSmith, jillSmith, jamesBrown, jenniferBrown, joshuaJohnson, jessicaJohnson));
    }
}


