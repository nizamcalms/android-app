package com.bazinga.lantoonnew.home.changereferlanguage;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bazinga.lantoonnew.R;
import com.bazinga.lantoonnew.registration.langselection.model.Language;
import com.bazinga.lantoonnew.retrofit.ApiClient;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class ChangeReferenceLanguageAdapter extends ArrayAdapter<Language> {
    int referLang;

    public ChangeReferenceLanguageAdapter(Context context, List<Language> languageArrayList, int referLang) {
        super(context, 0, languageArrayList);
        this.referLang = referLang;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        //Language myLanguageData = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_change_language, parent, false);

        }

        ImageView imageView = convertView.findViewById(R.id.imgView);
        //LinearLayout llItem = convertView.findViewById(R.id.llItem);
        TextView tvName = convertView.findViewById(R.id.textView);

        // Populate the data into the template view using the data object
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transform(new CenterCrop(), new RoundedCorners(30));
        if (ApiClient.isTest)
            Glide.with(getContext()).load(ApiClient.BASE_TEST_URL + "Lantoon/" + getItem(position).getImagePath()).apply(requestOptions).into(imageView);
        else
            Glide.with(getContext()).load(ApiClient.BASE_PROD_URL + "Lantoon/" + getItem(position).getImagePath()).apply(requestOptions).into(imageView);
        //tvName.setText(getItem(position).getLanguageName() + " / " + getItem(position).getNativeName());
        tvName.setText(getItem(position).getNativeName());
        if (Integer.valueOf(getItem(position).getLanguageID()) == referLang) {
            tvName.setBackground(getContext().getDrawable(R.drawable.bg_tv_laguage_selected));
            tvName.setTextColor(getContext().getColor(R.color.black));
        } else {
            tvName.setBackground(getContext().getDrawable(R.drawable.bg_tv_laguage));
            tvName.setTextColor(getContext().getColor(R.color.white));
        }


        // Return the completed view to render on screen
        return convertView;
    }
}