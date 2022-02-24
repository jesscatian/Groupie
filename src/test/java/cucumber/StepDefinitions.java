package cucumber;

import csci310.models.*;
import csci310.servlets.SendProposalServlet;
import csci310.utilities.*;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.Before;

import io.cucumber.java.en_old.Ac;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.*;
import java.lang.reflect.Array;
import java.time.DateTimeException;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.*;


/**
 * Step definitions for Cucumber tests.
 */
public class StepDefinitions {
    private static final String ROOT_URL = "http://localhost:8080/";
    private final WebDriver driver = new ChromeDriver();

    private static String keywords = null;
    private static String startDate = null;
    private static String endDate = null;
    private static String countrycode = null;
    private static SendProposalServlet servlet = null;

    @Before()
    public void before() {
        User u = new User("asdf", "asdf");
        DatabaseManager.object().insertUser(u);
        User user9 = new User("user9", "9");
        DatabaseManager.object().insertUser(user9);
        User user10 = new User("user10", "10");
        DatabaseManager.object().insertUser(user10);
        servlet = new SendProposalServlet();
        DatabaseManager.object().close();
    }



    @Given("I am on the index page")
    public void i_am_on_the_index_page() {
        driver.get(ROOT_URL);
    }

    @When("I click the link {string}")
    public void i_click_the_link(String linkText) {
        driver.findElement(By.linkText(linkText)).click();
    }

    @Then("I should see header {string}")
    public void i_should_see_header(String header) {
        assertTrue(driver.findElement(By.cssSelector("h2")).getText().contains(header));
    }

    @Then("I should see text {string}")
    public void i_should_see_text(String text) {
        assertTrue(driver.getPageSource().contains(text));
    }

    @Given("I am on the signup page")
    public void iAmOnTheSignupPage() {
        driver.get(ROOT_URL + "signup.html");
    }

    @When("I fill out my credentials")
    public void iFillOutMyCredentials() {
        driver.findElement(By.cssSelector("#input-username")).sendKeys("asdf");
        driver.findElement(By.cssSelector("#input-password")).sendKeys("asdf");
        try {
            driver.findElement(By.cssSelector("#input-password-confirm")).sendKeys("asdf");
        } catch(NoSuchElementException e) {}
    }

    @And("I go to the login page")
    public void iGoToTheLoginPage() {
        driver.get(ROOT_URL + "login.html");
    }

    @Then("I should be taken to the dashboard")
    public void iShouldBeTakenToTheDashboard() {
        assertEquals(driver.getCurrentUrl(), ROOT_URL+"dashboard.html");
    }

    @Then("I should see errors in the username and password field")
    public void iShouldSeeErrorsUnderTheUsernameAndPasswordField() {
        assertTrue(driver.findElement(By.cssSelector("#input-username")).getAttribute("class").contains("invalid"));
        assertTrue(driver.findElement(By.cssSelector("#input-password")).getAttribute("class").contains("invalid"));
    }

    @Given("I am on the login page")
    public void iAmOnTheLoginPage() {
        driver.get(ROOT_URL + "login.html");
    }

    @And("I go to the signup page")
    public void iGoToTheSignupPage() {
        driver.get(ROOT_URL + "signup.html");
    }

    @Then("I should see an error at the bottom of the screen")
    public void iShouldSeeAnErrorAtTheBottomOfTheScreen() {
        assertFalse(driver.findElement(By.cssSelector(".error-msg")).getAttribute("class").contains("hidden"));
    }

    @And("I fill out the wrong username")
    public void iFillOutTheWrongUsername() {
        driver.findElement(By.cssSelector("#input-username")).sendKeys("o");
        driver.findElement(By.cssSelector("#input-password")).sendKeys("asdf");
    }
    @And("I fill out the wrong password")
    public void iFillOutTheWrongPassword() {
        driver.findElement(By.cssSelector("#input-username")).clear();
        driver.findElement(By.cssSelector("#input-username")).sendKeys("asdf");
        driver.findElement(By.cssSelector("#input-password")).clear();
        driver.findElement(By.cssSelector("#input-password")).sendKeys("o");
    }

