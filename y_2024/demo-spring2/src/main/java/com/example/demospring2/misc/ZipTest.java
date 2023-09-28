package com.example.demospring2.misc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2023-08-23
 */
public class ZipTest {

    private static void addToZip(File file, ZipOutputStream zos) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        ZipEntry zipEntry = new ZipEntry(file.getName());
        zos.putNextEntry(zipEntry);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = fis.read(buffer)) > 0) {
            zos.write(buffer, 0, length);
        }

        fis.close();
        zos.closeEntry();
    }

    public static void test1() throws IOException {
        File file = File.createTempFile("mock", ".csv");
        FileOutputStream fos = new FileOutputStream(file);
        fos.write("hh".getBytes());
        fos.close();

        File file1 = File.createTempFile("test", ".zip");
        FileOutputStream fos1 = new FileOutputStream(file1);
        ZipOutputStream zos1 = new ZipOutputStream(fos1);

        addToZip(file, zos1);
        zos1.close();
        fos1.close();
        System.out.println(111);

    }

    public static void test2() {
        String zipFile = "/Users/admin/Desktop/test.zip"; // ZIP文件路径
        String[] sourceFiles = {"/Users/admin/Desktop/mock3862945416231462375.csv"}; // 要压缩的文件列表

        try {
            FileOutputStream fos = new FileOutputStream(zipFile);
            ZipOutputStream zos = new ZipOutputStream(fos);

            // 将文件写入ZIP文件中
            for (String file : sourceFiles) {
                ZipEntry ze = new ZipEntry(file);
                zos.putNextEntry(ze);

                // 读取文件内容并写入ZIP文件中
                FileInputStream fis = new FileInputStream(file);
                byte[] buffer = new byte[1024];
                int len;
                while ((len = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, len);
                }
                fis.close();

                // 关闭当前条目并定位到下一个条目
                zos.closeEntry();
            }

            // 关闭ZipOutputStream对象
            zos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        test1();
//        test2();
    }
}
