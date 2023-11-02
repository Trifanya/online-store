package ru.devtrifanya.online_store.rest.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.devtrifanya.online_store.services.FeatureService;
import ru.devtrifanya.online_store.rest.utils.MainClassConverter;
import ru.devtrifanya.online_store.rest.validators.FeatureValidator;
import ru.devtrifanya.online_store.rest.dto.requests.AddOrUpdateFeatureRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/features")
public class FeatureController {
    private final FeatureService featureService;

    private final FeatureValidator validator;

    private final MainClassConverter converter;

    /**
     * Адрес: .../features/newFeature
     * Добавление новой характеристики, только для администратора.
     */
    @PostMapping("/newFeature")
    public ResponseEntity<?> createNewFeature(@RequestBody @Valid AddOrUpdateFeatureRequest request) {
        validator.performNewFeatureValidation(request.getFeature());

        featureService.createNewFeature(converter.convertToFeature(request.getFeature()));

        return ResponseEntity.ok("Характеристика успешно добавлена.");
    }
    /**
     * Адрес: .../features/updateFeature
     * Обновление характеристики, только для администратора.
     */
    @PatchMapping("/updateFeature")
    public ResponseEntity<?> updateFeatureInfo(@RequestBody @Valid AddOrUpdateFeatureRequest request) {
        validator.performUpdatedFeatureValidation(request.getFeature());

        featureService.updateFeature(converter.convertToFeature(request.getFeature()));

        return ResponseEntity.ok("Характеристика успешно обновлена.");
    }

    /**
     * Адрес: .../features/{featureId}/deleteFeature
     * Удаление характеристики, только для администратора.
     */
    @DeleteMapping("/{featureId}/deleteFeature")
    public ResponseEntity<?> deleteFeature(@PathVariable("featureId") int featureToDeleteId) {
        featureService.deleteFeature(featureToDeleteId);
        return ResponseEntity.ok("Характеристика успешно удалена.");
    }
}
