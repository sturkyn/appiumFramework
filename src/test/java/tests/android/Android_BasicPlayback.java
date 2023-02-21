package tests.android;

import java.util.ArrayList;
import java.util.Arrays;

import base.BaseListener;
import base.BaseTest;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.Factory;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import uat_regression.screenelements.DownloadScreen_Android;
import uat_regression.screenelements.MainActivityScreen;
import uat_regression.screenelements.PlayerControllerScreen_iOS;
import uat_regression.screenelements.PlayerControlsScreen;
import uat_regression.screenelements.SettingsMenu_iOS;
import uat_regression.screenelements.Settings_Android;
import uat_regression.screenelements.ViewControllerSettingsScreen_iOS;
import uat_regression.utils.Android_Constants;
import uat_regression.utils.AssertFromLogs;
import uat_regression.utils.AssertFromLogs_Android;
import uat_regression.utils.BackgroundLogs;
import uat_regression.utils.CheckForLogFile_iOS2;
import uat_regression.utils.CheckForText;
import uat_regression.utils.CheckForText_iOS;
import uat_regression.utils.GetAssertValues;
import uat_regression.utils.GetDataFromExcel;
import uat_regression.utils.IOSConstants;
import uat_regression.utils.TakeScreenShotListener;
import uat_regression.utils.IOSLogs;
import uat_regression.utils.IOSLogsE2E;
import uat_regression.utils.IOSLogsEnd;
import uat_regression.utils.LogsThreadSync;
import uat_regression.utils.Suite_TestData;
import uat_regression.utils.Suite_TestData_Android;
import uat_regression.utils.TakeScreenShot;
import uat_regression.module.LandingPage;
import uat_regression.module.LoginPlayerMaker;
import uat_regression.module.LoginPrestoPLay;
import uat_regression.module.PlayerController_Android;
import uat_regression.module.SettingsPage;
import uat_regression.module.SettingsPage_Android;
import uat_regression.module.SwitchOrientation;
import uat_regression.module.ValidateBlackScreen;
import uat_regression.module.ViewController;

import utils.log.Reporter;


@Listeners(BaseListener.class)
public class Android_BasicPlayback extends BaseTest {

    @Factory(dataProviderClass=DataProviderClass.class,dataProvider="Android_BasicPlayback_Screen")
    public static Object[] factoryMethod(String asset_name) {

        Android_BasicPlayback instance = new Android_BasicPlayback(asset_name);
        return new Object[] { instance };
    }

    public Android_BasicPlayback(String asset_name) {
        this.asset_name = asset_name;
    }

    ReadExcel_DataProviderFactory ilogs ;

    GetDataFromExcel data_provider = new GetDataFromExcel();
    private String username= null;
    private String password= null;
    private String playcontent_assert= null;
    private String pause_assert= null;
    private String pauseplay_assert= null;

    //Data provider variables
    private String asset_name= null;
    int audio_count;
    int subtitle_count;
    private ArrayList<String>  videoPositionList;

    private String value =  IOSConstants.SEARCH_VALUE;
    private boolean sdkerror = false;

    @Test
    public void DRMSettings() throws Exception {
        String drm = "skip";
        if(asset_name.equalsIgnoreCase("QA - C12 - BBB Staging")) {
            drm = IOSConstants.ENABLE_DRM_OMA;
        }else if(asset_name.equalsIgnoreCase("QA - C12 - BBB Staging Copy1")) {
            drm = IOSConstants.ENABLE_DRM_WV;
        }
        SettingsPage_Android.selectDRM(driver, drm);
    }
    @Test(priority = 2)
    public void playContent() throws Exception {
        //Click on the first asset to start playing
        SettingsPage_Android.returnTOMainActivity(driver);

        driver.manage().logs().get("logcat");
        System.out.println("Play starts ...");
        //TakeScreenShot.getScreenshot(driver,  "test-output/Logs/Android");
        Thread.sleep(1500);
        MainActivityScreen.firstAsset_Android(driver).click();
        PlayerController_Android.waitForInvisibilityLoading(driver);
        String[] asset_list = LoginPlayerMaker.LoadingSlow_Assets;
        boolean contains = Arrays.stream(asset_list).anyMatch(asset_name::equals);
        if(contains) {
            Thread.sleep(5000);
        }
        String file ="logplayContent";
        AssertFromLogs_Android.assertPlay(driver, file);
    }


