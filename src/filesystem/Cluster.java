/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package filesystem;

/**
 *
 * @author Johannes
 */
public class Cluster {
    
    private Cluster nextCluster;
    private int cluster;
    private int length;
    private int pos = 0;
    private File file;
    
    public Cluster(int cluster, int length, File file, Cluster previousCluster){
        this.cluster = cluster;
        this.length = length;
        this.file = file;
        if (previousCluster != null){
            previousCluster.setNextCluster(this);
        }
        FileSystem.addCluster(this);
    }
    
    public void resetPos(){
        pos = 0;
    }
    
    /**
     * @return the nextCluster
     */
    public Cluster getNextCluster() {
        return nextCluster;
    }

    /**
     * @param nextCluster the nextCluster to set
     */
    public void setNextCluster(Cluster nextCluster) {
        this.nextCluster = nextCluster;
    }

    /**
     * @return the cluster
     */
    public int getCluster() {
        return cluster;
    }

    /**
     * @param cluster the cluster to set
     */
    public void setCluster(int cluster) {
        this.cluster = cluster;
    }

    /**
     * @return the length
     */
    public int getLength() {
        return length;
    }

    /**
     * @param length the length to set
     */
    public void setLength(int length) {
        this.length = length;
    }

    /**
     * @return the pos
     */
    public int getPos() {
        return pos;
    }

    public void inkrementPos(int i){
        pos += i;
    }
    
    /**
     * @param pos the pos to set
     */
    public void setPos(int pos) {
        this.pos = pos;
    }

    /**
     * @return the file
     */
    public File getFile() {
        return file;
    }
    
}
