package com.bazinga.lantoon.home.chapter.lesson.ui.qp2;

import androidx.lifecycle.ViewModelProvider;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bazinga.lantoon.Audio;
import com.bazinga.lantoon.CommonFunction;
import com.bazinga.lantoon.PlayPauseView;
import com.bazinga.lantoon.R;
import com.bazinga.lantoon.Tags;
import com.bazinga.lantoon.home.chapter.lesson.QuestionsActivity;
import com.bazinga.lantoon.home.chapter.lesson.ReferencePopup;
import com.bazinga.lantoon.home.chapter.lesson.model.Question;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class QP2Fragment extends Fragment implements View.OnClickListener {

    private QP2ViewModel mViewModel;
    Audio audio;
    Question question;
    TextView tvQuestionNo, tvQuestionName;
    ImageButton imgBtnHome, imgBtnHelp;
    ProgressBar pbTop;
    ImageView imbBtnQuestionImg, imgBtnAnsImage1,imgBtnAnsImage2,imgBtnAnsImage3,imgBtnAnsImage4;
    Button  btnAudioSlow1, btnAudioSlow2;
    PlayPauseView btnAudio1, btnAudio2;
    int[] imageViewIds;
    String[] imagePaths;
    CommonFunction cf;
    ReferencePopup referencePopup;
    int quesNo, totalQues;
    //MediaPlayer cf.mediaPlayer;

    public static QP2Fragment newInstance(int questionNo, int totalQuestions, String data) {
        QP2Fragment fragment = new QP2Fragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Tags.TAG_QUESTION_NO,questionNo);
        bundle.putInt(Tags.TAG_QUESTIONS_TOTAL,totalQuestions);
        bundle.putString(Tags.TAG_QUESTION_TYPE, data);
        fragment.setArguments(bundle);

        return fragment;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_question_type_qp2, container, false);
        initializeView(view);

        return view;
    }
    private void initializeView(View view) {

        imgBtnHome = view.findViewById(R.id.imgBtnHome);
        imgBtnHelp = view.findViewById(R.id.imgBtnHelp);
        pbTop = view.findViewById(R.id.pbTop);
        tvQuestionNo = view.findViewById(R.id.tvQuestionNo);
        tvQuestionName = view.findViewById(R.id.tvQuestionName);
        btnAudio1 = view.findViewById(R.id.btnAudio1);
        btnAudio2 = view.findViewById(R.id.btnAudio2);
        btnAudioSlow1 = view.findViewById(R.id.btnAudioSlow1);
        btnAudioSlow2 = view.findViewById(R.id.btnAudioSlow2);
        imbBtnQuestionImg =view.findViewById(R.id.imbBtnQuestionImg);
        imgBtnAnsImage1 = view.findViewById(R.id.imgBtnAnsImage1);
        imgBtnAnsImage2 = view.findViewById(R.id.imgBtnAnsImage2);
        imgBtnAnsImage3 = view.findViewById(R.id.imgBtnAnsImage3);
        imgBtnAnsImage4 = view.findViewById(R.id.imgBtnAnsImage4);
        imgBtnHome.setOnClickListener(this::onClick);
        imgBtnHelp.setOnClickListener(this::onClick);
        btnAudio1.setOnClickListener(this::onClick);
        btnAudio2.setOnClickListener(this::onClick);
        btnAudioSlow1.setOnClickListener(this::onClick);
        btnAudioSlow2.setOnClickListener(this::onClick);
        imgBtnAnsImage1.setOnClickListener(this::onClick);
        imgBtnAnsImage2.setOnClickListener(this::onClick);
        imgBtnAnsImage3.setOnClickListener(this::onClick);
        imgBtnAnsImage4.setOnClickListener(this::onClick);
        setClickableButton(false);
    }
    private void setClickableButton(boolean clickable) {
        btnAudio1.setClickable(clickable);
        btnAudio2.setClickable(clickable);
        btnAudioSlow1.setClickable(clickable);
        btnAudioSlow2.setClickable(clickable);
        imgBtnAnsImage1.setClickable(clickable);
        imgBtnAnsImage2.setClickable(clickable);
        imgBtnAnsImage3.setClickable(clickable);
        imgBtnAnsImage4.setClickable(clickable);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(QP2ViewModel.class);
        // TODO: Use the ViewModel
        cf = new CommonFunction();
        //cf.fullScreen(getActivity().getWindow());
        audio = new Audio();

        quesNo = getArguments().getInt(Tags.TAG_QUESTION_NO);
        totalQues = getArguments().getInt(Tags.TAG_QUESTIONS_TOTAL);
        setTopBarState(quesNo, totalQues);
        Gson g = new Gson();
        question = g.fromJson(getArguments().getString(Tags.TAG_QUESTION_TYPE), Question.class);
        //PlayAudios(question);
        if(question.getReference() == null)
            imgBtnHelp.setVisibility(View.INVISIBLE);
        else
            referencePopup = new ReferencePopup( question.getReference());
        cf.setImage(getActivity(),QuestionsActivity.strFilePath + question.getQtypeImagePath(),imbBtnQuestionImg);
        imageViewIds = new int[]{R.id.imgBtnAnsImage1, R.id.imgBtnAnsImage2, R.id.imgBtnAnsImage3, R.id.imgBtnAnsImage4};
        imagePaths = new String[]{QuestionsActivity.strFilePath + question.getRightImagePath(), QuestionsActivity.strFilePath + question.getWrongImagePath1(), QuestionsActivity.strFilePath + question.getWrongImagePath2(), QuestionsActivity.strFilePath + question.getWrongImagePath3()};
        cf.setShuffleImages(getActivity(), imageViewIds, imagePaths, getView());
        Log.d("data qp1 " ,new GsonBuilder().setPrettyPrinting().create().toJson(question));
    }
    private void setTopBarState(int quesNo, int totalQues) {
        tvQuestionNo.setText(quesNo + "/" + totalQues);
        int percentage = cf.percent(quesNo, totalQues);
        Log.d("percentage", String.valueOf(percentage));
        pbTop.setProgress(cf.percent(quesNo, totalQues));
    }
    private void PlayAudios(Question question) {
        try {
            tvQuestionName.setText(question.getWord());
            cf.shakeAnimation(imbBtnQuestionImg, 1000);
            btnAudio1.setState(PlayPauseView.STATE_PLAY);
            cf.mediaPlayer = new MediaPlayer();
            cf.mediaPlayer.setDataSource(QuestionsActivity.strFilePath + question.getAudioPath());
            cf.mediaPlayer.prepare();
            cf.mediaPlayer.start();
            cf.mediaPlayer.setOnCompletionListener(mp -> {
                mp.stop();
                mp.release();
                btnAudio1.setState(PlayPauseView.STATE_PAUSE);
                btnAudio1.setImageDrawable(getActivity().getDrawable(R.drawable.anim_vector_play));
                btnAudio2.setState(PlayPauseView.STATE_PLAY);
                cf.mediaPlayer = new MediaPlayer();
                try {
                    cf.mediaPlayer.setDataSource(QuestionsActivity.strFilePath + question.getAnsAudioPath());
                    cf.mediaPlayer.prepare();
                    cf.mediaPlayer.start();
                    cf.mediaPlayer.setOnCompletionListener(mp1 -> {
                        mp1.stop();
                        mp1.release();
                        btnAudio2.setState(PlayPauseView.STATE_PAUSE);
                        btnAudio2.setImageDrawable(getActivity().getDrawable(R.drawable.anim_vector_play));
                        setClickableButton(true);
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("qfragment error",e.getMessage());
                }

            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        PlayAudios(question);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBtnHome:
                cf.onClickHomeButton(getView(),getActivity(),getArguments().getInt(Tags.TAG_QUESTION_NO));
                break;
            case R.id.imgBtnHelp:
                if(question.getReference() != null) {
                    referencePopup.showPopupWindow(getView());
                }
                break;
            case R.id.imgBtnNext:
                break;
            case R.id.btnAudio1:
                audio.playAudioFileAnim(getActivity(),QuestionsActivity.strFilePath + question.getAudioPath(),btnAudio1);
                tvQuestionName.setText(question.getWord());
                cf.shakeAnimation(imbBtnQuestionImg, 1000);
                break;
            case R.id.btnAudio2:
                audio.playAudioFileAnim(getActivity(),QuestionsActivity.strFilePath + question.getAnsAudioPath(),btnAudio2);
                break;
            case R.id.btnAudioSlow1:
                audio.playAudioSlow(getActivity(),QuestionsActivity.strFilePath + question.getAudioPath());
                tvQuestionName.setText(question.getWord());
                break;
            case R.id.btnAudioSlow2:
                audio.playAudioSlow(getActivity(),QuestionsActivity.strFilePath + question.getAnsAudioPath());
                break;
            case R.id.imgBtnAnsImage1:
                cf.checkQuestion(imgBtnAnsImage1.getTag().toString(),quesNo,totalQues,getView(),getActivity(),imageViewIds,imagePaths,question,audio,btnAudio1);

                break;
            case R.id.imgBtnAnsImage2:
                cf.checkQuestion(imgBtnAnsImage2.getTag().toString(),quesNo,totalQues,getView(),getActivity(),imageViewIds,imagePaths,question,audio,btnAudio1);
                break;
            case R.id.imgBtnAnsImage3:
                cf.checkQuestion(imgBtnAnsImage3.getTag().toString(),quesNo,totalQues,getView(),getActivity(),imageViewIds,imagePaths,question,audio,btnAudio1);
                break;
            case R.id.imgBtnAnsImage4:
                cf.checkQuestion(imgBtnAnsImage4.getTag().toString(),quesNo,totalQues,getView(),getActivity(),imageViewIds,imagePaths,question,audio,btnAudio1);
                break;
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}