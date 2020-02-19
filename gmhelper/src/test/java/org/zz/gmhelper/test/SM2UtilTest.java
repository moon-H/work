package org.zz.gmhelper.test;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.junit.Assert;
import org.junit.Test;
import org.zz.gmhelper.BCECUtil;
import org.zz.gmhelper.SM2Util;
import org.zz.gmhelper.test.util.FileUtil;
import org.zz.gmhelper.util.HexConverter;

import java.math.BigInteger;
import java.security.KeyPair;
import java.util.Arrays;

public class SM2UtilTest extends GMBaseTest {

    @Test
    public void testSignAndVerify() {
        try {
//            AsymmetricCipherKeyPair keyPair = SM2Util.generateKeyPair();
//            ECPrivateKeyParameters priKey = (ECPrivateKeyParameters) keyPair.getPrivate();
//            ECPublicKeyParameters pubKey = (ECPublicKeyParameters) keyPair.getPublic();

            String priHex = "25F4F6590052E1BF5664747667B34BAFBF355240737C23B0E2BAE6EC308BFC55";
            String xHex = "6990795BFEAF66A11AA1105AB0D21980FBABC46968A00B95FFC1B82D655DB629";
            String yHex = "25900EC79267DFCD1DAEA170A115E13E524C5C6330382B89C5FFD8C95290BB34";
            byte[] byteY = ByteUtils.fromHexString(yHex);
            byte last = byteY[byteY.length - 1];
            String ybit = getBit(last);
            System.out.println("YYYY=" + ybit);
            String encodedPubHex = "04FF6712D3A7FC0D1B9E01FF471A87EA87525E47C7775039D19304E554DEFE0913F632025F692776D4C13470ECA36AC85D560E794E1BCCF53D82C015988E0EB956";
            String signHex = "111111";
            byte[] signBytes = ByteUtils.fromHexString(signHex);
            byte[] src = ByteUtils.fromHexString("0102030405060708010203040506070801020304050607080102030405060708");
            byte[] withId = ByteUtils.fromHexString("1111");

            ECPrivateKeyParameters priKey = new ECPrivateKeyParameters(
                    new BigInteger(ByteUtils.fromHexString(priHex)), SM2Util.DOMAIN_PARAMS);
            ECPublicKeyParameters pubKey = BCECUtil.createECPublicKeyParameters(xHex, yHex, SM2Util.CURVE, SM2Util.DOMAIN_PARAMS);


            System.out.println("Pri Hex:"
                    + ByteUtils.toHexString(priKey.getD().toByteArray()).toUpperCase());
            System.out.println("Pub X Hex:"
                    + ByteUtils.toHexString(pubKey.getQ().getAffineXCoord().getEncoded()).toUpperCase());
            System.out.println("Pub X Hex:"
                    + ByteUtils.toHexString(pubKey.getQ().getAffineYCoord().getEncoded()).toUpperCase());
            System.out.println("Pub Point Hex:"
                    + ByteUtils.toHexString(pubKey.getQ().getEncoded(false)).toUpperCase());

            byte[] sign = SM2Util.sign(priKey, withId, signBytes);
            byte[] decode22 = SM2Util.decodeDERSM2Sign(sign);
            System.out.println("SM2 sign with withId result:\n" + ByteUtils.toHexString(sign));
            System.out.println("SM2 sign with withId### :\n" + ByteUtils.toHexString(decode22));
            byte[] rawSign = SM2Util.decodeDERSM2Sign(sign);
            sign = SM2Util.encodeSM2SignToDER(rawSign);
            System.out.println("SM2 sign with withId result:\n" + ByteUtils.toHexString(sign));
            boolean flag = SM2Util.verify(pubKey, withId, signBytes, sign);
            System.out.println("-----------------:\n" + flag);
            if (!flag) {
                Assert.fail("verify failed");
            }

            sign = SM2Util.sign(priKey, SRC_DATA);
            System.out.println("SM2 sign without withId result:\n" + ByteUtils.toHexString(sign));
            flag = SM2Util.verify(pubKey, SRC_DATA, sign);
            if (!flag) {
                Assert.fail("verify failed");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testEncryptAndDecrypt() {
        try {
            AsymmetricCipherKeyPair keyPair = SM2Util.generateKeyPair();
            ECPrivateKeyParameters priKey = (ECPrivateKeyParameters) keyPair.getPrivate();
            ECPublicKeyParameters pubKey = (ECPublicKeyParameters) keyPair.getPublic();

            System.out.println("Pri Hex:"
                    + ByteUtils.toHexString(priKey.getD().toByteArray()).toUpperCase());
            System.out.println("Pub X Hex:"
                    + ByteUtils.toHexString(pubKey.getQ().getAffineXCoord().getEncoded()).toUpperCase());
            System.out.println("Pub X Hex:"
                    + ByteUtils.toHexString(pubKey.getQ().getAffineYCoord().getEncoded()).toUpperCase());
            System.out.println("Pub Point Hex:"
                    + ByteUtils.toHexString(pubKey.getQ().getEncoded(false)).toUpperCase());

            byte[] encryptedData = SM2Util.encrypt(pubKey, SRC_DATA);
            System.out.println("SM2 encrypt result:\n" + ByteUtils.toHexString(encryptedData));
            byte[] decryptedData = SM2Util.decrypt(priKey, encryptedData);
            System.out.println("SM2 decrypt result:\n" + ByteUtils.toHexString(decryptedData));
            if (!Arrays.equals(decryptedData, SRC_DATA)) {
                Assert.fail();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testKeyPairEncoding() {
        try {
            AsymmetricCipherKeyPair keyPair = SM2Util.generateKeyPair();
            ECPrivateKeyParameters priKey = (ECPrivateKeyParameters) keyPair.getPrivate();
            ECPublicKeyParameters pubKey = (ECPublicKeyParameters) keyPair.getPublic();

            byte[] priKeyPkcs8Der = BCECUtil.convertECPrivateKeyToPKCS8(priKey, pubKey);
            System.out.println("private key pkcs8 der length:" + priKeyPkcs8Der.length);
            System.out.println("private key pkcs8 der:" + ByteUtils.toHexString(priKeyPkcs8Der));
            FileUtil.writeFile("D:/ec.pkcs8.pri.der", priKeyPkcs8Der);

            String priKeyPkcs8Pem = BCECUtil.convertECPrivateKeyPKCS8ToPEM(priKeyPkcs8Der);
            FileUtil.writeFile("D:/ec.pkcs8.pri.pem", priKeyPkcs8Pem.getBytes("UTF-8"));
            byte[] priKeyFromPem = BCECUtil.convertECPrivateKeyPEMToPKCS8(priKeyPkcs8Pem);
            if (!Arrays.equals(priKeyFromPem, priKeyPkcs8Der)) {
                throw new Exception("priKeyFromPem != priKeyPkcs8Der");
            }

            BCECPrivateKey newPriKey = BCECUtil.convertPKCS8ToECPrivateKey(priKeyPkcs8Der);

            byte[] priKeyPkcs1Der = BCECUtil.convertECPrivateKeyToSEC1(priKey, pubKey);
            System.out.println("private key pkcs1 der length:" + priKeyPkcs1Der.length);
            System.out.println("private key pkcs1 der:" + ByteUtils.toHexString(priKeyPkcs1Der));
            FileUtil.writeFile("D:/ec.pkcs1.pri", priKeyPkcs1Der);

            byte[] pubKeyX509Der = BCECUtil.convertECPublicKeyToX509(pubKey);
            System.out.println("public key der length:" + pubKeyX509Der.length);
            System.out.println("public key der:" + ByteUtils.toHexString(pubKeyX509Der));
            FileUtil.writeFile("D:/ec.x509.pub.der", pubKeyX509Der);

            String pubKeyX509Pem = BCECUtil.convertECPublicKeyX509ToPEM(pubKeyX509Der);
            FileUtil.writeFile("D:/ec.x509.pub.pem", pubKeyX509Pem.getBytes("UTF-8"));
            byte[] pubKeyFromPem = BCECUtil.convertECPublicKeyPEMToX509(pubKeyX509Pem);
            if (!Arrays.equals(pubKeyFromPem, pubKeyX509Der)) {
                throw new Exception("pubKeyFromPem != pubKeyX509Der");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testSM2KeyRecovery() {
        try {
            String priHex = "67501B3F18221E8049E449BAF2B3EC79CCD308BCA996DADFC61DC676F1063783";
            String xHex = "924E76D5AF6CDDEA94120BEBE1704571C28B6990616C683BA56B870BE86DBF64";
            String yHex = "1452D77F3CBD32AFCDB1CD625FB3C28175D0028D47112DF25177632F406D343D";
            String encodedPubHex = "04FF6712D3A7FC0D1B9E01FF471A87EA87525E47C7775039D19304E554DEFE0913F632025F692776D4C13470ECA36AC85D560E794E1BCCF53D82C015988E0EB956";
            String signHex = "000000";
            byte[] signBytes = ByteUtils.fromHexString(signHex);
            byte[] src = ByteUtils.fromHexString("0102030405060708010203040506070801020304050607080102030405060708");
            byte[] withId = ByteUtils.fromHexString("0000");

            ECPrivateKeyParameters priKey = new ECPrivateKeyParameters(
                    new BigInteger(ByteUtils.fromHexString(priHex)), SM2Util.DOMAIN_PARAMS);
            ECPublicKeyParameters pubKey = BCECUtil.createECPublicKeyParameters(xHex, yHex, SM2Util.CURVE, SM2Util.DOMAIN_PARAMS);

            if (!SM2Util.verify(pubKey, src, signBytes)) {
                Assert.fail("verify failed");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testSM2KeyGen2() {
        try {
            AsymmetricCipherKeyPair keyPair = SM2Util.generateKeyPair();
            ECPrivateKeyParameters priKey = (ECPrivateKeyParameters) keyPair.getPrivate();
            ECPublicKeyParameters pubKey = (ECPublicKeyParameters) keyPair.getPublic();

            System.out.println("Pri Hex:"
                    + ByteUtils.toHexString(priKey.getD().toByteArray()).toUpperCase());
            System.out.println("Pub X Hex:"
                    + ByteUtils.toHexString(pubKey.getQ().getAffineXCoord().getEncoded()).toUpperCase());
            System.out.println("Pub X Hex:"
                    + ByteUtils.toHexString(pubKey.getQ().getAffineYCoord().getEncoded()).toUpperCase());
            System.out.println("Pub Point Hex:"
                    + ByteUtils.toHexString(pubKey.getQ().getEncoded(false)).toUpperCase());
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testEncodeSM2CipherToDER() {
        try {
            AsymmetricCipherKeyPair keyPair = SM2Util.generateKeyPair();
            ECPrivateKeyParameters priKey = (ECPrivateKeyParameters) keyPair.getPrivate();
            ECPublicKeyParameters pubKey = (ECPublicKeyParameters) keyPair.getPublic();

            byte[] encryptedData = SM2Util.encrypt(pubKey, SRC_DATA);

            byte[] derCipher = SM2Util.encodeSM2CipherToDER(encryptedData);
            FileUtil.writeFile("derCipher.dat", derCipher);

            byte[] decryptedData = SM2Util.decrypt(priKey, SM2Util.decodeDERSM2Cipher(derCipher));
            if (!Arrays.equals(decryptedData, SRC_DATA)) {
                Assert.fail();
            }

            Assert.assertTrue(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testGenerateBCECKeyPair() {
        try {
//            String priHex = "67501B3F18221E8049E449BAF2B3EC79CCD308BCA996DADFC61DC676F1063783";
//            String xHex = "924E76D5AF6CDDEA94120BEBE1704571C28B6990616C683BA56B870BE86DBF64";
//            String yHex = "1452D77F3CBD32AFCDB1CD625FB3C28175D0028D47112DF25177632F406D343D";
//            String encodedPubHex = "04FF6712D3A7FC0D1B9E01FF471A87EA87525E47C7775039D19304E554DEFE0913F632025F692776D4C13470ECA36AC85D560E794E1BCCF53D82C015988E0EB956";
//            String signHex = "000000";
//            byte[] signBytes = ByteUtils.fromHexString(signHex);
//            byte[] src = ByteUtils.fromHexString("0102030405060708010203040506070801020304050607080102030405060708");
//            byte[] withId = ByteUtils.fromHexString("0000");

//            ECPrivateKeyParameters priKey = new ECPrivateKeyParameters(
//                    new BigInteger(ByteUtils.fromHexString(priHex)), SM2Util.DOMAIN_PARAMS);
//            ECPublicKeyParameters pubKey = BCECUtil.createECPublicKeyParameters(xHex, yHex, SM2Util.CURVE, SM2Util.DOMAIN_PARAMS);

            //-------------------------------------
            KeyPair keyPair = SM2Util.generateBCECKeyPair();
            ECPrivateKeyParameters priKey = SM2Util.convertPrivateKey((BCECPrivateKey) keyPair.getPrivate());
            ECPublicKeyParameters pubKey = SM2Util.convertPublicKey((BCECPublicKey) keyPair.getPublic());

            byte[] sign = SM2Util.sign(priKey, WITH_ID, SRC_DATA);
            System.out.println("--------------结果--:" + HexConverter.bytesToHexString(sign));


            boolean flag = SM2Util.verify(pubKey, WITH_ID, SRC_DATA, sign);
            if (!flag) {
                Assert.fail("verify failed");
            }

            sign = SM2Util.sign(priKey, SRC_DATA);
            flag = SM2Util.verify(pubKey, SRC_DATA, sign);
            if (!flag) {
                Assert.fail("verify failed");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail();
        }
    }

    public static String getBit(byte by) {
        StringBuffer sb = new StringBuffer();
        sb.append((by >> 7) & 0x1).append((by >> 6) & 0x1).append((by >> 5) & 0x1).append((by >> 4) & 0x1).append((by >> 3) & 0x1).append((by >> 2) & 0x1).append((by >> 1) & 0x1).append((by >> 0) & 0x1);
        return sb.toString();
    }

}
