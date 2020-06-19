package com.taiyusoft.demoupload.controller;


import com.taiyusoft.demoupload.util.FileOperateUtil;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.channels.FileChannel;


/**
 * @Description
 * @Author wq
 * @CreateDate 2020/6/18
 */
@RestController
public class TestUpload {

    @RequestMapping(value = "upload")
    public String upload(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request) throws IOException {

        boolean multipartContent = ServletFileUpload.isMultipartContent(request);
        if (!multipartContent) return "请上传文件";

        String fileDir = "C:\\Users\\Ad\\Desktop";
//        文件名称
        String fileName = request.getParameter("fileName");
//        总块数量
        String blockNum = request.getParameter("blockNum");
//        当前块
        String blockIndex = request.getParameter("blockIndex");
//      防止文件顺序读取时错乱
//      11会在2前面
        int nameLength = blockNum.length() - blockIndex.length();
        for (int j = 0; j < nameLength; j++) {
            blockIndex = 0 + blockIndex;
        }
//        文件保存路径


        String path = fileDir+File.separator + fileName;
        if (FileOperateUtil.createPath(path)) {
            File file1 = new File(path + File.separator + blockIndex + "-" + fileName);
            file1.createNewFile();
            file.transferTo(file1);
        }
        if (blockNum.equals(blockIndex)) {
            boolean b = mergeFiles(path,  fileDir+File.separator + "merge-" + fileName);
            System.out.println(" - 合成状态:" + b);
        }
        return blockIndex;
    }

    public static boolean mergeFiles(String path, String resultPath) throws IOException {
        File file = new File(path);
        File[] files = file.listFiles();

        for (int i = 0; i < files.length; i++) {
            if (!files[i].exists() || !files[i].isFile()) {
                System.out.println(files[i] + "--不是文件--");
                return false;
            }
        }

        File resultFile = new File(resultPath);
        FileChannel resultFileChannel = null;
        try {
            resultFileChannel = new FileOutputStream(resultFile, true).getChannel();
            for (int i = 0; i < files.length; i++) {
                FileChannel blk = new FileInputStream(files[i]).getChannel();
                resultFileChannel.transferFrom(blk, resultFileChannel.size(), blk.size());
                blk.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (resultFileChannel != null) resultFileChannel.close();
        }
//        删除文件
        return FileOperateUtil.deleteGeneralFile(path);
    }

}


