package application.page;

import org.openqa.selenium.By;

import application.AbstractPage;

/**
 * Create poll page
 *
 * @author srdjan
 */

public class CreatePollPage extends AbstractPage {

  private final String PATH = "meeting/organize/groups";
  private final By PAGE_IDENTIFIER = By.cssSelector("[class='OrganizationForm']");

  @Override
  public String getPath() {
    return PATH;
  }

  @Override
  public By getPageIdentifier() {
    return PAGE_IDENTIFIER;
  }
}
