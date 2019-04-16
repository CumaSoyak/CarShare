
package com.araba.cuma.araba.Class;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("data")
    @Expose
    private List<Advert> data = null;

    public List<Advert> getData() {
        return data;
    }

    public void setData(List<Advert> data) {
        this.data = data;
    }

}
