package com.example.xsl.selectlibrary.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xsl.selectlibrary.R;
import com.example.xsl.selectlibrary.SquareFrameLayout;
import com.example.xsl.selectlibrary.bean.PictureInfo;
import com.example.xsl.selectlibrary.utils.CeleryImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author xsl
 * @version 1.0
 * @date 2017/4/17
 * @description
 */
public class CelerySelectPictureAdapter extends BaseAdapter {

    private Activity context;
    private List<PictureInfo> list;
    private int limit;
    private TextView cofirm;
    private HashMap<String,Boolean> hashMap = new HashMap<>();


    public CelerySelectPictureAdapter(Activity context, List<PictureInfo> list, int limit, TextView cofirm, HashMap<String,Boolean> hashMap){
        this.context = context;
        this.list = list;
        this.limit = limit;
        this.cofirm = cofirm;
        this.hashMap = hashMap;
        if (hashMap.size()>0) {
            cofirm.setText("确定(" + hashMap.size() + "/" + limit + ")");
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

    public List<String> getdata(){
        List<String> list = new ArrayList<>();
        Iterator it = hashMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (java.util.Map.Entry)it.next();
            list.add(entry.getKey()+"");
        }
        return list;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            holder  = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_celery_select_picture_item,parent,false);
            holder.frameLayout = (SquareFrameLayout) convertView.findViewById(R.id.framelayout);
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            holder.cb_image = (ImageView) convertView.findViewById(R.id.cb_image);
            holder.mengcheng = (View) convertView.findViewById(R.id.mengcheng);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }


            holder.cb_image.setVisibility(View.VISIBLE);
            //有小图就取小图显示
            final String imagePath = TextUtils.isEmpty(list.get(position).getThumb_path())?list.get(position).getPath():list.get(position).getThumb_path();

            //加载本地图片
            CeleryImageLoader.displayImageThumbnail(context,imagePath,holder.imageView,null);


            /**
             * 图片选中逻辑
             */
            final ViewHolder finalHolder = holder;
            holder.cb_image.setTag(imagePath);
            holder.cb_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("图片路径",imagePath+"");
                    //添加选中图片
                    if (!hashMap.containsKey(imagePath) && hashMap.size()<limit) {
                        //蒙层可见，图片checkbox设置成选中方式
                        finalHolder.mengcheng.setVisibility(View.VISIBLE);
                        finalHolder.cb_image.setImageResource(R.mipmap.celery_checkbox_nel);
                        hashMap.put(imagePath, true);
                        cofirm.setText("确定(" + hashMap.size() + "/" + limit + ")");
                        return;
                    }

                    //限制所选图片张数
                    if (!hashMap.containsKey(imagePath) && hashMap.size()>=limit){
                        Toast.makeText(context, "最多只可以选 " + limit + " 张图片", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    //移除选中图片
                    if (hashMap.containsKey(imagePath)){
                        //蒙层不可见，图片checkbox设置成未选中方式
                        finalHolder.mengcheng.setVisibility(View.INVISIBLE);
                        finalHolder.cb_image.setImageResource(R.mipmap.celery_checkbox_nor);
                        hashMap.remove(imagePath);
                        if (hashMap.size()==0){
                            cofirm.setText("确定");
                        }else {
                            cofirm.setText("确定(" + hashMap.size() + "/" + limit + ")");
                        }
                        return;
                    }
                }
            });

            /**
             * 判断图片是否被选中
             */
            if (hashMap.containsKey(imagePath)){
                holder.cb_image.setImageResource(R.mipmap.celery_checkbox_nel);
                holder.mengcheng.setVisibility(View.VISIBLE);
            }else {
                holder.cb_image.setImageResource(R.mipmap.celery_checkbox_nor);
                holder.mengcheng.setVisibility(View.INVISIBLE);
            }


        return convertView;
    }



    private static class ViewHolder{
        SquareFrameLayout frameLayout;
        ImageView imageView;
        View mengcheng;
        ImageView cb_image;
    }


}