package kea.exercise.exam_backend_3rd_semester.config;

import jakarta.transaction.Transactional;
import kea.exercise.exam_backend_3rd_semester.discipline.Discipline;
import kea.exercise.exam_backend_3rd_semester.discipline.DisciplineRepository;
import kea.exercise.exam_backend_3rd_semester.discipline.DisciplineType;
import kea.exercise.exam_backend_3rd_semester.exception.ResourceNotFoundException;
import kea.exercise.exam_backend_3rd_semester.participant.Gender;
import kea.exercise.exam_backend_3rd_semester.participant.Participant;
import kea.exercise.exam_backend_3rd_semester.participant.ParticipantRepository;
import kea.exercise.exam_backend_3rd_semester.result.*;
import kea.exercise.exam_backend_3rd_semester.resultType.ResultType;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DataInitializationService {
    private final DisciplineRepository disciplineRepository;
    private final ParticipantRepository participantRepository;
    private final ResultRepository resultRepository;




    public DataInitializationService(DisciplineRepository disciplineRepository, ParticipantRepository participantRepository, ResultRepository resultRepository) {
        this.disciplineRepository = disciplineRepository;
        this.participantRepository = participantRepository;
        this.resultRepository = resultRepository;
    }

    @Transactional
    public void initializeData() {
        if (disciplineRepository.count() == 0) {
            createDisciplines();
        }
        if (participantRepository.count() == 0) {
            createParticipants();
        }
        if (resultRepository.count() == 0) {
            createResults();
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

    private void createResults() {
        List<Discipline> disciplines = disciplineRepository.findAll();
        List<Participant> participants = participantRepository.findAll();

        Discipline hundredMeters = disciplines.stream().filter(d -> d.getName().equals("100m")).findFirst().orElseThrow(() -> new ResourceNotFoundException("Discipline not found"));
        Discipline twoHundredMeters = disciplines.stream().filter(d -> d.getName().equals("200m")).findFirst().orElseThrow(() -> new ResourceNotFoundException("Discipline not found"));
        Discipline hundredMetersHurdles = disciplines.stream().filter(d -> d.getName().equals("100m hurdles")).findFirst().orElseThrow(() -> new ResourceNotFoundException("Discipline not found"));
        Discipline fourHundredMetersHurdles = disciplines.stream().filter(d -> d.getName().equals("400m hurdles")).findFirst().orElseThrow(() -> new ResourceNotFoundException("Discipline not found"));
        Discipline longJump = disciplines.stream().filter(d -> d.getName().equals("long jump")).findFirst().orElseThrow(() -> new ResourceNotFoundException("Discipline not found"));
        Discipline highJump = disciplines.stream().filter(d -> d.getName().equals("high jump")).findFirst().orElseThrow(() -> new ResourceNotFoundException("Discipline not found"));
        Discipline shotPut = disciplines.stream().filter(d -> d.getName().equals("shot put")).findFirst().orElseThrow(() -> new ResourceNotFoundException("Discipline not found"));
        Discipline discusThrow = disciplines.stream().filter(d -> d.getName().equals("discus throw")).findFirst().orElseThrow(() -> new ResourceNotFoundException("Discipline not found"));
        Discipline hammerThrow = disciplines.stream().filter(d -> d.getName().equals("hammer throw")).findFirst().orElseThrow(() -> new ResourceNotFoundException("Discipline not found"));
        Discipline javelinThrow = disciplines.stream().filter(d -> d.getName().equals("javelin throw")).findFirst().orElseThrow(() -> new ResourceNotFoundException("Discipline not found"));
        Discipline heptathlon = disciplines.stream().filter(d -> d.getName().equals("heptathlon")).findFirst().orElseThrow(() -> new ResourceNotFoundException("Discipline not found"));
        Discipline threeThousandMetersSteeplechase = disciplines.stream().filter(d -> d.getName().equals("3000m steeplechase")).findFirst().orElseThrow(() -> new ResourceNotFoundException("Discipline not found"));
        Discipline decathlon = disciplines.stream().filter(d -> d.getName().equals("decathlon")).findFirst().orElseThrow(() -> new ResourceNotFoundException("Discipline not found"));
        Discipline fiveThousandMeters = disciplines.stream().filter(d -> d.getName().equals("5000m")).findFirst().orElseThrow(() -> new ResourceNotFoundException("Discipline not found"));
        Discipline halfMarathon = disciplines.stream().filter(d -> d.getName().equals("half marathon")).findFirst().orElseThrow(() -> new ResourceNotFoundException("Discipline not found"));
        Discipline marathon = disciplines.stream().filter(d -> d.getName().equals("marathon")).findFirst().orElseThrow(() -> new ResourceNotFoundException("Discipline not found"));

        Participant johnDoe = participants.stream().filter(p -> p.getFullName().equals("John Michael Doe")).findFirst().orElseThrow(() -> new ResourceNotFoundException("Participant not found"));
        Participant janeDoe = participants.stream().filter(p -> p.getFullName().equals("Jane Marie Doe")).findFirst().orElseThrow(() -> new ResourceNotFoundException("Participant not found"));
        Participant jackSmith = participants.stream().filter(p -> p.getFullName().equals("Jack William Smith")).findFirst().orElseThrow(() -> new ResourceNotFoundException("Participant not found"));
        Participant jillSmith = participants.stream().filter(p -> p.getFullName().equals("Jill Elizabeth Smith")).findFirst().orElseThrow(() -> new ResourceNotFoundException("Participant not found"));
        Participant jamesBrown = participants.stream().filter(p -> p.getFullName().equals("James Robert Brown")).findFirst().orElseThrow(() -> new ResourceNotFoundException("Participant not found"));
        Participant jenniferBrown = participants.stream().filter(p -> p.getFullName().equals("Jennifer Anne Brown")).findFirst().orElseThrow(() -> new ResourceNotFoundException("Participant not found"));
        Participant joshuaJohnson = participants.stream().filter(p -> p.getFullName().equals("Josh James Johnson")).findFirst().orElseThrow(() -> new ResourceNotFoundException("Participant not found"));
        Participant jessicaJohnson = participants.stream().filter(p -> p.getFullName().equals("Jessica Mary Johnson")).findFirst().orElseThrow(() -> new ResourceNotFoundException("Participant not found"));

        List<Result> results = List.of(
            new Result(ResultType.TIME, LocalDate.of(2021, 6, 1), 10, johnDoe, hundredMeters),
                new Result(ResultType.TIME, LocalDate.of(2021, 6, 2), 20, johnDoe, twoHundredMeters),
                new Result(ResultType.TIME, LocalDate.of(2021, 6, 3), 30, johnDoe, fourHundredMetersHurdles),
                new Result(ResultType.TIME, LocalDate.of(2021, 6, 4), 40, johnDoe, fourHundredMetersHurdles),

                new Result(ResultType.TIME, LocalDate.of(2021, 6, 5), 50, janeDoe, hundredMeters),
                new Result(ResultType.TIME, LocalDate.of(2021, 6, 6), 60, janeDoe, twoHundredMeters),
                new Result(ResultType.TIME, LocalDate.of(2021, 6, 7), 70, janeDoe, hundredMetersHurdles),
                new Result(ResultType.TIME, LocalDate.of(2021, 6, 8), 80, janeDoe, fourHundredMetersHurdles),

                new Result(ResultType.DISTANCE, LocalDate.of(2021, 6, 9), 230, jackSmith, longJump),
                new Result(ResultType.DISTANCE, LocalDate.of(2021, 6, 10), 240, jackSmith, longJump),
                new Result(ResultType.DISTANCE, LocalDate.of(2021, 6, 11), 250, jackSmith, longJump),
                new Result(ResultType.DISTANCE, LocalDate.of(2021, 6, 12), 260, jackSmith, longJump),

                new Result(ResultType.DISTANCE, LocalDate.of(2021, 6, 13), 170, jillSmith, highJump),
                new Result(ResultType.DISTANCE, LocalDate.of(2021, 6, 14), 180, jillSmith, highJump),
                new Result(ResultType.DISTANCE, LocalDate.of(2021, 6, 15), 190, jillSmith, highJump),
                new Result(ResultType.DISTANCE, LocalDate.of(2021, 6, 16), 200, jillSmith, highJump),

                new Result(ResultType.DISTANCE, LocalDate.of(2021, 6, 17), 350, jamesBrown, shotPut),
                new Result(ResultType.DISTANCE, LocalDate.of(2021, 6, 18), 450, jamesBrown, discusThrow),
                new Result(ResultType.DISTANCE, LocalDate.of(2021, 6, 19), 375, jamesBrown, hammerThrow),
                new Result(ResultType.DISTANCE, LocalDate.of(2021, 6, 20), 400, jamesBrown, javelinThrow),

                new Result(ResultType.POINTS, LocalDate.of(2021, 6, 21), 5000, jenniferBrown, heptathlon),
                new Result(ResultType.TIME, LocalDate.of(2021, 6, 22), 100, jenniferBrown, threeThousandMetersSteeplechase),

                new Result(ResultType.POINTS, LocalDate.of(2021, 6, 23), 9000, joshuaJohnson, decathlon),

                new Result(ResultType.TIME, LocalDate.of(2021, 6, 24), 200, jessicaJohnson, fiveThousandMeters),
                new Result(ResultType.TIME, LocalDate.of(2021, 6, 25), 300, jessicaJohnson, halfMarathon),
                new Result(ResultType.TIME, LocalDate.of(2021, 6, 26), 400, jessicaJohnson, marathon)
        );

        resultRepository.saveAll(results);
    }
}


