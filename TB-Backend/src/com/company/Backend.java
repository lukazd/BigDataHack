package com.company;

import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneCategory;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneScore;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;
import java.util.HashMap;
import java.util.List;

public class Backend {

    static int tweetcount = 2;

    static HashMap<String,Double> Hsums;
    static HashMap<String,Integer> Hcounts;

    static HashMap<String, Double> Tsums = new HashMap<>();
    static HashMap<String,Integer> Tcounts = new HashMap<>();

    static ToneAnalyzer service;

    public static void main(String[] args) throws TwitterException {

        ConfigurationBuilder cb = new ConfigurationBuilder();

        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("Ovr3EoOFw1Ppu73Zk63PNk59u")
                .setOAuthConsumerSecret("CKjdghqEB3gtK1ZbBJqmu8uiPYjZk654YYeeXTLtO2Z49lnkZ1")
                .setOAuthAccessToken("138859220-frq4DrOGb9Fsyw02Dh0iDZO3r4fHbRJ2mq3kUm3V")
                .setOAuthAccessTokenSecret("PQ9qGeX0mS1EJiaWp1alpqiIHX9zpfIDTZC765jUJ0Fiw");

        TwitterFactory tf = new TwitterFactory(cb.build());
        twitter4j.Twitter twitter = tf.getInstance();

        service = new ToneAnalyzer(ToneAnalyzer.VERSION_DATE_2016_05_19);
        service.setEndPoint("https://gateway.watsonplatform.net/tone-analyzer/api");
        service.setUsernameAndPassword("4f41c873-5cd6-46ac-a159-becf87a5687f", "8uGOobS4Tozq");
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

        calcSumsAndAves(tweets, Tsums, Tcounts);
        calcSumsAndAves(tweets, Hsums, Hcounts);

        HashMap<String,Double> Tavgs = new HashMap<>();
        Tsums.forEach((k,v)->{
            Double a = v/tweetcount;
            Tavgs.put(k,a);
        });
        System.out.println("Average values:");
        Tavgs.forEach((k,v)->{
            System.out.println(k + ": " + Double.toString(v));
        });
        System.out.println("Counts:");
        Tcounts.forEach((k,v)->{
            System.out.println(k + ": " + Double.toString(v));
        });

        HashMap<String,Double> Havgs = new HashMap<>();
        Hsums.forEach((k,v)->{
            Double a = v/tweetcount;
            Havgs.put(k,a);
        });
        System.out.println("Average values:");
        Havgs.forEach((k,v)->{
            System.out.println(k + ": " + Double.toString(v));
        });
        System.out.println("Counts:");
        Hcounts.forEach((k,v)->{
            System.out.println(k + ": " + Double.toString(v));
        });

    }

    static void calcSumsAndAves(List<Status> tweets, HashMap<String,Double> sums, HashMap<String,Integer> counts){
        for (Status tweet : tweets) {
            String text = tweet.getText();
            System.out.println("@" + tweet.getUser().getScreenName() + " - " + text);
            ToneAnalysis tone = service.getTone(text, null).execute();

            for (ToneCategory tc : tone.getDocumentTone().getTones()) {
                for (ToneScore ts : tc.getTones()) {
                    String n = ts.getName();
                    Double s = ts.getScore();
                    Double current = sums.get(n);

                    if (current == null) {
                        sums.put(n, s);
                    } else {
                        sums.put(n, s+current);
                    }

                    Integer cur = counts.get(n);
                    if (cur == null) {
                        counts.put(n,0);
                        cur = 0;
                    }
                    if (s>.2) {
                        counts.put(n,cur+1);
                    }
                }
            }
        }
    }

}
