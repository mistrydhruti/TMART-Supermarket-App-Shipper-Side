package com.example.qrcodescanner.Model;

public class TakeOrderModel {
    private String DeliveryAddress,DeliveryTo,OrderId,ShipperImage,ShipperName,ShipperPhoneNo,ExpectedTime,payment,Totalamount,CustomerEmail;

    public TakeOrderModel()
    {

    }

    public TakeOrderModel(String deliveryAddress, String deliveryTo, String orderId, String shipperImage, String shipperName, String shipperPhoneNo, String expectedTime, String payment, String totalamount, String customerEmail) {
        DeliveryAddress = deliveryAddress;
        DeliveryTo = deliveryTo;
        OrderId = orderId;
        ShipperImage = shipperImage;
        ShipperName = shipperName;
        ShipperPhoneNo = shipperPhoneNo;
        ExpectedTime = expectedTime;
        this.payment = payment;
        Totalamount = totalamount;
        CustomerEmail = customerEmail;
    }

    public String getDeliveryAddress() {
        return DeliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        DeliveryAddress = deliveryAddress;
    }

    public String getDeliveryTo() {
        return DeliveryTo;
    }

    public void setDeliveryTo(String deliveryTo) {
        DeliveryTo = deliveryTo;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public String getShipperImage() {
        return ShipperImage;
    }

    public void setShipperImage(String shipperImage) {
        ShipperImage = shipperImage;
    }

    public String getShipperName() {
        return ShipperName;
    }

    public void setShipperName(String shipperName) {
        ShipperName = shipperName;
    }

    public String getShipperPhoneNo() {
        return ShipperPhoneNo;
    }

    public void setShipperPhoneNo(String shipperPhoneNo) {
        ShipperPhoneNo = shipperPhoneNo;
    }

    public String getExpectedTime() {
        return ExpectedTime;
    }

    public void setExpectedTime(String expectedTime) {
        ExpectedTime = expectedTime;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getTotalamount() {
        return Totalamount;
    }

    public void setTotalamount(String totalamount) {
        Totalamount = totalamount;
    }

    public String getCustomerEmail() {
        return CustomerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        CustomerEmail = customerEmail;
    }
}
