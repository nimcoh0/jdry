package org.softauto.tool.tools;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import org.softauto.analyzer.core.system.config.Configuration;
import org.softauto.tool.Tool;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;

public class CompileTool implements Tool {
    @Override
    public int run(InputStream in, PrintStream out, PrintStream err, List<String> args) throws Exception {
        OptionParser p = new OptionParser();
        OptionSpec<String> analyzer = p.accepts("analyzer", "absolute path to analyze file.").withRequiredArg()
                .ofType(String.class);
        OptionSpec<String> listener = p.accepts("listener", "generate Listeners interface.").withRequiredArg()
                .ofType(String.class);
        OptionSpec<String> step = p.accepts("step", "generate steps interface.").withRequiredArg()
                .ofType(String.class);
        OptionSpec<String> protocol = p.accepts("protocol", "generate protocol enum.").withRequiredArg()
                .ofType(String.class);
        OptionSpec<String> templateDir = p.accepts("templateDir", "set templateDir.").withRequiredArg()
                .ofType(String.class);
        //OptionSpec<String> output = p.accepts("output", "output path").withRequiredArg()
                //.ofType(String.class);
        OptionSet opts = p.parse(args.toArray(new String[0]));


        if (args.size() != 6 && args.size() != 8 && args.size() != 10) {
            err.println("Usage: -analyzer <analyzer_file> (-listener <output_file> | -step <output_file> | -protocol <output_file>) -templateDir templateDir ");
            p.printHelpOn(err);
            return 1;
        }
        if (analyzer.value(opts) == null) {
            err.println("-analyzer must be specified.");
            return 1;
        }

        if (listener.value(opts) == null && step.value(opts)== null && protocol.value(opts)== null) {
            err.println("one or more of -listener | -step | -protocol  must be specified.");
            return 1;
        }



        if (templateDir.value(opts) == null) {
            err.println("-templateDir must be specified.");
            return 1;
        }


        Configuration.put("templateDir",templateDir.value(opts));
        if(listener.value(opts) != null ){
            org.softauto.compiler.Compiler.compileListeners(analyzer.value(opts),listener.value(opts));
        }
        if(step.value(opts) != null ){
            org.softauto.compiler.Compiler.compileSteps(analyzer.value(opts),step.value(opts));
        }
        if(protocol.value(opts) != null ){
            org.softauto.compiler.Compiler.compileProtocols(analyzer.value(opts),protocol.value(opts));
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
