package org.softauto.core;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

public  class BracketsUtils {

    private LinkedList<Integer> stuck = new LinkedList<>();
    private LinkedHashMap<Integer,Integer> groups = new LinkedHashMap<>();

    String openBracketTag;

    String closeBracketTag;

    String expression;



    public BracketsUtils setExpression(String expression) {
        this.expression = expression;
        return this;
    }

    public BracketsUtils setOpenBracketTag(String openBracketTag) {
        this.openBracketTag = openBracketTag;
        return this;
    }

    public BracketsUtils setCloseBracketTag(String closeBracketTag) {
        this.closeBracketTag = closeBracketTag;
        return this;
    }

    public BracketsUtils analyze(){
        try {
            if(expression.contains(openBracketTag)) {
                char[] charArray = expression.toCharArray();
                for (int i = 0; i < charArray.length - (closeBracketTag.length()); i++) {
                    int openTag = CharSequence.compare(expression.subSequence(i, i + openBracketTag.length()), openBracketTag.subSequence(0, openBracketTag.length()));
                    if (openTag == 0) {
                        stuck.push(i);
                    }
                    int closeTag = CharSequence.compare(expression.subSequence(i, i + closeBracketTag.length()), closeBracketTag.subSequence(0, closeBracketTag.length()));
                    if (closeTag == 0) {
                        Integer start = stuck.pop();
                        groups.put(start, i + 1);
                    }
                }
                if( charArray[charArray.length-1] == closeBracketTag.charAt(0)){
                    Integer start = stuck.pop();
                    groups.put(start, charArray.length );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public Integer getNumberOfGroups(){
        return groups.size();
    }

    public Integer getGroupNumber(String exp){
        int j=0;
        for(Map.Entry entry : groups.entrySet()){
            if(expression.subSequence(Integer.valueOf(entry.getKey().toString()),Integer.valueOf(entry.getValue().toString())).equals(exp)){
                return j;
            }
            j++;
        }
        return null;
    }

    public Integer getGroupNumber(Map.Entry group){
        int j=0;
        for(Map.Entry entry : groups.entrySet()){
            if(Integer.valueOf(entry.getKey().toString()).equals(Integer.valueOf(group.getKey().toString())) &&
                    Integer.valueOf(entry.getValue().toString()).equals(Integer.valueOf(group.getValue().toString()))){
                return j;
            }
            j++;
        }
        return null;
    }

    public LinkedHashMap<Integer, Integer> getGroups() {
        return groups;
    }

    public Map.Entry getGroup(int start,int end){
        for(Map.Entry entry : groups.entrySet()){
            if(entry.getKey().equals(start) && entry.getValue().equals(end)){
                return entry;
            }
        }
        return null;
    }

    public Map.Entry getGroup(int i){
        int j = 0;
        for(Map.Entry entry : groups.entrySet()){
            if(j == i){
                return entry;
            }
            j++;
        }
        return null;
    }

    public String removeGroupBracket(String expression,int group){
        Map.Entry entry = getGroup(group);
        String s = (String) expression.subSequence(Integer.valueOf(entry.getKey().toString()),Integer.valueOf(entry.getValue().toString()));
        String noBrackets = s.substring(openBracketTag.length(),s.length()-closeBracketTag.length());
        expression.replace(s,noBrackets);
        return expression;
    }

    public String removeBracket(String expression){
        String noBrackets = expression.substring(openBracketTag.length(),expression.length()-closeBracketTag.length());
        expression = expression.replace(expression,noBrackets);
        return expression;
    }

    public String getBracketVar(String expression,int group){
        Map.Entry entry = getGroup(group);
        String s = (String) expression.subSequence(Integer.valueOf(entry.getKey().toString()),Integer.valueOf(entry.getValue().toString()));
        String noBrackets = s.substring(openBracketTag.length(),s.length()-closeBracketTag.length());
        return noBrackets;
    }

    public String getGroupLiteralText(Map.Entry group){
       int groupNumber = getGroupNumber(group);
       String groupLiteralText = null;
       if(groups.size() >= groupNumber+1) {
           Map.Entry next = getGroup(groupNumber + 1);
           if(next != null) {
               groupLiteralText = expression.substring(Integer.valueOf(group.getValue().toString()), Integer.valueOf(next.getKey().toString()));
           }else {
               groupLiteralText =  expression.substring(Integer.valueOf(group.getValue().toString()),expression.toString().length());
           }
       }else {
           groupLiteralText =  expression.substring(Integer.valueOf(group.getValue().toString()),expression.toString().length());
       }
      if(groupLiteralText.endsWith("(") && groupLiteralText.contains(".")){
          groupLiteralText = groupLiteralText.substring(0,groupLiteralText.lastIndexOf("."));
      }
      return groupLiteralText;
    }


}
