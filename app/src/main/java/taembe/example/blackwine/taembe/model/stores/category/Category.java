package taembe.example.blackwine.taembe.model.stores.category;

import java.util.List;

/**
 * Created by Blackwine on 2/26/2017.
 */

public class Category {
    private String category_id, name, en_name, is_active, parent_id, url_key;
    private int level, position;
    private List<Category> childdren;

    public Category(String name, String url_key) {
        this.name = name;
        this.url_key = url_key;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEn_name() {
        return en_name;
    }

    public void setEn_name(String en_name) {
        this.en_name = en_name;
    }

    public String getIs_active() {
        return is_active;
    }

    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getUrl_key() {
        return url_key;
    }

    public void setUrl_key(String url_key) {
        this.url_key = url_key;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public List<Category> getChilddren() {
        return childdren;
    }

    public void setChilddren(List<Category> childdren) {
        this.childdren = childdren;
    }
}
