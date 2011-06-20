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

package ljbos.filesystem.utils;

/**
 * This class includes the constants and constant strings of the filesystem.
 * Later the constants may be could be stored in a properties file and loaded
 * by this class.
 * 
 * @author Parttimenerd
 */
public final class Constants {
    public static final int CLUSTER_BLOCK_SIZE = 2024; //in byte
    public static final int PASSWD_LENGTH = 64; //to store
    public static final int PASSWD_ROUNDS = 32000;
    public static final int AES_MODE = 128;
}
