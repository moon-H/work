package com.cssweb.mytest.zip;

/**
 * Created by lenovo on 2016/10/10.
 */
public class ZipTest {
    static String inputPath = "C:\\Users\\lenovo\\Desktop\\temp\\ziptest\\4444.zip";
    static String zipPath = "C:\\Users\\lenovo\\Desktop\\temp\\ziptest\\4500";
    static String zipOutPath = "C:\\Users\\lenovo\\Desktop\\temp\\ziptest\\out\\4500.zip";
    static String output = "C:\\Users\\lenovo\\Desktop\\temp\\ziptest\\out";

    public static void main(String[] args) {
        //        ZipUtils zip = new ZipUtils();
        //        try {
        //            ZipUtils.UnZipFolder(inputPath, output);
        //        } catch (Exception e) {
        //            e.printStackTrace();
        //        }
        try {
            ZipUtils.ZipFolder(zipPath, zipOutPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
