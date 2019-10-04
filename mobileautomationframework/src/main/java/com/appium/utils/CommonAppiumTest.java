package com.appium.utils;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.HasOnScreenKeyboard;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.LoadLibs;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.interactions.Interaction;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import com.common.utils.CustomLogger;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CommonAppiumTest {

	public AppiumDriver<MobileElement> driver;
	public CustomLogger log;


	public CommonAppiumTest(AppiumDriver<MobileElement> driver, CustomLogger log) {
		this.driver = driver;
		this.log = log;
	}

	public void waitForPageToLoad(MobileElement id) throws Exception {
		WebDriverWait wait = new WebDriverWait(driver, 20);
		wait.until(ExpectedConditions.elementToBeClickable(id));
	}

	public void waitForElementToDisAppear(String id) throws Exception{
		WebDriverWait wait = new WebDriverWait(driver, 20);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(id)));
	}

	public void waitForElementToAppear(String locatorXpath, MobileElement waitForEle) throws Exception{
		WebDriverWait wait = new WebDriverWait(driver, 20);
		waitForEle = (MobileElement) wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(locatorXpath)));
	}


	public boolean isMobileElementDisplayed(MobileElement ele) {
		boolean isElementPresent;
		try{
			WebDriverWait wait = new WebDriverWait(driver, 30);
			MobileElement screenWait = (MobileElement) wait.until(ExpectedConditions.visibilityOf(ele));
			isElementPresent = screenWait.isDisplayed();
			log.info("MobileElement is displayed");
			return isElementPresent;	
		}catch(Exception e){
			isElementPresent = false;
			log.error(e.getMessage());
			Assert.fail("MobileElement is not found/displayed");
			return isElementPresent;
		} 
	}

	public boolean isMobileElementEnabled(MobileElement ele) {
		boolean isElementPresent;
		try{
			WebDriverWait wait = new WebDriverWait(driver, 20);
			MobileElement screenWait = (MobileElement) wait.until(ExpectedConditions.visibilityOf(ele));
			isElementPresent = screenWait.isEnabled();
			log.info("MobileElement is enabled");
			return isElementPresent;	
		}catch(Exception e){
			isElementPresent = false;
			log.error(e.getMessage());
			Assert.fail("MobileElement is not found/enabled");
			return isElementPresent;
		} 
	}



	public boolean isMobileElementSelected(MobileElement ele) {
		boolean isElementPresent;
		try{
			WebDriverWait wait = new WebDriverWait(driver, 20);
			MobileElement screenWait = (MobileElement) wait.until(ExpectedConditions.visibilityOf(ele));
			isElementPresent = screenWait.isSelected();
			log.info("MobileElement is selected");
			return isElementPresent;	
		}catch(Exception e){
			isElementPresent = false;
			log.error(e.getMessage());
			Assert.fail("MobileElement is not found/selected");
			return isElementPresent;
		} 
	}

	public MobileElement waitForElement(MobileElement arg) {
		MobileElement el = null;
		try {
			waitForPageToLoad(arg);
			el = arg;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return el;
	}

	public boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			log.info("Element found");
			return true;
		} catch (NoSuchElementException e) {
			log.error(e.getMessage());
			Assert.fail("Element not found");
			return false;
		}
	}

	public void swipeLeftUntilTextExists(String expected) {
		do {
			swipeLeft();
		} while (!driver.getPageSource().contains(expected));
	}

	@SuppressWarnings("rawtypes")
	public void swipeRight() {
		Dimension size = driver.manage().window().getSize();
		int startx = (int) (size.width * 0.9);
		int endx = (int) (size.width * 0.20);
		int starty = size.height / 2;
		new TouchAction(driver).press(PointOption.point(startx, starty))
		.waitAction(WaitOptions.waitOptions(Duration.ofSeconds(2)))
		.moveTo(PointOption.point(endx,starty)).release().perform();
	}

	@SuppressWarnings("rawtypes")
	public void swipeLeft() {
		Dimension size = driver.manage().window().getSize();
		int startx = (int) (size.width * 0.8);
		int endx = (int) (size.width * 0.20);
		int starty = size.height / 2;
		new TouchAction(driver).press(PointOption.point(startx, starty))
		.waitAction(WaitOptions.waitOptions(Duration.ofSeconds(2)))
		.moveTo(PointOption.point(endx,starty)).release();
	}


	public void setContext(String context) throws Exception{
		Set<String> contextNames = driver.getContextHandles();
		if (contextNames.contains(context)) {
			driver.context(context);
			log.info("Context changed successfully");
		} else {
			log.error(context + "not found on this page");
		}
		log.info("Current context" + driver.getContext());
	}

	public void clickBackButton() throws Exception{
		driver.navigate().back();
	}

	public String getCurrentMethodName() throws Exception {
		return Thread.currentThread().getStackTrace()[2].getMethodName();
	}


	public boolean isKeyBoardDisplayed() {
		return ((HasOnScreenKeyboard) driver).isKeyboardShown();
	}

	public void hideKeyboard() throws Exception {
		try {
			if (isKeyBoardDisplayed()) {
				driver.hideKeyboard();
				log.info("Keyboard closed successfully");
				if(isKeyBoardDisplayed()==true) {
					MobileElement iOSKeyboardDoneBtn = driver.findElementByAccessibilityId("Done");
					if(isMobileElementDisplayed(iOSKeyboardDoneBtn)) {
						click(iOSKeyboardDoneBtn);
						log.info("Keyboard closed successfully");
					}
				} else {
					log.info("No keyboard displayed");
				}
			}
		} catch (Exception e) {
			log.error("Unable to find element");
		}
	}


	public void click(MobileElement locator) throws Exception {
		MobileElement mobElement = waitForElement(locator);
		try {
			//			if (mobElement.isDisplayed()) 
			if(isMobileElementDisplayed(mobElement))
			{
				mobElement.click();
				log.info("Successfully clicked on element");
				//				driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
			} else {
				log.error("Unable to identify MobileElement");
				Assert.fail("MobileElement is not displayed");
			}
		} catch (Exception e) {
			try {
				mobElement.click();
			} catch (Exception e1) {
				Assert.fail("Test Failed");
			}
		}
	}

	public String sendKeys(MobileElement locator, String keysToSend) {
		MobileElement mobElement = waitForElement(locator);
		try {
			if(isMobileElementDisplayed(mobElement)) {
				mobElement.clear();
				mobElement.sendKeys(keysToSend);	
				log.info("Successfully entered text on textfield");
				hideKeyboard();
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			captureScreenShot();
			Assert.fail("Unable to send keys");
		}
		return keysToSend;
	}

	public String captureScreenShot() {
		String destFile = null;
		String ScreenshotName = "Screenshots";
		File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy__hh_mm_ssaa");
			new File(ScreenshotName).mkdirs();
			destFile = dateFormat.format(new Date())+".png";
			FileUtils.copyFile(src, new File(ScreenshotName +"/" +destFile));
			log.info("Screenshot captured successfully");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		return destFile;
	}

	public void resetApp() throws Exception{
		driver.resetApp();
	}

	public String getText(MobileElement locator)
	{
		String text = null;
		try {
			MobileElement mobElement = waitForElement(locator);
			text = mobElement.getText();
			log.info(mobElement.getText());
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
		return text;
	}


	public void swipeScreen(String AccessibilityID, int swipeStartXCoordinate, int swipeStartYCoordinate, int swipeEndXCoordinate, int swipeEndYCoordinate) {
		WebDriverWait wait = new WebDriverWait(driver, 20);
		wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.id(AccessibilityID)));
		PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
		Interaction moveToStart = finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), swipeStartXCoordinate, swipeStartYCoordinate);
		Interaction pressDown = finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg());
		Interaction moveToEnd = finger.createPointerMove(Duration.ofMillis(1000), PointerInput.Origin.viewport(), swipeEndXCoordinate, swipeEndYCoordinate);
		Interaction pressUp = finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg());

		org.openqa.selenium.interactions.Sequence swipe = new org.openqa.selenium.interactions.Sequence(finger, 0);
		swipe.addAction(moveToStart);
		swipe.addAction(pressDown);
		swipe.addAction(moveToEnd);
		swipe.addAction(pressUp);

		driver.perform(Arrays.asList(swipe));
	}

	public void tapOnElement(MobileElement element, int xCoordinate, int ycoOrdinate) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("element", ((RemoteWebElement) element).getId());
		params.put("x", xCoordinate);
		params.put("y", ycoOrdinate);
		driver.executeScript("mobile: tap", params);
	}

	public void tapOnElementBasedOnCoOrdinates(int xCoordinate, int ycoOrdinate) {
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("x", xCoordinate);
		args.put("y", ycoOrdinate);
		driver.executeScript("mobile: tap", args);
	}

	public void tapOnElementBasedOnDuration(MobileElement ele) {
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("element", ((MobileElement) ele).getId());
		args.put("duration", 1.5);
		driver.executeScript("mobile: touchAndHold", args);
	}

	public void clickOnAlertAcceptBtn() {
		driver.switchTo().alert().accept();
		log.info("Clicked on accept button on alert popup");
	}

	public void clickOnAlertDismissBtn() {
		driver.switchTo().alert().dismiss();
	}

	public AppiumDriver<MobileElement> getDriver() {
		return driver;
	}

	public String readToastMsg() throws TesseractException {
		String imageName = captureScreenShot();
		String result = null;
		File imageFile = new File("./Screenshots/", imageName);
		System.out.println("The image is: " +imageFile);
		ITesseract instance = new Tesseract();
		File tessDataFolder = LoadLibs.extractTessResources("tessdata"); 
		instance.setDatapath(tessDataFolder.getAbsolutePath()); 
		result = instance.doOCR(imageFile);
		log.info("The converted image is: " +result);
		return result;
	}

	@SuppressWarnings("rawtypes")
	public void swipeDown() {	
		Dimension size = driver.manage().window().getSize();
		int startx = size.width/2;
		int starty = (int) (size.height * 0.5);
		int endy = (int) (size.height * 0.2);
		//		int startx = 666;
		//		int starty = 1455;
		//		int endy = 165;
		new TouchAction(driver).press(PointOption.point(startx, starty))
		.waitAction(WaitOptions.waitOptions(Duration.ofSeconds(2)))
		.moveTo(PointOption.point(startx,endy)).release().perform();
	}





}
