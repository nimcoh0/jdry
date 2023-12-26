package org.softauto.transformers;

import org.softauto.config.Context;
import org.softauto.core.Configuration;
import soot.*;
import soot.jimple.Stmt;
import soot.options.Options;

import java.io.File;
import java.util.Iterator;
import java.util.Map;

public class TestSoot extends BodyTransformer {

    public static void main(String[] args)	{

        //String mainclass = Configuration.get(Context.MAIN_CLASS).asString();
        SootClass appClass = Scene.v().loadClassAndSupport(Configuration.get(Context.MAIN_CLASS).asString());

//		//set classpath
        String javapath = System.getProperty("java.class.path");
        //String jredir = System.getProperty("java.home")+"/lib/rt.jar";
        //String path = javapath+File.pathSeparator+jredir;
        Scene.v().setSootClassPath(javapath);

        //add an intra-procedural analysis phase to Soot
        TestSoot analysis = new TestSoot();
        //Example example = new Example();
        PackManager.v().getPack("wjtp").add(new Transform("wjtp.TestSoot", analysis));

        //load and set main class
        Options.v().set_app(true);
        Options.v().setPhaseOption("jb","use-original-names:true");
        Options.v().set_whole_program(true);
        //SootClass appclass = Scene.v().loadClassAndSupport(mainclass);
        Scene.v().setMainClass(appClass);
        Scene.v().loadNecessaryClasses();

        //start working
        PackManager.v().runPacks();
    }

    @Override
    protected void internalTransform(Body b, String phaseName,
                                     Map<String, String> options) {

        Iterator<Unit> it = b.getUnits().snapshotIterator();
        while(it.hasNext()){
            Stmt stmt = (Stmt)it.next();

            System.out.println(stmt);
        }
    }
}