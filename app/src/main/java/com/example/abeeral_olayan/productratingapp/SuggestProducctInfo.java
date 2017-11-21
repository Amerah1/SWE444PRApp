package com.example.abeeral_olayan.productratingapp;
/**
 * Created by Leenah on 17/11/17.
 */

public class SuggestProducctInfo {


    private String spname;
    private String spimageURL;
    private String spdesc;
    private String spprice;
    private String spcat;


    public SuggestProducctInfo() {
    }

    public SuggestProducctInfo(String name, String url,String desc,String price,String cat)
    {
        this.spname = name;
        this.spimageURL= url;
        this.spdesc= desc;
        this.spprice= price;
        this.spcat= cat;
    }

   public String getSPName() {
        return spname;
    }
    public String getSPimageURL() {
        return spimageURL;
    }
    public String getSPdesc() {
        return spdesc;
    }
    public String getSPprice() {
        return spprice;
    }
    public String getSPcat() {
        return spcat;
    }


}
