package test.scripts.stepdefinitions;

import Utils.Helper;
import com.github.javafaker.Faker;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.BeforeStep;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.Test;
import test.scripts.Pages.DrawIOPage;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class Flowchart {

    private WebDriver driver;
    private Helper helper;
    private DrawIOPage dioPage;
    private HashMap<String, WebElement> m1;
    private Actions actions;
    private JavascriptExecutor js;

    @Before
    public void setup() throws AWTException, InterruptedException {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--disable-popup-blocking");
        driver = WebDriverManager.chromedriver().capabilities(chromeOptions).create();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        helper = new Helper(driver);
        dioPage =  new DrawIOPage(driver);
        actions = new Actions(driver);
        js = (JavascriptExecutor) driver;
    }

    @BeforeStep
    public void matchMap() {
        m1 = helper.map;
    }

    @After
    public void quitChrome() {
        driver.close();
        driver.quit();
    }

    @Given("the user has a flowchart in progress")
    public void the_user_has_a_flowchart_in_progress() throws InterruptedException {
        // Write code here that turns the phrase above into concrete actions
        helper.openNewDrawing();
        helper.createInProgressFlowChart();
    }

    @When("the user adds a new {} adjacent {}")
    public void user_adds_new_step_adjacent_existing_step(String step, String existing_step) {
        // Write code here that turns the phrase above into concrete actions
        String[] shapes = new String[]{"Circle", "Rectangle", "Parallelogram"};
        String shape = shapes[new Random().nextInt(shapes.length)];
        helper.addStep(step, existing_step, shape, false);
    }

    @When("the user adds a new {} with {}")
    public void the_user_adds_a_new_shape_with_step_description(String shape, String text) {
        helper.addStep(text, "End", shape, true);
    }

    @And("the user should be able to {} the {} with {}")
    public void the_user_should_be_able_to_update_the_step(String action, String step, String updateStep) throws InterruptedException {
        // Write code here that turns the phrase above into concrete actions
        if (action.toLowerCase().equals("update")) {
            String[] shapes = new String[]{"Circle", "Rectangle", "Parallelogram"};
            helper.updateStep(step, shapes[new Random().nextInt(shapes.length)], updateStep);
            Assert.assertTrue(dioPage.g(step).isDisplayed(), "Primary Step");
            Assert.assertTrue(dioPage.g(updateStep).isDisplayed(), "Secondary Step");
        } else {
            helper.deleteSteps(step, updateStep);
            Assert.assertTrue(dioPage.gs(step).isEmpty(), "Primary Step");
            Assert.assertTrue(dioPage.gs(updateStep).isEmpty(), "Secondary Step");
        }

    }

    @Then("all shapes and steps should be reflected")
    public void all_steps_should_be_reflected() {
        // Write code here that turns the phrase above into concrete actions
        String[] steps = new String[]{"Start", "Read A", "Read B", "Calculate C = A + B", "Print C", "End"};
        Object[] mapKeySet = m1.keySet().toArray();
        Arrays.sort(mapKeySet);
        Arrays.sort(steps);
        System.out.println(mapKeySet);
        System.out.println(steps);
        for(String step : steps) {
            Assert.assertEquals(mapKeySet, steps);
        }
    }

    @Then("the {} should be added to the appropriate point")
    public void the_step_should_be_added_to_the_appropriate_point(String step) {
        // Write code here that turns the phrase above into concrete actions
        Assert.assertTrue(dioPage.g(step).isDisplayed());
    }

    @Then("the user should have an option to {} the {} with {}")
    public void the_user_should_have_an_option_to_update_the_shape_with_step(String action, String shape, String text) {
        if(action.toLowerCase().equals("update")) {
            helper.updateShape(text, shape);
            Assert.assertEquals(helper.styles.get(shape), helper.fetchShapeProperty(text));
        } else {
            helper.deleteShape(text);
            Assert.assertTrue(dioPage.gs(text).isEmpty(), "Shape deleted");
        }
    }

    @And("user should be able to update the text {} with {}")
    public void update_text(String step, String updated_step) throws InterruptedException {
        helper.updateStepText(step, updated_step);
        Assert.assertTrue(dioPage.g(step).isDisplayed());
    }

    @And("user should be able to delete the text {}")
    public void delete_text(String step) throws InterruptedException {
        helper.deleteStepText(step);
        Assert.assertTrue(dioPage.gs(step).isEmpty());
    }

}
