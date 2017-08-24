package com.cssweb.mytest.rename;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 安卓修改路网图图片名称
 *
 * @param SPLIT_INDEX 每行分割数量
 */
public class RenamefilePS {
    private static final String FILE_DIR = "D:/rename";
    private static final String NEW_FILE_DIR = "D:/rename/new";
    private static final int SPLIT_INDEX = 13;

    public static void main(String[] args) {
        // -------------
        File fileDir = new File(FILE_DIR);
        System.out.println("#### is dir = " + fileDir.isDirectory());
        File[] fileList = fileDir.listFiles();
        int fileListSize = fileList.length;
        System.out.println("#### fileListSize = " + fileListSize);

        for (int i = 0; i < fileListSize; i++) {
            File file = fileList[i];
            if (file.isDirectory()) {
                continue;
            }
            String fileName = fileList[i].getName();
            System.out.println("#### fileName = " + fileName);
            String[] nameSplitArray = fileName.split("\\.");
            System.out.println("####  nameSplit front = " + nameSplitArray[0]);
            String[] frontSplitArray = nameSplitArray[0].split("_");
            System.out.println("####  frontSplitArray front = " + frontSplitArray[0]);
            int orgPrefix = Integer.parseInt(frontSplitArray[0]);
            int orgSuffix = Integer.parseInt(frontSplitArray[1]);
            System.out.println("####  orgPrefix = " + orgPrefix + " orgSuffix = " + orgSuffix);
            System.out.println("####  orgSuffix % SPLIT_INDEX = " + orgSuffix % SPLIT_INDEX);

            int preIndex = orgSuffix / SPLIT_INDEX;
            int newPrefix;
            int newSuffix;
            if (orgSuffix % SPLIT_INDEX == 0) {
                newPrefix = preIndex;
                newSuffix = SPLIT_INDEX;
            } else {
                newPrefix = preIndex + 1;
                newSuffix = orgSuffix % SPLIT_INDEX;
            }

            System.out.println("####  newPrefix = " + newPrefix + " newSuffix = " + newSuffix);
            File orgFile = fileList[i];
            orgFile.renameTo(new File(NEW_FILE_DIR + File.separator + newPrefix + "_" + newSuffix + ".png"));

        }
        // ---------
//        Renamefile.rename();
//        String path1 = "C:/Users/lenovo/Desktop/temp/1/1_51.png";
//        String path2 = "C:/Users/lenovo/Desktop/temp/2/1_51.png";
//        try {
//            copyFile(path1, path2);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    public static void copyFile(String file1, String file2) throws IOException {
        FileInputStream fis = new FileInputStream(file1);
        FileOutputStream fos = new FileOutputStream(file2);
        int temp;
        while ((temp = fis.read()) != -1) {
            fos.write(temp);
        }
        fis.close();
        fos.close();
        System.out.println("从" + file1 + "到" + file2);
    }

}
