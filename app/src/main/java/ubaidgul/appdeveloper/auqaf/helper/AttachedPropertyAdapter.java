package ubaidgul.appdeveloper.auqaf.helper;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import ubaidgul.appdeveloper.auqaf.R;

public class AttachedPropertyAdapter extends RecyclerView.Adapter<AttachedPropertyAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<AttachedProperty> contactList;
    private List<AttachedProperty> contactListFiltered;
    String charString;
    private AttachedPropertyAdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title,sub_title, tv_category,tv_register_sr,tv_land_seq,tv_address;//tv_notification,
        public AppCompatButton btnStartSurvey, btnAttachedProperties,btnViewData;
        public ImageView ivIcon;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            sub_title = view.findViewById(R.id.sub_title);
            tv_address = view.findViewById(R.id.location);
            tv_category = view.findViewById(R.id.tv_category);
//            tv_notification = view.findViewById(R.id.tv_notification_no);
            tv_register_sr = view.findViewById(R.id.register_sr);
            tv_land_seq = view.findViewById(R.id.land_seq);
            ivIcon = view.findViewById(R.id.iv_icon);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
//                    listener.onAuqafPropertySelected(contactListFiltered.get(getAdapterPosition()));
                    listener.onAttachedPropertySelected(contactListFiltered.get(getAbsoluteAdapterPosition()));
                }
            });
        }
    }


    public AttachedPropertyAdapter(Context context, List<AttachedProperty> contactList, AttachedPropertyAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.contactList = contactList;
        this.contactListFiltered = contactList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.attached_prperties_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    public static CharSequence highlightText(String search, String originalText) {
        if (search != null && !search.equalsIgnoreCase("")) {
            String normalizedText = Normalizer.normalize(originalText, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
            int start = normalizedText.indexOf(search);
            if (start < 0) {
                return originalText;
            } else {
                Spannable highlighted = new SpannableString(originalText);
                while (start >= 0) {
                    int spanStart = Math.min(start, originalText.length());
                    int spanEnd = Math.min(start + search.length(), originalText.length());
                    highlighted.setSpan(new ForegroundColorSpan(Color.YELLOW), spanStart, spanEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    start = normalizedText.indexOf(search, spanEnd);
                }
                return highlighted;
            }
        }
        return originalText;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        try {
            final AttachedProperty contact = contactListFiltered.get(position);
            holder.title.setText(highlightText(charString,contact.getLeaseeName()));
            holder.sub_title.setText(highlightText(charString,contact.getLeaseeFName()));
            holder.tv_address.setText(highlightText(charString,contact.getAreaName()));
            holder.tv_category.setText(highlightText(charString,contact.getLanduseType()));
//            holder.tv_notification.setText(highlightText(charString,contact.getNotificationNo()));
            holder.tv_land_seq.setText(String.valueOf(contact.getLandSequence()));
            holder.tv_register_sr.setText(String.valueOf(contact.getRegisterSrNo()));
            if (contact.getLanduseType().equals("Shop")){
                holder.ivIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.shop));
            } else if (contact.getLanduseType().equals("House")){
                holder.ivIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.home));
            } else if (contact.getLanduseType().equals("Hospital")){
                holder.ivIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.hospital));
            } else if (contact.getLanduseType().equals("Dispensary")){
                holder.ivIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.pharmacy));
            } else if (contact.getLanduseType().equals("Madrisa") || contact.getLanduseType().equals("Madrasa")){
                holder.ivIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.madrisa));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return contactListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                charString = charSequence.toString();
                if (charString.isEmpty()) {
                    contactListFiltered = contactList;
                } else {
                    List<AttachedProperty> filteredList = new ArrayList<>();
                    for (AttachedProperty row : contactList) {
                        // title match condition. this might differ depending on your requirement
                        // here we are looking for title or type number match
                        if (row.getLeaseeName().toLowerCase().contains(charString.toLowerCase()) || row.getLeaseeFName().toLowerCase().contains(charSequence)|| row.getLanduseType().toLowerCase().contains(charSequence) ||  row.getAreaName().toLowerCase().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }
                    contactListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactListFiltered = (ArrayList<AttachedProperty>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface AttachedPropertyAdapterListener {
        void onAttachedPropertySelected(AttachedProperty contact);
    }
}
