/*
 * Copyright (C) 2011 Parttimenerd
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ljbos.filesystem;

/**
 *
 * @author Parttimenerd
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
