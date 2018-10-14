package com.mrbluyee.nfccodebook.bean;

public class TreeItem extends Node<TreeItem> {
    public String name;

    public TreeItem(int id, int pId, int level, boolean isExpand, String name) {
        super(id, pId, level, isExpand);
        this.name = name;
    }
}
