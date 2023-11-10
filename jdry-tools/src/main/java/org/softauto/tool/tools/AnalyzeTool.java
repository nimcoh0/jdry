package org.softauto.tool.tools;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import org.softauto.tool.Tool;
import org.softauto.analyzer.Analyzer;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;

public class AnalyzeTool implements Tool {
    @Override
    public int run(InputStream in, PrintStream out, PrintStream err, List<String> args) throws Exception {
        OptionParser p = new OptionParser();
        OptionSpec<String> conf = p.accepts("conf", "absolute path to configuration file.").withRequiredArg()
                .ofType(String.class);
        OptionSpec<String> discover = p.accepts("discover", "absolute path to discovery file.").withRequiredArg()
                .ofType(String.class);
        OptionSpec<String> recorded = p.accepts("recorded", "optional : absolute path to recorded file.").withRequiredArg()
                .ofType(String.class);
        OptionSpec<String> output = p.accepts("output", "output path").withRequiredArg()
                .ofType(String.class);
        OptionSet opts = p.parse(args.toArray(new String[0]));
        //args = (List<String>) opts.nonOptionArguments();

        if (args.size() != 6 && args.size() != 8) {
            err.println("Usage: -conf <configuration_file> -discover <discovery_file> (-recorded <recorded_file>) -output <output_file>");
            p.printHelpOn(err);
            return 1;
        }
        if (conf.value(opts) == null) {
            err.println("-conf must be specified.");
            return 1;
        }

        if (discover.value(opts) == null) {
            err.println("-discover must be specified.");
            return 1;
        }

        if (output.value(opts) == null) {
            err.println("-output must be specified.");
            return 1;
        }

        if (recorded.value(opts) != null) {
            new Analyzer(conf.value(opts),discover.value(opts),recorded.value(opts),output.value(opts) );
        }else{
            new Analyzer(conf.value(opts),discover.value(opts),output.value(opts) );
        }

        return 0;
    }

    @Override
    public String getName() {
        return "analyzer";
    }

    @Override
    public String getShortDescription() {
       return "analyze the discovery & (recorded - only in the pro version ) file and create tests schema";
    }
}
