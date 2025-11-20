package com.yupi.springbootinit.model.entity;

import lombok.Data;

import java.io.Serializable;

/**
 *图片
 *
 * */
@Data
public class Picture implements Serializable {

    private String url;

    private String title;

    private static final long serialVersionUID = 1L;
}
