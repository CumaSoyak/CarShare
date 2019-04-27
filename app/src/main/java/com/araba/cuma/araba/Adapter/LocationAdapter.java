package com.araba.cuma.araba.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.araba.cuma.araba.Model.Location;
import com.araba.cuma.araba.R;

import java.util.ArrayList;
import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> implements Filterable {
    private List<Location> locationList;
    private List<Location> locationListFiltered;
    private Context context;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(Location position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public LocationAdapter(List<Location> locationList, Context context,OnItemClickListener mListener) {
        this.locationList = locationList;
        this.context = context;
        this.locationListFiltered =locationList;
        this.mListener=mListener;
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_location_item, parent, false);
        return new LocationViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        final Location currentItem = locationListFiltered.get(position);

        holder.imageView.setImageResource(currentItem.getImageResource());
        holder.textView1.setText(currentItem.getCity());
        holder.textView2.setText(currentItem.getArea());
    }

    @Override
    public int getItemCount() {
        return locationListFiltered.size();
    }

    public void filterList(ArrayList<Location> filteredList) {
        locationListFiltered = filteredList;
        notifyDataSetChanged();
    }

   public class LocationViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView1;
        TextView textView2;
        CardView cardView;

        LocationViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            textView1 = itemView.findViewById(R.id.text_view1);
            textView2 = itemView.findViewById(R.id.text_view2);
            cardView = itemView.findViewById(R.id.city_cardview);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {

                            mListener.onItemClick(locationListFiltered.get(getAdapterPosition()));

                    }
                }
            });
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    locationListFiltered = locationList;
                } else {
                    List<Location> filteredList = new ArrayList<>();
                    for (Location row : locationList) {
                        if (row.getCity().toLowerCase().contains(charString.toLowerCase())
                                || row.getArea().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    locationListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = locationListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                locationListFiltered = (ArrayList<Location>) filterResults.values;
                notifyDataSetChanged();
            }

        };

    }
}