package mobileApp.AppName;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.GherkinKeyword;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import helper.mobileAppContextDriver;
import helper.webAppHelper;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.BeforeStep;
import io.cucumber.java.Scenario;

public class mobileAppHook extends webAppHelper {

	// Declare Driver Instance
	// ==========================================
	private mobileAppContextDriver context;
	private String DestFile;
	private File SrcFile;

	private static ExtentTest featureExtentTest;
	private ExtentTest scenarioExtentTest;

	public mobileAppHook(mobileAppContextDriver context) {
		super();
		this.context = context;
	}

	private static ExtentSparkReporter extentSparkReporter;
	private static ExtentReports extentReports = new ExtentReports();

	@BeforeAll
	public static void beforeALl() throws ClassNotFoundException {

		System.out.println("Im in a BeforeAll Scenario");
		System.out.println("BeforeScenario - Thread ID" + Thread.currentThread().getId());

		// Define Extent Report
		extentSparkReporter = new ExtentSparkReporter(System.getProperty("user.dir") + "/reports/extentReport"
				+ new SimpleDateFormat("_yyMMdd_HHmmss").format(new Date()) + ".html");

		extentReports.attachReporter(extentSparkReporter);

	}

	@Before
	public void setup(Scenario scenario) throws ClassNotFoundException {
		System.out.println("Im in a BeforeScenario");
		System.out.println("BeforeScenario - Thread ID" + Thread.currentThread().getId());
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
		System.out.println("Im in a Before StepDefination");

	}

	@AfterStep
	public void afterStep(Scenario scenario) throws IOException {
		System.out.println("Im in a AfterStep StepDefination");

		try {

			DestFile = System.getProperty("user.dir") + "\\screenshots\\"
					+ scenario.getSourceTagNames().toArray()[0].toString().replace("@", "") + "_"
					+ new SimpleDateFormat("_yyMMdd_HHmmss").format(new Date()) + ".png";

			SrcFile = ((TakesScreenshot) context.getAndroidDriver()).getScreenshotAs(OutputType.FILE);

			// Generating and Copying Screenshot to DestFile
			FileUtils.copyFile(SrcFile, new File(DestFile));

			// Attaching screenshot to Cucumber Report
			context.getScenario().attach(FileUtils.readFileToByteArray(SrcFile), "image/png",
					context.getScenario().getStatus().toString());

			// Attached Screenshot to Extent Report
			context.getExtentTestScenario().createNode(" ======================================== ")
					.info("Captured Screenshot: ", MediaEntityBuilder.createScreenCaptureFromPath(DestFile).build());

		} catch (Exception e) {
			// Extent Report
			context.getExtentTestScenario().createNode(" ======================================== ")
					.warning(e.getMessage());
		}

	}

	@After
	public void tearDown() throws IOException {
		System.out.println("Im in a AfterScenario");
		System.out.println("AfterScenario - Thread ID" + Thread.currentThread().getId());

		context.getAndroidDriver().quit();
	}

	@AfterAll
	public static void afterAll() {

		System.out.println("Im in a After Scenario");
		extentReports.flush();

	}

}
