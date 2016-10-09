package com.company;

import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneCategory;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneScore;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.util.HashMap;
import java.util.List;

public class Main {


    public static void main(String[] args) throws TwitterException, {

        ConfigurationBuilder cb = new ConfigurationBuilder();

        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("Ovr3EoOFw1Ppu73Zk63PNk59u")
                .setOAuthConsumerSecret("CKjdghqEB3gtK1ZbBJqmu8uiPYjZk654YYeeXTLtO2Z49lnkZ1")
                .setOAuthAccessToken("138859220-frq4DrOGb9Fsyw02Dh0iDZO3r4fHbRJ2mq3kUm3V")
                .setOAuthAccessTokenSecret("PQ9qGeX0mS1EJiaWp1alpqiIHX9zpfIDTZC765jUJ0Fiw");

        TwitterFactory tf = new TwitterFactory(cb.build());
        twitter4j.Twitter twitter = tf.getInstance();

        ToneAnalyzer service = new ToneAnalyzer(ToneAnalyzer.VERSION_DATE_2016_05_19);
        service.setEndPoint("https://gateway.watsonplatform.net/tone-analyzer/api");
        service.setUsernameAndPassword("4f41c873-5cd6-46ac-a159-becf87a5687f", "8uGOobS4Tozq");
        int tweetcount = 1;
        List<Status> tweets = null;

        try {
            Query query = new Query("americafirst");
            query.count(tweetcount);
            QueryResult  result;
            result = twitter.search(query);
            tweets = result.getTweets();

        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to search tweets: " + te.getMessage());
            System.exit(-1);
        }


        HashMap<String,Double> Hsums;
        HashMap<String,Integer> Hcounts;

        HashMap<String, Double> Tsums = new HashMap<>();
        HashMap<String,Integer> Tcounts = new HashMap<>();
        for (Status tweet : tweets) {
            String text = tweet.getText();
            System.out.println("@" + tweet.getUser().getScreenName() + " - " + text);
            ToneAnalysis tone = service.getTone(text, null).execute();

            for (ToneCategory tc : tone.getDocumentTone().getTones()) {
                for (ToneScore ts : tc.getTones()) {
                    String n = ts.getName();
                    Double s = ts.getScore();
                    Double current = Tsums.get(n);

                    if (current == null) {
                        Tsums.put(n, s);
                    } else {
                        Tsums.put(n, s+current);
                    }

                    Integer cur = Tcounts.get(n);
                    if (cur == null) {
                        Tcounts.put(n,0);
                        cur = 0;
                    }
                    if (s>.2) {
                        Tcounts.put(n,cur+1);
                    }
                }
            }
        }


    }
}
