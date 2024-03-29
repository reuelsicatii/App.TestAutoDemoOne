package helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.cucumber.java.Scenario;

public class webAppHelper {

	public static Properties properties;

	public RemoteWebDriver initializeBrowser(String browserName) throws MalformedURLException {
		RemoteWebDriver driver = null;
		DesiredCapabilities dc = new DesiredCapabilities();
		ChromeOptions chromeOptions = new ChromeOptions();

		if (GetPropertValue("data/TestProperties.xml", "webAppGrid").equalsIgnoreCase("Browser")) {
			if (browserName.equalsIgnoreCase("chrome")) {
				dc.setBrowserName("chrome");

			} else if (browserName.equalsIgnoreCase("firefox")) {
				dc.setBrowserName("firefox");

			} else if (browserName.equalsIgnoreCase("edge")) {
				dc.setBrowserName("MicrosoftEdge");

			} else if (browserName.equalsIgnoreCase("oepra")) {
				dc.setBrowserName("opera");

			} else if (browserName.equalsIgnoreCase("ie")) {
				dc.setBrowserName("ie");
			}

			driver = new RemoteWebDriver(new URL("http://localhost:4444"), dc);

		} else if (GetPropertValue("data/TestProperties.xml", "webAppGrid").equalsIgnoreCase("BrowserStack")) {

			// Deprecated Capabilities
			// =============================================
			/*
			 * dc.setCapability("os", GetPropertValue("Data/TestProperties.xml", "os"));
			 * dc.setCapability("os_version", GetPropertValue("Data/TestProperties.xml",
			 * "os_version")); dc.setCapability("browser", browserName);
			 * dc.setCapability("browser_version",
			 * GetPropertValue("Data/TestProperties.xml", "browser_version"));
			 * dc.setCapability("name", "BrowserStack - Grid AutoDemo");
			 */

			// Setting Capabilities
			// =============================================
			chromeOptions.setPlatformName("Windows 10");
			chromeOptions.setBrowserVersion("92");
			HashMap<String, Object> cloudOptions = new HashMap<>();
			cloudOptions.put("build", "myTestBuild");
			cloudOptions.put("name", "myTestName");
			chromeOptions.setCapability("cloud:options", cloudOptions);

			driver = new RemoteWebDriver(
					new URL("https://licenses_l5aIeD:QYTmGxxvTJ9bxLAERqYU@hub-cloud.browserstack.com/wd/hub"),
					chromeOptions);

		}

		return driver;
	}

	public WebDriverWait initializeBrowserWait(RemoteWebDriver driver) throws MalformedURLException {

		return new WebDriverWait(driver, Duration.ofSeconds(30));

	}

	public void getScreenshot(RemoteWebDriver driver, Scenario scenario) throws IOException {

		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_hhmmssa");
		String timeStamp = sdf.format(date);
		String DestFile = System.getProperty("user.dir") + "\\screenshots\\" + scenario.getName() + "_" + timeStamp
				+ ".png";

		File SrcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(SrcFile, new File(DestFile));

	}

	public static String GetPropertValue(String Path, String Key) {

		properties = new Properties();
		FileInputStream fis;
		try {
			fis = new FileInputStream(Path);
			properties.loadFromXML(fis);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return properties.getProperty(Key);

	}
}
