package com.example.fourscreen.fragments.list.data;

import com.example.fourscreen.R;

public class Content {

    private String itemName;
    private int firstPic, secondPic;
    private boolean checkboxState;

    public Content(String itemName) {
        this.itemName = itemName;
        this.firstPic = R.drawable.boy;
        this.secondPic = R.drawable.horse;
        this.checkboxState = false;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getFirstPic() {
        return firstPic;
    }

    public void setFirstPic(int firstPic) {
        this.firstPic = firstPic;
    }

    public int getSecondPic() {
        return secondPic;
    }

    public void setSecondPic(int secondPic) {
        this.secondPic = secondPic;
    }

    public boolean isCheckboxState() {
        return checkboxState;
    }

    public void setCheckboxState(boolean checkboxState) {
        this.checkboxState = checkboxState;
    }
}
