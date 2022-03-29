package base;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import application.WebDriverFactory;

/**
 * Abstract test class for common methods
 *
 * @author srdjan
 */
public abstract class AbstractTest {

  @BeforeClass
  public void commonSetUp() {
  }

  @AfterClass
  public void commonTearDown() {
    WebDriverFactory.getInstance().quitDriver();
  }
}
