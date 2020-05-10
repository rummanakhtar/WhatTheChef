package com.example.whatthechef;

public class ListItem {
    private String itemName;
    private String itemDescription;
    private String itemImage;
    private String cardFlag;

    public ListItem(String itemName, String itemDescription, String itemImage, String cardFlag) {
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemImage = itemImage;
        this.cardFlag = cardFlag;
    }

    public ListItem(){

    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public String getItemImage() {
        return itemImage;
    }

    public String getCardFlag() {
        return cardFlag;
    }

    public void setCardFlag(String cardFlag) {
        this.cardFlag = cardFlag;
    }
}
