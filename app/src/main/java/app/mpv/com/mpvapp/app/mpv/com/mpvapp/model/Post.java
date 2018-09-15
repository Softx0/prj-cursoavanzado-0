package app.mpv.com.mpvapp.app.mpv.com.mpvapp.model;

public class Post {

    private String imageUrl;
    private String priceImage;
    private String titleImage;
    private String addressImage;

    public Post() {
    }

    public Post(String imageUrl, String priceImage, String titleImage, String addressImage) {
        this.imageUrl = imageUrl;
        this.priceImage = priceImage;
        this.titleImage = titleImage;
        this.addressImage = addressImage;
    }

    public String getAddressImage() {
        return addressImage;
    }

    public void setAddressImage(String addressImage) {
        this.addressImage = addressImage;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPriceImage() {
        return priceImage;
    }

    public void setPriceImage(String priceImage) {
        this.priceImage = priceImage;
    }

    public String getTitleImage() {
        return titleImage;
    }

    public void setTitleImage(String titleImage) {
        this.titleImage = titleImage;
    }

}
