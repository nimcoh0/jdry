package org.softauto.analyzer.directivs.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;
import org.softauto.analyzer.directivs.argument.Argument;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Request implements Cloneable, Serializable {


    List<Argument> arguments = new ArrayList<>();


    public List<Argument> getArguments() {
        return arguments;
    }

    public Request setArguments(List<Argument> arguments) {
        this.arguments = arguments;
        return this;
    }

    public Request addArgument(Argument argument) {
        this.arguments.add(argument);
        return this;
    }

    @JsonIgnore
    public String getAsArrayTypesString(){
        if(arguments!= null) {
            List<String> types = new ArrayList<>();
            for (Argument argument : arguments) {
                types.add(argument.getType() + ".class");
            }

            String joinedString = StringUtils.join(types, ",");
            return "new Class[]{" + joinedString + "}";
        }
        return "new Class[]{}";
    }

    @JsonIgnore
    public String getNamesAsArrayString(){
        if(arguments != null) {
            List<String> names = new ArrayList<>();
            for (int i = 0; i < arguments.size(); i++) {
                String name = null;
                if(arguments.get(i).getName() != null ){
                    name = arguments.get(i).getName();
                }else {
                    name = "arg" + i;
                }
                names.add(name);
            }
            String joinedString = StringUtils.join(names, ",");
            return "new Object[]{" + joinedString + "}";
        }
        return "new Object[]{}";
    }

}
