package com.space.shiro.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * 功能
 * @author zhuzhe
 * @date 2018/6/4 15:27
 * @email 1529949535@qq.com
 */
@Data
public class Module implements Serializable {

    private Integer id;

    private String name;

    private Set<Role> roles;
}
