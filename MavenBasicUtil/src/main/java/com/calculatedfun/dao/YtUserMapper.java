package com.calculatedfun.dao;


import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.calculatedfun.dto.UserExample;
import com.calculatedfun.dto.Users;

public interface YtUserMapper {
    int countByExample(UserExample example);

    int deleteByExample(UserExample example);

    int deleteByPrimaryKey(String id);

    int insert(Users record);

    int insertSelective(Users record);

    List<Users> selectByExample(UserExample example);

    Users selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") Users record, @Param("example") UserExample example);

    int updateByExample(@Param("record") Users record, @Param("example") UserExample example);

    int updateByPrimaryKeySelective(Users record);

    int updateByPrimaryKey(Users record);
    /**
     *获取用户信息
     * @param user
     * @return
     */
    List<Users> getUserList(@Param("name")String name,@Param("usercode")String usercode,@Param("loginname")String loginname);
    /**
     * 根据用户编号查询用户信息
     */
    Users getByUserno(Integer usercode);
}