package one;

import one.dao.YmDao;
import one.pojo.YmPhone;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Map;

public class YmManager {

    private static SqlSession sqlSession;
    private static YmDao ymDao;


    private static YmDao init(){

        if (ymDao != null) {
            return ymDao;
        }

        String resource = "mybatis-config.xml";

        Reader reader = null;
        try {
            reader = Resources.getResourceAsReader(resource);
            SqlSessionFactory ssf = new SqlSessionFactoryBuilder().build(reader);

            sqlSession = ssf.openSession();

            YmDao ymDao = sqlSession.getMapper(YmDao.class);
            return ymDao;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static void destroyConnection() {
        sqlSession.close();

    }

    public static int updataTaskTime(Map<String,Object> map){
        YmDao ymDao = init();
        return ymDao.updateTaskTime(map);
    }

    public static List<YmPhone> getListPhone(){
        YmDao ymDao = init();
        return ymDao.selectPhoneList();
    }

    public static int insertYm(YmPhone ymPhone){
        YmDao ymDao = init();
        if (ymDao == null) {
            return 0;
        }
        int num = ymDao.insertPhone(ymPhone);

        sqlSession.commit();

        return num;
    }

}
;