package one.service;

import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.touch.offset.PointOption;
import one.GetPhone;
import one.GetSms;
import one.YmManager;
import one.pojo.YmPhone;
import one.source.AndroidDriverWait;
import one.source.ExpectedCondition;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import static one.Constant.YQM_LIST;

public class ZykService {

    public static Logger logger = Logger.getLogger("MainTest");

    private AndroidDriver driver;
    private String url;
    private String deviceName;

    private static int step = 0;

    String phoneOut = null;
    public static String yzmOut = null;
    String outYqm = null;
    int phoneNoCount = 0;
    public List<WebElement> LISE_EDIT;
    public boolean DRIVER_FLAG = true;
    //    private String DEVICE_NAME_ID = "b4e496df7d93";
    public boolean GET_PHONE_WEIXIN = false;


    public ZykService(String deviceName, String url) throws MalformedURLException {
        this.deviceName = deviceName;
        this.url = url;
        // "http://localhost:4725/wd/hub"
        init();
    }

    /**
     * 初始化方法
     */
    private void init() throws MalformedURLException {
        //设置自动化相关参数
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(CapabilityType.BROWSER_NAME, "");
        capabilities.setCapability("deviceName", this.deviceName);
        capabilities.setCapability("udid", this.deviceName);
        capabilities.setCapability("platformName", "Android");
        //设置安卓系统版本
        capabilities.setCapability("platformVersion", "7.1.2");
        //设置apk路径
        //capabilities.setCapability("app", app.getAbsolutePath());
        //设置app的主包名和主类名
        capabilities.setCapability("appPackage", "com.guya.yddrug");
        capabilities.setCapability("appActivity", ".LaunchActivity");
        capabilities.setCapability("noSign", "true");
        //每次启动时覆盖session，否则第二次后运行会报错不能新建session
        capabilities.setCapability("sessionOverride", true);
        capabilities.setCapability("newCommandTimeout", 300);
//        capabilities.setCapability("device", "Selendroid");
//        capabilities.setCapability("autoLaunch",false);
        //初始化
        driver = new AndroidDriver(new URL(this.url), capabilities);
        //全局等待时间
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        //收起键盘
        //driver.hideKeyboard();
    }


    public void logout() {
        if (driver != null) {
            logger.info("--进行销毁session-----------");
            driver.quit();
        }
    }

    /**
     * 验证是否为数字
     *
     * @param str
     * @return
     */
    public boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * 点击我的或者
     */
    private void clickWodeOrSkip() {
        String processEdsc = "【进入点击我的或者跳过的流程】-> ";
        if (step < 2) {
            List<WebElement> list = driver.findElementsByClassName("android.widget.TextView");
            for (WebElement webElement : list) {
                String text = null;
                try {
                    text = webElement.getText();
                    logger.info(text + "\n");
                    if ("立即登录".equals(text)) {
                        step = 3;
                    }
                    if (text != null && text != "" && isInteger(text)) {
                        step = 99;
                    }
                    if ("跳过".equals(text) || text.equals("我的")) {
                        if (text.equals("跳过")) {
                            logger.info(processEdsc+"进入登录====");
                            step = 1;
                        } else if (text.equals("我的")) {
                            logger.info(processEdsc+"进入我的界面+++++");
                            step = 2;
                        }
                        webElement.click();
                        break;
                    }
                } catch (Exception e) {
                    logger.info(processEdsc+"在点击我的或者跳过的步骤 = " + e.toString());
                }
            }
        }
    }

    private void loginOrLogout() {
        String processEdsc = "【进入点击立即登录或者退出登录的流程】-> ";
        if (step == 2) {
            //再次扫描
            List<WebElement> list2 = driver.findElementsByClassName("android.widget.TextView");
            for (WebElement webElement : list2) {
                try {
                    String text = webElement.getText();
                    logger.info(text + "\n");
                    if (text != "" && (isInteger(text) || text.equals("立即登录"))) {
                        if (text.equals("立即登录")) {
                            logger.info(processEdsc+"立即登录");
                            step = 3;
                        } else {
                            step = 99; // 需要退出登录
                            logger.info(processEdsc+"需要退出登录--------------");
                        }
                        webElement.click();
                        break;
                    }
                } catch (Exception e) {
                    logger.info(processEdsc+"异常 = "+e.toString());
                }
            }
        }
    }

