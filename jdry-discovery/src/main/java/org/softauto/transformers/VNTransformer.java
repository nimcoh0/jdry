package org.softauto.transformers;

import soot.*;
import soot.jimple.*;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.UnitGraph;

import java.util.Iterator;
import java.util.Map;
import soot.SceneTransformer;

public class VNTransformer extends SceneTransformer {

    @Override
    protected void internalTransform(String arg0, Map arg1) {
        SootClass mainClass = Scene.v().getMainClass(); // get main class

        System.out.println("Methods: " + mainClass.getMethodCount()); // get methods count for class
        SootMethod testMethod = mainClass.getMethodByName("startServer"); // get method test from main class
        Body methodBody = testMethod.retrieveActiveBody();// get method body as an object
        testMethod.getSource();

        // Create directed-graph based on the method body
        UnitGraph graph = new BriefUnitGraph(methodBody);
        Iterator<Unit> unitIt = graph.iterator();
        // iterate over method body
        while (unitIt.hasNext()) {
            Unit unit = unitIt.next();
            System.out.println(unit);
            if (unit instanceof AssignStmt) { // check if it is Assignment Statement
                AssignStmt assignStmt = (AssignStmt) unit;

                if (assignStmt.getRightOp() instanceof AddExpr) {// check if add operation is right side
                    System.out.println("This is Addition operation");
                }
                if (assignStmt.getRightOp() instanceof MulExpr) {// check if Multiplication operation is right side
                    System.out.println("This Multiplication operation");
                }
                if (assignStmt.getRightOp() instanceof DivExpr) {// check if Divide operation is right side
                    System.out.println("This Divide operation");
                }
                if (assignStmt.getRightOp() instanceof SubExpr) {// check if Subtract operation is right side
                    System.out.println("This Subtract operation");
                }
                //iterate over UseBoxes of current Unit (statement)-- It is right side of statement
                for (ValueBox useBox : assignStmt.getUseBoxes()) {
                    // if Value of defBox is Local variable
                    if (useBox.getValue() instanceof Local) {
                        Local useLocal = (Local) useBox.getValue();
                        System.out.println("Used:" + useLocal.getName());
                    }
                }
                //iterate over DefBoxes of current Unit (statement)-- It is left side of statement
                for (ValueBox defBox : assignStmt.getDefBoxes()) {
                    // if Value of defBox is Local variable
                    if (defBox.getValue() instanceof Local) {
                        Local defLocal = (Local) defBox.getValue();
                        System.out.println("Defined:" + defLocal.getName());
                    }
                }
            }
        }


    }
}
