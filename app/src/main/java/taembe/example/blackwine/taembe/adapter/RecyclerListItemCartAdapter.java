package taembe.example.blackwine.taembe.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.util.List;

import taembe.example.blackwine.taembe.R;
import taembe.example.blackwine.taembe.model.stores.cart.ItemCartModel;

import static android.R.attr.type;
import static taembe.example.blackwine.taembe.R.id.ivImageProduct;

/**
 * Created by Blackwine on 2/13/2017.
 */

public class RecyclerListItemCartAdapter extends RecyclerView.Adapter<RecyclerListItemCartAdapter.ViewHolder> {
    private Context context;
    private List<ItemCartModel> items;
    private final String TAG = getClass().getSimpleName();
    private int LAYOUT_CART_ITEM_TYPE = R.layout.item_cart_vertical;


    public RecyclerListItemCartAdapter(Context context, List<ItemCartModel> items) {
        this.context = context;
        this.items = items;
    }


    public int getItemType() {
        return type;
    }


    public interface setOnClickItem {
        void onClickItemCart(ItemCartModel item);
        void onClickButtonDelete(ItemCartModel item, int position);
        void onChangeQty(ItemCartModel item, int s);
    }

    private  RecyclerListItemCartAdapter.setOnClickItem listener;
    public  void setOnClickItemEvent(RecyclerListItemCartAdapter.setOnClickItem listener){
        this.listener = listener;
    }

    public  void setOnClickButtonEvent(RecyclerListItemCartAdapter.setOnClickItem listener){
        this.listener = listener;
    }


    @Override
    public RecyclerListItemCartAdapter.ViewHolder  onCreateViewHolder(ViewGroup parent, int viewType) {
        Context mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View itemView =  inflater.inflate(LAYOUT_CART_ITEM_TYPE ,parent,false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final ItemCartModel item = items.get(position);
        StringBuilder options = new StringBuilder();
        for(int i =0;i<item.getOptions().size();i++){
            JsonObject option = item.getOptions().get(i);
            options.append(new Gson().toJson(option).replaceAll("[{}]","")+"\n");
        }

        ImageView ivImageProduct = holder.ivProductImage;
        TextView tvProductTitle = holder.tvProductTitle;
        TextView tvProductPrice = holder.tvProductPrice;
        TextView tvOptionProduct = holder.tvOptionProduct;
        Spinner spinnerQty = holder.spinnerQty;

        String[] qty = new String[20];
        for(int i=0;i<20;i++){
            qty[i]=String.valueOf(i+1);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(holder.itemView.getContext(),R.layout.support_simple_spinner_dropdown_item, qty);
        spinnerQty.setAdapter(adapter);
        spinnerQty.setSelection(item.getQty()-1);

        tvOptionProduct.setText(options);
        tvProductTitle.setText(item.getProduct().getName());
        tvProductPrice.setText(String.valueOf(item.getProduct().getFinalPrice_integer())+"đ");
        Picasso.with(context).load(item.getProduct().getFirstImage()).into(ivImageProduct);

        holder.spinnerQty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                listener.onChangeQty(item, position+1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.onClickItemCart(item);
                }
            }
        });

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.onClickButtonDelete(item, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivProductImage;
        public TextView tvProductTitle, tvProductPrice,tvOptionProduct;
        public Button button;
        public Spinner spinnerQty;

        private final String TAG = getClass().getSimpleName();

        public ViewHolder(View itemView) {
            super(itemView);
            ivProductImage = (ImageView) itemView.findViewById(ivImageProduct);
            tvProductTitle = (TextView) itemView.findViewById(R.id.tvTitleProduct);
            tvProductPrice = (TextView) itemView.findViewById(R.id.tvPriceProduct);
            button = (Button) itemView.findViewById(R.id.button);
            tvOptionProduct = (TextView) itemView.findViewById(R.id.tvOptionProduct);
            spinnerQty = (Spinner)itemView.findViewById(R.id.spinnerQty);
        }
    }
}
