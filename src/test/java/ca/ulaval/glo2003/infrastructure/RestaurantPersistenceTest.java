package ca.ulaval.glo2003.infrastructure;
import ca.ulaval.glo2003.domain.restaurant.Restaurant;
import ca.ulaval.glo2003.domain.restaurant.RestaurantPersistence;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public abstract class RestaurantPersistenceTest{
    protected abstract RestaurantPersistence createPersistence();
    private RestaurantPersistence restaurantPersistence;

    @BeforeEach
    public void setUp(){
        restaurantPersistence = createPersistence();
    }

    @Test
    public void givenSavedRestaurant_whenFindingAll_shouldReturnSavedRestaurant(){
        Restaurant restaurant = new Restaurant("123","Oumar",12,"Lol",new HashMap<>(),new HashMap<>());
        restaurantPersistence.save(restaurant);

        var foundRestaurant = restaurantPersistence.listAll();

        assertEquals(foundRestaurant.get(0).getId(),"123");
    }

    @Test
    public void givenManySavedRestaurants_whenFindingAll_shouldReturnAllSaveRestaurants(){
        Restaurant firstRestaurant = new Restaurant("456","Lolipop",12,"Lol",new HashMap<>(),new HashMap<>());
        Restaurant secondRestaurant = new Restaurant("123","Allo",12,"Lol",new HashMap<>(),new HashMap<>());
        restaurantPersistence.save(firstRestaurant);
        restaurantPersistence.save(secondRestaurant);

        var foundAllRestaurant = restaurantPersistence.listAll();
        int sizeList = foundAllRestaurant.size();

        assertEquals(sizeList,2);
    }

    @Test
    public void givenNoRestaurant_whenFindingAll_shouldReturnAnEmptyList(){
        var foundAllRestaurant = restaurantPersistence.listAll();

        assertTrue(foundAllRestaurant.isEmpty());
    }

    @Test
    public void givenRestaurantIdNotValid_shouldThrowExcption(){
        Restaurant firstRestaurant = new Restaurant("456","Lolipop",12,"Lol",new HashMap<>(),new HashMap<>());
        Restaurant secondRestaurant = new Restaurant("123","Allo",12,"Lol",new HashMap<>(),new HashMap<>());
        restaurantPersistence.save(firstRestaurant);
        restaurantPersistence.save(secondRestaurant);


        assertThrows(NotFoundException.class,()->{
            restaurantPersistence.getRestaurantById("Invalid");
        });
    }

    @Test
    public void givenRestaurantValidId_shouldReturnRestaurant(){
        Restaurant firstRestaurant = new Restaurant("456","Lolipop",12,"Lol",new HashMap<>(),new HashMap<>());
        restaurantPersistence.save(firstRestaurant);

        var foundRestaurantById = restaurantPersistence.getRestaurantById("456");

        assertThat(foundRestaurantById.equals(firstRestaurant));
    }

    @Test
    public void givenRestaurantExist_shouldReturnTrue(){
        Restaurant firstRestaurant = new Restaurant("456","Lolipop",12,"Lol",new HashMap<>(),new HashMap<>());
        restaurantPersistence.save(firstRestaurant);

        var foundRestaurantExist = restaurantPersistence.isRestaurantExist("456");
         assertTrue(foundRestaurantExist);
    }

    @Test
    public void givenRestaurantExist_shouldReturnFalse(){

        var foundRestaurantExist = restaurantPersistence.isRestaurantExist("456");

        assertFalse(foundRestaurantExist);
    }

    @Test
    public void givenRestaurantWithId_ToBeDelete_ShouldBeDeleted(){
        Restaurant firstRestaurant = new Restaurant("456","Lolipop",12,"Lol",new HashMap<>(),new HashMap<>());
        Restaurant secondRestaurant = new Restaurant("123","Allo",12,"Lol",new HashMap<>(),new HashMap<>());
        restaurantPersistence.save(firstRestaurant);
        restaurantPersistence.save(secondRestaurant);
        restaurantPersistence.delete("456");

        assertEquals(restaurantPersistence.listAll().get(0).getId(),"123");
        assertEquals(restaurantPersistence.listAll().size(),1);
    }

    @Test
    public void givenRestaurantList_ShouldBeCleared(){
        Restaurant firstRestaurant = new Restaurant("456","Lolipop",12,"Lol",new HashMap<>(),new HashMap<>());
        Restaurant secondRestaurant = new Restaurant("123","Allo",12,"Lol",new HashMap<>(),new HashMap<>());
        restaurantPersistence.save(firstRestaurant);
        restaurantPersistence.save(secondRestaurant);
        restaurantPersistence.clear();

        assertThat(restaurantPersistence.listAll().isEmpty());

    }








}