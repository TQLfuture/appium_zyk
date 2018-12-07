package one.exec;

import one.service.ZykService;

import java.net.MalformedURLException;

public class MainWhite {

    public void execZyk(){
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

    public static void main(String[] args) {
        new MainWhite().execZyk();
    }

    public String execTest(String a){
        System.out.printf("aa " + a);
        return a+"test";
    }


}
