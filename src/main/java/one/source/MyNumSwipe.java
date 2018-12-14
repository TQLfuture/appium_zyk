package one.source;


import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

public class MyNumSwipe {
    /**
     * This Method for swipe up
     *
     * @author zt
     * @param driver
     * @param during
     * @param num
     */
    public void swipeToUp(AndroidDriver<AndroidElement> driver,int during,int num){
        int width = driver.manage().window().getSize().width;//获取当前屏幕的宽度
        int height = driver.manage().window().getSize().height;//获取当前屏幕的高度
        for(int i = 0;i < num;i++){
           // driver.swipe(width/2, height*3/4, width/2, height/4, during);
        }
    }

    /**
     * This Method for swipe down
     *
     * @author zt
     * @param driver
     * @param during
     * @param num
     */
    public void swipeToDown(AndroidDriver<AndroidElement> driver,int during,int num){
        int width = driver.manage().window().getSize().width;//获取当前屏幕的宽度
        int height = driver.manage().window().getSize().height;//获取当前屏幕的高度
        for(int i = 0;i < num;i++){
           // driver.swipe(width/2, height/4, width/2, height*3/4, during);
        }
    }

    /**
     * This Method for swipe Left
     *
     * @author zt
     * @param driver
     * @param during
     * @param num
     */
    public void swipeToLeft(AndroidDriver<AndroidElement> driver,int during,int num){
        int width = driver.manage().window().getSize().width;//获取当前屏幕的宽度
        int height = driver.manage().window().getSize().height;//获取当前屏幕的高度
        for(int i = 0;i < num;i++){
         //   driver.swipe(width*3/4, height/2, width/4, height/2, during);
        }
    }

    /**
     * This Method for swipe right
     *
     * @author zt
     * @param driver
     * @param during
     * @param num
     */
    public void swipeToRight(AndroidDriver<AndroidElement> driver,int during,int num){
        int width = driver.manage().window().getSize().width;//获取当前屏幕的宽度
        int height = driver.manage().window().getSize().height;//获取当前屏幕的高度
        for(int i = 0;i < num;i++){
          //  driver.swipe(width/4, height/2, width*3/4, height/2, during);
        }
    }
}