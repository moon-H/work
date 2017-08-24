package com.cssweb.liwx.swt;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.cssweb.mytest.zip.ZipUtils;

public class MyView {
	Button btnLargePath;
	Button btnMiddlePath;
	Button btnSmallPath;

	Text txtLargePath;
	private static final int BASE_LOCATION_Y = 37;
	private static final int BASE_LOCATION_X = 29;
	private static final int BASE_TEXT_X = 120;

	private static final int BASE_TEXT_WIDTH = 500;
	private static final int BASE_TEXT_WIDTH2 = 100;
	private static final int BASE_TEXT_HEIGHT = 23;

	private static final int BASE_BTN_WIDTH = 80;
	private static final int BASE_BTN_HEIGHT = 27;

	private static final int BASE_MARGIN = 27;

	String strPathLarge;
	String strPathMiddle;
	String strPathSmall;
	String strCityCode;
	String strVersion;
	String strPathOutPut;

	Text txtCityCode;
	Text txtVersion;
	private static final String ANDROID = "android";
	private static final String IOS = "ios";
	private static final String TEMP = ANDROID + "/temp";
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日HH点mm分ss秒");
	File androidTimeFile;
	File iosOutPutParentFile;
	String lastPath;
	String optionTime;

	public void initView() {
		/**
		 * 初始化Display和Shell
		 */
		Display display = new Display();
		final Shell shell = new Shell(display, SWT.SHELL_TRIM);
		shell.setSize(700, 500);
		int[] location = getCenterLocation(shell);// 居中
		shell.setLocation(location[0], location[1]);
		shell.setText("修改路网图图片");// 窗口标题

		final Text txtLarge = new Text(shell, SWT.NONE);
		txtLarge.setBounds(BASE_TEXT_X, BASE_LOCATION_Y, BASE_TEXT_WIDTH,
				BASE_TEXT_HEIGHT);

		Button btnLarge = new Button(shell, SWT.NONE);
		btnLarge.setBounds(BASE_LOCATION_X, BASE_LOCATION_Y, BASE_BTN_WIDTH,
				BASE_BTN_HEIGHT);
		btnLarge.setText("路径--大");
		btnLarge.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				String path = openFile("路网图--大", shell);
				if (path != null && path.length() != 0) {
					txtLarge.setText(path);
					strPathLarge = path;
					lastPath = strPathLarge;
				}
			}
		});

		final Text txtMiddle = new Text(shell, SWT.NONE);
		txtMiddle.setBounds(BASE_TEXT_X, BASE_LOCATION_Y * 2, BASE_TEXT_WIDTH,
				BASE_TEXT_HEIGHT);

		Button btnMiddle = new Button(shell, SWT.NONE);
		btnMiddle.setBounds(BASE_LOCATION_X, BASE_LOCATION_Y * 2,
				BASE_BTN_WIDTH, BASE_BTN_HEIGHT);
		btnMiddle.setText("路径---中");
		btnMiddle.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				String path = openFile("路网图--中", shell);
				if (path != null && path.length() != 0) {
					txtMiddle.setText(path);
					strPathMiddle = path;
					lastPath = strPathMiddle;

				}
			}
		});
		final Text textSmall = new Text(shell, SWT.NONE);
		textSmall.setBounds(BASE_TEXT_X, BASE_LOCATION_Y * 3, BASE_TEXT_WIDTH,
				BASE_TEXT_HEIGHT);

		Button btnSmall = new Button(shell, SWT.NONE);
		btnSmall.setBounds(BASE_LOCATION_X, BASE_LOCATION_Y * 3,
				BASE_BTN_WIDTH, BASE_BTN_HEIGHT);
		btnSmall.setText("路径---小");
		btnSmall.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				// DirectoryDialog dirDialog = new DirectoryDialog(shell,
				// SWT.OPEN);
				// dirDialog.setFilterPath("SystemRoot");
				String path = openFile("路网图--小", shell);
				if (path != null && path.length() != 0) {
					textSmall.setText(path);
					strPathSmall = path;
					lastPath = strPathSmall;

				}
			}
		});

		/**
		 * 城市代码,版本
		 */
		txtCityCode = new Text(shell, SWT.NONE);
		txtCityCode.setBounds(BASE_TEXT_X, BASE_LOCATION_Y * 4,
				BASE_TEXT_WIDTH2, BASE_TEXT_HEIGHT);

		Button btnCityCode = new Button(shell, SWT.NONE);
		btnCityCode.setBounds(BASE_LOCATION_X, BASE_LOCATION_Y * 4,
				BASE_BTN_WIDTH, BASE_BTN_HEIGHT);
		btnCityCode.setText("城市代码");

		txtVersion = new Text(shell, SWT.NONE);
		txtVersion.setBounds(BASE_LOCATION_X + BASE_TEXT_WIDTH2
				+ BASE_BTN_WIDTH + BASE_MARGIN + BASE_TEXT_WIDTH2,
				BASE_LOCATION_Y * 4, BASE_TEXT_WIDTH2, BASE_TEXT_HEIGHT);

		Button btnVersion = new Button(shell, SWT.NONE);
		btnVersion.setBounds(BASE_LOCATION_X + BASE_TEXT_WIDTH2
				+ BASE_BTN_WIDTH + BASE_MARGIN, BASE_LOCATION_Y * 4,
				BASE_BTN_WIDTH, BASE_BTN_HEIGHT);
		btnVersion.setText("版本号");

		final Text textOutPath = new Text(shell, SWT.NONE);
		textOutPath.setBounds(BASE_TEXT_X, BASE_LOCATION_Y * 5,
				BASE_TEXT_WIDTH, BASE_TEXT_HEIGHT);

		Button btnOutPutPath = new Button(shell, SWT.NONE);
		btnOutPutPath.setBounds(BASE_LOCATION_X, BASE_LOCATION_Y * 5,
				BASE_BTN_WIDTH, BASE_BTN_HEIGHT);
		btnOutPutPath.setText("输出路径");
		btnOutPutPath.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				// DirectoryDialog dirDialog = new DirectoryDialog(shell,
				// SWT.OPEN);
				// dirDialog.setFilterPath("SystemRoot");
				String path = openFile("输出路径", shell);
				if (path != null && path.length() != 0) {
					textOutPath.setText(path);
					strPathOutPut = path;
					lastPath = strPathOutPut;
				}
			}
		});

		/**
		 * 打包按钮
		 */
		Button androidZipBtn = new Button(shell, SWT.NONE);
		androidZipBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// Main.geneCode(txtXMLSrc.getText() ,txtCodeTo.getText());
				String cityCode = txtCityCode.getText().trim();
				String version = txtVersion.getText().trim();
				System.out.println("大 = " + strPathLarge);
				System.out.println("中 = " + strPathMiddle);
				System.out.println("小 = " + strPathSmall);
				System.out.println("城市代码 = " + cityCode);
				System.out.println("版本= " + version);
				System.out.println("输出路径= " + strPathOutPut);

				// 1.在输出目录创建IOS和Android文件夹
				// 2.在Android文件夹下创建temp文件夹
				// 3.在Android文件夹下创建城市代码_时间戳文件夹，在下面在创建城市代码文件夹，在此文件夹内创建三个分辨率的文件夹2,3,4
				// 4.将最初选定的资源复制到temp文件夹下，并改名
				// 5.将temp文件夹下的文件复制到城市名称下制定目录内
				// 6.循环执行步骤4,5
				// 7.执行完改名操作，在与tiles文件夹平级的目录下创建版本文件
				// 8.生成zip包
				if (strPathLarge == null || strPathLarge.length() == 0) {
					showDialog(shell, "请选择大图路径");
					return;
				}
				if (strPathMiddle == null || strPathMiddle.length() == 0) {
					showDialog(shell, "请选择中图路径");
					return;
				}
				if (strPathSmall == null || strPathSmall.length() == 0) {
					showDialog(shell, "请选择小图路径");
					return;
				}

				if (cityCode == null || cityCode.length() == 0) {
					showDialog(shell, "请输入城市代码");
					return;
				}
				if (version == null || version.length() == 0) {
					showDialog(shell, "请输入版本号");
					return;
				}
				if (strPathOutPut == null || strPathOutPut.length() == 0) {
					showDialog(shell, "请选择输出路径");
					return;
				}
				try {
					makeOutPutDir(strPathOutPut, cityCode, version, 0);
					changeAndroid(strPathLarge, "4");
					changeAndroid(strPathMiddle, "3");
					changeAndroid(strPathSmall, "2");
				} catch (Exception e2) {
					e2.printStackTrace();
					showDialog(shell, "修改文件名称失败");
					return;
				}
				try {
					createVersionFile(version, androidTimeFile.getPath()
							+ File.separator + "version");
				} catch (IOException e1) {
					e1.printStackTrace();
					showDialog(shell, "创建版本文件失败");
					return;
				}
				try {
					ZipUtils.ZipFolder(strPathOutPut + File.separator + ANDROID
							+ File.separator + cityCode + "_" + optionTime,
							strPathOutPut + File.separator + TEMP+File.separator+cityCode
									+ ".zip");
					copyFile(strPathOutPut + File.separator + TEMP
							+ File.separator + cityCode + ".zip", strPathOutPut
							+ File.separator + ANDROID + File.separator
							+ cityCode + "_" + optionTime + File.separator
							+ cityCode + ".zip");
					deleteFile(strPathOutPut + File.separator + TEMP);
				} catch (Exception e1) {
					e1.printStackTrace();
					showDialog(shell, "生成压缩文件失败");
					return;
				}
				showDialog(shell, "android 成功");

			}
		});
		androidZipBtn.setText("生成Android zip包");
		androidZipBtn.setBounds(170, BASE_LOCATION_Y * 6, BASE_BTN_WIDTH * 2,
				BASE_BTN_HEIGHT);
		/**
		 * 打包按钮
		 */
		Button iosZipBtn = new Button(shell, SWT.NONE);
		iosZipBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String cityCode = txtCityCode.getText().trim();
				String version = txtVersion.getText().trim();
				makeOutPutDir(strPathOutPut, cityCode, version, 1);
				File largeFile = new File(strPathLarge);
				File[] largeList = largeFile.listFiles();
				int splitIndex = getSpiltIndex(largeList);
				iosRename(iosOutPutParentFile.getPath(), splitIndex);
				try {
					createVersionFile(version, strPathOutPut + File.separator
							+ IOS + File.separator + cityCode + "_"
							+ optionTime + File.separator + cityCode
							+ File.separator + "version");
				} catch (IOException e1) {
					e1.printStackTrace();
					showDialog(shell, "创建版本文件失败");
					return;
				}
				try {
					ZipUtils.ZipFolder(strPathOutPut + File.separator + IOS
							+ File.separator + cityCode + "_" + optionTime,
							strPathOutPut + File.separator + TEMP
									+ File.separator + cityCode + ".zip");
					copyFile(strPathOutPut + File.separator + TEMP
							+ File.separator + cityCode + ".zip", strPathOutPut
							+ File.separator + IOS + File.separator + cityCode
							+ "_" + optionTime + File.separator + cityCode
							+ ".zip");
					deleteFile(strPathOutPut + File.separator + TEMP);
				} catch (Exception e1) {
					e1.printStackTrace();
					showDialog(shell, "生成压缩文件失败");
					return;
				}
				showDialog(shell, "ios 成功");

			}
		});
		iosZipBtn.setText("生成iOS zip包");
		iosZipBtn.setBounds(400, BASE_LOCATION_Y * 6, BASE_BTN_WIDTH * 2,
				BASE_BTN_HEIGHT);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		display.dispose();
	}

	private void changeAndroid(String path, String subPath) {
		File largeFile = new File(path);
		File[] largeList = largeFile.listFiles();

		int splitIndex = getSpiltIndex(largeList);
		System.out.println("大 --- index = " + splitIndex);
		copyToTempAndRename(largeList, splitIndex);
		copyToPackDir(subPath);
	}

	public String openFile(String text, Shell shell) {
		DirectoryDialog dd = new DirectoryDialog(shell);
		dd.setText(text);
		if (lastPath != null && lastPath.length() != 0) {
			dd.setFilterPath(lastPath);
		} else {
			dd.setFilterPath("SystemDrive");
		}
		// dd.setMessage("这个是什么？？");
		String selecteddir = dd.open();
		return selecteddir;
	}

	private static int[] getCenterLocation(Shell shell) {
		int width = shell.getMonitor().getClientArea().width;
		int height = shell.getMonitor().getClientArea().height;
		int x = shell.getSize().x;
		int y = shell.getSize().y;
		if (x > width) {
			shell.getSize().x = width;
		}
		if (y > height) {
			shell.getSize().y = height;
		}
		int[] locations = new int[2];
		locations[0] = (width - x) / 2;
		locations[1] = (height - y) / 2;

		return locations;
	}

	private void makeOutPutDir(String outPath, String cityCode, String version,
			int flag) {
		optionTime = dateFormat.format(System.currentTimeMillis());
		if (flag == 0) {

			File androidTempFile = new File(outPath + File.separator + TEMP);
			androidTimeFile = new File(outPath + File.separator + ANDROID
					+ File.separator + cityCode + "_" + optionTime
					+ File.separator + cityCode);
			androidTimeFile.mkdirs();
			System.out.println("路径 = " + outPath + File.separator + ANDROID
					+ File.separator + cityCode + "_" + optionTime
					+ File.separator + cityCode);

			for (int i = 2; i < 5; i++) {
				// 创建2，3,4文件夹
				File tilesFile = new File(outPath + File.separator + ANDROID
						+ File.separator + cityCode + "_" + optionTime
						+ File.separator + cityCode + File.separator + "tiles"
						+ File.separator + i);
				tilesFile.mkdirs();
			}

			if (androidTempFile.exists()) {
				deleteFile(androidTempFile.getPath());
			}

			androidTempFile.mkdirs();

		} else {

			iosOutPutParentFile = new File(outPath + File.separator + IOS
					+ File.separator + cityCode + "_" + optionTime
					+ File.separator + cityCode + File.separator + "tiles");
			iosOutPutParentFile.mkdirs();
		}

	}

	private void copyToPackDir(String subDir) {
		File parentFile = new File(strPathOutPut + File.separator + TEMP);
		File[] filelist = parentFile.listFiles();
		// File file = filelist[0];
		// if (file == null)
		// return;
		for (File file : filelist) {

			String fileName = file.getName();
			String[] nameSplitArray = fileName.split("\\.");
			String[] frontSplitArray = nameSplitArray[0].split("_");
			int orgPrefix = Integer.parseInt(frontSplitArray[0]);
			int orgSuffix = Integer.parseInt(frontSplitArray[1]);

			int newPrefix = orgSuffix - 1;
			int newSuffix = orgPrefix - 1;
			// System.out.println("@@@  newPrefix = " + newPrefix
			// + " newSuffix = " + newSuffix);
			// System.out.println("@@@ path " + androidTimeFile.getPath()
			// + File.separator + subDir + File.separator + newPrefix
			// + "_" + newSuffix + ".png");
			boolean renameResult = file.renameTo(new File(androidTimeFile
					.getPath()
					+ File.separator
					+ "tiles"
					+ File.separator
					+ subDir
					+ File.separator
					+ newPrefix
					+ "_"
					+ newSuffix
					+ ".png"));
			System.out.println("@@@  renameResult = " + renameResult);
		}
	}

	private void copyToTempAndRename(File[] fileListSize, int index) {
		for (int i = 0; i < fileListSize.length; i++) {
			String fileName = fileListSize[i].getName();
			if (!fileName.endsWith("png")) {
				continue;
			}
			System.out.println("&&& fileName = " + fileName);
			String[] nameSplitArray = fileName.split("\\.");
			System.out.println("&&&  nameSplit front = " + nameSplitArray[0]);
			String[] frontSplitArray = nameSplitArray[0].split("_");
			System.out.println("&&&  frontSplitArray front = "
					+ frontSplitArray[0]);
			int orgPrefix = Integer.parseInt(frontSplitArray[0]);
			int orgSuffix = Integer.parseInt(frontSplitArray[1]);
			System.out.println("&&&  orgPrefix = " + orgPrefix
					+ " orgSuffix = " + orgSuffix);
			System.out.println("&&&  orgSuffix % SPLIT_INDEX = " + orgSuffix
					% index);

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

			System.out.println("&&&  newPrefix = " + newPrefix
					+ " newSuffix = " + newSuffix);
			File orgFile = fileListSize[i];
			System.out.println("&&&  orgFile path = " + orgFile.getPath()
					+ " name = " + orgFile.getName());
			File newFile = new File(strPathOutPut + File.separator + TEMP
					+ File.separator + newPrefix + "_" + newSuffix + ".png");
			boolean newFileExist = newFile.exists();
			if (newFileExist) {
				boolean deleteResult = newFile.delete();
			}
			System.out.println("&&&  newFile exist = " + newFileExist);
			try {
				copyFile(orgFile.getPath(), newFile.getPath());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void deleteFile(String path) {
		File parentFile = new File(path);
		File[] fileList = parentFile.listFiles();
		for (File file : fileList) {
			if (file.isDirectory()) {
				deleteFile(file.getPath());
			} else {
				file.delete();
			}
		}
		parentFile.delete();
	}

	public void copyFile(String file1, String file2) throws IOException {
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

	private int getSpiltIndex(File[] chileList) {
		ArrayList<File> tempList = new ArrayList<>();
		int spitIndex = -1;
		for (File file1 : chileList) {
			FileInputStream b = null;
			try {

				String fileName = file1.getName();
				if (!fileName.endsWith("png")) {
					continue;
				}
				String[] nameSplitArray = fileName.split("\\.");
				String[] frontSplitArray = nameSplitArray[0].split("_");
				int orgPrefix = Integer.parseInt(frontSplitArray[0]);

				b = new FileInputStream(file1);
				SimpleImageInfo imageInfo = new SimpleImageInfo(b);
				// System.out.println("## img info : " + imageInfo);
				if (imageInfo.getWidth() != 256 && orgPrefix == 1) {
					tempList.add(file1);
					// int orgSuffix = Integer.parseInt(frontSplitArray[1]);
					// System.out.println("---- spitIndex : " + spitIndex);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (b != null)
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
		return spitIndex;
	}

	private void showDialog(Shell shell, String msg) {
		MessageBox box = new MessageBox(shell, SWT.YES);

		// 设置对话框的标题
		box.setText("提示");
		// 设置对话框显示的消息
		box.setMessage(msg);
		box.open();
	}

	private void showDialogTwoBtn(Shell shell, String msg) {
		MessageBox box = new MessageBox(shell, SWT.YES | SWT.NO);

		// 设置对话框的标题
		box.setText("提示");
		// 设置对话框显示的消息
		box.setMessage(msg);
		box.open();
	}

	private void createVersionFile(String content, String path)
			throws IOException {
		File versionFile = new File(path);
		if (versionFile.exists()) {
			versionFile.delete();
		}
		versionFile.createNewFile();

		RandomAccessFile mm = null;
		FileOutputStream o = null;
		try {
			o = new FileOutputStream(versionFile.getPath());
			o.write(content.getBytes("GBK"));
			o.close();
			// mm=new RandomAccessFile(fileName,"rw");
			// mm.writeBytes(content);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (mm != null) {
				mm.close();
			}
		}

	}

	private void iosRename(String path, int splitIndex) {
		File fileDir = new File(strPathLarge);
		System.out.println("#### is dir = " + fileDir.isDirectory());
		File[] fileList = fileDir.listFiles();
		int fileListSize = fileList.length;
		System.out.println("#### fileListSize = " + fileListSize);

		for (File file : fileList) {
			String fileName = file.getName();
			if (!fileName.endsWith("png")) {
				continue;
			}
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
					% splitIndex);

			int preIndex = orgSuffix / splitIndex;
			int newPrefix;
			int newSuffix;
			if (orgSuffix % splitIndex == 0) {
				newPrefix = preIndex;
				newSuffix = splitIndex;
			} else {
				newPrefix = preIndex + 1;
				newSuffix = orgSuffix % splitIndex;
			}

			System.out.println("####  newPrefix = " + newPrefix
					+ " newSuffix = " + newSuffix);
			// orgFile.renameTo(new File(NEW_FILE_DIR + File.separator +
			// newPrefix
			// + "_" + newSuffix + ".png"));
			try {
				copyFile(file.getPath(), path + File.separator + newPrefix
						+ "_" + newSuffix + ".png");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}
