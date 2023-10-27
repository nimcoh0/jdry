package org.softauto.model.item;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.softauto.Main;
import org.softauto.discovery.handlers.flow.ClassInfo;
import org.softauto.discovery.handlers.flow.FlowObject;
import soot.SootMethod;
import soot.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ItemBuilder {

    private static Logger logger = LogManager.getLogger(Main.class);

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

        protected String type;

        protected String namespce;

        protected List<String> parametersTypes = new ArrayList<>();

        protected List<String> argumentsNames = new ArrayList<>();

        protected int id;

        protected String returnType;

        protected List<FlowObject> childes = new ArrayList<>();

        private ClassInfo classInfo;

        private int modifier;



        private List<HashMap<String,String>> crudToSubject = new ArrayList<>();

        public Builder setCrudToSubject(List<HashMap<String,String>> crudToSubject) {
            this.crudToSubject = crudToSubject;
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

        public Builder setClassInfo(ClassInfo classInfo) {
            this.classInfo = classInfo;
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

        public Builder setParametersTypes(List<Type> parametersTypes) {
            List<String> types = new ArrayList<>();
            for(Type type : parametersTypes){
                types.add(type.toString());
            }
            this.parametersTypes = types;
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
                item.setClassInfo(classInfo);
                item.setArgumentsNames(argumentsNames);
                item.setModifier(modifier);
                item.setCrudToSubject(crudToSubject);


                logger.debug("sucessfully build item "+namespce+"."+name);
            } catch (Exception e) {
                logger.error("fail build Item ",e.getMessage());
            }
            return new ItemBuilder(item);
        }

    }
}
