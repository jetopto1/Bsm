package com.jetopto.bsm.utils.classes;

public class Category {

    private String label;
    private int imageId;

    public Category(String label, int id) {
        this.label = label;
        this.imageId = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Label : " + label);
        sb.append("Resource : " + imageId);
        return sb.toString();
    }
}
