package taembe.example.blackwine.taembe.model.stores.product;

import java.util.List;

/**
 * Created by Blackwine on 2/22/2017.
 */

public class OptionProduct {
    private List<AdditionalOption> additional_fields;
    private String is_require;
    private String option_id;
    private String sort_order;
    private String title;

    public List<AdditionalOption> getAdditional_fields() {
        return additional_fields;
    }

    public void setAdditional_fields(List<AdditionalOption> additional_fields) {
        this.additional_fields = additional_fields;
    }

    public String getIs_require() {
        return is_require;
    }

    public void setIs_require(String is_require) {
        this.is_require = is_require;
    }

    public String getOption_id() {
        return option_id;
    }

    public void setOption_id(String option_id) {
        this.option_id = option_id;
    }

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

    public OptionProduct(List<AdditionalOption> additional_fields, String is_require, String option_id, String sort_order, String title) {
        this.additional_fields = additional_fields;
        this.is_require = is_require;
        this.option_id = option_id;
        this.sort_order = sort_order;
        this.title = title;
    }


}
