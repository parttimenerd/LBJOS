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

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

/**
 *
 * @author Parttimenerd
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
