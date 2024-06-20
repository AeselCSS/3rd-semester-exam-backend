package kea.exercise.exam_backend_3rd_semester.participant;

import kea.exercise.exam_backend_3rd_semester.discipline.Discipline;
import kea.exercise.exam_backend_3rd_semester.discipline.DisciplineRepository;
import kea.exercise.exam_backend_3rd_semester.discipline.DisciplineRequestDTO;
import kea.exercise.exam_backend_3rd_semester.discipline.DisciplineType;
import kea.exercise.exam_backend_3rd_semester.resultType.ResultType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ParticipantControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private DisciplineRepository disciplineRepository;

    @BeforeEach
    void setup() {
        participantRepository.deleteAll();
        disciplineRepository.deleteAll();
    }

    @Test
    void testCreateParticipant() {
        Discipline discipline = new Discipline("Discipline A", DisciplineType.RUNNING, ResultType.TIME);
        disciplineRepository.save(discipline);

        ParticipantRequestDTO requestDTO = new ParticipantRequestDTO(null, "John Doe", Gender.MALE, LocalDate.of(2000, 1, 1), "Club A", List.of("Discipline A"));

        webTestClient.post()
                .uri("/participant")
                .contentType(APPLICATION_JSON)
                .bodyValue(requestDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ParticipantResponseDTO.class)
                .value(responseDTO -> {
                    assertEquals("John Doe", responseDTO.fullName());
                    assertEquals(Gender.MALE, responseDTO.gender());
                    assertEquals(24, responseDTO.age());
                    assertEquals("Club A", responseDTO.club());
                    assertEquals(1, responseDTO.disciplines().size());
                    assertEquals("Discipline A", responseDTO.disciplines().getFirst());
                });
    }

    @Test
    void testGetParticipantById() {
        Discipline discipline = new Discipline("Discipline A", DisciplineType.RUNNING, ResultType.TIME);
        disciplineRepository.save(discipline);

        Participant participant = new Participant("John Doe", Gender.MALE, LocalDate.of(2000, 1, 1), "Club A");
        participant.addDiscipline(discipline);
        participant = participantRepository.save(participant);

        webTestClient.get()
                .uri("/participant/id/{id}", participant.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(ParticipantResponseDTO.class)
                .value(responseDTO -> {
                    assertEquals("John Doe", responseDTO.fullName());
                    assertEquals(Gender.MALE, responseDTO.gender());
                    assertEquals(24, responseDTO.age());
                    assertEquals("Club A", responseDTO.club());
                    assertEquals(1, responseDTO.disciplines().size());
                    assertEquals("Discipline A", responseDTO.disciplines().getFirst());
                });
    }

    @Test
    void testAddDisciplineToParticipant() {
        Discipline discipline = new Discipline("Discipline A", DisciplineType.RUNNING, ResultType.TIME);
        disciplineRepository.save(discipline);

        Participant participant = new Participant("John Doe", Gender.MALE, LocalDate.of(2000, 1, 1), "Club A");
        participant = participantRepository.save(participant);

        DisciplineRequestDTO disciplineRequestDTO = new DisciplineRequestDTO(null, "Discipline A", DisciplineType.RUNNING, ResultType.TIME);

        webTestClient.patch()
                .uri("/participant/{id}/addDiscipline", participant.getId())
                .contentType(APPLICATION_JSON)
                .bodyValue(disciplineRequestDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ParticipantResponseDTO.class)
                .value(responseDTO -> {
                    assertEquals(1, responseDTO.disciplines().size());
                    assertEquals("Discipline A", responseDTO.disciplines().getFirst());
                });
    }

    @Test
    void testRemoveDisciplineFromParticipant() {
        Discipline discipline = new Discipline("Discipline A", DisciplineType.RUNNING, ResultType.TIME);
        disciplineRepository.save(discipline);

        Participant participant = new Participant("John Doe", Gender.MALE, LocalDate.of(2000, 1, 1), "Club A");
        participant.addDiscipline(discipline);
        participant = participantRepository.save(participant);

        DisciplineRequestDTO disciplineRequestDTO = new DisciplineRequestDTO(null, "Discipline A", DisciplineType.RUNNING, ResultType.TIME);

        webTestClient.patch()
                .uri("/participant/{id}/removeDiscipline", participant.getId())
                .contentType(APPLICATION_JSON)
                .bodyValue(disciplineRequestDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ParticipantResponseDTO.class)
                .value(responseDTO -> {
                    assertEquals(0, responseDTO.disciplines().size());
                });
    }

    @Test
    void testUpdateParticipant() {
        Discipline disciplineA = new Discipline("Discipline A", DisciplineType.RUNNING, ResultType.TIME);
        Discipline disciplineB = new Discipline("Discipline B", DisciplineType.RUNNING, ResultType.TIME);
        disciplineRepository.save(disciplineA);
        disciplineRepository.save(disciplineB);

        Participant participant = new Participant("John Doe", Gender.MALE, LocalDate.of(2000, 1, 1), "Club A");
        participant.addDiscipline(disciplineA);
        participant = participantRepository.save(participant);

        ParticipantRequestDTO requestDTO = new ParticipantRequestDTO(null, "Jane Doe", Gender.FEMALE, LocalDate.of(2000, 1, 1), "Club B", List.of("Discipline B"));

        webTestClient.patch()
                .uri("/participant/{id}", participant.getId())
                .contentType(APPLICATION_JSON)
                .bodyValue(requestDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ParticipantResponseDTO.class)
                .value(responseDTO -> {
                    assertEquals("Jane Doe", responseDTO.fullName());
                    assertEquals(Gender.FEMALE, responseDTO.gender());
                    assertEquals(24, responseDTO.age());
                    assertEquals("Club B", responseDTO.club());
                    assertEquals(1, responseDTO.disciplines().size());
                    assertEquals("Discipline B", responseDTO.disciplines().getFirst());
                });
    }

    @Test
    void testDeleteParticipant() {
        Discipline discipline = new Discipline("Discipline A", DisciplineType.RUNNING, ResultType.TIME);
        disciplineRepository.save(discipline);

        Participant participant = new Participant("John Doe", Gender.MALE, LocalDate.of(2000, 1, 1), "Club A");
        participant.addDiscipline(discipline);
        participant = participantRepository.save(participant);

        webTestClient.delete()
                .uri("/participant/{id}", participant.getId())
                .exchange()
                .expectStatus().isNoContent();

        webTestClient.get()
                .uri("/participant/id/{id}", participant.getId())
                .exchange()
                .expectStatus().isNotFound();
    }
}
