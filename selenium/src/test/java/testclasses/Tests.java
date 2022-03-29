package testclasses;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import application.components.panel.CreatePanel;
import application.components.panel.LogInPanel;
import application.page.CreatePollPage;
import application.page.DashboardPage;
import application.page.HomePage;
import base.AbstractTest;

/**
 * Test class
 *
 * @author srdjan
 */
public class Tests extends AbstractTest {

  private HomePage homePage;

  @BeforeMethod
  public void openPage() {
    homePage = new HomePage();
    homePage.open();
    Assert.assertTrue(homePage.isOpened(), "Page is opened!");
  }

  @Test(description = "login and create poll, then vote.")
  public void createPollAndVote() {
    LogInPanel logInPanel = homePage.getHeader().clickLogInButton();
    Assert.assertTrue(logInPanel.setOrganizerUserName(), "Username is set.");
    Assert.assertTrue(logInPanel.setOrganizerPassword(), "Password is set.");
    Assert.assertTrue(logInPanel.clickSignInButton(), "Sign in button is clicked.");

    DashboardPage dashboardPage = new DashboardPage();
    Assert.assertTrue(dashboardPage.isLoaded(), "Dashboard page is loaded.");

    CreatePanel createPanel = dashboardPage.getHeader().clickCreateButton();
    Assert.assertTrue(createPanel.clickCreateGroupPoll(), "Create group poll is clicked.");
    CreatePollPage createPollPage = new CreatePollPage();
    Assert.assertTrue(createPollPage.isLoaded(), "Create poll page is loaded.");
  }
}
