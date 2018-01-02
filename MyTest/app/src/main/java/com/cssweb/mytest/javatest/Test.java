package com.cssweb.mytest.javatest;

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
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by lenovo on 2016/9/7.
 */
public class Test {
    private static String path = "C:/Users/lenovo/Desktop/css/xian.txt";
    private static String outPath = "C:/Users/lenovo/Desktop/css/station.xls";
    private static final String KEY = "95C5CD0BBF5E3E5227273458AB39A15C";

    public static void main(String[] args) {


        //        String key = "4B142EE630356B184B142EE630356B18";
        //        String org = "0F3560CAFFBAAEA9BEDC7CDD2B633A81343F8BA673C6813AFEC1D603CD2B9BF8CEA2F3A2C563C750";
        //        try {
        //            String src = DesThree.decryptThreeDESECB(HexConverter.hexStringToBytes(org), key);
        //            System.out.print("-------" + src);
        //        } catch (Exception e) {
        //            e.printStackTrace();
        //        }

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
            Date date = dateFormat.parse("20170907092555");
            long current = System.currentTimeMillis();
            long expire = date.getTime();

            SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            System.out.println("-------" + (current > expire));
            System.out.println("-------2222::" + mDateFormat.format(new Date(1507630856000l)));
            //            Date tkExpireDate = DateFormat.getDateInstance().parse("20170907092555");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Man man = new Man();
        man.setAge("19");
        man.setName("嘿嘿");

        sortTest(man);
    }

    private static void sortTest(Object person) {
        try {
            Field[] fields = person.getClass().getDeclaredFields();
            TreeMap<String, String> map = new TreeMap<>();
            for (Field field : fields) {
                map.put(field.getName(), String.valueOf(field.get(person)));

                //                System.out.print("成员变量" + i + "类型 : " + fields[i].getType().getName());
                //                System.out.print("\t成员变量" + i + "变量名: " + fields[i].getName() + "\t");
                //                System.out.println("成员变量" + i + "值: " + fields[i].get(person));
            }
            StringBuilder sb = new StringBuilder();
            Set<String> keys = map.keySet();
            for (String key : keys) {
                sb.append(key).append("=").append(map.get(key)).append("&");
            }
            System.out.println("排序后的参数：：：" + sb.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //        for (Field field : fields) {
    //            System.out.println("-----field= " + field.getName());
    //            System.out.println("-----fieldValue= " + field.get(person));
    //
    //        }


    //    TreeMap<String, String> map = new TreeMap<>();
    //        map.put("124","hahah");
    //        map.put("123","hahah");
    //        map.put("222","hahah");
    //        map.put("acd","hahah");
    //        map.put("bcd","hahah");
    //        map.put("ccd","hahah");
    //        map.put("cca","hahah");
    //        System.out.println("-----Map2= "+map.toString());
    //    Set<String> keys = map.keySet();
    //        for(
    //    String key :keys)
    //
    //    {
    //        System.out.println("key  = " + key);
    //    }

    //}


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
