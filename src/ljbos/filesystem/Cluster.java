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

import java.io.IOException;

/**
 *
 * @author Parttimenerd
 */
public class Cluster {
    
    private Cluster nextCluster;
    private int offset;
    private int length;
    private File file;
    private byte[] data;
    
    public Cluster(int offset, int length, File file){
        this.offset = offset;
        this.length = length;
        data = new byte[length];
        this.file = file;
    }
    
    public Cluster(int offset, int length, File file, Cluster previousCluster){
        this(offset, length, file);
        if (previousCluster != null){
            previousCluster.setNextCluster(this);
        }
    }
    
    public void read() throws IOException{
        FileSystem.getSource().read(data, offset, length);
    }
    
    public void write() throws IOException{
        FileSystem.getSource().write(data, offset, length);
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

    public int getOffset() {
        return offset;
    }

    /**
     * @param cluster the cluster to set
     */
    public void setOffset(int offset) {
        this.offset = offset;
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
     * @return the file
     */
    public File getFile() {
        return file;
    }
 
    public int size(){
        return length;
    }

    public void setData(byte[] data){
        this.data = data;
    }
    
    public byte[] getData() {
        return data;
    }
}
