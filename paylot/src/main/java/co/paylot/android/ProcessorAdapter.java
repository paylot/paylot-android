package co.paylot.android;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;
import java.util.Locale;

import co.paylot.android.models.ProcessorItem;

/**
 * Created with love by Dozie on 10/24/2018.
 */

public class ProcessorAdapter extends RecyclerView.Adapter<ProcessorAdapter.ViewHolder> {

    private Context context;
    private List<ProcessorItem> processors;
    private OnItemClickListener listener;

    public ProcessorAdapter(Context context, List<ProcessorItem> processors) {
        this.context = context;
        this.processors = processors;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.currency_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final ProcessorItem item = getItem(position);

        holder.nameText.setText(item.getCurrencyData().getName());

        String amountText = String.format(Locale.getDefault(), "%f %s",
                item.getAmount(),
                item.getCurrencyData().getSymbol());

        holder.valueText.setText(amountText);

        String logoUrl = String.format("https://res.cloudinary.com/dozie/image/upload/paylot/%s.png",
                item.getCurrencyData().getSymbol());

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.paylot_ic_currency);

        Glide.with(context).setDefaultRequestOptions(requestOptions)
                .load(logoUrl)
                .into(holder.logoView);

        holder.cardBody.setCardBackgroundColor(getColor(item.isSelected() ? R.color.paylot_skyBlue : R.color.paylot_white));
        holder.cardBody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectItem(holder.getAdapterPosition());
                if(listener != null){
                    listener.onItemClick(item);
                }
            }
        });
        holder.nameText.setTextColor(getColor(item.isSelected() ? R.color.paylot_white : R.color.paylot_black));
        holder.valueText.setTextColor(getColor(item.isSelected() ? R.color.paylot_white : R.color.paylot_black));
    }

    private void selectItem(int position) {
        for (int i = 0; i < getItemCount(); i++){
            ProcessorItem item = getItem(i);
            item.setSelected(i == position);
        }

        notifyDataSetChanged();
    }

    private int getColor(int colorId){
        return context.getResources().getColor(colorId);
    }

    @Override
    public int getItemCount() {
        return processors.size();
    }

    public ProcessorItem getItem(int position) {
        return processors.get(position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public interface OnItemClickListener{
        void onItemClick(ProcessorItem item);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameText, valueText;
        ImageView logoView;
        CardView cardBody;

        public ViewHolder(View itemView) {
            super(itemView);

            cardBody = itemView.findViewById(R.id.currency_layout);
            nameText = itemView.findViewById(R.id.currency_name);
            valueText = itemView.findViewById(R.id.currency_value);
            logoView = itemView.findViewById(R.id.currency_logo);
        }
    }
}
