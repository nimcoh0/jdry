package org.softauto.discovery;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.softauto.Main;
import org.softauto.config.Configuration;
import org.softauto.config.Context;
import soot.*;
import soot.options.Options;
import java.util.*;

public abstract class AbstractDiscovery {

    private static Logger logger = LogManager.getLogger(Main.class);

    List<String> argsList = new ArrayList();

    List<Object> items = new ArrayList<>();

    List<String> clazzes  = new ArrayList<>();

    List<String> excludePackagesList = Arrays.asList("java.*", "com.google.*",  "javax.*","jdk.*","soot.*");

    String [] args = null;

    protected boolean isInClazzList(String name){
        for(String s : clazzes){
            if(s.equals(name)){
                return true;
            }
        }
        return false;
    }



    public AbstractDiscovery(){
        argsList.addAll(Arrays.asList(new String[]{
                "-w",
                "-p",
                "jb",
                "use-original-names:true",
                "cg",
                "verbose:true",
                "safe-newinstance:true"

        }));
        args = argsList.toArray(new String[argsList.size()]);
        Options.v().set_process_dir(Configuration.get(Context.CLASS_DIR).asList());
        Options.v().set_whole_program(true);
        Options.v().set_app(true);
        Options.v().set_exclude(getExcludePackagesList());
        Options.v().set_no_bodies_for_excluded(true);
        Options.v().set_allow_phantom_refs(true);
        Options.v().setPhaseOption("jb","use-original-names:true");
        Scene.v().getSootClassPath();
    }


    public List<String> getExcludePackagesList() {
        return excludePackagesList;
    }

    public List<Object> getItems() {
        return items;
    }
}
