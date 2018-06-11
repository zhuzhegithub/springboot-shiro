package com.space.shiro.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * 权限
 * @author zhuzhe
 * @date 2018/6/4 15:26
 * @email 1529949535@qq.com
 */
@Data
public class Role implements Serializable{

    private static final long serialVersionUID = -7018588078056943942L;

    private Integer id;

    private String name;

    private Set<User> users = new HashSet<>();
    private Set<Permissions> permissions = new HashSet<>();
}
