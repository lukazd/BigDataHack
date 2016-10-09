package com.company;

        import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;
        import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;
        import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneCategory;
        import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneScore;
        import twitter4j.*;
        import twitter4j.conf.ConfigurationBuilder;

        import java.util.Arrays;
        import java.util.HashMap;
        import java.util.List;

public class Main {

    static int tweetcount = 5;
    static int secondsDelay = 60;
    static int loopcount = 1;
    static int minutes_to_run = 5;

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

    static HashMap<String, Object> wordcounts;

    static int iterations;
    static int runavg_Tanger;
    static HashMap<String, Double> h_soc_avgs;
    static HashMap<String, Double> t_soc_avgs;

    static void setTweets(String q) {
        try {
            Query query = new Query(q);
            query.count(tweetcount);
            query.resultType(Query.ResultType.popular);
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

    static void updateWordCounts(HashMap<String,Object> dict) {
        for (Status t : tweets) {
            String text = t.getText();
            text = text.toUpperCase();
            String[] w = text.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");
            List<String> words = Arrays.asList(w);
            for (String word : words) {

                if (word.length() > 3) {
                    if (dict.containsKey(word)) {
                        Integer count = (Integer) dict.get(word);
                        dict.put(word, count + 1 );
                    } else {
                        dict.put(word, 1);
                    }
                }
            }
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

        String Hquery = "imwithher%20strongertogether%20-americafirst%20-neverhillary";
        String Tquery = "americafirst%20neverhillary%20-imwithher%20-strongertogether";

        iterations = 0;
        runavg_Tanger = 0;
        long start_time = System.currentTimeMillis();
        while (true) {

            Tsums = new HashMap<>();
            Tcounts = new HashMap<>();
            Hsums = new HashMap<>();
            Hcounts = new HashMap<>();
            wordcounts = new HashMap<>();

            setTweets(Tquery);
            calcSumsAndAves(tweets, Tsums, Tcounts);
            updateWordCounts(wordcounts);
            setTweets(Hquery);
            calcSumsAndAves(tweets, Hsums, Hcounts);
            updateWordCounts(wordcounts);

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

            double hanger = (double) Hcounts.get("Anger");
            double hdisgust = (double) Hcounts.get("Disgust");
            double h_angerdisgust = hanger + hdisgust;
            double tanger = (double) Tcounts.get("Anger");
            double tdisgust = (double) Tcounts.get("Disgust");
            double t_angerdisgust = tanger + tdisgust;
            Double totalangry = h_angerdisgust + t_angerdisgust;

            if(totalangry != 0) {
                double hillaryangry = h_angerdisgust / totalangry;
                double trumpangry = t_angerdisgust / totalangry;
                int percent_trump = (int) (trumpangry*100);
                int percent_hillary = (int) (hillaryangry*100);
                runavg_Tanger = ( (runavg_Tanger*iterations + percent_trump) / (iterations + 1) );
                System.out.println(hillaryangry + " : " + trumpangry);
                pushData.updateAngryTweets(percent_hillary, percent_trump);
            } else {
                pushData.updateAngryTweets(0,0);
                System.out.println("No angry tweets");
            }

            pushData.updateAngriestTweet(angriest_tweet.getUser().getScreenName(),angriest_tweet.getText());
            pushData.updateMostUsedWords(wordcounts);

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

            System.out.println("Wordcounts size:");
            System.out.println(wordcounts.size());


            try {
                Thread.sleep(secondsDelay*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.exit(-1);
            }

            iterations++;
            if (iterations >= loopcount || (System.currentTimeMillis()-start_time > minutes_to_run*60*1000)) {
                pushData.destroyConnection();
                System.exit(0);
            }

        }
    }
}