    @And("I fill out new credentials")
    public void iFillOutNewCredentials() {
        driver.findElement(By.cssSelector("#input-username")).sendKeys("asdf" + Integer.toString( (int)Math.random()*100000));
        driver.findElement(By.cssSelector("#input-password")).sendKeys("o");
    }

    @Then("I should be taken to the login page")
    public void iShouldBeTakenToTheLoginPage() {
        assertEquals(ROOT_URL + "login.html", driver.getCurrentUrl());
    }

    @And("I click on the log in button")
    public void iClickOnTheLogInButton() {
        driver.findElement(By.cssSelector(".on-enter-target")).click();
        WebDriverWait wait = new WebDriverWait(driver, 50);
        wait.until(ExpectedConditions.or(ExpectedConditions.presenceOfElementLocated(By.id("profile")), ExpectedConditions.visibilityOfElementLocated(By.className("error-msg"))));
    }

    @When("I click on the create account button")
    public void iClickOnTheCreateAccountButton() {
        driver.findElement(By.cssSelector("#create-account")).click();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {}
    }

    @And("I change the confirm password field to not match my password")
    public void iChangeTheConfirmPasswordFieldToNotMatchMyPassword() {
        User u = new User("asdf", "asdf");
        DatabaseManager.object().deleteUser(u);
        driver.findElement(By.cssSelector("#input-password-confirm")).sendKeys("12345");
    }

    @And("I click the cancel button")
    public void iClickTheCancelButton() {
        driver.findElement(By.cssSelector("#cancel")).click();
    }

    @Given("I am logged in")
    public void iAmLoggedIn() {
        iAmOnTheLoginPage();
        iFillOutMyCredentials();
        iClickOnTheLogInButton();
        iShouldBeTakenToTheDashboard();
    }

    @And("I click search")
    public void iClickSearch() {
        driver.findElement(By.id("search-button")).click();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {

        }
    }

    @And("I enter keywords")
    public void iEnterKeywords() {
        keywords = "lit";
        driver.findElement(By.id("keywords")).sendKeys(keywords);
    }

    @Then("After timeout I should see the alert Signed out")
    public void iShouldSeeTheAlertSignedOut() throws InterruptedException {
        Thread.sleep(61000);
        Alert alert = driver.switchTo().alert();
        String alertMessage= driver.switchTo().alert().getText();
        assertEquals(alertMessage, "You have been safely logged out due to being inactive for more than 60 seconds.");
    }

    @Then("I should see results matching my query")
    public void iShouldSeeResultsMatchingMyQuery() {
        //replicate API call to match against results
        String urlString = databaseConfig.rootUrl + "&size=10"
                + (keywords != null ? "&keyword=" + keywords : "")
                + (startDate != null && endDate != null ? "&localStartDateTime=" + startDate + "T00:00:00" + "," + endDate + "T00:00:00": "")
                + (countrycode != null ? "&countryCode=" + countrycode.toUpperCase() : "");
        String apiResponse;

        try {
            apiResponse = HelperFunctions.get(urlString);
        } catch (IOException e) {
            fail("Error in API response");
            return;
        }

        RawResult rawResult = HelperFunctions.shared().fromJson(apiResponse,RawResult.class);
        Events events;

        try {
            events = new Events(rawResult);
        } catch (Exception e) {
            fail("Couldn't create events list");
            return;
        }

        int eventIndex = 0;
        List<WebElement> l = driver.findElements(By.cssSelector(".result"));
        Map<Long, String> ordinalNumbers = new HashMap<>(42);
        ordinalNumbers.put(1L, "1st");
        ordinalNumbers.put(2L, "2nd");
        ordinalNumbers.put(3L, "3rd");
        ordinalNumbers.put(21L, "21st");
        ordinalNumbers.put(22L, "22nd");
        ordinalNumbers.put(23L, "23rd");
        ordinalNumbers.put(31L, "31st");
        for (long d = 1; d <= 31; d++) {
            ordinalNumbers.putIfAbsent(d, "" + d + "th");
        }

        DateTimeFormatter dayOfMonthFormatter = new DateTimeFormatterBuilder()
                .appendPattern("MMMM ")
                .appendText(ChronoField.DAY_OF_MONTH, ordinalNumbers)
                .appendPattern(" yyyy")
                .toFormatter();
        for(WebElement w : l) {
            String date = w.findElement(By.cssSelector(".subtitle")).getAttribute("innerText");
            String title = w.findElement(By.cssSelector("a")).getAttribute("innerText");
            String url = w.findElement(By.cssSelector("a")).getAttribute("href");
            try {
                assertEquals(dayOfMonthFormatter.format(DateTimeFormatter.ofPattern("yyyy-MM-dd").parse(events.getEvents().get(eventIndex).getDate())).toUpperCase(), date);
            } catch (DateTimeException e) {
                fail("Could not parse date " + events.getEvents().get(eventIndex).getDate());
            }
            try {
                assertEquals(events.getEvents().get(eventIndex).getName(), title);
                assertEquals(events.getEvents().get(eventIndex).getUrl(), url);
            } catch (IndexOutOfBoundsException exp) {
                fail("Results longer than API returned list of events");
            }
            eventIndex++;
        }
        if(events.getEvents().size() == 0) {
            try {
                driver.findElement(By.cssSelector(".error"));
            } catch (NoSuchElementException exp) { fail("Could not see results empty message"); }
        }
    }

