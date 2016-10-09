package com.company;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.Objects;
import java.util.concurrent.atomic.*;

public class PushData {
    public enum Candidate {
       HILLARY("Hillary"),
       TRUMP("Trump");

        private final String text;

        Candidate(final String text){
            this.text = text;
        }
        @Override
        public String toString(){
            return text;
        }
    }
    private static Firebase fb = new Firebase("https://twitterbicker.firebaseio.com/");

//    public static void main(String [] args) {
//
//        PushData pushData = new PushData();
//        HashMap data = new HashMap<String, Object>();
//        data.put("Agreeableness", 30);
//        data.put("Conscientiousness", 20);
//        data.put("Emotional Range", 9);
//        data.put("Extraversion", 0);
//        data.put("Openness", 14);
//        pushData.updateMostUsedWords(data);
//        //pushData.updateAngryTweets(40, 60);
//        while(true){}
//        // pushData.destroyConnection();
//    }
    public void updateSocialTendencies(Candidate candidate, Map<String, Object> socialTendencies){
        fb.child(candidate.toString()).updateChildren(socialTendencies);
    }
    public void updateAngryTweets(int hillaryValue, int trumpValue){
        HashMap<String, Object> angerScore = new HashMap<String, Object>();
        angerScore.put(Candidate.HILLARY.toString(), hillaryValue);
        angerScore.put(Candidate.TRUMP.toString(), trumpValue);
        fb.child("AngerScore").updateChildren(angerScore);
    }
    public void updateAngriestTweet(String username, String tweet) {
        HashMap<String, Object> angriestTweet = new HashMap<String, Object>();
        angriestTweet.put("User", username);
        angriestTweet.put("Tweet", tweet);
        fb.child("AngriestTweet").updateChildren(angriestTweet);
    }
    public void updateMostUsedWords(HashMap<String, Object> wordFrequency) {
        fb.child("WordFrequency").setValue(wordFrequency);
    }
    public void destroyConnection(){
        fb.goOffline();
    }
}

