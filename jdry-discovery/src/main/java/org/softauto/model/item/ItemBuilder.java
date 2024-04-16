package org.softauto.model.item;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.softauto.Discover;
import org.softauto.flow.FlowObject;
import soot.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ItemBuilder {

    private static Logger logger = LogManager.getLogger(Discover.class);

    private static final Marker JDRY = MarkerManager.getMarker("JDRY");

    public static Builder newBuilder() { return new Builder();}

    Item item;

    public Item getItem() {
        return item;
    }

    public ItemBuilder(Item item){
        this.item = item;
    }

    public static class Builder {

        HashMap<String, Object> annotations ;

        protected String name;

        protected String fullname;

        protected String type;

        protected String namespce;

        protected LinkedList<String> parametersTypes = new LinkedList<>();

        protected List<String> argumentsNames = new ArrayList<>();

        protected int id;

        protected String returnType;

        protected boolean returnTypeGeneric;

        private String unboxReturnType;

        protected List<FlowObject> childes = new ArrayList<>();



        private int modifier;

        private String returnName;

        private List<String> responseChain = new ArrayList<>();

        protected HashMap<Integer,Boolean> argsType = new HashMap<>();

        private String subsignature;

        private String unboxReturnTypeTargetObject;

        protected String resultParameterizedType;

        private HashMap<Integer,String> parametersParameterizedType = new HashMap<>();

        public Builder  setUnboxReturnTypeTargetObject(String unboxReturnTypeTargetObject) {
            this.unboxReturnTypeTargetObject = unboxReturnTypeTargetObject;
            return this;
        }

        public Builder setFullname(String fullname) {
            this.fullname = fullname;
            return this;
        }

        public Builder setParametersParameterizedType(HashMap<Integer, String> parametersParameterizedType) {
            this.parametersParameterizedType = parametersParameterizedType;
            return this;
        }

        public Builder setResultParameterizedType(String resultParameterizedType) {
            this.resultParameterizedType = resultParameterizedType;
            return this;
        }



        public Builder setSubsignature(String subsignature) {
            this.subsignature = subsignature;
            return this;
        }



        public Builder setReturnTypeGeneric(boolean returnTypeGeneric) {
            this.returnTypeGeneric = returnTypeGeneric;
            return this;
        }

        public Builder setArgsType(HashMap<Integer, Boolean> argsType) {
            this.argsType = argsType;
            return this;
        }

        public Builder setResponseChain(List<String> responseChain) {
            this.responseChain = responseChain;
            return this;
        }

        public Builder setUnboxReturnType(String unboxReturnType) {
            this.unboxReturnType = unboxReturnType;
            return this;
        }

        public Builder setReturnName(String returnName) {
            this.returnName = returnName;
            return this;
        }

        public Builder setModifier(int modifier) {
            this.modifier = modifier;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setType(String type) {
            this.type = type;
            return this;
        }

        public Builder setArgumentsNames(List<String> argumentsNames) {
            this.argumentsNames = argumentsNames;
            return this;
        }



        public Builder setAnnotations( HashMap<String, Object> annotations) {
            this.annotations = annotations;
            return this;
        }

        public Builder setChildes(List<FlowObject> childes) {
            this.childes = childes;
            return this;
        }

        public Builder setReturnType(String returnType) {
            this.returnType = returnType;
            return this;
        }



        public Builder setNamespce(String namespce) {
            this.namespce = namespce;
            return this;
        }



        public Builder setParametersTypes(LinkedList<String> parametersTypes) {
            this.parametersTypes = parametersTypes;
            return this;
        }

        public Builder setId(int id) {
            this.id = id;
            return this;
        }


        public List<Item> buildChildes() {
            List<Item> items = new ArrayList<>();
            for (FlowObject child : childes) {
                Item item = new ItemFactory().setFlowObject(child).build().getItem();
                if (item != null) {
                    items.add(item);
                }
            }
            return items;
        }


        public ItemBuilder build() {
            List<Item> items = buildChildes();
            Item item = null;
            try {
                item = new Item();
                item.setAnnotations(annotations);
                item.setName(name);
                item.setType(type);
                item.setId(id);
                item.setChildes(items);
                item.setNamespce(namespce);
                item.setParametersTypes(parametersTypes);
                item.setReturnType(returnType);
                item.setUnboxReturnTypeTargetObject(unboxReturnTypeTargetObject);
                item.setArgumentsNames(argumentsNames);
                item.setModifier(modifier);
                item.setResponseChain(responseChain);
                item.setUnboxReturnType(unboxReturnType);
                item.setArgsType(argsType);
                item.setReturnTypeGeneric(returnTypeGeneric);
                item.setReturnName(returnName);

                item.setResultParameterizedType(resultParameterizedType);
                item.setParametersParameterizedType(parametersParameterizedType);
                item.setSubsignature(subsignature);
                //item.setFullName(fullname);
                logger.debug(JDRY,"sucessfully build item "+namespce+"."+name);
            } catch (Exception e) {
                logger.error(JDRY,"fail build Item ",e.getMessage());
            }
            return new ItemBuilder(item);
        }

    }
}
