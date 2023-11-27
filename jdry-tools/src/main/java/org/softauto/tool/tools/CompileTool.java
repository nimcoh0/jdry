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
        OptionSpec<String> analyzer = p.accepts("analyzer", "absolute path to analyzer file.").withRequiredArg()
                .ofType(String.class);
        OptionSpec<String> step = p.accepts("step", "generate steps interface.").withRequiredArg()
                .ofType(String.class);
        OptionSpec<String> listener = p.accepts("listener", "generate listeners interface.").withRequiredArg()
                .ofType(String.class);
        OptionSpec<String> all = p.accepts("all", "generate steps & listeners interface.").withRequiredArg()
                .ofType(String.class);
        OptionSpec<String> templateDir = p.accepts("templateDir", "set templateDir.").withRequiredArg()
                .ofType(String.class);
        //OptionSpec<String> output = p.accepts("output", "output path").withRequiredArg()
               //.ofType(String.class);
        OptionSet opts = p.parse(args.toArray(new String[0]));


        if (args.size() != 6 ) {
            err.println("Usage: -analyzer <analyzer_file> -step | -listener | all <output_file> ) -templateDir templateDir ");
            p.printHelpOn(err);
            return 1;
        }
        if (analyzer.value(opts) == null) {
            err.println("-analyzer must be specified.");
            return 1;
        }

        if (step.value(opts) == null && listener.value(opts) == null && all.value(opts) == null) {
            err.println("-step or -listener or -all  must be specified.");
            return 1;
        }

        if (templateDir.value(opts) == null) {
            err.println("-templateDir must be specified.");
            return 1;
        }


        //Configuration.put("templateDir",templateDir.value(opts));
        org.softauto.compiler.Compiler.setTemplateDir(templateDir.value(opts));

        if(step.value(opts) != null ){
            org.softauto.compiler.Compiler.compileSteps(analyzer.value(opts),step.value(opts));
        }
        if(listener.value(opts) != null ){
            org.softauto.compiler.Compiler.compileListeners(analyzer.value(opts),listener.value(opts));
        }
        if(all.value(opts) != null ){
            org.softauto.compiler.Compiler.compileSteps(analyzer.value(opts),all.value(opts));
            org.softauto.compiler.Compiler.compileListeners(analyzer.value(opts),all.value(opts));
        }
        return 0;
    }

    @Override
    public String getName() {
        return "compile";
    }

    @Override
    public String getShortDescription() {
        return "generate steps/listeners proxy ";
    }
}
