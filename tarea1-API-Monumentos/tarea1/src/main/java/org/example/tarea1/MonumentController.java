package org.example.tarea1;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/monument")
public class MonumentController {

    private final MonumentRepository monumentRepository;

    private boolean isValid(Monument m) {
        return m != null
                && StringUtils.hasText(m.getCountryCode())
                && m.getCountryCode().matches("^[A-Z]{2}$")
                && StringUtils.hasText(m.getCountryName())
                && StringUtils.hasText(m.getCity())
                && m.getLatitude() != null
                && m.getLatitude() >= -90 && m.getLatitude() <= 90
                && m.getLongitude() != null
                && m.getLongitude() >= -180 && m.getLongitude() <= 180
                && StringUtils.hasText(m.getName())
                && StringUtils.hasText(m.getDescription())
                && StringUtils.hasText(m.getPhotoUrl());
    }

    @PostMapping
    public ResponseEntity<Monument> addMonument(@RequestBody Monument monument) {
        if (isValid(monument)) {
            monument.setId(null); // el ID lo genera la base de datos
            return ResponseEntity.status(201)
                    .body(monumentRepository.save(monument));
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping
    public ResponseEntity<List<Monument>> getAllMonuments() {
        List<Monument> result = monumentRepository.findAll();
        if (result.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Monument> getMonumentById(@PathVariable Long id) {
        return ResponseEntity.of(monumentRepository.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Monument> updateMonument(
            @PathVariable Long id,
            @RequestBody Monument monument) {

        Optional<Monument> existing = monumentRepository.findById(id);

        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (!isValid(monument)) {
            return ResponseEntity.badRequest().build();
        }

        Monument m = existing.get();
        m.setCountryCode(monument.getCountryCode());
        m.setCountryName(monument.getCountryName());
        m.setCity(monument.getCity());
        m.setLatitude(monument.getLatitude());
        m.setLongitude(monument.getLongitude());
        m.setName(monument.getName());
        m.setDescription(monument.getDescription());
        m.setPhotoUrl(monument.getPhotoUrl());

        return ResponseEntity.ok(monumentRepository.save(m));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMonument(@PathVariable Long id) {
        monumentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
