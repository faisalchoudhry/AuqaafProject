package ubaidgul.appdeveloper.auqaf.helper;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class AuqafPropertyAdapter extends RecyclerView.Adapter<AuqafPropertyAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<AuqafProperty> contactList;
    private List<AuqafProperty> contactListFiltered;
    String charString;
    private AuqafPropertyAdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, location,tvAddresslabel,tvCategoryLabel,tvOthers,tv_notification_no,tvCategory,tvNla,tvNlk,tvNlm,
                tvcultla,tvcultlk,tvcultlm,tvnCultla,tvnCultlk,tvnCultlm,tvZone,tvCircle;
//        public AppCompatButton btnStartSurvey,btnEditBaseData,btnShopsSurvey,btnHouseSurvey,btnOthersSurvey;
        public ImageView ivIcon;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            location = view.findViewById(R.id.location);
            tvCategory = view.findViewById(R.id.tv_category);
            tvAddresslabel = view.findViewById(R.id.tv_address_label);
            tvCategoryLabel = view.findViewById(R.id.tv_category_label);
//            tvNlm = view.findViewById(R.id.tv_nlm);
//            tvcultla = view.findViewById(R.id.tv_cultla);
//            tvcultlk = view.findViewById(R.id.tv_cultlk);
//            tvcultlm = view.findViewById(R.id.tv_cultlm);
//            tvnCultla = view.findViewById(R.id.tv_ncultla);
//            tvnCultlk = view.findViewById(R.id.tv_ncultlk);
//            tvnCultlm = view.findViewById(R.id.tv_ncultlm);
//            tvShops = view.findViewById(R.id.tv_shops);
//            tvHouses = view.findViewById(R.id.tv_house);
//            tvOthers = view.findViewById(R.id.tv_others);
            tv_notification_no = view.findViewById(R.id.tv_notification_no);
//            btnStartSurvey = view.findViewById(R.id.btn_start_survey);
//            btnEditBaseData = view.findViewById(R.id.btn_edit_base_data);
//            btnShopsSurvey = view.findViewById(R.id.btn_shops_survey);
//            btnHouseSurvey = view.findViewById(R.id.btn_house_survey);
//            btnOthersSurvey = view.findViewById(R.id.btn_others_survey);

            ivIcon = view.findViewById(R.id.iv_icon);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
//                    listener.onAuqafPropertySelected(contactListFiltered.get(getAdapterPosition()));
                    listener.onAuqafPropertySelected(contactListFiltered.get(getAbsoluteAdapterPosition()));
                }
            });
        }
    }


    public AuqafPropertyAdapter(Context context, List<AuqafProperty> contactList, AuqafPropertyAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.contactList = contactList;
        this.contactListFiltered = contactList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_results_list_item, parent, false);

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
            final AuqafProperty contact = contactListFiltered.get(position);
            holder.title.setText(highlightText(charString,contact.getWaqfPropertyName()));
            holder.location.setText(highlightText(charString,contact.getLocation()));
            holder.tv_notification_no.setText(highlightText(charString,contact.getNotificationNo()));
            holder.tvCategory.setText(highlightText(charString,contact.getCategory()));
            holder.tvCategoryLabel.setText("Category:");
            holder.tvAddresslabel.setText("Address:");
//            holder.tvZone.setText(highlightText(charString,"Z: "+contact.getZone()));
//            holder.tvCircle.setText(highlightText(charString,"C: "+contact.getCircle()));
//            holder.tvShops.setText(String.valueOf(contact.getShops()));
//            holder.tv_survey_status.setText(contact.getIsSurveyed()==1?"Yes":"No");
//            holder.tvHouses.setText(String.valueOf(contact.getHouses()));
//            holder.tvOthers.setText(String.valueOf(contact.getOthers()));
//            holder.tvCulLa.setText(Math.round((((contact.getCultA()*8)+(contact.getCultK()))*20)+contact.getCultM())+" M");
//            holder.tvNculLa.setText(Math.round((((contact.getNcultA()*8)+(contact.getNcultK()))*20)+contact.getNcultM())+" M");
//            holder.tvNla.setText(Math.round((((contact.getNlaA()*8)+(contact.getNlaK()))*20)+contact.getNlaM())+" M");
//            if (contact.getCategory().equals("Shrine")){
//                holder.ivIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.shrine));
//            } else if (contact.getCategory().equals("Mosque")){
//                holder.ivIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.mosque));
//            } else {
//                holder.ivIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.more_options));
//            }
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
                    List<AuqafProperty> filteredList = new ArrayList<>();
                    for (AuqafProperty row : contactList) {
                        // title match condition. this might differ depending on your requirement
                        // here we are looking for title or type number match
                        if (row.getWaqfPropertyName().toLowerCase().contains(charString.toLowerCase()) ||row.getNotificationNo().toLowerCase().contains(charString.toLowerCase()) || row.getCircle().toLowerCase().contains(charSequence)|| row.getZone().toLowerCase().contains(charSequence) || row.getCategory().toLowerCase().contains(charSequence) || row.getLocation().toLowerCase().contains(charSequence)) {
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
                contactListFiltered = (ArrayList<AuqafProperty>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface AuqafPropertyAdapterListener {
        void onAuqafPropertySelected(AuqafProperty contact);
    }
}
