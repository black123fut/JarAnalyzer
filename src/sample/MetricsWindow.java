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
    private VBox layout;
    private Graph<String> graph;

    public MetricsWindow(Graph<String> graph){
        subStage = new Stage();
        subStage.setTitle("Metricas");
        this.graph = graph;

        layout = new VBox();
        layout.setPadding(new Insets(20, 20, 20, 20));
        Scene scene = new Scene(layout, 450, 500);
        subStage.setScene(scene);
        subStage.show();

        createLisView();
        createRanking();
    }

    @SuppressWarnings("Duplicates")
    private void createLisView() {
        Label grado = new Label("Grados");
        grado.setFont(Font.font("Arial", 14));

        ListView<String> listView1 = new ListView<>();
        listView1.getItems().addAll("Nodo                                   Entrante       Saliente");

        LinkedList<String> keys = graph.getList();
        for (int i = 0; i < keys.length(); i++) {
            StringBuilder key = new StringBuilder(keys.get(i));
            System.out.println(key.length());
            if (key.length() < 42){
                while (key.toString().length() < 42){
                    key.append(' ');
                }
                System.out.println(key.length() + " dentro");
            }

            if (keys.get(i).equals("")){
                System.out.println("break");
                continue;
            }


            listView1.getItems().addAll(key.toString() + graph.getNode(keys.get(i)).getEntrante() +
                            "                " + graph.getNode(keys.get(i)).getSaliente());
        }

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
        layout.getChildren().addAll(grado, listView1);
    }

    @SuppressWarnings("Duplicates")
    public void createRanking(){
        Label rankingText = new Label("Cantidad de dependencias en jars (Ranking)");
        LinkedList<GraphNode<String>> nodesList = graph.getNodeList();

        for (int i = 0; i < nodesList.length(); i++) {
            System.out.println(nodesList.get(i).getSaliente());
        }
        LinkedList<GraphNode<String>> newlist = new LinkedList<>();
        for (int i = 0; i < nodesList.length(); i++) {
            if (nodesList.get(i).getSaliente() == 0)
                continue;
            newlist.add(nodesList.get(i));
        }

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

}
