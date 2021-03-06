package sample;

import DataStructure.Graph;

import DataStructure.LinkedList;
import main.java.graph.VisEdge;
import main.java.graph.VisGraph;
import main.java.graph.VisNode;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;

import java.util.jar.JarFile;

public class Analizer extends JarFile {
    private File file;
    private VisGraph visualGraph;
    private Graph<String> graph;
    private LinkedList<String> jars;

    /**
     * Constructor.
     * @param path Ruta del jar.
     */
    public Analizer(String path) throws IOException {
        super(new File(path));
        this.file = new File(path);
        graph = new Graph<>();
        visualGraph = new VisGraph();
    }

    /**
     * Constructor.
     * @param file Archivo que se va a analizar.
     */
    public Analizer(File file) throws Exception {
        super(file);
        this.file = file;
        graph = new Graph<>();
        visualGraph = new VisGraph();
    }

    /**
     * Genera el grafo del jar.
     */
    public void generateGraph2(){
        try{
            LinkedList<String> jarsList = getJars();
            LinkedList<String> dependenciesList = getDependencies();
            jars = jarsList;

            //Agrega los nodos dibujados a partir de la lista de jars.
            LinkedList<VisNode> jarNodes = new LinkedList<>();
            for (int i = 0; i < jarsList.length(); i++) {
                jarNodes.add(new VisNode(i, "Jar: \n" + jarsList.get(i)));
            }
            for (int i = 0; i < jarNodes.length(); i++) {
                visualGraph.addNodes(jarNodes.get(i));
            }

            LinkedList<VisNode> dependencesNodes = new LinkedList<>();
            int index = jarsList.length(), num = 0;
            //Agrega los nodos dibujados a partir de la lista de dependencias.
            outerloop:
            for (int i = 0; i < dependenciesList.length(); i++) {
                for (int j = 0; j < jarsList.length(); j++) {
                    if (jarsList.get(j).equals(dependenciesList.get(i))){
                        num++;
                        continue outerloop;
                    }
                }
                dependencesNodes.add(new VisNode(index, dependenciesList.get(num)));
                num++;
                index++;
            }
            //Agrega los nodos al grafo que se dibuja.
            for (int i = 0; i < dependencesNodes.length(); i++) {
                visualGraph.addNodes(dependencesNodes.get(i));
            }

            //Abre al consola.
            Runtime rt = Runtime.getRuntime();
            //Se introduce un comando en la consola.
            Process proc = rt.exec("jdeps " + file.getPath());
            //Lee el output de la consola.
            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(proc.getInputStream()));
            String s;
            while((s = stdInput.readLine()) != null){
                int vertex1 = 0, vertex2 = 0;
                String dependence = getDependence(s);
                String jar = getJar(s);

                //Crea el grafo abstracto.
                graph.addVertex(jar);
                graph.addVertex(dependence);
                graph.addEdge(jar, dependence);

                //Busca la manera de que se conecten los vertices y aristas de la mejor manera.
                for (int i = 0; i < jarNodes.length(); i++) {
                    if (jarNodes.get(i).getLabel().equals("Jar: \n" + jar))
                        vertex1 = i;
                }
                int error = 0;
                for (int i = 0; i < dependencesNodes.length(); i++) {
                    error = 0;
                    for (int j = 0; j < jarNodes.length(); j++) {
                        if (jarNodes.get(j).getLabel().equals("Jar: \n" + dependence)){
                            vertex2 = j;
                            error = 1;
                            break;
                        }
                    }
                    if (dependencesNodes.get(i).getLabel().equals(dependence)){
                        vertex2 = i;
                    }
                }
                //Conecta los vertices con aristas.
                VisEdge edge;
                if (error == 1){
                    edge = new VisEdge(jarNodes.get(vertex1), jarNodes.get(vertex2), "to",
                            "");
                }
                else{
                    edge = new VisEdge(jarNodes.get(vertex1), dependencesNodes.get(vertex2), "to",
                            "");
                }
                visualGraph.addEdges(edge);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Obtiene las dependencias del jar.
     * @return Una Lista con las dependencias.
     */
    @SuppressWarnings("Duplicates")
    private LinkedList<String> getDependencies(){
        LinkedList<String> list = null;
        try{
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec("jdeps " + file.getPath());
            list = new LinkedList<>();
            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(proc.getInputStream()));
            String s;
            while ((s = getDependence(stdInput.readLine())) != null){
                if (!s.equals("")){
                    int error = 0;
                    for (int i = 0; i < list.length(); i++) {
                        if (s.equals(list.get(i))){
                            error = 1;
                            break;
                        }
                    }
                    if (error == 1)
                        continue;
                    list.add(s);
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Obtiene un lista con los jar dentro del jar.
     * @return La lista de jars.
     */
    @SuppressWarnings("Duplicates")
    private LinkedList<String> getJars(){
        LinkedList<String> list = null;
        try{
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec("jdeps " + file.getPath());
            list = new LinkedList<>();
            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(proc.getInputStream()));
            String s;
            while ((s = getJar(stdInput.readLine())) != null){
                if (!s.equals("")){
                    int error = 0;
                    for (int i = 0; i < list.length(); i++) {
                        if (s.equals(list.get(i))){
                            error = 1;
                            break;
                        }
                    }
                    if (error == 1)
                        continue;
                    list.add(s);
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Obtiene la lista de jars.
     * @return Una lista de jars.
     */
    public LinkedList<String> getJarsList(){
        return jars;
    }

    /**
     * Obtiene el Grafo Dibujado.
     * @return Grafo dibujado.
     */
    public VisGraph getVisualGraph(){
        return visualGraph;
    }

    /**
     * Obtiene un jar.
     * @param data Renglon donde se busque el jar.
     * @return Una String con el jar.
     */
    private String getJar(String data){
        try {
            if (data != null){
                if (data.length() < 53)
                    data = "";
                data = data.substring(3, 53).trim();
            }
        } catch (StringIndexOutOfBoundsException e){
            data = "";
        }
        return data;
    }

    /**
     * Obtiene el grafo abstracto.
     * @return El grafo.
     */
    public Graph<String> getGraph(){
        System.out.println("Conexo? :     " +  graph.esConexo());
        return graph;
    }

    /**
     * Obtiene una dependencia.
     * @param data Renglon donde se buscara la dependencia.
     * @return Una String de la dependencia.
     */
    private String getDependence(String data){
        try {
            if (data != null){
                if (data.length() < 108)
                    data = "";
                data = data.substring(56, 108).trim();
            }
        } catch (StringIndexOutOfBoundsException e){
            data = "";
        }
        return data;
    }
}