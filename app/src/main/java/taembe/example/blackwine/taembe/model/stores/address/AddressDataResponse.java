package taembe.example.blackwine.taembe.model.stores.address;

import java.util.List;

/**
 * Created by Blackwine on 2/26/2017.
 */

public class AddressDataResponse {
    private List<Address> region;

    public List<Address> getRegion() {
        return region;
    }

    public void setRegion(List<Address> region) {
        this.region = region;
    }
}
