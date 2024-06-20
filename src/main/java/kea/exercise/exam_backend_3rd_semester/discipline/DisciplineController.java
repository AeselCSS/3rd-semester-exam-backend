package kea.exercise.exam_backend_3rd_semester.discipline;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/discipline")
public class DisciplineController {
    private final DisciplineService disciplineService;

    public DisciplineController(DisciplineService disciplineService) {
        this.disciplineService = disciplineService;
    }

    @GetMapping
    public List<DisciplineResponseDTO> getAll() {
        return disciplineService.getAll();
    }

    @GetMapping("/{id}")
    public DisciplineResponseDTO getById(@PathVariable Long id) {
        return disciplineService.getById(id);
    }

    @GetMapping("/name/{name}")
    public DisciplineResponseDTO getByName(@PathVariable String name) {
        return disciplineService.getByName(name);
    }
}
