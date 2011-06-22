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

import java.util.ArrayList;

/**
 *
 * @author Parttimenerd
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
    
    @Override
    public ArrayList<MainFile> getFileList() {
        return filelist;
    }

    @Override
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
    public void delete(){
        filelist.clear();
    }

    @Override
    public boolean isDirectory() {
        return true;
    }
}
