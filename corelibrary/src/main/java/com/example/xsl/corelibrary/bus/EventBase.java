package com.example.xsl.corelibrary.bus;

/**
 * @author xsl
 * @version 1.0
 * @date 2017/4/11
 * @description
 *  RxBus 数据定义
 */
public class EventBase {

    private String tag;
    private String flag;
    private Object data;

    public EventBase(String tag, Object data) {
        this.tag = tag;
        this.data = data;
    }

    public EventBase(String tag, String flag, Object data) {
        this.tag = tag;
        this.flag = flag;
        this.data = data;
    }

    public EventBase() {
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "EventBase{" +
                "tag='" + tag + '\'' +
                ", flag='" + flag + '\'' +
                ", data=" + data +
                '}';
    }

}
