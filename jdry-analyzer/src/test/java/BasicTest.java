import org.softauto.analyzer.Analyzer;

public class BasicTest {

    Analyzer main;

    public BasicTest(){
        main = new Analyzer(System.getProperty("user.dir")+"/resources/analyzer.yaml");
    }



}
