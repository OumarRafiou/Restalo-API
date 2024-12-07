package ca.ulaval.glo2003.domain;

import static com.google.common.truth.Truth.assertThat;

import ca.ulaval.glo2003.domain.restaurant.SearchRequest;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SearchRequestTest {
    private SearchRequest validSearchRequest;
    private SearchRequest invalidSearchRequest1;
    private SearchRequest invalidSearchRequest2;
    private SearchRequest testSearchRequest1;
    private SearchRequest testSearchRequest2;

    public SearchRequestTest() {}

    @BeforeEach
    public void setup() {
        Map<String, String> validHours = new HashMap<>();
        validHours.put("from", "11:00:00");
        validHours.put("to", "19:30:00");
        this.validSearchRequest = new SearchRequest();
        this.validSearchRequest.setName("control");
        this.validSearchRequest.setOpened(validHours);

        Map<String, String> invalidHours1 = new HashMap<>();
        invalidHours1.put("from", "11:00:00");
        invalidHours1.put("to", "25:30:00");
        this.invalidSearchRequest1 = new SearchRequest();
        this.invalidSearchRequest1.setOpened(invalidHours1);

        Map<String, String> invalidHours2 = new HashMap<>();
        invalidHours2.put("from", "34:00:00");
        this.invalidSearchRequest2 = new SearchRequest();
        this.invalidSearchRequest2.setOpened(invalidHours2);

        Map<String, String> testHours2 = new HashMap<>();
        testHours2.put("from", "8:00:00");
        testHours2.put("to", "15:45:00");
        this.testSearchRequest2 = new SearchRequest();
        this.testSearchRequest2.setName("tester2");
        this.testSearchRequest2.setOpened(testHours2);

        Map<String, String> testHours1 = new HashMap<>();
        testHours1.put("from", "17:00:00");
        testHours1.put("to", "23:45:00");
        this.testSearchRequest1 = new SearchRequest();
        this.testSearchRequest1.setName("tester1");
        this.testSearchRequest1.setOpened(testHours1);
    }

    @Test
    public void setName_canSetName() {
        testSearchRequest2.setName("Roger");

        assertThat(this.testSearchRequest2.getName()).isEqualTo("Roger");
    }

    @Test
    public void setOpened_CanSetOpened() {
        Map<String, String> NewHours = new HashMap<>();
        NewHours.put("from", "00:15:00");
        NewHours.put("to", "09:47:29");
        this.testSearchRequest2.setOpened(NewHours);
        assertThat(this.testSearchRequest2.getTime().getFrom())
            .isEqualTo("00:15:00");
        assertThat(this.testSearchRequest2.getTime().getTo())
            .isEqualTo("09:47:29");
    }

    @Test
    public void validate_ThrowIfInvalidTime() {
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> {
                this.invalidSearchRequest1.validate();
            }
        );
    }

    @Test
    public void verificationName_ReturnTrueIfContainsSameName() {
        testSearchRequest2.setName("Roger");
        assertThat(testSearchRequest2.verificationName("Roger"))
            .isEqualTo(true);
    }

    @Test
    public void verificationName_ReturnTrueIfContainsSameNameWithSpaceAndCapitals() {
        testSearchRequest2.setName("Roger");
        assertThat(testSearchRequest2.verificationName("R o Ge  r"))
            .isEqualTo(true);
        assertThat(testSearchRequest2.verificationName("I am rO geR the good"))
            .isEqualTo(true);
    }

    @Test
    public void verificationName_ReturnTrueIfContainsName() {
        testSearchRequest2.setName("Roger");
        assertThat(testSearchRequest2.verificationName("AntiRogerIstic"))
            .isEqualTo(true);
    }

    @Test
    public void verificationName_ReturnFalseIfNotContainsName() {
        testSearchRequest2.setName("Roger");
        assertThat(testSearchRequest2.verificationName("RogentDanscarotte"))
            .isEqualTo(false);
    }

    @Test
    public void verificationTime_ReturnTrueIfToEqualsClose() {
        Map<String, String> NewHours = new HashMap<>();
        NewHours.put("open", "08:00:00");
        NewHours.put("close", "23:45:00");
        Map<String, String> Hours = new HashMap<>();
        Hours.put("from", "17:00:00");
        Hours.put("to", "23:45:00");
        testSearchRequest2.setOpened(Hours);
        assertThat(testSearchRequest2.verificationTime(NewHours))
            .isEqualTo(true);
    }

    @Test
    public void verificationTime_ReturnTrueIfOpenInRestaurantTime() {
        Map<String, String> NewHours = new HashMap<>();
        NewHours.put("open", "08:00:00");
        NewHours.put("close", "23:45:00");
        Map<String, String> Hours = new HashMap<>();
        Hours.put("from", "09:15:00");
        Hours.put("to", "19:47:29");
        testSearchRequest2.setOpened(Hours);
        assertThat(testSearchRequest2.verificationTime(NewHours))
            .isEqualTo(true);
    }

    @Test
    public void verificationTime_ReturnFalseIfToAfterClose() {
        Map<String, String> NewHours = new HashMap<>();
        NewHours.put("open", "08:00:00");
        NewHours.put("close", "23:45:00");
        Map<String, String> Hours = new HashMap<>();
        Hours.put("to", "23:50:00");
        testSearchRequest2.setOpened(Hours);
        assertThat(testSearchRequest2.verificationTime(NewHours))
            .isEqualTo(false);
    }

    @Test
    public void verificationTime_ReturnFalseIfBeforeOpeningTime() {
        Map<String, String> NewHours = new HashMap<>();
        NewHours.put("open", "08:00:00");
        NewHours.put("close", "23:45:00");
        Map<String, String> Hours = new HashMap<>();
        Hours.put("to", "07:45:00");
        testSearchRequest2.setOpened(Hours);
        assertThat(testSearchRequest2.verificationTime(NewHours))
            .isEqualTo(false);
    }
}
