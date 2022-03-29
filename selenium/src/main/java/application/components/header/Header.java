package application.components.header;

import org.openqa.selenium.By;

import application.components.panel.CreatePanel;
import application.components.panel.LogInPanel;
import application.support.SeleniumSupport;

/**
 * Use this class for header elements and methods
 *
 * @author srdjan
 */
public class Header extends SeleniumSupport {

  private final By LOGIN_BUTTON = By.cssSelector("[data-testid='at-text-button']");
  private final By CREATE_POLL_BUTTON = By.cssSelector("[data-testid='at-icon']");

  private LogInPanel logInPanel;
  private CreatePanel createPanel;

  public Header() {
    this.logInPanel = new LogInPanel();
    this.createPanel = new CreatePanel();
  }

  public LogInPanel clickLogInButton() {
    click(findElement(LOGIN_BUTTON));
    return logInPanel;
  }

  public CreatePanel clickCreateButton() {
    click(findElement(CREATE_POLL_BUTTON));
    return createPanel;
  }
}
