package com.example.abeeral_olayan.productratingapp;
/**
 * Created by Leenah on 17/11/17.
 */

public class SuggestProducctInfo {


    public String SPName;
    public String SPimageURL;
    public String SPdesc;
    public String SPprice;
    public String SPcat;


    public SuggestProducctInfo() {
    }

    public SuggestProducctInfo(String name, String url,String desc,String price,String cat)
    {
        this.SPName = name;
        this.SPimageURL= url;
        this.SPdesc= desc;
        this.SPprice= price;
        this.SPcat= cat;
    }

   /* public String getSPName() {
        return SPName;
    }
    public String getSPimageURL() {
        return SPimageURL;
    }*/


}
