package kea.exercise.exam_backend_3rd_semester.config;

import jakarta.transaction.Transactional;
import kea.exercise.exam_backend_3rd_semester.discipline.Discipline;
import kea.exercise.exam_backend_3rd_semester.discipline.DisciplineRepository;
import kea.exercise.exam_backend_3rd_semester.discipline.DisciplineType;
import kea.exercise.exam_backend_3rd_semester.exception.ResourceNotFoundException;
import kea.exercise.exam_backend_3rd_semester.participant.Gender;
import kea.exercise.exam_backend_3rd_semester.participant.Participant;
import kea.exercise.exam_backend_3rd_semester.participant.ParticipantRepository;
import kea.exercise.exam_backend_3rd_semester.result.Result;
import kea.exercise.exam_backend_3rd_semester.result.ResultRepository;
import kea.exercise.exam_backend_3rd_semester.result.ResultUtils;
import kea.exercise.exam_backend_3rd_semester.resultType.ResultType;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

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
                new Discipline("Half Marathon", DisciplineType.RUNNING, ResultType.TIME),
                new Discipline("Marathon", DisciplineType.RUNNING, ResultType.TIME),
                new Discipline("100m Hurdles", DisciplineType.RUNNING, ResultType.TIME),
                new Discipline("400m Hurdles", DisciplineType.RUNNING, ResultType.TIME),
                new Discipline("3000m Steeplechase", DisciplineType.RUNNING, ResultType.TIME),
                new Discipline("4x100m Relay", DisciplineType.RUNNING, ResultType.TIME),
                new Discipline("4x400m Relay", DisciplineType.RUNNING, ResultType.TIME),
                // Jumping disciplines
                new Discipline("High Jump", DisciplineType.JUMPING, ResultType.DISTANCE),
                new Discipline("Pole Vault", DisciplineType.JUMPING, ResultType.DISTANCE),
                new Discipline("Long Jump", DisciplineType.JUMPING, ResultType.DISTANCE),
                new Discipline("Triple Jump", DisciplineType.JUMPING, ResultType.DISTANCE),
                // Throwing disciplines
                new Discipline("Shot Put", DisciplineType.THROWING, ResultType.DISTANCE),
                new Discipline("Discus Throw", DisciplineType.THROWING, ResultType.DISTANCE),
                new Discipline("Hammer Throw", DisciplineType.THROWING, ResultType.DISTANCE),
                new Discipline("Javelin Throw", DisciplineType.THROWING, ResultType.DISTANCE),
                // Combined events
                new Discipline("Heptathlon", DisciplineType.COMBINED_EVENTS, ResultType.POINTS),
                new Discipline("Decathlon", DisciplineType.COMBINED_EVENTS, ResultType.POINTS)
        );

        disciplineRepository.saveAll(disciplines);
    }

    private void createParticipants() {
        List<Discipline> disciplines = disciplineRepository.findAll();
        List<Participant> participants = List.of(
                // CHILD age group
                new Participant("Martin Andersen", Gender.MALE, LocalDate.of(2015, 5, 15), "Copenhagen Athletics Club"),
                new Participant("Greta Jørgensen", Gender.FEMALE, LocalDate.of(2014, 8, 25), "Copenhagen Athletics Club"),
                new Participant("Theo Thrane", Gender.MALE, LocalDate.of(2013, 3, 10), "Roskilde Athletics Club"),
                new Participant("Ellen Larsen", Gender.FEMALE, LocalDate.of(2012, 11, 20), "Roskilde Athletics Club"),
                new Participant("August Sørensen", Gender.MALE, LocalDate.of(2015, 7, 5), "Odense Athletics Club"),
                new Participant("Wilma Hansen", Gender.FEMALE, LocalDate.of(2014, 9, 30), "Odense Athletics Club"),
                new Participant("Severin Clausen", Gender.MALE, LocalDate.of(2013, 1, 1), "Aarhus Athletics Club"),
                new Participant("Clara Hansen", Gender.FEMALE, LocalDate.of(2012, 4, 15), "Aarhus Athletics Club"),
                new Participant("Albert Nielsen", Gender.MALE, LocalDate.of(2015, 6, 12), "Aarhus Athletics Club"),
                new Participant("Tilde Mogensen", Gender.FEMALE, LocalDate.of(2014, 10, 18), "Copenhagen Athletics Club"),

                // YOUTH age group
                new Participant("Emil Petersen", Gender.MALE, LocalDate.of(2010, 5, 15), "Copenhagen Athletics Club"),
                new Participant("Emilie Petersen", Gender.FEMALE, LocalDate.of(2009, 8, 25), "Copenhagen Athletics Club"),
                new Participant("Frederik Olsen", Gender.MALE, LocalDate.of(2008, 3, 10), "Roskilde Athletics Club"),
                new Participant("Louise Olsen", Gender.FEMALE, LocalDate.of(2007, 11, 20), "Roskilde Athletics Club"),
                new Participant("Jonas Eriksen", Gender.MALE, LocalDate.of(2010, 7, 5), "Odense Athletics Club"),
                new Participant("Victoria Eriksen", Gender.FEMALE, LocalDate.of(2009, 9, 30), "Odense Athletics Club"),
                new Participant("Erik Schmidt", Gender.MALE, LocalDate.of(2008, 1, 1), "Aarhus Athletics Club"),
                new Participant("Maja Schmidt", Gender.FEMALE, LocalDate.of(2007, 4, 15), "Aarhus Athletics Club"),
                new Participant("Niels Johansen", Gender.MALE, LocalDate.of(2010, 6, 12), "Aarhus Athletics Club"),
                new Participant("Lærke Johansen", Gender.FEMALE, LocalDate.of(2009, 10, 18), "Copenhagen Athletics Club"),

                // JUNIOR age group
                new Participant("Simon Poulsen", Gender.MALE, LocalDate.of(2005, 5, 15), "Copenhagen Athletics Club"),
                new Participant("Emma Jensen", Gender.FEMALE, LocalDate.of(2004, 8, 25), "Copenhagen Athletics Club"),
                new Participant("Kasper Knudsen", Gender.MALE, LocalDate.of(2003, 3, 10), "Roskilde Athletics Club"),
                new Participant("Sofia Nielsen", Gender.FEMALE, LocalDate.of(2002, 11, 20), "Roskilde Athletics Club"),
                new Participant("Jacob Frederiksen", Gender.MALE, LocalDate.of(2005, 7, 5), "Odense Athletics Club"),
                new Participant("Ida Hansen", Gender.FEMALE, LocalDate.of(2004, 9, 30), "Odense Athletics Club"),
                new Participant("Anders Holm", Gender.MALE, LocalDate.of(2003, 1, 1), "Aarhus Athletics Club"),
                new Participant("Freja Pedersen", Gender.FEMALE, LocalDate.of(2002, 4, 15), "Aarhus Athletics Club"),
                new Participant("Christian Thomsen", Gender.MALE, LocalDate.of(2005, 6, 12), "Aarhus Athletics Club"),
                new Participant("Anna Andersen", Gender.FEMALE, LocalDate.of(2004, 10, 18), "Copenhagen Athletics Club"),

                // ADULT age group
                new Participant("Martin Sørensen", Gender.MALE, LocalDate.of(1990, 5, 15), "Copenhagen Athletics Club"),
                new Participant("Laura Christensen", Gender.FEMALE, LocalDate.of(1992, 8, 25), "Copenhagen Athletics Club"),
                new Participant("Rasmus Rasmussen", Gender.MALE, LocalDate.of(1995, 3, 10), "Roskilde Athletics Club"),
                new Participant("Julie Larsen", Gender.FEMALE, LocalDate.of(1997, 11, 20), "Roskilde Athletics Club"),
                new Participant("Michael Møller", Gender.MALE, LocalDate.of(1988, 7, 5), "Odense Athletics Club"),
                new Participant("Mathilde Møller", Gender.FEMALE, LocalDate.of(1991, 9, 30), "Odense Athletics Club"),
                new Participant("Henrik Larsen", Gender.MALE, LocalDate.of(1993, 1, 1), "Aarhus Athletics Club"),
                new Participant("Clara Rasmussen", Gender.FEMALE, LocalDate.of(1996, 4, 15), "Aarhus Athletics Club"),
                new Participant("Jens Christensen", Gender.MALE, LocalDate.of(1994, 6, 12), "Aarhus Athletics Club"),
                new Participant("Katrine Sørensen", Gender.FEMALE, LocalDate.of(1993, 10, 18), "Copenhagen Athletics Club"),

                // SENIOR age group
                new Participant("Thomas Andersen", Gender.MALE, LocalDate.of(1960, 5, 15), "Copenhagen Athletics Club"),
                new Participant("Cecilie Thomsen", Gender.FEMALE, LocalDate.of(1958, 8, 25), "Copenhagen Athletics Club"),
                new Participant("Søren Pedersen", Gender.MALE, LocalDate.of(1955, 3, 10), "Roskilde Athletics Club"),
                new Participant("Maria Holm", Gender.FEMALE, LocalDate.of(1953, 11, 20), "Roskilde Athletics Club"),
                new Participant("Peter Hansen", Gender.MALE, LocalDate.of(1962, 7, 5), "Odense Athletics Club"),
                new Participant("Amalie Frederiksen", Gender.FEMALE, LocalDate.of(1961, 9, 30), "Odense Athletics Club"),
                new Participant("Lars Nielsen", Gender.MALE, LocalDate.of(1957, 1, 1), "Aarhus Athletics Club"),
                new Participant("Karoline Knudsen", Gender.FEMALE, LocalDate.of(1954, 4, 15), "Aarhus Athletics Club"),
                new Participant("Mads Jensen", Gender.MALE, LocalDate.of(1958, 6, 12), "Aarhus Athletics Club"),
                new Participant("Isabella Poulsen", Gender.FEMALE, LocalDate.of(1960, 10, 18), "Copenhagen Athletics Club")
        );

        Random random = new Random();

        // Assigning random disciplines to participants
        participants.forEach(participant -> {
            Set<Discipline> assignedDisciplines = new HashSet<>();
            while (assignedDisciplines.size() < 4) {
                assignedDisciplines.add(disciplines.get(random.nextInt(disciplines.size())));
            }
            assignedDisciplines.forEach(participant::addDiscipline);
            participantRepository.save(participant);
        });
    }

    private void createResults() {
        List<Participant> participants = participantRepository.findAll();

        List<Result> results = new ArrayList<>();

        participants.forEach(participant -> {
            participant.getDisciplines().forEach(discipline -> {
                String formattedValue1, formattedValue2;
                formattedValue2 = switch (discipline.getResultType()) {
                    case TIME -> {
                        formattedValue1 = ResultUtils.formatValue(ResultType.TIME, 10 + (int) (Math.random() * 360000)); // Random time in hundredths of a second
                        yield ResultUtils.formatValue(ResultType.TIME, 10 + (int) (Math.random() * 360000)); // Random time in hundredths of a second
                    }
                    case DISTANCE -> {
                        formattedValue1 = ResultUtils.formatValue(ResultType.DISTANCE, 100 + (int) (Math.random() * 10000)); // Random distance in centimeters
                        yield ResultUtils.formatValue(ResultType.DISTANCE, 100 + (int) (Math.random() * 10000)); // Random distance in centimeters
                    }
                    case POINTS -> {
                        formattedValue1 = ResultUtils.formatValue(ResultType.POINTS, 10 + (int) (Math.random() * 10000)); // Random points
                        yield ResultUtils.formatValue(ResultType.POINTS, 10 + (int) (Math.random() * 10000)); // Random points
                    }
                    default ->
                            throw new ResourceNotFoundException("Invalid result type: " + discipline.getResultType());
                };

                results.add(new Result(discipline.getResultType(), LocalDate.of(2024, 6, 1), ResultUtils.parseFormattedValue(discipline.getResultType(), formattedValue1), participant, discipline));
                results.add(new Result(discipline.getResultType(), LocalDate.of(2024, 6, 2), ResultUtils.parseFormattedValue(discipline.getResultType(), formattedValue2), participant, discipline));
            });
        });

        resultRepository.saveAll(results);
    }
}


