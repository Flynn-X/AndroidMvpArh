package com.example.xsl.selectlibrary.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author xsl
 * @version 1.0
 * @date 2017/4/17
 * @description
 */
public class PictureInfo implements Parcelable {

    //图片id
    private String _id;
    //缩略图路径
    private String thumb_path;
    //原图路径
    private String path;
    //图片宽
    private int width;
    //图片高
    private int height;
    //所属文件夹名称
    private String folderName;

    public PictureInfo() {

    }


    protected PictureInfo(Parcel in) {
        _id = in.readString();
        thumb_path = in.readString();
        path = in.readString();
        width = in.readInt();
        height = in.readInt();
        folderName = in.readString();
    }

    public static final Creator<PictureInfo> CREATOR = new Creator<PictureInfo>() {
        @Override
        public PictureInfo createFromParcel(Parcel in) {
            return new PictureInfo(in);
        }

        @Override
        public PictureInfo[] newArray(int size) {
            return new PictureInfo[size];
        }
    };

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getThumb_path() {
        return thumb_path;
    }

    public void setThumb_path(String thumb_path) {
        this.thumb_path = thumb_path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(thumb_path);
        dest.writeString(path);
        dest.writeInt(width);
        dest.writeInt(height);
        dest.writeString(folderName);
    }
}