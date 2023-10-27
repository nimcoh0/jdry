package org.softauto.analyzer.model.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.softauto.analyzer.model.data.Data;
import org.softauto.analyzer.model.expected.Expected;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class TestBuilder {

    private static Logger logger = LogManager.getLogger(TestBuilder.class);

    public static Builder newBuilder() { return new Builder();}

    private Test test;

    public TestBuilder(Test test){
        this.test = test;
    }

    public Test getTest() {
        return test;
    }

    public static class Builder {

        private String testId;

        private String name;

        private String namespace;

        private Data data;

        private int order;

        private HashMap<String,Object> publish = new HashMap<>();

        private String fullName;

        private HashMap<String, Object> annotations = new HashMap<>();

        private String context;

        private String subject;

        private List<HashMap<String, String>> crud = new ArrayList<>();

        //private String resultType;

        //private String resultName;

        private String resultPublishName;

        private Expected expected;

        public Builder setCrud(List<HashMap<String, String>> crud) {
            this.crud = crud;
            return this;
        }

        public Builder setSubject(String subject) {
            this.subject = subject;
            return this;
        }

        public Builder setContext(String context) {
            this.context = context;
            return this;
        }

        public Builder setExpected(Expected expected) {
            this.expected = expected;
            return this;
        }

        public Builder setResultPublishName(String resultPublishName) {
            this.resultPublishName = resultPublishName;
            return this;
        }

        /*
        public Builder setResultName(String resultName) {
            this.resultName = resultName;
            return this;
        }

        public Builder setResultType(String resultType) {
            this.resultType = resultType;
            return this;
        }

         */

        public Builder setPublish(HashMap<String, Object> publish) {
            this.publish = publish;
            return this;
        }

        public Builder addPublish(String key,String value) {
            this.publish.put(key,value);
            return this;
        }

        public Builder setTestId(String testId) {
            this.testId = testId;
            return this;
        }

        public Builder setFullName(String fullName) {
            this.fullName = fullName;
            return this;
        }




        public Builder setOrder(int order) {
            this.order = order;
            return this;
        }

        public Builder setData(Data data) {
            this.data = data;
            return this;
        }

        public Builder setAnnotations(HashMap<String, Object> annotations) {
            this.annotations = annotations;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setNamespace(String namespace) {
            this.namespace = namespace;
            return this;
        }



        public TestBuilder build(){
            Test test = new Test();
            try {
                test.setName(name);
                test.setNamespace(namespace);
                test.setData(data);
                test.setOrder(order);
                test.setTestId(testId);
                test.setPublish(publish);
                test.setFullName(fullName);
                test.setContext(context);
                test.setSubject(subject);
                test.setCrud(crud);
                test.setExpected(expected);
                test.setResultPublishName(resultPublishName);
                //test.setSubject(subject);
            } catch (Exception e) {
                logger.error("fail build test "+fullName,e);
            }
            return new TestBuilder(test);
        }
    }

}
