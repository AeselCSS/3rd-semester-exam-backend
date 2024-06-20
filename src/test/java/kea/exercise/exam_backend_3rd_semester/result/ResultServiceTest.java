package kea.exercise.exam_backend_3rd_semester.result;

import kea.exercise.exam_backend_3rd_semester.discipline.Discipline;
import kea.exercise.exam_backend_3rd_semester.discipline.DisciplineRepository;
import kea.exercise.exam_backend_3rd_semester.discipline.DisciplineType;
import kea.exercise.exam_backend_3rd_semester.exception.BadRequestException;
import kea.exercise.exam_backend_3rd_semester.exception.ResourceNotFoundException;
import kea.exercise.exam_backend_3rd_semester.participant.Gender;
import kea.exercise.exam_backend_3rd_semester.participant.Participant;
import kea.exercise.exam_backend_3rd_semester.participant.ParticipantRepository;
import kea.exercise.exam_backend_3rd_semester.resultType.ResultType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class ResultServiceTest {

    @Mock
    private ResultRepository resultRepository;

    @Mock
    private ParticipantRepository participantRepository;

    @Mock
    private DisciplineRepository disciplineRepository;

    @InjectMocks
    private ResultService resultService;

    private Participant participant;
    private Discipline disciplineTime;
    private Discipline disciplineDistance;
    private Discipline disciplinePoints;

    @BeforeEach
    void setUp() {
        participant = new Participant("John Doe", Gender.MALE, LocalDate.of(1990, 5, 15), "Copenhagen Athletics Club");
        participant.setId(1L);
        disciplineTime = new Discipline("100m", DisciplineType.RUNNING, ResultType.TIME);
        disciplineTime.setId(1L);
        disciplineDistance = new Discipline("Long Jump", DisciplineType.JUMPING, ResultType.DISTANCE);
        disciplineDistance.setId(2L);
        disciplinePoints = new Discipline("Decathlon", DisciplineType.COMBINED_EVENTS, ResultType.POINTS);
        disciplinePoints.setId(3L);

        participant.addDiscipline(disciplineTime);
        participant.addDiscipline(disciplineDistance);
        participant.addDiscipline(disciplinePoints);

        when(participantRepository.findById(anyLong())).thenReturn(Optional.of(participant));
        when(disciplineRepository.findById(anyLong())).thenReturn(Optional.of(disciplineTime));
        when(disciplineRepository.findById(anyLong())).thenReturn(Optional.of(disciplineDistance));
        when(disciplineRepository.findById(anyLong())).thenReturn(Optional.of(disciplinePoints));
    }

    @Test
    void createTimeResult() {
        ResultRequestDTO requestDTO = new ResultRequestDTO(participant.getId(), disciplineTime.getId(), ResultType.TIME, LocalDate.now(), "00:10:23.45");

        when(resultRepository.save(any(Result.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResultResponseDTO responseDTO = resultService.createResult(requestDTO);

        assertNotNull(responseDTO);
        assertEquals(requestDTO.resultValue(), responseDTO.formattedValue());
    }

    @Test
    void createDistanceResult() {
        ResultRequestDTO requestDTO = new ResultRequestDTO(participant.getId(), disciplineDistance.getId(), ResultType.DISTANCE, LocalDate.now(), "7.35");

        when(resultRepository.save(any(Result.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResultResponseDTO responseDTO = resultService.createResult(requestDTO);

        assertNotNull(responseDTO);
        assertEquals(requestDTO.resultValue(), responseDTO.formattedValue());
    }

    @Test
    void createPointsResult() {
        ResultRequestDTO requestDTO = new ResultRequestDTO(participant.getId(), disciplinePoints.getId(), ResultType.POINTS, LocalDate.now(), "9000");

        when(resultRepository.save(any(Result.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResultResponseDTO responseDTO = resultService.createResult(requestDTO);

        assertNotNull(responseDTO);
        assertEquals(requestDTO.resultValue(), responseDTO.formattedValue());
    }

    @Test
    void createBatchResults() {
        ResultRequestDTO requestDTO1 = new ResultRequestDTO(participant.getId(), disciplineTime.getId(), ResultType.TIME, LocalDate.now(), "00:10:23.45");
        ResultRequestDTO requestDTO2 = new ResultRequestDTO(participant.getId(), disciplineDistance.getId(), ResultType.DISTANCE, LocalDate.now(), "7.35");
        ResultRequestDTO requestDTO3 = new ResultRequestDTO(participant.getId(), disciplinePoints.getId(), ResultType.POINTS, LocalDate.now(), "9000");

        when(resultRepository.save(any(Result.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResultResponseDTO responseDTO1 = resultService.createResult(requestDTO1);
        ResultResponseDTO responseDTO2 = resultService.createResult(requestDTO2);
        ResultResponseDTO responseDTO3 = resultService.createResult(requestDTO3);

        assertNotNull(responseDTO1);
        assertNotNull(responseDTO2);
        assertNotNull(responseDTO3);
    }

    @Test
    void createResultThrowsBadRequest_WhenParticipantNotInDiscipline() {
        Discipline otherDiscipline = new Discipline("200m", DisciplineType.RUNNING, ResultType.TIME);
        otherDiscipline.setId(4L);

        ResultRequestDTO requestDTO = new ResultRequestDTO(participant.getId(), otherDiscipline.getId(), ResultType.TIME, LocalDate.now(), "00:10:23.45");

        when(disciplineRepository.findById(anyLong())).thenReturn(Optional.of(otherDiscipline));

        assertThrows(BadRequestException.class, () -> resultService.createResult(requestDTO));
    }

    @Test
    void createResultThrowsResourceNotFound_WhenParticipantNotFound() {
        ResultRequestDTO requestDTO = new ResultRequestDTO(999L, disciplineTime.getId(), ResultType.TIME, LocalDate.now(), "00:10:23.45");

        when(participantRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> resultService.createResult(requestDTO));
    }

    @Test
    void createResultThrowsResourceNotFound_WhenDisciplineNotFound() {
        ResultRequestDTO requestDTO = new ResultRequestDTO(participant.getId(), 999L, ResultType.TIME, LocalDate.now(), "00:10:23.45");

        when(disciplineRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> resultService.createResult(requestDTO));
    }

    @Test
    void updateResult() {
        Result existingResult = new Result(ResultType.TIME, LocalDate.now(), 62345, participant, disciplineTime);
        existingResult.setId(1L);

        when(resultRepository.findById(anyLong())).thenReturn(Optional.of(existingResult));
        when(resultRepository.save(any(Result.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResultRequestDTO requestDTO = new ResultRequestDTO(participant.getId(), disciplineTime.getId(), ResultType.TIME, LocalDate.now(), "00:09:15.30");

        ResultResponseDTO responseDTO = resultService.updateResult(existingResult.getId(), requestDTO);

        assertNotNull(responseDTO);
        assertEquals(requestDTO.resultValue(), responseDTO.formattedValue());
    }

    @Test
    void deleteResult() {
        Result existingResult = new Result(ResultType.TIME, LocalDate.now(), 62345, participant, disciplineTime);
        existingResult.setId(1L);

        when(resultRepository.existsById(anyLong())).thenReturn(true);

        assertDoesNotThrow(() -> resultService.deleteResult(existingResult.getId()));
    }
}
