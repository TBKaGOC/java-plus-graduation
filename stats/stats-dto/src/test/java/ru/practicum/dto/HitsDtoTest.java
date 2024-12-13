package ru.practicum.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HitsDtoTest {

    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void testValidHitsDto() {
        HitsDto hitsDto = new HitsDto("myApp", "/my-uri", 100L);
        Set<ConstraintViolation<HitsDto>> violations = validator.validate(hitsDto);
        assertTrue(violations.isEmpty(), "There should be no validation errors for a valid HitsDto");
    }

    @Test
    void testAppIsBlank() {
        HitsDto hitsDto = new HitsDto("", "/my-uri", 100L);
        Set<ConstraintViolation<HitsDto>> violations = validator.validate(hitsDto);
        assertEquals(1, violations.size(), "There should be one validation error for a blank app");
        ConstraintViolation<HitsDto> violation = violations.iterator().next();
        assertEquals("Идентификатор сервиса для которого записывается информация не должен быть пустым.", violation.getMessage());
    }

    @Test
    void testUriIsBlank() {
        HitsDto hitsDto = new HitsDto("myApp", "", 100L);
        Set<ConstraintViolation<HitsDto>> violations = validator.validate(hitsDto);
        assertEquals(1, violations.size(), "There should be one validation error for a blank uri");
        ConstraintViolation<HitsDto> violation = violations.iterator().next();
        assertEquals("URI для которого был осуществлен запрос не должен быть пустым.", violation.getMessage());
    }

    @Test
    void testBothAppAndUriAreBlank() {
        HitsDto hitsDto = new HitsDto("", "", 100L);
        Set<ConstraintViolation<HitsDto>> violations = validator.validate(hitsDto);
        assertEquals(2, violations.size(), "There should be two validation errors when app and uri are blank");
    }

    @Test
    void testHitsCanBeNull() {
        HitsDto hitsDto = new HitsDto("myApp", "/my-uri", null);
        Set<ConstraintViolation<HitsDto>> violations = validator.validate(hitsDto);
        assertTrue(violations.isEmpty(), "HitsDto should allow null for the hits field as it is not constrained");
    }
}
