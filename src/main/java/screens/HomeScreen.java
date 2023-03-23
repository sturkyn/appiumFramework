package screens;

import base.BasePage;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

public class HomeScreen extends BasePage {
    protected final By textBox = AppiumBy.accessibilityId("hello");

    public String checkText() {

        return getElementText(textBox);

    }
}
