package base;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import utils.PropertiesUtils;

import java.net.MalformedURLException;
import java.net.URL;

public class BaseDriver {

    private WebDriver dr;
    private String platform = PropertiesUtils.getProp("platformName");
    private String browser = PropertiesUtils.getProp("browserName");
    private boolean initiated = false;

    public BaseDriver(String platformName, String browserName) {
        this.platform = platformName;
        this.browser = browserName;
    }

    public void initiateDriver() throws MalformedURLException {
        initiated = true;

        switch (platform) {
            case "Android":
                DesiredCapabilities androidCaps = new DesiredCapabilities();
                androidCaps.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
                androidCaps.setCapability(MobileCapabilityType.PLATFORM_VERSION, "");
                androidCaps.setCapability(MobileCapabilityType.DEVICE_NAME, "");
                androidCaps.setCapability(MobileCapabilityType.APP, "src/test/resources/playmaker-app-mobile-debug.apk");
                // Add more desired capabilities as needed

                // Start the Appium server and initialize the Android driver
                dr = new AndroidDriver(new URL("http://0.0.0.0:4723/wd/hub"), androidCaps);

                androidCaps.setCapability("appium:udid", androidCaps.getCapability("udid").toString());
                androidCaps.setCapability("appium:deviceName", androidCaps.getCapability("deviceName").toString());
                androidCaps.setCapability("appium:platformVersion", androidCaps.getCapability("platformVersion").toString());

                break;

            case "iOS":
                DesiredCapabilities iOSCaps = new DesiredCapabilities();
                iOSCaps.setCapability(MobileCapabilityType.PLATFORM_NAME, "iOS");
                iOSCaps.setCapability(MobileCapabilityType.PLATFORM_VERSION, "");
                iOSCaps.setCapability(MobileCapabilityType.DEVICE_NAME, "");
                iOSCaps.setCapability(MobileCapabilityType.APP, "src/test/resources/PlaymakerApp.ipa");
                // Add more desired capabilities as needed

                // Start the Appium server and initialize the iOS driver
                dr = new IOSDriver(new URL("http://0.0.0.0:4723/wd/hub"), iOSCaps);

                iOSCaps.setCapability("appium:udid", iOSCaps.getCapability("udid").toString());
                iOSCaps.setCapability("appium:deviceName", iOSCaps.getCapability("deviceName").toString());
                iOSCaps.setCapability("appium:platformVersion", iOSCaps.getCapability("platformVersion").toString());

                break;

            case "Web":
                switch(browser) {
                    case "Chrome":
                        ChromeOptions options = new ChromeOptions();
                        options.addArguments(
                                "--lang=en",
                                "--window-size=2000,1500",
                                "--disable-extensions",
                                "--disable-dev-shm-usage",
                                "--verbose",
                                "--disable-web-security",
                                "--ignore-certificate-errors",
                                "--allow-running-insecure-content",
                                "--allow-insecure-localhost",
                                "--no-sandbox",
                                "--disable-gpu",
                                "--headless"
                        );

                        options.addArguments("user-agent=Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.5060.53 Safari/537.36");
                        WebDriverManager.chromedriver().setup();
                        dr = new ChromeDriver(options);
                        dr.manage().window().maximize();

                        break;

                    case "Safari" :

                        WebDriverManager.safaridriver().setup();

                        break;

                    case "Edge" :

                        WebDriverManager.edgedriver().setup();

                        break;

                }

            case "Tizen":
                DesiredCapabilities tizenCaps = new DesiredCapabilities();
                tizenCaps.setCapability("platformName", "TizenTV");
                tizenCaps.setCapability("appium:automationName", "TizenTV");
                tizenCaps.setCapability("appium:deviceName", "device-host:192.168.242.73");
                tizenCaps.setCapability("appium:chromeDriverExecutable", "Users/sgunay/..");

                break;

            case "WebOS":
                //driver = new WebOSDriver;

                break;

        }
    }

    public boolean isInitiated() {
        return initiated;
    }

    public WebDriver getDr() {
        return dr;
    }

}


