package main.java.api;


import javafx.application.Platform;
import javafx.stage.Stage;
import main.java.graph.VisGraph;
import main.java.gui.GraphView;

public class VisFx{

    /**
     * Plots the given graph to the mainStage.
     * @param graph the network graph to be plotted.
     * @param mainStage the main Stage.
     */
    public static void graphNetwork(VisGraph graph , Stage mainStage){
        GraphView graphView = new GraphView(graph);
        Platform.runLater(() -> graphView.start(mainStage));
    }

}
