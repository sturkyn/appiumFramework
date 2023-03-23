package tests;

import base.BaseListener;
import base.BaseTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import screens.HomeScreen;
import utils.Assert;

import static utils.ScreenObjectSupplier.*;

@Listeners(BaseListener.class)
public class HomeScreenTest extends BaseTest {

    @Test
    public void checkElementText() {
        loadApp("PlaymakerApp.ipa");
        loadSiteUrl("demo.castlabs.com");

        Assert.assertEquals($(HomeScreen.class).checkText(), "Hello World!", "Checking for text");
                ;

    }


}
