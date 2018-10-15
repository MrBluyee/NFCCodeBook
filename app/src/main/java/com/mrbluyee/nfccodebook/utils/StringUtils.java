package com.mrbluyee.nfccodebook.utils;

import android.util.Log;

import java.util.Random;

public class StringUtils {
    //字符序列转换为16进制字符串
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("0x");
        if (src == null || src.length <= 0) {
            return null;
        }
        char[] buffer = new char[2];
        for (int i = 0; i < src.length; i++) {
            buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
            buffer[1] = Character.forDigit(src[i] & 0x0F, 16);
            stringBuilder.append(buffer);
        }
        return stringBuilder.toString();
    }

    public static String byteToHexString(byte src) {
        StringBuilder stringBuilder = new StringBuilder("0x");
        char[] buffer = new char[2];
        buffer[0] = Character.forDigit((src >>> 4) & 0x0F, 16);
        buffer[1] = Character.forDigit(src & 0x0F, 16);
        stringBuilder.append(buffer);
        return stringBuilder.toString();
    }

    public static char getRandomLowerChar() {
        return (char)(new Random().nextInt(26) + 97);
    }

    public static char getRandomNumberChar() {
        return (char)(new Random().nextInt(10) + 48);
    }

    public static char getRandomCapitalChar() {
        return (char)(new Random().nextInt(26) + 65);
    }

    public static char getRandomSpecialChar() {
        Random random = new Random();
        int number = random.nextInt(4);
        long result=0;
        switch(number){
            case 0:
                result = random.nextInt(15) + 33;
                break;
            case 1:
                result = random.nextInt(7) + 58;
                break;
            case 2:
                result = random.nextInt(6) + 91;
                break;
            case 3:
                result = random.nextInt(4) + 123;
                break;
         }
        return (char)result;
    }

    public static char getRandomLowerNumberChar(){
        Random random=new Random();
        int number=random.nextInt(2);
        char result = 0;
        switch(number){
            case 0:
                result = getRandomLowerChar();
                break;
            case 1:
                result = getRandomNumberChar();
                break;
        }
        return result;
    }

    public static char getRandomLowerCapitalChar(){
        Random random=new Random();
        int number=random.nextInt(2);
        char result = 0;
        switch(number){
            case 0:
                result = getRandomLowerChar();
                break;
            case 1:
                result = getRandomCapitalChar();
                break;
        }
        return result;
    }

    public static char getRandomLowerSpecialChar(){
        Random random=new Random();
        int number=random.nextInt(2);
        char result = 0;
        switch(number){
            case 0:
                result = getRandomLowerChar();
                break;
            case 1:
                result = getRandomSpecialChar();
                break;
        }
        return result;
    }

    public static char getRandomNumberCapitalChar(){
        Random random=new Random();
        int number=random.nextInt(2);
        char result = 0;
        switch(number){
            case 0:
                result = getRandomCapitalChar();
                break;
            case 1:
                result = getRandomNumberChar();
                break;
        }
        return result;
    }

    public static char getRandomNumberSpecialChar(){
        Random random=new Random();
        int number=random.nextInt(2);
        char result = 0;
        switch(number){
            case 0:
                result = getRandomSpecialChar();
                break;
            case 1:
                result = getRandomNumberChar();
                break;
        }
        return result;
    }

    public static char getRandomCapitalSpecialChar(){
        Random random=new Random();
        int number=random.nextInt(2);
        char result = 0;
        switch(number){
            case 0:
                result = getRandomCapitalChar();
                break;
            case 1:
                result = getRandomSpecialChar();
                break;
        }
        return result;
    }

    public static char getRandomLowerNumberCapitalChar() {
        Random random=new Random();
        int number=random.nextInt(3);
        char result = 0;
        switch(number){
            case 0:
                result = getRandomLowerChar();
                break;
            case 1:
                result = getRandomNumberChar();
                break;
            case 2:
                result = getRandomCapitalChar();
                break;
        }
        return result;
    }

    public static char getRandomNumberCapitalSpecialChar() {
        Random random=new Random();
        int number=random.nextInt(3);
        char result = 0;
        switch(number){
            case 0:
                result = getRandomSpecialChar();
                break;
            case 1:
                result = getRandomNumberChar();
                break;
            case 2:
                result = getRandomCapitalChar();
                break;
        }
        return result;
    }

    public static char getRandomLowerCapitalSpecialChar() {
        Random random=new Random();
        int number=random.nextInt(3);
        char result = 0;
        switch(number){
            case 0:
                result = getRandomSpecialChar();
                break;
            case 1:
                result = getRandomLowerChar();
                break;
            case 2:
                result = getRandomCapitalChar();
                break;
        }
        return result;
    }

    public static char getRandomLowerNumberSpecialChar() {
        Random random=new Random();
        int number=random.nextInt(3);
        char result = 0;
        switch(number){
            case 0:
                result = getRandomLowerChar();
                break;
            case 1:
                result = getRandomNumberChar();
                break;
            case 2:
                result = getRandomSpecialChar();
                break;
        }
        return result;
    }

    public static char getRandomAll() {
        Random random=new Random();
        int number=random.nextInt(4);
        char result = 0;
        switch(number){
            case 0:
                result = getRandomLowerChar();
                break;
            case 1:
                result = getRandomNumberChar();
                break;
            case 2:
                result = getRandomCapitalChar();
                break;
            case 3:
                result = getRandomSpecialChar();
                break;
        }
        return result;
    }

    public static String getRandomString(int form_count,int length) {
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
            switch(form_count){
                case 3:
                    sb.append(getRandomLowerChar());
                    break;
                case 5:
                    sb.append(getRandomNumberChar());
                    break;
                case 7:
                    sb.append(getRandomCapitalChar());
                    break;
                case 11:
                    sb.append(getRandomSpecialChar());
                    break;
                case 8:
                    sb.append(getRandomLowerNumberChar());
                    break;
                case 10:
                    sb.append(getRandomLowerCapitalChar());
                    break;
                case 14:
                    sb.append(getRandomLowerSpecialChar());
                    break;
                case 12:
                    sb.append(getRandomNumberCapitalChar());
                    break;
                case 16:
                    sb.append(getRandomNumberSpecialChar());
                    break;
                case 18:
                    sb.append(getRandomCapitalSpecialChar());
                    break;
                case 15:
                    sb.append(getRandomLowerNumberCapitalChar());
                    break;
                case 19:
                    sb.append(getRandomLowerNumberSpecialChar());
                    break;
                case 21:
                    sb.append(getRandomLowerCapitalSpecialChar());
                    break;
                case 23:
                    sb.append(getRandomNumberCapitalSpecialChar());
                    break;
                case 26:
                    sb.append(getRandomAll());
                    break;
            }
        }
        return sb.toString();
    }
}
