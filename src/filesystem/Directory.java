/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package filesystem;

import java.util.ArrayList;

/**
 *
 * @author Johannes
 */
public class Directory extends MainFile {

    private ArrayList<MainFile> filelist;

    public Directory(String name, Directory parent, String owner) {
        super(name, parent, owner);
        filelist = new ArrayList<MainFile>();
        Directory dir = parent;
    }

    @Override
    public boolean isEmpty(){
        return filelist.isEmpty();
    }
    
    /**
     * @return size in bytes
     */
    @Override
    public int size(){
        int size = 0;
        for (MainFile file : filelist) {
            size += file.size();
        }
        return size;
    }
    
    public ArrayList<MainFile> getFileList() {
        return filelist;
    }

    /**
     * @param filelist the filelist to set
     */
    public void setFilelist(ArrayList<MainFile> filelist) {
        this.filelist = filelist;
    }

    public boolean addFile(MainFile file){
        if (file.getParent() == this){
            for (MainFile mainFile : filelist) {
                if (mainFile.getName().equals(file.getName())){
                    return true;
                }
            }
            filelist.add(file);
            return true;
        } else {
            for (MainFile mainFile : filelist) {
                if (mainFile.isDirectory()){
                    if (((Directory)mainFile).addFile(file)){
                        return true;
                    }
                }
            }
            return false;
        }
    }
    
    @Override
    public MainFile getFile(String filename) {
        for (MainFile dir : filelist){
            if (dir.getName().equals(filename) || dir.getPath().equals(filename)){
                return dir;
            }
            MainFile file = dir.getFile(filename);
            if (file != null){
                return file;
            }
        }
        return null;
    }
    
    @Override
    public void clear(){
        filelist.clear();
    }

    @Override
    public boolean isDirectory() {
        return true;
    }
}
