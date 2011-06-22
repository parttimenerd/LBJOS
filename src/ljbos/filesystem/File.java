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
    
    //private byte[] data;
    private ArrayList<Cluster> clusters = new ArrayList<Cluster>();
    private Cluster last;
    
    public File(String name, Directory parent, String owner){
        super(name, parent, owner);
    }

    @Override
    public boolean isEmpty(){
        return clusters.size() < 0;
    }
    
    @Override
    public int size(){
        int length = 0;
        for (int i = 0; i < clusters.size(); i++) {
            length += clusters.get(i).size();
        }
        return length;
    }

    @Override
    public boolean isDirectory() {
        return false;
    }

    @Override
    public MainFile getFile(String filename) {
        if (filename == null ? parent.getPath() + "/" + name == null : filename.equals(parent.getPath() + "/" + name)){
            return this;
        }
        return null;
    }
    
    public void delete(){
        //...
    }
    
    public void addCluster(Cluster cluster){
        if (cluster == null){
            return;
        }
        if (last == null){
            last = cluster;
        } else {
            last.setNextCluster(cluster);
        }
    }
    
    @Override
    public ArrayList<Cluster> getClusters(){
        return clusters;
    }
}
