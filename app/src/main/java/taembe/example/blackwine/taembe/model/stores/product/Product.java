package taembe.example.blackwine.taembe.model.stores.product;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Blackwine on 2/20/2017.
 */


public class Product {
    private String sku, name, en_name, feature_1, feature_2,feature_3, feature_4,feature_5,
            price, special_price, special_to_date,special_from_date, status;
    private int price_integer;
    private String made_in;
    private List <OptionProduct> options =  new ArrayList<>();
    private String brand_origin;
    private String manufacturer;
    private List<Image> images = new ArrayList<>();
    private StockDataProduct stock_data;

    public Product() {
    }

    public Product(String name, String price) {
        this.name = name;
        this.price = price;
    }

    public List<OptionProduct> getOptions() {
        return options;
    }

    public void setOptions(List<OptionProduct> options) {
        this.options = options;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice_integer() {
        this.price_integer = Math.round(Float.parseFloat(price));
        return price_integer;
    }

    public int getSpecialPrice_integer() {
        Date date, today = new Date();
        SimpleDateFormat df = null;
        if(special_price==null)
            return 0;
        int special_price_integer = Math.round(Float.parseFloat(special_price));
        if(special_to_date!=null) {
            try {
                date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH).parse(special_to_date);
                if(today.before(date)){
                    //is Apply special price
                    return special_price_integer;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return special_price_integer;
    }

    public int getFinalPrice_integer(){
        if(getSpecialPrice_integer()>0)
            return getSpecialPrice_integer();
        return getPrice_integer();
    }

    public void setPrice_integer(int price_integer) {
        this.price_integer = price_integer;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getEn_name() {
        return en_name;
    }

    public void setEn_name(String en_name) {
        this.en_name = en_name;
    }

    public String getFeature_1() {
        return feature_1;
    }



    public void setFeature_1(String feature_1) {
        this.feature_1 = feature_1;
    }

    public String getFeature_2() {
        return feature_2;
    }

    public void setFeature_2(String feature_2) {
        this.feature_2 = feature_2;
    }

    public String getFeature_3() {
        return feature_3;
    }

    public void setFeature_3(String feature_3) {
        this.feature_3 = feature_3;
    }

    public String getFeature_4() {
        return feature_4;
    }

    public void setFeature_4(String feature_4) {
        this.feature_4 = feature_4;
    }

    public String getFeature_5() {
        return feature_5;
    }

    public void setFeature_5(String feature_5) {
        this.feature_5 = feature_5;
    }

    public String getSpecial_price() {
        return special_price;
    }

    public void setSpecial_price(String special_price) {
        this.special_price = special_price;
    }

    public String getSpecial_to_date() {
        return special_to_date;
    }

    public void setSpecial_to_date(String special_to_date) {
        this.special_to_date = special_to_date;
    }

    public String getSpecial_from_date() {
        return special_from_date;
    }

    public void setSpecial_from_date(String special_from_date) {
        this.special_from_date = special_from_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMade_in() {
        return made_in;
    }

    public void setMade_in(String made_in) {
        this.made_in = made_in;
    }

    public String getBrand_origin() {
        return brand_origin;
    }

    public void setBrand_origin(String brand_origin) {
        this.brand_origin = brand_origin;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public StockDataProduct getStock_data() {
        return stock_data;
    }

    public void setStock_data(StockDataProduct stock_data) {
        this.stock_data = stock_data;
    }

    public String getFirstImage(){
        if(images == null)
            return null;
        if(images.size()>0){
            return images.get(0).getFullURL();
        }
        return null;
    }

    public List<String> getAllFeatureVi(){
        List<String> s = new ArrayList<>();
        if(feature_1 != null)
            s.add(feature_1);
        if(feature_2 != null)
            s.add(feature_2);
        if(feature_3 != null)
            s.add(feature_3);
        if(feature_4 != null)
            s.add(feature_4);
        if(feature_5 != null)
            s.add(feature_5);
        if(s.size()==0){
            String tmp = "No desciption";
            s.add(tmp);
            return s;
        }

        return s;
    }



}
