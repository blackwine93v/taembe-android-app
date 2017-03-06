package taembe.example.blackwine.taembe.model.stores.product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Blackwine on 2/25/2017.
 */

public class GetProductSkuDataResponse {
    private Product product;
    private List<Product> similar = new ArrayList<>();

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<Product> getSimilar() {
        return similar;
    }

    public void setSimilar(List<Product> similar) {
        this.similar = similar;
    }
}
