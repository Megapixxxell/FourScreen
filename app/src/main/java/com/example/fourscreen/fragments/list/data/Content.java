package com.example.fourscreen.fragments.list.data;

import com.example.fourscreen.R;

//Модель элемента списка в виде класса
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

    public int getSecondPic() {
        return secondPic;
    }

    public boolean isCheckboxState() {
        return checkboxState;
    }

    public void setCheckboxState(boolean checkboxState) {
        this.checkboxState = checkboxState;
    }
}