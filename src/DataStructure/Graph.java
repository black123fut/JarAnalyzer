package DataStructure;

import java.util.HashMap;
import java.util.Map;

public class Graph<T> {
    private int errorState = 0;
    private LinkedList<T> list;
    private LinkedList<GraphNode<T>> nodeList;
    private Map<T, GraphNode<T>> adjacencyList;

    public Graph(){
        this.adjacencyList = new HashMap<>();
    }

    public boolean addVertex(T vertex){
        if ( adjacencyList.containsKey(vertex)){
            return false;
        }
        adjacencyList.put(vertex, new GraphNode<>(vertex));
        return true;
    }

    public boolean addEdge(T vertex1, T vertex2){
        return addEdge(vertex1, vertex2, 0);
    }

    public boolean addEdge(T vertex1, T vertex2, int weight){
        if (!containsVertex(vertex1) || !containsVertex(vertex2)){
            throw new RuntimeException("Vertex does not exit");
        }
        GraphNode<T> node1 = getNode(vertex1);
        GraphNode<T> node2 = getNode(vertex2);
        node2.setEntrante(node2.getEntrante() + 1);
        return node1.addEdge(node2, weight);
    }

    public boolean removeVertex(T vertex){
        if (!adjacencyList.containsKey(vertex)){
            return false;
        }
        final GraphNode<T> toRemove = getNode(vertex);

        adjacencyList.values().forEach(node -> node.removeEdge(toRemove));
        adjacencyList.remove(vertex);
        return true;
    }

    public boolean removeEdge(T vertex1, T vertex2){
        if (!containsVertex(vertex1) || !containsVertex(vertex2)){
            return false;
        }
        return getNode(vertex1).removeEdge(getNode(vertex2));
    }

    public int vertexCount(){
        return adjacencyList.keySet().size();
    }

    public int edgeCount(){
        return adjacencyList.values()
                .stream()
                .mapToInt(GraphNode::getSaliente)
                .sum();
    }

    public boolean containsVertex(T vertex){
        return adjacencyList.containsKey(vertex);
    }

    public boolean containsEdge(T vertex1, T vertex2){
        if (!containsVertex(vertex1) || !containsVertex(vertex2)){
            return false;
        }
        return getNode(vertex1).hasEdge(getNode(vertex2));
    }

    public LinkedList<T> getList(){
        list = new LinkedList<>();
        adjacencyList.keySet().forEach(key -> {
        list.add(key);
        });
        return list;
    }

    public LinkedList<GraphNode<T>> getNodeList(){
        nodeList = new LinkedList<>();
        adjacencyList.keySet().forEach(key -> {
            GraphNode<T> node = getNode(key);
            nodeList.add(node);
        });
        return nodeList;
    }

    public GraphNode<T> getNode(T value){
        return adjacencyList.get(value);
    }

    private void resetGraph(){
        adjacencyList.keySet().forEach(key -> {
            GraphNode<T> node = getNode(key);
            node.setParent(null);
            node.setVisited(false);
        });
    }

    public boolean isRelated(){



        adjacencyList.keySet().forEach(key -> {
            GraphNode<T> node = getNode(key);
            System.out.println(key.toString() + "     " + node.getSaliente());
            if (!(node.getSaliente() > 0)){
                errorState = 1;
            }
        });
        if (errorState == 1){
            errorState = 0;
            return false;
        }
        else
            return true;
    }
}




















