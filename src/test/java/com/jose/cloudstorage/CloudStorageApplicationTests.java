package com.jose.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.util.ObjectUtils;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;
	private WebDriverWait wait;

	// Inputs
	private String firstName = "Jose";
	private String lastName = "Soto";
	private String username = "josesoto";
	private String password = "123456";

	public void sleep() throws InterruptedException {
		Thread.sleep(4000);
	}

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
		this.wait = new WebDriverWait(driver, 4000);
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	// Test get login page
	@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	// Test get signup page
	@Test
	public void getSignUpPage() {
		driver.get("http://localhost:" + this.port + "/signup");
		Assertions.assertEquals("Sign Up", driver.getTitle());
	}

	// Test unauthorized access
	@Test
	public void getUnauthorizedLogin() {
		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	// Test signup, login and logout
	@Test
	public void testSignupLoginLogout() throws InterruptedException {
		// sign up
		driver.get("http://localhost:" + this.port + "/signup");
		driver.findElement(By.id("inputFirstName")).sendKeys(firstName);
		driver.findElement(By.id("inputLastName")).sendKeys(lastName);
		driver.findElement(By.id("inputUsername")).sendKeys(username);
		driver.findElement(By.id("inputPassword")).sendKeys(password);
		wait.until(webDriver -> webDriver.findElement(By.id("submit-button"))).click();

		// redirect to login page
		driver.get("http://localhost:" + this.port + "/login");

		// login
		driver.findElement(By.id("inputUsername")).sendKeys(username);
		driver.findElement(By.id("inputPassword")).sendKeys(password);
		wait.until(webDriver -> webDriver.findElement(By.id("submit-button"))).click();

		// check if user has successfully logged in
		Assertions.assertEquals("Home", driver.getTitle());

		// check logout
		wait.until(webDriver -> webDriver.findElement(By.id("buttonLogout"))).click();

		sleep();

		// check if user was successfully logged out
		Assertions.assertEquals("Login", driver.getTitle());
	}

	// Test signup and login
	@Test
	public void testSignupLogin() throws InterruptedException {
		// sign up
		driver.get("http://localhost:" + this.port + "/signup");
		driver.findElement(By.id("inputFirstName")).sendKeys(firstName);
		driver.findElement(By.id("inputLastName")).sendKeys(lastName);
		driver.findElement(By.id("inputUsername")).sendKeys(username);
		driver.findElement(By.id("inputPassword")).sendKeys(password);
		wait.until(webDriver -> webDriver.findElement(By.id("submit-button"))).click();

		// redirect to login page
		driver.get("http://localhost:" + this.port + "/login");

		// login
		driver.findElement(By.id("inputUsername")).sendKeys(username);
		driver.findElement(By.id("inputPassword")).sendKeys(password);
		wait.until(webDriver -> webDriver.findElement(By.id("submit-button"))).click();

		sleep();

		// check if user has successfully logged in
		Assertions.assertEquals("Home", driver.getTitle());
	}

	// Test notes
	@Test
	public void testNote() throws InterruptedException {
		// signup and login
		testSignupLogin();

		// go to notes tab
		wait.until(webDriver -> webDriver.findElement(By.id("nav-notes-tab"))).click();

		// create a new note
		boolean noteCreated = false;
		try {
			sleep();
			wait.until(webDriver -> webDriver.findElement(By.id("buttonAddNewNote"))).click();
			sleep();
			driver.findElement(By.id("note-title")).sendKeys("test-note-title");
			driver.findElement(By.id("note-description")).sendKeys("test-note-description");
			wait.until(webDriver -> webDriver.findElement(By.id("saveNoteButton"))).click();
			noteCreated = true;
		} catch(Exception e) {
			e.printStackTrace();
		}

		// go to notes tab
		driver.get("http://localhost:" + this.port + "/home");
		sleep();
		wait.until(webDriver -> webDriver.findElement(By.id("nav-notes-tab"))).click();

		// create another note
		try {
			sleep();
			wait.until(webDriver -> webDriver.findElement(By.id("buttonAddNewNote"))).click();
			sleep();
			driver.findElement(By.id("note-title")).sendKeys("test-note-title-2");
			driver.findElement(By.id("note-description")).sendKeys("test-note-description-2");
			wait.until(webDriver -> webDriver.findElement(By.id("saveNoteButton"))).click();
		} catch(Exception e) {
			e.printStackTrace();
		}

		// go to notes tab
		driver.get("http://localhost:" + this.port + "/home");
		sleep();
		wait.until(webDriver -> webDriver.findElement(By.id("nav-notes-tab"))).click();

		// delete a note
		boolean noteDeleted = false;
		WebElement notesTable = wait.until(webDriver -> webDriver.findElement(By.id("userTable")));
		List<WebElement> noteLink = notesTable.findElements(By.tagName("a"));
		for (WebElement deleteNoteButton : noteLink) {
			sleep();
			deleteNoteButton.click();
			noteDeleted = true;
			break;
		}

		// go to notes tab
		driver.get("http://localhost:" + this.port + "/home");
		sleep();
		wait.until(webDriver -> webDriver.findElement(By.id("nav-notes-tab"))).click();

		// edit a note
		notesTable = wait.until(webDriver -> webDriver.findElement(By.id("userTable")));
		List<WebElement> noteList = notesTable.findElements(By.tagName("td"));
		boolean noteEdited = false;
		for (WebElement row : noteList) {
			sleep();
			WebElement editButton = null;
			editButton = row.findElement(By.tagName("button"));;
			editButton.click();
			if (!ObjectUtils.isEmpty(editButton)) {
				sleep();
				driver.findElement(By.id("note-title")).sendKeys("-edited");
				driver.findElement(By.id("note-description")).sendKeys("-edited");
				wait.until(webDriver -> webDriver.findElement(By.id("saveNoteButton"))).click();
				noteEdited = true;
				break;
			}
		}

		sleep();

		Assertions.assertTrue(noteCreated);
		Assertions.assertTrue(noteDeleted);
		Assertions.assertTrue(noteEdited);
	}

	// Test credentials
	@Test
	public void testCredentials() throws InterruptedException {
		// signup and login
		testSignupLogin();

		// go to credentials tab
		wait.until(webDriver -> webDriver.findElement(By.id("nav-credentials-tab"))).click();

		// create a new credential
		boolean credentialCreated = false;
		try {
			sleep();
			wait.until(webDriver -> webDriver.findElement(By.id("buttonAddNewCredential"))).click();
			sleep();
			driver.findElement(By.id("credential-url")).sendKeys("http://www.google.com");
			driver.findElement(By.id("credential-username")).sendKeys(username);
			driver.findElement(By.id("credential-password")).sendKeys(password);
			wait.until(webDriver -> webDriver.findElement(By.id("saveCredentialButton"))).click();
			credentialCreated = true;
		} catch(Exception e) {
			e.printStackTrace();
		}

		// go to credentials tab
		driver.get("http://localhost:" + this.port + "/home");
		sleep();
		wait.until(webDriver -> webDriver.findElement(By.id("nav-credentials-tab"))).click();

		try {
			sleep();
			wait.until(webDriver -> webDriver.findElement(By.id("buttonAddNewCredential"))).click();
			sleep();
			driver.findElement(By.id("credential-url")).sendKeys("http://www.gmail.com");
			driver.findElement(By.id("credential-username")).sendKeys(username);
			driver.findElement(By.id("credential-password")).sendKeys(password);
			wait.until(webDriver -> webDriver.findElement(By.id("saveCredentialButton"))).click();
			credentialCreated = true;
		} catch(Exception e) {
			e.printStackTrace();
		}

		// go to credentials tab
		driver.get("http://localhost:" + this.port + "/home");
		sleep();
		wait.until(webDriver -> webDriver.findElement(By.id("nav-credentials-tab"))).click();

		// delete a credential
		boolean credentialDeleted = false;
		WebElement credentialsTable = wait.until(webDriver -> webDriver.findElement(By.id("credentialTable")));
		List<WebElement> noteLink = credentialsTable.findElements(By.tagName("a"));
		for (WebElement deleteNoteButton : noteLink) {
			sleep();
			deleteNoteButton.click();
			credentialDeleted = true;
			break;
		}

		// go to credentials tab
		driver.get("http://localhost:" + this.port + "/home");
		sleep();
		wait.until(webDriver -> webDriver.findElement(By.id("nav-credentials-tab"))).click();

		// edit a credential
		credentialsTable = wait.until(webDriver -> webDriver.findElement(By.id("credentialTable")));
		List<WebElement> credentialList = credentialsTable.findElements(By.tagName("td"));
		boolean credentialEdited = false;
		for (WebElement row : credentialList) {
			sleep();
			WebElement editButton = null;
			editButton = row.findElement(By.tagName("button"));;
			editButton.click();
			if (!ObjectUtils.isEmpty(editButton)) {
				sleep();
				driver.findElement(By.id("credential-url")).sendKeys("http://www.google.com");
				driver.findElement(By.id("credential-username")).sendKeys(username);
				driver.findElement(By.id("credential-password")).sendKeys(username);
				wait.until(webDriver -> webDriver.findElement(By.id("saveCredentialButton"))).click();
				credentialEdited = true;
				break;
			}
		}

		sleep();

		Assertions.assertTrue(credentialCreated);
		Assertions.assertTrue(credentialDeleted);
		Assertions.assertTrue(credentialEdited);
	}

}
