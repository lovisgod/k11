package com.lovisgod.iswhpay.data;

import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.horizonpay.smartpossdk.aidl.pinpad.AidlPinPadInputListener;
import com.horizonpay.smartpossdk.aidl.pinpad.IAidlPinpad;
import com.horizonpay.smartpossdk.data.PinpadConst;
import com.horizonpay.utils.ConvertUtils;
import com.horizonpay.utils.FormatUtils;
import com.lovisgod.iswhpay.utils.DeviceHelper;
import com.lovisgod.iswhpay.utils.DeviceUtils;
import com.lovisgod.iswhpay.utils.HexUtil;
import com.lovisgod.iswhpay.utils.IswHpCodes;
import com.lovisgod.iswhpay.utils.TripleDES;

/***************************************************************************************************
 *                          Copyright (C),  Shenzhen Horizon Technology Limited                    *
 *                                   http://www.horizonpay.cn                                      *
 ***************************************************************************************************
 * usage           :
 * Version         : 1
 * Author          : Ashur Liu
 * Date            : 2017/12/18
 * Modify          : create file
 **************************************************************************************************/
public class PinPad3DesHandler {

    private static final String TAG = "PinPad3DesHandler";
    private static final int KEK_KEY_INDEX = 0;
    private static final int MASTER_KEY_INDEX = 0;
    private static final int WORK_KEY_INDEX = 0;
    static IAidlPinpad innerpinpad;
    private static boolean isSupport;


    static public void inititial() {

        try {
            innerpinpad = DeviceHelper.getPinpad();
            isSupport = innerpinpad.isSupport();
            innerpinpad.setKeyAlgorithm(PinpadConst.KeyAlgorithm.DES);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }



    static void loadClearMasterKey(String key) {
        try {
            //MK: 4B9F30D51BCB6A594B9F30D51BCB6A59
            byte[] masterKey = HexUtil.hexStringToByte(key);
            boolean result = innerpinpad.injectClearTMK(MASTER_KEY_INDEX, masterKey, new byte[4]);
            if (result) {
                System.out.println( "loadClearMasterKey  success");
            } else {
                System.out.println("loadClearMasterKey  failed");
            }

        } catch (RemoteException e) {
            Log.d(TAG, e.getMessage());
        }
    }

    static int loadMasterKey(String key) {
        int keyReturn;
        try {
            byte[] masterKey = HexUtil.hexStringToByte(key);
            byte[] kcv = HexUtil.hexStringToByte("40826A58");
            boolean result = innerpinpad.injectSecureTMK(KEK_KEY_INDEX, MASTER_KEY_INDEX, masterKey, kcv);
            if (result) {
                keyReturn = 0;
                DeviceUtils.INSTANCE.showText("injectSecureTMK success");
            } else {
                keyReturn = IswHpCodes.PIN_LOAD_ERROR;
                DeviceUtils.INSTANCE.showText("injectSecureTMK failed");
            }
            getRandomNumber(2);
            return keyReturn;
        } catch (RemoteException e) {
            Log.d(TAG, e.getMessage());
            return IswHpCodes.PIN_LOAD_ERROR;
        }

    }

    public static String getRandomNumber(int len) {
        if (len > 16) {
            return null;
        }
        byte[] aa = new byte[0];
        byte[] bb = new byte[0];
        byte[] cc = new byte[0];
        byte[] dd = new byte[0];
        try {
            aa = innerpinpad.getRandom();
            bb = innerpinpad.getRandom();
            cc = innerpinpad.getRandom();
            dd = innerpinpad.getRandom();
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }

        String s = ConvertUtils.bytes2HexString(aa) + ConvertUtils.bytes2HexString(bb) + ConvertUtils.bytes2HexString(cc) + ConvertUtils.bytes2HexString(dd);
        Log.d(TAG, "getRandomNumber: " + s);

        StringBuffer stringBuffer = new StringBuffer();

        s = s.toLowerCase();
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isLowerCase(s.charAt(i))) {
                stringBuffer.append(s.charAt(i));
            }
        }

        Log.d(TAG, "getRandomNumber: " + stringBuffer.toString());

        if (stringBuffer.toString().length() < 16) {
            for (int i = stringBuffer.toString().length(); i < 16; i++) {
                stringBuffer.append("0");
            }
        }

        Log.d(TAG, "getRandomNumber: " + stringBuffer.toString());

