/**
 * Created by Wangjf on 2017/7/6.
 */

package com.cssweb.jni;

public class QRCodeUsbPosImpl {

    // 0x31 is usepage of USB keyboard.
    public final int HID_KBW_PROTOCOL = 0x31;
    // 0x8C is usepage of Barcode Scanner.
    public final int HID_POS_PROTOCOL = 0x8C;

    public native int Init();

    public native void StartWork();

    public native void StopWork();

    public native int GetStatus();

    public native int GetVendorId();

    public native int GetProductId();

    public native int GetProtocol();

    public native byte[] GetQRCodeString();

}
