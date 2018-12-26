
package com.araba.cuma.araba.Class;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("data")
    @Expose
    private List<Ilanlar> data = null;

    public List<Ilanlar> getData() {
        return data;
    }

    public void setData(List<Ilanlar> data) {
        this.data = data;
    }

}
