package utils;

import com.esotericsoftware.reflectasm.ConstructorAccess;
import com.fasterxml.jackson.databind.ser.Serializers;
import utils.log.Reporter;
import base.BaseTest;
import screens.HomeScreen;


public class ScreenObjectSupplier {
    public static <T> T $(Class<T> pageObject) {
        return ConstructorAccess.get(pageObject).newInstance();
    }

    public static HomeScreen loadSiteUrl(String url) {
        Reporter.log("Opening URL: " + url);
        try {
            BaseTest.getDriver().get(url);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return $(HomeScreen.class);
    }

    public static HomeScreen loadApp(String app) {
        Reporter.log("Loading App:" + app);
        try {
            BaseTest.getDriver().get(app);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return $(HomeScreen.class);
    }

    private ScreenObjectSupplier() {
    }

}
