package com.brainz.sms_backend.Guardian;

import com.brainz.sms_backend.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GuardianService {

    private final GuardianRepository guardianRepository;

    public List<GuardianResponse> getAll() {
        return guardianRepository.findAll().stream()
                .map(GuardianResponse::from).collect(Collectors.toList());
    }

    public GuardianResponse getById(Long id) {
        return GuardianResponse.from(guardianRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Guardian not found: " + id)));
    }

    public GuardianResponse create(GuardianRequest request) {
        Guardian g = new Guardian();
        mapToEntity(request, g);
        return GuardianResponse.from(guardianRepository.save(g));
    }

    public GuardianResponse update(Long id, GuardianRequest request) {
        Guardian g = guardianRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Guardian not found: " + id));
        mapToEntity(request, g);
        return GuardianResponse.from(guardianRepository.save(g));
    }

    public void delete(Long id) {
        if (!guardianRepository.existsById(id))
            throw new ResourceNotFoundException("Guardian not found: " + id);
        guardianRepository.deleteById(id);
    }

    private void mapToEntity(GuardianRequest r, Guardian g) {
        g.setName(r.getName());
        g.setPhone(r.getPhone());
        g.setEmail(r.getEmail());
        g.setAddress(r.getAddress());
        g.setRelationship(r.getRelationship());
        g.setOccupation(r.getOccupation());
    }
}
