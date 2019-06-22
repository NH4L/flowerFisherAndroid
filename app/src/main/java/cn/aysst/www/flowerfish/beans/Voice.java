package cn.aysst.www.flowerfish.beans;


import java.util.ArrayList;

/**
 * 语音对象封装
 */
public class Voice {

    public ArrayList<WSBean> ws;

    public class WSBean {
        public ArrayList<CWBean> cw;
    }

    public class CWBean {
        public String w;
    }


}
