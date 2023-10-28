package ru.devtrifanya.online_store.rest.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.devtrifanya.online_store.rest.dto.requests.DeleteFeatureRequest;
import ru.devtrifanya.online_store.rest.dto.requests.AddFeatureRequest;
import ru.devtrifanya.online_store.rest.utils.MainClassConverter;
import ru.devtrifanya.online_store.rest.validators.FeatureValidator;
import ru.devtrifanya.online_store.services.FeatureService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/catalog/allFeatures")
public class FeatureController {
    private final FeatureService featureService;

    private final FeatureValidator validator;

    private final MainClassConverter converter;

    @PostMapping("/newFeature")
    public ResponseEntity<?> createNewFeature(@RequestBody @Valid AddFeatureRequest request) {
        validator.performNewFeatureValidation(request.getFeature());

        featureService.createNewFeature(
                converter.convertToFeature(request.getFeature())
        );

        return ResponseEntity.ok("Характеристика успешно добавлена.");
    }

    @PatchMapping("/updateFeature")
    public ResponseEntity<?> updateFeatureInfo(@RequestBody @Valid AddFeatureRequest request) {
        validator.performUpdatedFeatureValidation(request.getFeature());

        featureService.updateFeature(
                converter.convertToFeature(request.getFeature())
        );

        return ResponseEntity.ok("Характеристика успешно обновлена.");
    }

    @DeleteMapping("/deleteFeature")
    public ResponseEntity<?> deleteFeature(@RequestBody @Valid DeleteFeatureRequest request) {
        featureService.deleteFeature(request.getFeatureToDeleteId());
        return ResponseEntity.ok("Характеристика успешно удалена.");
    }
}
