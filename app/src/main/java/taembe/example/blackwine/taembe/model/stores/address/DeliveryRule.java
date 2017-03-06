package taembe.example.blackwine.taembe.model.stores.address;

/**
 * Created by Blackwine on 2/26/2017.
 */

public class DeliveryRule {
    private int threshold, fee;

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }
}
