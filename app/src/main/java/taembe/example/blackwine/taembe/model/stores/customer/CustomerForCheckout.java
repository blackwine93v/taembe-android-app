package taembe.example.blackwine.taembe.model.stores.customer;

/**
 * Created by Blackwine on 2/24/2017.
 */

public class CustomerForCheckout extends Customer {
    private String comment, currentPaymentMethod;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCurrentPaymentMethod() {
        return currentPaymentMethod;
    }

    public void setCurrentPaymentMethod(String currentPaymentMethod) {
        this.currentPaymentMethod = currentPaymentMethod;
    }

}
