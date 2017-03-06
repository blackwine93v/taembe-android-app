package taembe.example.blackwine.taembe.model.stores.promotion;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Blackwine on 2/25/2017.
 */

public class PromotionDataResponse {
    private List<Promotion> allPromo;

    public List<Promotion> getAllPromo() {
        return allPromo;
    }

    public void setAllPromo(List<Promotion> allPromo) {
        this.allPromo = allPromo;
    }

    public List<Promotion> getBanner(){
        List<Promotion> banners = new ArrayList<>();
        for(int i=0;i<allPromo.size();i++){
            if(allPromo.get(i).getType().compareTo("Banner")==0)
                banners.add(allPromo.get(i));
        }
        return banners;
    }

    public List<Promotion> getPromotion(){
        List<Promotion> promotions = new ArrayList<>();
        for(int i=0;i<allPromo.size();i++){
            if(allPromo.get(i).getType().compareTo("Promotion")==0)
                promotions.add(allPromo.get(i));
        }
        return promotions;
    }
}
