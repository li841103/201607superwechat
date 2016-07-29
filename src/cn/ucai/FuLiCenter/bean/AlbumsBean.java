package cn.ucai.FuLiCenter.bean;

import java.io.Serializable;

/**
 * Created by Zhou on 2016/7/29.
 */
public class AlbumsBean implements Serializable {
    @Override
    public String toString() {
        return "AlbumsBean{" +
                "pid=" + pid +
                ", imgId=" + imgId +
                ", imgUrl='" + imgUrl + '\'' +
                ", thumbUrl='" + thumbUrl + '\'' +
                '}';
    }

    /**
     * pid : 7677
     * imgId : 28296
     * imgUrl : 201509/goods_img/7677_P_1442391216432.png
     * thumbUrl : no_picture.gif
     */

    private int pid;
    private int imgId;
    private String imgUrl;
    private String thumbUrl;

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }
}
