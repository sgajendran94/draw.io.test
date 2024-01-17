package Utils;

import com.github.javafaker.Faker;
import org.openqa.selenium.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import test.scripts.Pages.DrawIOPage;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;

public class Helper {

    private WebDriver driver;
    private WebDriverWait wait;
    private Actions actions;
    private Faker faker = new Faker();
    private DrawIOPage dioPage;
    private JavascriptExecutor js;
    public HashMap<String, WebElement> map;
    public HashMap<String, String> styles;
    private Select select;

    public Helper(WebDriver driver) throws AWTException {
        this.driver = driver;
        wait = new WebDriverWait(driver, 20);
        actions = new Actions(driver);
        dioPage = new DrawIOPage(driver);
        js = (JavascriptExecutor) driver;
        map = new HashMap<>();
        styles = new HashMap<>();
        styles.put("Ellipse", "ellipse;whiteSpace=wrap;html=1;rotation=0;");
        styles.put("Parallelogram", "shape=parallelogram;perimeter=parallelogramPerimeter;whiteSpace=wrap;html=1;fixedSize=1;");
        styles.put("Rectangle", "whiteSpace=wrap;html=1;");
        styles.put("Process", "shape=process;whiteSpace=wrap;html=1;backgroundOutline = 1;");
    }

    public void waitForElementToBeClickable(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public void waitForVisibility(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
    }


    public void openNewDrawing() throws InterruptedException {
        driver.get(dioPage.url);
        waitForElementToBeClickable(dioPage.dynamicText("Decide later", "span"));
        dioPage.dynamicText("Decide later", "span").click();
    }

    public HashMap<String, WebElement> createInProgressFlowChart() throws InterruptedException {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        HashMap<String, String> start = new HashMap();
        HashMap<String, String> readA = new HashMap();
        HashMap<String, String> readB = new HashMap();
        HashMap<String, String> sumAB = new HashMap();
        HashMap<String, String> printSum = new HashMap();
        HashMap<String, String> end = new HashMap();
        start.put("Circle", "Start");
        readA.put("Rectangle", "Read A");
        readB.put("Rectangle", "Read B");
        sumAB.put("Process", "Calculate C = A + B");
        printSum.put("Parallelogram", "Print C");
        end.put("Circle", "End");
        list.add(start);
        list.add(readA);
        list.add(readB);
        list.add(sumAB);
        list.add(printSum);
        list.add(end);
        createSimpleFlowChart(list);
        Thread.sleep(5000);
        return map;
    }

    public void createSimpleFlowChart(ArrayList<HashMap<String, String>> flowChartData) throws InterruptedException {
        Dimension canvas_size = dioPage.diagramContainer.getSize();
        Point containerLocation = dioPage.diagramContainer.getLocation();
        Point generalShapeLocation = dioPage.generalShape("Rectangle").getLocation();
        int mid_point_width = (canvas_size.getWidth() + containerLocation.getX()) / 2;
        int start_pos_height = containerLocation.getY() - generalShapeLocation.getY() + 10;
        int count = 0;
        WebElement element;
        for (HashMap flowChartPoint : flowChartData) {
            if (count == 0) {
                element = createFlowChartPoint((String) flowChartPoint.keySet().toArray()[0], (String) flowChartPoint.values().toArray()[0], mid_point_width, start_pos_height);
                map.put((String) flowChartPoint.values().toArray()[0], element);
                actions.moveToElement(element).build().perform();

            } else {
                element = createFlowChartPoint((String) flowChartPoint.keySet().toArray()[0], (String) flowChartPoint.values().toArray()[0]);
                map.put((String) flowChartPoint.values().toArray()[0], element);
                actions.moveToElement(element).build().perform();
            }
            if (count != flowChartData.size() - 1) {
                actions.moveByOffset(1,1).build().perform();
                waitForElementToBeClickable(dioPage.connectorDown());
                dioPage.connectorDown().click();
            }
            count++;
        }

    }

    public WebElement createFlowChartPoint(String shape, String text, int x, int y) {
        actions.dragAndDropBy(dioPage.generalShape(shape), x, y).release().sendKeys(text).build().perform();
        js.executeScript("arguments[0].scrollBy(0,60);", dioPage.diagramContainer);
        actions.click().build().perform();
        return dioPage.g(text);
    }

    public WebElement createFlowChartPoint(String shape, String text) {
        waitForVisibility(dioPage.connectionShape(shape));
        actions.moveToElement(dioPage.connectionShape(shape)).click().sendKeys(text).build().perform();
        js.executeScript("arguments[0].scrollBy(0,60);", dioPage.diagramContainer);
        actions.click().build().perform();
        return dioPage.g(text);
    }

    public void updateStepText(String step, String updateStepText) throws InterruptedException {
        WebElement stepEle = map.get(step);
        js.executeScript("arguments[0].scrollIntoView();", stepEle);
        js.executeScript("arguments[0].scrollBy(0,-20);", dioPage.diagramContainer);
        actions.moveToElement(stepEle).doubleClick().sendKeys(updateStepText).sendKeys(Keys.ESCAPE).build().perform();
        map.put(step, dioPage.g(updateStepText));
    }

    public void deleteStepText(String step) throws InterruptedException {
        js.executeScript("arguments[0].scrollIntoView();", dioPage.g(step));
        js.executeScript("arguments[0].scrollBy(0,-20);", dioPage.diagramContainer);
        actions.moveToElement(dioPage.g(step)).doubleClick().keyDown(Keys.CONTROL).sendKeys(Keys.chord("A")).keyUp(Keys.CONTROL).sendKeys(Keys.DELETE).sendKeys(Keys.ESCAPE).build().perform();
    }

    public void updateStep(String step, String updateShape, String updateText) throws InterruptedException {
        WebElement stepEle = map.get(step);
        actions.moveToElement(stepEle).build().perform();
        actions.moveByOffset(1,1).build().perform();
        waitForVisibility(dioPage.connectorDown());
        dioPage.connectorDown().click();
        map.put(updateText, createFlowChartPoint(updateShape, updateText));
        actions.moveToElement(map.get(updateText)).build().perform();
        actions.moveByOffset(1,1).build().perform();
        waitForVisibility(dioPage.connectorLeft());
        waitForElementToBeClickable(dioPage.connectorLeft());
        dioPage.connectorLeft().click();
    }

    public void deleteSteps(String primary, String secondary) throws InterruptedException {
        WebElement stepEle = map.get(primary);
        js.executeScript("arguments[0].scrollIntoView();", stepEle);
        js.executeScript("arguments[0].scrollBy(0,-60);", dioPage.diagramContainer);
        Thread.sleep(500);
        actions.sendKeys(Keys.ESCAPE).build().perform();
        actions.moveToElement(stepEle).build().perform();
        actions.moveByOffset(1,1).build().perform();
        waitForVisibility(dioPage.connectorDown());
        dioPage.connectorDown().click();
        actions.sendKeys(Keys.DELETE).build().perform();
        actions.moveToElement(stepEle).build().perform();
        waitForVisibility(dioPage.connectorLeft());
        dioPage.connectorLeft().click();
        actions.sendKeys(Keys.DELETE).build().perform();
        actions.moveToElement(stepEle).click().sendKeys(Keys.DELETE).build().perform();
        stepEle = map.get(secondary);
        actions.moveToElement(stepEle).build().perform();
        waitForVisibility(dioPage.connectorLeft());
        dioPage.connectorLeft().click();
        actions.sendKeys(Keys.DELETE).build().perform();
        actions.moveToElement(stepEle).click().sendKeys(Keys.DELETE).build().perform();
        map.remove(primary);
        map.remove(secondary);
    }

    public void updateShape(String step, String updateShape) {
        WebElement stepEle = map.get(step);
        js.executeScript("arguments[0].scrollIntoView();", stepEle);
        js.executeScript("arguments[0].scrollBy(0,-20);", dioPage.diagramContainer);
        actions.moveToElement(stepEle).contextClick().build().perform();
        waitForVisibility(dioPage.contextPopUp);
        dioPage.dynamicText("Edit Style", "td").click();
        waitForVisibility(dioPage.diagramTypeModal);
        dioPage.textArea.clear();
        dioPage.textArea.sendKeys(styles.get(updateShape));
        dioPage.dynamicText("Apply", "button").click();
        waitForElementToBeClickable(stepEle);
    }

    public String fetchShapeProperty(String step) {
        WebElement stepEle = map.get(step);
        actions.moveToElement(stepEle).contextClick().build().perform();
        waitForVisibility(dioPage.contextPopUp);
        dioPage.dynamicText("Edit Style", "td").click();
        waitForVisibility(dioPage.diagramTypeModal);
        String shapeProperty = dioPage.textArea.getText();
        dioPage.dynamicText("Cancel", "button").click();
        waitForElementToBeClickable(stepEle);
        return shapeProperty;
    }

    public void selectStorageDestination(String destination) {
        select = new Select(dioPage.storageDestination);
        select.selectByValue(destination);
    }

    public void addStep(String step, String existing_step, String shape, Boolean connect) {
        WebElement ele = map.get(existing_step);
        js.executeScript("arguments[0].scrollIntoView();", ele);
        js.executeScript("arguments[0].scrollBy(0,-60);", dioPage.diagramContainer);
        actions.moveToElement(ele).build().perform();
        actions.moveByOffset(1,1).build().perform();
        if (connect) {
            waitForVisibility(dioPage.connectorDown());
            dioPage.connectorDown().click();
        } else {
            waitForVisibility(dioPage.connectorRight());
            dioPage.connectorRight().click();
        }
        map.put(step, createFlowChartPoint(shape, step));
    }

    public void deleteShape(String text) {
        WebElement ele = map.get(text);
        js.executeScript("arguments[0].scrollIntoView();", ele);
        js.executeScript("arguments[0].scrollBy(0,-60);", dioPage.diagramContainer);
        actions.moveToElement(ele).click().sendKeys(Keys.DELETE).build().perform();
    }


}
