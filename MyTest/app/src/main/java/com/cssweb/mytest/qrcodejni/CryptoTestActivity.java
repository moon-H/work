package com.cssweb.mytest.qrcodejni;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;

import com.cssweb.framework.utils.HexConverter;
import com.cssweb.framework.utils.MLog;
import com.cssweb.mytest.R;
import com.cssweb.mytest.Utils;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by liwx on 2017/8/17.
 */

public class CryptoTestActivity extends FragmentActivity {
    private static final String TAG = "CryptoTestActivity";
    private CryptoManager mCryptoManager;
    SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);

    private byte[] mSignRes = new byte[64];
    //-----------OK START--------
//    private String org = "ABBA12344321CD";
//    String keyX = "CA0E5E2B3A7D379ED4C7F03F0C329E6A31D866169FBEB86A246E2BA022F8B938";
//    String keyY = "6B93791FC6F2D6264095EA10F3E7F94003AC9983F6BD0C9A7615D035C47EDE76";
//    String privateKey = "205F05024063476B7678A218B06CE124D06402AD370183C6198FB03F3B382009";
//    String uId = "4401";
    private String org = "0FDF5D399061EEB5D7DD734A7D288BF32B7B10FB7427E8BA6A73DD73CD333E745A0D4A240000019C";
    String keyX = "461D273AD64AA055C72E91D1CF0FD799F1FD5441871F4B4BB2999068B8EFEE29";
    String keyY = "3CAD306E7B69C0F727814B1568EE5BC5E286EDCF83A9C559A31B93617F5A350A";
    String privateKey = "205F05024063476B7678A218B06CE124D06402AD370183C6198FB03F3B382009";
    String uId = "0000019C";
    //-----------OK END--------
    //    private String org = "3621DFF3502A4697D9818287600430B4840BD80D9F211BD32AE1E75F30AFBE07E8656AC451E6E7E3CF2B408DF2580F3B20FDAA0221F102BB0F320CB1F476ED8F13948726A41A088A066B69EE7DD57F24B0288E896A37A8D1FA1ACB696F8CB51613F7A158709090417F1E2FFE1853ECF8E0E14666B07D3D8456574551B540FFC7410159E9A1873220DB1F6FA463530711250591D4E060E94B50F9CCC2F0AA3CA73193FB1C1D851FC7170247E5B3A923B07DC17216AF91746900E790CC990894C7B8715B8EF35105030659DF05940000001259E0678C05";
    //    String keyX = "F7A158709090417F1E2FFE1853ECF8E0E14666B07D3D8456574551B540FFC741";
    //    String keyY = "F28E8310D8BEA07741192CAE7C9B56965F333C2D05348929C5140278BADE52ED";
    //    String privateKey = "F5F637CEBBAA88FE4C7E68FD60B863BA66C6BDFE597C764FD02DE7F1BC60DCD7";
    //    String uId = "00000052";
    //    private String org = "6DA3584F3838318EFCCE9354943E34E92401C72DA7A51543F86D8A6414B84D09A97C47969EF3EB08A0F8DC67809D9C7B9D33E211619124E4ED0BE577349DA15F6F7C2199B8AA1729CB6B6F86A261700FC40C952C4A52870E9D30F4DB271F0A720D188C02EDB6F2A084BE5AD3CF1CD894A8FD46A682400DBEA5998B103918F1DC9C0159EEA87FF4DF68EE3C8F5EDBBE43E6AC32DEBDF1943D398686404BD1C375090853FD1728907FBA2CD84DD5274C876F9465E3B347D70D431B26A30FB9A5924DE88567558705030659E56C340000000159E5DE8F05";
    //    String keyX = "188C02EDB6F2A084BE5AD3CF1CD894A8FD46A682400DBEA5998B103918F1DC9C";
    //    String keyY = "80A19C568B44F058150D633CEE427D0E4888A7FEA73C0982908E7404ED97ADEB";
    //    String privateKey = "6EC0A87A1D5BC656C5468EBCA86EC2A613363C2256728A50DC53C8FDC3F8F278";
    //    String uId = "4401";


    //---------------
    String keyWrapValue = "137E216DCFE138A55A0C706C73B57190";
    String WK = "691D32E64798C4D9691D32E64798C4D9";
    String keyPrivate = "D3BE365606213017AABCD3219CC52D79A9173628C541EF94EA60930F076CAC58A0F0BB3647FC5D42";
    String PK = "8356FACDC0A5B02AE090E9463F5D23E7814FE016A92D355F684B760788D00D64";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_jnitest);
        Log.d(TAG, "onCreate");
        lookDeviceInfo();
        //        asyncUse();
        //        byte[] signData = CryptoManager.getInstance().signData(keyX + keyY, privateKey, uId, org);
        //        if (signData != null) {
        //            MLog.d(TAG, "onSignSuccess " + HexConverter.bytesToHexString(signData));
        //        } else {
        //            MLog.d(TAG, "onSignFailed ");
        //        }

        byte[] signData = HexConverter.hexStringToBytes("98E866BAE0087E5F2BC0917E440E5CEA74A3A0E5A5ADC8EC8E853CB2E3691E2EA0434364039D1018A2A0D866A72086D9B3CA91E00289DA4ABFBEF3B466EC2FF4");

        boolean result = CryptoManager.getInstance().verifyData(keyX + keyY, privateKey, uId, signData, org);
        MLog.d(TAG, "verify result " + result);


        byte[] orgWKey = DpCryptoUtil.showData(HexConverter.bytesToHexString(HexConverter.hexStringToBytes(keyWrapValue)));
        MLog.d(TAG, "work key =  " + HexConverter.bytesToHexString(orgWKey));
        try {
            byte[] privateKeyByte = PostPayDES.SDecrypt_DES(HexConverter.hexStringToBytes(keyPrivate), orgWKey);
            MLog.d(TAG, "private key =  " + HexConverter.bytesToHexString(privateKeyByte));
        } catch (Exception e) {
            e.printStackTrace();
        }

        MLog.d(TAG, "BASE = " + Base64.encodeToString(HexConverter.hexStringToBytes(privateKey), Base64.NO_WRAP));
        MLog.d(TAG, "BASE decode  = " + new String(Base64.decode("hNWWumkWsNGsDTXYQhOUwI9g3bMGAAAAAAAAAASuhAOXa3uidKvieJ1mQH5iYQa3+Q2RGXxdkIRmDtKfqdyeUaSiq+mVNuPb2NLMV4t3OEqqXOhT4z0jWx8bpFzKWY8jHu/9/3039XYdb1/V8gDY54bNw7SZA9Yv3+m6+dMuK9ebF7ALjO3IQLQ/EAWijctBltV20DRroJUclIkGtqYtm7k/OCn3gN4wCkZRRno=", Base64.NO_WRAP)));


        MLog.d(TAG, "time = " + Integer.parseInt("59E72A69", 16));
        MLog.d(TAG, "time = " + Integer.parseInt("59e72ae9", 16));


        String[] times = {"59E99A8B", "59E99B03"};
        for (String string : times) {
            long time = Integer.parseInt(string, 16);
            MLog.d(TAG, "string = " + string + " " + time + " " + mDateFormat.format(time * 1000));
        }

        String amount = Utils.addLeftChar(Integer.toHexString(300), 2, "0");
        MLog.d(TAG, "amount = " + amount);
        MLog.d(TAG, "status = " + Integer.valueOf(04));
        long handleTime1 = Integer.valueOf(HexConverter.bytesToHexString(HexConverter.hexStringToBytes("0004")), 16);
        MLog.d(TAG, "HANDLE----" + handleTime1);
        //        int yBit = CryptoManager.getInstance().getYBit(keyX + keyY, privateKey, uId);
        //        MLog.d(TAG, "yBit= " + yBit);
        //        String pubKey = "5B32213E742A5618ECA72BBFC33E800EF3B4D97552918F368B4C4A9ACA07BD879E7AA60C0C5521042E766233AF52E1D9FE1E6F9BDE2351E779E842239D45A687";
        //        int yBit = CryptoManager.getInstance().getYBit(pubKey, privateKey, uId);
        //        MLog.d(TAG, "----- yBit= " + yBit);
        //        String base = "062E8B1A2C088C0267D8C802CBE7806B";
        //        String base = "78F8EA914DD00325F57D8F9DA2358C3DCC6E6C5B5E1196CB31988F3168AAF726F2613B983048DD8F6970A0419CDC6136BB7DFE149D165378783950F9E2CDE0C5C0C687FFC4E600B09CF263D8D1E56373857B97DFCBC666FFF14FF1C9D40B5F7D0D5B32213E742A5618ECA72BBFC33E800EF3B4D97552918F368B4C4A9ACA07BD870159b0a0237B4341A9D2767D300885E7F733B5FB24996E2B6EF1B77C40F0820F17AC7AE8EC2A93E477B2518F15401A813DF896EFAB165F029284B2461C1C42673B2FDAE2ED03000059a7f8360000000059a8f5580a15594B1287C1C0B4C2FB27C132B1B08F6543BCBBA46DBEA1E895722B04263B5187CBA3D6206F229CED0B6F3D3EB65DC5A896F10CB697BB2CA531363656AA6AA2";
        //
        //        byte[] res = Base64.encode(HexConverter.hexStringToBytes(base), Base64.NO_WRAP);
        //        MLog.d(TAG, "---Base64-1---" + HexConverter.bytesToHexString(res));
        //        MLog.d(TAG, "---Base64--2--" + new String(res));
        //        String resStr = Base64.encodeToString(HexConverter.hexStringToBytes(base), Base64.NO_WRAP);
        //        MLog.d(TAG, "---Base64--3--" + resStr);

    }

    /**
     * 异步回调方式
     */
    private void asyncUse() {
        //        mCryptoManager = new CryptoManager(this);
        //        mCryptoManager.signData(keyX, keyY, privateKey, uId, org, new CryptoManager.CryptoListener() {
        //            @Override
        //            public void onSignSuccess(final byte[] singData) {
        //                MLog.d(TAG, "onSignSuccess " + HexConverter.bytesToHexString(singData));
        //                //                new Handler().postDelayed(new Runnable() {
        //                //                    @Override
        //                //                    public void run() {
        //                //                        verifyData(singData);
        //                //                    }
        //                //                }, 1000);
        //
        //            }
        //
        //            @Override
        //            public void onVerifySuccess() {
        //
        //            }
        //
        //            @Override
        //            public void onOptionFailed(int errCode, int nativeCode) {
        //                MLog.d(TAG, "sign failed " + errCode + " " + nativeCode);
        //            }
        //        });
    }

    //    private void verifyData(byte[] singData) {
    //        mCryptoManager.verifyData(keyX+keyY, privateKey, uId, singData, org, new CryptoManager.CryptoListener() {
    //            @Override
    //            public void onSignSuccess(byte[] singData) {
    //
    //            }
    //
    //            @Override
    //            public void onVerifySuccess() {
    //                MLog.d(TAG, "onVerifySuccess");
    //            }
    //
    //            @Override
    //            public void onOptionFailed(int errCode, int nativeCode) {
    //                MLog.d(TAG, "verify failed " + errCode + " " + nativeCode);
    //            }
    //        });
    //    }

    private void lookDeviceInfo() {
        StringBuilder builder = new StringBuilder();
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                builder.append(field.getName() + "---" + field.get(null).toString() + "\n");
                //                mInfosMap.put(field.getName(), field.get(null).toString());
                // MLog.d(TAG, field.getName() + " : " + field.get(null));
            } catch (Exception e) {
            }
        }
        Log.i(TAG, "----" + builder.toString());

    }

    private byte[] getBytesUTF8(String str) {
        byte[] value;
        try {
            value = str.getBytes("UTF-8");
            //            MLog.d(TAG,"getBytesUTF8 value = "+" org = "+str+"  "+HexConverter.bytesToHexString(value));
            return value;
        } catch (Exception e) {
            MLog.d(TAG, "getBytesUTF8 occur error:" + " org =" + str, e);
            return null;
        }
    }

}