    @And("I specify a date range")
    public void iSpecifyADateRange() {
        startDate = "2021-11-10";
        endDate = "2021-12-25";
        driver.findElement(By.id("start-date")).sendKeys("11102021");
        driver.findElement(By.id("end-date")).sendKeys("12252021");
    }

    @And("I specify a location")
    public void iSpecifyALocation() {
        countrycode = "GB";
        driver.findElement(By.id("country")).sendKeys(countrycode);
    }

    @Given("I am on the signup page for the first time")
    public void iAmOnTheSignupPageForTheFirstTime() {
        User u = new User("asdf", "asdf");
        DatabaseManager.object().deleteUser(u);
        iAmOnTheSignupPage();
    }

    @And("I visit dashboard.html")
    public void iVisitDashboardHtml() {
        driver.get(ROOT_URL + "dashboard.html");
    }

    @And("I specify my location as North Korea")
    public void iSpecifyMyLocationAsNorthKorea() {
        driver.findElement(By.id("country")).sendKeys("KP");
    }

    @Then("I should see no results")
    public void iShouldSeeNoResults() {
        try {
            driver.findElement(By.cssSelector(".error"));
        } catch (NoSuchElementException exp) { fail("Could not see results empty message"); }
    }

    @And("I click profile")
    public void iClickProfile() {
        driver.findElement(By.cssSelector("#profile")).click();
    }

    @Then("I should be taken to the account page")
    public void iShouldBeTakenToTheAccountPage() {
        assertEquals(ROOT_URL + "account.html", driver.getCurrentUrl());
    }

    // pendinginvites.feature
    @Given("I am on the Pending Invites page")
    public void iAmOnThePendingInvitesPages() {
        iAmOnTheLoginPage();
        driver.findElement(By.cssSelector("#input-username")).sendKeys("test");
        driver.findElement(By.cssSelector("#input-password")).sendKeys("a");
        try {
            driver.findElement(By.cssSelector("#input-password-confirm")).sendKeys("a");
        } catch(NoSuchElementException e) {}
        iClickOnTheLogInButton();
        driver.get("http://localhost:8080/pendinginvites.html");
    }

