package com.example.grocerylisting.CustomView;

import com.example.grocerylisting.Tflite.Classifier;

import java.util.List;

public interface ResultsView {
    public void setResults(final List<Classifier.Recognition> results);
}
