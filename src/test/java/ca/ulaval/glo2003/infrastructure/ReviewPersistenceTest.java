package ca.ulaval.glo2003.infrastructure;

import ca.ulaval.glo2003.domain.review.Review;
import ca.ulaval.glo2003.domain.review.ReviewPersistence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public abstract class ReviewPersistenceTest {
    protected abstract ReviewPersistence createPersistence();
    private ReviewPersistence reviewPersistence;
    @BeforeEach
    public void setUp(){
        reviewPersistence = createPersistence();
    }
    @Test
    public void givenSavedReview_whenFindingAll_shouldReturnSavedReview(){
        Review review = new Review("123","Oumar","12",4,"Good","Lol","Oumar");
        reviewPersistence.save(review);

        var foundReview = reviewPersistence.listAll();

        assertEquals(foundReview.get(0).getReviewId(),"Lol");
    }
    @Test
    public void givenManySavedReviews_whenFindingAll_shouldReturnAllSaveReviews(){
        Review firstReview = new Review("456","Lolipop","2022-10-13",4,"Good","66948","Oumar");
        Review secondReview = new Review("123","Allo","2022-10-13",4,"Good","66748","Salut");
        reviewPersistence.save(firstReview);
        reviewPersistence.save(secondReview);

        var foundAllReview = reviewPersistence.listAll();
        int sizeList = foundAllReview.size();

        assertEquals(sizeList,2);
    }
    @Test
    public void givenNoReview_whenFindingAll_shouldReturnAnEmptyList(){
        var foundAllReview = reviewPersistence.listAll();

        assertTrue(foundAllReview.isEmpty());
    }
    @Test
    public void givenReviewIdValid_shouldReturnReview(){
        Review firstReview = new Review("456","Lolipop","2022-10-13",4,"Good","66948","Oumar");
        reviewPersistence.save(firstReview);

        var foundReviewById = reviewPersistence.getReviewById("66948");

        assertEquals(foundReviewById.getReviewId(),"66948");
    }

    @Test
    public void givenReviewIdNotValid_shouldThrowException(){
        Review firstReview = new Review("456","Lolipop","2022-10-13",4,"Good","66948","Oumar");
        Review secondReview = new Review("123","Allo","2022-10-13",4,"Good","66748","Salut");
        reviewPersistence.save(firstReview);
        reviewPersistence.save(secondReview);

        assertThrows(Exception.class,()->{
            reviewPersistence.getReviewById("Invalid");
        });
    }

    @Test
    public void givenReviewWIthId_ToBeDeleted_shouldBeDeleted(){
        Review firstReview = new Review("456","Lolipop","2022-10-13",4,"Good","66948","Oumar");
        Review secondReview = new Review("123","Allo","2022-10-13",4,"Good","66748","Salut");
        reviewPersistence.save(firstReview);
        reviewPersistence.save(secondReview);

        reviewPersistence.delete("66948");

        assertThrows(Exception.class,()->{
            reviewPersistence.getReviewById("66948");
        });


    }

    @Test
    public void givenReviewList_shouldBeCleared(){
        Review firstReview = new Review("456","Lolipop","2022-10-13",4,"Good","66948","Oumar");
        Review secondReview = new Review("123","Allo","2022-10-13",4,"Good","66748","Salut");
        reviewPersistence.save(firstReview);
        reviewPersistence.save(secondReview);

        reviewPersistence.clear();

        var foundAllReview = reviewPersistence.listAll();

        assertTrue(foundAllReview.isEmpty());
    }

    @Test
    public void givenReviewByRestaurantId_shouldReturnReview(){
        Review firstReview = new Review("456","Lolipop","2022-10-13",4,"Good","66948","Oumar");
        Review secondReview = new Review("123","Allo","2022-10-13",4,"Good","66748","Salut");
        reviewPersistence.save(firstReview);
        reviewPersistence.save(secondReview);

        var foundReviewByRestaurantId = reviewPersistence.getReviewByRestaurantId("456");

        assertThat(foundReviewByRestaurantId.get(0).getReviewId()).isEqualTo("66948");
    }

    @Test
    public void givenReviewByRestaurantIdNotValid_shouldThrowException(){
        Review firstReview = new Review("456","Lolipop","2022-10-13",4,"Good","66948","Oumar");
        Review secondReview = new Review("123","Allo","2022-10-13",4,"Good","66748","Salut");
        reviewPersistence.save(firstReview);
        reviewPersistence.save(secondReview);

        assertThrows(Exception.class,()->{
            reviewPersistence.getReviewByRestaurantId("Invalid");
        });
    }

    @Test
    public void givenReviewByUsername_shouldReturnReview(){
        Review firstReview = new Review("456","Lolipop","2022-10-13",4,"Good","66948","Oumar");
        Review secondReview = new Review("123","Allo","2022-10-13",4,"Good","66748","Salut");
        reviewPersistence.save(firstReview);
        reviewPersistence.save(secondReview);

        var foundReviewByUsername = reviewPersistence.getReviewByUsername("Salut");

        assertEquals(foundReviewByUsername.get(0).getReviewId(),"66748");
        }

    @Test
    public void givenReviewByUsernameNotValid_shouldThrowException(){
        Review firstReview = new Review("456","Lolipop","2022-10-13",4,"Good","66948","Oumar");
        Review secondReview = new Review("123","Allo","2022-10-13",4,"Good","66748","Salut");
        reviewPersistence.save(firstReview);
        reviewPersistence.save(secondReview);

        assertThrows(Exception.class,()->{
            reviewPersistence.getReviewByUsername("Invalid");
        });
    }



}