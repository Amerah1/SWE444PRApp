package com.example.abeeral_olayan.productratingapp;

/**
 * Created by Leenah on 08/11/17.
 */


public class ImageUpload_Category {

    public String imageName;

    public String imageURL;

    public ImageUpload_Category() {

    }

    public ImageUpload_Category(String name, String url) {

        this.imageName = name;
        this.imageURL= url;
    }

    public String getImageName() {
        return imageName;
    }

    public String getImageURL() {
        return imageURL;
    }

}

