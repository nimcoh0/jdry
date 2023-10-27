package org.softauto.analyzer.core.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Csv {


    private String path;

    private List<String> header = new ArrayList<>();

    private List<String> lines = new ArrayList<>();

    public Csv(String path){
        this.path = path;
    }

    public Csv read(){
        try {
            String line = "";
            BufferedReader br = new BufferedReader(new FileReader(path));
            String[] header = br.readLine().split(",");
            setHeader(Arrays.asList(header));
            while ((line = br.readLine()) != null)   //returns a Boolean value
            {
                addLine(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    public static Csv setPath(String path){
        return new Csv(path);
    }


    private List<String> getHeader() {
        return header;
    }

    private void setHeader(List<String> header) {
        this.header = header;
    }

    private List<String> getLines() {
        return lines;
    }

    private void setLines(List<String> lines) {
        this.lines = lines;
    }

    private void addLine(String line) {
        this.lines.add(line);
    }

    public HashMap<String,List<String>> parse(){
        HashMap<String,List<String>> content = new HashMap<>();
        for(int i=0;i<header.size();i++){
            List<String> valueList = new ArrayList<>();
            for(int j=0;j<lines.size();j++) {
                String[] values = lines.get(j).split(",");
                valueList.add(values[i]);
            }
            content.put(header.get(i),valueList);
        }
        return content;
    }

}
