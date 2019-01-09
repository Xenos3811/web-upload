package com.example.repository;

import com.example.entity.FileBlock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author: xjc
 * @date 2018/11/20 20:36
 * @description
 **/
public interface FileBlockRepository extends JpaRepository<FileBlock,String> {

    List<FileBlock> findByMd5(String md5);

    FileBlock findByMd5AndChunk(String md5,String chunk);

    void deleteByMd5(String md5);

}
