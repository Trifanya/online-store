package ru.devtrifanya.online_store.controllers;

import jakarta.validation.Valid;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.devtrifanya.online_store.dto.FeatureDTO;
import ru.devtrifanya.online_store.models.Feature;
import ru.devtrifanya.online_store.services.FeatureService;
import ru.devtrifanya.online_store.util.ErrorResponse;
import ru.devtrifanya.online_store.util.MainExceptionHandler;
import ru.devtrifanya.online_store.util.exceptions.InvalidDataException;
import ru.devtrifanya.online_store.util.validators.FeatureValidator;

import java.util.List;

@RestController
@RequestMapping("/{categoryId}")
@Data
public class FeatureController {
    private final FeatureService featureService;
    private final FeatureValidator featureValidator;
    private final ModelMapper modelMapper;
    private final MainExceptionHandler mainExceptionHandler;

    /**
     * Адрес: .../{categoryId}/characteristics/new
     * Добавление новой характеристики для текущей категории.
     */
    @PostMapping("/newFeature")
    public ResponseEntity<String> add(@RequestBody @Valid FeatureDTO featureDTO,
                                                    @PathVariable("categoryId") int categoryId,
                                                    BindingResult bindingResult) {
        //characteristicValidator.validate(characteristicDTO);
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            StringBuilder errorMessage = new StringBuilder();
            for (FieldError error : errors) {
                errorMessage.append(error.getDefaultMessage() + "\n");
            }
            throw new InvalidDataException(errorMessage.toString());
        }
        featureService.create(convertToCharacteristic(featureDTO), categoryId);
        return ResponseEntity.ok("Характеритика успешно добавлена.");
    }

    @PatchMapping("/features/edit/{featureId}")
    public ResponseEntity<String> edit(@RequestBody @Valid FeatureDTO featureDTO,
                                                     @PathVariable("featureId") int id,
                                                     BindingResult bindingResult) {
        //characteristicValidator.validate(characteristicDTO);
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            StringBuilder errorMessage = new StringBuilder();
            for (FieldError error : errors) {
                errorMessage.append(error.getDefaultMessage() + "\n");
            }
            throw new InvalidDataException(errorMessage.toString());
        }
        featureService.update(id, convertToCharacteristic(featureDTO));
        return ResponseEntity.ok("Характеритика успешно изменена.");
    }

    @DeleteMapping("/features/delete/{featureId}")
    public ResponseEntity<String> delete(@PathVariable("featureId") int id) {
        featureService.delete(id);
        return ResponseEntity.ok("Характеритика успешно удалена.");
    }


    public Feature convertToCharacteristic(FeatureDTO featureDTO) {
        return modelMapper.map(featureDTO, Feature.class);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        return mainExceptionHandler.handleException(exception);
    }
}
