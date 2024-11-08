package org.softauto.discovery;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.softauto.Discover;
import org.softauto.config.Configuration;
import org.softauto.config.Context;
import soot.Scene;
import soot.options.Options;
import java.util.*;


public abstract class AbstractDiscovery {

    private static Logger logger = LogManager.getLogger(Discover.class);

    private static final Marker JDRY = MarkerManager.getMarker("JDRY");

    List<String> argsList = new ArrayList();

    ObjectNode discovery = new ObjectMapper().createObjectNode();

    List<String> clazzes  = new ArrayList<>();

    List<String> excludePackagesList = Arrays.asList("java.*", "com.google.*",  "javax.*","jdk.*","soot.*");

    String [] args = null;

    static {
        soot.options.Options.v().set_keep_line_number(true);
        soot.options.Options.v().set_whole_program(true);
        soot.options.Options.v().setPhaseOption("cg","verbose:true");
    }
    protected boolean isInClazzList(String name){
        for(String s : clazzes){
            if(s.equals(name)){
                return true;
            }
        }
        return false;
    }


    /**
     * set soot configuration
     */
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
        //Options.v().set_process_jar_dir(Configuration.get(Context.JAR_DIR).asList());
        Options.v().set_whole_program(true);
        Options.v().set_app(true);
        Options.v().set_exclude(getExcludePackagesList());
        Options.v().set_no_bodies_for_excluded(true);
        Options.v().set_allow_phantom_refs(true);
        Options.v().setPhaseOption("jb","use-original-names:true");
        Options.v().setPhaseOption("jb.sils", "enabled:false");
        Scene.v().loadNecessaryClasses();

    }


    public List<String> getExcludePackagesList() {
        return excludePackagesList;
    }

    public JsonNode getDiscovery() {
        return discovery;
    }
}
