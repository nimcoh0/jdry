import org.softauto.analyzer.core.Main;

public class BasicTest {

    Main main;

    public BasicTest(){
        main = new Main(System.getProperty("user.dir")+"/resources/analyzer.yaml");
    }



}
