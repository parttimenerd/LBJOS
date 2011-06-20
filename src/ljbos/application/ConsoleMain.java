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

package ljbos.application;

import ljbos.filesystem.Directory;
import ljbos.filesystem.FileSystem;
import ljbos.filesystem.MainFile;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author Parttimenerd
 */
public class ConsoleMain {

    private static Directory actual;

    /**
     * @param args the command line arguments
     * @throws FileNotFoundException
     * @throws IOException  
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        FileSystem.config(new File("D:/test.txt"));
        actual = FileSystem.getRoot();
        BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
        System.out.print(actual.getPath() + ">");
        String str = buf.readLine();
        while (!"exit".equals(str)) {
            executeString(str);
            System.out.print(actual.getPath() + ">");
            str = buf.readLine();
        }
    }

    public static void executeString(String str) throws IOException {
        String[] arr = str.replace('\n', ' ').trim().split("\\s");
        String com = arr[0];
        if (arr.length == 2) {
            String arg = arr[1];
            if ("cd".equals(com)) {
                if (".".equals(arg)){
                    if (actual.getParent() != null){
                        actual = actual.getParent();
                    }
                    return;
                }
                MainFile f = actual.getFile(arg);
                if (!f.isDirectory()){
                    return;
                }
                Directory act = (Directory) f;
                if (act == null) {
                    act = (Directory) FileSystem.getRoot().getFile(arg);
                }
                if (act != null) {
                    actual = act;
                }
            } else if ("mkdir".equals(com)){
                if (arg.indexOf(".") != -1){
                    new ljbos.filesystem.File(arg, actual, FileSystem.USER_OWNER);
                } else {
                    new ljbos.filesystem.Directory(arg, actual, FileSystem.USER_OWNER);
                }
            }
        }
        if ("files".equals(com)) {
            for (MainFile file : actual.getFileList()) {
                System.out.println(file.getPath());
            }
        } else if ("store".equals(com)){
            FileSystem.storeFiles();
        }
    }
}
