package gr.aueb.cf.eduapp.dto;

import org.springframework.validation.BindingResult;

import java.util.Map;

public record ValidationResponseEntityDTO(
        String code,
        String massage,
        Map<String,String> errors     // αντί για BindingResult bindingResult
        ) {
}
