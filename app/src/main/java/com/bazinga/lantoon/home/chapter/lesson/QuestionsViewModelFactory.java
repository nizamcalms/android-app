package com.bazinga.lantoon.home.chapter.lesson;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import org.jetbrains.annotations.NotNull;

public class QuestionsViewModelFactory implements ViewModelProvider.Factory {
    int langid, chaperno, lessonno;

    public QuestionsViewModelFactory(int langid, int chaperno, int lessonno) {
        this.langid = langid;
        this.chaperno = chaperno;
        this.lessonno = lessonno;
    }

    @NonNull
    @NotNull
    @Override
    public <T extends ViewModel> T create(@NonNull @NotNull Class<T> modelClass) {
        return (T) new QuestionsViewModel(langid,chaperno,lessonno);
    }
}