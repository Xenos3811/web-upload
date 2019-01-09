package com.example.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: xjc
 * @date 2018/11/20 17:29
 * @description
 **/
@Entity
@Getter
@Setter
@Table(name = "file_block")
public class FileBlock implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String id;
//    块下标
    private String chunk;
//    块大小
    private Long chunkSize;
//    块标识
    private String md5;
//    块路径
    private String path;
//    块名称
    private String name;
//    块类型
    private String type;
//    块总数
    private String chunks;
//    文件真实名称
    private String realName;

    private Date created;
}
