import javafx.application.Application;
import javafx.stage.Stage;
import controllers.ServerHome;

public class Main  extends Application {

    ServerHome srvHome;
    public static void main(String[] args) {
        System.out.println("Launching Server");
        launch(args);
        System.out.println("Closing Server");
    }


    // Make sure when you make a new scene you construct it, so it is added to map
    @Override
    public void start(Stage stage) {
        srvHome = new ServerHome(stage);
        srvHome.render();
        stage.show();
    }
}
