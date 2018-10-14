package com.mrbluyee.nfccodebook.bean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CodeBook{
    public HashMap<String,CodeRecord> book = null;

    public CodeBook(String data){
        this.book = new HashMap<String,CodeRecord>();
        try {
            if(data != null){
                JSONArray dataTemp = new JSONArray(data);
                if(dataTemp != null){
                    for(int i=0;i<dataTemp.length();i++){
                        JSONObject jsonObject = dataTemp.getJSONObject(i);
                        String recordName = jsonObject.getString("recordName");
                        CodeRecord newRecord = new CodeRecord();
                        newRecord.account = jsonObject.getString("account");
                        newRecord.password = jsonObject.getString("password");
                        newRecord.remark = jsonObject.getString("remark");
                        book.put(recordName,newRecord);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CodeBook(HashMap<String,CodeRecord> book){
        this.book = book;
    }

    public void addRecord(String recordName,CodeRecord newRecord){
       if(book != null && recordName != null &&newRecord != null){
           book.put(recordName,newRecord);
       }
    }

    public void modifyRecord(String recordName,CodeRecord newRecord){
        addRecord(recordName,newRecord);
    }

    public CodeRecord getRecord(String recordName){
        return book.get(recordName);
    }

    public void delectRecord(String recordName){
        book.remove(recordName);
    }

    public String convertRecordToString(){
        JSONArray dataTemp = new JSONArray();
        Iterator<Map.Entry<String,CodeRecord>> iterator = book.entrySet().iterator();
        try {
            while (iterator.hasNext()) {
                Map.Entry<String, CodeRecord> entry = iterator.next();
                String key = entry.getKey();
                CodeRecord value = entry.getValue();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("recordName", key);
                jsonObject.put("account", value.account);
                jsonObject.put("password", value.password);
                jsonObject.put("remark", value.remark);
                dataTemp.put(jsonObject);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return dataTemp.toString();
    }
}
