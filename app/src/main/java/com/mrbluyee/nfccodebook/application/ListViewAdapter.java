package com.mrbluyee.nfccodebook.application;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mrbluyee.nfccodebook.R;

import java.util.List;

public class ListViewAdapter extends BaseAdapter {
    private Context context;//上下文对象
    private List<String> recordList;

    public ListViewAdapter(Context context, List<String> recordList){
        this.context = context;
        this.recordList= recordList;
    }

    @Override
    public int getCount() {
        return recordList == null ? 0 : recordList.size();
    }

    @Override
    public Object getItem(int position) {
        return recordList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_view, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.contentTv.setText(recordList.get(position));
        return convertView;
    }

    private final class ViewHolder{
        TextView contentTv;
        ViewHolder(View view){
            contentTv = (TextView) view.findViewById(R.id.textView_List_Item);
        }
    }
}
