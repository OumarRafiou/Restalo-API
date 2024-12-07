package ca.ulaval.glo2003.controllers.assembler;

import ca.ulaval.glo2003.controllers.dto.AvailabilityDto;
import ca.ulaval.glo2003.domain.availability.Availability;

import java.util.ArrayList;
import java.util.List;

public class AvailabilityAssembler {

    public AvailabilityDto toDto(
            Availability availability
    ){
        return new AvailabilityDto(
                availability.getStart(),
                availability.getRemainingPlaces()
        );
    }
    public List<AvailabilityDto> toDtoList(List<Availability> availabilities) {
        List<AvailabilityDto> dtoList = new ArrayList<>();
        for (Availability availability : availabilities) {
            dtoList.add(toDto(availability));
        }
        return dtoList;
    }
}
