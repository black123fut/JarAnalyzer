package DataStructure;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GraphNode<T> {
    private T vertex;
    private List<GraphEdge<T>> edges;
    private int entrante;
    private int jarEntrante;
    private GraphNode<T> parent;
    private boolean isVisited;

    public GraphNode(T vertex){
        this.vertex = vertex;
        this.edges = new ArrayList<>();
    }

    public T getVertex(){
        return vertex;
    }

    public boolean addEdge(GraphNode<T> node, int weight){
        if (hasEdge(node)){
            return false;
        }

        GraphEdge<T> newEdge = new GraphEdge<>(this, node, weight);
        return edges.add(newEdge);
    }

    public boolean removeEdge(GraphNode<T> node){
        Optional<GraphEdge<T>> optional = findEdge(node);
        if (optional.isPresent())
            return edges.remove(optional.get());

        return false;
    }

    public boolean hasEdge(GraphNode<T> node){
        return findEdge(node).isPresent();
    }

    public Optional<GraphEdge<T>> findEdge(GraphNode<T> node){
        return edges.stream()
                .filter(edge -> edge.isBetween(this, node))
                .findFirst();
    }

    public List<GraphEdge<T>> getEdges(){
        return edges;
    }

    public int getSaliente(){
        return edges.size();
    }

    public void setEntrante(int entrante){
        this.entrante = entrante;
    }

    public int getEntrante(){
        return entrante;
    }

    public void setJarEntrante(int jarEntrante){
        this.jarEntrante = jarEntrante;
    }

    public int getJarEntrante(){
        return jarEntrante;
    }

    public GraphNode<T> getParent(){
        return parent;
    }

    public boolean isVisited() {
        return isVisited;
    }

    public void setVisited(boolean isVisited) {
        this.isVisited = isVisited;
    }

    public void setParent(GraphNode<T> parent) {
        this.parent = parent;
    }


}
