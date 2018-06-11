package com.space.shiro.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * 权限
 * @author zhuzhe
 * @date 2018/6/4 15:27
 * @email 1529949535@qq.com
 */
@Data
public class Permissions implements Serializable {

    private static final long serialVersionUID = 2869115090144706877L;

    private Integer id;

    private String name;

    private Set<Role> roles;
}
