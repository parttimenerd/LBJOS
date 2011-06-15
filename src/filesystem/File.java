/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package filesystem;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

/**
 *
 * @author Johannes
 */
public class File extends MainFile {
    
    private byte[] data;
    private ArrayList<Cluster> clusters = new ArrayList<Cluster>();
    
    public File(String name, Directory parent, String owner){
        super(name, parent, owner);
    }

    @Override
    public boolean isEmpty(){
        return data == null || data.length == 0;
    }
    
    @Override
    public int size(){
        return data.length;
    }
    
    /**
     * @return the data
     */
    public byte[] getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(byte[] data) {
        this.data = data;
        setLastModifiedTime();
    }
    
    public void clear(){
        data = null;
    }

    @Override
    public boolean isDirectory() {
        return false;
    }

    @Override
    public MainFile getFile(String filename) {
        if (filename == parent.getPath() + "/" + name){
            return this;
        }
        return null;
    }
    
    public ByteArrayInputStream getInputStream(){
        return new ByteArrayInputStream(data);
    }
    
    public OutputStream getOutputputStream(){
        return new OutputStream();
    }

    public ArrayList<Cluster> getClusters() {
        return clusters;
    }

    /**
     * @param clusters the clusters to set
     */
    public void setClusters(ArrayList<Cluster> clusters) {
        this.clusters = clusters;
    }
    
    public class OutputStream extends java.io.ByteArrayOutputStream {
        public OutputStream(){
            buf = data;
        }
    }
}
