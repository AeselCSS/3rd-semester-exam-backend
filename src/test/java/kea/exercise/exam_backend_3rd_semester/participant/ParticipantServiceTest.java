package kea.exercise.exam_backend_3rd_semester.participant;

import kea.exercise.exam_backend_3rd_semester.discipline.Discipline;
import kea.exercise.exam_backend_3rd_semester.discipline.DisciplineRepository;
import kea.exercise.exam_backend_3rd_semester.discipline.DisciplineType;
import kea.exercise.exam_backend_3rd_semester.exception.ResourceNotFoundException;
import kea.exercise.exam_backend_3rd_semester.resultType.ResultType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class ParticipantServiceTest {

    private Participant participant;
    private Discipline discipline;

    @Mock
    private ParticipantRepository participantRepository;

    @Mock
    private DisciplineRepository disciplineRepository;

    @InjectMocks
    private ParticipantService participantService;

    @BeforeEach
    void beforeEach() {
        discipline = new Discipline("Discipline A", DisciplineType.RUNNING, ResultType.TIME);
        participant = new Participant("John Doe", Gender.MALE, LocalDate.of(2000, 1, 1), "Club A");
        participant.addDiscipline(discipline);

        // Mock repository responses
        when(disciplineRepository.save(any(Discipline.class))).thenReturn(discipline);
        when(disciplineRepository.findByName("Discipline A")).thenReturn(Optional.of(discipline));
        when(participantRepository.save(any(Participant.class))).thenReturn(participant);
        when(participantRepository.findById(anyLong())).thenReturn(Optional.of(participant));
    }

    @AfterEach
    void afterEach() {
        // clear database
        participantRepository.deleteAll();
        disciplineRepository.deleteAll();
    }

    @Test
    void createParticipantReturnsCorrectDto() {
        ParticipantRequestDTO requestDTO = new ParticipantRequestDTO(null, "John Doe", Gender.MALE, LocalDate.of(2000, 1, 1), "Club A", new ArrayList<>(List.of("Discipline A")));

        when(disciplineRepository.findByName("Discipline A")).thenReturn(Optional.of(discipline));
        when(participantRepository.save(any(Participant.class))).thenReturn(participant);

        ParticipantResponseDTO responseDTO = participantService.createParticipant(requestDTO);

        assertEquals("John Doe", responseDTO.fullName());
        assertEquals(Gender.MALE, responseDTO.gender());
        assertEquals(24, responseDTO.age());
        assertEquals("Club A", responseDTO.club());
        assertEquals(1, responseDTO.disciplines().size());
        assertEquals("Discipline A", responseDTO.disciplines().get(0));
    }

    @Test
    void createParticipantThrowsExceptionWhenDisciplineNotFound() {
        ParticipantRequestDTO requestDTO = new ParticipantRequestDTO(null, "John Doe", Gender.MALE, LocalDate.of(2000, 1, 1), "Club A", new ArrayList<>(List.of("Non-existent Discipline")));

        when(disciplineRepository.findByName("Non-existent Discipline")).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            participantService.createParticipant(requestDTO);
        });

        assertEquals("Discipline not found: Non-existent Discipline", exception.getMessage());
    }

    @Test
    void getParticipantByIdReturnsCorrectDto() {
        when(participantRepository.findById(1L)).thenReturn(Optional.of(participant));

        ParticipantResponseDTO responseDTO = participantService.getParticipantById(1L);

        assertEquals("John Doe", responseDTO.fullName());
        assertEquals(Gender.MALE, responseDTO.gender());
        assertEquals(24, responseDTO.age());
        assertEquals("Club A", responseDTO.club());
    }

    @Test
    void getParticipantByIdThrowsExceptionWhenNotFound() {
        when(participantRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            participantService.getParticipantById(1L);
        });

        assertEquals("Participant not found with id: 1", exception.getMessage());
    }

    @Test
    void updateParticipantReturnsCorrectDto() {
        ParticipantRequestDTO requestDTO = new ParticipantRequestDTO(null, "Jane Doe", Gender.FEMALE, LocalDate.of(2000, 1, 1), "Club B", new ArrayList<>(List.of("Discipline B")));
        Discipline newDiscipline = new Discipline("Discipline B", DisciplineType.RUNNING, ResultType.TIME);

        when(participantRepository.findById(1L)).thenReturn(Optional.of(participant));
        when(disciplineRepository.findByName("Discipline B")).thenReturn(Optional.of(newDiscipline));
        when(participantRepository.save(any(Participant.class))).thenReturn(participant);

        ParticipantResponseDTO responseDTO = participantService.updateParticipant(1L, requestDTO);

        assertEquals("Jane Doe", responseDTO.fullName());
        assertEquals(Gender.FEMALE, responseDTO.gender());
        assertEquals(24, responseDTO.age());
        assertEquals("Club B", responseDTO.club());
        assertEquals(1, responseDTO.disciplines().size());
        assertEquals("Discipline B", responseDTO.disciplines().get(0));
    }

    @Test
    void updateParticipantWithPartialDataReturnsCorrectDto() {
        ParticipantRequestDTO requestDTO = new ParticipantRequestDTO(1L, "Jonathan Hubertus Doe", null, null, null, null);

        when(participantRepository.findById(1L)).thenReturn(Optional.of(participant));
        when(participantRepository.save(any(Participant.class))).thenReturn(participant);

        ParticipantResponseDTO responseDTO = participantService.updateParticipant(1L, requestDTO);

        // validate that only the fullName was updated
        assertEquals("Jonathan Hubertus Doe", responseDTO.fullName());
        assertEquals(Gender.MALE, responseDTO.gender());
        assertEquals(24, responseDTO.age());
        assertEquals("Club A", responseDTO.club());
        assertEquals(1, responseDTO.disciplines().size());
    }

    @Test
    void updateParticipantThrowsExceptionWhenNotFound() {
        ParticipantRequestDTO requestDTO = new ParticipantRequestDTO(1L, "Jonathan Hubertus Doe", null, null, null, null);

        when(participantRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            participantService.updateParticipant(1L, requestDTO);
        });

        assertEquals("Participant not found with id: 1", exception.getMessage());
    }

    @Test
    void addDisciplineToParticipant() {
        Discipline newDiscipline = new Discipline("Discipline B", DisciplineType.RUNNING, ResultType.TIME);

        when(participantRepository.findById(1L)).thenReturn(Optional.of(participant));
        when(disciplineRepository.findByName("Discipline B")).thenReturn(Optional.of(newDiscipline));
        when(participantRepository.save(any(Participant.class))).thenReturn(participant);

        ParticipantResponseDTO responseDTO = participantService.addDiscipline(1L, "Discipline B");

        assertEquals(2, responseDTO.disciplines().size());
        assertTrue(responseDTO.disciplines().contains("Discipline A"));
        assertTrue(responseDTO.disciplines().contains("Discipline B"));
    }

    @Test
    void addDisciplineThrowsExceptionWhenDisciplineNotFound() {
        when(participantRepository.findById(1L)).thenReturn(Optional.of(participant));
        when(disciplineRepository.findByName("Non-existent Discipline")).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            participantService.addDiscipline(1L, "Non-existent Discipline");
        });

        assertEquals("Discipline not found with name: Non-existent Discipline", exception.getMessage());
    }

    @Test
    void removeDisciplineFromParticipant() {
        Discipline existingDiscipline = new Discipline("Discipline A", DisciplineType.RUNNING, ResultType.TIME);
        participant.addDiscipline(existingDiscipline);  // Ensure the participant has the discipline to begin with

        when(participantRepository.findById(1L)).thenReturn(Optional.of(participant));
        when(disciplineRepository.findByName("Discipline A")).thenReturn(Optional.of(existingDiscipline));

        ParticipantResponseDTO responseDTO = participantService.removeDiscipline(1L, "Discipline A");

        // Verify the discipline is removed
        assertEquals(0, participant.getDisciplines().size());
        assertEquals(0, responseDTO.disciplines().size());

        // Verify that the participant was saved with the updated disciplines
        verify(participantRepository).save(participant);
    }



    @Test
    void removeDisciplineThrowsExceptionWhenDisciplineNotFound() {
        when(participantRepository.findById(1L)).thenReturn(Optional.of(participant));
        when(disciplineRepository.findByName("Non-existent Discipline")).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            participantService.removeDiscipline(1L, "Non-existent Discipline");
        });

        assertEquals("Discipline not found with name: Non-existent Discipline", exception.getMessage());
    }

    @Test
    void getParticipantsWithFiltersAndSorting() {
        Participant participant2 = new Participant("Jane Doe", Gender.FEMALE, LocalDate.of(1995, 1, 1), "Club B");
        participant2.addDiscipline(discipline);

        when(participantRepository.findAll()).thenReturn(List.of(participant, participant2));

        List<ParticipantResponseDTO> responseDTOs = participantService.getParticipants(Gender.FEMALE, null, null, null, "fullName", "asc");

        assertEquals(1, responseDTOs.size());
        assertEquals("Jane Doe", responseDTOs.get(0).fullName());

        responseDTOs = participantService.getParticipants(null, null, "Club A", null, "fullName", "asc");
        assertEquals(1, responseDTOs.size());
        assertEquals("John Doe", responseDTOs.get(0).fullName());

        responseDTOs = participantService.getParticipants(null, AgeGroup.ADULT, null, null, "fullName", "asc");
        assertEquals(2, responseDTOs.size());
        assertEquals("Jane Doe", responseDTOs.get(0).fullName());
        assertEquals("John Doe", responseDTOs.get(1).fullName());

        responseDTOs = participantService.getParticipants(null, null, null, "Discipline A", "fullName", "asc");
        assertEquals(2, responseDTOs.size());
        assertEquals("Jane Doe", responseDTOs.get(0).fullName());
        assertEquals("John Doe", responseDTOs.get(1).fullName());
    }
}
