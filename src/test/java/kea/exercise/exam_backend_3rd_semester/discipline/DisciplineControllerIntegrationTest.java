package kea.exercise.exam_backend_3rd_semester.discipline;

import kea.exercise.exam_backend_3rd_semester.resultType.ResultType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class DisciplineControllerIntegrationTest {
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private DisciplineRepository disciplineRepository;

    @BeforeEach
    void setup() {
        disciplineRepository.deleteAll();
    }

    @Test
    void testGetAll() {
        Discipline discipline1 = new Discipline("Discipline A", DisciplineType.RUNNING, ResultType.TIME);
        Discipline discipline2 = new Discipline("Discipline B", DisciplineType.JUMPING, ResultType.DISTANCE);
        disciplineRepository.save(discipline1);
        disciplineRepository.save(discipline2);

        webTestClient.get()
                .uri("/discipline")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(DisciplineResponseDTO.class)
                .value(disciplines -> {
                    assertEquals(2, disciplines.size());
                    assertEquals("Discipline A", disciplines.getFirst().name());
                    assertEquals(DisciplineType.RUNNING, disciplines.getFirst().disciplineType());
                    assertEquals(ResultType.TIME, disciplines.get(0).resultType());
                    assertEquals("Discipline B", disciplines.get(1).name());
                    assertEquals(DisciplineType.JUMPING, disciplines.get(1).disciplineType());
                    assertEquals(ResultType.DISTANCE, disciplines.get(1).resultType());
                });
    }

    @Test
    void testGetById() {
        Discipline discipline = new Discipline("Discipline A", DisciplineType.RUNNING, ResultType.TIME);
        discipline = disciplineRepository.save(discipline);

        webTestClient.get()
                .uri("/discipline/{id}", discipline.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(DisciplineResponseDTO.class)
                .value(responseDTO -> {
                    assertEquals("Discipline A", responseDTO.name());
                    assertEquals(DisciplineType.RUNNING, responseDTO.disciplineType());
                    assertEquals(ResultType.TIME, responseDTO.resultType());
                });
    }

    @Test
    void testGetByName() {
        Discipline discipline = new Discipline("Discipline A", DisciplineType.RUNNING, ResultType.TIME);
        discipline = disciplineRepository.save(discipline);

        webTestClient.get()
                .uri("/discipline/name/{name}", discipline.getName())
                .exchange()
                .expectStatus().isOk()
                .expectBody(DisciplineResponseDTO.class)
                .value(responseDTO -> {
                    assertEquals("Discipline A", responseDTO.name());
                    assertEquals(DisciplineType.RUNNING, responseDTO.disciplineType());
                    assertEquals(ResultType.TIME, responseDTO.resultType());
                });
    }
}