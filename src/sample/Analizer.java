package sample;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Analizer extends JarFile {

    private JarEntry classpath;

    public Analizer(String path) throws IOException {
        super(new File(path));
    }

    public Analizer(File file) throws IOException {
        super(file);
        classpath = this.getJarEntry(".classpath");
        try {
            System.out.println(classpath.getName());
            System.out.println(classpath.getAttributes());
            System.out.println(Arrays.toString(classpath.getCertificates()));
            System.out.println(Arrays.toString(classpath.getCodeSigners()));
        } catch (NullPointerException e){
            System.out.print("null");
        }
    }

    public void printData() throws IOException{

    }
}
