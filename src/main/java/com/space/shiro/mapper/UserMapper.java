package com.space.shiro.mapper;

import com.space.shiro.bean.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @author zhuzhe
 * @date 2018/6/4 15:49
 * @email 1529949535@qq.com
 */
@Component
@Mapper
public interface UserMapper {

    User queryUserName(String userName);
}