    @When("I click the check mark")
    public void iClickTheCheckMark(){
        WebDriverWait wait = new WebDriverWait(driver, 50);
        WebElement Category_Body = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("check")));
        Category_Body.click();
    }

    @Then("I should see the alert Accepted Invite")
    public void iShouldSeeTheAlertAcceptedInvite(){
        assertEquals(driver.switchTo().alert().getText(), "Accepted Invite");
    }

    @When("I click the cross mark")
    public void iClickTheCrossMark(){
        WebDriverWait wait = new WebDriverWait(driver, 50);
        WebElement Category_Body = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("cross")));
        Category_Body.click();
    }

    @Then("I should see the alert Declined Invite")
    public void iShouldSeeTheAlertDeclinedInvite(){
        assertEquals(driver.switchTo().alert().getText(), "Declined Invite");
    }

    // proposalResponse.feature
    @Given("I am on the proposal response page")
    public void iAmOnTheProposalResponsePage() throws InterruptedException {
        iAmOnTheLoginPage();
        iFillOutMyCredentials();
        iClickOnTheLogInButton();
//        i_select_an_event();
//        i_click_submit();
//
//        i_click_submit();
        driver.get(ROOT_URL + "proposalResponse.html");
        WebDriverWait wait = new WebDriverWait(driver, 1);
    }

    @And("I click yes")
    public void iClickYes() {
        WebDriverWait wait = new WebDriverWait(driver, 1);
        driver.findElement(By.cssSelector("#yes00")).click();
    }

    @Then("the button corresponding to yes should be clicked")
    public void theButtonCorrespondingToYesShouldBeClicked() {
        assertTrue(driver.findElement(By.cssSelector("input[id*='yes00']")).isSelected());
    }

    @And("I click 1 in the excitement menu")
    public void iClick1InTheExcitementMenu() {
        WebDriverWait wait = new WebDriverWait(driver, 1);
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("00")));
        Select dropdown = new Select(driver.findElement(By.id("00")));
        dropdown.selectByVisibleText("1");

    }

    @Then("One should be selected for excitement")
    public void oneShouldBeSelectedForExcitement() {
        WebDriverWait wait = new WebDriverWait(driver, 1);
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("00")));
        Select dropdown = new Select(driver.findElement(By.id("00")));
        WebElement w = dropdown.getFirstSelectedOption();
        assertEquals(1, Integer.parseInt(w.getAttribute("value")));
    }

    // dashboard.feature
    @And("I click next")
    public void i_click_next() {
        driver.findElement(By.id("next-button")).click();
    }

    @Then("I should see a no events alert")
    public void i_should_see_a_no_events_alert() {
        assertEquals(driver.switchTo().alert().getText(), "Please add at least one event");
    }

    @And("I select an event")
    public void i_select_an_event() throws InterruptedException {
        driver.findElement(By.id("keywords")).sendKeys("b");
        driver.findElement(By.id("search-button")).click();
        WebDriverWait wait = new WebDriverWait(driver, 50);
        WebElement Category_Body = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("eventsBox1")));
        Category_Body.click();
    }

    @And("I click submit")
    public void i_click_submit() {
        driver.findElement(By.id("submit-button")).click();
    }

    @Then("I should see a no users alert")
    public void i_should_see_a_no_users_alert() {
        assertEquals(driver.switchTo().alert().getText(), "Please add at least one user");
    }

    @And("I select an user")
    public void i_select_an_user() {
        WebDriverWait wait = new WebDriverWait(driver, 1);
        driver.findElement(By.id("username")).sendKeys("asdf");
        driver.findElement(By.id("username")).sendKeys(Keys.ENTER);
        driver.findElement(By.id("usersBox")).click();
    }

    @Then("I should see a no proposal name alert")
    public void i_should_see_a_no_proposal_name_alert() {
        assertEquals(driver.switchTo().alert().getText(), "Please add a proposal name");
    }

    @And("I write a proposal name")
    public void i_write_a_proposal_name() {
        driver.findElement(By.id("proposalName")).sendKeys("testing");
    }

    @Then("I should see a success alert")
    public void i_should_see_a_success_alert() {
        assertEquals(driver.switchTo().alert().getText(), "Proposal sent!");
    }

