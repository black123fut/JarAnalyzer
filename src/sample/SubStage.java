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
    private int index = 0;

    private File file;
    private Graph<String> graph;
    private Graph<String> mainGraph;
    private VisGraph visGraph;

    private VBox layout;
    private ListView<String> listView;
    private LinkedList<String> clasesList;
    private LinkedList<String> jars;
    private Analizer analizer;

    /**
     * Constructor.
     * @param jars Lista de jars.
     * @param file Archivo para el grafo de clses.
     * @param mainStage Stage principal de la aplicacion.
     * @param analizer Analizador principal.
     * @param mainGraph grafo principal.
     */
    public SubStage(LinkedList<String> jars, File file, Stage mainStage, Analizer analizer, Graph<String> mainGraph){
        this.jars = jars;
        this.file = file;
        this.mainGraph = mainGraph;
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

    /**
     * Crea botones.
     */
    private void createButtons(){
        //Boton para seleccionar el jar el cual se quieren ver las clases.
        Button button = new Button("Seleccionar");
        button.setOnAction(e -> {
            index = 1;
            zoomIn(listView.getSelectionModel().getSelectedItem());
            generateClassGraph();
            VisFx.graphNetwork(getVisGraph(), mainStage);
        });

        //Regresa al grafo principal.
        Button regresar = new Button("Regresar");
        regresar.setOnAction(e -> {
            index = 0;
            VisFx.graphNetwork(analizer.getVisualGraph(), mainStage);
        });

        //Muestra las metricas del grafo.
        Button metricas = new Button("Metricas");
        metricas.setOnAction(e -> {
            if (index == 0){
                new MetricsWindow(mainGraph, 0);
            }
            if (index == 1){
                new MetricsWindow(graph, 1);
            }

        });

        layout.getChildren().addAll(button, regresar, metricas);
    }

    /**
     * Crea la listView que muestra los jars.
     */
    private void createListView(){
        listView = new ListView<>();
        for (int i = 0; i < jars.length(); i++) {
            listView.getItems().addAll(jars.get(i));
        }
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        layout.getChildren().add(listView);
    }

    /**
     * Busca las clases de un jar.
     * @param text Jar del ual busca las clases.
     */
    private void zoomIn(String text){
        text = text.replace(".", "/") + "/";
        try{
            graph = new Graph<>();
            clasesList = new LinkedList<>();
            JarFile jarFile = new JarFile(file);
            Enumeration enumeration = jarFile.entries();
            //Abre los archivos de una ruta.
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
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Genera el grafo de clases.
     */
    public void generateClassGraph(){
        graph = new Graph<>();
        visGraph = new VisGraph();

        //Hace virtual el grafo de clases.
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
    }
}
