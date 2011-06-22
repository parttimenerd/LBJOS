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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Parttimenerd
 */
public class FileSystem {

    private static java.io.RandomAccessFile source;
    private static java.io.File file;
    private static Directory root;
    private static File boot;
    public static final String ROOT_DIR_NAME = "root";
    private static final String BOOT_FILE_NAME = "boot";
    public static final String SYSTEM_OWNER = "System";
    public static final String USER_OWNER = "User";
    private static ArrayList<Cluster> clusters = new ArrayList<Cluster>();
    private static int pos;
    private static int header_end_pos;

    public static void config(java.io.File file) throws FileNotFoundException, IOException {
        FileSystem.file = file;
        source = new RandomAccessFile(file, "rw");
        root = new Directory(FileSystem.ROOT_DIR_NAME, null, FileSystem.SYSTEM_OWNER);
        boot = new File(BOOT_FILE_NAME, null, SYSTEM_OWNER);
        loadFiles();
    }

    private static void loadFiles() throws FileNotFoundException, IOException {
        if (!file.exists()) {
            return;
        }
        BufferedReader reader = new BufferedReader(new FileReader(file));
        ArrayList<Byte> list = new ArrayList<Byte>();
        //Read boot sector
        reader.skip(2);
        pos = 0;
        byte b = (byte) reader.read();
        while (b != '#') {
            list.add(b);
            b = (byte) reader.read();
            pos++;
        }
        String str = new String(listToByteArr(list));
        str = str.substring(FileSystem.BOOT_FILE_NAME.length());
        int boot_length = Integer.parseInt(str.substring(0, str.length()));
        reader.read();
        list.clear();
        b = (byte) reader.read();
        pos++;
        for (int i = 0; i < boot_length; i++) {
            list.add(b);
            b = (byte) reader.read();
            pos++;
            System.out.print(b + "|");
        }
        System.out.println("");
        boot.setData(listToByteArr(list));
        //Read normal files
        list.clear();
        //reader.read();
        //b = (byte) reader.read();
        while (b != '#') {
            list.add(b);
            b = (byte) reader.read();
            pos++;
        }
        String header = new String(listToByteArr(list));
        list.clear();
        b = (byte) reader.read();
        pos++;
        reader.mark(pos);
        header_end_pos = pos;
        while (b != -1) {
            list.add(b);
            b = (byte) reader.read();
        }
        pos = 0;
        HashMap<String, Directory> map = new HashMap<String, Directory>();
        String[] headers = header.split("(\\|\\|)");
        for (String string : headers) {
            if (string.length() > 10 && header.startsWith(FileSystem.ROOT_DIR_NAME)) {
                String[] items = string.split("\\|");
                String[] nameparts = items[0].split("\\/");
                for (int i = 1; i < nameparts.length - 1; i++) {
                    nameparts[0] += nameparts[i];
                }
                nameparts = new String[]{
                    nameparts[0],
                    nameparts[nameparts.length - 1]
                };
                long created_time = Long.parseLong(items[2]);
                long modified_time = Long.parseLong(items[3]);
                if (items.length == 4) {
                    if (!map.containsKey(items[0])) {
                        Directory mf = new Directory(nameparts[1], map.containsKey(nameparts[0]) ? map.get(nameparts[0]) : null, items[1]);
                        mf.setCreatedTime(created_time);
                        mf.setLastModifiedTime(modified_time);
                        map.put(items[0], mf);
                        if (items[0].length() == root.getName().length()) {
                            root = mf;
                        }
                    }
                } else {
                    File mf = new File(nameparts[1], map.containsKey(nameparts[0]) ? map.get(nameparts[0]) : null, items[1]);
                    mf.setCreatedTime(created_time);
                    mf.setLastModifiedTime(modified_time);
                    int cluster = Integer.parseInt(items[4]);
                    int length = Integer.parseInt(items[5]);
                    byte[] data = new byte[length];
                    for (int i = 0; i < length; i++) {
                        data[i] = list.get(i + cluster);
                    }
                    mf.setData(data);
                }
            }
        }
    }

    private static byte[] listToByteArr(List<Byte> list) {
        byte[] arr = new byte[list.size()];
        int num = 0;
        for (byte b : list) {
            arr[num] = b;
            num++;
        }
        return arr;
    }

