package sample;

import DataStructure.Graph;
import DataStructure.LinkedList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.java.api.VisFx;
import main.java.graph.VisEdge;
import main.java.graph.VisGraph;
import main.java.graph.VisNode;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.jar.JarFile;

public class SubStage {
    private Stage mainStage;
    private Stage stage;

    private File file;
    private Graph<String> graph;
    private VisGraph visGraph;

    private VBox layout;
    private ListView<String> listView;
    private LinkedList<String> clasesList;
    private LinkedList<String> jars;
    private Analizer analizer;

    public SubStage(LinkedList<String> jars, File file, Stage mainStage, Analizer analizer){
        this.jars = jars;
        this.file = file;
        this.mainStage = mainStage;
        this.analizer = analizer;
        stage = new Stage();
        stage.setResizable(false);
        layout = new VBox();
        layout.setPadding(new Insets(20, 20, 20, 20));

        Scene scene = new Scene(layout, 350, 400);
        stage.setTitle("Zoom in");
        stage.setScene(scene);
        stage.show();

        createListView();
        createButtons();
    }

    private VisGraph getVisGraph(){
        return visGraph;
    }

    private void createButtons(){
        Button button = new Button("Seleccionar");
        button.setOnAction(e -> {
            zoomIn(listView.getSelectionModel().getSelectedItem());
            generateClassGraph();
            VisFx.graphNetwork(getVisGraph(), mainStage);
        });

        Button regresar = new Button("Regresar");
        regresar.setOnAction(e -> {
            VisFx.graphNetwork(analizer.getVisualGraph(), mainStage);
        });

        layout.getChildren().addAll(button, regresar);
    }

    private void createListView(){
        listView = new ListView<>();
        for (int i = 0; i < jars.length(); i++) {
            listView.getItems().addAll(jars.get(i));
        }
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        layout.getChildren().add(listView);
    }

    private void zoomIn(String text){
        text = text.replace(".", "/") + "/";
        try{
            graph = new Graph<>();
            clasesList = new LinkedList<>();
            JarFile jarFile = new JarFile(file);
            Enumeration enumeration = jarFile.entries();
            while (enumeration.hasMoreElements()){
                int error = 0;
                String direccion = enumeration.nextElement().toString();
                if (direccion.startsWith(text)){
                    direccion = direccion.replace(text, "");

                    for (int i = 0; i < direccion.length(); i++) {
                        if (direccion.charAt(i) == '/'){
                            error = 1;
                            break;
                        }
                    }
                    if (error == 1){
                        continue;
                    }
                    if (direccion.endsWith(".class"))
                        clasesList.add(direccion);
                }
            }
//            for (int i = 0; i < clasesList.length(); i++) {
//                System.out.println(clasesList.get(i));
//            }

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void generateClassGraph(){
        try{
            graph = new Graph<>();
            visGraph = new VisGraph();
            JarFile jarFile = new JarFile(file);
//            Enumeration enumeration = jarFile.entries();
//            while (enumeration.hasMoreElements()){
//
//                System.out.println(enumeration.nextElement());
//            }
            System.out.println(clasesList.length());

            for (int i = 0; i < clasesList.length(); i++) {
                VisNode node1 = new VisNode(i, clasesList.get(i));

                graph.addVertex(clasesList.get(i));
                if ((i + 1) == clasesList.length()){
                    VisNode node2 = new VisNode(0, clasesList.get(0));
                    VisEdge visEdge = new VisEdge(node1, node2, "to", "");

                    visGraph.addNodes(node1, node2);
                    visGraph.addEdges(visEdge);
                    graph.addEdge(clasesList.get(i), clasesList.get(0));
                }
                else{
                    graph.addVertex(clasesList.get(i  + 1));
                    VisNode node2 = new VisNode(i + 1, clasesList.get(i + 1));
                    VisEdge visEdge = new VisEdge(node1, node2, "to", "");

                    visGraph.addNodes(node1, node2);
                    visGraph.addEdges(visEdge);
                    graph.addEdge(clasesList.get(i), clasesList.get(i + 1));
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
