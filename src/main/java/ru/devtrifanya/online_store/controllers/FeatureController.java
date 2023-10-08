/*
package ru.devtrifanya.online_store.controllers;

import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.devtrifanya.online_store.dto.FeatureDTO;
import ru.devtrifanya.online_store.models.Feature;
import ru.devtrifanya.online_store.services.FeatureService;
import ru.devtrifanya.online_store.util.ErrorResponse;
import ru.devtrifanya.online_store.util.MainClassConverter;
import ru.devtrifanya.online_store.util.MainExceptionHandler;
import ru.devtrifanya.online_store.util.exceptions.InvalidDataException;
import ru.devtrifanya.online_store.util.validators.FeatureValidator;

import java.util.List;

@RestController
@RequestMapping("/{categoryId}/features")
@Data
public class FeatureController {
    private final FeatureService featureService;
    private final FeatureValidator featureValidator;
    private final MainExceptionHandler exceptionHandler;
    private final MainClassConverter converter;

    */
/**
     * Адрес: .../{categoryId}/features/new
     * Добавление новой характеристики для текущей категории.
     *//*

    @PostMapping("/newFeature")
    public ResponseEntity<Feature> createNewFeature(@RequestBody @Valid FeatureDTO featureDTO,
                                                    @PathVariable("categoryId") int categoryId,
                                                    BindingResult bindingResult) {
        featureValidator.validate(featureDTO, categoryId);
        if (bindingResult.hasErrors()) {
            exceptionHandler.throwInvalidDataException(bindingResult);
        }

        Feature createdFeature = featureService.createNewFeature(
                converter.convertToFeature(featureDTO),
                categoryId
        );

        return ResponseEntity.ok(createdFeature);
    }


    @PatchMapping("/edit/{featureId}")
    public ResponseEntity<String> editFeatureInfo(@RequestBody @Valid FeatureDTO featureDTO,
                                                  @PathVariable("categoryId") int categoryId,
                                                  @PathVariable("featureId") int featureId,
                                                  BindingResult bindingResult) {
        featureValidator.validate(featureDTO, categoryId);
        if (bindingResult.hasErrors()) {
            exceptionHandler.throwInvalidDataException(bindingResult);
        }
        featureService.updateFeatureInfo(featureId, converter.convertToFeature(featureDTO));

        return ResponseEntity.ok("Характеритика успешно изменена.");
    }

    @DeleteMapping("/delete/{featureId}")
    public ResponseEntity<String> deleteFeature(@PathVariable("featureId") int id) {
        featureService.deleteFeature(id);
        return ResponseEntity.ok("Характеритика успешно удалена.");
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        return exceptionHandler.handleException(exception);
    }
}
*/
