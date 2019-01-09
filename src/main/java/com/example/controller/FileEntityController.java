package com.example.controller;

import com.example.common.FileUtils;
import com.example.common.Response;
import com.example.entity.FileBlock;
import com.example.entity.FileEntity;
import com.example.serivce.FileBlockService;
import com.example.serivce.FileEntityService;
import lombok.extern.slf4j.Slf4j;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * @author: xjc
 * @date 2018/11/20 15:49
 * @description
 **/
@RestController
@RequestMapping("/file/")
@Slf4j
public class FileEntityController {

    @Autowired
    protected FileEntityService service;

    @Autowired
    protected FileBlockService fileBlockService;


    @Value("${tempPath:G:\\upload\\temp}")
    protected String tempPath;

    @Value("${basePath:G:\\upload\\base}")
    protected String basePath;

    @GetMapping("index")
    public ModelAndView index() {
        return new ModelAndView("upload");
    }

    @GetMapping( "/get/{md5}")
    public Response file(@PathVariable String md5){

        Response result = service.findById(md5);

        return result;
    }


    @GetMapping("/list/{userId}")
    public Response getByUser(@PathVariable String userId){

        return service.findByUserId(userId);

    }

    @DeleteMapping("/{userId}/{md5}")
    public Response deleteByUser(@PathVariable String userId,@PathVariable String md5){

        return service.deleteByUserIdAndId(userId,md5);
    }

    /**
     * @author:xjc
     * @date:2018/11/20
     * @description:文件分片上传
     * @params:文件,整个文件的md5,片号,文件名,分片总数
     */
    @PostMapping("upload")
    public Response upload(@RequestParam(value = "file") MultipartFile file,
                           @RequestParam(value = "md5") String md5,
                           @RequestParam(value = "chunk", defaultValue = "0", required = false) String chunk,
                           @RequestParam(value = "name") String name,
                           @RequestParam(value = "chunks", defaultValue = "0", required = false) String chunks,
                           @RequestParam(value = "type") String type
                           ){

//        如果文件不足1M 则chunks为上传上来为0.修改chunks属性
        if("0".equals(chunks))
            chunks = "1";

//        临时文件储存路劲
        String uploadPath = tempPath + "\\###" + chunk + "###" + name;

        //            开始上传文件片段

        FileBlock fileBlock = new FileBlock();
        try {
            service.upload(file, chunk, name, uploadPath);

//            上传成功封装属性保存到数据库中


            fileBlock.setId(Sid.next());
            fileBlock.setMd5(md5);
            fileBlock.setChunk(chunk);
            fileBlock.setChunkSize(file.getSize());
            fileBlock.setPath(uploadPath);
            fileBlock.setName("###" + chunk + "###" + name);
            fileBlock.setCreated(new Date());
            fileBlock.setType(type);
            fileBlock.setChunks(chunks);
            fileBlock.setRealName(name);
//          存在并发环境,查询filblock是否已经存在
            if(fileBlockService.findByMd5AndChunk(md5,chunk)==null){
                synchronized (fileBlockService.getClass()){
                    if(fileBlockService.findByMd5AndChunk(md5,chunk)==null)
                        fileBlockService.save(fileBlock);
                }
            }

        } catch (Exception e) {
            log.error(e.getStackTrace().toString());
            return Response.error();
        }

        return Response.ok("ok",fileBlock);
    }

    /**
     * @author:xjc
     * @date:2018/11/20
     * @description:检查片段是否已经上传
     * @params:
     */
    @GetMapping("check")
    public boolean check(@RequestParam(value = "md5") String md5,
                         @RequestParam(value = "chunk") String chunk
                         ){
        //            判断大文件是否存已经合并并存在
        Response result = service.findById(md5);
        if(result.getStatus()==200)
            return true;

//        判断文件块是否已经上传
        FileBlock fileBlock = fileBlockService.findByMd5AndChunk(md5, chunk);
        if(fileBlock==null)

            return false;


        return true;
    }

    /**
     * @author:xjc
     * @date:2018/11/20
     * @description:合并片段
     * @params:
     */
    @PostMapping("merge")
    public Response merge(@RequestParam(value = "md5") String md5, HttpServletRequest request){
//        判断文件是否需要合并
        if(service.findById(md5).getStatus()==200)
            return Response.ok();

        if(md5==null||"".equals(md5))
            return Response.error();
//        查询所有md5片段集合(该集合是无顺序的),转换顺序
        List<FileBlock> list = fileBlockService.findByMd5(md5);
//        判断文件块总数是否相等
        if(list.size()!= Integer.parseInt(list.get(0).getChunks()))
            return Response.error();

        if(list.size()==0)
            return Response.error();
//        取源文件路径paths然后合并
        String[] paths=new String[list.size()];

        Long size=0L;

        for(int i=0;i<list.size();i++){
            FileBlock fileBlock=list.get(i);
            size+=fileBlock.getChunkSize();
            String path = fileBlock.getPath();
            Integer chunk = Integer.parseInt(fileBlock.getChunk());
            paths[chunk]=path;
        }

        FileBlock fileBlock=list.get(0);
        String name=fileBlock.getRealName();

//        保存路径
        String savePath=basePath+"\\"+name;

        try {
//            合并文件
            FileUtils.mergeFile(paths,savePath);
//            成功后保存至数据库中
            FileEntity file = new FileEntity();
            file.setId(md5);
            file.setPath(savePath);
            file.setChunks(paths.length);
            file.setName(name);
            file.setSize(size);
            file.setCreated(new Date());
            file.setType(fileBlock.getType());

//            设置User ------
//            request.getSession().getAttribute("user");
//            file.setUserId();

            file.setFix(name.substring(name.lastIndexOf(".")+1));

            service.save(file);

//            成功合并后删除文件块与和数据库信息
            fileBlockService.deleteByMd5(md5);
            return Response.ok("ok",file);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error();
        }



    }

}
