package org.softauto.analyzer.core.handlers;

import org.softauto.analyzer.directivs.argument.Argument;
import org.softauto.analyzer.model.suite.Suite;
import org.softauto.analyzer.model.test.Test;
import org.softauto.analyzer.core.rules.NameRules;

import java.util.*;
import java.util.stream.Collectors;

public class HandleDependencies {

    List<Test> tests;

    Test test;

    List<Test> usedTests = new ArrayList<>();

    public HandleDependencies setTests(List<Test> tests) {
        this.tests = tests;
        return this;
    }

    public HandleDependencies setTest(Test test) {
        this.test = test;
        return this;
    }

    private boolean isExist(Test test,LinkedList<Test> tests)    {
        for(Test t : tests){
            if(test.getFullName().equals(t.getFullName())){
                return true;
            }
        }
        return false;
    }

    public LinkedList<Test> build(){
        LinkedList<Test> tests = new LinkedList<>();
        //for(Test test : this.tests) {
            tests = buildDependencies(test,this.tests,new LinkedList<>());

            if(!isExist(test,tests)) {
                tests.add(test);
            }

        //}
        return tests;
    }


    private List<Test> findTests(String context,String Subject){
        List<Test> testList = new ArrayList<>();
        for(Test test : tests){
            if(!test.getFullName().equals(this.test.getFullName()) ) {
                if (context != null && Subject != null && test.getContext() != null && test.getSubject() != null) {
                    if (test.getContext().equals(context) && test.getSubject().equals(Subject)) {
                        testList.add(test);
                    /*
                    if (Suite.hasEntity(test.getContext())) {
                        String  entityClassName = Suite.getEntityClass(test.getContext());
                        boolean isRootCrudCreate = this.test.isCrudCreate(entityClassName);
                        boolean isTestCrudCreate = test.isCrudCreate(entityClassName);

                        if (isTestCrudCreate) {
                            testList.add(test);
                        }
                        if (!isTestCrudCreate && !isRootCrudCreate) {
                            testList.add(test);
                        }
                    }


                     */
                    }
                }
            }
        }
        return testList;
    }

    public List<Test> filterByCrud(List<Test> tests,String  entityClassName){
        List<Test> testList = new ArrayList<>();
        if(this.test.isCreator(entityClassName)){
            return testList;
        }else {
            for(Test test : tests){
                if(test.isCreator(entityClassName)){
                    testList.add(test);
                }
            }
        }
        return testList;
    }

    private List<Test> findTestsByArgumentNameAndType(Argument argument ,List<Test> tests){
        List<Test> testList = new ArrayList<>();
        try {
            //String[] splitName = StringUtilsWrapper.splitByCharacterTypeCamelCase(argument.getName());
            //String name = StringUtils.join(splitName,"/").toLowerCase();
            String name =  NameRules.getLookupDependencyName(argument);
            for(Test test : tests){
                if (test.getResultPublishName().equals(name) ) {
                    if(Suite.hasEntity(name)) {
                        String entity = Suite.getEntityClass(name);
                        if(test.getData().getResponse().getType().equals(entity)) {
                            LinkedList<Test> t =  testList.stream().collect(Collectors.toCollection(LinkedList::new));
                            if(!test.getFullName().equals(this.test.getFullName()) ) {
                                if(!isExist(test, t)) {
                                    testList.add(test);
                                }
                            }
                        }
                    }
                }
            }
            for(Test test : tests){
                if (test.getResultPublishName().equals(name) && test.getData().getResponse().getType().equals(argument.getType())) {
                    LinkedList<Test> t =  testList.stream().collect(Collectors.toCollection(LinkedList::new));
                    if(!test.getFullName().equals(this.test) && !isExist(test, t)) {
                        testList.add(test);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return testList;
    }

    private Set<Test> findTestsByArgument(Argument argument ,List<Test> tests){
        Set<Test> testList = new HashSet<>();
        //for(Test test : tests){
           // if(test.getApi().getRequest().getArguments().size() > 0){
              //  for(Argument argument1 : test.getApi().getRequest().getArguments()){
                    String  entityClassName = Suite.getEntityClass(argument.getContext());
                    List<Test> testsInContext =   findTests(this.test.getContext(),argument.getContext());
                    List<Test> filterTestList = filterByCrud(testsInContext,entityClassName);
                    List<Test> testsByArgumentNameAndType = findTestsByArgumentNameAndType(argument,filterTestList);
                    testList.addAll(testsByArgumentNameAndType);
              //  }
          //  }
       // }
        return testList;
    }


    private LinkedList<Test> buildDependencies(Test test, List<Test> tests,LinkedList<Test> dependencies){
        usedTests.add(test);
        if(test.getApi().getRequest().getArguments().size() > 0){
            for(Argument argument : test.getApi().getRequest().getArguments()){
                    Set<Test> testList = findTestsByArgument(argument,tests);
                    if(testList.size() > 0) {
                        for (Test test1 : testList) {
                            LinkedList<Test> t = usedTests.stream().collect(Collectors.toCollection(LinkedList::new));
                            if (!isExist(test1, t) && !test1.getFullName().equals(this.test.getFullName())) {
                                dependencies = buildDependencies(test1, tests, dependencies);
                                dependencies.add(test1);
                            }
                        }
                    }
            }
        }
        return dependencies;
    }



}