    @Test(priority = 3)
    public void check_BlackScreen() throws Exception {
        switch(asset_name) {
            case"HLS Clear Key":
            case "QA - 3rd party WV playback":
                break;
            default:
                if(asset_name.contains("TOS")) {
                    Thread.sleep(4000); //TOS has black screen in begining
                }
                String imageName = null;
                switch(Android_DriverSetUp.getUDID()) {
                    case "FA69W0302834":
                        imageName ="Android_BlackScreen_BasicPlayback_FA69W0302834";
                        break;
                    case "R5CRB0CKFWM":
                        imageName ="Android_BlackScreen_BasicPlayback_R5CRB0CKFWM";
                        break;
                    case "de71d6e":
                        imageName ="Android_BlackScreen_BasicPlayback_de71d6e";
                        break;
                    default:
                        imageName ="Android_BlackScreen_BasicPlayback"; //for CAP_APP_UDID_OFC_SAMSUNG
                        break;

                }

                ValidateBlackScreen.blackScreen_Check(driver, asset_name, imageName);
                break;
        }

    }


    @Test(priority = 4, dependsOnMethods = {"playContent"})
    public void playPause() throws Exception {

        driver.manage().logs().get("logcat");
        //	PlayerControlsScreen.playercontrols_element(driver).click();
        Thread.sleep(1000);
        PlayerControlsScreen.playpause_element(driver).click();

        String file = "logplayPause";
        AssertFromLogs_Android.assertPause(driver, file);

        PlayerControlsScreen.playpause_element(driver).click();

        AssertFromLogs_Android.assertResumePlay(driver, file);
        Thread.sleep(1000);
    }

    @Test(priority = 5, dependsOnMethods = {"playContent"})
    public void changeAudio() throws Exception {
        switch(asset_name) {
            case"HLS Clear Key":
            case "QA - 3rd party WV playback":
                break;
            default:
                String suite_name = this.getClass().getName();
                PlayerController_Android.changeAudio(driver, suite_name);
                break;
        }
    }



    @Test(priority = 6, dependsOnMethods = {"playContent"})
    public void changeSubtitle() throws Exception {
        switch(asset_name) {
            case"HLS Clear Key":
            case "QA - 3rd party WV playback":
                break;
            default:
                String suite_name = this.getClass().getName();
                PlayerController_Android.changeSubtitles(driver, suite_name,false);
                break;
        }
    }

    @Test(priority = 7, dependsOnMethods = {"playContent"})
    public void changeVideoQuality() throws Exception {
        switch(asset_name) {
            case"HLS Clear Key":
            case "QA - 3rd party WV playback":
                break;
            default:
                String suite_name = this.getClass().getName();
                PlayerController_Android.changeVideos(driver, suite_name);
                break;
        }
    }

    @Test(priority = 8, dependsOnMethods = {"playContent"})
    public void seekToPosition() throws Exception {
        driver.manage().logs().get("logcat");
        System.out.println("seekToPosition()");
        String suite_name = this.getClass().getName();
        videoPositionList = Suite_TestData_Android.getVideoPosition_AssertInput(asset_name, suite_name);
        //	System.out.println("Video Position array" + videoPositionList );

        PlayerController_Android.sliderSeek(driver, 0.6);
        String logFile ="logseek_mid";
        String current_log_assert = videoPositionList.get(0);

        AssertFromLogs_Android.assertSeek(driver, logFile, Integer.parseInt(current_log_assert));
        //PlayerController_Android.clearContentPanel(driver);
    }

