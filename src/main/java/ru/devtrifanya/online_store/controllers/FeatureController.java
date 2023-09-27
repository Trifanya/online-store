package ru.devtrifanya.online_store.controllers;

import jakarta.validation.Valid;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.devtrifanya.online_store.dto.FeatureDTO;
import ru.devtrifanya.online_store.models.Feature;
import ru.devtrifanya.online_store.services.FeatureService;
import ru.devtrifanya.online_store.util.errorResponses.ErrorResponse;
import ru.devtrifanya.online_store.util.exceptions.feature.FeatureAlreadyExistException;
import ru.devtrifanya.online_store.util.exceptions.feature.InvalidFeatureDataException;
import ru.devtrifanya.online_store.util.validators.FeatureValidator;

import java.util.List;

@RestController
@RequestMapping("/{categoryId}")
@Data
public class FeatureController {
    private final FeatureService featureService;
    private final FeatureValidator featureValidator;
    private final ModelMapper modelMapper;

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
            throw new InvalidFeatureDataException(errorMessage.toString());
        }
        featureService.createCharacteristic(convertToCharacteristic(featureDTO), categoryId);
        return new ResponseEntity<>("Характеритика успешно добавлена.", HttpStatus.CREATED);
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
            throw new InvalidFeatureDataException(errorMessage.toString());
        }
        featureService.updateCharacteristic(id, convertToCharacteristic(featureDTO));
        return new ResponseEntity<>("Характеритика успешно изменена.", HttpStatus.CREATED);
    }

    @DeleteMapping("/features/delete/{featureId}")
    public ResponseEntity<String> delete(@PathVariable("id") int id) {
        featureService.deleteCharacteristic(id);
        return new ResponseEntity<>("Характеристика успешно удалена.", HttpStatus.OK);
    }


    public Feature convertToCharacteristic(FeatureDTO featureDTO) {
        return modelMapper.map(featureDTO, Feature.class);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        //String errorMessage = null;
        HttpStatus status = null;
        if (exception instanceof InvalidFeatureDataException) {
            status = HttpStatus.BAD_REQUEST;
        } else if (exception instanceof FeatureAlreadyExistException) {
            status = HttpStatus.ALREADY_REPORTED;
        }
        ErrorResponse response = new ErrorResponse(exception.getMessage());
        return new ResponseEntity<>(response, status);
    }

}