        return stringBuffer.toString().substring(0,len);
    }

    static public int loadPinkey(String key) {
        try {
            //Encrypted PIN key
            byte[] pinKey = HexUtil.hexStringToByte(key);
//            byte[] kcv = HexUtil.hexStringToByte("D2DB51F1");
            byte[] kcv = HexUtil.hexStringToByte("00000000");
            boolean result = innerpinpad.injectWorkKey(WORK_KEY_INDEX, PinpadConst.PinPadKeyType.TPINK, pinKey, kcv);
            if (result) {
                DeviceUtils.INSTANCE.showText("load pin key success");
                return 0;
            } else {
                DeviceUtils.INSTANCE.showText("load pin key failed");
                return IswHpCodes.PIN_LOAD_ERROR;
            }
        } catch (RemoteException e) {
            Log.d(TAG, e.getMessage());
            return IswHpCodes.PIN_LOAD_ERROR;
        }
    }


    static public void loadWorkKey() {
        //Pin key: 35353535353535353535353535353535
        try {
            //Encrypted PIN key
            byte[] pinKey = HexUtil.hexStringToByte("D0FB24EA73F599C1D0FB24EA73F599C1");
            byte[] kcv = HexUtil.hexStringToByte("D2DB51F1");   // load PIN key "35353535353535353535353535353535"
            kcv = HexUtil.hexStringToByte("00000000");  // load PIN key "D0FB24EA73F599C1D0FB24EA73F599C1"
            boolean result = innerpinpad.injectWorkKey(WORK_KEY_INDEX, PinpadConst.PinPadKeyType.TPINK, pinKey, kcv);
            if (result) {
                DeviceUtils.INSTANCE.showText("load pin key success");
            } else {
                DeviceUtils.INSTANCE.showText("load pin key failed");
            }
        } catch (RemoteException e) {
            Log.d(TAG, e.getMessage());
        }
        //Mac key: 33333333333333333333333333333333
        try {
            //Encrypted Mac key
            byte[] mackey = HexUtil.hexStringToByte("4BF6E91B1E3A9D814BF6E91B1E3A9D81");
            byte[] kcv = HexUtil.hexStringToByte("ADC67D84");
            boolean result = innerpinpad.injectWorkKey(WORK_KEY_INDEX, PinpadConst.PinPadKeyType.TMACK, mackey, kcv);
            if (result) {
                DeviceUtils.INSTANCE.showText("load mac key success");
            } else {
                DeviceUtils.INSTANCE.showText("load mac key failed");
            }
        } catch (RemoteException e) {
            Log.d(TAG, e.getMessage());
        }
        //Data key: 37373737373737373737373737373737
        try {
            //plain data key
            byte[] datakey = HexUtil.hexStringToByte("16B2CCB944DA2CE916B2CCB944DA2CE9");
            byte[] kcv = HexUtil.hexStringToByte("3AA3EA2D");
//            byte[] kcv = new byte[4];
            boolean result = innerpinpad.injectWorkKey(WORK_KEY_INDEX, PinpadConst.PinPadKeyType.TDATAK, datakey, kcv);
            if (result) {
                DeviceUtils.INSTANCE.showText("load td key success");
            } else {
                DeviceUtils.INSTANCE.showText("load td key failed");
            }
        } catch (RemoteException e) {
            Log.d(TAG, e.getMessage());
        }

    }


//    private void inputPin() {
//        try {
//            innerpinpad.inputOnlinePin(new Bundle(), new int[]{4, 4}, 10, "1234567890123456", 0, PinpadConst.PinAlgorithmMode.ISO9564FMT1, new AidlPinPadInputListener.Stub() {
//                @Override
//                public void onConfirm(byte[] data, boolean noPin, String s) throws RemoteException {
//
//                    byte[] formatedData = FormatUtils.appendArray(data, (data.length + 7) / 8 * 8, true, (byte) 0x00);
//                    String builder = "Is Pin input:" + (noPin ? "No" : "Yes") +
//                            "\npinBlock:" + HexUtil.bytesToHexString(data) +
//                            "\nen pin:" + HexUtil.bytesToHexString(formatedData);
//                    try {
//
//                        String dec = TripleDES.decrypt("1234567890123456", HexUtil.bytesToHexString(data), "D0FB24EA73F599C1D0FB24EA73F599C1");
//                        System.out.println("decryped ::::" + dec);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    DeviceUtils.INSTANCE.showText(builder);
//                }
//
//                @Override
//                public void onSendKey(int keyCode) throws RemoteException {
//                }
//
//                @Override
//                public void onCancel() throws RemoteException {
//                    DeviceUtils.INSTANCE.showText("inputOnlinePin: onCancel");
//                }
//
//                @Override
//                public void onError(int errorCode) throws RemoteException {
//                    DeviceUtils.INSTANCE.showText( "inputOnlinePin:" + "onError:" + errorCode);
//
//                }
//            });
//        } catch (RemoteException e) {
//            e.printStackTrace();
//            DeviceUtils.INSTANCE.showText("inputOnlinePin:" + "RemoteException");
//        }
//    }



    private void calcMac() {
        byte[] data = HexUtil.hexStringToByte("00000000005010016222620910029130840241205100100367FD3414057DB801BE18A309A544C5174CC777525974CBD467BCC56EA16629F3B016488A6C314921485C75F57066D4682FEDC1F910C5C8136A201279B590898B40D7098461D345168810CCFEBC61204B3E6F364A95175EF54C7EBAAEC2A6AEE44D9783747124D313B78A3F754C5ECC611533C4957377DD2067DF927C80461C4E4C20A8A4CC57EF1CCE2BC1AEEA442431256F66A25AB855912BA82FB8AD308F0EDE358CDDDEA63C95401B8335C8689E5735E0FB96733426FD71A7248E140A95CB4B4313AC0DBDA1E70EA8800000000000");
        byte[] mac = new byte[0];
        try {
            mac = innerpinpad.calcMac(WORK_KEY_INDEX, PinpadConst.MacType.TYPE_CUP_ECB, data, new byte[16]);
            //System.out.println("Random Data:"+ HexUtil.bytesToHexString(innerpinpad.getRandom()));
        } catch (RemoteException e) {
            e.printStackTrace();
        }


        DeviceUtils.INSTANCE.showText( "Calc mac:::::" + HexUtil.bcd2Str(mac));

    }

}