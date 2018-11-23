package com.gome.beautymirror.gallery;

class Picture {
    private String title;
    private String imageId;
    private boolean isVedio;

    public Picture() {
        super();
    }

    public Picture(String title, String imageId ,boolean isVedio) {
        super();
        this.title = title;
        this.imageId = imageId;
        this.isVedio = isVedio;
    }

    public boolean isVedio() {
        return isVedio;
    }

    public boolean setVedio(boolean isVedio) {
        return this.isVedio = isVedio;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }
}
