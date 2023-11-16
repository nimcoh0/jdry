package org.softauto.discovery;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.softauto.Discover;
import org.softauto.config.Context;
import org.softauto.core.Configuration;
import soot.SootClass;

import java.util.Collection;
import java.util.LinkedList;

public class ClassInheritanceDiscovery {

    private static Logger logger = LogManager.getLogger(Discover.class);

    private LinkedList<SootClass> buildTree(SootClass sootClass,String domain){
        LinkedList<SootClass> sootClassList = new LinkedList<>();
        try {
            if(sootClass != null && sootClass.hasSuperclass()){
                Object o = buildTree(sootClass.getSuperclass(),domain);
                if(o instanceof SootClass){
                    if(((SootClass) o).getPackageName().contains(domain)) {
                        sootClassList.add((SootClass) o);
                    }
                }
                if(o instanceof LinkedList<?>){
                    sootClassList.addAll((Collection<? extends SootClass>) o);
                }
            }
            if(sootClass.getPackageName().contains(domain)) {
                sootClassList.add(sootClass);
            }
            logger.debug("successfully build class Inheritance for "+sootClass.getName());
        } catch (Exception e) {
            logger.error("fail build class Inheritance for "+sootClass.getName(),e);
        }
        return sootClassList;
    }


    public LinkedList<SootClass> apply(Object o) {
        try {
            String domain = Configuration.get(Context.DOMAIN).asString();
            SootClass sootClass = (SootClass) o;
            LinkedList<SootClass> list = buildTree(sootClass,domain);
            return list;
        } catch (Exception e) {
            logger.error("fail build tree for "+ ((SootClass)o).getName());
        }
        return null;
    }


}
