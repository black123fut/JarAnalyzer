package sample;


import javafx.application.Application;

import javafx.stage.Stage;
import main.java.api.VisFx;
import main.java.graph.VisEdge;
import main.java.graph.VisGraph;
import main.java.graph.VisNode;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception{
        MainWindow windows = new MainWindow();
        stage = windows.getStage();
        stage.setTitle("Jar Analizer");
        stage.show();
//        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
//        primaryStage.setTitle("Hello World");
//        primaryStage.setScene(new Scene(root, 300, 275));
//        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
