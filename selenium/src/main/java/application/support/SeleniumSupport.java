package application.support;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.MoveTargetOutOfBoundsException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import application.WebDriverFactory;

/**
 * Selenium support class
 *
 * @author srdjan
 */
public class SeleniumSupport {
  protected static final long LONG_TIME = 30;
  protected static final long SHORT_TIME = 10;
  protected static final long DURATION_MILLISECONDS = 100;
  private static final Logger LOG = LogManager.getLogger(SeleniumSupport.class.getName());

  protected WebDriver getDriver() {
    return WebDriverFactory.getInstance().getDriver("chrome");
  }

  protected WebDriverWait getWait(long timeOutInSeconds) {
    return new WebDriverWait(getDriver(), timeOutInSeconds, 50);
  }

  /**
   * Returns the {@link WebElement} found by given locator. Keeps retrying for 5 seconds.
   *
   * @param locator the {@link By} locator
   * @return the {@link WebElement} found or {@code null} when not found
   */
  public WebElement findElement(By locator) {
    WebElement element;
    element = locateElement(locator).orElse(null);
    if (element != null) {
      LOG.debug("Found element {} with selector {}", element, locator);
    } else {
      LOG.info("Failed to find element with selector {}", locator);
    }
    return element;
  }

  /**
   * Returns the {@link WebElement} found by given locator.
   *
   * @param locator the {@link By} locator
   * @return the {@link WebElement} found or {@code Optional#empty()} when not located
   */
  public Optional<WebElement> locateElement(By locator) {
    WebElement element;
    try {
      element = getDriver().findElement(locator);
    } catch (Exception e) {
      element = null;
    }
    LOG.trace("Found {} with {}", element, locator);
    return Optional.ofNullable(element);
  }

  /**
   * Clear and send keys to input element on the page.
   *
   * @param element Element to clear and sendKeys on the page.
   * @param keysToSend Text to fill in the element.
   * @return {@code true} if value set, {@code false} otherwise
   */
  public boolean setInputValue(WebElement element, String keysToSend) {
    LOG.debug("Setting input value '{}' for {}", keysToSend, element);
    boolean setted = false;
    if (this.longWaitTillVisible(element)) {
      scrollToElement(element);
      clearText(element);
      element.sendKeys(keysToSend);
      setted = true;
    }
    return setted;
  }

  /**
   * If there's a text, then clear else click on it to get the focus
   *
   * @param webElement {@link WebElement} as locator
   */
  protected void clearText(WebElement webElement) {
    webElement.clear();
    webElement.click();
  }

  /**
   * Scrolls the scrollbar to the required element to view and perform the action
   *
   * @param webElement the {@link WebElement} to scroll to on the page
   */
  public void scrollToElement(WebElement webElement) {
    WebElement element = waitUntilVisibleByElement(webElement);
    LOG.debug("Scrolling to {}", element);
    Actions scrollElement = new Actions(getDriver());
    try {
      scrollElement.moveToElement(element).perform();
    } catch (MoveTargetOutOfBoundsException e) {
      LOG.debug("Exception scrolling to element {}: {}", element, e.getMessage());
    } catch (JavascriptException e) {
      LOG.warn("Unable to scroll to element {}", element, e);
    }
  }

  /**
   * Returns the {@link WebElement} found by given element. Keeps retrying for 30 seconds in every 100 milliseconds
   *
   * @param webElement the {@link WebElement} element
   * @return the {@link WebElement} found or {@link Exception} when not found
   */
  public WebElement waitUntilVisibleByElement(WebElement webElement) {
    WebElement element = null;
    try {
      Wait<WebDriver> wait = fluentWait();
      element = wait.until(ExpectedConditions.visibilityOf(webElement));
    } catch (ElementNotVisibleException t) {
      LOG.error("waitUntilVisibleByLocator fail", t);
    }
    return element;
  }

  /**
   * Returns the wait time
   *
   * @return {@link FluentWait} wait
   */
  public Wait<WebDriver> fluentWait() {
    return new FluentWait<WebDriver>(getDriver()).withTimeout(Duration.ofSeconds(LONG_TIME)).pollingEvery(Duration.ofMillis(DURATION_MILLISECONDS))
        .ignoring(StaleElementReferenceException.class).ignoring(NoSuchElementException.class);
  }

  /**
   * Waits long time till given element is visible.
   *
   * @param element the element to check for visibility on page
   * @return {@code true} if the element is visible, {@code false} otherwise
   * @see #waitTillVisible(WebElement, WebDriverWait)
   */
  public boolean longWaitTillVisible(WebElement element) {
    return waitTillVisible(element, getLongWait());
  }

  /**
   * Get the long wait to use.
   *
   * @return webdriverwait longWait
   */
  public WebDriverWait getLongWait() {
    return getWait(LONG_TIME);
  }

  /**
   * Checking that an element, known to be present on the DOM of a page, is visible. Visibility means that the element is not only displayed but also has a
   * height and width that is greater than 0. Can also be used to see if element is displayed.
   *
   * @param element WebElement to check for visibility on page
   * @param waiter the {@link WebDriverWait} to use for waiting
   * @return {@code true} if the element is visible, {@code false} otherwise
   */
  public boolean waitTillVisible(WebElement element, WebDriverWait waiter) {
    LOG.trace("Wait for {} to be visible", element);
    boolean visible = false;
    if (element != null) {
      try {
        waiter.until(ExpectedConditions.visibilityOf(element));
        visible = true;
      } catch (TimeoutException e) {
        LOG.warn("Timeout waiting for visibility of element {}", element);
      }
    }
    return visible;
  }

