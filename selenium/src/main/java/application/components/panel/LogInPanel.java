package application.components.panel;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import application.support.SeleniumSupport;

/**
 * Class for login elements and methods
 *
 * @author srdjan
 */
public class LogInPanel extends SeleniumSupport {

  private final By USERNAME = By.id("username");
  private final By PASSWORD = By.id("password");
  private final By LOG_IN_BUTTON = By.cssSelector("button[class*='Button--login-signup']");

  public boolean setOrganizerUserName() {
    WebElement username = findElement(USERNAME);
    if (username != null) {
      return setInputValue(username, "srdjan.arsic@yahoo.com");
    }
    return false;
  }

  public boolean setOrganizerPassword() {
    WebElement password = findElement(PASSWORD);
    if (password != null) {
      return setInputValue(password, "srdjanDoodle!");
    }
    return false;
  }

  public boolean clickSignInButton() {
    return click(findElement(LOG_IN_BUTTON));
  }
}
