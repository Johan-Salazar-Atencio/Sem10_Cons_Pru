# Laboratorio 10 – Pruebas Unitarias

**Curso:** Construcción y Pruebas de Software
**Estudiante:** Johan Salazar Atencio
**Stack:** Java 21 · Maven · JUnit 5 · Log4j 2 · Spring Boot 3.5 · MySQL (XAMPP / MariaDB 10.4)

| Carpeta | Contenido |
|---|---|
| `cps_lab10/` | Parte 1: proyecto Maven standalone con `Calculator` y `CalculatorTest` (JUnit 5 + Log4j) |
| `petclinic_test/` | Parte 2: aplicación PetClinic con pruebas CRUD de propietarios (`OwnerServiceTest`) sobre MySQL |

---

## Parte 1 – Pruebas unitarias standalone (`cps_lab10`)

- `Calculator`: `add`, `sub`, `mul`, `div`.
- `CalculatorTest`: un `@Test` por operación, incluidas las pruebas de la **TAREA** (multiplicación y división; la división también comprueba la `ArithmeticException` al dividir entre cero).
- Log4j (`log4j-core 2.14.1`): ciclo de vida con `@BeforeAll` (fija el nivel `INFO` con `Configurator`), `@AfterAll`, `@BeforeEach`, `@AfterEach` y un `log.info` en cada prueba.

```bash
cd cps_lab10
mvn clean test
```

Resultado: `Tests run: 4, Failures: 0, Errors: 0, Skipped: 0` – **BUILD SUCCESS**.

### Observaciones de las trazas

1. El orden de las trazas confirma el ciclo de vida de JUnit 5: `@BeforeAll` se ejecuta una sola vez al inicio, luego cada prueba queda envuelta por `@BeforeEach` / `@AfterEach`, y `@AfterAll` se ejecuta una sola vez al final.
2. JUnit 5 no ejecuta las pruebas en el orden en que están declaradas (se ejecutaron `testAdd`, `testDiv`, `testMul`, `testSub`). Por eso cada prueba debe ser independiente; aquí `@BeforeEach` crea una `Calculator` nueva en cada prueba.
3. Los métodos `@BeforeAll` / `@AfterAll` deben ser `static`, porque se ejecutan antes de crear y después de descartar las instancias de la clase de prueba.
4. Sin un archivo `log4j2.xml`, Log4j usa su configuración por defecto, que solo muestra `ERROR`. Por eso `Configurator.setRootLevel(Level.INFO)` en `@BeforeAll` es necesario para que aparezcan los `log.info`.

---

## Parte 2 – Pruebas CRUD de propietarios con MySQL (`petclinic_test`)

### Base de datos

Con XAMPP (MySQL en `localhost:3306`, usuario `root` sin contraseña) se ejecutan los scripts de la carpeta `data`, que crean la base `PETCLINIC_DB`:

```bash
mysql -uroot < petclinic_test/data/schema-mysql.sql
mysql -uroot < petclinic_test/data/data-mysql.sql
```

`application-mysql.yml` apunta a `jdbc:mysql://localhost:3306/PETCLINIC_DB`. En él se usa `spring.sql.init.mode: never`, porque el esquema ya se cargó con los scripts; con `always`, Spring ejecutaría contra MySQL los `schema.sql` / `data.sql` del classpath, que están escritos para H2.

### Código agregado

PetClinic solo traía la capa de `Pet`, así que se agregó la capa de `Owner` siguiendo el mismo patrón:

- `entities/Owner`, `dtos/OwnerDTO`, `mappers/OwnerMapper` (MapStruct), `repositories/OwnerRepository`
- `exceptions/OwnerNotFoundException`
- `services/OwnerService` + `OwnerServiceImpl`
- `test/.../services/OwnerServiceTest` (`@SpringBootTest`, `@Slf4j`, `@Autowired OwnerService`)

| Prueba | Qué valida |
|---|---|
| `testFindOwnerById` | El propietario con id 1 es *George Franklin* |
| `testFindOwnerByFirstName` | Buscar "George" devuelve exactamente 1 propietario |
| `testCreateOwner` | El propietario creado recibe un id y conserva todos sus campos |
| `testUpdateOwner` | Tras actualizar, se mantiene el id y cambian todos los campos |
| `testDeleteOwner` | Tras eliminar, `findById` lanza `OwnerNotFoundException` |

### Ejecución

```bash
cd petclinic_test
# Solo OwnerServiceTest contra MySQL
mvn -Dtest=com.tecsup.petclinic.services.OwnerServiceTest -Dspring.profiles.active=mysql clean test
# Toda la suite (Pet + Owner)
mvn test -Dspring.profiles.active=mysql
mvn test -Dspring.profiles.active=h2
```

Resultados:

- `OwnerServiceTest` con MySQL: `Tests run: 5, Failures: 0, Errors: 0`.
- Suite completa: `Tests run: 20, Failures: 0, Errors: 0` con ambos perfiles (`mysql` y `h2`).

### Observaciones

1. `@SpringBootTest` levanta el contexto completo de Spring (pool Hikari, JPA, repositorios), así que estas pruebas son en realidad de integración con la base de datos: necesitan que MySQL esté encendido.
2. El perfil activo (`-Dspring.profiles.active`) decide contra qué base se ejecutan las mismas pruebas sin cambiar el código: H2 en memoria (se reinicia en cada ejecución) o MySQL (los datos persisten).
3. Con MySQL, los registros que crean `testCreateOwner` y `testUpdateOwner` quedan en la tabla `owners`. Las pruebas de búsqueda usan datos semilla ("George", id 1) que las demás pruebas no modifican, para que el resultado no dependa de ejecuciones anteriores.
4. En VS Code, la extensión de Java puede recompilar `target/classes` con su propio compilador justo después de un `mvn clean`, lo que provoca errores `NoClassDefFoundError` que no vienen del código. Si ocurre, basta con volver a ejecutar `mvn test` (sin `clean`).

---

## Conclusiones

1. Las pruebas unitarias con JUnit 5 permiten verificar de forma automática y repetible el comportamiento de cada método, incluidos los casos de error (división entre cero, propietario inexistente), con aserciones claras (`assertEquals`, `assertThrows`, `assertNotNull`).
2. Las anotaciones del ciclo de vida (`@BeforeAll`, `@BeforeEach`, `@AfterEach`, `@AfterAll`), junto con un logger como Log4j o SLF4J, dejan trazas que muestran en qué orden se ejecutan las pruebas y facilitan el diagnóstico cuando una falla.
3. Con Spring Boot y los perfiles de configuración, las mismas pruebas CRUD se ejecutan contra H2 o MySQL sin cambiar el código. Las pruebas deben ser independientes entre sí y de los datos que dejan otras ejecuciones.
