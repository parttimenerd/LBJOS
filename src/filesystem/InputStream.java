/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package filesystem;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Johannes
 */
public class InputStream extends java.io.InputStream {
    
    private ArrayList<Cluster> clusters;
    private Cluster actual;
    private FileInputStream stream;
    
    public InputStream(String name) throws FileNotFoundException {
        MainFile file = FileSystem.getRoot().getFile(name);
        if (name == null || file == null || file.isDirectory()){
            throw new FileNotFoundException();
        }
        clusters = ((File)file).getClusters();
        stream = FileSystem.getInputStream();
        actual = clusters.isEmpty() ? null : clusters.get(0);
    }

    public InputStream(File file) throws FileNotFoundException {
	String name = (file != null ? file.getPath() : null);
        if (name == null) {
            throw new NullPointerException();
        }
        clusters = file.getClusters();
        actual = clusters.isEmpty() ? null : clusters.get(0);
        stream = FileSystem.getInputStream();
    }
    
    public int read() throws IOException {
        if (actual == null){
            return -1;
        }
        if (actual.getPos() >= actual.getLength()){
            if (actual.getNextCluster() != null){
                actual.resetPos();
                actual = actual.getNextCluster();
            } else {
                actual = null;
                return -1;
            }
        }
        stream.read();
        return 0;
    }

    private int readBytes(byte b[], int off, int len) throws IOException {
        return 0;
    }

    public int read(byte b[]) throws IOException{
	return readBytes(b, 0, b.length);
    }

    public int read(byte b[], int off, int len) throws IOException {
	return readBytes(b, off, len);
    }

    public native long skip(long n) throws IOException;
    
    public int avaliable() throws IOException{
        return stream.available();
    }

    public void close() throws IOException {
        stream.close();
    }

    protected void finalize() throws IOException {
	close();
    }
}
