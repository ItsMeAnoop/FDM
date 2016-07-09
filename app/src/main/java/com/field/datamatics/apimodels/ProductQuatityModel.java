package com.field.datamatics.apimodels;

/**
 * Created by anoop on 5/11/15.
 */
public class ProductQuatityModel {
    private String routplanno;
    private String visiteddate;
    private String date;
    private String productno;
    private String quantity;
    private String narration;
    private String starttime;
    private String endtime;
    private String startcordinates;
    private String endcordinates;
    public ProductQuatityModel(String routplanno,
            String visiteddate,
            String date,
            String productno,
            String quantity,
            String narration,
            String starttime,
            String endtime,
            String startcordinates,
            String endcordinates){
                this.routplanno=routplanno;
                this.visiteddate=visiteddate;
                this.date=date;
                this.productno=productno;
                this.quantity=quantity;
                this. narration=narration;
                this. starttime=starttime;
                this. endtime=endtime;
                this. startcordinates=startcordinates;
                this. endcordinates=endcordinates;

    }

    public String getRoutplanno() {
        return routplanno;
    }

    public void setRoutplanno(String routplanno) {
        this.routplanno = routplanno;
    }

    public String getVisiteddate() {
        return visiteddate;
    }

    public void setVisiteddate(String visiteddate) {
        this.visiteddate = visiteddate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getProductno() {
        return productno;
    }

    public void setProductno(String productno) {
        this.productno = productno;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getStartcordinates() {
        return startcordinates;
    }

    public void setStartcordinates(String startcordinates) {
        this.startcordinates = startcordinates;
    }

    public String getEndcordinates() {
        return endcordinates;
    }

    public void setEndcordinates(String endcordinates) {
        this.endcordinates = endcordinates;
    }
}
