package br.com.rafael;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.matchesPattern;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class DogApiTests {

    private static ExtentReports extent;
    private static ExtentTest test;

    @BeforeAll
    public static void setup() {
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter("target/extent-report.html");
        sparkReporter.config().setReportName("Report DOG-API");

        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);

        RestAssured.baseURI = "https://dog.ceo/api";
    }

    @AfterAll
    public static void tearDown() {
        extent.flush();
    }

    @Test
    @DisplayName("Obter lista de raças")
    public void listarTodasRacas() {
        test = extent.createTest("Obter lista de raças", "Este teste verifica se a API retorna uma lista de todas as raças de cães disponíveis.");

        Response response = RestAssured.given()
            .when()
            .get("/breeds/list/all");

        test.info("Requisição GET para /breeds/list/all executada");

        response.then()
            .statusCode(200)
            .body("message", notNullValue())
            .body("message", instanceOf(Map.class))
            .body("message", not(empty()))
            .body("message", hasKey("bulldog"))
            .body("message.bulldog", instanceOf(List.class))
            .body("message.bulldog.size()", greaterThan(0));

        test.pass("Verificações de resposta bem-sucedidas");
    }

    @Test
    @DisplayName("Obter imagens de uma raça")
    public void listarImagensRaca() {
        test = extent.createTest("Obter imagens de uma raça", "Este teste verifica se a API retorna uma lista de imagens para uma raça específica.");

        Response response = RestAssured.given()
            .pathParam("breed", "bulldog")
            .when()
            .get("/breed/{breed}/images");

        test.info("Requisição GET para /breed/bulldog/images executada");

        response.then()
            .statusCode(200)
            .body("message", notNullValue())
            .body("message", instanceOf(List.class))
            .body("message.size()", greaterThan(0))
            .body("message[0]", matchesPattern("https://images.dog.ceo/breeds/.*\\.(jpg|png)"));

        test.pass("Verificações de resposta bem-sucedidas");
    }

    @Test
    @DisplayName("Obter imagens de uma raça inexistente")
    public void listarImagensDeUmaRacaInexistente() {
        test = extent.createTest("Obter imagens de uma raça inexistente", "Este teste verifica o comportamento da API quando é solicitada uma raça inexistente.");

        Response response = RestAssured.given()
            .pathParam("breed", "xyz123")
            .when()
            .get("/breed/{breed}/images");

        test.info("Requisição GET para /breed/xyz123/images executada");

        response.then()
            .statusCode(404)
            .body("message", matchesPattern(".*not found.*"))
            .body("message", matchesPattern("Breed not found.*"));

        test.pass("Verificações de resposta bem-sucedidas");
    }

    @Test
    @DisplayName("Obter imagem aleatória de um cão")
    public void listarImagemAleatoria() {
        test = extent.createTest("Obter imagem aleatória de um cão", "Este teste verifica se a API retorna uma URL válida para uma imagem aleatória de um cão.");

        Response response = RestAssured.given()
            .when()
            .get("/breeds/image/random");

        test.info("Requisição GET para /breeds/image/random executada");

        response.then()
            .statusCode(200)
            .body("message", notNullValue())
            .body("message", matchesPattern("https://images.dog.ceo/breeds/.*\\.(jpg|png)"));

        test.pass("Verificações de resposta bem-sucedidas");
    }

    @Test
    @DisplayName("Testar com parâmetros inválidos em /breeds/image/random")
    public void testeComParametrosInvalidos() {
        test = extent.createTest("Testar com parâmetros inválidos em /breeds/image/random", "Este teste verifica como a API lida com parâmetros inválidos.");

        Response response = RestAssured.given()
            .queryParam("invalidParam", "xyz")
            .when()
            .get("/breeds/image/random");

        test.info("Requisição GET para /breeds/image/random com parâmetro inválido executada");

        response.then()
            .statusCode(200)
            .body("message", notNullValue())
            .body("message", matchesPattern("https://images.dog.ceo/breeds/.*\\.(jpg|png)"));

        test.pass("Verificações de resposta bem-sucedidas");
    }

    @Test
    @DisplayName("Verificar tempo de resposta da API")
    public void testeTempoResposta() {
        test = extent.createTest("Verificar tempo de resposta da API", "Este teste verifica o tempo de resposta da API.");

        Response response = RestAssured.given()
            .when()
            .get("/breeds/list/all");

        test.info("Requisição GET para /breeds/list/all executada");

        response.then()
            .statusCode(200)
            .time(lessThan(2000L));

        test.pass("Verificação de tempo de resposta bem-sucedida");
    }
}
