package taembe.example.blackwine.taembe.model.stores.product;

/**
 * Created by Blackwine on 2/21/2017.
 */

public class Image {
    private String id, etag, label, position, url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFullURL(){
//        final String URL_IMAGE = "http://back.taembe.com"+url;
//        return URL_IMAGE;
        final String BASE_URL_IMAGE = "https://taembe.vn/img/tag/", TAG_NAME="/detail.jpg";
        return BASE_URL_IMAGE + etag + TAG_NAME;
    }


    public Image(String id, String etag, String label, String position, String url) {

        this.id = id;
        this.etag = etag;
        this.label = label;
        this.position = position;
        this.url = url;
    }
}
