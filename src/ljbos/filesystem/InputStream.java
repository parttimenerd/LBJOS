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

import java.io.FileInputStream;
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
