package org.softauto.tool.tools;

import org.softauto.Discover;
import org.softauto.tool.Tool;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.File;
import java.net.URI;
import java.util.List;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;

public class DiscoveryTool implements Tool {

    @Override
    public int run(InputStream in, PrintStream out, PrintStream err, List<String> args) throws Exception {
        OptionParser p = new OptionParser();
        OptionSpec<String> conf = p.accepts("conf", "absolute path to configuration file.").withRequiredArg()
                .ofType(String.class);
        OptionSpec<String> output = p.accepts("output", "output path and name .").withRequiredArg()
                .ofType(String.class);
        OptionSet opts = p.parse(args.toArray(new String[0]));
        //args = (List<String>) opts.nonOptionArguments();

        if (args.size() != 4 ) {
            err.println("Usage: -conf f -output output-file");
            p.printHelpOn(err);
            return 1;
        }
        if (output.value(opts) == null) {
            err.println("-output must be specified.");
            return 1;
        }
        if (conf.value(opts) == null) {
            err.println("-conf must be specified.");
            return 1;
        }

        new Discover(conf.value(opts),output.value(opts) );

        return 0;
    }

    @Override
    public String getName() {
        return "discovery";
    }

    @Override
    public String getShortDescription() {
        return "create a call graph of the target application";
    }




}
