package org.softauto.analyzer.core.system.nlp;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import java.io.InputStream;

public class Nlp {

    private final static String resource = "org/softauto/analyzer/core/system/nlp/lib/en-pos-maxent.bin";

    private static Nlp nlp = null;

    POSTaggerME posTagger;

    public static Nlp getInstance(){
        if(nlp == null){
            nlp = new Nlp();
        }
        return nlp;
    }



    private Nlp(){
        try {
            InputStream posModelIn = getClass().getClassLoader().getResourceAsStream("en-pos-maxent.bin");
            POSModel posModel = new POSModel(posModelIn);
            posTagger = new POSTaggerME(posModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String[] tokens;

    private String tags[];

    public String[] getTokens() {
        return tokens;
    }

    public String[] getTags() {
        return tags;
    }

    public Nlp setTokens(String[] tokens) {
        this.tokens = tokens;
        return this;
    }

    public Nlp build(){
        try {
            //InputStream targetStream = new ByteArrayInputStream(cachedBody);
            // loading the parts-of-speech model from stream
            //POSModel posModel = new POSModel(targetStream);
            // initializing the parts-of-speech tagger with model
            //POSTaggerME posTagger = new POSTaggerME(posModel);
            // Tagger tagging the tokens
            tags = posTagger.tag(tokens);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }
}
