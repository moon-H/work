package com.cssweb.mytest.rename;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by lenovo on 2016/8/15.
 */
public class ReNameForAndroidIos {

    private int spilitIndex = -1;
    private static final String ORG_FILE_DIR = "D:/rename/org";

    private static final String TEMP_FILE_DIR = "D:/rename/temp";
    private static final String TEMP_FILE_DIR1 = "D:/rename/temp/2";
    private static final String TEMP_FILE_DIR2 = "D:/rename/temp/3";
    private static final String TEMP_FILE_DIR3 = "D:/rename/temp/4";

    private static final String NEW_FILE_DIR = "D:/rename/new";
    private static final String NEW_FILE_DIR_ANDROID = "D:/rename/new/Android";
    private static final String NEW_FILE_DIR_IOS = "D:/rename/new/Android";
    private static final String NEW_FILE_DIR1 = "D:/rename/new/Android/2";
    private static final String NEW_FILE_DIR2 = "D:/rename/new/Android/3";
    private static final String NEW_FILE_DIR3 = "D:/rename/new/Android/4";


    static File tempDir = new File(TEMP_FILE_DIR);
    static File tempDir1 = new File(TEMP_FILE_DIR1);
    static File tempDir2 = new File(TEMP_FILE_DIR2);
    static File tempDir3 = new File(TEMP_FILE_DIR3);

    static File newDir = new File(NEW_FILE_DIR);
    static File newAndroidDir = new File(NEW_FILE_DIR_ANDROID);
    static File newIosDir = new File(NEW_FILE_DIR_IOS);
    static File newDir1 = new File(NEW_FILE_DIR1);
    static File newDir2 = new File(NEW_FILE_DIR2);
    static File newDir3 = new File(NEW_FILE_DIR3);
    private static int spitIndex = -1;
    private static ArrayList<File> tempList = new ArrayList<>();

    public static void main(String[] args) {
        tempList.clear();
        makeTempDir();


        //        File orgFile = new File("D:/rename/org/1/1_01.png");
        //        System.out.println("#### is exist = " + orgFile.exists());
        //        orgFile.renameTo(new File(TEMP_FILE_DIR + File.separator + "1" + File.separator + 1 + "_" + 1 + ".png"));


        File fileDir = new File(ORG_FILE_DIR);
        System.out.println("---- path0  = " + fileDir.getPath());
        File[] fileList = fileDir.listFiles();

        for (File file : fileList) {
            if (file.isDirectory()) {
                String dirName = file.getName();
                System.out.println("---- path1  = " + file.getPath());
                File[] chileList = file.listFiles();
                for (File file1 : chileList) {
                    FileInputStream b = null;
                    try {

                        String fileName = file1.getName();
                        String[] nameSplitArray = fileName.split("\\.");
                        String[] frontSplitArray = nameSplitArray[0].split("_");
                        int orgPrefix = Integer.parseInt(frontSplitArray[0]);

                        b = new FileInputStream(file1);
                        SimpleImageInfo imageInfo = new SimpleImageInfo(b);
                        //                        System.out.println("## img info : " + imageInfo);
                        if (imageInfo.getWidth() != 256 && orgPrefix == 1) {
                            tempList.add(file1);
                            //                            int orgSuffix = Integer.parseInt(frontSplitArray[1]);
                            //                            System.out.println("---- spitIndex : " + spitIndex);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            b.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                int min = -1;
                for (int i = 0; i < tempList.size(); i++) {
                    File file1 = tempList.get(i);
                    String fileName = file1.getName();
                    String[] nameSplitArray = fileName.split("\\.");
                    String[] frontSplitArray = nameSplitArray[0].split("_");
                    int orgPrefix = Integer.parseInt(frontSplitArray[0]);
                    int orgSuffix = Integer.parseInt(frontSplitArray[1]);
                    if (i == 0) {
                        min = orgSuffix;
                    }
                    if (orgSuffix < min) {
                        min = orgSuffix;
                    }

                }
                spitIndex = min;
                System.out.println("----- spitIndex : " + spitIndex);

                renameForAndroid(chileList, spitIndex, dirName);
                tempList.clear();
            }

        }

    }

    private static void renameForAndroid(File[] fileListSize, int index, String newPath) {
        System.out.println("&&&renameForAndroid  path : " + newPath + " size = " + fileListSize.length);

        for (int i = 0; i < fileListSize.length; i++) {
            //            File file = fileListSize[i];
            //            if (file.isDirectory()) {
            //                continue;
            //            }
            String fileName = fileListSize[i].getName();
            System.out.println("&&& fileName = " + fileName);
            String[] nameSplitArray = fileName.split("\\.");
            System.out.println("&&&  nameSplit front = " + nameSplitArray[0]);
            String[] frontSplitArray = nameSplitArray[0].split("_");
            System.out.println("&&&  frontSplitArray front = " + frontSplitArray[0]);
            int orgPrefix = Integer.parseInt(frontSplitArray[0]);
            int orgSuffix = Integer.parseInt(frontSplitArray[1]);
            System.out.println("&&&  orgPrefix = " + orgPrefix + " orgSuffix = " + orgSuffix);
            System.out.println("&&&  orgSuffix % SPLIT_INDEX = " + orgSuffix % index);

            int preIndex = orgSuffix / index;
            int newPrefix;
            int newSuffix;
            if (orgSuffix % index == 0) {
                newPrefix = preIndex;
                newSuffix = index;
            } else {
                newPrefix = preIndex + 1;
                newSuffix = orgSuffix % index;
            }

            System.out.println("&&&  newPrefix = " + newPrefix + " newSuffix = " + newSuffix);
            File orgFile = fileListSize[i];
            System.out.println("&&&  orgFile path = " + orgFile.getPath() + " name = " + orgFile.getName());
            File newFile = new File(TEMP_FILE_DIR + File.separator + newPath + File.separator + newPrefix + "_" + newSuffix + ".png");
            boolean newFileExist = newFile.exists();
            if (newFileExist) {
                boolean deleteResult = newFile.delete();
                System.out.println("&&&  deleteResult = " + deleteResult);
            }
            System.out.println("&&&  newFile exist = " + newFileExist);
            boolean reNameResult = orgFile.renameTo(newFile);
            System.out.println("&&& reNameResult = " + reNameResult);
            System.out.println("&&& renameToFile = " + TEMP_FILE_DIR + File.separator + newPath + File.separator + newPrefix + "_" + newSuffix + ".png");
            System.out.println("&&& 新路径 = " + newPath);
            Renamefile.rename(TEMP_FILE_DIR + File.separator + newPath, NEW_FILE_DIR_ANDROID + File.separator + newPath);
        }
    }


    private static void makeTempDir() {
        if (tempDir.exists()) {
            File[] list1 = tempDir.listFiles();
            for (File file : list1) {
                file.delete();
            }
            boolean deleteResult = tempDir.delete();
            System.out.println("#### deleteResult = " + deleteResult);
        }
        tempDir.mkdir();
        tempDir1.mkdir();
        tempDir2.mkdir();
        tempDir3.mkdir();

        if (newDir.exists()) {
            File[] list1 = newDir.listFiles();
            for (File file : list1) {
                file.delete();
            }
            boolean deleteResult = newDir.delete();
            System.out.println("#### deleteResult = " + deleteResult);
        }
        newDir.mkdir();
        newAndroidDir.mkdir();
        newIosDir.mkdir();
        newDir1.mkdir();
        newDir2.mkdir();
        newDir3.mkdir();
    }
}
