package taembe.example.blackwine.taembe.model.stores.address;

/**
 * Created by Blackwine on 2/26/2017.
 */

public class Address {
    private String code, name;
    private boolean state;
    private  DeliveryRule delivery;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public DeliveryRule getDelivery() {
        return delivery;
    }

    public void setDelivery(DeliveryRule delivery) {
        this.delivery = delivery;
    }
}
