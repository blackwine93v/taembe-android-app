package taembe.example.blackwine.taembe.model.stores.search;

import java.util.List;

import taembe.example.blackwine.taembe.model.stores.category.Category;
import taembe.example.blackwine.taembe.model.stores.product.Product;

/**
 * Created by Blackwine on 2/26/2017.
 */

public class SearchDataResponse {
    private int nextStartIndex, previousStartIndex, totalResults;
    private List<Product> products;
    private List<Category> childrenCategories;
    private String categoryName;

    public List<Category> getChildrenCategories() {
        return childrenCategories;
    }

    public void setChildrenCategories(List<Category> childrenCategories) {
        this.childrenCategories = childrenCategories;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getNextStartIndex() {
        return nextStartIndex;
    }

    public void setNextStartIndex(int nextStartIndex) {
        this.nextStartIndex = nextStartIndex;
    }

    public int getPreviousStartIndex() {
        return previousStartIndex;
    }

    public void setPreviousStartIndex(int previousStartIndex) {
        this.previousStartIndex = previousStartIndex;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