  public boolean shortWaitTillVisible(By locator) {
    return doFindElementWaitTillVisible(locator, getShortWait()) != null;
  }

  protected WebElement doFindElementWaitTillVisible(By locator, WebDriverWait waiter) {
    try {
      LOG.trace("Finding element with locator {} with waiter {}", locator, waiter);
      return waiter.until(ExpectedConditions.visibilityOfElementLocated(locator));
    } catch (TimeoutException e) {
      LOG.info("Timeout waiting for visibility of element by locator {}", locator);
      return null;
    }
  }

  /**
   * Get the short wait to use.
   *
   * @return webdriverwait shortWait
   */
  public WebDriverWait getShortWait() {
    return getWait(SHORT_TIME);
  }

  /**
   * Wait for element to be clickable. Return <code>true</code> if so.
   *
   * @param element
   * @return <code>true</code> if element is clickable
   */
  private boolean doWaitTillClickable(WebElement element) {
    LOG.trace("Waiting for (visible) element {} to be clickable", element);
    try {
      getLongWait().until(ExpectedConditions.elementToBeClickable(element));
      return true;
    } catch (TimeoutException e) {
      LOG.warn("Timeout waiting for element to be clickables {}", element);
      return false;
    }
  }

  private void doClick(WebElement element, int tries) {
    waitUntilVisibleByElement(element);
    boolean scrolled = false;
    try {
      doWaitTillClickable(element);
      if (!element.isDisplayed()) {
        scrollToElement(element);
        scrolled = true;
      }
      doWaitTillClickable(element);
      element.click();
    } catch (ElementClickInterceptedException e) {
      if (tries > 3) {
        throw new IllegalStateException(String.format("Element %s can not be clicked after %s tries", element, tries), e);
      } else {
        LOG.trace("Trying again after click on {} was intercepted: {}", element, e.getMessage());
        if (!scrolled && tries == 1) {
          // element can be under header which makes it unclickable, so scroll 1st to top and then to element
          scrollToTop();
          scrollToElement(element);
        }
        doClick(element, tries + 1);
      }
    }
  }

  /**
   * Scrolls the (browser) window to the top.
   */
  public void scrollToTop() {
    JavascriptExecutor js = (JavascriptExecutor) getDriver();
    js.executeScript("window.scrollTo(0,0);");
  }

  /**
   * Wait for the visibility of element and then click on the element.
   *
   * @param element to click on the page.
   * @return {@code true} if element has been clicked without an exception, {@code false} otherwise
   */
  public boolean click(WebElement element) {
    if (element != null) {
      doClick(element, 1);
      return true;
    } else {
      throw new IllegalStateException("Element is null so can not be clicked");
    }
  }

  /**
   * Checking that an element is either invisible or not present on the page.
   *
   * @param locator the Selenium locator of the element to find
   * @return {@code true} if the element is not displayed or the element doesn't exist or stale element, {@code false} otherwise
   */
  public boolean waitTillInvisible(By locator) {
    try {
      return getLongWait().until(ExpectedConditions.invisibilityOfElementLocated(locator));
    } catch (TimeoutException e) {
      LOG.info("Timeout waiting for invisibility of element {}", locator);
      return false;
    }
  }

  /**
   * Returns the {@link WebElement}s found by given locator. Keeps retrying for 5 seconds.
   *
   * @param locator the {@link By} locator
   * @return the {@link WebElement}s found, can be empty if nothing was found
   */
  public List<WebElement> findElements(By locator) {
    List<WebElement> elements = Collections.emptyList();
    int attempts = 0;
    while (attempts < 50) {
      attempts++;
      elements = getDriver().findElements(locator);
      if (!elements.isEmpty()) {
        break;
      }
      waitExplicitly(DURATION_MILLISECONDS);
    }
    if (elements.isEmpty()) {
      LOG.info("Failed to find elements for locator {} after {} attempts", locator, attempts);
    } else {
      LOG.trace("Found these elements for {}: {}", locator, elements);
    }
    return elements;
  }

  /**
   * Wait method in case this is needed, should not be used.
   *
   * @param millis
   */
  public void waitExplicitly(long millis) {
    try {
      LOG.trace("Waiting explicitly for {}ms", millis);
      Thread.sleep(millis);
    } catch (final InterruptedException e) {
      Thread.currentThread().interrupt();
      LOG.info("Explicit wait interrupted", e);
    }
  }

  /**
   * Checking that an element for given selector, known to be present on the DOM of a page, is visible. Visibility means that the element is not only displayed
   * but also has a height and width that is greater than 0. Can also be used to see if element is displayed.
   *
   * @param locator Locator of element to check for visibility on page
   * @return {@code true} if the concerning element is visible, {@code false} otherwise
   */
  public boolean longWaitTillVisible(By locator) {
    return findElementWaitTillVisible(locator) != null;
  }

  /**
   * Checking that an element is present on the page and visible. Visibility means that the element is not only displayed but also has a height and width that
   * is greater than 0.
   *
   * @param locator the Selenium locator of the element to find
   * @return the {@link WebElement} found or {@code null} if not present/found after timeout
   */
  public WebElement findElementWaitTillVisible(By locator) {
    return doFindElementWaitTillVisible(locator, getLongWait());
  }
}
