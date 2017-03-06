package taembe.example.blackwine.taembe.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import taembe.example.blackwine.taembe.R;
import taembe.example.blackwine.taembe.model.stores.product.Product;

import static taembe.example.blackwine.taembe.R.id.ivImageProduct;

/**
 * Created by Blackwine on 2/13/2017.
 */

public class RecyclerListProductAdapter extends RecyclerView.Adapter<RecyclerListProductAdapter.ViewHolder> {
    private Context context;
    private List<Product> products;
    //private int item_layout;
    private int type;
    private final String TAG = getClass().getSimpleName();

    private int LAYOUT_PRODUCT_TYPE_HORIZONTAL = R.layout.item_product_horizontal;
    private int LAYOUT_PRODUCT_TYPE_VERTICAL = R.layout.item_product_vertical;
    public static int LAYOUT_PRODUCT_TYPE_HORIZONTAL_TYPE = 1;
    public static int LAYOUT_PRODUCT_TYPE_VERTICAL_TYPE = 2;

    public RecyclerListProductAdapter(Context context, List<Product> products, int type) {
        this.context = context;
        this.products = products;
        this.type = type;
    }


    public int getItemType() {
        return type;
    }

    public interface setOnClickItem {
        void onClickItemProduct(Product product);
        void onClickButtonBuy(Product product);
    }

    private  RecyclerListProductAdapter.setOnClickItem listener;
    public  void setOnClickItemEvent(RecyclerListProductAdapter.setOnClickItem listener){
        this.listener = listener;
    }

    public  void setOnClickButtonEvent(RecyclerListProductAdapter.setOnClickItem listener){
        this.listener = listener;
    }


    @Override
    public RecyclerListProductAdapter.ViewHolder  onCreateViewHolder(ViewGroup parent, int viewType) {
        Context mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = null;
        if(getItemType()==LAYOUT_PRODUCT_TYPE_HORIZONTAL_TYPE)
            itemView = inflater.inflate(LAYOUT_PRODUCT_TYPE_HORIZONTAL,parent,false);
        else if(getItemType()==LAYOUT_PRODUCT_TYPE_VERTICAL_TYPE)
            itemView =  inflater.inflate(LAYOUT_PRODUCT_TYPE_VERTICAL,parent,false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Product product = products.get(position);
        float discountPercentFloat = product.getSpecialPrice_integer()>0?(float)product.getSpecialPrice_integer()/(float)product.getPrice_integer()*100:0;

        ImageView ivImageProduct = holder.ivProductImage;
        TextView tvProductTitle = holder.tvProductTitle;
        TextView tvProductPrice = holder.tvProductPrice;
        TextView tvDiscountProductTag = holder.tvDiscountProductTag;
        tvProductTitle.setText(product.getName());
        tvProductPrice.setText(String.valueOf(product.getFinalPrice_integer())+"đ");

        if(discountPercentFloat != 0) {
            tvDiscountProductTag.setVisibility(View.VISIBLE);
            tvDiscountProductTag.setText("-"+String.valueOf(100 - Math.round(discountPercentFloat))+"%");
        }
        else{
            tvDiscountProductTag.setVisibility(View.INVISIBLE);
        }
        Picasso.with(context).load(products.get(position).getFirstImage()).into(ivImageProduct);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.onClickItemProduct(product);
                }
            }
        });

        /*if(getItemType()==LAYOUT_PRODUCT_TYPE_VERTICAL_TYPE){
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        listener.onClickButtonBuy(product);
                    }
                }
            });
        }*/

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivProductImage;
        public TextView tvProductTitle, tvProductPrice, tvDiscountProductTag;
        //public Button button;

        private final String TAG = getClass().getSimpleName();

        public ViewHolder(View itemView) {
            super(itemView);
            ivProductImage = (ImageView) itemView.findViewById(ivImageProduct);
            tvProductTitle = (TextView) itemView.findViewById(R.id.tvTitleProduct);
            tvProductPrice = (TextView) itemView.findViewById(R.id.tvPriceProduct);
            tvDiscountProductTag = (TextView) itemView.findViewById(R.id.tvDiscountProductTag);
            //button = (Button) itemView.findViewById(R.id.button);
        }
    }
}
