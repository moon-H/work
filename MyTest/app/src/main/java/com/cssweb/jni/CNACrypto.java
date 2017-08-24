package com.cssweb.jni;

/**
 * Created by Wangjf on 2017/7/6.
 */
public class CNACrypto {
    public native int CNA_sm3_hash(byte[] input, byte[] output);

    public native int CNA_sm3_sign_hash(byte[] input, byte[] output);

    /**
     * Init CNA std curve parameters.
     *
     * @return 0 is OK.
     */
    public native int CNA_sm2_init_std_curve();

    public native int CNA_sm2_init_curve(byte[] p, byte[] a, byte[] b, byte[] gx, byte[] gy, byte[] n);

    /**
     * Set Signature USER_ID.
     *
     * @param uid use city code. String.getBytes("UTF8") , e.g. : “4401” for 广州
     * @return 0 is OK.
     */
    public native int CNA_sm2_set_uid(byte[] uid);

    /**
     * Set public key. public Key is Point (X,Y)
     *
     * @param pkX HEX String.getBytes("UTF8")
     * @param pkY HEX String.getBytes("UTF8")
     * @return 0 is OK.
     */
    public native int CNA_sm2_set_public_key(byte[] pkX, byte[] pkY);

    /**
     * Set Private KEY.
     *
     * @param d HEX String.getBytes("UTF8")
     * @return 0 is OK.
     */
    public native int CNA_sm2_set_private_key(byte[] d);

    /**
     * Verify data without SM3 Hash.
     *
     * @param signature 64 bytes sign data.
     * @param digest    SM3 Hash data.
     * @return 0 is OK.
     */
    public native int CNA_sm2_verify(byte[] signature, byte[] digest);

    /**
     * Verify data without SM3 Hash(include USER_ID).
     *
     * @param signature 64 bytes sign data.
     * @param msg       msg data.
     * @return 0 is OK.
     */
    public native int CNA_sm2_verify_with_sm3(byte[] signature, byte[] msg);

    /**
     * Signature using SM3 & SM2。
     *
     * @param pData data to sign .
     * @param sig   return sign data (64 bytes).
     * @return 0 is OK.
     */
    public native int CNA_sm2_sign(byte[] pData, byte[] sig);

    /**
     * Get y_bit for compressed key.
     *
     * @return
     */
    public native int CNA_sm2_get_public_key_y_bit();


    /**
     * Free CNA pointers.
     *
     * @return 0 is OK.
     */
    public native int CNA_free();
}
