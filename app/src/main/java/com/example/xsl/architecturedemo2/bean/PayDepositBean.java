package com.example.xsl.architecturedemo2.bean;

import java.util.List;

public class PayDepositBean {


    /**
     * msg : success
     * code : 200
     * dataList : [{"name":"偶徐","identity":"151213161514548798","identityType":2,"mobile":"15121514563","customerCode":2018112016475743,"consultantId":"cb7093f2213c4171b244d435a7685a10","consultantName":"高然","membershipFeeId":"e18c3f5eb09f44c7a85cfcf4f95d7e8b","status":2,"id":"5d87799ef6284384b5896198d37faf22","deleted":0}]
     */

    private String msg;
    private int code;
    private List<DataListBean> dataList;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<DataListBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<DataListBean> dataList) {
        this.dataList = dataList;
    }

    public static class DataListBean {

        /**
         * name : vxc
         * identity : 420625192210121569
         * identityType : 1
         * mobile : 12103265895
         * customerCode : 2019012511092829
         * consultantId : a3cca224f22a46d0aa3746d066cfbff0
         * consultantName : 王云云
         * membershipFeeId : 882c1d7da2f9474596cb7ee52def57ad
         * id : 4253d06db6e8468fa43591ddfedd903d
         * deleted : 0
         */

        private String name;
        private String identity;
        private int identityType;
        private String mobile;
        private long customerCode;
        private String consultantId;
        private String consultantName;
        private String membershipFeeId;
        private String id;
        private int status;
        private int deleted;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIdentity() {
            return identity;
        }

        public void setIdentity(String identity) {
            this.identity = identity;
        }

        public int getIdentityType() {
            return identityType;
        }

        public void setIdentityType(int identityType) {
            this.identityType = identityType;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public long getCustomerCode() {
            return customerCode;
        }

        public void setCustomerCode(long customerCode) {
            this.customerCode = customerCode;
        }

        public String getConsultantId() {
            return consultantId;
        }

        public void setConsultantId(String consultantId) {
            this.consultantId = consultantId;
        }

        public String getConsultantName() {
            return consultantName;
        }

        public void setConsultantName(String consultantName) {
            this.consultantName = consultantName;
        }

        public String getMembershipFeeId() {
            return membershipFeeId;
        }

        public void setMembershipFeeId(String membershipFeeId) {
            this.membershipFeeId = membershipFeeId;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getDeleted() {
            return deleted;
        }

        public void setDeleted(int deleted) {
            this.deleted = deleted;
        }
    }
}
