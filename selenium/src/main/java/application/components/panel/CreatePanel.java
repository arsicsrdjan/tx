package application.components.panel;

import org.openqa.selenium.By;

import application.support.SeleniumSupport;

/**
 * Class for create panel methods and elements
 *
 * @author srdjan
 */
public class CreatePanel extends SeleniumSupport {

  private final By GROUP_POLL = By.xpath("//p[text()='Create group poll']");

  public boolean clickCreateGroupPoll() {
    return click(findElement(GROUP_POLL));
  }
}
