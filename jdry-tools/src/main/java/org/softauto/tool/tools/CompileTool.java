package org.softauto.tool.tools;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import org.softauto.tool.Tool;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;

public class CompileTool implements Tool {
    @Override
    public int run(InputStream in, PrintStream out, PrintStream err, List<String> args) throws Exception {
        OptionParser p = new OptionParser();
        OptionSpec<String> discovery = p.accepts("discovery", "absolute path to discovery file.").withRequiredArg()
                .ofType(String.class);
        OptionSpec<String> listener = p.accepts("listener", "generate Listeners interface.").withRequiredArg()
                .ofType(String.class);
        OptionSpec<String> step = p.accepts("step", "generate steps interface.").withRequiredArg()
                .ofType(String.class);
        OptionSpec<String> templateDir = p.accepts("templateDir", "set templateDir.").withRequiredArg()
                .ofType(String.class);
        //OptionSpec<String> output = p.accepts("output", "output path").withRequiredArg()
                //.ofType(String.class);
        OptionSet opts = p.parse(args.toArray(new String[0]));


        if (args.size() != 6 && args.size() != 8 && args.size() != 10) {
            err.println("Usage: -discovery <discovery_file> (-listener <output_file> | -step <output_file> ) -templateDir templateDir ");
            p.printHelpOn(err);
            return 1;
        }
        if (discovery.value(opts) == null) {
            err.println("-discovery must be specified.");
            return 1;
        }

        if (listener.value(opts) == null && step.value(opts)== null ) {
            err.println("one or more of -listener | -step  must be specified.");
            return 1;
        }



        if (templateDir.value(opts) == null) {
            err.println("-templateDir must be specified.");
            return 1;
        }


        //Configuration.put("templateDir",templateDir.value(opts));
        org.softauto.compiler.Compiler.setTemplateDir(templateDir.value(opts));
        if(listener.value(opts) != null ){
            org.softauto.compiler.Compiler.compileListeners(discovery.value(opts),listener.value(opts));
        }
        if(step.value(opts) != null ){
            org.softauto.compiler.Compiler.compileSteps(discovery.value(opts),step.value(opts));
        }

        return 0;
    }

    @Override
    public String getName() {
        return "compiler";
    }

    @Override
    public String getShortDescription() {
        return "generate listeners proxy & steps proxy & protocols enum & (tests - only in the pro version)";
    }
}
