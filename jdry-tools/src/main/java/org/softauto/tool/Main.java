package org.softauto.tool;


import org.softauto.tool.tools.AnalyzeTool;
import org.softauto.tool.tools.CompileTool;
import org.softauto.tool.tools.DiscoveryTool;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.io.InputStream;

/** Command-line driver. */
public class Main {

    /**
     * Available tools, initialized in constructor.
     */
    final Map<String, Tool> tools;

    int maxLen = 0;

    Main() {
        tools = new TreeMap<>();
        for (Tool tool : new Tool[] { new DiscoveryTool(), new CompileTool(), new AnalyzeTool()}) {
            Tool prev = tools.put(tool.getName(), tool);
            if (prev != null) {
                throw new AssertionError("Two tools with identical names: " + tool + ", " + prev);
            }
            maxLen = Math.max(tool.getName().length(), maxLen);
        }
    }

    public static void main(String[] args) throws Exception {
        int rc = new Main().run(args);
        System.exit(rc);
    }

    /**
     * Delegates to tool specified on the command-line.
     */
    private int run(String[] args) throws Exception {
        if (args.length != 0) {
            Tool tool = tools.get(args[0]);
            if (tool != null) {
                return tool.run(System.in, System.out, System.err, Arrays.asList(args).subList(1, args.length));
            }
        }

        System.err.print("Version ");
        try (InputStream versionInput = Main.class.getClassLoader().getResourceAsStream("VERSION.txt")) {
            printStream(versionInput);
        }
        System.err.print(" of ");
        try (InputStream noticeInput = Main.class.getClassLoader().getResourceAsStream("NOTICE.txt")) {
            printHead(noticeInput, 5);
        }


        System.err.println("----------------");

        System.err.println("Available tools:");
        for (Tool k : tools.values()) {
            System.err.printf("%" + maxLen + "s  %s\n", k.getName(), k.getShortDescription());
        }

        return 1;
    }

    private static void printStream(InputStream in) throws Exception {
        byte[] buffer = new byte[1024];
        for (int i = in.read(buffer); i != -1; i = in.read(buffer))
            System.err.write(buffer, 0, i);
    }

    private static void printHead(InputStream in, int lines) throws Exception {
        BufferedReader r = new BufferedReader(new InputStreamReader(in));
        for (int i = 0; i < lines; i++) {
            String line = r.readLine();
            if (line == null) {
                break;
            }
            System.err.println(line);
        }
    }
}
