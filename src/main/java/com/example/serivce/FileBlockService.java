package com.example.serivce;

import com.example.common.FileUtils;
import com.example.entity.FileBlock;
import com.example.repository.FileBlockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author: xjc
 * @date 2018/11/20 20:40
 * @description
 **/
@Service
@Transactional
public class FileBlockService {

    @Autowired
    protected FileBlockRepository repository;

    public void save(FileBlock fileBlock){

        repository.save(fileBlock);

    }


    public List<FileBlock> findByMd5(String md5){

        return repository.findByMd5(md5);
    }

    public FileBlock findByMd5AndChunk(String md5,String chunk){

        return  repository.findByMd5AndChunk(md5,chunk);
    }

    public void deleteByMd5(String md5) {

//        删除文件./
        List<FileBlock> list = repository.findByMd5(md5);

        for (FileBlock fileBlock : list) {
            FileUtils.delete(fileBlock.getPath());
        }

//        删除数据库信息
        repository.deleteByMd5(md5);

    }
}
