package ru.devtrifanya.online_store.rest.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.devtrifanya.online_store.models.Feature;
import ru.devtrifanya.online_store.rest.dto.requests.NewFeatureRequest;
import ru.devtrifanya.online_store.rest.utils.MainClassConverter;
import ru.devtrifanya.online_store.services.implementations.FeatureService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/catalog/{categoryId}")
public class FeatureController {
    private final FeatureService featureService;

    private final MainClassConverter converter;

    @PostMapping("/newFeature")
    public ResponseEntity<?> createNewFeature(@RequestBody @Valid NewFeatureRequest request) {
        Feature createdFeature = featureService.createNewFeature(
                converter.convertToFeature(request.getFeature()),
                request.getCategoryId()
        );
        return ResponseEntity.ok("Характеристика успешно добавлена.");
    }

    @PatchMapping("/editFeature")
    public ResponseEntity<?> updateFeatureInfo(@RequestBody @Valid NewFeatureRequest request) {
        Feature updatedFeature = featureService.updateFeatureInfo(
                converter.convertToFeature(request.getFeature())
        );
        return ResponseEntity.ok("Характеристика успешно обновлена.");
    }
}
