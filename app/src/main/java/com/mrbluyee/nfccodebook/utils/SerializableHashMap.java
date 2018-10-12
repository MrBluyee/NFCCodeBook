package com.mrbluyee.nfccodebook.utils;

import java.io.Serializable;
import java.util.HashMap;

public class SerializableHashMap implements Serializable{
    private HashMap<String,CodeRecord> map;

    public HashMap<String,CodeRecord> getMap(){
        return map;
    }

    public void setMap(HashMap<String,CodeRecord> map){
        this.map = map;
    }
}
