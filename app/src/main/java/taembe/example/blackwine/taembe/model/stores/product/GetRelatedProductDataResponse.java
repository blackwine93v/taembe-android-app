package taembe.example.blackwine.taembe.model.stores.product;

import java.util.List;

import taembe.example.blackwine.taembe.common.MyUtils;

/**
 * Created by Blackwine on 2/25/2017.
 */

public class GetRelatedProductDataResponse {
    private List<Product> data;

    public List<Product> getData() {
        return MyUtils.filterProducts(data);
    }

    public void setData(List<Product> data) {
        this.data = data;
    }
}
