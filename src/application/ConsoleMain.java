/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import filesystem.Directory;
import filesystem.FileSystem;
import filesystem.MainFile;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author Johannes
 */
public class ConsoleMain {

    private static Directory actual;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        FileSystem.config(new File("D:/test.txt"));
        actual = FileSystem.getRoot();
        BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
        System.out.print(actual.getPath() + ">");
        String str = buf.readLine();
        while (str != "exit") {
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
                    new filesystem.File(arg, actual, FileSystem.USER_OWNER);
                } else {
                    new filesystem.Directory(arg, actual, FileSystem.USER_OWNER);
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
