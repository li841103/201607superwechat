package cn.ucai.FuLiCenter.bean;

import java.io.Serializable;

/**
 * Created by Zhou on 2016/8/11.
 */
public class SettlementBean implements Serializable {
    private String souHuoRen;
    private String phoneNumber;
    private String address;
    private String jieDaoAddress;

    @Override
    public String toString() {
        return "SettlementBean{" +
                "souHuoRen='" + souHuoRen + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", jieDaoAddress='" + jieDaoAddress + '\'' +
                '}';
    }

    public String getSouHuoRen() {
        return souHuoRen;
    }

    public void setSouHuoRen(String souHuoRen) {
        this.souHuoRen = souHuoRen;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getJieDaoAddress() {
        return jieDaoAddress;
    }

    public void setJieDaoAddress(String jieDaoAddress) {
        this.jieDaoAddress = jieDaoAddress;
    }

    public SettlementBean(String souHuoRen, String phoneNumber, String address, String jieDaoAddress) {

        this.souHuoRen = souHuoRen;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.jieDaoAddress = jieDaoAddress;
    }
}
