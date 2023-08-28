package JavaAuto.Capstone1;

import org.testng.annotations.*;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

public class testCodeCopy {

    public WebDriver driver;
    public dataReaderClass dataReader;

    @BeforeSuite
    @Parameters("browser")
    public void driverSetup(@Optional("chrome") String browser) {
        if (browser.equalsIgnoreCase("chrome")) {
            System.setProperty("webdriver.chrome.driver", "C:\\Users\\MRIDUL\\Downloads\\chromedriver-win32\\chromedriver-win32\\chromedriver.exe");
            //the below 2 lines are code are for chrome versions >=116
            ChromeOptions options = new ChromeOptions(); 
            options.addArguments("--remote-allow-origins=*");
            driver = new ChromeDriver(options);
            
        }
     // calling the dataReaderClass method from 'dataReaderClass' generic function
        dataReader = new dataReaderClass("data.properties");
    }
    
    //----------------------GENERIC FUNCTION TO TAKE SCREENSHOT FOR FAILED TEST CASES--------------------------------
    @AfterMethod
    public void ssMethod(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            // Capture and attach screenshot on test failure
        	captureScreenshot(result.getMethod().getMethodName());
        }
    }

    public void captureScreenshot(String methodName) {
        try {
            File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String screenshotPath = "screenshots/" + methodName + ".png";
            FileUtils.copyFile(screenshotFile, new File(screenshotPath));

            // Attach the screenshot to the test report
            Reporter.log("<br><img src='" + screenshotPath + "' height='200' width='300'/><br>");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test(priority = 1)
    public void titleVerify1() throws InterruptedException {
        // Step 1: Launch the URL and verify the title
        driver.get("https://www.saucedemo.com/");
        Assert.assertEquals(driver.getTitle(), "Swag Labs");
        Thread.sleep(2000);
    }

    @Test(priority = 2)
    @Parameters({"validUsername", "validPassword"})
        public void validLoginVerify(String username, String password) throws InterruptedException {
        // Step 2: Login with provided credentials
        driver.findElement(By.id("user-name")).sendKeys(username);
        Thread.sleep(2000);
        driver.findElement(By.id("password")).sendKeys(password);
        Thread.sleep(2000);
        driver.findElement(By.id("login-button")).click();
        Thread.sleep(2000);
    }

 
    @Test(priority = 3)
    public void titleVerify2() throws InterruptedException {
        driver.get("https://www.saucedemo.com/");
        Assert.assertEquals(driver.getTitle(), "Swag Labs");
        Thread.sleep(2000);
    }

    //------------------------------THIS TEST WILL GET FAILED AS INVALID CREDENTIALS ARE GIVEN--------------------------------
    @Test(priority = 4)
    @Parameters({"invalidUsername", "invalidPassword"})
        public void invalidLoginVerify(String username, String password) throws InterruptedException {
        driver.findElement(By.id("user-name")).sendKeys(username);
        Thread.sleep(2000);
        driver.findElement(By.id("password")).sendKeys(password);
        Thread.sleep(2000);
        driver.findElement(By.id("login-button")).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5)); 
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("error-message")));

     // Verification of failed login
       String errorMessage = driver.findElement(By.className("error-message")).getText();
        Assert.assertTrue(errorMessage.contains("Epic sadface: Username and password do not match any user in this service"));
        Thread.sleep(2000); 
        

    }

    @AfterSuite
    public void afterSuite() {
        driver.quit();
    }
}
