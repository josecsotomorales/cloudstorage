package com.jose.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.util.ObjectUtils;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;

	// Inputs
	private String firstName = "Jose";
	private String lastName = "Soto";
	private String username = "josesoto";
	private String password = "123456";

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
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
		Thread.sleep(4000);
		driver.findElement(By.id("submit-button")).click();

		// redirect to login page
		driver.get("http://localhost:" + this.port + "/login");

		// login
		driver.findElement(By.id("inputUsername")).sendKeys(username);
		driver.findElement(By.id("inputPassword")).sendKeys(password);
		driver.findElement(By.id("submit-button")).click();
		Thread.sleep(4000);

		// check if user has successfully logged in
		Assertions.assertEquals("Home", driver.getTitle());

		// check logout
		driver.findElement(By.id("buttonLogout")).click();

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
		Thread.sleep(4000);
		driver.findElement(By.id("submit-button")).click();

		// redirect to login page
		driver.get("http://localhost:" + this.port + "/login");

		// login
		driver.findElement(By.id("inputUsername")).sendKeys(username);
		driver.findElement(By.id("inputPassword")).sendKeys(password);
		driver.findElement(By.id("submit-button")).click();
		Thread.sleep(4000);

		// check if user has successfully logged in
		Assertions.assertEquals("Home", driver.getTitle());
	}

	// Test notes
	@Test
	public void testNote() throws InterruptedException {
		// signup and login
		testSignupLogin();

		// go to notes tab
		driver.findElement(By.id("nav-notes-tab")).click();
		Thread.sleep(4000);

		// create a new note
		boolean noteCreated = false;
		try {
			driver.findElement(By.id("buttonAddNewNote")).click();
			Thread.sleep(4000);
			driver.findElement(By.id("note-title")).sendKeys("test-note-title");
			driver.findElement(By.id("note-description")).sendKeys("test-note-description");
			driver.findElement(By.id("saveNoteButton")).click();
			Thread.sleep(4000);
			noteCreated = true;
		} catch(Exception e) {
			e.printStackTrace();
		}

		// go to notes tab
		driver.findElement(By.linkText("here")).click();
		driver.findElement(By.id("nav-notes-tab")).click();
		Thread.sleep(4000);

		// create another note
		try {
			driver.findElement(By.id("buttonAddNewNote")).click();
			Thread.sleep(4000);
			driver.findElement(By.id("note-title")).sendKeys("test-note-title-2");
			driver.findElement(By.id("note-description")).sendKeys("test-note-description-2");
			driver.findElement(By.id("saveNoteButton")).click();
			Thread.sleep(4000);
		} catch(Exception e) {
			e.printStackTrace();
		}

		// go to notes tab
		driver.findElement(By.id("nav-notes-tab")).click();
		Thread.sleep(4000);

		// delete a note
		Thread.sleep(4000);
		boolean noteDeleted = false;
		WebElement notesTable = driver.findElement(By.id("userTable"));
		List<WebElement> noteLink = notesTable.findElements(By.tagName("a"));
		for (WebElement deleteNoteButton : noteLink) {
			deleteNoteButton.click();
			noteDeleted = true;
			break;
		}

		// go to notes tab
		Thread.sleep(4000);
		driver.findElement(By.linkText("here")).click();
		driver.findElement(By.id("nav-notes-tab")).click();
		Thread.sleep(4000);

		// edit a note
		Thread.sleep(4000);
		notesTable = driver.findElement(By.id("userTable"));
		List<WebElement> noteList = notesTable.findElements(By.tagName("td"));
		boolean noteEdited = false;
		for (WebElement row : noteList) {
			WebElement editButton = null;
			editButton = row.findElement(By.tagName("button"));
			editButton.click();
			if (!ObjectUtils.isEmpty(editButton)) {
				Thread.sleep(4000);
				driver.findElement(By.id("note-title")).sendKeys("-edited");
				driver.findElement(By.id("note-description")).sendKeys("-edited");
				driver.findElement(By.id("saveNoteButton")).click();
				noteEdited = true;
				Assertions.assertEquals("Home", driver.getTitle());
				break;
			}
		}
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
		driver.findElement(By.id("nav-credentials-tab")).click();
		Thread.sleep(4000);

		// create a new credential
		boolean credentialCreated = false;
		try {
			driver.findElement(By.id("new-credential")).click();
			Thread.sleep(4000);
			driver.findElement(By.id("credential-url")).sendKeys("http://www.google.com");
			driver.findElement(By.id("credential-username")).sendKeys(username);
			driver.findElement(By.id("credential-password")).sendKeys(password);
			driver.findElement(By.id("credential-submit")).click();
			Thread.sleep(4000);
			credentialCreated = true;
		} catch(Exception e) {
			e.printStackTrace();
		}

		try {
			driver.findElement(By.id("new-credential")).click();
			Thread.sleep(4000);
			driver.findElement(By.id("credential-url")).sendKeys("http://www.gmail.com");
			driver.findElement(By.id("credential-username")).sendKeys(username);
			driver.findElement(By.id("credential-password")).sendKeys(password);
			driver.findElement(By.id("credential-submit")).click();
			Thread.sleep(4000);
		} catch(Exception e) {
			e.printStackTrace();
		}

		// delete a credential
		Thread.sleep(4000);
		boolean credentialDeleted = false;
		WebElement notesTable = driver.findElement(By.id("credentialTable"));
		List<WebElement> noteLink = notesTable.findElements(By.tagName("a"));
		for (WebElement deleteNoteButton : noteLink) {
			deleteNoteButton.click();
			credentialDeleted = true;
			break;
		}

		// edit a credential
		Thread.sleep(4000);
		notesTable = driver.findElement(By.id("credentialTable"));
		List<WebElement> noteList = notesTable.findElements(By.tagName("td"));
		boolean credentialEdited = false;
		for (WebElement row : noteList) {
			WebElement editButton = null;
			editButton = row.findElement(By.tagName("button"));
			editButton.click();
			if (!ObjectUtils.isEmpty(editButton)) {
				Thread.sleep(4000);
				driver.findElement(By.id("credential-url")).sendKeys("http://www.google.com");
				driver.findElement(By.id("credential-username")).sendKeys(username);
				driver.findElement(By.id("credential-password")).sendKeys(username);
				driver.findElement(By.id("credential-submit")).click();
				credentialEdited = true;
				Assertions.assertEquals("Home", driver.getTitle());
				break;
			}
		}
		Assertions.assertTrue(credentialCreated);
		Assertions.assertTrue(credentialDeleted);
		Assertions.assertTrue(credentialEdited);
	}

}
