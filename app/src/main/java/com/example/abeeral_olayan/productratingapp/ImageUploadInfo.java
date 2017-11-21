package com.example.abeeral_olayan.productratingapp;


    /**
     * Created by Leenah on 06/11/17.
     */
    public class ImageUploadInfo {

        private String imageName;

        private String imageURL;
        private String Pdesc;
        private String Pprice;
        private String Pcat;


        public ImageUploadInfo() {
        }

        public ImageUploadInfo(String name, String url,String desc,String price,String cat)
        {
            this.imageName = name;
            this.imageURL= url;
            this.Pdesc= desc;
            this.Pprice= price;
            this.Pcat= cat;
        }

        public String getImageName() {return imageName;}
        public String getImageURL() {
            return imageURL;
        }
        public String getPdesc() {
            return Pdesc;
        }
        public String getPcat() {
            return Pcat;
        }
        public String getPprice() {return Pprice;}

    }

