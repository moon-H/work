package com.cssweb.liwx;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.swing.*;

public class Calculator extends JFrame {
	private static final String FILE_DIR = "D:/rename";
	private static final String NEW_FILE_DIR = "D:/rename/new";

	private JLabel lbNum1;
	private JTextField tfNUm1;
	private JTextField tfResult;
	private JButton btSimmit;
	private JButton btReset;
	private JPanel panel;
	private JPanel panelLarge;
	private JPanel panelMiddle;
	private JPanel panelSmall;
	private JTextField tv_LagePath;
	private JTextField tv_Middle;
	private JTextField tv_Small;
	private JButton btn_LargePath;
	private JButton btn_MiddlePath;
	private JButton btn_SmallPath;

	private double num1;
	private double result;

	public Calculator(String title) throws HeadlessException {
		super(title);
		Container cp = getContentPane();
		cp.setLayout(new FlowLayout());
		panel = new JPanel();
		panelLarge = new JPanel();
		panelMiddle = new JPanel();
		panelSmall = new JPanel();

		panelLarge.setLayout(new FlowLayout());
		panelMiddle.setLayout(new FlowLayout());
		panelSmall.setLayout(new FlowLayout());

		btn_LargePath = new JButton("大");
		btn_MiddlePath = new JButton("中");
		btn_SmallPath = new JButton("小");

		panel.setLayout(new FlowLayout());
		lbNum1 = new JLabel("输入一个数");
		tfNUm1 = new JTextField(15);
		tfResult = new JTextField(15);
		tfResult.setBackground(Color.yellow);
		btSimmit = new JButton("开始");

		btSimmit.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {

				JFileChooser fd = new JFileChooser();
				fd.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fd.showOpenDialog(null);
				File f = fd.getSelectedFile();

				System.out.println("选择的是文件夹  = " + (f != null));
				System.out.println("选择的是文件夹 = " + f.getPath());

				showWarmDialog("选择的路径是" + f.getPath());
				// String str = tfNUm1.getText();
				// int index = 0;
				// try {
				// index = Integer.parseInt(str.trim());
				// if (index == 0) {
				// showWarmDialog();
				// return;
				// }
				// } catch (Exception e) {
				// showWarmDialog();
				// return;
				// }
				// if (renameFile(index)) {
				// showCompleteDialog();
				// }
			}

		});
		btReset = new JButton("重置");
		btReset.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				tfNUm1.setText("");
				tfResult.setText("");
			}

		});
		// panel.add(lbNum1);
		// panel.add(tfNUm1);
		// panel.add(btSimmit);
		// panel.add(btReset);
		// panel.add(tfResult);
		panelLarge.add(btn_LargePath);
		panelMiddle.add(btn_MiddlePath);
		panelSmall.add(btn_SmallPath);

		cp.add(panelLarge, BorderLayout.CENTER);
		cp.add(panelMiddle, BorderLayout.CENTER);
		cp.add(panelSmall, BorderLayout.CENTER);
		setVisible(true);
		setSize(600, 150);
		setLocation(250, 200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		new Calculator("批量修改文件名称");
	}

	private boolean renameFile(int splitIndex) {
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
			File orgFile = fileList[i];
			orgFile.renameTo(new File(NEW_FILE_DIR + File.separator + newPrefix
					+ "_" + newSuffix + ".png"));
		}
		return true;
	}

	private void showWarmDialog() {
		JOptionPane.showMessageDialog(Calculator.this, "请输入大于0的数字", "警告",
				JOptionPane.WARNING_MESSAGE);
	}

	private void showWarmDialog(String msg) {
		JOptionPane.showMessageDialog(Calculator.this, msg, "提示",
				JOptionPane.WARNING_MESSAGE);
	}

	private void showCompleteDialog() {
		JOptionPane.showMessageDialog(Calculator.this, "修改完成", "提示",
				JOptionPane.WARNING_MESSAGE);
	}
}