package application;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;

import application.components.header.Header;
import application.support.SeleniumSupport;

/**
 * Abstract page for common page elements and methods
 *
 * @author srdjan
 */
public abstract class AbstractPage extends SeleniumSupport {

  private static final Logger LOG = LogManager.getLogger(SeleniumSupport.class.getName());
  private  final By ACCEPT_ALL = By.cssSelector("[id='onetrust-accept-btn-handler']");
  private String siteBaseUrl = "https://doodle.com/";

  private Header header;

  public AbstractPage() {
    this.header = new Header();
  }

  public Header getHeader() {
    return header;
  }

  /**
   * Opens page in the browser.
   *
   * @return <code>true</code> if the page was opened successfully, <code>false</code> otherwise
   */
  public void open() {
    String pageUrl = getPageUrl();
    LOG.debug("Opening page on URL '{}'", pageUrl);
    getDriver().get(pageUrl);
  }

  /**
   * Returns the URL to {@code this} page.
   *
   * @return the URL to {@code this} page.
   */
  public String getPageUrl() {
    return siteBaseUrl + getPath();
  }

  /**
   * Returns the (base) path to {@code this} page.
   *
   * @return the path to {@code this} page
   */
  public abstract String getPath();

  /**
   * Checks if {@code this} page is opened in the browser.
   *
   * @return {@code true} if {@code this} page is opened, {@code false} otherwise
   */
  public boolean isOpened() {
    String pageUrl = getPageUrl();
    String currentUrl = getDriver().getCurrentUrl();
    LOG.info("Checking if page with URL '{}' is currently opened (i.e. same as '{}'", pageUrl, currentUrl);
    if (shortWaitTillVisible(ACCEPT_ALL)) {
      findElement(ACCEPT_ALL).click();
    }
    return currentUrl.equals(pageUrl);
  }

  /**
   * returns the pageIdentifier WebElement (unique identifier of the page).
   *
   * @return
   */
  public abstract By getPageIdentifier();

  /**
   * Checks if {@code this} page is loaded in the browser.
   *
   * @return {@code true} if {@code this} page is loaded, {@code false} otherwise
   */
  public boolean isLoaded() {
    return longWaitTillVisible(getPageIdentifier());
  }
}
