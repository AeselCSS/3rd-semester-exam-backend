package kea.exercise.exam_backend_3rd_semester.result;

import kea.exercise.exam_backend_3rd_semester.participant.AgeGroup;
import kea.exercise.exam_backend_3rd_semester.participant.Gender;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/results")
public class ResultController {
    private final ResultService resultService;

    public ResultController(ResultService resultService) {
        this.resultService = resultService;
    }

    @PostMapping
    public ResponseEntity<ResultResponseDTO> createResult(@RequestBody ResultRequestDTO requestDTO) {
        ResultResponseDTO responseDTO = resultService.createResult(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PostMapping("/batch")
    public ResponseEntity<List<ResultResponseDTO>> createResults(@RequestBody List<ResultRequestDTO> requestDTOs) {
        List<ResultResponseDTO> responseDTOs = resultService.createResults(requestDTOs);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTOs);
    }

    @GetMapping
    public ResponseEntity<List<ResultResponseDTO>> getResultsByDiscipline(
            @RequestParam(required = false, defaultValue = "all") String disciplineName,
            @RequestParam(required = false) Gender gender,
            @RequestParam(required = false) AgeGroup ageGroup) {
        return ResponseEntity.ok(resultService.getResultsByDiscipline(gender, ageGroup, disciplineName));
    }


    @GetMapping("/{id}")
    public ResponseEntity<ResultResponseDTO> getResultById(@PathVariable Long id) {
        ResultResponseDTO responseDTO = resultService.getResultById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResultResponseDTO> updateResult(@PathVariable Long id, @RequestBody ResultRequestDTO requestDTO) {
        ResultResponseDTO responseDTO = resultService.updateResult(id, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResult(@PathVariable Long id) {
        resultService.deleteResult(id);
        return ResponseEntity.noContent().build();
    }
}
