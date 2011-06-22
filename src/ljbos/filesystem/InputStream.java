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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Parttimenerd
 */
public class InputStream extends java.io.InputStream {
    
    private ArrayList<Cluster> clusters;
    private Cluster actual;
    private int pos = 0;
    private byte[] data;
    
    public InputStream(String name) throws FileNotFoundException {
        MainFile file = FileSystem.getRoot().getFile(name);
        if (name == null || file == null || file.isDirectory()){
            throw new FileNotFoundException();
        }
        clusters = file.getClusters();
        actual = clusters.isEmpty() ? null : clusters.get(0);
    }

    public InputStream(File file) throws FileNotFoundException {
	String name = (file != null ? file.getPath() : null);
        if (name == null) {
            throw new NullPointerException();
        }
        clusters = file.getClusters();
        actual = clusters.isEmpty() ? null : clusters.get(0);
    }
    
    @Override
    public int read() throws IOException {
        if (actual == null || data == null || data.length < 1){
            return -1;
        }
        if (actual.size() <= pos){
            if (actual.getNextCluster() == null){
                return -1;
            }
            setCluster(actual.getNextCluster());
        }
        int val = data[pos];
        pos++;
        return val;
    }

    private void setCluster(Cluster cluster) throws IOException{
        pos = 0;
        actual = cluster;
        actual.read();
        data = actual.getData();
    }

    @Override
    public int read(byte b[]) throws IOException{
        int i = 0;
        for (; i < b.length; i++) {
            int val = read();
            if (val == -1){
                break;
            }
            b[i] = (byte)(val);
        }
        return i;
    }

    @Override
    public int read(byte b[], int off, int len) throws IOException {
        return -1;
    }

    @Override
    public long skip(long n) throws IOException{
        //TODO implement...
        return pos;
    }
    
    public boolean avaliable() throws IOException{
        return actual != null;
    }

    @Override
    public void close() throws IOException {
        data = null;
        actual = null;
    }

    @Override
    protected void finalize() throws IOException {
	close();
    }
}
