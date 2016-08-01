package cn.ucai.FuLiCenter.bean;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;

/**
 * Created by Zhou on 2016/7/29.
 */
public class CartBean implements Serializable {
    @Override
    public String toString() {
        return "CartBean{" +
                "id=" + id +
                ", userName=" + userName +
                ", goodsId=" + goodsId +
                ", count=" + count +
                ", checked=" + isChecked +
                ", goods='" + goods + '\'' +
                '}';
    }

    /**
     * id : 7672
     * userName : 7672
     * goodsId : 7672
     *  count : 2
     *  checked : true
     * goods : GoodDetailsBean
     */

    private int id;
    private String userName;
    private int goodsId;
    private int count;
    @JsonProperty("isChecked")
    private boolean isChecked;
    private GoodDetails goods;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
    @JsonIgnore
    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public GoodDetails getGoods() {
        return goods;
    }

    public void setGoods(GoodDetails goods) {
        this.goods = goods;
    }
}
