package com.soft.homegardening;

public class ModelGif {
    private int gifId;
    private String gifText;


    public ModelGif(int gifId, String gifText) {
        this.gifId = gifId;
        this.gifText = gifText;
    }

    public int getGifId() {
        return gifId;
    }

    public void setGifId(int gifId) {
        this.gifId = gifId;
    }

    public String getGifText() {
        return gifText;
    }

    public void setGifText(String gifText) {
        this.gifText = gifText;
    }

}