    public static void storeFiles() throws IOException {
        if (!file.exists()) {
            file.createNewFile();
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        Store store = new Store();
        store.addHeader(root.getPath() + "|" + root.getOwner() + "|" + root.getCreatedTime() + "|" + root.getLastModifiedTime());
        store.addStore(storeDir(0, root));
        System.out.print(store.getHeader());
        writer.write("#" + boot.getName() + "|" + boot.getData().length + "#");
        for (byte b : boot.getData()) {
            writer.write(b);
        }
        writer.write("#" + store.getHeader() + "#", 0, store.getHeader().length() + 2);
        writer.newLine();
        for (byte b : store.getData()) {
            writer.write(b);
        }
        writer.flush();
        writer.close();
    }

    private static Store storeDir(int pos, Directory dir) {
        int lastpos = pos;
        Store store = new Store();
        for (MainFile fi : dir.getFileList()) {
            if (fi.isDirectory()) {
                Directory d = (Directory) fi;
                store.addHeader(d.getPath() + "|" + d.getOwner() + "|" + d.getCreatedTime() + "|" + d.getLastModifiedTime());
                Store var = storeDir(lastpos, d);
                store.addHeader(var.getHeader());
                store.addData(var.getData());
                lastpos = store.getEndpos();
            } else {
                File f = (File) fi;
                store.addHeader(f.getPath() + "|" + f.getOwner() + "|" + f.getCreatedTime() + "|" + f.getLastModifiedTime() + "|" + lastpos + "|" + f.getData().length);
                store.addData(f.getData());
                lastpos += f.getData().length;
            }
        }
        store.setEndpos(lastpos);
        return store;
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
        FileSystem.config(new java.io.File("D:/test.txt"));
        Directory dir = new Directory("Test", FileSystem.getRoot(), FileSystem.USER_OWNER);
        BufferedReader reader = new BufferedReader(new FileReader("D:/Unbenannt.png"));
        int val = reader.read();
        ArrayList<Byte> list = new ArrayList<Byte>();
        while (val != -1) {
            list.add((byte) val);
            val = reader.read();
        }
        byte[] arr = new byte[list.size()];
        int num = 0;
        for (byte b : list) {
            arr[num] = b;
            num++;
        }
        System.out.println("Finished reading " + list.size() + "bytes...");
        for (int i = 0; i < 1; i++) {
            File file = new File(i + ".txt", dir, FileSystem.USER_OWNER);
            file.setData(arr);
        }
        File file = new File("testfile.txt", dir, FileSystem.USER_OWNER);
        file.setData("Test...".getBytes());
        FileSystem.getRoot().addFile(dir);
        try {
            FileSystem.storeFiles();
        } catch (IOException ex) {
            Logger.getLogger(FileSystem.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return the root
     */
    public static Directory getRoot() {
        return root;
    }

    public static boolean addFile(MainFile file) {
        return root.addFile(file);
    }

    /**
     * @return the source
     */
    public static java.io.RandomAccessFile getSource() {
        return source;
    }

    public static Cluster getCluster(int length) {
        //TODO implement
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private static class Store {

        private String header = "";
        private ArrayList<Byte> data = new ArrayList<Byte>();
        private int endpos;

        public Store() {
        }

        /**
         * @return the header
         */
        public String getHeader() {
            return header;
        }

        /**
         * @return the data
         */
        public ArrayList<Byte> getData() {
            return data;
        }

        /**
         * @return the endpos
         */
        public int getEndpos() {
            return endpos;
        }

        /**
         * @param header the header to set
         */
        public void setHeader(String header) {
            this.header = header;
        }

        public void addHeader(String header) {
            this.header += header + "||";
        }

        public void addData(byte[] data) {
            for (byte b : data) {
                this.data.add(b);
            }
        }

        /**
         * @param data the data to set
         */
        public void setData(byte[] data) {
            this.data.clear();
            for (byte b : data) {
                this.data.add(b);
            }
        }

        /**
         * @param endpos the endpos to set
         */
        public void setEndpos(int endpos) {
            this.endpos = endpos;
        }

        public void addData(ArrayList<Byte> data) {
            this.data.addAll(data);
        }

        private void addStore(Store store) {
            this.addHeader(store.getHeader());
            this.addData(store.getData());
        }
    }
}
