package ca.ulaval.glo2003.domain;

import static com.google.common.truth.Truth.assertThat;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TimeDurationTest {
    TimeDuration validTimeDuration;
    TimeDuration incompleteTimeDuration;

    @BeforeEach
    public void setup() {
        this.validTimeDuration = new TimeDuration("10:00:00", "20:30:00");
        Map<String, String> incompleteHours = new HashMap<>();
        this.incompleteTimeDuration = new TimeDuration();
    }

    @Test
    public void setFrom_CanSetFrom() {
        validTimeDuration.setFrom("09:00:00");
        assertThat(validTimeDuration.getFrom()).isEqualTo("09:00:00");
    }

    @Test
    public void setFrom_CanSetTo() {
        validTimeDuration.setTo("08:00:00");
        assertThat(validTimeDuration.getTo()).isEqualTo("08:00:00");
    }

    @Test
    public void setOpen_CanSetOpen() {
        validTimeDuration.setOpen("09:00:00");
        assertThat(validTimeDuration.getOpen()).isEqualTo("09:00:00");
    }

    @Test
    public void setClose_CanSetClose() {
        validTimeDuration.setClose("22:00:00");
        assertThat(validTimeDuration.getClose()).isEqualTo("22:00:00");
    }

    @Test
    public void containsFrom_ReturnsTrueIfExist() {
        assertThat(validTimeDuration.containsFrom()).isTrue();
    }

    @Test
    public void containsFrom_ReturnsFalseIfNotExist() {
        assertThat(incompleteTimeDuration.containsFrom()).isFalse();
    }

    @Test
    public void containsTo_ReturnsTrueIfExist() {
        assertThat(validTimeDuration.containsTo()).isTrue();
    }

    @Test
    public void containsTo_ReturnsFalseIfNotExist() {
        assertThat(incompleteTimeDuration.containsTo()).isFalse();
    }
}
