package one.dao;

import one.pojo.YmPhone;

import java.util.List;
import java.util.Map;

public interface YmDao {

    public List<YmPhone> selectPhoneList();

    public int insertPhone(YmPhone ymPhone);

    public int updateTaskTime(Map<String,Object> map);
}
