package com.annotation.drinking_zone.ProductRelated;

public class ProductsData {

//    private  int id;
    private String id;
    private String product_category;
    private String product_name;
    private String product_price;
    private String product_deposit;
    private String product_image_link;
    private String upto_quantity;
    private String max_quantity;
    private String min_quantity;
    private String product_isFresh;
    private String quantity;
    private String subTotal;
    private int reference_category;
    private String product_category_name;
    private String product_id;
    private String product_Status;
    private String totalPrice;
    private String address_id;
    private String bookingId;
    private String end_date;
    private String start_date;
    private String booked_on;
    private String reminder;
    private String transaction_title;
    private String transaction_type;
    private String transaction_amount;
    private String transaction_closing_balance;
    private String transaction_time;
    private String order_number;
    private String total_amount ;
    private String payment_status;
    private String delivery_time ;
    private String delivery_name;
    private String is_quick;
    private String status;



    public ProductsData(String transaction_title, String transaction_type, String transaction_amount, String transaction_closing_balance, String transaction_time) {
        this.transaction_title = transaction_title;
        this.transaction_type = transaction_type;
        this.transaction_amount = transaction_amount;
        this.transaction_closing_balance = transaction_closing_balance;
        this.transaction_time = transaction_time;
    }

    public ProductsData(String id, String order_number, String total_amount, String payment_status, String delivery_time, String delivery_name, String is_quick, String status) {
        this.id = id;
        this.order_number = order_number;
        this.total_amount = total_amount;
        this.payment_status = payment_status;
        this.delivery_time = delivery_time;
        this.delivery_name = delivery_name;
        this.is_quick = is_quick;
        this.status = status;
    }

    public ProductsData(String id, String product_category, String product_category_name, String product_name, String product_price, String product_deposit, String product_image_link, String upto_quantity, String min_quantity, String max_quantity) {
        this.id = id;
        this.product_category = product_category;
        this.product_name = product_name;
        this.product_price = product_price;
        this.product_deposit = product_deposit;
        this.product_image_link = product_image_link;
        this.upto_quantity = upto_quantity;
        this.min_quantity = min_quantity;
        this.max_quantity = max_quantity;
        this.product_category_name = product_category_name;
    }

    public ProductsData(String id, String product_category, String product_category_name, String product_name, String product_price, String product_deposit, String product_image_link, String upto_quantity, String min_quantity, String max_quantity, int reference_category) {
        this.id = id;
        this.product_category = product_category;
        this.product_name = product_name;
        this.product_price = product_price;
        this.product_deposit = product_deposit;
        this.product_image_link = product_image_link;
        this.upto_quantity = upto_quantity;
        this.min_quantity = min_quantity;
        this.max_quantity = max_quantity;
        this.product_category_name = product_category_name;
        this.reference_category = reference_category;
    }

    public ProductsData(String cid, String product_id, String product_name, String product_price, String product_deposit, String product_image_link, String quantity, String product_isFresh, String upto_quantity, String subTotal, String productStatus) {
        this.id = cid;
        this.product_id = product_id;
        this.product_name = product_name;
        this.product_price = product_price;
        this.product_deposit = product_deposit;
        this.product_image_link = product_image_link;
        this.product_isFresh = product_isFresh;
        this.quantity = quantity;
        this.upto_quantity = upto_quantity;
        this.subTotal = subTotal;
        this.product_Status = productStatus;
    }

    public ProductsData(String id, String product_name, String booking_id, String product_price, String product_deposit, String total_price, String sub_total, String product_image_link, String quantity, String booked_on, String start_date, String end_date, String address_id, String reminder) {
        this.id = id;
        this.product_name = product_name;
        this.product_price = product_price;
        this.product_deposit = product_deposit;
        this.product_image_link = product_image_link;
        this.subTotal = sub_total;
        this.quantity = quantity;
        this.totalPrice = total_price;
        this.bookingId = booking_id;
        this.booked_on = booked_on;
        this.start_date = start_date;
        this.end_date = end_date;
        this.address_id = address_id;
        this.reminder = reminder;
    }

