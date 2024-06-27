package kea.exercise.exam_backend_3rd_semester.result;

import jakarta.transaction.Transactional;
import kea.exercise.exam_backend_3rd_semester.discipline.DisciplineRepository;
import kea.exercise.exam_backend_3rd_semester.exception.BadRequestException;
import kea.exercise.exam_backend_3rd_semester.exception.ResourceNotFoundException;
import kea.exercise.exam_backend_3rd_semester.participant.AgeGroup;
import kea.exercise.exam_backend_3rd_semester.participant.Gender;
import kea.exercise.exam_backend_3rd_semester.participant.ParticipantRepository;
import kea.exercise.exam_backend_3rd_semester.resultType.ResultType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ResultService {
    private final ResultRepository resultRepository;
    private final ParticipantRepository participantRepository;
    private final DisciplineRepository disciplineRepository;

    public ResultService(ResultRepository resultRepository, ParticipantRepository participantRepository, DisciplineRepository disciplineRepository) {
        this.resultRepository = resultRepository;
        this.participantRepository = participantRepository;
        this.disciplineRepository = disciplineRepository;
    }

    @Transactional
    public ResultResponseDTO createResult(ResultRequestDTO requestDTO) {
        var result = toEntity(requestDTO);
        result = resultRepository.save(result);
        return toDto(result);
    }

    @Transactional
    public List<ResultResponseDTO> createResults(List<ResultRequestDTO> requestDTOs) {
        return requestDTOs.stream().map(this::createResult).collect(Collectors.toList());
    }

    public ResultResponseDTO getResultById(Long id) {
        var result = resultRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Result not found with id: " + id));
        return toDto(result);
    }

    public List<ResultResponseDTO> getResultsByDiscipline(Gender gender, AgeGroup ageGroup, String disciplineName) {
        List<Result> results = "all".equalsIgnoreCase(disciplineName) || disciplineName.isBlank()
                ? resultRepository.findAll()
                : resultRepository.findAllByDisciplineName(disciplineName);

        if (results == null) return new ArrayList<>();

        if (gender != null)
            results = results.stream().filter(result -> result.getParticipant().getGender() == gender).collect(Collectors.toList());

        if (ageGroup != null)
            results = results.stream().filter(result -> result.getParticipant().getAgeGroup() == ageGroup).collect(Collectors.toList());

        if (!disciplineName.equals("all"))
            results.sort(getResultComparator());
        else
            results = sortResultsByDisciplineAndValue(results);

        return results.stream().map(this::toDto).toList();
    }

    @Transactional
    public ResultResponseDTO updateResult(Long id, ResultRequestDTO requestDTO) {
        Result result = resultRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Result not found with id: " + id));

        result.setResultType(requestDTO.resultType());
        result.setDate(requestDTO.date());
        result.setResultValue(ResultUtils.parseFormattedValue(requestDTO.resultType(), requestDTO.resultValue()));

        var participant = participantRepository.findById(requestDTO.participantId())
                .orElseThrow(() -> new ResourceNotFoundException("Participant not found with id: " + requestDTO.participantId()));
        var discipline = disciplineRepository.findById(requestDTO.disciplineId())
                .orElseThrow(() -> new ResourceNotFoundException("Discipline not found with id: " + requestDTO.disciplineId()));

        result.setParticipant(participant);
        result.setDiscipline(discipline);

        result = resultRepository.save(result);
        return toDto(result);
    }

    public void deleteResult(Long id) {
        if (!resultRepository.existsById(id)) {
            throw new ResourceNotFoundException("Result not found with id: " + id);
        }
        resultRepository.deleteById(id);
    }

    private ResultResponseDTO toDto(Result result) {
        return new ResultResponseDTO(
                result.getId(),
                result.getResultType(),
                result.getDate(),
                ResultUtils.formatValue(result.getResultType(), result.getResultValue()),
                result.getParticipant().getId(),
                result.getDiscipline().getId(),
                result.getParticipant().getFullName(),
                result.getDiscipline().getName()
        );
    }

    private Result toEntity(ResultRequestDTO requestDTO) {
        var participant = participantRepository.findById(requestDTO.participantId())
                .orElseThrow(() -> new ResourceNotFoundException("Participant not found with id: " + requestDTO.participantId()));
        var discipline = disciplineRepository.findById(requestDTO.disciplineId())
                .orElseThrow(() -> new ResourceNotFoundException("Discipline not found with id: " + requestDTO.disciplineId()));

        if (!participant.getDisciplines().contains(discipline)) {
            throw new BadRequestException("Participant is not competing in the specified discipline.");
        }

        int resultValue = ResultUtils.parseFormattedValue(requestDTO.resultType(), requestDTO.resultValue());
        return new Result(requestDTO.resultType(), requestDTO.date(), resultValue, participant, discipline);
    }

    private Comparator<Result> getResultComparator() {
        return (r1, r2) -> switch (r1.getResultType()) {
            case TIME -> r1.getResultValue() - r2.getResultValue();
            case DISTANCE, POINTS -> r2.getResultValue() - r1.getResultValue();
            default -> 0;
        };
    }

    private List<Result> sortResultsByDisciplineAndValue(List<Result> results) {
        return Stream.of(
                        results.stream().filter(r -> r.getResultType() == ResultType.TIME).sorted(Comparator.comparingInt(Result::getResultValue)).toList(),
                        results.stream().filter(r -> r.getResultType() == ResultType.DISTANCE).sorted(Comparator.comparingInt(Result::getResultValue).reversed()).toList(),
                        results.stream().filter(r -> r.getResultType() == ResultType.POINTS).sorted(Comparator.comparingInt(Result::getResultValue).reversed()).toList()
                )
                .flatMap(List::stream)
                .sorted(Comparator.comparing(r -> r.getDiscipline().getName()))
                .toList();
    }
}
