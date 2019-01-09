package com.example.repository;

import com.example.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author: xjc
 * @date 2018/11/20 15:29
 * @description
 **/

//@RepositoryDefinition(domainClass = FileEntity.class, idClass = Long.class)
public interface FileEntityRepository extends JpaRepository<FileEntity,String> {

    List<FileEntity> findByUserId(String userId);

    void deleteByUserId(String userId);

    FileEntity findByUserIdAndId(String userId, String md5);
}
