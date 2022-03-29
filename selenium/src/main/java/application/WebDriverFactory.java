package application;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * Web driver factory class
 *
 * @author srdjan
 */
public class WebDriverFactory {

  private static final WebDriverFactory instance = new WebDriverFactory();

  private WebDriverFactory() {}

  public static WebDriverFactory getInstance() {
    return instance;
  }

  private static ThreadLocal<WebDriver> threadedDriver = new ThreadLocal<WebDriver>();

  public WebDriver getDriver(String browser) {
    WebDriver driver = null;
    setDriver(browser);
    if (threadedDriver.get() == null) {
      driver = new ChromeDriver();
      threadedDriver.set(driver);
      threadedDriver.get().manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
      threadedDriver.get().manage().window().maximize();
    }
    return threadedDriver.get();
  }

  public void quitDriver() {
    threadedDriver.get().quit();
    threadedDriver.set(null);
  }

  private void setDriver(String browser) {
    String driverPath = "";
    String os = System.getProperty("os.name").toLowerCase().substring(0, 3);
    String directory = System.getProperty("user.dir") + "/drivers/";
    String driverKey = "";
    String driverValue = "";

    if (browser.equalsIgnoreCase("firefox")) {
      driverKey = "webdriver.gecko.driver";
      driverValue = "geckodriver";
    } else if (browser.equalsIgnoreCase("chrome")) {
      driverKey = "webdriver.chrome.driver";
      driverValue = "chromedriver";
    } else if (browser.equalsIgnoreCase("ie")) {
      driverKey = "webdriver.ie.driver";
      driverValue = "IEDriverServer";
    } else {
      System.out.println("Browser type not supported");
    }
    driverPath = directory + driverValue + (os.equals("win") ? ".exe" : "");
    System.setProperty(driverKey, driverPath);
  }
}
