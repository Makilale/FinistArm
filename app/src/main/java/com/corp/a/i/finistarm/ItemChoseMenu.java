package com.corp.a.i.finistarm;

public class ItemChoseMenu {
    String namePosition;
    String pricePosition;
    String idProduct;
    boolean isCheck;
    boolean isNew;
    boolean isReport;

    public ItemChoseMenu(String namePosition, String pricePostion, String idProduct, boolean isCheck, boolean isNew, boolean isReport) {
        this.namePosition = namePosition;
        this.pricePosition = getPricePosition();
        this.idProduct = idProduct;
        this.isCheck = isCheck;
        this.isNew = isNew;
        this.isReport = isReport;
    }

    public boolean isReport() {
        return isReport;
    }

    public void setReport(boolean report) {
        isReport = report;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public boolean isNew() {
        return isNew;
    }

    public String getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }

    public String getNamePosition() {
        return namePosition;
    }

    public void setNamePosition(String namePosition) {
        this.namePosition = namePosition;
    }

    public String getPricePosition() {     return pricePostion;    }

    public void setPricePostion(String pricePostion) {
        this.pricePostion = pricePostion;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
