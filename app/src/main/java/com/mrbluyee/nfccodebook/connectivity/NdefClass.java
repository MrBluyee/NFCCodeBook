package com.mrbluyee.nfccodebook.connectivity;

import android.nfc.FormatException;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.Ndef;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Locale;

public class NdefClass{
    private Tag mTag = null;

    public NdefClass(Tag mTag){
        this.mTag = mTag;
    }

    public byte[] getId(){
        if(mTag != null) {
            return mTag.getId();
        }else {
            return null;
        }
    }

    private NdefRecord createTextRecord(byte[] datas) {
        byte[] langBytes = Locale.CHINA.getLanguage().getBytes(Charset.forName("US-ASCII"));
        //设置状态字节编码最高位数为0
        int utfBit = 0;
        //定义状态字节
        char status = (char) (utfBit + langBytes.length);
        byte[] data = new byte[1 + langBytes.length + datas.length];
        //设置第一个状态字节，先将状态码转换成字节
        data[0] = (byte) status;
        //设置语言编码，使用数组拷贝方法，从0开始拷贝到data中，拷贝到data的1到langBytes.length的位置
        System.arraycopy(langBytes, 0, data, 1, langBytes.length);
        //设置文本字节，使用数组拷贝方法，从0开始拷贝到data中，拷贝到data的1 + langBytes.length
        //到textBytes.length的位置
        System.arraycopy(datas, 0, data, 1 + langBytes.length, datas.length);
        //通过字节传入NdefRecord对象
        //NdefRecord.RTD_TEXT：传入类型 读写
        NdefRecord ndefRecord = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,
                NdefRecord.RTD_TEXT, new byte[0], data);
        return ndefRecord;
    }

    private String parseTextRecord(NdefRecord ndefRecord) {
        /**
         * 判断数据是否为NDEF格式
         */
        //判断TNF
        if (ndefRecord.getTnf() != NdefRecord.TNF_WELL_KNOWN) {
            return null;
        }
        //判断可变的长度的类型
        if (!Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
            return null;
        }
        try {
            //获得字节数组，然后进行分析
            byte[] payload = ndefRecord.getPayload();
            //下面开始NDEF文本数据第一个字节，状态字节
            //判断文本是基于UTF-8还是UTF-16的，取第一个字节"位与"上16进制的80，16进制的80也就是最高位是1，
            //其他位都是0，所以进行"位与"运算后就会保留最高位
            String textEncoding = ((payload[0] & 0x80) == 0) ? "UTF-8" : "UTF-16";
            //3f最高两位是0，第六位是1，所以进行"位与"运算后获得第六位
            int languageCodeLength = payload[0] & 0x3f;
            //下面开始NDEF文本数据第二个字节，语言编码
            //获得语言编码
            String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
            //下面开始NDEF文本数据后面的字节，解析出文本
            String textRecord = new String(payload, languageCodeLength + 1,
                    payload.length - languageCodeLength - 1, textEncoding);
            return textRecord;
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }

    public int writeNdef(byte[] datas){
        if (mTag==null){
            return -1;
        }
        Ndef ndef=Ndef.get(mTag);//获取ndef对象
        if (!ndef.isWritable()){
            return -2;
        }
        NdefRecord ndefRecord=createTextRecord(datas);//创建一个NdefRecord对象
        NdefMessage ndefMessage=new NdefMessage(new NdefRecord[]{ndefRecord});//根据NdefRecord数组，创建一个NdefMessage对象
        int size=ndefMessage.getByteArrayLength();
        if (ndef.getMaxSize()<size){
            return -3;
        }
        try {
            ndef.connect();//连接
            ndef.writeNdefMessage(ndefMessage);//写数据
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FormatException e) {
            e.printStackTrace();
        }finally {
            try {
                ndef.close();//关闭连接
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public String readNdef(){
        String result = null;
        if (mTag==null){
            return result;
        }
        Ndef ndef=Ndef.get(mTag);//获取ndef对象
        try {
            ndef.connect();//连接
            NdefMessage ndefMessage=ndef.getNdefMessage();//获取NdefMessage对象
            if (ndefMessage!=null){
                result = parseTextRecord(ndefMessage.getRecords()[0]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FormatException e) {
            e.printStackTrace();
        }finally {
            try {
                ndef.close();//关闭链接
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
