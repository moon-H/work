package cn.unicompay.wallet.client.framework.util;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

public class Util {
  @Deprecated
	public static void saveImageOnLine(String imgUrl,String imgFile) {
		InputStream is = null;
		ByteArrayOutputStream bos = null;
		FileOutputStream fos = null;
		try{
			URL url = new URL(imgUrl);
			is = (InputStream)url.getContent();
			bos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024*10];
			int length = 0;
			while((length=is.read(buffer, 0, buffer.length))!=-1) {
				bos.write(buffer, 0, length);
			}

			fos = new FileOutputStream(imgFile);
			fos.write(bos.toByteArray());
			fos.flush();
			bos.close();
			is.close();
			fos.close();
			is = null;
			bos = null;
			fos = null;
		}catch(Exception e){
			e.printStackTrace();
		} finally {
			try{if(is!=null) is.close();}catch(Exception e){}
			try{if(bos!=null) bos.close();}catch(Exception e){}
			try{if(fos!=null) fos.close();}catch(Exception e){}
		}
	}
	
	public static void saveImageOnLine(final String imageUrl,final String imgFile,final DownloadImgObserver observer,Handler handler) {
	  handler.post(new Runnable(){
	    public void run() {
	      InputStream is = null;
	      ByteArrayOutputStream bos = null;
	      FileOutputStream fos = null;
	      try{
	        URL url = new URL(imageUrl);
	        is = (InputStream)url.getContent();
	        bos = new ByteArrayOutputStream();
	        byte[] buffer = new byte[1024*10];
	        int length = 0;
	        while((length=is.read(buffer, 0, buffer.length))!=-1) {
	          bos.write(buffer, 0, length);
	        }

	        fos = new FileOutputStream(imgFile);
	        fos.write(bos.toByteArray());
	        fos.flush();
	        bos.close();
	        is.close();
	        fos.close();
	        is = null;
	        bos = null;
	        fos = null;
	        if(observer!=null) observer.onCompleted(imgFile);
	      }catch(Exception e){
	        e.printStackTrace();
	      } finally {
	        try{if(is!=null) is.close();}catch(Exception e){}
	        try{if(bos!=null) bos.close();}catch(Exception e){}
	        try{if(fos!=null) fos.close();}catch(Exception e){}
	      }
	    }
	  });
	}
	
	
	public static String trimDash(String phone) {
		if(phone==null||phone.length()==0) return phone;
		StringBuffer sb = new StringBuffer();
		char[] chars = phone.toCharArray();
		int len = chars.length;
		for(int i=0;i<len;i++) {
			if(chars[i]!='-') {
				sb.append(chars[i]);
			}
		}

		return sb.toString();
	}
	
	/**
	 * return current day. ex) "20110608044100:-0600"
	 * @return
	 */
	public static String currentDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss:z");
		Calendar cal = Calendar.getInstance();
		Date d = cal.getTime();
		return sdf.format(d);
	}

	
	
	/**
	 * ex) "20110608044100:-0600" => "06-08-2011 at 04:41 AM/PM"
	 */
	public static final int LOCAL_DATE_01 = 0;
	
	/**
	 * ex) "20110608044100:-0600" => "6/8/11"
	 */
	public static final int LOCAL_DATE_02 = 1;
	
	/**
	 * ex) "20110608044100:-0600" => "1:37pm"
	 */
	public static final int LOCAL_DATE_03 = 2;
	
	/**
	 * ex) "20110608044100:-0600" => "08/31/2011 on 12:44 pm"
	 */
	public static final int LOCAL_DATE_04 = 3;
	
	/**
	 * ex) "20110608044100:-0600" => "08/31/2011"
	 */
	public static final int LOCAL_DATE_05 = 4;
	
	/**
	 * ex) "20110608044100:-0600" => "08/31"
	 */
	public static final int LOCAL_DATE_06 = 5;
	
	
	/**
	 * String Date -> parsing
	 * @param date
	 * @return
	 */
	public static String convertDate2Local(String strDate, int localFlag)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss:z");
		
		Date date;
		try {
			date = sdf.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
			return strDate;
		}
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		
		SimpleDateFormat headDateFormat;
		SimpleDateFormat tailDateFormat;
		String outDate = "";
		switch (localFlag) {
		case LOCAL_DATE_01:
			headDateFormat = new SimpleDateFormat("MM-dd-yyyy");
			tailDateFormat = new SimpleDateFormat(" hh:mm a");
			outDate =  headDateFormat.format(date)+" at " + tailDateFormat.format(date);
			break;
		case LOCAL_DATE_02:
			headDateFormat = new SimpleDateFormat("M/d/yy");
			outDate =  headDateFormat.format(date);
			break;
		case LOCAL_DATE_03:
			headDateFormat = new SimpleDateFormat("h:mm a");
			outDate = headDateFormat.format(date);
			break;
		case LOCAL_DATE_04:
			headDateFormat = new SimpleDateFormat("MM/dd/yyyy 'on' hh:mm");
			tailDateFormat = new SimpleDateFormat("a");
			if( tailDateFormat.format(date).equals("AM") )
				outDate = headDateFormat.format(date) + " am";
			else if( tailDateFormat.format(date).equals("PM") )
				outDate = headDateFormat.format(date) + " pm ";
			else
				outDate = headDateFormat.format(date);
			break;
		case LOCAL_DATE_05:
			headDateFormat = new SimpleDateFormat("MM/dd/yyyy");
			outDate =  headDateFormat.format(date);
			break;
			
		case LOCAL_DATE_06:
			headDateFormat = new SimpleDateFormat("MM/dd");
			outDate =  headDateFormat.format(date);
			break;
		
		}
		
		return outDate;
	}
	
	public static interface DownloadImgObserver {
	  public void onCompleted(String file);
	}
	
	public static boolean isValidEmailAddress(String emailAddress){ 
		if( TextUtils.isEmpty(emailAddress) )
			return false;
		
		String emailPattern = "^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-+]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		Pattern pattern = Pattern.compile(emailPattern);
		
		Matcher matcher = pattern.matcher(emailAddress);
		return matcher.matches();

	}
	
	public static boolean isValidPassword(String password){
		if( TextUtils.isEmpty(password) )
			return false;

		if( password.length() < 4 )
			return false;
		
		return true;
	}
	
	public static boolean isNumber(String b){
		for(int i = 0; i < b.length(); i++){
			if(isNumber(b.charAt(i)) == false)
				return false;
		}
		return true;
	}
	public static boolean isNumber(char ch){
		if(ch >='0' && ch <='9')
			return true;
		else
			return false;
	}
	
	/**
	 * @param r
	 */
	public static void makeLooperThread(Runnable r){
		Looper.prepare();
		Handler handler = new Handler(Looper.getMainLooper());
		handler.post(r);
		Looper.loop();
	}
}
