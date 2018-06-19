package sample;

import DataStructure.Graph;

import DataStructure.LinkedList;
import com.sun.org.apache.xml.internal.security.signature.ReferenceNotInitializedException;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
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


    public Analizer(String path) throws IOException {
        super(new File(path));
        this.file = new File(path);
        graph = new Graph<>();
        visualGraph = new VisGraph();
    }

    public Analizer(File file) throws Exception {
        super(file);
        this.file = file;
        graph = new Graph<>();
        visualGraph = new VisGraph();
    }

    public void generateGraph2(){
        try{
            LinkedList<String> jarsList = getJars();
            LinkedList<String> dependenciesList = getDependencies();
            jars = jarsList;

            LinkedList<VisNode> jarNodes = new LinkedList<>();
            for (int i = 0; i < jarsList.length(); i++) {
                jarNodes.add(new VisNode(i, "Jar: \n" + jarsList.get(i)));
            }
            for (int i = 0; i < jarNodes.length(); i++) {
                visualGraph.addNodes(jarNodes.get(i));
            }

            LinkedList<VisNode> dependencesNodes = new LinkedList<>();
            int index = jarsList.length(), num = 0;
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


            for (int i = 0; i < dependencesNodes.length(); i++) {
                visualGraph.addNodes(dependencesNodes.get(i));
            }

            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec("jdeps " + file.getPath());
            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(proc.getInputStream()));
            String s;
            while((s = stdInput.readLine()) != null){
                int vertex1 = 0, vertex2 = 0;
                String dependence = getDependence(s);
                String jar = getJar(s);
                graph.addVertex(jar);
                graph.addVertex(dependence);
                graph.addEdge(jar, dependence);

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

            System.out.println("Conexo:    " + graph.isRelated());

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void classes(){
        try{
            JarFile jarFile = new JarFile(file);
            Enumeration enumeration = jarFile.entries();
            while (enumeration.hasMoreElements()){
                System.out.println(enumeration.nextElement());
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

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

    public void generateGraph(){
        try {
            JarFile jarFile = new JarFile(file);

            //Dependemcias
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec("jdeps " + file.getPath());

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(proc.getInputStream()));

            // read the output from the command

            String s, jar = "";
            int i = 0;
            graph.addVertex(file.getName());
//            VisNode centralNode = new VisNode(1, file.getName());
//            visualGraph.addNodes(centralNode);
            VisNode centralNode = null;
            while ((s = stdInput.readLine()) != null) {
                if (!getJar(s).equals(jar)){
                    centralNode = new VisNode(i++, getJar(s));
                    visualGraph.addNodes(centralNode);
                    graph.addVertex(getJar(s));
                }

                jar = getJar(s);
                String dependence = getDependence(s);

                if (graph.addVertex(dependence)){
                    graph.addEdge(jar, dependence);
                    VisNode dependenceNode = new VisNode(i++, dependence);

                    VisEdge edge = new VisEdge(centralNode, dependenceNode, "to", "");

                    visualGraph.addNodes(dependenceNode);
                    visualGraph.addEdges(edge);
                }

//                if (!s.equals("")){
//                    int error = 0;
//                    for (int i = 0; i < dependencies.length(); i++) {
//                        if (s.equals(dependencies.get(i))){
//                            error = 1;
//                            break;
//                        }
//                    }
//                    if (error == 1)
//                        continue;
//                    dependencies.add(s);
//                }
            }

//            for (int i = 0; i < dependencies.length(); i++) {
//                graph.addVertex(dependencies.get(i));
//                graph.addEdge(file.getName(), dependencies.get(i));
//
//                VisNode dependenceNode = new VisNode(i + 2, dependencies.get(i));
//                VisEdge edge = new VisEdge(centralNode, dependenceNode, "to", "");
//
//                visualGraph.addNodes(dependenceNode);
//                visualGraph.addEdges(edge);
//            }

//            graph.addVertex(file.getName());
//            VisNode centralNode = new VisNode(1, file.getName());
//            visualGraph.addNodes(centralNode);
//            for (int i = 0; i < dependencies.length(); i++) {
//                graph.addVertex(dependencies.get(i));
//                graph.addEdge(file.getName(), dependencies.get(i));
//
//                VisNode dependenceNode = new VisNode(1, dependencies.get(i));
//                VisEdge edge = new VisEdge(centralNode, dependenceNode, "", "");
//
//                visualGraph.addNodes(dependenceNode);
//                visualGraph.addEdges(edge);
//            }

//            Obtiene los archivos dentro del jar


        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public LinkedList<String> getJarsList(){
        return jars;
    }

    public VisGraph getVisualGraph(){
        return visualGraph;
    }

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

    public Graph<String> getGraph(){
        return graph;
    }

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