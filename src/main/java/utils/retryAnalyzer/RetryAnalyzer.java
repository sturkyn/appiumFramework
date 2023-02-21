package utils.retryAnalyzer;

import utils.PropKeys;
import utils.PropertiesUtils;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {

    private int counter = 0;
    private final int retryLimit = PropertiesUtils.getIntValue(PropKeys.RETRY_LIMIT.getPropName());

    @Override
    public boolean retry(ITestResult result) {

        if(counter < retryLimit)
        {
            counter++;
            return true;
        }
        return false;
    }
}

