package ca.ulaval.glo2003.domain;

import static com.google.common.truth.Truth.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ca.ulaval.glo2003.domain.reservation.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CustomerTest {
    private Customer validCustomer1;
    private Customer testCustomer1;
    private Customer testCustomer2;
    private Customer invalidCustomer;
    private Customer customerNull;

    @BeforeEach
    public void setup() {
        validCustomer1 = new Customer();
        validCustomer1.setName("Gontrand");
        validCustomer1.setEmail("test@test.ca");
        validCustomer1.setPhoneNumber("0987654321");

        invalidCustomer = new Customer();
        invalidCustomer.setName(null);
        invalidCustomer.setEmail("bad'email@test.ca");
        invalidCustomer.setPhoneNumber("123");

        testCustomer1 = new Customer();
        testCustomer2 = new Customer();
        customerNull = new Customer();
    }

    @Test
    public void setName_CanSetName() {
        validCustomer1.setName("Roger");

        assertThat(validCustomer1.getName()).isEqualTo("Roger");
    }

    @Test
    public void setEmail_CanSetEmail() {
        validCustomer1.setEmail("abc@email.com");

        assertThat(validCustomer1.getEmail()).isEqualTo("abc@email.com");
    }

    @Test
    public void setPhoneNumber_CanSetPhoneNumber() {
        validCustomer1.setPhoneNumber("1234567892");

        assertThat(validCustomer1.getPhoneNumber()).isEqualTo("1234567892");
    }

    @Test
    public void validateCustomer_NoThrowIfValidCustomer() {
        assertDoesNotThrow(
            () -> {
                validCustomer1.validate();
            }
        );
    }

    @Test
    public void validateCustomer_ThrowIfInvalidCustomer() {
        assertThrows(
            IllegalStateException.class,
            () -> {
                customerNull.validate();
            }
        );
    }

    @Test
    public void validate_ThrowIfInvalidName() {
        assertDoesNotThrow(
            () -> {
                validCustomer1.validate();
            }
        );
    }

    @Test
    public void validate_NoThrowIfVaalidName() {
        assertThrows(
            IllegalStateException.class,
            () -> {
                customerNull.validate();
            }
        );
    }

    @Test
    public void validate_NoThrowIfNormalEmail() {
        validCustomer1.setEmail("good@verygood.com");
        validCustomer1.setName("Iamgood");
        validCustomer1.setPhoneNumber("1823982913");

        assertDoesNotThrow(
            () -> {
                validCustomer1.validate();
            }
        );
    }

    @Test
    public void validate_NoThrowIfValidEmail() {
        testCustomer1.setEmail("abc@bc.ca");
        testCustomer1.setName("Qualinet");
        testCustomer1.setPhoneNumber("4186666666");
        assertDoesNotThrow(
            () -> {
                testCustomer1.validate();
            }
        );
    }

    @Test
    public void validate_NoThrowIfValidShortEmail() {
        testCustomer2.setEmail("a@b.c");
        testCustomer2.setName("Paul Houde");
        testCustomer2.setPhoneNumber("4186666666");
        assertDoesNotThrow(
            () -> {
                testCustomer2.validate();
            }
        );
    }

    @Test
    public void validate_ThrowIfInvalidEmail() {
        invalidCustomer.setName("Gabriel");
        invalidCustomer.setPhoneNumber("6666666666");
        assertThrows(
            IllegalArgumentException.class,
            () -> {
                invalidCustomer.validate();
            }
        );
    }

    @Test
    public void validate_ThrowIfNoEmail() {
        assertThrows(
            IllegalStateException.class,
            () -> {
                customerNull.validate();
            }
        );
    }

    @Test
    public void validate_NoThrowIfValidPhoneNumber() {
        validCustomer1.setPhoneNumber("1234567890");

        assertDoesNotThrow(
            () -> {
                validCustomer1.validate();
            }
        );
    }

    @Test
    public void validate_ThrowIfInvalidPhoneNumber() {
        validCustomer1.setPhoneNumber("1234567890");

        assertDoesNotThrow(
            () -> {
                validCustomer1.validate();
            }
        );
    }
}
