package sample;

import DataStructure.Graph;
import DataStructure.GraphNode;
import DataStructure.LinkedList;
import DataStructure.Node;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MetricsWindow {
    private Stage subStage;
    private int index;
    private VBox layout;
    private Graph<String> graph;

    /**
     * Constructor.
     * @param graph Grafo al que se le sacan las mertricas.
     * @param index Tipo de grafo.
     */
    public MetricsWindow(Graph<String> graph, int index){
        subStage = new Stage();
        subStage.setTitle("Metricas");
        this.graph = graph;
        this.index = index;

        layout = new VBox();
        layout.setPadding(new Insets(20, 20, 20, 20));
        Scene scene = new Scene(layout, 475, 500);
        subStage.setScene(scene);
        subStage.show();

        createLisView();
        createRanking();
        createJarRanking();
    }

    /**
     * Crea la listView de los Grados.
     */
    @SuppressWarnings("Duplicates")
    private void createLisView() {
        Label grado = new Label("Grados");
        grado.setFont(Font.font("Arial", 14));

        Label conexo = null;
        if (index == 0){
            if (graph.esConexo())
                conexo = new Label("El grafo de jars es conexo");
            else
                conexo = new Label("El grafo de jars es no conexo");
        }
        if (index == 1){
            if (graph.esConexo())
                conexo = new Label("El grafo de clases es conexo");
            else
                conexo = new Label("El grafo de clases es no conexo");
        }

        ListView<String> listView1 = new ListView<>();
        listView1.getItems().addAll("Nodo                                   Entrante       Saliente");

        LinkedList<String> keys = graph.getList();
        //Hace que se vean bien los jar
        for (int i = 0; i < keys.length(); i++) {
            int error = 0;
            StringBuilder key = new StringBuilder(keys.get(i));
            String key2 = keys.get(i);
            System.out.println(key.length());
            if (key.length() < 42){
                while (key.toString().length() < 42){
                    key.append(' ');
                }
                System.out.println(key.length() + " dentro");
            } else{
                error = 1;
                key2 = key2.substring(0, 38);
            }

            if (keys.get(i).equals("")){
                System.out.println("break");
                continue;
            }

            if (error == 0){
                listView1.getItems().addAll(key.toString() + graph.getNode(keys.get(i)).getEntrante() +
                        "                " + graph.getNode(keys.get(i)).getSaliente());
            } else{
                listView1.getItems().addAll(key2 + graph.getNode(keys.get(i)).getEntrante() +
                        "                " + graph.getNode(keys.get(i)).getSaliente());
            }
        }
        //Cambia la fuente del listView
        listView1.setCellFactory(cell -> {
            return new ListCell<String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null) {
                        setText(item);

                        // decide to add a new styleClass
                        // getStyleClass().add("costume style");
                        // decide the new font size
                        setFont(Font.font("Courier", 11));
                    }
                }
            };
        });
        layout.getChildren().addAll(conexo, grado, listView1);
    }

    /**
     * Crea el ranking de dependencias.
     */
    @SuppressWarnings("Duplicates")
    public void createRanking(){
        Label rankingText = null;
        if (index == 0){
            rankingText = new Label("Cantidad de dependencias en jars (Ranking)");
        }
        if (index == 1){
            rankingText = new Label("Cantidad de dependencias entre clases (Ranking)");
        }
        LinkedList<GraphNode<String>> nodesList = graph.getNodeList();

        //Agrega solo los nodos que son jar.
        LinkedList<GraphNode<String>> newlist = new LinkedList<>();
        for (int i = 0; i < nodesList.length(); i++) {
            if (nodesList.get(i).getSaliente() == 0)
                continue;
            newlist.add(nodesList.get(i));
        }

        //Bubblesort
        for (int i = 0; i < newlist.length() - 1; i++) {
            for (int j = 0; j < newlist.length() - 1 - i; j++) {
                if (newlist.get(j).getSaliente() < newlist.get(j + 1).getSaliente()){
                    //Nodo del alien con mas resistencia
                    Node<GraphNode<String>> node1 = newlist.getNode(j);
                    //Nodo del alien mas debil
                    Node<GraphNode<String>> node2 = newlist.getNode(j + 1);
                    //Copia del nodo del alien mas resistente
                    Node<GraphNode<String>> tmp = new Node<>(node1.getData());

                    //Cambia los datos
                    node1.setData(node2.getData());
                    node2.setData(tmp.getData());
                }
            }
        }

        //Acomoda la lista  de jars en un ranking de mayor a menor.
        ListView<String> listView1 = new ListView<>();
        listView1.getItems().addAll("Jar                                    #Dependencias");
        for (int i = 0; i < newlist.length(); i++) {
            StringBuilder key = null;
            if (!newlist.get(i).getVertex().equals("")){
                key = new StringBuilder(newlist.get(i).getVertex());
                System.out.println(key.length());
                if (key.length() < 42){
                    while (key.toString().length() < 42){
                        key.append(' ');
                    }
                }
            }
            if (key != null)
                listView1.getItems().addAll( "#" + (i + 1) + "  " + key.toString() +
                        newlist.get(i).getSaliente());
        }

        //Cambia el fondo del listView.
        listView1.setCellFactory(cell -> {
            return new ListCell<String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null) {
                        setText(item);

                        // decide to add a new styleClass
                        // getStyleClass().add("costume style");
                        // decide the new font size
                        setFont(Font.font("Courier", 11));
                    }
                }
            };
        });
        layout.getChildren().addAll(rankingText, listView1);
    }

    /**
     * Crea el Ranking de dependencias con otros jar.
     */
    @SuppressWarnings("Duplicates")
    public void createJarRanking(){
        Label label = null;
        if (index == 0)
            label = new Label("Cantidad de referencias por otros jar (Ranking)");
        if (index == 1)
            label = new Label("Cantidad de referencias por otras clases (Ranking)");

        LinkedList<GraphNode<String>> lista = graph.getNodeList();

        //Solo jars
        LinkedList<GraphNode<String>> newlist = new LinkedList<>();
        for (int i = 0; i < lista.length(); i++) {
            if (lista.get(i).getSaliente() == 0)
                continue;
            newlist.add(lista.get(i));
        }
        //Aristas con otros jar
        LinkedList<GraphNode<String>> nodes = new LinkedList<>();
        for (int i = 0; i < newlist.length(); i++) {
            for (int j = 0; j < newlist.length(); j++) {
                if (newlist.get(j).hasEdge(newlist.get(i))){
                    newlist.get(i).setJarEntrante(newlist.get(i).getJarEntrante() + 1);
                }
            }
        }

        for (int i = 0; i < newlist.length() - 1; i++) {
            for (int j = 0; j < newlist.length() - 1 - i; j++) {
                if (newlist.get(j).getJarEntrante() < newlist.get(j + 1).getJarEntrante()){
                    //Nodo con mas entradas
                    Node<GraphNode<String>> node1 = newlist.getNode(j);
                    //Nodo con menos entradas
                    Node<GraphNode<String>> node2 = newlist.getNode(j + 1);
                    //Copia  del Nodo con mas entradas.
                    Node<GraphNode<String>> tmp = new Node<>(node1.getData());

                    //Cambia los datos
                    node1.setData(node2.getData());
                    node2.setData(tmp.getData());
                }
            }
        }

        //Acomoda la lista en el listview.
        ListView<String> listView1 = new ListView<>();
        listView1.getItems().addAll("Jar                                    #Dependencias");
        for (int i = 0; i < newlist.length(); i++) {
            StringBuilder key = null;
            if (!newlist.get(i).getVertex().equals("")){
                key = new StringBuilder(newlist.get(i).getVertex());
                System.out.println(key.length());
                if (key.length() < 42){
                    while (key.toString().length() < 42){
                        key.append(' ');
                    }
                }
            }
            if (key != null)
                listView1.getItems().addAll( "#" + (i + 1) + "  " + key.toString() +
                        newlist.get(i).getJarEntrante());
        }

        //Cambia la fuente del listView.
        listView1.setCellFactory(cell -> {
            return new ListCell<String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null) {
                        setText(item);

                        // decide to add a new styleClass
                        // getStyleClass().add("costume style");
                        // decide the new font size
                        setFont(Font.font("Courier", 11));
                    }
                }
            };
        });
        layout.getChildren().addAll(label, listView1);
    }
}
