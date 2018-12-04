package one;

import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.touch.offset.PointOption;
import one.pojo.YmPhone;
import one.source.AndroidDriverWait;
import one.source.ExpectedCondition;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebElement;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import static one.Constant.YQM_LIST;

public class MainTest {

    public static Logger logger = Logger.getLogger("MainTest");

    private AndroidDriver driver;

    public static int step = 0;

    String phoneOut = null;
    public static String yzmOut = null;
    String outYqm = null;
    int phoneNoCount = 0;
    public List<WebElement> LISE_EDIT;
    public boolean DRIVER_FLAG = true;
    public AndroidDriver tempDriver;
    public  String DEVICE_NAME_ID = "98e19a167cf4";
    public boolean GET_PHONE_WEIXIN = false;
    @Before
    public void before() throws MalformedURLException {

        //设置自动化相关参数
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(CapabilityType.BROWSER_NAME, "");
//        capabilities.setCapability("deviceName","zyk");

        capabilities.setCapability("deviceName", DEVICE_NAME_ID);
        capabilities.setCapability("udid", DEVICE_NAME_ID);

        capabilities.setCapability("platformName","Android");

        //设置安卓系统版本
        capabilities.setCapability("platformVersion", "7.1.2");
        //设置apk路径
        //capabilities.setCapability("app", app.getAbsolutePath());
        //设置app的主包名和主类名
        capabilities.setCapability("appPackage", "com.guya.yddrug");
        capabilities.setCapability("appActivity", ".LaunchActivity");
        capabilities.setCapability("noSign","true");
        //每次启动时覆盖session，否则第二次后运行会报错不能新建session
        capabilities.setCapability("sessionOverride", true);
        capabilities.setCapability("newCommandTimeout",600);
//        capabilities.setCapability("device", "Selendroid");
//        capabilities.setCapability("autoLaunch",false);
         //初始化
        driver = new AndroidDriver(new URL("http://localhost:4723/wd/hub"), capabilities);

        //全局等待时间
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        //收起键盘
        //driver.hideKeyboard();


    }

    @After
    public void after(){

        driver.quit();
    }

