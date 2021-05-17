package com.example.xsl.corelibrary.utils;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Celery on 2016/12/06.
 */
public class HtmlFormat {


    /**
     * 获取适配img标签的富文本
     * @param htmltext 原始富文本
     * @return 返回html
     */
    public static String getContent(String htmltext){
        return "<style> img{max-width:100%}</style>\n" + htmltext;
    }


    /**
     * 抓取htm上的所有文字内容
     * @param htmltext 原始富文本
     * @return 返回文本字符串
     */
    public static String getContentTxt(String htmltext){
        String htmlStr = htmltext; //含html标签的字符串
        String textStr ="";
        Pattern p_script;
        Matcher m_script;
        Pattern p_style;
        Matcher m_style;
        Pattern p_html;
        Matcher m_html;
        try{
            //定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script> }
            String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
            //定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style> }
            String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";
            String regEx_html = "<[^>]+>"; //定义HTML标签的正则表达式
            p_script = Pattern.compile(regEx_script,Pattern.CASE_INSENSITIVE);
            m_script = p_script.matcher(htmlStr);
            htmlStr = m_script.replaceAll(""); //过滤script标签
            p_style = Pattern.compile(regEx_style,Pattern.CASE_INSENSITIVE);
            m_style = p_style.matcher(htmlStr);
            htmlStr = m_style.replaceAll(""); //过滤style标签
            p_html = Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE);
            m_html = p_html.matcher(htmlStr);
            htmlStr = m_html.replaceAll(""); //过滤html标签
            textStr = htmlStr;
        }catch(Exception e){
            e.printStackTrace();
        }
        return textStr;
    }

}
