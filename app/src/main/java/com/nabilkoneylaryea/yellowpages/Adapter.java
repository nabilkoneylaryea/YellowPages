package com.nabilkoneylaryea.yellowpages;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> implements Filterable {

    private static final String TAG = "ADAPTER" ;
    private LayoutInflater layoutInflater;
    private List<Contact> data;
    private List<Contact> dataFull;
    private itemClickListener listener;

    Adapter(Context context, List<Contact> data, itemClickListener listener) {
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;
        this.dataFull = new ArrayList<>(data);
        this.listener = listener;
    }

    public List<Contact> getData() {
        return data;
    }

    public List<Contact> getDataFull() {
        return dataFull;
    }
    @NonNull

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.custom_view, parent, false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //bind the textview with data recieved
        Log.i(TAG, "onBindViewHolder: Binding..." + position);
        Contact contact = data.get(position);

        String firstName = contact.getFirstName();
        String lastName = contact.getLastName();
        String number = contact.getPhoneNumber();

        if (contact.getImg() != null) {
            Uri img = contact.getImg();
            holder.civ_pfp.setImageURI(img);
            Log.i(TAG, "onBindViewHolder: Setting Image");
        }
        holder.name.setText(firstName + " " + lastName);
        holder.phoneNumber.setText(number);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public Filter getFilter() {
        return contactFilter;
    }

    private Filter contactFilter = new Filter() {

        //Automatically doing first method on background thread KEY ATTRIBUTE OF THIS FILTERABLE INTERFACE
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Contact> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(dataFull);
            }
            else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(Contact contact : dataFull) {

                    if (contact.toString2().toLowerCase().trim().contains(filterPattern)) {
                        filteredList.add(contact);
                    }

                }

            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            data.clear();
            data.addAll((List)results.values);
            notifyDataSetChanged();

        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name, phoneNumber;
        CircleImageView civ_pfp;
        itemClickListener listener;

        public ViewHolder(@NonNull View itemView, itemClickListener listener) {
            super(itemView);
            name = itemView.findViewById(R.id.textView);
            phoneNumber = itemView.findViewById(R.id.textView2);
            civ_pfp = itemView.findViewById(R.id.civ_itemPfp);
            this.listener = listener;

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            listener.onClick(getAdapterPosition());
        }
    }
    public interface itemClickListener {
        void onClick(int position);
    }
}
