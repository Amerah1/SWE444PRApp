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
        private String numOfRating;
        private String Rating;


        public ImageUploadInfo() {}

        public ImageUploadInfo(String imageName, String imageURL, String pdesc, String pprice, String pcat, String numOfRating, String rating) {
            this.imageName = imageName;
            this.imageURL = imageURL;
            Pdesc = pdesc;
            Pprice = pprice;
            Pcat = pcat;
            this.numOfRating = numOfRating;
            Rating = rating;
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
        public String getNumOfRating() {return numOfRating;}
        public String getRating() {return Rating;}

        public void setPcat(String pcat) {Pcat = pcat;}
        public void setPdesc(String pdesc) {Pdesc = pdesc;}
        public void setPprice(String pprice) {Pprice = pprice;}
        public void setImageName(String imageName) {this.imageName = imageName;}
        public void setImageURL(String imageURL) {this.imageURL = imageURL;}
        public void setNumOfRating(String numOfRating) {this.numOfRating = numOfRating;}
        public void setRating(String rating) {Rating = rating;}


    }

