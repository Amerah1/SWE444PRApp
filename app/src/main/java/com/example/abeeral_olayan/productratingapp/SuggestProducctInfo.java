package com.example.abeeral_olayan.productratingapp;
/**
 * Created by Leenah on 17/11/17.
 */

public class SuggestProducctInfo {


    private String SPName;
    private String SPimageURL;
    private String SPdesc;
    private String SPprice;
    private String SPcat;


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

   public String getSPName() {
        return SPName;
    }
    public String getSPimageURL() {
        return SPimageURL;
    }
    public String getSPdesc() {
        return SPdesc;
    }
    public String getSPprice() {
        return SPprice;
    }
    public String getSPcat() {
        return SPcat;
    }


}
