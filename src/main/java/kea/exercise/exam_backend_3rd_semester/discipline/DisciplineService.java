package kea.exercise.exam_backend_3rd_semester.discipline;

import kea.exercise.exam_backend_3rd_semester.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DisciplineService {

    private final DisciplineRepository disciplineRepository;

    public DisciplineService(DisciplineRepository disciplineRepository) {
        this.disciplineRepository = disciplineRepository;
    }

    public List<DisciplineResponseDTO> getAll() {
        return disciplineRepository.findAll().stream().map(this::toDto).toList();
    }

    public DisciplineResponseDTO getById(Long id) {
        return disciplineRepository.findById(id).map(this::toDto).orElseThrow(() -> new ResourceNotFoundException("No discipline found with id " + id));
    }

    public DisciplineResponseDTO getByName(String name) {
        return disciplineRepository.findByName(name).map(this::toDto).orElseThrow(() -> new ResourceNotFoundException("No discipline found with name " + name));
    }

    private DisciplineResponseDTO toDto(Discipline discipline) {
        return new DisciplineResponseDTO(discipline.getId(), discipline.getName(), discipline.getDisciplineType(), discipline.getResultType());
    }
}
