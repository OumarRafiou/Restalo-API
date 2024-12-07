package ca.ulaval.glo2003.controllers.dto;

import java.util.Map;

public record ReviewDto(
                            String name,
                            String date,
                            Integer rating,
                            String message) {
}