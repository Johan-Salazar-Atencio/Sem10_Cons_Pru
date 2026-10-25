package pe.edu.tecsup.lab10;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CalculatorTest {

    private static final Logger log = LogManager.getLogger(CalculatorTest.class);

    private Calculator calculator;

    @BeforeAll
    public static void beforeAll() {
        Configurator.setRootLevel(Level.INFO);
        Configurator.setLevel(CalculatorTest.class.getName(), Level.INFO);
        log.info("@BeforeAll - se ejecuta una vez antes de todas las pruebas");
    }

    @AfterAll
    public static void afterAll() {
        log.info("@AfterAll - se ejecuta una vez despues de todas las pruebas");
    }

    @BeforeEach
    public void beforeEach() {
        calculator = new Calculator();
        log.info("@BeforeEach - se ejecuta antes de cada prueba");
    }

    @AfterEach
    public void afterEach() {
        calculator = null;
        log.info("@AfterEach - se ejecuta despues de cada prueba");
    }

    @Test
    public void testAdd() {
        log.info("testAdd: 2 + 3");
        assertEquals(5, calculator.add(2, 3));
    }

    @Test
    public void testSub() {
        log.info("testSub: 5 - 3");
        assertEquals(2, calculator.sub(5, 3));
    }

    @Test
    public void testMul() {
        log.info("testMul: 4 * 3");
        assertEquals(12, calculator.mul(4, 3));
    }

    @Test
    public void testDiv() {
        log.info("testDiv: 10 / 2 y division entre cero");
        assertEquals(5, calculator.div(10, 2));
        assertThrows(ArithmeticException.class, () -> calculator.div(1, 0));
    }
}
