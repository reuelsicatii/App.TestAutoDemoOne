package webApp.AppName;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.GherkinKeyword;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import helper.webAppContextDriver;
import helper.webAppHelper;
import io.cucumber.core.gherkin.Feature;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.BeforeStep;
import io.cucumber.java.Scenario;

public class webAppHook extends webAppHelper {

	// Properties
	// ==========================================

	private String DestFile, SrcImage;
	private File SrcFile;

	private static ExtentTest featureExtentTest;
	private ExtentTest scenarioExtentTest;

	// Declare Driver Instance
	// ==========================================
	private webAppContextDriver context;

	public webAppHook(webAppContextDriver context) {
		super();
		this.context = context;
	}

	private static ExtentSparkReporter extentSparkReporter;
	private static ExtentReports extentReports = new ExtentReports();

	@BeforeAll
	public static void beforeALl() throws ClassNotFoundException {
		System.out.println("Im in BeforeAll - TestSuite Level - Thread ID" + Thread.currentThread().getId());

		// Define Extent Report
		// ====================================================
		/*
		 * extentSparkReporter = new ExtentSparkReporter( System.getProperty("user.dir")
		 * + "/reports/extentReport/" + yyMMdd + "/" + HHmmss + ".html");
		 */

		// Define Extent Report XAMPP htdocs Folder - Image not resolving
		// ==============================================================================
		extentSparkReporter = new ExtentSparkReporter("C:/xampp/htdocs/AutomationProject/reports/webApp"
				+ new SimpleDateFormat("_yyMMdd_HHmmss").format(new Date()) + ".html");

		extentReports.attachReporter(extentSparkReporter);

	}

	@Before
	public void before(Scenario scenario) throws ClassNotFoundException {
		System.out.println("Im in Before - Scenario Level - Thread ID" + Thread.currentThread().getId());
		
		context.setScenario(scenario);

		// Set SoftAssert
		context.setSoftAssert();

		// Set Feature Name
		featureExtentTest = extentReports
				.createTest(new GherkinKeyword("Feature"),
						"Feature Name: " + scenario.getSourceTagNames().toArray()[0].toString().replace("@", "") + " - "
								+ scenario.getName() + " - " + scenario.getLine(),
						" Scenario Name: " + scenario.getName());

		// Set Test Scenario and Case Name
		scenarioExtentTest = featureExtentTest.createNode(new GherkinKeyword("Scenario"),
				"TestCase ID: " + scenario.getLine(), scenario.getId());
		context.setExtentTestScenario(scenarioExtentTest);

	}

	@BeforeStep
	public void beforeStep() throws IOException, ClassNotFoundException {
		System.out.println("Im in BeforeStep - Step Level - Thread ID" + Thread.currentThread().getId());

	}

	@AfterStep
	public void afterStep(Scenario scenario) throws IOException {
		System.out.println("Im in AfterStep - Step Level - Thread ID" + Thread.currentThread().getId());

		try {

			// XAMPP Symbolic Link - Image not resolving
			// ====================================================
			/*
			 * DestFile = System.getProperty("user.dir") + "/reports/screenshots/" + yyMMdd
			 * + "/" + HHmmss +
			 * scenario.getSourceTagNames().toArray()[0].toString().replace("@", "") + "_" +
			 * new SimpleDateFormat("_yyMMdd_HHmmss").format(new Date()) + ".png";
			 * 
			 * SrcImage = "/reports/screenshots/" + yyMMdd + "/" + HHmmss +
			 * scenario.getSourceTagNames().toArray()[0].toString().replace("@", "") + "_" +
			 * new SimpleDateFormat("_yyMMdd_HHmmss").format(new Date()) + ".png";
			 */

			// XAMPP htdocs Folder - Image not resolving
			// ====================================================
			DestFile = "C:/xampp/htdocs/AutomationProject/screenshots/"
					+ scenario.getSourceTagNames().toArray()[0].toString().replace("@", "") + "_"
					+ new SimpleDateFormat("_yyMMdd_HHmmss").format(new Date()) + ".png";

			SrcImage = "/AutomationProject/screenshots/"
					+ scenario.getSourceTagNames().toArray()[0].toString().replace("@", "") + "_"
					+ new SimpleDateFormat("_yyMMdd_HHmmss").format(new Date()) + ".png";

			SrcFile = ((TakesScreenshot) context.getDriver()).getScreenshotAs(OutputType.FILE);

			// Generating and Copying Screenshot to DestFile
			FileUtils.copyFile(SrcFile, new File(DestFile));

			// Attaching screenshot to Cucumber Report
			context.getScenario().attach(FileUtils.readFileToByteArray(SrcFile), "image/png",
					context.getScenario().getStatus().toString());

			// Attached Screenshot to Extent Report
			context.getExtentTestScenario().createNode(" ======================================== ")
					.info("Captured Screenshot: ", MediaEntityBuilder.createScreenCaptureFromPath(SrcImage).build());

		} catch (Exception e) {
			// Extent Report
			context.getExtentTestScenario().createNode(" ======================================== ")
					.warning(e.getMessage());
		}

	}

	@After
	public void after() throws IOException {
		System.out.println("Im in After - Scenario Level - Thread ID" + Thread.currentThread().getId());

		context.getDriver().quit();
	}

	@AfterAll
	public static void afterAll() {
		System.out.println("Im in After - TestSuite Level - Thread ID" + Thread.currentThread().getId());
		
		extentReports.flush();

	}

}
