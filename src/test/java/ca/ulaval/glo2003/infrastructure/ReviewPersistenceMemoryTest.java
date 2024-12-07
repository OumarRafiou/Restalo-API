package ca.ulaval.glo2003.infrastructure;

import ca.ulaval.glo2003.domain.review.ReviewPersistence;

import static org.junit.jupiter.api.Assertions.*;

class ReviewPersistenceMemoryTest extends ReviewPersistenceTest {
    private final ReviewPersistence reviewPersistence = new ReviewPersistenceMemory();

    @Override
    protected ReviewPersistence createPersistence() {
        return new ReviewPersistenceMemory();
    }

}