//    @And("I select an user that has blocked me")
//    public void i_select_an_user_that_has_blocked_me() {
//        WebDriverWait wait = new WebDriverWait(driver, 1);
//        driver.findElement(By.id("username")).sendKeys("blockee");
//        driver.findElement(By.id("username")).sendKeys(Keys.ENTER);
//        driver.findElement(By.id("usersBox")).click();
//    }
//
//    @Then("I should see a error alert")
//    public void i_should_see_a_error_alert() {
//        assertEquals(driver.switchTo().alert().getText(), "This user has blocked you");
//    }

    //sent proposals page
    @Given("I am on the sent proposals page")
    public void iAmOnTheSentProposalsPage() throws InterruptedException {
        //create a proposal
        iAmOnTheLoginPage();
        iFillOutMyCredentials();
        iClickOnTheLogInButton();
        i_select_an_event();
        i_click_next();
        i_select_an_user();
        i_write_a_proposal_name();
        i_click_submit();
        driver.switchTo().alert().accept();
        //go to the sent proposals page
        driver.get("http://localhost:8080/sentProposals.html");
    }

    @When("I click on the finalize proposals button")
    public void iClickOnTheFinalizeProposalsButton(){
        driver.findElement(By.id("prop-button")).click();
    }

    @Then("I should see the alert is not empty")
    public void iShouldSeeTheAlertIsNotEmpty(){
        assertNotNull(driver.switchTo().alert().getText());
    }

    //profile availability
    @Given("I am on the profile page")
    public void iAmOnTheProfilePage() {
        iAmOnTheLoginPage();
        iFillOutMyCredentials();
        iClickOnTheLogInButton();
        driver.get("http://localhost:8080/account.html");
    }

    @And("I input a start date")
    public void iInputAStartDate(){
        driver.findElement(By.id("start-date")).click();
        driver.findElement(By.id("start-date")).sendKeys("2021-12-02");
    }

    @And("I input an end date")
    public void iInputAnEndDate(){
        driver.findElement(By.id("end-date")).click();
        driver.findElement(By.id("end-date")).sendKeys("2021-12-09");
    }
    @When("I click on the submit button")
    public void iClickOnTheSubmitButton(){
        driver.findElement(By.id("submit-avail")).click();
    }
    @Then("I should see the alert Unavailability has been set!")
    public void iShouldSeeTheAlertUnavailabilityHasBeenSet(){
        assertEquals(driver.switchTo().alert().getText(), "Unavailability has been set!");
    }

    //blocked list feature
    @Given("I click block")
    public void i_click_block() {
        driver.findElement(By.cssSelector(".blocked")).click();
    }

    @Then("the user should show up in the blocked list")
    public void the_user_should_show_up_in_the_blocked_list() {
        assertEquals(driver.findElement(By.cssSelector(".blocked-list")).getText(),"user3\n" +
                "Unblock\n" +
                "user4\n" +
                "Unblock\n" +
                "ExistingName\n" +
                "Unblock");
    }

    @Given("I click unblock")
    public void i_click_unblock() {
        driver.findElement(By.cssSelector(".unblock")).click();
    }

    @Then("the user should show up in the unblocked list")
    public void the_user_should_show_up_in_the_unblocked_list() {
        assertEquals(driver.findElement(By.cssSelector(".search-users")).getText(),"ExistingName\n" +
                "Block\n" +
                "NonExistingName\n" +
                "Block\n" +
                "a\n" +
                "Block\n" +
                "asdf\n" +
                "Block\n" +
                "user3\n" +
                "Block");
    }

    @Given("User10 has a proposal")
    public void userHasAProposal()  {
        Event event = new Event("testEvent1","2021-12-30","01:00:00","google.com","movie");
        Proposal proposal = new Proposal("testProp1",
                "user9",
                new ArrayList<>(Arrays.asList("user10")),
                new ArrayList<>(Arrays.asList(event)));
        DatabaseManager.object().insertSentProposal(proposal.getProposalID(), proposal.getProposalTitle(), proposal.getSenderUsername());
        DatabaseManager.object().insertReceivedProposal("user10", proposal.getProposalID());
        DatabaseManager.object().insertEvent(event, proposal.getProposalID());
    }

    @And("I am logged in to user10")
    public void iAmLoggedInToUser() {
        driver.get(ROOT_URL + "login.html");
        driver.findElement(By.cssSelector("#input-username")).sendKeys("user10");
        driver.findElement(By.cssSelector("#input-password")).sendKeys("10");
        iClickOnTheLogInButton();
    }

    @And("I am on the calendar page")
    public void iAmOnTheCalendarPage() {
        driver.get(ROOT_URL + "calendar.html");
        WebDriverWait wait = new WebDriverWait(driver, 50);
        WebElement switchButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("switchButton")));
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {}
    }

    @And("I click the switch button")
    public void iClickTheSwitchButton() {
        WebDriverWait wait = new WebDriverWait(driver, 50);
        WebElement switchButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("switchButton")));
        switchButton.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("eventList")));
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {}
    }

    @Then("I should see the proposal on both the calendar and the list")
    public void iShouldSeeTheProposalOnBothTheCalendarAndTheList() {
        assertTrue(driver.findElements(By.className("fc-event-title")).size() > 0);//There is an element in the calendar
        iClickTheSwitchButton();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {}
        assertTrue(driver.findElements(By.className("listItem")).size() > 0);
    }

    @Given("User10 has two proposals")
    public void userHasTwoProposals() throws IOException {
        Event event = new Event("testEvent1","2021-12-30","01:00:00","google.com","movie");
        Proposal proposal = new Proposal("testProp1",
                "user9",
                new ArrayList<>(Arrays.asList("user10")),
                new ArrayList<>(Arrays.asList(event)));
        System.out.println(proposal.getProposalID() + " - " + proposal.getProposalTitle() + " - " + proposal.getSenderUsername());
        DatabaseManager.object().insertSentProposal(proposal.getProposalID(), proposal.getProposalTitle(), proposal.getSenderUsername());
        DatabaseManager.object().insertReceivedProposal("user10", proposal.getProposalID());
        DatabaseManager.object().insertEvent(event, proposal.getProposalID());

        event = new Event("testEvent2","2021-12-31","01:00:00","google.com","movie");
        proposal = new Proposal("testProp2",
                "user9",
                new ArrayList<>(Arrays.asList("user10")),
                new ArrayList<>(Arrays.asList(event)));
        DatabaseManager.object().insertSentProposal(proposal.getProposalID(), proposal.getProposalTitle(), proposal.getSenderUsername());
        DatabaseManager.object().insertReceivedProposal("user10", proposal.getProposalID());
        DatabaseManager.object().insertEvent(event, proposal.getProposalID());
    }

    String firstEventName;

    @And("I change the sort option")
    public void iChangeTheSortOption() {
        firstEventName = driver.findElement(By.className("listItem")).findElement(By.tagName("a")).getText();
        Select sort = new Select(driver.findElement(By.id("listSortSelector")));
        sort.selectByVisibleText("Latest First");
    }

    @Then("The order of the list should change")
    public void theOrderOfTheListShouldChange() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {}
        assertNotEquals(driver.findElement(By.className("listItem")).findElement(By.tagName("a")).getText(), firstEventName);
    }

    @Given("User10 has a not finalized proposal")
    public void userHasANotFinalizedProposal() throws IOException {
        Event event = new Event("eventNotFinalized","2021-12-30","01:00:00","google.com","movie");
        Proposal proposal = new Proposal("testPropNotFinalized",
                "user9",
                new ArrayList<>(Arrays.asList("user10")),
                new ArrayList<>(Arrays.asList(event)));
        DatabaseManager.object().insertSentProposal(proposal.getProposalID(), proposal.getProposalTitle(), proposal.getSenderUsername());
        DatabaseManager.object().insertReceivedProposal("user10", proposal.getProposalID());
        DatabaseManager.object().insertEvent(event, proposal.getProposalID());
    }

    @And("I switch to show only finalized")
    public void iSwitchToShowOnlyFinalized() {
        Select sort = new Select(driver.findElement(By.id("finalizedSelector")));
        sort.selectByVisibleText("Only Finalized");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {}
    }

    @Then("I shouldnt see the not finalized proposals events")
    public void iShouldntSeeTheNotFinalizedProposalsEvents() {
        List<WebElement> listItems = driver.findElements(By.className("listItem"));
        for (int i =0; i < listItems.size(); i++)
        {
            assertNotEquals(listItems.get(i).findElement(By.tagName("a")).getText(), "eventNotFinalized");
        }
    }

    @Given("User10 has a finalized proposal")
    public void userHasAFinalizedProposal() throws IOException {
        Event event = new Event("eventFinalized","2021-12-30","01:00:00","google.com","movie");
        Proposal proposal = new Proposal("testPropFinalized",
                "user9",
                new ArrayList<>(Arrays.asList("user10")),
                new ArrayList<>(Arrays.asList(event)));
        DatabaseManager.object().insertSentProposal(proposal.getProposalID(), proposal.getProposalTitle(), proposal.getSenderUsername());
        DatabaseManager.object().insertReceivedProposal("user10", proposal.getProposalID());
        DatabaseManager.object().insertEvent(event, proposal.getProposalID());

        DatabaseManager.object().updateFinalizedProposal(proposal.getEvents().get(0).getEventID(),proposal.getProposalID());
    }

    @And("I switch to show only not finalized")
    public void iSwitchToShowOnlyNotFinalized() {
        Select sort = new Select(driver.findElement(By.id("finalizedSelector")));
        sort.selectByVisibleText("Only Not Finalized");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {}
    }

    @Then("I shouldnt see the finalized proposals events")
    public void iShouldntSeeTheFinalizedProposalsEvents() {
        List<WebElement> listItems = driver.findElements(By.className("listItem"));
        for (int i =0; i < listItems.size(); i++)
        {
            assertNotEquals(listItems.get(i).findElement(By.tagName("a")).getText(), "eventFinalized");
        }
    }

    @And("User10 has an event without a response")
    public void userHasAnEventWithoutAResponse() throws IOException {
        Event event = new Event("eventNoResponse","2021-12-30","01:00:00","google.com","movie");
        Proposal proposal = new Proposal("testPropNoResponse",
                "user9",
                new ArrayList<>(Arrays.asList("user10")),
                new ArrayList<>(Arrays.asList(event)));
        DatabaseManager.object().insertSentProposal(proposal.getProposalID(), proposal.getProposalTitle(), proposal.getSenderUsername());
        DatabaseManager.object().insertReceivedProposal("user10", proposal.getProposalID());
        DatabaseManager.object().insertEvent(event, proposal.getProposalID());
    }

    @And("I switch to show only not responded")
    public void iSwitchToShowOnlyNotResponded() {
        Select sort = new Select(driver.findElement(By.id("respondedSelector")));
        sort.selectByVisibleText("Only Not Responded");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {}
    }

    @Then("I shouldnt see the responded event")
    public void iShouldntSeeTheRespondedEvent() {
        List<WebElement> listItems = driver.findElements(By.className("listItem"));
        for (int i =0; i < listItems.size(); i++)
        {
            assertNotEquals(listItems.get(i).findElement(By.tagName("a")).getText(), "eventResponded");
        }
    }

    @And("User10 has an event with a response")
    public void userHasAnEventWithAResponse() throws IOException {
        Event event = new Event("eventResponded","2021-12-30","01:00:00","google.com","movie");
        Proposal proposal = new Proposal("testPropResponded",
                "user9",
                new ArrayList<>(Arrays.asList("user10")),
                new ArrayList<>(Arrays.asList(event)));
        DatabaseManager.object().insertSentProposal(proposal.getProposalID(), proposal.getProposalTitle(), proposal.getSenderUsername());
        DatabaseManager.object().insertReceivedProposal("user10", proposal.getProposalID());
        DatabaseManager.object().insertEvent(event, proposal.getProposalID());

        EventResponse response = new EventResponse(event.getEventID(), 2, 2, "user10");
        DatabaseManager.object().insertRespondedEvent(response);
    }

    @And("I switch to show only responded")
    public void iSwitchToShowOnlyResponded() {
        Select sort = new Select(driver.findElement(By.id("respondedSelector")));
        sort.selectByVisibleText("Only Responded");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {}
    }

    @Then("I shouldnt see the not responded event")
    public void iShouldntSeeTheNotRespondedEvent() {
        List<WebElement> listItems = driver.findElements(By.className("listItem"));
        for (int i =0; i < listItems.size(); i++)
        {
            assertNotEquals(listItems.get(i).findElement(By.tagName("a")).getText(), "eventNoResponse");
        }
    }

    @After()
    public void after() {
        driver.quit();
        User u = new User("asdf", "asdf");
        DatabaseManager.object().deleteUser(u);
        DatabaseManager.object().close();
        keywords = null;
        startDate = null;
        endDate = null;
        countrycode = null;
    }
}
