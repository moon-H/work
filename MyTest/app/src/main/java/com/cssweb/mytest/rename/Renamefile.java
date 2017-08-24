package com.cssweb.mytest.rename;

import java.io.File;

public class Renamefile {
    //    private static final String FILE_DIR = "D:/rename/new";
    //    private static final String NEW_FILE_DIR = "D:/rename/new/new";

    public static void rename(String curPath, String newPath) {
        System.out.println("@@@ is curPath = " + curPath + " newPath = " + newPath);
        //        File fileDir = ;
        //        System.out.println("@@@ is dir = " + fileDir.isDirectory());
        //        File[] fileList = fileDir.listFiles();
        //        int fileListSize = fileList.length;
        //        System.out.println("@@@ fileListSize = " + fileListSize);

        //        for (int i = 0; i < fileListSize; i++) {
        File parentFile = new File(curPath);
        //            if (file.isDirectory()) {
        //                continue;
        //            }
        File[] filelist = parentFile.listFiles();
        File file = filelist[0];
        if (file == null)
            return;

        String fileName = file.getName();
        System.out.println("@@@" + fileName);
        String[] nameSplitArray = fileName.split("\\.");
        System.out.println("#@@@  nameSplit front = " + nameSplitArray[0]);
        String[] frontSplitArray = nameSplitArray[0].split("_");
        System.out.println("@@@  frontSplitArray front = " + frontSplitArray[0]);
        int orgPrefix = Integer.parseInt(frontSplitArray[0]);
        int orgSuffix = Integer.parseInt(frontSplitArray[1]);
        System.out.println("@@@  orgPrefix = " + orgPrefix + " orgSuffix = " + orgSuffix);

        int newPrefix = orgSuffix - 1;
        int newSuffix = orgPrefix - 1;
        System.out.println("@@@  newPrefix = " + newPrefix + " newSuffix = " + newSuffix);
        //        File orgFile = fileList[i];
        boolean renameResult = file.renameTo(new File(newPath + File.separator + newPrefix + "_" + newSuffix + ".png"));
        System.out.println("@@@  renameResult = " + renameResult);
        //        }
    }
}
