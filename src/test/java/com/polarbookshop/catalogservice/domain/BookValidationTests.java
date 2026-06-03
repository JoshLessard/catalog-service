package com.polarbookshop.catalogservice.domain;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class BookValidationTests {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        try ( ValidatorFactory factory = Validation.buildDefaultValidatorFactory() ) {
            validator = factory.getValidator();
        }
    }

    @Test
    public void whenAllFieldsCorrectThenValidationSucceeds() {
        Book book = new Book( "1234567890", "Title", "Author", 9.90 );

        Set<ConstraintViolation<Book>> violations = validator.validate( book );

        assertThat( violations )
            .isEmpty();
    }

    @Test
    public void whenIsbnDefinedButIncorrectThenValidationFails() {
        Book book = new Book( "a234567890", "Title", "Author", 9.90 );

        Set<ConstraintViolation<Book>> violations = validator.validate( book );

        assertThat( violations )
            .hasSize( 1 );
        assertThat( violations.iterator().next().getMessage() )
            .isEqualTo( "The ISBN format must be valid." );
    }
}
