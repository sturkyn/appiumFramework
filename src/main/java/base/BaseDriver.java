package base;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;

public class BaseDriver {

    private WebDriver dr;
    private String platform = "Web";
    private boolean initiated = false;

    public BaseDriver(String platformName) {
        this.platform = platformName;
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

                androidCaps.setCapability(MobileCapabilityType.UDID, androidCaps.getCapability("udid").toString());
                androidCaps.setCapability(MobileCapabilityType.DEVICE_NAME, androidCaps.getCapability("deviceName").toString());
                androidCaps.setCapability(MobileCapabilityType.PLATFORM_VERSION, androidCaps.getCapability("platformVersion").toString());
                break;

            case "iOS":
                DesiredCapabilities iosCaps = new DesiredCapabilities();
                iosCaps.setCapability(MobileCapabilityType.PLATFORM_NAME, "iOS");
                iosCaps.setCapability(MobileCapabilityType.PLATFORM_VERSION, "");
                iosCaps.setCapability(MobileCapabilityType.DEVICE_NAME, "");
                iosCaps.setCapability(MobileCapabilityType.APP, "src/test/resources/PlaymakerApp.ipa");
                // Add more desired capabilities as needed

                // Start the Appium server and initialize the iOS driver
                dr = new IOSDriver(new URL("http://0.0.0.0:4723/wd/hub"), iosCaps);
                break;

            case "Web":
            default:
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

            case "Tizen":
                //driver = new TizenDriver;
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

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

}


