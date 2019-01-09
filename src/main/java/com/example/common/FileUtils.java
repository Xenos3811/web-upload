package com.example.common;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;

/**
 * @author: xjc
 * @date 2018/11/20 19:01
 * @description
 **/
public class FileUtils {

    public static void uploaded( MultipartFile file,
//                                        final String md5,
//                                       final String guid,
//                                       final String chunk,
                                       final String uploadFolderPath
//                                       final String fileName,
//                                       final String ext,
//                                       final String filePath)
    )throws Exception {
        File uploadFile = new File(uploadFolderPath);
        if(uploadFile.exists()){
            uploadFile.delete();
        }
        byte[] data = readInputStream(file.getInputStream());

        try (FileOutputStream outStream = new FileOutputStream(uploadFile)) {//写入数据
            outStream.write(data);
            outStream.flush();

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        //创建一个Buffer字符串
        byte[] buffer = new byte[1024];
        //每次读取的字符串长度，如果为-1，代表全部读取完毕
        int len;
        //使用一个输入流从buffer里把数据读取出来
        while ((len = inStream.read(buffer)) != -1) {
            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }
        //关闭输入流
        inStream.close();
        //把outStream里的数据写入内存
        return outStream.toByteArray();
    }

    /**
     * @author:xjc
     * @date:2018/11/20
     * @description:合并文件
     * @params:
     */
    public static void mergeFile(final String[] paths, final String finalSavePath)
            throws Exception {
        if(paths.length==1){
//            不需要合并
            InputStream is= new FileInputStream(paths[0]);
            saveStreamToFile(is,finalSavePath);
            return ;
        }
//        SequenceInputStream s ;
////        InputStream s1 = new FileInputStream(list.get(0).getPath());
////        InputStream s2 = new FileInputStream(list.get(1).getPath());
////        s = new SequenceInputStream(s1, s2);
////        for (int i=2;i<list.size();i++) {
////            InputStream s3 = new FileInputStream(list.get(i).getPath());
////            s = new SequenceInputStream(s, s3);
////        }
////
////        //通过输出流向文件写入数据
////        saveStreamToFile(s, finalSavePath);


        FileOutputStream output = new FileOutputStream(new File(finalSavePath));
        WritableByteChannel targetChannel = output.getChannel();

        for(int i =0; i<paths.length; i++){
            FileInputStream input = new FileInputStream(paths[i]);
            FileChannel inputChannel = input.getChannel();

            inputChannel.transferTo(0, inputChannel.size(), targetChannel);

            inputChannel.close();
            input.close();
        }
        targetChannel.close();
        output.close();

    }
    /**
     * 从stream中保存文件
     *
     * @param inputStream inputStream
     * @param filePath    保存路径
     * @throws Exception 异常 抛异常代表失败了
     */
    public static void saveStreamToFile( final InputStream inputStream,
                                         final String filePath)
            throws Exception {
        /*创建输出流，写入数据，合并分块*/
        OutputStream outputStream = new FileOutputStream(filePath);
        byte[] buffer = new byte[1024];
        int len = 0;
        try {
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
                outputStream.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            outputStream.close();
            inputStream.close();
        }
    }


    /**
     * 删除文件，可以是文件或文件夹
     *
     * @param fileName
     *            要删除的文件名
     * @return 删除成功返回true，否则返回false
     */
    public static boolean delete(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println("删除文件失败:" + fileName + "不存在！");
            return false;
        } else {
            if (file.isFile())
                return deleteFile(fileName);
            else
                return deleteDirectory(fileName);
        }
    }

    /**
     * 删除单个文件
     *
     * @param fileName
     *            要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                System.out.println("删除单个文件" + fileName + "成功！");
                return true;
            } else {
                System.out.println("删除单个文件" + fileName + "失败！");
                return false;
            }
        } else {
            System.out.println("删除单个文件失败：" + fileName + "不存在！");
            return false;
        }
    }

    /**
     * 删除目录及目录下的文件
     *
     * @param dir
     *            要删除的目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String dir) {
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!dir.endsWith(File.separator))
            dir = dir + File.separator;
        File dirFile = new File(dir);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            System.out.println("删除目录失败：" + dir + "不存在！");
            return false;
        }
        boolean flag = true;
        // 删除文件夹中的所有文件包括子目录
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                flag = FileUtils.deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
            // 删除子目录
            else if (files[i].isDirectory()) {
                flag = FileUtils.deleteDirectory(files[i]
                        .getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag) {
            System.out.println("删除目录失败！");
            return false;
        }
        // 删除当前目录
        if (dirFile.delete()) {
            System.out.println("删除目录" + dir + "成功！");
            return true;
        } else {
            return false;
        }
    }
}
