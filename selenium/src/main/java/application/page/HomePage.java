package application.page;

import org.openqa.selenium.By;

import application.AbstractPage;

/**
 * Home page class for methods and elements
 *
 * @author srdjan
 */
public class HomePage extends AbstractPage {

  private final String PATH = "en/";
  private final By PAGE_IDENTIFIER = By.cssSelector("[id='___gatsby']");

  @Override
  public String getPath() {
    return PATH;
  }

  @Override
  public By getPageIdentifier() {
    return PAGE_IDENTIFIER;
  }
}
