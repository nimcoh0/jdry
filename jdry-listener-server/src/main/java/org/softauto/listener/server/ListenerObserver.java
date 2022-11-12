package org.softauto.listener.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * observer for listeners impl classes
 */
public class ListenerObserver {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(ListenerObserver.class);
    private  List<HashMap<String,Object>> channels = null;

    private static ListenerObserver listenerObserver = null;

    public static ListenerObserver getInstance(){
        if(listenerObserver == null){
            listenerObserver =  new ListenerObserver();
            //channels = new ArrayList<>();
        }
        return listenerObserver;
    }

    /**
     * clear the channels list
     */
    public void reset(){
        channels = new ArrayList<>();
    }

    private ListenerObserver(){
        channels = new ArrayList<>();
    }

    /**
     * add new listener impl class
     * @param key
     * @param value
     */
    public void register(String key,Object value){
        try {
                HashMap<String, Object> hm = new HashMap<>();
                hm.put(key, value);
                channels.add(hm);
                logger.debug("ListenerObserver register " + key);
        }catch (Exception e){
            logger.error("fail to register listener impl class "+ key);
        }
    }

    public void unRegister(String key){
        channels.forEach(i->{
          i.forEach((k,v)->{
            if(k.equals(key)){
                v = null;
                i.put(k,v);
                logger.debug("ListenerObserver unregister " + key);
            }
          });
        });
    }


    /**
     * get channel
     * @param key
     * @return
     */
    public Object getChannel(String key) {
        try {
            for (HashMap<String, Object> chanel : channels) {
                if (chanel.containsKey(key)) {
                    return chanel.get(key);
                }
            }
            logger.debug("observer channel for " + key + " fail ");
        }catch (Exception e){
            logger.error(" fail to get channel by "+ key,e);
        }
        logger.debug("observer channel for " + key + " not found  ");
        return null;
    }

    /**
     * get all channels by key
     * @param key
     * @return
     */
    public  List<Object> getChannels(String key) {
        List<Object> l = new ArrayList<>();
        try {
            for (HashMap<String, Object> chanel : channels) {
                if (chanel.containsKey(key)) {
                    if(chanel.get(key) != null)
                        l.add(chanel.get(key));
                }
            }
            logger.debug("observer channel for " + key + " found " + l.size());
        }catch (Exception e){
            logger.error("fail to get observer channel for " + key );
        }
        return l;
    }

    /**
     * get all channels
     * @return
     */
    public  List<Object> getChannels() {
        List<Object> l = new ArrayList<>();
        try {
            for (HashMap<String, Object> chanel : channels) {
                chanel.forEach((k, v) -> {
                    l.add(v);
                });
            }
            logger.debug("observer channels for  found " + l.size());
        }catch (Exception e){
            logger.error("fail to get observer channels  "  );
        }
        return l;
    }


    public Object getLastChannel(String key){
        List<Object> channels = getChannels(key);
        if(channels != null && channels.size() > 0) {
            return channels.get(channels.size() - 1);
        }
        return null;
    }
}
