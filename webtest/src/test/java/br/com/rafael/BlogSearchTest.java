package br.com.rafael;

import java.time.Duration;
import java.util.List;
import java.util.logging.Logger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

class BlogSearchTest {
	private WebDriver driver;
	private WebDriverWait wait;
	private ChromeOptions options;
	private static final Logger logger = Logger.getLogger(BlogSearchTest.class.getName());

	@BeforeEach
	public void setUp() {
		String caminhoDoDriver;
		options = new ChromeOptions();

		if (System.getProperty("os.name").toLowerCase().contains("win")) {
			caminhoDoDriver = "C:\\chromedriver\\chromedriver-win64\\chromedriver.exe";
			System.setProperty("webdriver.chrome.driver", caminhoDoDriver);

			options.addArguments("--start-maximized");
			driver = new ChromeDriver(options);
		} else {
			caminhoDoDriver = "/usr/local/bin/chromedriver";
			System.setProperty("webdriver.chrome.driver", caminhoDoDriver);

			options.addArguments("--headless", "--start-maximized", "--no-sandbox");

			driver = new ChromeDriver(options);
		}
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
	}

	@Test
	void pesquisaComResultados() {
		logger.info("Iniciando teste de pesquisa com resultados...");

		realizarPesquisa("seguros");

		List<WebElement> resultados = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
				By.xpath("//main[contains(@class, 'site-main')]//div[contains(@class, 'ast-row')]//article")));

		Assertions.assertFalse(resultados.isEmpty(), "A pesquisa deveria retornar pelo menos um resultado.");
	}

	@Test
	void pesquisaSemResultados() {
		logger.info("Iniciando teste de pesquisa sem nenhum resultado...");

		realizarPesquisa("termoInexistente123456");

		WebElement mensagemSemResultado = wait.until(ExpectedConditions
				.visibilityOfElementLocated(By.xpath("//p[contains(text(), 'Lamentamos, mas nada foi encontrado')]")));

		Assertions.assertTrue(mensagemSemResultado.isDisplayed(), "A pesquisa deveria indicar que não há resultados.");
	}

	@AfterEach
	public void tearDown() {
	    if (driver != null) {
	        driver.quit();
	    }
	}

	private void realizarPesquisa(String textoDaPesquisa) {
		logger.info("Acessando o blog...");

		driver.get("https://blogdoagi.com.br/");
		Actions actions = new Actions(driver);

		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollTo(0, document.body.scrollHeight);");

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='web-stories-list__story-poster']//a")));

		js.executeScript("window.scrollTo(0, 0);");

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='web-stories-list__story-poster']//a")));

		By searchIconLocator = By.xpath("//div[@class='ast-search-icon']//a[@class='slide-search astra-search-icon']");
		WebElement searchIcon = wait.until(ExpectedConditions.elementToBeClickable(searchIconLocator));

		actions.moveToElement(searchIcon).click().perform();
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		By searchBoxLocator = By.xpath("//input[@id='search-field']");
	    WebElement searchBox = null;
		
		try {
			searchBox = wait.until(ExpectedConditions.elementToBeClickable(searchBoxLocator));
			actions.moveToElement(searchIcon).click().perform();
		} catch (Exception e) {
	        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
	        js.executeScript("window.scrollTo(0, 0);");

	        actions.moveToElement(searchIcon).click().perform();

	        searchBox = wait.until(ExpectedConditions.elementToBeClickable(searchBoxLocator));
		}
		searchBox.sendKeys(textoDaPesquisa, Keys.RETURN);
	}
}
