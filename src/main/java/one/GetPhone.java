package one;

import org.junit.Test;
import sun.net.www.http.HttpClient;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Logger;

import static one.Constant.ITEM_ID;
import static one.Constant.TOKEN;

public class GetPhone {
    public static Logger  logger = Logger.getLogger("getphone");

    public static String URL = "http://api.fxhyd.cn/UserInterface.aspx";

    //获取手机号码接口
    public static String GET_PHONE_API = URL+"?"+"action=getmobile&token="+TOKEN+"&itemid="+ITEM_ID;
    //释放手机号码
    public static String FREE_PHONE_API = URL+"?"+"action=release&token="+TOKEN+"&itemid="+ITEM_ID+"&mobile=";


   public static String getPhone(){
       logger.info("获取号码链接 = " + GET_PHONE_API);
       String result = one.HttpClient.doGet(GET_PHONE_API);
       if (result != null && result.contains("success")) {
           //获取手机
           result = result.replace("success|","");
           String phone = result.replace("\r\n","").trim();
           if (phone != "") {
               logger.info("phone = "+phone);
               return phone;
           }
       }
       return null;
   }


    public static boolean freePhone(String phone){

       String result = one.HttpClient.doGet(FREE_PHONE_API);
       if (result != null && result.contains("success")){
           return true;
       }

       return false;
    }


    @Test
    public void test(){
       getPhone();
    }

}
