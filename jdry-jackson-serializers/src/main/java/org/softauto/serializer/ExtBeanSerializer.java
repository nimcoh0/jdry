package org.softauto.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializer;
import com.fasterxml.jackson.databind.ser.BeanSerializerBuilder;
import com.fasterxml.jackson.databind.ser.std.StringSerializer;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class ExtBeanSerializer extends BeanSerializer {

    //private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(ExtBeanSerializer.class);
    //private static LinkedList<Rec> lifo = new LinkedList();
    //private static List<Rec> recToSkip = new ArrayList<>();
    //private static int head = -1;
    //private static int tail = -1;
    private int maxDepth = 2048;
    //private static int depth = 0;
    private boolean depthLimitEnabled = true;
    private boolean loopPreventionEnabled = true;
    //private boolean serializeBigObjects = false;

    private List<HashMap<String,String>> serializationExcludeProperties = new ArrayList<>();

    private List<String> serializationExcludeClassList = new ArrayList<>();



    private static int depth  = 0;

    public ExtBeanSerializer setSerializationExcludeClassList(List<String> serializationExcludeClassList) {
        this.serializationExcludeClassList = serializationExcludeClassList;
        return this;
    }

    public ExtBeanSerializer setSerializationExcludeProperties(List<HashMap<String,String>> serializationExcludeProperties) {
        this.serializationExcludeProperties = serializationExcludeProperties;
        return this;
    }

    private boolean isPropertyExclude(String clazz , String property){
        for(HashMap<String,String> map : serializationExcludeProperties) {
            for (Map.Entry entry : map.entrySet()) {
                if (entry.getKey().equals(clazz) && entry.getValue().equals(property)) {
                    return true;
                }
            }
        }
        return false;
    }
//private ThreadLocal<Set<Rec>> threadlocallifo;// = new ThreadLocal<>();

    //private ThreadLocal<Set<Rec>> threadlocalRecToSkip = new ThreadLocal<>();

    //private static LinkedList<Rec> hashCodeList = new LinkedList<>();
    private static  List<Rec> list = new ArrayList<Rec>();
    private static List<Rec> hashCodeList = Collections.synchronizedList(list);

    //private Set<Rec> recsToSkip = new LinkedHashSet<>();


    public static void setDepth(int depth) {
        ExtBeanSerializer.depth = depth;
    }

    public static void setHashCodeList(LinkedList<Rec> hashCodeList) {
        ExtBeanSerializer.hashCodeList = hashCodeList;
    }

    public ExtBeanSerializer(JavaType type, BeanSerializerBuilder builder, BeanPropertyWriter[] properties, BeanPropertyWriter[] filteredProperties) {
            super(type, builder, properties, filteredProperties);

            //threadlocallifo.set(Collections.synchronizedSet(recs));
            //threadlocalRecToSkip.set(Collections.synchronizedSet(recsToSkip));

            //CopyOnWriteArrayList<Rec> copyOnWriteArrayList = new CopyOnWriteArrayList<>();
            //recToSkip = new ArrayList<>();
            //addRecToRecToSkip(new ArrayLis());
            //lifo = new LinkedList();
    }

    /*
    public void setSerializeBigObjects(boolean serializeBigObjects) {
        this.serializeBigObjects = serializeBigObjects;
    }

     */

    private boolean isClassExclude(Object bean){
        for(String path : serializationExcludeClassList){
            if(bean.getClass().getTypeName().contains(path)){
                return true;
            }
        }
        return false;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public void setMaxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    public boolean isDepthLimitEnabled() {
        return depthLimitEnabled;
    }

    public void setDepthLimitEnabled(boolean depthLimitEnabled) {
        this.depthLimitEnabled = depthLimitEnabled;
    }

    public boolean isLoopPreventionEnabled() {
        return loopPreventionEnabled;
    }

    public void setLoopPreventionEnabled(boolean loopPreventionEnabled) {
        this.loopPreventionEnabled = loopPreventionEnabled;
    }


    private synchronized boolean isExist(String hashcode){
        try {
            // logger.debug("checking existence for rec in the lifo list with "+ rec.getBean() +" "+rec.getId()+" "+ rec.getProp());
            // for (int i = recs.size() - 1; i >= 0; i--) {
            //Map<Integer,Rec> map = Maps.uniqueIndex(recs, Rec::getKey);
            //return hashCodeList.stream().filter(item -> item.getHashcode().equals(hashcode))
                                            //   .findFirst().isPresent();
            for(Rec rec : hashCodeList){
                if(rec.getHashcode().equals(hashcode)){
                    return true;
                }
            }
            // Foo foo = set.stream().filter(item->item.equals(theItemYouAreLookingFor)).findFirst().get();

            //}
        }catch (Exception e){
            e.printStackTrace();
            //logger.error("fail checking existence rec in the lifo ",e);
        }
        return false;
    }

    private synchronized boolean isExist(Rec rec){
        try {
           // logger.debug("checking existence for rec in the lifo list with "+ rec.getBean() +" "+rec.getId()+" "+ rec.getProp());
           // for (int i = recs.size() - 1; i >= 0; i--) {
                //Map<Integer,Rec> map = Maps.uniqueIndex(recs, Rec::getKey);
            for(Rec r : hashCodeList){
                if(r.getHashcode().equals(rec.getHashcode()) && r.getBean().equals(rec.getBean()) ){
                    return true;
                }
            }

               // return hashCodeList.stream().filter(item -> item.getHashcode().equals(rec.getHashcode())
                                                 //   && item.getProp().equals(rec.getProp())
                                                 //   && item.getBean().equals(rec.getBean()))
                                                //    .findFirst().isPresent();

                // Foo foo = set.stream().filter(item->item.equals(theItemYouAreLookingFor)).findFirst().get();

            //}
        }catch (Exception e){
            e.printStackTrace();
            //logger.error("fail checking existence rec in the lifo ",e);
        }
        return false;
    }



    /*
    private synchronized boolean isSkip(Rec rec){
        try {
            if(rec != null) {
                //logger.debug("checking existence for rec in the skip list with " + rec.getBean() + " " + rec.getId() + " " + rec.getProp());
                //for (int i = 0; i < recsToSkip.size(); i++) {
                    //Rec _rec = recsToSkip.get(i);
                    Rec _rec = recsToSkip.stream().filter(rec::equals).reduce((first, second) -> second).orElse(null);
                    //if (_rec.getBean().equals(rec.getBean()) && _rec.getId().equals(rec.getId()) && _rec.getProp().equals(rec.getProp())) {
                       // logger.debug("found rec in the skip list at location " + i);
                    if(_rec != null){
                        return true;
                    }
               // }
            }
        }catch (Exception e){
            e.printStackTrace();
           // logger.error("fail checking existence rec in the Skip list ",e);
        }
        return false;
    }


     */
    /*
    private synchronized void addToSkip(Rec rec){
        try {
            //for (Rec r : recsToSkip) {
               // if (r.getBean().equals(rec.getBean()) && r.getId().equals(rec.getId()) && r.getProp().equals(rec.getProp())) {
                //    return;
              //  }
          //  }
            if(!isSkip(rec)) {
                 recsToSkip.add(rec);
            }
            //logger.debug("adding rec to skip list "+ rec.getBean() +" "+rec.getId()+" "+ rec.getProp());
           // logger.debug("skip list size "+recToSkip.size());

        }catch (Exception e){
            e.printStackTrace();
           // logger.error("fail adding Rec to skip list ",e);
        }
    }



     */

    @Override
    protected synchronized void serializeFields(Object bean, JsonGenerator gen, SerializerProvider provider) throws IOException {
        depth++;
        //gen.configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true);
        String hashcode = Integer.toHexString(bean.hashCode());


            if (depthLimitEnabled && depth > maxDepth) {
                //if(serializeBigObjects) {
                //depth = depth - 1;
                //}
                return;
            }
            BeanPropertyWriter[] props;

            if(isExist(hashcode) || isClassExclude(bean)) {
                props = new BeanPropertyWriter[0];
            }else {
                if (this._filteredProps != null && provider.getActiveView() != null) {
                    props = this._filteredProps;
                } else {
                    props =  this._props;
                }
            }
            Rec rec = Rec.newBuilder().setBean(bean.getClass().getName()).setHashcode(Integer.toHexString(bean.hashCode())).build();
            hashCodeList.add(rec);

            int i = 0;

            try {
                for (int len = props.length; i < len; ++i) {
                    BeanPropertyWriter prop = props[i];

                    try {
                        if (!isPropertyExclude(bean.getClass().getName(), prop.getName())) {

                            if (prop != null) {
                                // Rec rec = null;
                                if (loopPreventionEnabled) {
                                    //Rec rec = Rec.newBuilder().setBean(bean.getClass().getName()).setHashcode(Integer.toHexString(bean.hashCode())).setProp(prop.getName()).build();
                                    //hashCodeList.add(rec);
                                    //rec = Rec.newBuilder().setBean(bean.getClass().getName()).setHashcode(Integer.toHexString(bean.hashCode())).setProp(prop.getName()).build();
                                    //Class c = ClassUtils.getClass(prop.getType().getRawClass().getTypeName());
                                    //String hashcode = Integer.toHexString(bean.hashCode());
                                    //if (!ClassUtils.isPrimitiveOrWrapper(c) && !c.getTypeName().equals("java.lang.String")) {
                                    //if (!hashCodeList.contains(hashcode)) {
                                    // hashCodeList.add(hashcode);
                                    prop.serializeAsField(bean, gen, provider);

                                    // }
                                    // } else {
                                    // prop.serializeAsField(bean, gen, provider);
                                    // new StringSerializer().serialize("remove by loop prevention", gen, provider);
                                    //}

                        /*
                        if (j != -1 && head != -1 && rec.getBean().equals(recs.get(head).getBean()) && rec.getProp().equals(recs.get(head).getProp()) && rec.getId().equals(recs.get(head).getId())) {
                            addToSkip(recs.get(tail));
                            head = -1;
                            tail = -1;
                        }
                        if (j != -1 && head == -1) {
                            head = j;
                            tail = j;
                        } else if (j != -1 && j == tail + 1) {
                            tail = j;
                        } else {
                            head = -1;
                            tail = -1;
                        }


                         */
                                    //synchronized(rec) {

                                    //lifo.add(rec);
                                    //}
                                }
                                //if(maxDepth > depth) {
                                // if (recsToSkip == null || !isSkip(rec)) {
                                //     prop.serializeAsField(bean, gen, provider);
                                // }
                                //else {
                                // logger.debug("rec in skip list ... skipping : " + rec.getBean() + " " + rec.getProp() + " " + rec.getId());

                                // }
                                //}
                                //else {
                                //logger.debug("depth is "+depth+ "skipping "+  rec.getBean() + " " + rec.getProp() + " " + rec.getId());
                                // return ;
                                //}
                            }
                        }
                        }catch(Exception e){
                            // e.printStackTrace();
                            //BeanPropertyWriter p = BeanPropertyWriter.MARKER_FOR_EMPTY;
                            //FieldUtils.writeDeclaredField(prop,"_accessorMethod",null,true);
                            //Field f = FieldUtils.getDeclaredField(this.getClass(),"demoField",true);
                            //FieldUtils.writeDeclaredField(prop,"_field",f,true);
                            //Class c = prop.getType().getRawClass();
                            //prop.serializeAsField((Object)null, gen, provider);
                            //gen.writeFieldName("sadf");
                            //gen.writeEndObject();
                            //prop.serializeAsPlaceholder(bean, gen, provider);
                        }
                    }

                depth--;

                if (this._anyGetterWriter != null) {
                    this._anyGetterWriter.getAndSerialize(bean, gen, provider);
                }
            } catch (Exception var9) {
                String name = i == props.length ? "[anySetter]" : props[i].getName();
                this.wrapAndThrow(provider, var9, bean, name);
            } catch (StackOverflowError var10) {
                JsonMappingException mapE = new JsonMappingException(gen, "Infinite recursion (StackOverflowError)", var10);
                String name = i == props.length ? "[anySetter]" : props[i].getName();
                mapE.prependPath(new JsonMappingException.Reference(bean, name));
                throw mapE;
            }
        //}
    }



    public static class Rec {

        public static Builder newBuilder() { return new Builder();}



        private String bean;

        private String hashcode;

        private String prop;

        public Rec(String bean,String hashcode,String prop){
            this.bean = bean;
            this.hashcode = hashcode;
            this.prop = prop;

        }



        public String getBean() {
            return bean;
        }

        public String getHashcode() {
            return hashcode;
        }

        public String getProp() {
            return prop;
        }

        public static class Builder {
            private String bean;

            private String hashcode;

            private String prop;



            public Builder setBean(String bean) {
                this.bean = bean;
                return this;
            }

            public Builder setHashcode(String hashcode) {
                this.hashcode = hashcode;
                return this;
            }

            public Builder setProp(String prop) {
                this.prop = prop;
                return this;
            }

            public Rec build(){
                return new Rec(bean,hashcode,prop);
            }
        }
    }
}
