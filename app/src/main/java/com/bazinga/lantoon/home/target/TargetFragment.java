package com.bazinga.lantoon.home.target;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bazinga.lantoon.CommonFunction;
import com.bazinga.lantoon.NetworkUtil;
import com.bazinga.lantoon.R;
import com.bazinga.lantoon.home.HomeActivity;
import com.bazinga.lantoon.home.target.adpter.TargetListAdapter;
import com.bazinga.lantoon.home.target.model.Target;

import java.util.List;


public class TargetFragment extends Fragment implements View.OnClickListener {

    private TargetViewModel targetViewModel;
    Button btnCompleted, btnOnGoing, btnUpcoming;
    TargetListAdapter targetListAdapter;
    List<Target> targetList;
    ListView lvTargets;
    boolean fragmentDestroyed = false;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        targetViewModel = new ViewModelProvider(this).get(TargetViewModel.class);
        View root = inflater.inflate(R.layout.fragment_target, container, false);
        btnCompleted = root.findViewById(R.id.btnCompleted);
        btnOnGoing = root.findViewById(R.id.btnOnGoing);
        btnUpcoming = root.findViewById(R.id.btnUpcoming);
        btnCompleted.setOnClickListener(this::onClick);
        btnOnGoing.setOnClickListener(this::onClick);
        btnUpcoming.setOnClickListener(this::onClick);
        btnCompleted.setTextColor(Color.WHITE);
        //initial completed
        btnCompleted.setBackground(getActivity().getDrawable(R.drawable.button_bg));
        btnOnGoing.setTextColor(getActivity().getColor(R.color.target_button_text_disabled));
        btnOnGoing.setBackground(getActivity().getDrawable(R.drawable.button_bg_target_unselected));
        btnUpcoming.setTextColor(getActivity().getColor(R.color.target_button_text_disabled));
        btnUpcoming.setBackground(getActivity().getDrawable(R.drawable.button_bg_target_unselected));


        lvTargets = root.findViewById(R.id.lvTargets);
        //lvTargets.setVisibility(View.INVISIBLE);
        if (NetworkUtil.getConnectivityStatus(getContext()) != 0) {
            targetViewModel.getTargets(HomeActivity.sessionManager.getUid()).observe(getActivity(), new Observer<List<Target>>() {
                @Override
                public void onChanged(List<Target> targets) {
                    if (!fragmentDestroyed) {
                        if (targets != null) {
                            targetList = targets;

                        /*targetAdapter = new TargetAdapter(getContext(),targetList);
                        lvTargets.setAdapter(targetAdapter);*/
                            targetListAdapter = new TargetListAdapter(getContext(), targets);
                            targetListAdapter.getFilter().filter("1");
                            lvTargets.setAdapter(targetListAdapter);
                        }
                    }
                }
            });
        }else CommonFunction.netWorkErrorAlert(getActivity());
        return root;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCompleted:
                if(targetListAdapter!=null) {
                    targetListAdapter.getFilter().filter("1");
                    btnCompleted.setTextColor(Color.WHITE);
                    btnCompleted.setBackground(getActivity().getDrawable(R.drawable.button_bg));
                    btnOnGoing.setTextColor(getActivity().getColor(R.color.target_button_text_disabled));
                    btnOnGoing.setBackground(getActivity().getDrawable(R.drawable.button_bg_target_unselected));
                    btnUpcoming.setTextColor(getActivity().getColor(R.color.target_button_text_disabled));
                    btnUpcoming.setBackground(getActivity().getDrawable(R.drawable.button_bg_target_unselected));
                }
                break;
            case R.id.btnOnGoing:
                if(targetListAdapter!=null) {
                    targetListAdapter.getFilter().filter("2");
                    btnOnGoing.setTextColor(Color.WHITE);
                    btnOnGoing.setBackground(getActivity().getDrawable(R.drawable.button_bg));
                    btnCompleted.setTextColor(getActivity().getColor(R.color.target_button_text_disabled));
                    btnCompleted.setBackground(getActivity().getDrawable(R.drawable.button_bg_target_unselected));
                    btnUpcoming.setTextColor(getActivity().getColor(R.color.target_button_text_disabled));
                    btnUpcoming.setBackground(getActivity().getDrawable(R.drawable.button_bg_target_unselected));
                }
                break;
            case R.id.btnUpcoming:
                if(targetListAdapter!=null) {
                    targetListAdapter.getFilter().filter("3");
                    btnUpcoming.setTextColor(Color.WHITE);
                    btnUpcoming.setBackground(getActivity().getDrawable(R.drawable.button_bg));
                    btnOnGoing.setTextColor(getActivity().getColor(R.color.target_button_text_disabled));
                    btnOnGoing.setBackground(getActivity().getDrawable(R.drawable.button_bg_target_unselected));
                    btnCompleted.setTextColor(getActivity().getColor(R.color.target_button_text_disabled));
                    btnCompleted.setBackground(getActivity().getDrawable(R.drawable.button_bg_target_unselected));
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fragmentDestroyed = true;
    }

}