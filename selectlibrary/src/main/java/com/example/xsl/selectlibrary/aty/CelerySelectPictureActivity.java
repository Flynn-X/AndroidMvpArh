package com.example.xsl.selectlibrary.aty;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.xsl.selectlibrary.R;
import com.example.xsl.selectlibrary.adapter.CelerySelectPictureAdapter;
import com.example.xsl.selectlibrary.adapter.LVAdapter;
import com.example.xsl.selectlibrary.bean.PictureInfo;
import com.example.xsl.selectlibrary.utils.OSUtils;
import com.example.xsl.selectlibrary.utils.SelectLibrary;
import com.example.xsl.selectlibrary.utils.TransitionAnimUtils;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CelerySelectPictureActivity extends AppCompatActivity {

    private LinearLayout layout,bottomLayout,clayout;
    private Toolbar toolbar;
    private GridView gridView;
    private ListView listView;
    private TextView confirm,title,folder_name_tv;
    private View bgView;
    private ImageView arrow_img;
    private static ArrayList<PictureInfo> list = new ArrayList<>();
    private CelerySelectPictureAdapter adapter;

    private String exTittle,exConfirmText;
    private int exTittleColor,exConfirmTextColor,tabBgColor,tabTextColor;
    private boolean isStatusBarDarkText = false;
    private int resId;
    //?????????????????????1???
    private int limit = 9;
    private List<String> seletedList = new ArrayList<>();
    HashMap<String,Boolean> hashMap = new HashMap<>();
    //??????
    private ArrayList<String> thumb_list = new ArrayList<String>();
    //??????
    private ArrayList<String> normal_list = new ArrayList<String>();
    //???????????????map
    private Map<String,PictureInfo> folderMap = new HashMap<>();
    //????????????map
    private Map<PictureInfo,String> allMap = new HashMap<>();
    private Uri imageUri = null;
    public static final int TAKE_PHOTO = 0x010;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //?????? Android 7.0 ??? uri ??????????????????
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_celery_select_picture);
        Bundle bundle = getIntent().getBundleExtra("bundle");
        resId = bundle.getInt("exResId",-1);
        exTittle = bundle.getString("exTittle");
        exTittleColor = bundle.getInt("exTittleColor",0xffffffff);
        exConfirmText = bundle.getString("exConfirmText");
        exConfirmTextColor = bundle.getInt("exConfirmTextColor",0xffffffff);
        limit = bundle.getInt("limit",9);
        seletedList = (List<String>) bundle.getSerializable("data");
        isStatusBarDarkText = bundle.getBoolean("isStatusBarDarkText",false);
        tabBgColor = bundle.getInt("tabBgColor",0);
        tabTextColor = bundle.getInt("tabTextColor",0);
        if (isStatusBarDarkText) {
            OSUtils.setStatusBarDarkText(this, true, false);
        }
        init();
    }



    /**
     * ?????????
     */
    private void init() {
        layout = (LinearLayout) findViewById(R.id.layout);
//        addExtraView(this,layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setTitleTextColor(0xffffffff);
        if (resId>0) {
            toolbar.setNavigationIcon(resId);
        }else {
            toolbar.setNavigationIcon(R.mipmap.celery_left);
        }
        setSupportActionBar(toolbar);
        title = (TextView) findViewById(R.id.title);
        confirm = (TextView) findViewById(R.id.confirm);
        gridView = (GridView) findViewById(R.id.gridView);
        folder_name_tv = (TextView) findViewById(R.id.folder_name_tv);
        bgView = (View) findViewById(R.id.bgView);
        listView = (ListView) findViewById(R.id.listView);
        bottomLayout = (LinearLayout) findViewById(R.id.bottomLayout);
        clayout = (LinearLayout) findViewById(R.id.clayout);
        arrow_img = (ImageView) findViewById(R.id.arrow_img);

        if (seletedList != null) {
            for (int i = 0; i < seletedList.size(); i++) {
                hashMap.put(seletedList.get(i), true);
            }
        }
        adapter = new CelerySelectPictureAdapter(this, list,limit,confirm,hashMap);
        gridView.setAdapter(adapter);

        if (!TextUtils.isEmpty(exTittle)){
            title.setText(exTittle);
        }

        if (!TextUtils.isEmpty(exTittle)){
            title.setText(exTittle);
        }

        if (exTittleColor != 0xffffffff){
            title.setTextColor(exTittleColor);
        }

        if (!TextUtils.isEmpty(exConfirmText)){
            confirm.setText(exConfirmText);
        }

        if (exConfirmTextColor != 0xffffffff){
            confirm.setTextColor(exConfirmTextColor);
        }

        if (tabBgColor>0) {
            clayout.setBackgroundResource(tabBgColor);
        }
        if (tabTextColor>0) {
            folder_name_tv.setTextColor(tabTextColor);
        }

        //??????????????????
        confirm.setOnClickListener(onClickListener);
        bottomLayout.setOnClickListener(onClickListener);
        bgView.setOnClickListener(onClickListener);


        /**
         * gridview item ??????????????????
         */
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                ViewGroup viewGroup = (ViewGroup) view;
                for (int i=0;i<viewGroup.getChildCount();i++){
                    View viewGroupChildAt = viewGroup.getChildAt(i);
                    if (viewGroupChildAt instanceof ImageView && i == 0){
                        ImageView imageView = (ImageView) viewGroupChildAt;
                        SelectLibrary.openScan(CelerySelectPictureActivity.this)
                                .withView(imageView)
                                .position(position)
                                .numColumns(4)
                                .horizontalSpacing(SelectLibrary.dp2px(CelerySelectPictureActivity.this,1))
                                .verticalSpacing(SelectLibrary.dp2px(CelerySelectPictureActivity.this,1))
                                .showSave(false)
                                .thumbImgs(thumb_list)
                                .normalImgs(normal_list)
                                .start();
                    }
                  }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = view.findViewById(R.id.folder_name);
                folder_name_tv.setText(textView.getText().toString());
                arrow_img.setImageResource(R.mipmap.ci_arrow_up);

                PictureInfo pictureInfo = (PictureInfo) parent.getItemAtPosition(position);
                getImageData(pictureInfo.getFolderName());
                TransitionAnimUtils.bottomExit(bgView,listView);
            }
        });


        //??????????????????
        ObjectAnimator
                .ofFloat(bgView,"alpha",1f,0f)
                .setDuration(1)
                .start();

        //??????????????????
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            //????????????
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
        }else{
            getImageData(null);
        }

    }



    /**
     * ??????????????????
     */
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int i = v.getId();
            if (i == R.id.confirm) {
                Intent intent = new Intent();
                intent.putExtra("data", (Serializable) adapter.getdata());
                intent.putExtra("type", 0);//0??????????????? 1 ?????????
                setResult(RESULT_OK,intent);
                finish();
            }

            if (i == R.id.bgView){
                TransitionAnimUtils.bottomExit(bgView,listView);
                arrow_img.setImageResource(R.mipmap.ci_arrow_up);
            }

            if (i == R.id.bottomLayout){
                if (listView.getVisibility() == View.INVISIBLE) {
                    TransitionAnimUtils.bottomEnter(bgView, listView);
                    arrow_img.setImageResource(R.mipmap.ci_arrow_down);
                }else {
                    TransitionAnimUtils.bottomExit(bgView,listView);
                    arrow_img.setImageResource(R.mipmap.ci_arrow_up);
                }
            }

        }
    };


    /**
     * ?????????imageUri
     * @return
     */
    private Uri initImageUri(){
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "DCIM");
        if (!file.exists()){
            file.mkdirs();
        }
        file = new File(file.getPath()+ File.separator + System.currentTimeMillis() +  ".jpg");
        imageUri = Uri.fromFile(file);
        return imageUri;
    }


    /**
     * ????????????????????????????????????
     * @param folderName ????????????
     */
    private void getImageData(final String folderName) {
        list.clear();
        if (TextUtils.isEmpty(folderName)) {
            folderMap.clear();
            allMap.clear();
        }
        //???????????????????????????????????????
        new Thread(new Runnable() {
            @Override
            public void run() {
                /**
                 * ??????????????????????????????????????????50K????????????ID?????????
                 * ????????????????????????????????????????????????DATA??????
                 * ?????????????????????????????????????????????ID?????????????????????????????????
                 * ?????????????????????????????????????????????????????????????????????????????????
                 * ????????????????????????????????????????????????????????????????????????Adapter
                 * ??????????????????ID????????????????????????????????????????????????????????????
                 */
                String[] projection = {
                        MediaStore.Images.Media._ID
                        , MediaStore.Images.Media.DATA
                        , MediaStore.Images.Media.HEIGHT
                        , MediaStore.Images.Media.WIDTH
                        ,MediaStore.Images.Media.WIDTH
                };

                Uri ext_uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                String where = MediaStore.Images.Media.SIZE + ">=?";
                Cursor cursor = getContentResolver().query(
                        ext_uri,
                        projection,
                        where,
                        new String[]{1 * 20 * 1024 + ""},
                        MediaStore.Images.Media.DATE_ADDED + " desc");
                //??????MediaStore.Images.Media._ID ??????list postion ??????
                HashMap<String,Object> map = new HashMap<>();
                while (cursor.moveToNext()) {
                    //?????????????????????
                    String id = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media._ID));
                    //?????????????????????
                    byte[] data = cursor.getBlob(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    String path = new String(data, 0, data.length - 1);
                    //???
                    int width = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.WIDTH));
                    //???
                    int height = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.HEIGHT));

                    PictureInfo pictureInfo = new PictureInfo();
                    pictureInfo.set_id(id);
                    pictureInfo.setPath(path);
                    pictureInfo.setWidth(width);
                    pictureInfo.setHeight(height);
                    if (!TextUtils.isEmpty(path)) {
                        String[] strings = path.split(File.separator);
                        String folder_name = strings[strings.length-2];
                        pictureInfo.setFolderName(folder_name);
                        //folderName ????????????????????????????????????????????????????????????
                        if (TextUtils.isEmpty(folderName)) {
                            if (!folderMap.containsKey(folder_name)) {
                                folderMap.put(folder_name, pictureInfo);
                            }
                            allMap.put(pictureInfo, folder_name);
                        }

                        if (TextUtils.isEmpty(folderName) || folder_name.equals(folderName)){
                            list.add(pictureInfo);
                            map.put(id, list.size() - 1);
                        }
                    }
                }

                getThumbnail(list,map);

            }
        }).start();

    }

    /**
     * ??????????????????????????????????????????id??????MediaStore.Images.Thumbnails.IMAGE_ID??????
     * ???????????????????????????
     * @param list ??????????????????
     * @param map ??????MediaStore.Images.Media._ID ??????list postion ??????map??????
     */
    private void getThumbnail(List<PictureInfo> list, HashMap<String,Object> map){
        String[] projection = {
                MediaStore.Images.Thumbnails.IMAGE_ID
                ,MediaStore.Images.Thumbnails.DATA
                ,MediaStore.Images.Thumbnails.HEIGHT
                ,MediaStore.Images.Thumbnails.WIDTH
        };

        Uri ext_uri = MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI;
        String where = null ;
        Cursor cursor = getContentResolver().query(
                ext_uri,//???????????????????????????Uri
                projection,//???????????????????????????
                where,//????????????
                null, //?????????????????????????????????
                MediaStore.Images.Thumbnails.IMAGE_ID + " desc");//????????????
        while (cursor.moveToNext()) {
            String image_id = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Thumbnails.IMAGE_ID));
            //?????????????????????
            byte[] data = cursor.getBlob(cursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA));
            //???????????????
            String path = new String(data, 0, data.length - 1);
            //???
            int width = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Thumbnails.WIDTH));
            //???
            int height = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Thumbnails.HEIGHT));
            //??????image_id?????????????????????
            if (map.containsKey(image_id)){
                int postion = (int) map.get(image_id);
                //??????PictureInfoBean???????????????????????????
                PictureInfo pictureInfo =  list.get(postion);
                pictureInfo.setThumb_path(path);
            }
        }

        //????????????
        normal_list.clear();
        thumb_list.clear();
        for (PictureInfo pictureInfo:list){
            normal_list.add(pictureInfo.getPath());
            thumb_list.add(pictureInfo.getThumb_path());
        }

        //???????????????UI
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
                //????????????
                LVAdapter adapter = new LVAdapter(CelerySelectPictureActivity.this,folderMap,allMap,folder_name_tv);
                listView.setAdapter(adapter);
            }
        });
    }





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * ??????????????????
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100){
            if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                if (grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    getImageData(null);
                }else {
                    Toast.makeText(CelerySelectPictureActivity.this,"??????SD??????????????????,?????????????????????",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }else if (requestCode == 101){
            if (permissions[0].equals(Manifest.permission.CAMERA)){
                if (grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    initImageUri();
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, TAKE_PHOTO);
                }else {
                    Toast.makeText(CelerySelectPictureActivity.this,"???????????????????????????,?????????????????????",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TAKE_PHOTO:
                    if (imageUri != null) {
                        // ???????????????????????????MediaScanner ???????????????????????????
                        //????????????????????????????????????????????????????????????????????????
                        Intent broadcastIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        broadcastIntent.setData(imageUri);
                        this.sendBroadcast(broadcastIntent);
                        //??????????????????
                        List<String> list = new ArrayList<>();
//                        Log.e("??????????????????",imageUri.getPath());
                        list.add(imageUri.getPath());
                        Intent intent = new Intent();
                        intent.putExtra("data", (Serializable) list);
                        intent.putExtra("type", 1);//0??????????????? 1 ?????????
                        setResult(RESULT_OK,intent);
                        finish();
                    }
                    break;
            }
        }
    }

    /**
     * ????????????????????????View
     * @param activity
     * @param layout
     */
    protected void addExtraView(Activity activity,LinearLayout layout){
        //????????????5.0?????? ?????? ???flyme ?????? miui?????????????????????
        if((Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP &&
                (OSUtils.getRomType().equals(OSUtils.ROM_TYPE.FLYME)
                        || OSUtils.getRomType().equals(OSUtils.ROM_TYPE.MIUI)))
                || Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1 ){
            /**
             * ?????????????????????????????????
             * */
            int statusBarHeight = -1;
            //??????status_bar_height?????????ID
            int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                //????????????ID????????????????????????
                statusBarHeight = activity.getResources().getDimensionPixelSize(resourceId);
            }
            View view = new View(activity);
            view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, statusBarHeight);
            params.height = statusBarHeight;
            view.setLayoutParams(params);
            layout.addView(view);
        }
    }

}
