package kea.exercise.exam_backend_3rd_semester.result;

import kea.exercise.exam_backend_3rd_semester.discipline.Discipline;
import kea.exercise.exam_backend_3rd_semester.discipline.DisciplineRepository;
import kea.exercise.exam_backend_3rd_semester.discipline.DisciplineType;
import kea.exercise.exam_backend_3rd_semester.participant.Gender;
import kea.exercise.exam_backend_3rd_semester.participant.Participant;
import kea.exercise.exam_backend_3rd_semester.participant.ParticipantRepository;
import kea.exercise.exam_backend_3rd_semester.resultType.ResultType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ResultControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private DisciplineRepository disciplineRepository;

    private Participant participant;
    private Discipline disciplineTime;
    private Discipline disciplineDistance;
    private Discipline disciplinePoints;

    @BeforeEach
    void setUp() {
        disciplineTime = new Discipline("100m", DisciplineType.RUNNING, ResultType.TIME);
        disciplineDistance = new Discipline("Long Jump", DisciplineType.JUMPING, ResultType.DISTANCE);
        disciplinePoints = new Discipline("Decathlon", DisciplineType.COMBINED_EVENTS, ResultType.POINTS);
        disciplineTime = disciplineRepository.save(disciplineTime);
        disciplineDistance = disciplineRepository.save(disciplineDistance);
        disciplinePoints = disciplineRepository.save(disciplinePoints);

        participant = new Participant("John Doe", Gender.MALE, LocalDate.of(1990, 5, 15), "Copenhagen Athletics Club");
        participant.addDiscipline(disciplineTime);
        participant.addDiscipline(disciplineDistance);
        participant.addDiscipline(disciplinePoints);
        participant = participantRepository.save(participant);
    }

    @AfterEach
    void tearDown() {
        resultRepository.deleteAll();
        participantRepository.deleteAll();
        disciplineRepository.deleteAll();
    }

    @Test
    void createTimeResult() {
        ResultRequestDTO requestDTO = new ResultRequestDTO(participant.getId(), disciplineTime.getId(), ResultType.TIME, LocalDate.now(), "00:10:23.45");

        webTestClient.post()
                .uri("/results")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ResultResponseDTO.class)
                .value(resultResponseDTO -> {
                    assertNotNull(resultResponseDTO);
                    assertEquals(requestDTO.resultValue(), resultResponseDTO.formattedValue());
                });
    }

    @Test
    void createDistanceResult() {
        ResultRequestDTO requestDTO = new ResultRequestDTO(participant.getId(), disciplineDistance.getId(), ResultType.DISTANCE, LocalDate.now(), "7.35");

        webTestClient.post()
                .uri("/results")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ResultResponseDTO.class)
                .value(resultResponseDTO -> {
                    assertNotNull(resultResponseDTO);
                    assertEquals(requestDTO.resultValue(), resultResponseDTO.formattedValue());
                });
    }

    @Test
    void createPointsResult() {
        ResultRequestDTO requestDTO = new ResultRequestDTO(participant.getId(), disciplinePoints.getId(), ResultType.POINTS, LocalDate.now(), "9000");

        webTestClient.post()
                .uri("/results")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ResultResponseDTO.class)
                .value(resultResponseDTO -> {
                    assertNotNull(resultResponseDTO);
                    assertEquals(requestDTO.resultValue(), resultResponseDTO.formattedValue());
                });
    }

    @Test
    void updateResult() {
        Result existingResult = new Result(ResultType.TIME, LocalDate.now(), 62345, participant, disciplineTime);
        existingResult = resultRepository.save(existingResult);

        ResultRequestDTO requestDTO = new ResultRequestDTO(participant.getId(), disciplineTime.getId(), ResultType.TIME, LocalDate.now(), "00:09:15.30");

        webTestClient.put()
                .uri("/results/" + existingResult.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ResultResponseDTO.class)
                .value(resultResponseDTO -> {
                    assertNotNull(resultResponseDTO);
                    assertEquals(requestDTO.resultValue(), resultResponseDTO.formattedValue());
                });
    }

    @Test
    void deleteResult() {
        Result existingResult = new Result(ResultType.TIME, LocalDate.now(), 62345, participant, disciplineTime);
        existingResult = resultRepository.save(existingResult);

        webTestClient.delete()
                .uri("/results/" + existingResult.getId())
                .exchange()
                .expectStatus().isNoContent();
    }
}
