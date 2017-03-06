package taembe.example.blackwine.taembe.model.stores.product;

/**
 * Created by Blackwine on 2/22/2017.
 */

public class AdditionalOption {
    private String sort_order;
    private String title;
    private String  value_id;

    public String getSort_order() {
        return sort_order;
    }

    public void setSort_order(String sort_order) {
        this.sort_order = sort_order;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue_id() {
        return value_id;
    }

    public void setValue_id(String value_id) {
        this.value_id = value_id;
    }

    public AdditionalOption(String sort_order, String title, String value_id) {

        this.sort_order = sort_order;
        this.title = title;
        this.value_id = value_id;
    }
}
