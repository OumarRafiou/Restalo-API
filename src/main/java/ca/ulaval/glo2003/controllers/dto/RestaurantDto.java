package ca.ulaval.glo2003.controllers.dto;

import java.util.Map;

public record RestaurantDto(String id,
                            String name,
                            Integer capacity,
                            Map<String, String> hours,
                            Map<String, Integer> reservations) {
}

