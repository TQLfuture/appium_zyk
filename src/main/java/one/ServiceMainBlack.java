package one;

import one.service.ZykService;

import java.net.MalformedURLException;

public class ServiceMainBlack {

    public static void main(String[] args) {
        String url = "http://localhost:4725/wd/hub";
        String deviceName = "b4e496df7d93";
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
