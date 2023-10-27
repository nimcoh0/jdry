package org.softauto.analyzer.core.system.context;

import java.util.HashMap;

public class ItemContext {

    private HashMap<String,Object> ctx = new HashMap<>();

    public ItemContext setCtx(HashMap<String,Object> ctx){
        this.ctx = ctx;
        return this;
    }

    public ItemContext addCtx(String key,Object value){
        this.ctx.put(key,value);
        return this;
    }

    public ItemContext addCtx(Object value){
        this.ctx.put(value.getClass().getTypeName(),value);
        return this;
    }

    public HashMap<String,Object> getCtx(){
        return ctx;
    }

    public Object getCtx(String name){
        return ctx.get(name);
    }
}
