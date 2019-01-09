package com.example.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: xjc
 * @date 2018/11/20 15:12
 * @description
 **/
@Getter
@Setter
@Entity
@Table(name="file")
public class FileEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private String id;

    //    文件类型	type
    @Column(name="type")
    private String type;
    //    文件后缀	fix
    @Column(name="fix")
    private String fix;
    //    文件名	name
    @Column(name="name")
    private String name;
    //    创建者	creator
    @Column(name="user_id")
    private Long userId;
    //    文件大小	size
    @Column(name="size")
    private Long size;
    //    文件地址	path
    @Column(name="path")
    private String path;
    //    备注信息	remark
    @Column(name="remark")
    private String remark;
    //    其他	other
    @Column(name="other")
    private String other;
//  块数
    private Integer chunks;

    private Date created;


}
