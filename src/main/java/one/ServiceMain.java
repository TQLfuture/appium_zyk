package one;

import one.service.ZykService;

import java.net.MalformedURLException;

public class ServiceMain {

    public static void main(String[] args) {
        String url = "http://localhost:4723/wd/hub";
        String deviceName = "98e19a167cf4";
        ZykService zykService = null;
        try {
             zykService = new ZykService(deviceName,url);
             zykService.regist();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (zykService != null){
                zykService.logout();
            }
        }
    }
}
