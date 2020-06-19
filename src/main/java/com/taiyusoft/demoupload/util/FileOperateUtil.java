package com.taiyusoft.demoupload.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * @Description 提供文件 创建 复制  删除 剪切
 * @Author wq
 * @CreateDate 2020/4/9
 */
public class FileOperateUtil {

    /**
     * 创建
     *
     * @param srcPath 创建路径
     * @return
     */
    public static boolean createPath(String srcPath) {
        boolean flag;
        File file = new File(srcPath);
        if (file.exists()) {
            //文件存在,创建成功
            flag = true;
        } else {
            flag = file.mkdirs();
        }
        return flag;
    }

    /**
     * 复制文件
     *
     * @param InPath  资源文件夹
     * @param OutPath 目标文件夹
     * @return
     */
    public static boolean copyFile(String InPath, String OutPath) {
        boolean flag = false;
        File in = new File(InPath);
        File out = new File(OutPath);
        if (out.exists() && out.isFile()) {
            System.out.println("目标目录下已有同名文件!");
            return false;
        }

        if (in.exists()) {
            try {
                Files.copy(in.toPath(), out.toPath());
                flag = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

    /**
     * 删除
     *
     * @param path
     * @return
     */
    public static boolean deleteGeneralFile(String path) {
        boolean flag = false;

        File file = new File(path);
        if (!file.exists()) { // 文件不存在
            System.out.println("要删除的文件不存在！");
        }

        if (file.isDirectory()) { // 如果是目录，则单独处理
            flag = deleteDirectory(file.getAbsolutePath());
        } else if (file.isFile()) {
            flag = deleteFile(file);
        }

        if (flag) {
            System.out.println("删除文件或文件夹成功!");
        }

        return flag;
    }

    private static boolean deleteDirectory(String path) {
        boolean flag = true;
        File dirFile = new File(path);
        if (!dirFile.isDirectory()) {
            return flag;
        }
        File[] files = dirFile.listFiles();
        for (File file : files) { // 删除该文件夹下的文件和文件夹
            if (file.isFile()) {
                flag = deleteFile(file);
            } else if (file.isDirectory()) {
                flag = deleteDirectory(file.getAbsolutePath());
            }
            if (!flag) { // 只要有一个失败就立刻不再继续
                break;
            }
        }
        flag = dirFile.delete(); // 删除空目录
        return flag;
    }

    private static boolean deleteFile(File file) {
        return file.delete();
    }

    /**
     * 剪切
     *
     * @param srcPath 源文件
     * @param destDir 复制文件地址
     * @return
     */
    public static boolean cutGeneralFile(String srcPath, String destDir) {
        if (!copyFile(srcPath, destDir)) {
            System.out.println("复制失败导致剪切失败!");
            return false;
        }
        if (!deleteGeneralFile(srcPath)) {
            System.out.println("删除源文件(文件夹)失败导致剪切失败!");
            return false;
        }
        return true;
    }

}
