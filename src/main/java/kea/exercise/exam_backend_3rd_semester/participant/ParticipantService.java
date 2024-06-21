package kea.exercise.exam_backend_3rd_semester.participant;

import kea.exercise.exam_backend_3rd_semester.discipline.Discipline;
import kea.exercise.exam_backend_3rd_semester.discipline.DisciplineRepository;
import kea.exercise.exam_backend_3rd_semester.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParticipantService {
    private final ParticipantRepository participantRepository;
    private final DisciplineRepository disciplineRepository;

    public ParticipantService(ParticipantRepository participantRepository, DisciplineRepository disciplineRepository) {
        this.participantRepository = participantRepository;
        this.disciplineRepository = disciplineRepository;
    }

    public ParticipantResponseDTO createParticipant(ParticipantRequestDTO participantRequestDTO) {
        Participant participant = toEntity(participantRequestDTO);

        // Add disciplines to the participant
        if (participantRequestDTO.disciplines() != null) {
            for (String disciplineName : participantRequestDTO.disciplines()) {
                Discipline discipline = disciplineRepository.findByName(disciplineName)
                        .orElseThrow(() -> new ResourceNotFoundException("Discipline not found: " + disciplineName));
                participant.addDiscipline(discipline);
            }
        }

        participantRepository.save(participant);
        return toDto(participant);
    }


    public List<ParticipantResponseDTO> getParticipants(String search, Gender gender, AgeGroup ageGroup, String club, String discipline, String sortBy, String sortDirection) {
        // Find all participants from the database
        List<Participant> participants = participantRepository.findAll();

        // Filter participants based on the query parameters
        if (search != null && !search.isEmpty()) {
            participants = participants.stream().filter(p -> p.getFullName().toLowerCase().contains(search.toLowerCase())).toList();
        }

        if (gender != null) {
            participants = participants.stream().filter(p -> p.getGender() == gender).toList();
        }
        if (ageGroup != null) {
            participants = participants.stream().filter(p -> p.getAgeGroup() == ageGroup).toList();
        }
        if (club != null) {
            participants = participants.stream().filter(p -> p.getClub().equals(club)).toList();
        }
        if (discipline != null) {
            participants = participants.stream().filter(p -> p.getDisciplines().stream().anyMatch(d -> d.getName().equals(discipline))).toList();
        }

        // Sort participants based on the query parameters
        if (sortBy != null) {
            Comparator<Participant> comparator = Comparator.comparing(participant -> switch (sortBy) {
                case "fullName" -> participant.getFullName();
                case "gender" -> participant.getGender().toString();
                case "ageGroup" -> participant.getAgeGroup().toString();
                case "club" -> participant.getClub();
                case "discipline" ->
                        participant.getDisciplines().stream().map(Discipline::getName).toList().toString();
                default -> null;
            });

            if (sortDirection.equals("desc")) {
                comparator = comparator.reversed();
            }

            participants = participants.stream().sorted(comparator).toList();
        }

        // Convert the participants to DTOs and return them
        return participants.stream().map(this::toDto).toList();
    }


    public ParticipantResponseDTO getParticipantById(Long id) {
        return toDto(participantRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Participant not found with id: " + id)));
    }

    public ParticipantResponseDTO getParticipantByName(String name) {
        return toDto(participantRepository.findByFullNameIgnoreCase(name).orElseThrow(()-> new ResourceNotFoundException("Participant not found with name: " + name)));
    }

    public ParticipantResponseDTO updateParticipant(Long id, ParticipantRequestDTO participantRequestDTO) {
        Participant participant = participantRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Participant not found with id: " + id));

        if (participantRequestDTO.fullName() != null) {
            participant.setFullName(participantRequestDTO.fullName());
        }
        if (participantRequestDTO.gender() != null) {
            participant.setGender(participantRequestDTO.gender());
        }
        if (participantRequestDTO.dateOfBirth() != null) {
            participant.setDateOfBirth(participantRequestDTO.dateOfBirth());
        }
        if (participantRequestDTO.club() != null) {
            participant.setClub(participantRequestDTO.club());
        }
        if (participantRequestDTO.disciplines() != null) {
            participant.setDisciplines(participantRequestDTO.disciplines()
                    .stream()
                    .map(disciplineName -> disciplineRepository.findByName(disciplineName)
                            .orElseThrow(() -> new ResourceNotFoundException("Discipline not found with name: " + disciplineName)))
                    .collect(Collectors.toSet()));
        }

        participantRepository.save(participant);
        return toDto(participant);
    }

    public void deleteParticipant(Long id) {
        participantRepository.deleteById(id);
    }

    public ParticipantResponseDTO addDiscipline(Long id, String disciplineName) {
        Participant participant = participantRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Participant not found with id: " + id));
        Discipline discipline = disciplineRepository.findByName(disciplineName).orElseThrow(() -> new ResourceNotFoundException("Discipline not found with name: " + disciplineName));
        participant.addDiscipline(discipline);
        participantRepository.save(participant);
        return toDto(participant);
    }

    public ParticipantResponseDTO removeDiscipline(Long id, String disciplineName) {
        Participant participant = participantRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Participant not found with id: " + id));
        Discipline discipline = disciplineRepository.findByName(disciplineName).orElseThrow(() -> new ResourceNotFoundException("Discipline not found with name: " + disciplineName));
        participant.removeDiscipline(discipline);
        participantRepository.save(participant);
        return toDto(participant);
    }

    private ParticipantResponseDTO toDto(Participant participant) {
        return new ParticipantResponseDTO(
                participant.getId(),
                participant.getFullName(),
                participant.getGender(),
                participant.getAge(),
                participant.getAgeGroup(),
                participant.getClub(),
                participant.getDisciplines().stream().map(Discipline::getName).toList()
        );
    }

    private Participant toEntity(ParticipantRequestDTO participantRequestDTO) {
        return new Participant(
                participantRequestDTO.fullName(),
                participantRequestDTO.gender(),
                participantRequestDTO.dateOfBirth(),
                participantRequestDTO.club()
        );
    }
}
