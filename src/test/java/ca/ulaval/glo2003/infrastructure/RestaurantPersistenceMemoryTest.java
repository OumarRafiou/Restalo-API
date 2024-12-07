package ca.ulaval.glo2003.infrastructure;

import ca.ulaval.glo2003.domain.restaurant.RestaurantPersistence;


class RestaurantPersistenceMemoryTest extends RestaurantPersistenceTest{
    private final RestaurantPersistence restaurantPersistence = new RestaurantPersistenceMemory();

    @Override
    protected RestaurantPersistence createPersistence() {
        return new RestaurantPersistenceMemory();
    }
}