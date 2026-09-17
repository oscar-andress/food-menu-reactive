package demo.reactividad.repository;

import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.r2dbc.test.autoconfigure.DataR2dbcTest;

import demo.reactividad.entity.Menu;
import demo.reactividad.testcontainers.AbstractPostgresContainerTest;
import reactor.test.StepVerifier;

@DataR2dbcTest
class MenuRepositoryTest extends AbstractPostgresContainerTest {

    private static final String TITLE = "DEVOS";
    private static final String DESCRIPTION = "Menu de prueba";
    private static final String UPDATED_DESCRIPTION = "Lorem Ipsum";
    private static final Logger log = LoggerFactory.getLogger(MenuRepositoryTest.class);

    @Autowired
    private MenuRepository menuRepository;

    private Menu menu;

    @BeforeEach
    void setUp() {
        this.menu = this.menuRepository.deleteAll()
                .then(this.menuRepository.save(new Menu(TITLE, DESCRIPTION)))
                .block();
    }

    @Test
    void findAll_Success() {
        this.menuRepository.findAll()
                .doOnNext(m -> log.info("{}", m))
                .as(StepVerifier::create)
                .assertNext(m -> {
                    Assertions.assertEquals(TITLE, m.getTitle());
                    Assertions.assertEquals(DESCRIPTION, m.getDescription());
                })
                .expectComplete()
                .verify();
    }

    @Test
    void findById_Success() {
        this.menuRepository.findById(this.menu.getId())
                .doOnNext(m -> log.info("{}", m))
                .as(StepVerifier::create)
                .assertNext(m -> Assertions.assertEquals(TITLE, m.getTitle()))
                .expectComplete()
                .verify();
    }

    @Test
    void findById_WhenMenuDoesNotExist_ReturnsEmpty() {
        this.menuRepository.findById(UUID.randomUUID())
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

    @Test
    void updateMenu_Success() {
        this.menuRepository.findById(this.menu.getId())
                .doOnNext(m -> m.setDescription(UPDATED_DESCRIPTION))
                .flatMap(m -> this.menuRepository.save(m))
                .doOnNext(m -> log.info("{}", m))
                .as(StepVerifier::create)
                .assertNext(c -> Assertions.assertEquals(UPDATED_DESCRIPTION, c.getDescription()))
                .expectComplete()
                .verify();
    }

    @Test
    void save_WhenTitleIsNull_Fails() {
        Menu invalidMenu = new Menu();
        invalidMenu.setDescription(DESCRIPTION);

        this.menuRepository.save(invalidMenu)
                .as(StepVerifier::create)
                .expectError()
                .verify();
    }
}