    public ProductsData(String product_name, String product_price, String product_deposit, String product_image_link, String quantity, String product_isFresh, String  subTotal) {
        this.product_name = product_name;
        this.product_price = product_price;
        this.product_deposit = product_deposit;
        this.product_image_link = product_image_link;
        this.quantity = quantity;
        this.product_isFresh =product_isFresh;
        this.subTotal = subTotal;
    }

    public String getTransaction_title() {
        return transaction_title;
    }

    public void setTransaction_title(String transaction_title) {
        this.transaction_title = transaction_title;
    }

    public String getTransaction_type() {
        return transaction_type;
    }

    public void setTransaction_type(String transaction_type) {
        this.transaction_type = transaction_type;
    }

    public String getTransaction_amount() {
        return transaction_amount;
    }

    public void setTransaction_amount(String transaction_amount) {
        this.transaction_amount = transaction_amount;
    }

    public String getTransaction_closing_balance() {
        return transaction_closing_balance;
    }

    public void setTransaction_closing_balance(String transaction_closing_balance) {
        this.transaction_closing_balance = transaction_closing_balance;
    }

    public String getTransaction_time() {
        return transaction_time;
    }

    public void setTransaction_time(String transaction_time) {
        this.transaction_time = transaction_time;
    }

    public String getReminder() {
        return reminder;
    }

    public void setReminder(String reminder) {
        this.reminder = reminder;
    }

    public String getAddress_id() {
        return address_id;
    }

    public void setAddress_id(String address_id) {
        this.address_id = address_id;
    }


    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getBooked_on() {
        return booked_on;
    }

    public void setBooked_on(String booked_on) {
        this.booked_on = booked_on;
    }

    public String getUpto_quantity() {
        return upto_quantity;
    }

    public void setUpto_quantity(String upto_quantity) {
        this.upto_quantity = upto_quantity;
    }

    public String getMax_quantity() {
        return max_quantity;
    }

    public void setMax_quantity(String max_quantity) {
        this.max_quantity = max_quantity;
    }

    public String getMin_quantity() {
        return min_quantity;
    }

    public void setMin_quantity(String min_quantity) {
        this.min_quantity = min_quantity;
    }

    public String getProduct_isFresh() {
        return product_isFresh;
    }

    public void setProduct_isFresh(String product_isFresh) {
        this.product_isFresh = product_isFresh;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }


    public String getProduct_category() {
        return product_category;
    }

    public void setProduct_category(String product_category) {
        this.product_category = product_category;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getProduct_deposit() {
        return product_deposit;
    }

    public void setProduct_deposit(String product_deposit) {
        this.product_deposit = product_deposit;
    }

    public String getProduct_image_link() {
        return product_image_link;
    }

    public void setProduct_image_link(String product_image_link) {
        this.product_image_link = product_image_link;
    }

    public String getProduct_category_name() {
        return product_category_name;
    }

    public void setProduct_category_name(String product_category_name) {
        this.product_category_name = product_category_name;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_Status() {
        return product_Status;
    }

    public void setProduct_Status(String product_Status) {
        this.product_Status = product_Status;
    }

    public int getReference_category() {
        return reference_category;
    }

    public void setReference_category(int reference_category) {
        this.reference_category = reference_category;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public String getDelivery_time() {
        return delivery_time;
    }

    public void setDelivery_time(String delivery_time) {
        this.delivery_time = delivery_time;
    }

    public String getDelivery_name() {
        return delivery_name;
    }

    public void setDelivery_name(String delivery_name) {
        this.delivery_name = delivery_name;
    }

    public String getIs_quick() {
        return is_quick;
    }

    public void setIs_quick(String is_quick) {
        this.is_quick = is_quick;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
