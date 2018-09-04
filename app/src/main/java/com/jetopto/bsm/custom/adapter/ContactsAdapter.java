package com.jetopto.bsm.custom.adapter;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jetopto.bsm.R;
import com.jetopto.bsm.utils.classes.UserContacts;

import java.util.ArrayList;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ItemViewHolder> {

    private final String TAG = ContactsAdapter.class.getSimpleName();

    private ArrayList<UserContacts> mContactsList;
    private ItemClickListener mListener;

    public interface ItemClickListener {
        void onItemClick(UserContacts contact);
    }

    public ContactsAdapter(ArrayList<UserContacts> list, ItemClickListener listener) {
        mContactsList = list;
        mListener = listener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contacts
                , parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        final UserContacts contact = mContactsList.get(position);
        holder.mPhoneNumber.setText(contact.getPhoneNumber());
        holder.mContactName.setText(contact.getName());
        String thumb = contact.getThumbUri();
        if (null != thumb) {
            holder.mContactIcon.setImageURI(Uri.parse(thumb));
        }
        holder.mPhoneIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(contact);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mContactsList.size();
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView mContactIcon;
        ImageView mPhoneIcon;
        TextView mContactName;
        TextView mPhoneNumber;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mContactIcon = itemView.findViewById(R.id.contact_icon);
            mPhoneIcon = itemView.findViewById(R.id.phone_icon);
            mContactName = itemView.findViewById(R.id.contact_name);
            mPhoneNumber = itemView.findViewById(R.id.phome_number);
        }
    }
}
