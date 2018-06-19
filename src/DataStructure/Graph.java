package DataStructure;

import java.util.HashMap;
import java.util.Map;

public class Graph<T> {
    private int errorState = 0;
    private LinkedList<T> list;
    private LinkedList<GraphNode<T>> nodeList;
    private Map<T, GraphNode<T>> adjacencyList;

    /**
     * Constructor.
     */
    public Graph(){
        this.adjacencyList = new HashMap<>();
    }

    /**
     * Agrega un vertice al Map.
     * @param vertex Data que se guardara en el vertice.
     * @return true si se agrego y false si no lo agrego.
     */
    public boolean addVertex(T vertex){
        if ( adjacencyList.containsKey(vertex)){
            return false;
        }
        adjacencyList.put(vertex, new GraphNode<>(vertex));
        return true;
    }

    /**
     * Agrega una arista.
     * @param vertex1 Vertice donde cmomienza.
     * @param vertex2 Vertice donde termina.
     * @return true si se agrego y false si no lo agrego.
     */
    public boolean addEdge(T vertex1, T vertex2){
        return addEdge(vertex1, vertex2, 0);
    }

    /**
     * Agrega una arista.
     * @param vertex1 Vertice donde cmomienza.
     * @param vertex2 Vertice donde termina.
     * @param weight Peso de la arista.
     * @return true si se agrego y false si no lo agrego.
     */
    public boolean addEdge(T vertex1, T vertex2, int weight){
        if (!containsVertex(vertex1) || !containsVertex(vertex2)){
            throw new RuntimeException("Vertex does not exit");
        }
        GraphNode<T> node1 = getNode(vertex1);
        GraphNode<T> node2 = getNode(vertex2);
        node2.setEntrante(node2.getEntrante() + 1);
        return node1.addEdge(node2, weight);
    }

    public int vertexCount(){
        return adjacencyList.keySet().size();
    }

    /**
     * Cantidad de aristas.
     * @return La cantidad de aristas.
     */
    public int edgeCount(){
        return adjacencyList.values()
                .stream()
                .mapToInt(GraphNode::getSaliente)
                .sum();
    }

    /**
     * Verifica si contiene el vertice.
     * @param vertex Vertice que se analizara.
     * @return  true si se agrego y false si no lo agrego.
     */
    public boolean containsVertex(T vertex){
        return adjacencyList.containsKey(vertex);
    }

    /**
     * Obtiene una lista con la informacion de los nodos.
     * @return Una lista.
     */
    public LinkedList<T> getList(){
        list = new LinkedList<>();
        adjacencyList.keySet().forEach(key -> {
        list.add(key);
        });
        return list;
    }

    /**
     * Obtiene una lista con los nodos.
     * @return Una lista.
     */
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

    /**
     * Verifica si es conexo el grafo.
     * @return True si es conexo y falso si no es convexo.
     */
    public boolean esConexo(){
        LinkedList<T> lista = new LinkedList<>();
        LinkedList<T> nodes = getList();
        for (int i = 0; i < vertexCount(); i++) {
            GraphNode<T> tmp = getNode(nodes.get(i));
            if (tmp.getEdges().size() > 0){
                if (!lista.contains(tmp.getVertex())){
                    if (lista.length() == 0)
                        esConexo(tmp, lista);
                    else
                        esConexo1(tmp, lista);
                }
            }
        }
        return lista.length() == vertexCount();
    }

    private void esConexo(GraphNode<T> node, LinkedList<T> lista){
        if (!lista.contains(node.getVertex())){
            lista.add(node.getVertex());
        }
        for (int i = 0; i < node.getEdges().size(); i++) {
            GraphEdge<T> edge = node.getEdges().get(i);
            if (!(edge.fromNode() == (node)) || !(edge.toNode() == (node))){
                if (!lista.contains(edge.fromNode().getVertex())){
                    esConexo(edge.fromNode(), lista);
                }
                else if (!lista.contains(edge.toNode().getVertex())){
                    esConexo(edge.toNode(), lista);
                }
            }
        }
    }

    private void esConexo1(GraphNode<T> node, LinkedList<T> lista){
        for (int i = 0; i < node.getEdges().size(); i++) {
            GraphEdge<T> tmp = node.getEdges().get(i);
            if (lista.contains(tmp.fromNode().getVertex()) || lista.contains(tmp.toNode().getVertex())){
                esConexo(node, lista);
                break;
            }
        }
    }
}