package com.mrbluyee.nfccodebook.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mrbluyee.nfccodebook.R;
import com.mrbluyee.nfccodebook.bean.TreeItem;
import com.mrbluyee.nfccodebook.utils.ScreenUtils;

import java.util.List;

public class TagInfoAdapter extends TreeAdapter<TreeItem> {
    Context context;

    public TagInfoAdapter(List<TreeItem> nodes, Context context) {
        super(nodes);
        this.context = context;
    }

    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount() + 1;
    }

    /**
     * 获取当前位置的条目类型
     */
    @Override
    public int getItemViewType(int position) {
        if (getItem(position).hasChild()) {
            return 1;
        }
        return 0;
    }

    @Override
    protected Holder<TreeItem> getHolder(int position) {
        switch(getItemViewType(position)) {
            case 1:
                return new Holder<TreeItem>() {
                    private ImageView iv;
                    private TextView tv;

                    @Override
                    protected void setData(TreeItem node) {
                        iv.setVisibility(node.hasChild() ? View.VISIBLE : View.INVISIBLE);
                        iv.setBackgroundResource(node.isExpand ? R.mipmap.expand : R.mipmap.fold);
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) iv.getLayoutParams();
                        params.leftMargin = (node.level + 1) * new ScreenUtils(context).dip2px(20);
                        iv.setLayoutParams(params);
                        tv.setText(node.name);
                    }
                    @Override
                    protected View createConvertView() {
                        View view = View.inflate(context, R.layout.item_tree_list_has_child, null);
                        iv = (ImageView) view.findViewById(R.id.item_tree_list_has_child_ivIcon);
                        tv = (TextView) view.findViewById(R.id.item_tree_list_has_child_tvName);
                        return view;
                    }
                };
            default:
                return new Holder<TreeItem>() {
                    private TextView tv;

                    @Override
                    protected void setData(TreeItem node) {
                        tv.setText(node.name);
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tv.getLayoutParams();
                        params.leftMargin = (node.level + 3) * new ScreenUtils(context).dip2px(20);
                        tv.setLayoutParams(params);
                    }

                    @Override
                    protected View createConvertView() {
                        View view = View.inflate(context, R.layout.item_tree_list_no_child, null);
                        tv = (TextView) view.findViewById(R.id.item_tree_list_has_no_child_tvName);
                        return view;
                    }
                };
        }
    }
}