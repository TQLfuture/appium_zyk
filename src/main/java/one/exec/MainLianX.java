package one.exec;

import one.service.ZykService;

import java.net.MalformedURLException;

public class MainLianX {


    public void execZyk(){
        String url = "http://localhost:4727/wd/hub";
        String deviceName = "62919053";
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
        new MainLianX().execZyk();
    }

}
