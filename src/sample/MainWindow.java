package sample;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import main.java.api.VisFx;

import java.io.File;
import java.io.IOException;

public class MainWindow {
    private Stage stage;
    private AnchorPane pane;

    public MainWindow() {
        stage = new Stage();
        pane = new AnchorPane();

        Scene scene = new Scene(pane, 720, 480);
        stage.setScene(scene);
        createButtons();
        createBackground();
    }

    public Stage getStage(){
        return stage;
    }

    private void createButtons() {
        Button button = new Button("Buscar JAR");
        button.setLayoutX(320);
        button.setLayoutY(240);
        button.setOnAction(event -> {
            try{
                openFileChooser();
            } catch (IOException e){
                e.printStackTrace();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        });
        pane.getChildren().add(button);
    }

    private void openFileChooser() throws IOException, Exception {
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File("/Users/isaacbenavides/Documents"));
        fc.getExtensionFilters().addAll(new ExtensionFilter("JAR", "*.jar"));

        File jarFile = fc.showOpenDialog(null);

        if (jarFile != null){
            System.out.println("got it");
            Analizer analizer = new Analizer(jarFile);
            analizer.generateGraph2();

            new SubStage(analizer.getJarsList(), jarFile, stage);

            VisFx.graphNetwork(analizer.getVisualGraph(), stage);
        } else {
            System.out.println("Null file");
        }
    }

    private void createBackground(){
        Label titule = new Label("Jar Analyzer");
        titule.setFont(Font.font("Times new Roman", 50));
        titule.setTextFill(Color.valueOf("F8FFFD"));
        titule.setTranslateX(230);
        titule.setTranslateY(50);
        pane.getChildren().add(titule);

        Image backgroundImage = new Image("images/background.jpg");

        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, null);

        pane.setBackground(new Background(background));
    }
}
