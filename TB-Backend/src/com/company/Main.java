package com.company;

import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;
import com.mashape.unirest.http.exceptions.UnirestException;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.util.List;

public class Main {

    public static void main(String[] args) throws TwitterException, UnirestException {

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

        try {
            Query query = new Query("americafirst");
            query.count(1);
            QueryResult  result;
            result = twitter.search(query);
            List<Status> tweets = result.getTweets();
            int tweetcount = 0;
            //Trump Numbers
            List<Double> TAnger;
            List<Double> TDis;
            List<Double> TFear;
            List<Double> TJoy;
            List<Double> TSad;
            List<Double> TAnalyit;
            List<Double> TConf;
            List<Double> TTent;
            List<Double> TOpen;
            List<Double> TConc;
            List<Double> TExt;
            List<Double> TAgree;
            List<Double> TEmo;
            //Hillary Numbers
            List<Double> HAnger;
            List<Double> HDis;
            List<Double> HFear;
            List<Double> HJoy;
            List<Double> HSad;
            List<Double> HAnalyit;
            List<Double> HConf;
            List<Double> HTent;
            List<Double> HOpen;
            List<Double> HConc;
            List<Double> HExt;
            List<Double> HAgree;
            List<Double> HEmo;

            for (Status tweet : tweets) {
                String text = tweet.getText();
                System.out.println("@" + tweet.getUser().getScreenName() + " - " + text);
                ToneAnalysis tone = service.getTone(text, null).execute();
                System.out.println(tone);
                tweetcount++;
                //TODO: set up data to send to the front end
                //
            }
            System.out.println("Number of tweets: " + tweetcount);

            System.exit(0);
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to search tweets: " + te.getMessage());
            System.exit(-1);
        }



    }
}