    @Test(priority = 9, dependsOnMethods = {"playContent"})
    public void seekToBegining() throws Exception {
        switch(asset_name) {
            case"HLS Clear Key":
            case "QA - 3rd party WV playback":
                break;
            default:
                driver.manage().logs().get("logcat");
                PlayerController_Android.clearContentPanel(driver);
                String suite_name = this.getClass().getName();
                videoPositionList = Suite_TestData_Android.getVideoPosition_AssertInput(asset_name, suite_name);

                PlayerController_Android.sliderSeek(driver, 0.1);
                String logFile ="logseek_begin";
                String current_log_assert = videoPositionList.get(1);
                AssertFromLogs_Android.assertSeekBegining(driver, logFile, current_log_assert);
                break;
        }
    }

    @Test(priority = 10, dependsOnMethods = {"playContent"})
    public void changeOrientation() throws Exception {
        switch(asset_name) {
            case"HLS Clear Key":
            case "QA - 3rd party WV playback":
                break;
            default:
                SwitchOrientation.validateOrientation(driver);
                break;
        }
    }

    @Test(priority = 11, dependsOnMethods = {"playContent"})
    public void putToBackground() throws Exception {
        switch(asset_name) {
            case"HLS Clear Key":
            case "QA - 3rd party WV playback":
                break;
            default:
                driver.manage().logs().get("logcat");
                System.out.println("putToBackground()");
                BackgroundLogs.androidInBackground(driver);
                Thread.sleep(4000);
                String logFile = "logbg";
                BackgroundLogs.androidValidateLogs(driver, logFile);
                break;
        }
    }

    @Test(priority = 12, dependsOnMethods = {"playContent"})
    public void IncreasePlayBAckRate() throws Exception {
        switch(asset_name) {
            case"HLS Clear Key":
            case "QA - 3rd party WV playback":
                break;
            default:
                PlayerController_Android.increasePlayBackRate(driver);
                break;
        }
    }


    @Test(priority = 13, dependsOnMethods = {"playContent"})
    public void DecreasePlayBAckRate() throws Exception {
        switch(asset_name) {
            case"HLS Clear Key":
            case "QA - 3rd party WV playback":
                break;
            default:
                PlayerController_Android.decreasePlaybackRate(driver);
                break;
        }
    }

    @Test(priority = 16, dependsOnMethods = {"playContent"})
    public void fastForward() throws Exception {
        switch(asset_name) {
            case"HLS Clear Key":
            case "QA - 3rd party WV playback":
                break;
            default:
                PlayerController_Android.fastForward(driver);
                break;
        }
    }

    @Test(priority = 17, dependsOnMethods = {"playContent"})
    public void Rewind() throws Exception {
        switch(asset_name) {
            case"HLS Clear Key":
            case "QA - 3rd party WV playback":
                break;
            default:
                PlayerController_Android.rewind(driver);
                break;
        }
    }

    @Test(priority = 14, dependsOnMethods = {"playContent"})
    public void seekToEnd() throws Exception {
        SettingsPage_Android.seekEnd(driver, false,false);
    }

    @Test(priority = 15, dependsOnMethods = {"playContent"})
    public void enableLoop() throws Exception {
        switch(asset_name) {
            case"HLS Clear Key":
            case "QA - 3rd party WV playback":
                break;
            default:
                SettingsPage_Android.enableLoop(driver, "ON");

                DownloadScreen_Android.firstDownloadAsset(driver).click();
                Thread.sleep(12000);  //let it play for 10 sec

                SettingsPage_Android.seekEnd(driver, true,false);
                break;
        }
    }

    @AfterClass
    public void afterclass()throws Exception  {
        System.out.println("ENTER After Class");
        driver.pressKey(new KeyEvent().withKey(AndroidKey.BACK));
        Thread.sleep(1000);
    }

    @AfterSuite(alwaysRun = true)
    public void tearDown() throws Exception {
        System.out.println("ENTER After Suite");
        LOGGER.debug("Entering tearDown");
        Thread.sleep(1000);
        LOGGER.debug("Teardown Complete");
    }

}
