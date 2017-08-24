package com.cssweb.mytest.javatest;

import com.cssweb.framework.security.DES3;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2016/9/7.
 */
public class Test {
    private static String path = "C:/Users/lenovo/Desktop/css/xian.txt";
    private static String outPath = "C:/Users/lenovo/Desktop/css/station.xls";

    public static void main(String[] args) {
        //        try {
        //            readF1(path);
        //        } catch (IOException e) {
        //            e.printStackTrace();
        //        }
        //        exportExcel();
        //        System.out.println(addAlpha("#FFFFFF", 0.9));
        //        int i = 1;
        //        Integer intValue = Integer.valueOf(10);
        //        System.out.println("#byte  = " + HexConverter.byteToHexString(intValue.byteValue()));



    }

    private void intToByte(int value) {

    }


    public static String addAlpha(String originalColor, double alpha) {
        long alphaFixed = Math.round(alpha * 255);
        String alphaHex = Long.toHexString(alphaFixed);
        if (alphaHex.length() == 1) {
            alphaHex = "0" + alphaHex;
        }
        originalColor = originalColor.replace("#", "#" + alphaHex);
        return originalColor;
    }

    public static final void readF1(String filePath) throws IOException {
        ArrayList<Station> stationList = new ArrayList<>();
        boolean isStationInfo = false;
        Station station = null;
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
        for (String line = br.readLine(); line != null; line = br.readLine()) {
            System.out.println("-----" + line);
            String stationInfo = line.trim();
            if (stationInfo.startsWith(".")) {
                station = new Station();
                isStationInfo = true;
                station.setName(stationInfo.substring(1, stationInfo.length()));
            }
            if (stationInfo.equals("}")) {
                isStationInfo = false;
            }
            if (isStationInfo) {
                if (stationInfo.startsWith("left")) {
                    String[] leftArray = stationInfo.split(":");
                    station.setLeft(leftArray[1].substring(0, leftArray[1].length() - 3));
                } else if (stationInfo.startsWith("top")) {
                    String[] topArray = stationInfo.split(":");
                    station.setTop(topArray[1].substring(0, topArray[1].length() - 3));
                } else if (stationInfo.startsWith("width")) {
                    String[] widthArray = stationInfo.split(":");
                    station.setWidth(widthArray[1].substring(0, widthArray[1].length() - 3));
                } else if (stationInfo.startsWith("height")) {
                    String[] heightArray = stationInfo.split(":");
                    station.setHeight(heightArray[1].substring(0, heightArray[1].length() - 3));
                }
            }
            if (stationInfo.equals("}")) {
                stationList.add(station);
            }
        }
        for (Station st : stationList) {
            System.out.println("### " + st.toString());
        }
        br.close();
        exportExcel(stationList);
    }

    public static void exportExcel(List<Station> list) {
        // 第一步，创建一个webbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet("站点");
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
        HSSFRow row = sheet.createRow((int) 0);
        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

        HSSFCell cell = row.createCell((short) 0);
        cell.setCellValue("站点名称");
        cell.setCellStyle(style);
        cell = row.createCell((short) 1);
        cell.setCellValue("left");
        cell.setCellStyle(style);
        cell = row.createCell((short) 2);
        cell.setCellValue("top");
        cell.setCellStyle(style);
        cell = row.createCell((short) 3);
        cell.setCellValue("width");
        cell.setCellStyle(style);
        cell = row.createCell((short) 4);
        cell.setCellValue("height");
        cell.setCellStyle(style);
        //        cell = row.createCell((short) 3);
        //        cell.setCellValue("生日");
        //        cell.setCellStyle(style);

        // 第五步，写入实体数据 实际应用中这些数据从数据库得到，
        //
        //        for (int i = 0; i < 10; i++) {
        //            Student student = new Student();
        //            student.setId("学号" + i);
        //            student.setName("名字" + i);
        //            student.setAge("年龄" + i);
        //            list.add(student);
        //        }

        for (int i = 0; i < list.size(); i++) {
            row = sheet.createRow((int) i + 1);
            Station sta = (Station) list.get(i);
            // 第四步，创建单元格，并设置值
            row.createCell((short) 0).setCellValue(sta.getName());
            row.createCell((short) 1).setCellValue(sta.getLeft());
            row.createCell((short) 2).setCellValue(sta.getTop());
            row.createCell((short) 3).setCellValue(sta.getWidth());
            row.createCell((short) 4).setCellValue(sta.getHeight());
            cell = row.createCell((short) 5);
            //            cell.setCellValue(new SimpleDateFormat("yyyy-mm-dd").format(stu.getBirth()));
        }
        // 第六步，将文件存到指定位置
        try {
            FileOutputStream fout = new FileOutputStream(outPath);
            wb.write(fout);
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class Station {
        String name;
        String left;
        String top;
        String width;
        String height;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLeft() {
            return left;
        }

        public void setLeft(String left) {
            this.left = left;
        }

        public String getTop() {
            return top;
        }

        public void setTop(String top) {
            this.top = top;
        }

        public String getWidth() {
            return width;
        }

        public void setWidth(String width) {
            this.width = width;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        @Override
        public String toString() {
            return "Station{" + "name='" + name + '\'' + ", left='" + left + '\'' + ", top='" + top + '\'' + ", width='" + width + '\'' + ", height='" + height + '\'' + '}';
        }
    }

}
