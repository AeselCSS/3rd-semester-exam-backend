package kea.exercise.exam_backend_3rd_semester.participant;

import kea.exercise.exam_backend_3rd_semester.discipline.DisciplineRequestDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/participant")
public class ParticipantController {
    private final ParticipantService participantService;

    public ParticipantController(ParticipantService participantService) {
        this.participantService = participantService;
    }

    @PostMapping
    public ResponseEntity<ParticipantResponseDTO> createParticipant(@RequestBody ParticipantRequestDTO participantRequestDTO) {
        var participantResponseDTO = participantService.createParticipant(participantRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(participantResponseDTO);
    }

    @GetMapping
    public ResponseEntity<List<ParticipantResponseDTO>> getParticipants(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Gender gender,
            @RequestParam(required = false) AgeGroup ageGroup,
            @RequestParam(required = false) String club,
            @RequestParam(required = false) String discipline,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        return ResponseEntity.ok(participantService.getParticipants(search, gender, ageGroup, club, discipline, sortBy, sortDirection));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<ParticipantResponseDTO> getParticipantById(@PathVariable Long id) {
        return ResponseEntity.ok(participantService.getParticipantById(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ParticipantResponseDTO> getParticipantByName(@PathVariable String name) {
        return ResponseEntity.ok(participantService.getParticipantByName(name));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ParticipantResponseDTO> updateParticipant(@PathVariable Long id, @RequestBody ParticipantRequestDTO participantRequestDTO) {
        return ResponseEntity.ok(participantService.updateParticipant(id, participantRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParticipant(@PathVariable Long id) {
        participantService.deleteParticipant(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/addDiscipline")
    public ResponseEntity<ParticipantResponseDTO> addDiscipline(@PathVariable Long id, @RequestBody DisciplineRequestDTO disciplineRequestDTO) {
        return ResponseEntity.ok(participantService.addDiscipline(id, disciplineRequestDTO.name()));
    }

    @PatchMapping("/{id}/removeDiscipline")
    public ResponseEntity<ParticipantResponseDTO> removeDiscipline(@PathVariable Long id, @RequestBody DisciplineRequestDTO disciplineRequestDTO) {
        return ResponseEntity.ok(participantService.removeDiscipline(id, disciplineRequestDTO.name()));
    }
}

