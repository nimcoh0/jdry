package org.softauto.espl1;


public class ExpressionParser {

    String expression;

    String before;

    String after;

    String exp;



    public String getBefore() {
        return before;
    }

    public String getAfter() {
        return after;
    }

    public String getExp() {
        return exp;
    }

    public ExpressionParser(String expression){
        this.expression = expression;
    }

    private Integer getBracketsCloseIndex(String s){
        int open = 1;
        int close = 0;
        //for(Integer index : s.chars().toArray().length){
        for(int i=0;i< s.chars().toArray().length;i++){
            String letter = Character.toString(s.charAt(i));
            if(letter.equals("{")){
                open++;
            }
            if(letter.equals("}")){
                close++;
            }
            if(open == close){
                return i;
            }
        }
        return null;
    }

    private String parseBefore(String str,String tag){
       int tagIndex =  str.indexOf(tag);
       return str.substring(0,tagIndex);
    }

    private String parseAfter(String str,int closeIndex){
       return str.substring(closeIndex+1,str.length());
    }


    public ExpressionParser parse(String tag){
        if(expression != null && expression.contains(tag)) {
            String s = expression.substring(expression.indexOf(tag), expression.length());
            s = s.replace(tag,"");
            s = s.substring(1,s.length());
            int closeIndex = getBracketsCloseIndex(s);
            exp =  s.substring(0,closeIndex);
            before = parseBefore(expression,tag);
            after = parseAfter(s,closeIndex);

        }
        return this;
    }




    public String getMethodName(){
        int i = expression.indexOf("#");
        return expression.substring(i+1,expression.indexOf("{"));
    }

}
