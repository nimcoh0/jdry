package org.softauto.analyzer.core.system.nlp;

import org.apache.commons.lang3.StringUtils;

public class NameGenerator {

    private String name;

    private String result = "";

    String[] tokens;

    String[] tags;

    public String getResult() {
        return result;
    }

    public NameGenerator setName(String name) {
        this.name = name;
        return this;
    }

    public String getNoun(){
        String result = "";
        for(int i=0;i<tokens.length;i++){
            if(tags[i].contains("NN")){
                result = result+tokens[i];
            }
        }
        return result;
    }

    public String getFirstNoun(){
        String result = "";
        for(int i=0;i<tokens.length;i++){
            if(tags[i].contains("NN")){
                return result = result+tokens[i];
            }
        }
        return result;
    }

    public String getLastNoun(){
        String result = "";
        for(int i=0;i<tokens.length;i++){
            if(tags[i].contains("NN")){
                result = tokens[i];
            }
        }
        return result;
    }

    public NameGenerator build(){
        String[] sentence = StringUtils.splitByCharacterTypeCamelCase(name);
        Nlp nlp = Nlp.getInstance().setTokens(sentence).build();
        tokens = nlp.getTokens();
        tags = nlp.getTags();
        return this;
    }
}
