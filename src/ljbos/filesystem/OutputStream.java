/*
 * Copyright (C) 2011 Parttikmenerd
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

import com.sun.corba.se.impl.logging.UtilSystemException;
import com.sun.corba.se.impl.orbutil.closure.Constant;
import java.io.FileNotFoundException;
import java.io.IOException;
import ljbos.filesystem.utils.Constants;

/**
 *
 * @author Parttimenerd
 */
public class OutputStream extends java.io.OutputStream {
    
    private Cluster actual;
    private int pos = 0;
    private byte[] data = new byte[Constants.CLUSTER_BLOCK_SIZE];
    private final File file;
    
    public OutputStream(String name) throws FileNotFoundException {
        file = (File) FileSystem.getRoot().getFile(name);
        if (name == null || file == null || file.isDirectory()){
            throw new FileNotFoundException();
        }
    }

    public OutputStream(File file) throws FileNotFoundException {
	String name = (file != null ? file.getPath() : null);
        this.file = file;
        if (name == null) {
            throw new NullPointerException();
        }
    }
    
    @Override
    public void write(int b) throws IOException {
        if (pos >= Constants.CLUSTER_BLOCK_SIZE){
            flush();
        }
        data[pos] = (byte) b;
        pos++;
    }
    
    @Override
    public void flush() throws IOException{
        Cluster cluster = FileSystem.getCluster(pos + 1);
        byte[] arr = new byte[pos + 1];
        System.arraycopy(data, 0, arr, 0, pos + 1);
        cluster.setData(arr);
        cluster.write();
        file.addCluster(cluster);
        pos = 0;
    }
    
    @Override
    public void finalize(){
        flush();
    }
}
