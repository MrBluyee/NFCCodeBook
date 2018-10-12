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

public class MifareClassicClass{
    private Tag mTag = null;
    private  MifareClassic mfc = null;

    public MifareClassicClass(Tag mTag){
        this.mTag = mTag;
        if(this.mTag != null) {
            mfc = MifareClassic.get(this.mTag);
        }
    }

    public byte[] getId(){
        if(mTag != null) {
            return mTag.getId();
        }else {
            return null;
        }
    }

    public boolean authenticateSector(int sector,byte[] key,int keytype)throws IOException{
        boolean result = false;
        if (keytype == 0) {
            result = mfc.authenticateSectorWithKeyA(sector, key);
        } else {
            result = mfc.authenticateSectorWithKeyB(sector, key);
        }
        return result;
    }
/*
    private int writeBlocks(int startBlock,int endBlock,byte[] data){
        int blockIndex = startBlock;
        //if(authenticateSector(int sector,byte[] key,int keytype){

        //}
    }
*/
    public int writeBlock(Tag mTag, int sector,int block,byte[] data){
        int status = 0;
        if (mTag == null){
            return -1;
        }
        MifareClassic mfc=MifareClassic.get(mTag);
        try {
            mfc.connect();//打开连接
            boolean auth;
            auth=mfc.authenticateSectorWithKeyA(sector,MifareClassic.KEY_DEFAULT);//keyA验证扇区
            if (auth){
                mfc.writeBlock(block,data);//写入数据
            }else{
                status = -2;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                mfc.close();//关闭连接
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return status;
    }
/*
    private byte[] readBlock(){
        byte[] blockData = null;
        if (mTag==null){

        }

        MifareClassic mfc=MifareClassic.get(mTag);
        try {
           // mfc.connect();//打开连接
            boolean auth;
            //int sector=Integer.parseInt(sectorNum.getText().toString().trim());//写入的扇区
            //int block=Integer.parseInt(blockNum.getText().toString().trim());//写入的块区
            //auth=mfc.authenticateSectorWithKeyA(sector,MifareClassic.KEY_DEFAULT);//keyA验证扇区
           // if (auth){
              //  blockData = mfc.readBlock(block);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                mfc.close();//关闭连接
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/
}