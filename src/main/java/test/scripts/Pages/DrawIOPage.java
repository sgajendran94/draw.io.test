package test.scripts.Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;
import java.util.stream.IntStream;

public class DrawIOPage {

    WebDriver driver;

    public static final String url = "https://app.diagrams.net/";

    public enum Destinations {
        DEVICE("Device"),
        GDRIVE("Google Drive"),
        ODRIVE("OneDrive"),
        GHUB("GitHub"),
        GLAB("GitLab");

        private String destination;

        Destinations(String destination) {
            this.destination = destination;
        }

        public String getDestination() {
            return destination;
        }
    }

    private static final String[] generalShapeList = new String[]{
            "Rectangle",
            "Rounded Rectangle",
            "Text",
            "Heading",
            "Ellipse",
            "Square",
            "Circle",
            "Process",
            "Diamond",
            "Parallelogram",
            "Hexagon",
            "Triangle",
            "Cyclinder",
            "Cloud",
            "Document",
            "Internal Storage",
            "Cube",
            "Step",
            "Trapezoid",
            "Tape"
    };

    private static final String[] toolBarShapeList = new String[]{
            "None",
            "Rectangle",
            "Circle",
            "Diamond",
            "None",
            "Parallelogram",
            "None",
            "None",
            "Step",
            "Process"
    };

    public DrawIOPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public WebElement saveDestination(Destinations destination) {
        return driver.findElement(By.cssSelector("a[title='" + destination.getDestination() + "']"));
    }

    public WebElement saveDestination() {
        return driver.findElement(By.cssSelector("a[title='" + Destinations.DEVICE + "']"));
    }

    public WebElement dynamicText(String text, String tagName) {
        return driver.findElement(By.xpath("//" + tagName + "[contains(text(), '" + text + "')]"));
    }

    @FindBy(css = "div.geDialog")
    public WebElement diagramTypeModal;

    @FindBy(css = "div.geDialog > div > div > input")
    public WebElement fileName;

    @FindBy(css = "div.geDiagramContainer.geDiagramBackdrop")
    public WebElement diagramContainer;

    @FindBy(css = "div.geBackgroundPage")
    public WebElement canvas;

    @FindBy(css = "div.geFooterContainer")
    public WebElement footerContainer;

    @FindBy(css = "div.geSidebarContainer.geFormatContainer")
    public WebElement formatContainer;

    @FindBy(css = "div.geSidebarContainer")
    public WebElement sidebarContainer;

    @FindBy(css = "div.mxPopupMenu")
    public WebElement contextPopUp;

    @FindBy(css = "textarea")
    public  WebElement textArea;

    @FindBy(xpath = "//div[contains(text(), 'Save As')]/following-sibling::div/input")
    public WebElement saveAsTextArea;

    @FindBy(xpath = "//div[@data-action = 'saveAs']")
    public WebElement save;

    @FindBy(xpath = "//div[contains(text(), 'Where')]/following-sibling::div/select")
    public WebElement storageDestination;

    @FindBy(xpath = "//td[contains(text(), 'Dx:')]/following-sibling::td/input")
    public WebElement dX;

    @FindBy(css = "div.geFormatSection > select")
    public WebElement editData;

    public WebElement generalShape(String shape) {
        return driver.findElements(By.xpath("//a[contains(text(), 'General')]//following-sibling::div/div/a")).get(IntStream.range(0, generalShapeList.length).filter(i -> generalShapeList[i].equals(shape)).findFirst().orElse(-1));
    }

    public WebElement connectionShape(String shape) {
        return driver.findElements(By.xpath("//div[contains(@class, 'geShapePicker')]//a")).get(2 * IntStream.range(0, toolBarShapeList.length).filter(i -> toolBarShapeList[i].equals(shape)).findFirst().orElse(-1));
    }

    public WebElement connectorDown() {
        return driver.findElement(By.xpath("//img[contains(@title, 'Click to connect and clone')][2]"));
    }

    public WebElement connectorRight() {
        return driver.findElement(By.xpath("//img[contains(@title, 'Click to connect and clone')][3]"));
    }

    public WebElement connectorLeft() {
        return driver.findElement(By.xpath("//img[contains(@title, 'Click to connect and clone')][4]"));
    }

    public WebElement g() {
        List<WebElement> elementList = driver.findElements(By.xpath("(//div[contains(@class, 'geDiagramContainer')]//*[local-name()='svg']/*[local-name()='g']//*[local-name()='g'])[2]//*[local-name()='g']"));
        return elementList.get(elementList.size() - 1);
    }

    public WebElement g(String text) {
        return driver.findElement(By.xpath("//div[contains(text(), '" + text + "')]"));
    }

    public List<WebElement> gs(String text) {
        return driver.findElements(By.xpath("//div[contains(text(), '" + text + "')]"));
    }

}