    public  boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }


    public void performTapAction(WebElement elementToTap,int x,int y) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        HashMap<String, Double> tapObject = new HashMap<String, Double>();
        tapObject.put("x", (double) x); // in pixels from left
        tapObject.put("y", (double) y); // in pixels from top
        tapObject.put("element", Double.valueOf(((RemoteWebElement) elementToTap).getId()));
        js.executeScript("mobile: tap", tapObject);
    }


    public void searchClick(String keyword) {
        List<WebElement> list = driver.findElementsByClassName("android.widget.TextView");
        for (WebElement webElement:list) {
            try {
                String text = webElement.getText();
                System.out.printf(text +"\n");
                if (text.equals(keyword)) {
                    webElement.click();
                    break;
                }
            } catch (Exception e ) {
                System.out.printf(e.toString());
            }

        }
    }

    @Test
    public void test2() throws InterruptedException {
        /**
         * step 1 跳过
         * step 2 我的
         * step 3 立即登录
         */
        while (true) {
            if (step < 2) {
                List<WebElement> list = driver.findElementsByClassName("android.widget.TextView");
                for (WebElement webElement : list) {
                    try {
                        String text = webElement.getText();
                        logger.info(text + "\n");
                        if ("立即登录".equals(text)) {
                            step = 3;

                        }
                        if (text != null && text != "" && isInteger(text)) {
                            step = 99;

                        }
                        if ("跳过".equals(text) || text.equals("我的")) {
                            if (text.equals("跳过")) {
                                logger.info("进入登录====");
                                step = 1;
                            } else if (text.equals("我的")) {
                                logger.info("进入我的界面+++++");
                                step = 2;
                            }
                            webElement.click();
                            break;
                        }
                    } catch (Exception e) {
                        System.out.printf(e.toString());
                    }

                }
            }

            if (step == 2) {
                //再次扫描
                List<WebElement> list2 = driver.findElementsByClassName("android.widget.TextView");
                int d = 0;
                for (WebElement webElement : list2) {
                    try {
                        String text = webElement.getText();
                        logger.info(text + "\n");
                        if (text != "" && (isInteger(text) || text.equals("立即登录"))) {
                            if (text.equals("立即登录")) {
                                logger.info("立即登录");
                                step = 3;
                            } else {
                                step = 99; // 需要退出登录
                                logger.info("需要退出登录--------------");
                            }
                            logger.info("两者的index = " + d);
                            webElement.click();
                            break;
                        }
                    } catch (Exception e) {
                        System.out.printf(e.toString());
                    }
                    d ++;

                }
            }

            if (step == 3 || step == 100) {

                if (step == 100 || GET_PHONE_WEIXIN) {
                    try {
                        logger.info("先查找微信,重新获取号码");
                        //查找元素
                        List<WebElement> listWinxin = new AndroidDriverWait(driver, 30).until(new ExpectedCondition<List<WebElement>>() {
                            @Override
                            public List<WebElement> apply(AndroidDriver androidDriver) {
                                logger.info("进行到微信界面查找=====");
                                return androidDriver.findElements(By.className("android.widget.TextView"));
                            }
                        });
                        for (WebElement w : listWinxin) {
                            try {
                                String text = w == null ? "空指针" : w.getText();
                                logger.info("text = " + text);
                                if (text != null && text.contains("微信号")) {
                                    Thread.sleep(5000);
                                    logger.info("进行返回微信=====");
                                    TouchAction action = new TouchAction(driver);
                                    PointOption pointOption = PointOption.point(40, 134);
                                    action.tap(pointOption).release().perform();
                                    GET_PHONE_WEIXIN = false;
                                    break;
                                }
                            } catch (Exception e) {
                                logger.info("微信界面的错误====" + e.getMessage());
                            }
                        }
                    } catch (Exception e) {
                        logger.info("-----------------" + e.getMessage());
                    }
                }

                List<WebElement> listEdit = new ArrayList<>();
                try {
                     listEdit = new AndroidDriverWait(driver,30).until(new ExpectedCondition<List<WebElement>>() {
                         @Override
                         public List<WebElement> apply(AndroidDriver androidDriver) {
                             logger.info("进行人到等待获取的方法");
                             return androidDriver.findElements(By.className("android.widget.EditText"));
                         }
                     });
                } catch (Exception e) {
                    logger.info("error = " + e.getMessage());
                }

                if (listEdit.size() == 0) {
                    //进入点击微信
                    try {
                        driver.hideKeyboard();
                    } catch (Exception e) {
                        logger.info("隐藏键盘失败 ==" + e.getMessage());
                    }
                    try {

                        Thread.sleep(5000);
                        logger.info("触发之后===");
                        Map tap3 = new HashMap();
                        tap3.put("tapCount", new Double(2));
                        tap3.put("touchCount", new Double(1));
                        tap3.put("duration", new Double(0.5));
                        tap3.put("x", new Double(370));
                        tap3.put("y", new Double(1154));
                        driver.executeScript("mobile: tap", tap3);
                        GET_PHONE_WEIXIN = true;
                        continue;
                    } catch (Exception e) {
                        logger.info("点击微信失败------");
                    }
                }

               // List<WebElement> listEdit = driver.findElementsByClassName("android.widget.EditText");
                int editSize = listEdit.size();
                logger.info("编辑框数据 = " + editSize);
                if (phoneNoCount >=3) {
                    logger.info("无效次数太多，重新获取号码");
                    phoneOut = null;
                }
                //获取号码
                if (phoneOut == null || step == 100) {
                    logger.info("进入好吗获取==============");
                    while (true) {
                        //获取号码
                        String phone = GetPhone.getPhone();
                        logger.info("获取好吗 = ");
                        if (phone != null) {
                            phoneOut = phone;
                            if (step == 100) {
                                WebElement wEdit = listEdit.get(0);
                                wEdit.sendKeys(phoneOut);
                            }
                            break;
                        }
                        Thread.sleep(5000);
                    }
                }

                if (editSize == 2) {

                    WebElement wEdit = listEdit.get(0);
                    wEdit.sendKeys(phoneOut);
                    phoneNoCount += 1;
                } else if (editSize == 3) {
                    LISE_EDIT = listEdit;
                    phoneNoCount = 0;
                    //进行验证码点击
                    List<WebElement> list = driver.findElementsByClassName("android.widget.TextView");
                    if (list != null && list.size() >(4+1)) {
                        WebElement wYzm = list.get(4);
                        logger.info("当前正触发获取验证码=====");
//                        logger.info("前面的 = " + driver.getPageSource());
//                        LISE_EDIT.get(2).sendKeys("A1123");


                        Map tap = new HashMap();
                        tap.put("tapCount", new Double(2));
                        tap.put("touchCount", new Double(1));
                        tap.put("duration", new Double(0.5));
                        tap.put("x", new Double(600));
                        tap.put("y", new Double(540));
                        driver.executeScript("mobile: tap", tap);

                        Thread.sleep(5000);
                        logger.info("触发之后===");
                        Map tap3 = new HashMap();
                        tap3.put("tapCount", new Double(2));
                        tap3.put("touchCount", new Double(1));
                        tap3.put("duration", new Double(0.5));
                        tap3.put("x", new Double(370));
                        tap3.put("y", new Double(1154));
                        driver.executeScript("mobile: tap", tap3);

                       // logger.info("进行返回微信=====");
//                        Map tapBack = new HashMap();
//                        tapBack.put("tapCount", new Double(2));
//                        tapBack.put("touchCount", new Double(1));
//                        tapBack.put("duration", new Double(0.5));
//                        tapBack.put("x", new Double(40));
//                        tapBack.put("y", new Double(134));
//                        driver.executeScript("mobile    : tap", tapBack);
//                        Thread.sleep(5000);

                        if (false) {

                            //获得新的driver
                            try {
                                //隐藏
//                                driver.hideKeyboard();
                            } catch (Exception e) {
                                logger.info("隐藏键盘出错"+e.getMessage());
                            }
                            //点击微信
//                            Thread.sleep(3000);
                            Map tap2 = new HashMap();
                            tap2.put("tapCount", new Double(2));
                            tap2.put("touchCount", new Double(1));
                            tap2.put("duration", new Double(0.5));
                            tap2.put("x", new Double(370));
                            tap2.put("y", new Double(1154));
                            tempDriver.executeScript("mobile    : tap", tap2);
                            Thread.sleep(3000);
                            //点击返回
//                            Map tapBack = new HashMap();
//                            tapBack.put("tapCount", new Double(2));
//                            tapBack.put("touchCount", new Double(1));
//                            tapBack.put("duration", new Double(0.5));
//                            tapBack.put("x", new Double(63));
//                            tapBack.put("y", new Double(118));
//                            tempDriver.executeScript("mobile    : tap", tapBack);
                            DRIVER_FLAG = false;
                        }
//                        wYzm.click();
                        step = 4;
//                        logger.info("后面的 = " + driver.getPageSource());
                    }
                }
            }
            //进行验证码输入
            if (step == 4) {
                outYqm = YQM_LIST.get(new Random().nextInt(YQM_LIST.size()-1));
                //全部输入完成进行登录按钮
                //获取验证码
                if (yzmOut == null) {

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //只允许一个设置
                            yzmOut = "SMS_IN_THREAD";
                            int m = 0;
                            while (true) {
                                //获取短信
                                String yzm = GetSms.getSms(phoneOut);
                                logger.info("正在获取短信 = " + yzm);
                                if (yzm != null) {
                                    logger.info("已经成功获取到短信 = " + yzm);
                                    yzmOut = yzm;
                                    break;
                                }
                                try {
                                    Thread.sleep(5000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                if (m == 20) {
                                    yzmOut = "CHANGE_PHONE";
                                    break;
                                }
                                m += 1;
                            }
                        }
                    }).start();
                }


                while (true) {
                    if (yzmOut != null && yzmOut.equals("CHANGE_PHONE")) {
                        logger.info("需要换号码===");
                        step = 100;
                        yzmOut = null;
                        break;
                    }

                    logger.info("循环等待验证码信息 = " + yzmOut);
                    if (yzmOut != null && yzmOut != "SMS_IN_THREAD") {
                        logger.info("============进入输入流程==============");
                        try {
                            logger.info("先查找微信");
                            boolean weixinFlag = false;
                            //查找元素
                            List<WebElement> listWinxin = new AndroidDriverWait(driver,60).until(new ExpectedCondition<List<WebElement>>() {
                                @Override
                                public List<WebElement> apply(AndroidDriver androidDriver) {
                                    logger.info("进行到微信界面查找=====");
                                    return androidDriver.findElements(By.className("android.widget.TextView"));
                                }
                            });
                            for (WebElement w:listWinxin) {
                                try {
                                    String text = w == null? "空指针":w.getText();
                                    logger.info("text = " + text);
                                    if (text != null && text.contains("微信")) {
                                        Thread.sleep(5000);
                                        logger.info("进行返回微信=====");
                                        TouchAction action = new TouchAction(driver);
                                        PointOption pointOption = PointOption.point(40,134);
                                        action.tap(pointOption).release().perform();
                                        weixinFlag = true;
                                        break;
                                    }
                                } catch (Exception e) {
                                    logger.info("微信界面的错误===="+e.getMessage());
                                }
                            }

//                              List<WebElement> listInputEdit = driver.findElements(By.className("android.widget.EditText"));
//                              logger.info("kkk = " + driver.getPageSource());
                            List<WebElement> listInputEdit = new AndroidDriverWait(driver,60).until(new ExpectedCondition<List<WebElement>>() {
                                @Override
                                public List<WebElement> apply(AndroidDriver androidDriver) {
                                    logger.info("进入到等待方法中获取元素");
                                    return androidDriver.findElements(By.className("android.widget.EditText"));
                                }
                            });
                            //List<WebElement> listInputEdit = driver.findElementsByClassName("android.widget.EditText");
                            if (listInputEdit != null && listInputEdit.size() == 3) {
                                WebElement w1 = listInputEdit.get(1);
                                logger.info("++++++++++++++++进行输入++++++++++++++++++");
                                w1.sendKeys(yzmOut);
                                WebElement w2 = listInputEdit.get(2);
                                w2.sendKeys(outYqm);

                                YmPhone ymPhone = new YmPhone();
                                ymPhone.setPhone(phoneOut);
                                ymPhone.setYzm(yzmOut);
                                ymPhone.setMa(outYqm);
                                YmManager.insertYm(ymPhone);
                                phoneOut = null;
                                yzmOut = null;
                                DRIVER_FLAG = true;
                                step = 5;
                                logger.info("清除号码======");
                                break;
                            }
                        } catch (Exception e) {
                            logger.info("输入数据 = "+e.getMessage());
                        }
                    }
                    Thread.sleep(3000);
                    if (step == 5) {
                        logger.info("输入已经完成===============");
                        break;
                    }

                }
            }

            //进行登录
            if (step == 5) {
                List<WebElement> list = driver.findElementsByClassName("android.widget.TextView");
//                if (list != null && list.size() >=(5+1)) {
//                    WebElement wLogin = list.get(5);
//                    logger.info("正在触发登录");
//                    wLogin.click();
//                    step = 2;
//                }
                int b = 0;
                for (WebElement webElement : list) {
                    try {
                        String text = webElement.getText();
                        System.out.printf(text + "\n");
                        if ("登录".equals(text)) {
                            logger.info("登录的index = " + b);
                            try {
                                driver.hideKeyboard();
                            } catch (Exception e) {
                                logger.info(e.getMessage());
                            }
                            webElement.click();
                            step = 2;
                            break;
                        }
                    } catch (Exception e) {
                        System.out.printf(e.toString());
                    }
                    b ++;
                }
            }

            // 进行系统退出 退出界面
            if (step == 99) {
                List<WebElement> list = driver.findElementsByClassName("android.widget.TextView");
                if (list != null && list.size() >= (11+1)) {
                    WebElement wLogOut = list.get(11);
                    logger.info("退出登录index = ") ;
                    step = 2;

                    wLogOut.click();
                }
            }
            logger.info("现在的步骤是 = " + step);

        }
    }
        // 第一步 是跳过


        // 第二步是我的


        @Test
        public void test1 () throws InterruptedException {
        //driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS)
//            String loc_text = "new UiSelector().text('我的')";
//            //List list =  driver.findElementsByName(loc_text);
//            //System.out.printf(list.toString());
//
//            //查找 我的
//            //driver.findElementByXPath("//android.widget.TextView[contians(@text,'我的')]").click();
//            //TouchAction action = new TouchAction(driver);
//            //action.tap(620, 1210).perform();
//
//            //driver.findElementsByAndroidUIAutomator("new UiSelector().className("+classname+").index("+index+")");
//
//            while (true) {
//                Thread.sleep(1000);
//                if (step < 2) {
//                    List<WebElement> list = driver.findElementsByClassName("android.widget.TextView");
//                    for (WebElement webElement : list) {
//                        try {
//                            String text = webElement.getText();
//                            System.out.printf(text + "\n");
//                            if ("跳过".equals(text) || text.equals("我的")) {
//                                if (text.equals("跳过")) {
//                                    step = 1;
//                                } else if (text.equals("我的")) {
//                                    step = 2;
//                                }
//                                webElement.click();
//                                break;
//                            }
//                        } catch (Exception e) {
//                            System.out.printf(e.toString());
//                        }
//
//                    }
//                }
//
//                if (step < 2) {
//                    continue;
//                }
//
//
//                if (step != 9 || step != 10) {
//
//                    //再次扫描
//                    List<WebElement> list2 = driver.findElementsByClassName("android.widget.TextView");
//                    for (WebElement webElement : list2) {
//                        try {
//                            String text = webElement.getText();
//                            System.out.printf(text + "\n");
//                            if (text != "" && (isInteger(text) || text.equals("立即登录"))) {
//                                if (text.equals("立即登录")) {
//                                    step = 10;
//                                } else {
//                                    step = 9; // 需要退出登录
//                                }
//                                webElement.click();
//                                break;
//                            }
//                        } catch (Exception e) {
//                            System.out.printf(e.toString());
//                        }
//
//                    }
//                }
//
//                if (step == 10) {
//                    List<WebElement> listEdit = driver.findElementsByClassName("android.widget.EditText");
//                    int editSize = listEdit.size();
//                    System.out.printf("编辑框 = " + editSize);
//                    if (editSize == 2) {
//                        WebElement wEdit = listEdit.get(0);
//                        wEdit.clear();
//                        wEdit.sendKeys("15555455516");
//                    } else if (editSize == 3) {
//                        listEditWeb = listEdit;
//                        step = 8;
//                    }
//                    continue;
//                }
//
//
//                if (step == 8) {
//                    //设置验证码
//                    WebElement yzmW = listEditWeb.get(1);
//                    yzmW.clear();
//                    yzmW.sendKeys("123456");
//                    //设置邀请码
//                    WebElement yqmW = listEditWeb.get(2);
//                    yqmW.clear();
//                    yqmW.sendKeys("6RY50");
//                    step = 7;
//                }
//
//
//                if (step == 7) { //开始进行登录
//                    //再次扫描
//                    List<WebElement> list4 = driver.findElementsByClassName("android.widget.TextView");
//                    for (WebElement webElement : list4) {
//                        try {
//                            String text = webElement.getText();
//                            System.out.printf(text + "\n");
//                            if (text != "" && (isInteger(text) || text.equals("登录"))) {
//                                if (text.equals("登录")) {
//                                    System.out.printf("按钮是否可编辑 = " + webElement.isEnabled());
//                                    step = 99;
//                                    webElement.click();
//                                    break;
//                                }
//
//                            }
//                        } catch (Exception e) {
//                            System.out.printf(e.toString());
//                        }
//
//                    }
//                }
//
//
//                if (step == 99) {
//                    List<WebElement> list3 = driver.findElementsByClassName("android.widget.TextView");
//                    for (WebElement webElement : list3) {
//                        try {
//                            String text = webElement.getText();
//                            System.out.printf(text + "\n");
//                            if (text.equals("退出登录")) {
//                                webElement.click();
//                                step = 2;
//                                break;
//                            }
//                        } catch (Exception e) {
//                            System.out.printf(e.toString());
//                        }
//
//                    }
//                }
//
//
//            }
//
//
//            //System.out.printf(list.size()+"");
//
//            //String source = driver.getPageSource();
//            //System.out.println(source);
//
//
//        }
//

    }

}
