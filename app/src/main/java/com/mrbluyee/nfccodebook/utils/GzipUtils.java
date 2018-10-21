package com.mrbluyee.nfccodebook.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GzipUtils {
    public static byte[] compress(String str){
        if (null == str || str.length() <= 0) {
            return null;
        }
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            GZIPOutputStream gzip = new GZIPOutputStream(out);
            gzip.write(str.getBytes("UTF-8"));
            gzip.close();
            return out.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String unCompress(byte[] data){
        if(data == null || data.length == 0){
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try{
            ByteArrayInputStream in = new ByteArrayInputStream(data);
            GZIPInputStream gzip = new GZIPInputStream(in);
            byte[] buffer = new byte[256];
            int n = -1;
            while((n = gzip.read(buffer)) > 0) {
                out.write(buffer, 0, n);
            }
            gzip.close();
            out.close();
        } catch (Exception e) {
            if(!(e instanceof EOFException)){
                e.printStackTrace();
            }
        }
        try {
            String result = out.toString("UTF-8");
            return result;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
