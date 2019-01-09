package com.example.serivce;

import com.example.common.FileUtils;
import com.example.entity.FileBlock;
import com.example.entity.FileEntity;
import com.example.repository.FileEntityRepository;
import com.example.common.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author: xjc
 * @date 2018/11/20 15:33
 * @description
 **/
@Service
@Transactional
public class FileEntityService {

    @Autowired
    FileEntityRepository fileEntityRepository;

    public Response findById(String md5){
        if(md5==null||"".equals(md5))
            return Response.error("null");

        FileEntity fileEntity = null;
        try {
//        没找到会报错
            fileEntity = fileEntityRepository.findById(md5).get();
             return Response.ok("OK",fileEntity);
        } catch (Exception e) {
            System.out.println(FileEntityService.class+"找不到ID:::"+md5);
            return Response.error("null");
        }
    }

    public void upload(MultipartFile file, String chunk, String name,String uploadPath) {
        try {
//            上传片段
            FileUtils.uploaded(file,uploadPath);
//            成功存储数据到数据库
            FileBlock fileBlock = new FileBlock();


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void save(FileEntity fileEntity){

        fileEntityRepository.save(fileEntity);

    }


    public void update(String md5, String savePath) {

        FileEntity entity = fileEntityRepository.findById(md5).get();

        entity.setPath(savePath);

        fileEntityRepository.saveAndFlush(entity);

    }

    public Response findByUserId(String userId) {

        List<FileEntity> fileEntities = fileEntityRepository.findByUserId(userId);


        return Response.ok("ok",fileEntities);

    }


    public Response deleteByUserIdAndId(String userId, String md5) {

        FileEntity fileEntity=fileEntityRepository.findByUserIdAndId(userId,md5);

        if(fileEntity==null)
            return Response.error("没有找到文件");
        return Response.ok("OK",fileEntity);

    }
}
