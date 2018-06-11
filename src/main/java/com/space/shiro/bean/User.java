package com.space.shiro.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author zhuzhe
 * @date 2018/6/4 15:25
 * @email 1529949535@qq.com
 */
@Data
public class User implements Serializable {

    private static final long serialVersionUID = -8894803927963266646L;

    private Integer id;

    private String username;

    private String password;

    private Set<Role> roles = new HashSet<>();
}