    /**
     * 获取手机号  点击短信发送
     */
    private void getPhoneAndClickSms() throws InterruptedException {
        String processEdsc = "【进入获取手机号和点击验证码发送的流程】-> ";

        if (step == 3 || step == 100) {
            if (step == 100 || GET_PHONE_WEIXIN) {
                try {
                    logger.info(processEdsc+"先查找微信,重新获取号码");
                    //查找元素
                    List<WebElement> listWinxin = new AndroidDriverWait(driver, 10).until(new ExpectedCondition<List<WebElement>>() {
                        @Override
                        public List<WebElement> apply(AndroidDriver androidDriver) {
                            logger.info(processEdsc+"进行到微信界面查找=====");
                            return androidDriver.findElements(By.className("android.widget.TextView"));
                        }
                    });
                    for (WebElement w : listWinxin) {
                        try {
                            String text = w == null ? "空指针" : w.getText();
                            logger.info(processEdsc+"text = " + text);
                            if (text != null && text.contains("微信号")) {
                                Thread.sleep(5000);
                                logger.info(processEdsc+"从微信界面返回输入界面=====");
                                TouchAction action = new TouchAction(driver);
                                PointOption pointOption = PointOption.point(40, 134);
                                action.tap(pointOption).release().perform();
                                break;
                            }
                        } catch (Exception e) {
                            logger.info(processEdsc+"微信界面的错误====" + e.getMessage());
                        }
                    }
                } catch (Exception e) {
                    logger.info(processEdsc+"-----------------" + e.getMessage());
                }
            }
            List<WebElement> listEdit = new ArrayList<>();
            try {
                listEdit = new AndroidDriverWait(driver, 10).until(new ExpectedCondition<List<WebElement>>() {
                    @Override
                    public List<WebElement> apply(AndroidDriver androidDriver) {
                        logger.info(processEdsc+" 进入到获取输入框 ");
                        return androidDriver.findElements(By.className("android.widget.EditText"));
                    }
                });
            } catch (Exception e) {
                logger.info(processEdsc+"error = " + e.getMessage());
            }
            if (listEdit.size() == 0) {
                //进入点击微信
                try {
                    driver.hideKeyboard();
                } catch (Exception e) {
                    logger.info(processEdsc+"隐藏键盘失败 ==" + e.getMessage());
                }
                try {
                    Thread.sleep(5000);
                    logger.info(processEdsc+"进入到点击微信");
                    Map tap3 = new HashMap();
                    tap3.put("tapCount", new Double(2));
                    tap3.put("touchCount", new Double(1));
                    tap3.put("duration", new Double(0.5));
                    tap3.put("x", new Double(370));
                    tap3.put("y", new Double(1154));
                    driver.executeScript("mobile: tap", tap3);
                    GET_PHONE_WEIXIN = true;
                    return;
                } catch (Exception e) {
                    logger.info(processEdsc+"点击微信失败------");
                }
            }
            int editSize = listEdit.size();
            logger.info(processEdsc+"编辑框数据 = " + editSize);
            if (phoneNoCount >= 3) {
                logger.info(processEdsc+"无效次数太多，重新获取号码");
                phoneOut = null;
            }
            //获取号码
            if (phoneOut == null || step == 100) {
                logger.info(processEdsc+"进入获取号码的流程============");
                while (true) {
                    //获取号码
                    String phone = GetPhone.getPhone();
                    logger.info(processEdsc+"正在获取号码 = " + phone);
                    if (phone != null) {
                        phoneOut = phone;
                        if (step == 100) {
                            WebElement wEdit = listEdit.get(0);
                            wEdit.sendKeys(phoneOut);
                        }
                        break;
                    }
                    logger.info(processEdsc+" 在获取号码的流程中进行点击");
                    Thread.sleep(2000);
                    //随便点击
                    Map tap = new HashMap();
                    tap.put("tapCount", new Double(2));
                    tap.put("touchCount", new Double(1));
                    tap.put("duration", new Double(0.5));
                    tap.put("x", new Double(380));
                    tap.put("y", new Double(115));
                    driver.executeScript("mobile: tap", tap);
                    Thread.sleep(3000);
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
                List<WebElement> list = new AndroidDriverWait(driver,10).until(new ExpectedCondition<List<WebElement>>() {
                    @Override
                    public List<WebElement> apply(AndroidDriver androidDriver) {
                        return androidDriver.findElements(By.className("android.widget.TextView"));

                    }
                });
                if (list != null && list.size() > (4 + 1)) {
                    logger.info(processEdsc+"当前正触发获取验证码=====");
                    Map tap = new HashMap();
                    tap.put("tapCount", new Double(2));
                    tap.put("touchCount", new Double(1));
                    tap.put("duration", new Double(0.5));
                    tap.put("x", new Double(600));
                    tap.put("y", new Double(540));
                    driver.executeScript("mobile: tap", tap);

                    Thread.sleep(5000);
                    logger.info(processEdsc+"点击微信===");
                    Map tap3 = new HashMap();
                    tap3.put("tapCount", new Double(2));
                    tap3.put("touchCount", new Double(1));
                    tap3.put("duration", new Double(0.5));
                    tap3.put("x", new Double(370));
                    tap3.put("y", new Double(1154));
                    driver.executeScript("mobile: tap", tap3);
                    step = 4;
                }
            }
        }
    }

    private void getSmsInThread() {
        String processEdsc = "【线程获取短信流程】-> ";
        new Thread(new Runnable() {
            @Override
            public void run() {
                //只允许一个设置
                yzmOut = "SMS_IN_THREAD";
                int m = 0;
                while (true) {
                    //获取短信
                    String yzm = GetSms.getSms(phoneOut);
                    logger.info(processEdsc+ "正在获取短信 = " + yzm);
                    if (yzm != null) {
                        logger.info(processEdsc+"已经成功获取到短信 = " + yzm);
                        yzmOut = yzm;
                        break;
                    }
                    try {
                        Thread.sleep(2000);
                        logger.info(processEdsc+" 在获取验证码进行点击");
                        //随便点击
                        Map tap = new HashMap();
                        tap.put("tapCount", new Double(2));
                        tap.put("touchCount", new Double(1));
                        tap.put("duration", new Double(0.5));
                        tap.put("x", new Double(380));
                        tap.put("y", new Double(115));
                        driver.executeScript("mobile: tap", tap);
                        Thread.sleep(3000);
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

    private void inputYzm() throws InterruptedException {
        String processEdsc = "【输入验证码流程】-> ";
        //进行验证码输入
        if (step == 4) {
            outYqm = YQM_LIST.get(new Random().nextInt(YQM_LIST.size() - 1));
            //全部输入完成进行登录按钮
            //获取验证码
            if (yzmOut == null) {
                this.getSmsInThread();

                int weixinCount = 0;
                while (true) {
                    if (yzmOut != null && yzmOut.equals("CHANGE_PHONE")) {
                        logger.info(processEdsc+"====需要换号码===");
                        step = 100;
                        yzmOut = null;
                        break;
                    }
                    logger.info("循环等待验证码信息 = " + yzmOut);
                    if (yzmOut != null && yzmOut != "SMS_IN_THREAD") {
                        logger.info(processEdsc+"=========进入输入流程==============");
                        try {
                            logger.info(processEdsc+"先查找是否跳转到微信登录界面中=");
                            //查找元素
                            List<WebElement> listWinxin = new AndroidDriverWait(driver, 10).until(new ExpectedCondition<List<WebElement>>() {
                                @Override
                                public List<WebElement> apply(AndroidDriver androidDriver) {
                                    logger.info(processEdsc+"====现在进行到微信界面查找=====");
                                    return androidDriver.findElements(By.className("android.widget.TextView"));
                                }
                            });
                            for (WebElement w : listWinxin) {
                                try {
                                    String text = w == null ? "空指针" : w.getText();
                                    logger.info(processEdsc+"内容 text = " + text);
                                    if (text != null && text.contains("微信号")) {
                                        weixinCount++;
                                        Thread.sleep(5000);
                                        logger.info(processEdsc+"进行返回微信=====");
                                        TouchAction action = new TouchAction(driver);
                                        PointOption pointOption = PointOption.point(40, 134);
                                        action.tap(pointOption).release().perform();
                                        break;
                                    }
                                } catch (Exception e) {
                                    logger.info(processEdsc+"微信界面的错误====" + e.getMessage());
                                }
                            }

                            List<WebElement> listInputEdit = new AndroidDriverWait(driver, 10).until(new ExpectedCondition<List<WebElement>>() {
                                @Override
                                public List<WebElement> apply(AndroidDriver androidDriver) {
                                    logger.info(processEdsc+"现在正在进行获取填充框方法中=======");
                                    return androidDriver.findElements(By.className("android.widget.EditText"));
                                }
                            });
                            if (listInputEdit != null && listInputEdit.size() == 3) {
                                WebElement w1 = listInputEdit.get(1);
                                logger.info(processEdsc+"++++++++++++++进行输入++++++++++++++++");
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
                                logger.info(processEdsc+"======清除号码======");
                                break;
                            }
                        } catch (Exception e) {
                            logger.info(processEdsc+"在输入数据的过程中发生异常 ==== " + e.getMessage());
                        }
                    }
                    Thread.sleep(3000);
                    if (step == 5) {
                        logger.info(processEdsc+"输入已经完成===============");
                        break;
                    }

                }
            }

        }

    }

    private void clickLoginOrLogout(){
        String processEdsc = "【退出系统或者进行登录】-> ";
        //进行登录
        if (step == 5) {
            //进行验证码点击
            List<WebElement> list = new AndroidDriverWait(driver,10).until(new ExpectedCondition<List<WebElement>>() {
                @Override
                public List<WebElement> apply(AndroidDriver androidDriver) {
                    return androidDriver.findElements(By.className("android.widget.TextView"));

                }
            });
            for (WebElement webElement : list) {
                try {
                    String text = webElement.getText();
                    System.out.printf(text + "\n");
                    if ("登录".equals(text)) {
                        try {
                            driver.hideKeyboard();
                        } catch (Exception e) {
                            logger.info(processEdsc+"在登录时隐藏键盘 = "+e.getMessage());
                        }
                        webElement.click();
                        step = 2;
                        break;
                    }
                } catch (Exception e) {
                    System.out.printf(e.toString());
                }
            }
        }

        // 进行系统退出 退出界面
        if (step == 99) {
            //进行验证码点击
            List<WebElement> list = new AndroidDriverWait(driver,10).until(new ExpectedCondition<List<WebElement>>() {
                @Override
                public List<WebElement> apply(AndroidDriver androidDriver) {
                    return androidDriver.findElements(By.className("android.widget.TextView"));

                }
            });
            if (list != null && list.size() >= (11+1)) {
                WebElement wLogOut = list.get(11);
                logger.info(processEdsc+"现在需要退出重新登录注册===") ;
                step = 2;
                wLogOut.click();
            }
        }
    }

    public void regist () throws Exception {
        /**
         * step 1 跳过
         * step 2 我的
         * step 3 立即登录
         */
        while (true) {
            logger.info("现在的步骤是 step = " + step);
//            Map<String,Object> map = new HashMap<>();
//            map.put("deviceName",this.deviceName);
//            YmManager.updataTaskTime(map);

            this.clickWodeOrSkip();

            this.loginOrLogout();

            this.getPhoneAndClickSms();

            this.inputYzm();

            this.clickLoginOrLogout();
        }

    }
}
