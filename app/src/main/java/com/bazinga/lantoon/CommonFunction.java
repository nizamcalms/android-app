package com.bazinga.lantoon;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bazinga.lantoon.home.chapter.lesson.LessonCompletedPopup;
import com.bazinga.lantoon.home.chapter.lesson.QuestionRightWrongPopup;
import com.bazinga.lantoon.home.chapter.lesson.QuestionsActivity;
import com.bazinga.lantoon.home.chapter.lesson.model.PostLessonResponse;
import com.bazinga.lantoon.retrofit.ApiClient;
import com.bazinga.lantoon.retrofit.ApiInterface;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommonFunction {
    Context context;

  /*  public void CommonFunction(Context context) {
        this.context = context;
    }*/
    int attemptCount = 0;

    public void fullScreen(Window window) {
        if (Build.VERSION.SDK_INT < 16) {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = window.getDecorView();
            // Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
            // Remember that you should never show the action bar if the
        }
    }

    public int percent(double quesNo, double totalQues) {

        // percent
        double per = (quesNo / totalQues) * 100;
        ;

        Log.d("percentage", String.valueOf(Math.round(per)));
        return (int) per;
    }

    public void shakeAnimation(View view, int duration) {
        ObjectAnimator
                .ofFloat(view, "translationX", 0, 25, -25, 25, -25, 15, -15, 6, -6, 0)
                .setDuration(duration)
                .start();
    }

    public void setImage(Activity activity, String filePath, ImageView imageView) {
        /*File file = new File(folderPath);
        File[] files = file.listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String filename) {

                return filename.contains(".jpg");
            }
        });*/
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transform(new CenterCrop(), new RoundedCorners(30));
        Glide.with(activity).load(filePath).apply(requestOptions).into(imageView);

    }

    public void setShuffleImages(Activity activity, int[] imageViewIds, String[] imagePaths, View view) {
        /*File file = new File(wrongImgFolderPath);
        File[] wrongImageFile = file.listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String filename) {

                return filename.contains(".jpg");
            }
        });
        ArrayList<File> arrayList = new ArrayList<File>();
        for (int i = 0; i < wrongImageFile.length; i++) {
            arrayList.add(wrongImageFile[i]);
        }
        file = new File(rightImgPaths);
        File[] rightImageFile = file.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.contains(".jpg");
            }
        });
        arrayList.add(rightImageFile[0]);*/


        Random rng = new Random();
        List<Integer> generated = new ArrayList<Integer>();
        for (int i = 0; i < imageViewIds.length; i++) {
            while (true) {
                Integer next = rng.nextInt(imageViewIds.length);
                if (!generated.contains(next)) {
                    generated.add(next);
                    ImageView iv = view.findViewById(imageViewIds[i]);
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions = requestOptions.transform(new CenterCrop(), new RoundedCorners(30));
                    iv.setTag(imagePaths[next]);
                    Glide.with(activity).load(imagePaths[next]).apply(requestOptions).into(iv);
                    break;
                }
            }
        }

    }

    public void checkQuestion(String tag, int quesNo, int
            totalQues, View view, Activity activity, int[] imageViewIds, String[] imagePaths,int pMark, int nMark) {
        attemptCount++;
        QuestionRightWrongPopup qrwp = new QuestionRightWrongPopup();
        if (CheckAnswerImage(tag)) {
            if (quesNo == totalQues) {

                qrwp.showPopup(activity, view, CheckAnswerImage(tag), true,quesNo,attemptCount,false,pMark,nMark);

            } else {
                qrwp.showPopup(activity, view, CheckAnswerImage(tag), false, quesNo, attemptCount,false,pMark,nMark);
            }

        } else {
            qrwp.showPopup(activity, view, CheckAnswerImage(tag), false, quesNo, attemptCount,false,pMark,nMark);
            setShuffleImages(activity, imageViewIds, imagePaths, view);
        }
        System.out.println("attemptCount "+attemptCount);
    }

    public boolean CheckAnswerImage(String imagePath) {
        if (imagePath.contains("right"))
            return true;
        else return false;
    }

    public void speechToText(Context context, TextView textView, String answerWord, boolean isLastQuestion, View view, Activity activity,int quesNo,int pMark, int nMark) {

        SpeechRecognizer speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);

        //String languagePref = Locale.ENGLISH;
        final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, QuestionsActivity.strSpeakCode);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, QuestionsActivity.strSpeakCode);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, QuestionsActivity.strSpeakCode);

        /*final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.);
*/
        QuestionRightWrongPopup qrwp = new QuestionRightWrongPopup();
        speechRecognizer.startListening(speechRecognizerIntent);
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {
                textView.setText("");
                textView.setHint("Listening...");

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {
                textView.setHint("Checking...");
            }

            @Override
            public void onError(int i) {
                textView.setHint("Speak again");
            }

            @Override
            public void onResults(Bundle bundle) {
                //micButton.setImageResource(R.drawable.ic_mic_black_off);
                ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                String output = data.get(0).substring(0, 1).toUpperCase() + data.get(0).substring(1).toLowerCase();
                textView.setText(output);
                attemptCount++;
                if (answerWord.equals(data.get(0))) {

                    qrwp.showPopup(activity, view, true, isLastQuestion, quesNo, attemptCount,true,pMark,nMark);

                } else {
                    qrwp.showPopup(activity, view, false, false, quesNo, attemptCount,true,pMark,nMark);
                }
                Log.d("attemptCount",QuestionsActivity.countMap.toString());

                /*if(data.get(0).equals(answerWord)) {
                    Log.d("check ok", data.get(0) + " " + answerWord);

                }*/
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

        /*micBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    speechRecognizer.stopListening();
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    //micBtn.setImageResource(R.drawable.ic_mic_black_24dp);
                    speechRecognizer.startListening(speechRecognizerIntent);
                }
                return false;
            }
        });*/
    }
    public static void postLesson(View view, Activity activity) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<PostLessonResponse> call = apiInterface.scoreUpdate(QuestionsActivity.score);
        call.enqueue(new Callback<PostLessonResponse>() {
            @Override
            public void onResponse(Call<PostLessonResponse> call, Response<PostLessonResponse> response) {

                Log.d("response postLesson", new GsonBuilder().setPrettyPrinting().create().toJson(response.body()));

                LessonCompletedPopup lessonCompletedPopup = new LessonCompletedPopup();
                lessonCompletedPopup.showPopupWindow(view, activity,response.body());

            }

            @Override
            public void onFailure(Call<PostLessonResponse> call, Throwable t) {
                Log.e("response postLesson", t.getMessage());
            }
        });
    }
}
