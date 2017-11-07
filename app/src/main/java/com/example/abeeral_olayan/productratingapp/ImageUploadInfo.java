package com.example.abeeral_olayan.productratingapp;


    /**
     * Created by Leenah on 06/11/17.
     */
    public class ImageUploadInfo {

        public String imageName;

        public String imageURL;
        public String Pdesc;
        public String Pprice;
        public String Pcat;


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

        public String getImageName() {
            return imageName;
        }
        public String getImageURL() {
            return imageURL;
        }

    }

