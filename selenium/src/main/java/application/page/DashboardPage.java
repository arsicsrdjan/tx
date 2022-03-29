package application.page;

import org.openqa.selenium.By;

import application.AbstractPage;
import application.components.header.Header;

/**
 * Class for dashboard page methods and elements
 *
 * @author srdjan
 */
public class DashboardPage extends AbstractPage {

  private final String PATH = "dashboard";
  private final By PAGE_IDENTIFIER = By.cssSelector("[id='root']");

  private Header header;

  public DashboardPage() {
    this.header = new Header();
  }

  @Override
  public String getPath() {
    return PATH;
  }

  @Override
  public By getPageIdentifier() {
    return PAGE_IDENTIFIER;
  }
}
