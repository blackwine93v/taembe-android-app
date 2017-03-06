package taembe.example.blackwine.taembe.model.stores.promotion;

import taembe.example.blackwine.taembe.common.MyGlobal;

/**
 * Created by Blackwine on 2/25/2017.
 */

public class Promotion {
    private String imgUrl, link, title, type;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getLink() {
        return link.substring(1); //replace first slash cause wrong URL
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFullUrlImg(){
        return MyGlobal.getBaseUrl() + imgUrl.substring(1);
    }
}
