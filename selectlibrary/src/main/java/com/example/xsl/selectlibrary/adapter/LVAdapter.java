package com.example.xsl.selectlibrary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xsl.selectlibrary.R;
import com.example.xsl.selectlibrary.bean.PictureInfo;
import com.example.xsl.selectlibrary.utils.CeleryImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by xsl on 2018/2/6.
 * 图片文件夹列表适配器
 */
public class LVAdapter extends BaseAdapter {

    private Context context;
    private Map<String,PictureInfo> folderMap;
    private List<PictureInfo> list = new ArrayList<>();
    private Map<PictureInfo,String> allMap;
    private TextView textView;
    public LVAdapter(Context context, Map<String,PictureInfo> folderMap,Map<PictureInfo,String> allMap,TextView textView) {
        list.clear();
        this.context = context;
        this.folderMap = folderMap;
        this.allMap = allMap;
        this.textView = textView;
        if (allMap != null && allMap.size()>0) {
            list.add(new PictureInfo());
        }
        for (Map.Entry<String,PictureInfo> entry : folderMap.entrySet()) {
            list.add(entry.getValue());
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.celery_picture_folder_list_item,null);
            holder.layout = (LinearLayout) convertView.findViewById(R.id.layout);
            holder.image = (ImageView) convertView.findViewById(R.id.image);
            holder.folder_name = (TextView) convertView.findViewById(R.id.folder_name);
            holder.count = (TextView) convertView.findViewById(R.id.count);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }


        if (position == 0){

            CeleryImageLoader.displayImageThumbnail(context,list.get(position).getPath(),holder.image,null);

            holder.folder_name.setText(context.getResources().getString(R.string.folder_name));

            //遍历所有数据计算图片数量
            int k = 0;
            for (Map.Entry<PictureInfo, String> entry : allMap.entrySet()) {
                k++;
            }
            holder.count.setText("（" + k + "）");

        }else {

            CeleryImageLoader.displayImageThumbnail(context,list.get(position).getPath(),holder.image,null);

            holder.folder_name.setText(list.get(position).getFolderName());

            //遍历所有数据计算图片数量
            int k = 0;
            for (Map.Entry<PictureInfo, String> entry : allMap.entrySet()) {
                if (entry.getValue().equals(list.get(position).getFolderName())) {
                    k++;
                }
            }
            holder.count.setText("（" + k + "）");
        }

        if (holder.folder_name.getText().equals(textView.getText().toString())){
            holder.layout.setBackgroundColor(0xfff0f0f0);
        }else {
            holder.layout.setBackgroundColor(0xffffffff);
        }

        return convertView;
    }

    static class ViewHolder{
        LinearLayout layout;
        ImageView image;
        TextView folder_name;
        TextView count;
    }
}
