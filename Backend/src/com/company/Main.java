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

    static int tweetcount = 2;
    static int secondsDelay = 30;

    static HashMap<String, Double> Hsums;
    static HashMap<String, Integer> Hcounts;
    static HashMap<String, Double> Havgs;

    static HashMap<String, Double> Tsums;
    static HashMap<String, Integer> Tcounts;

    static HashMap<String, Double> Tavgs;
    static twitter4j.Twitter twitter;
    static ToneAnalyzer service;
    static List<Status> tweets;
    static Status angriest_tweet;

    static int iterations;
    static int runavg_Tanger;

    static void setTweets(String q) {
        try {
            Query query = new Query(q);
            query.count(tweetcount);
            QueryResult result;
            result = twitter.search(query);
            tweets = result.getTweets();

        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to search tweets: " + te.getMessage());
            System.exit(-1);
        }
    }

    static void calcSumsAndAves(List<Status> tweets, HashMap<String, Double> sums, HashMap<String, Integer> counts) {
        Double max_anger = 0.0;
        Double oa = sums.get("Anger");
        Double old_anger = (oa == null) ? 0.0 : oa;

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
                        sums.put(n, s + current);
                    }

                    Integer cur = counts.get(n);
                    if (cur == null) {
                        counts.put(n, 0);
                        cur = 0;
                    }
                    if (s > .2) {
                        counts.put(n, cur + 1);
                    }
                }
            }
            Double new_anger = sums.get("Anger");
            Double d = new_anger - old_anger;
            if (d > max_anger) {
                max_anger = d;
                angriest_tweet = tweet;
            }

        }
    }

    static void updateWordCounts(HashMap<String,Integer> dict) {
        for (Status t : tweets) {
            String text = t.getText();
            text = text.toUpperCase();

        }
    }

    public static void main(String[] args) throws TwitterException {

        ConfigurationBuilder cb = new ConfigurationBuilder();

        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("Ovr3EoOFw1Ppu73Zk63PNk59u")
                .setOAuthConsumerSecret("CKjdghqEB3gtK1ZbBJqmu8uiPYjZk654YYeeXTLtO2Z49lnkZ1")
                .setOAuthAccessToken("138859220-frq4DrOGb9Fsyw02Dh0iDZO3r4fHbRJ2mq3kUm3V")
                .setOAuthAccessTokenSecret("PQ9qGeX0mS1EJiaWp1alpqiIHX9zpfIDTZC765jUJ0Fiw");

        TwitterFactory tf = new TwitterFactory(cb.build());
        twitter = tf.getInstance();

        service = new ToneAnalyzer(ToneAnalyzer.VERSION_DATE_2016_05_19);
        service.setEndPoint("https://gateway.watsonplatform.net/tone-analyzer/api");
        service.setUsernameAndPassword("4f41c873-5cd6-46ac-a159-becf87a5687f", "8uGOobS4Tozq");
        tweets = null;

        String Hquery = "imwithher";
        String Tquery = "americafirst";

        // LOOP BEGINS HERE
        iterations = 0;
        while (true) {
            iterations++;

            Tsums = new HashMap<>();
            Tcounts = new HashMap<>();
            Hsums = new HashMap<>();
            Hcounts = new HashMap<>();

            setTweets(Tquery);
            calcSumsAndAves(tweets, Tsums, Tcounts);
            setTweets(Hquery);
            calcSumsAndAves(tweets, Hsums, Hcounts);

            Tavgs = new HashMap<>();
            Tsums.forEach((k, v) -> {
                Double a = v / tweetcount;
                Tavgs.put(k, a);
            });

            System.out.println("Trump:");
            System.out.println("Average values:");
            Tavgs.forEach((k, v) -> {
                System.out.println(k + ": " + Double.toString(v));
            });
            System.out.println("Counts:");
            Tcounts.forEach((k, v) -> {
                System.out.println(k + ": " + Double.toString(v));
            });

            Havgs = new HashMap<>();
            Hsums.forEach((k, v) -> {
                Double a = v / tweetcount;
                Havgs.put(k, a);
            });

            System.out.println("Clinton:");
            System.out.println("Average values:");
            Havgs.forEach((k, v) -> {
                System.out.println(k + ": " + Double.toString(v));
            });
            System.out.println("Counts:");
            Hcounts.forEach((k, v) -> {
                System.out.println(k + ": " + Double.toString(v));
            });

            PushData pushData = new PushData();

            int totalangry = Tcounts.get("Anger") + Hcounts.get("Anger");
            if(totalangry != 0) {
                int hillaryangry = Hcounts.get("Anger") / totalangry;
                int trumpangry = Tcounts.get("Anger") / totalangry;
                System.out.println(hillaryangry + " : " + trumpangry);
                pushData.updateAngryTweets(hillaryangry, trumpangry);
            } else {
                System.out.println("No angry tweets");
            }

            pushData.updateAngriestTweet(angriest_tweet.getUser().toString(),angriest_tweet.getText());

            HashMap<String, Object> HSocial = new HashMap<>();
            HSocial.put("Agreeableness", Hcounts.get("Agreeableness"));
            HSocial.put("Conscientiousness", Hcounts.get("Conscientiousness"));
            HSocial.put("Emotional Range", Hcounts.get("Emotional Range"));
            HSocial.put("Extraversion", Hcounts.get("Extraversion"));
            HSocial.put("Openness", Hcounts.get("Openness"));
            pushData.updateSocialTendencies(PushData.Candidate.HILLARY, HSocial);

            HashMap<String, Object> TSocial = new HashMap<>();
            TSocial.put("Agreeableness", Tcounts.get("Agreeableness"));
            TSocial.put("Conscientiousness", Tcounts.get("Conscientiousness"));
            TSocial.put("Emotional Range", Tcounts.get("Emotional Range"));
            TSocial.put("Extraversion", Tcounts.get("Extraversion"));
            TSocial.put("Openness", Tcounts.get("Openness"));
            pushData.updateSocialTendencies(PushData.Candidate.TRUMP, TSocial);

            if (iterations > 4) {
                pushData.destroyConnection();
                System.exit(0);
            }

            try {
                Thread.sleep(secondsDelay*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }
    }
}
