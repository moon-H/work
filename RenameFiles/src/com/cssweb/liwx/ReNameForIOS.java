package com.cssweb.liwx;

import java.io.File;

public class ReNameForIOS {
	private static final String FILE_DIR = "D:/rename";
	private static final String NEW_FILE_DIR = "D:/rename/new";
	private static final int SPLIT_INDEX = 21;//…œ∫£
//	private static final int SPLIT_INDEX = 19;

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
			System.out.println("####  frontSplitArray front = "
					+ frontSplitArray[0]);
			int orgPrefix = Integer.parseInt(frontSplitArray[0]);
			int orgSuffix = Integer.parseInt(frontSplitArray[1]);
			System.out.println("####  orgPrefix = " + orgPrefix
					+ " orgSuffix = " + orgSuffix);
			System.out.println("####  orgSuffix % SPLIT_INDEX = " + orgSuffix
					% SPLIT_INDEX);

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

			System.out.println("####  newPrefix = " + newPrefix
					+ " newSuffix = " + newSuffix);
			File orgFile = fileList[i];
			orgFile.renameTo(new File(NEW_FILE_DIR + File.separator + newPrefix
					+ "_" + newSuffix + ".png"));

		}
		// ---------
	}
}
