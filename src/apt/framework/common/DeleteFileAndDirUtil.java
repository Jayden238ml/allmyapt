package apt.framework.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class DeleteFileAndDirUtil {
	public static void deleteFilesAndDirs(String path , long systemtime) {
		deleteFiles(path , systemtime );
		deleteDirs(path );
	}

	public static void deleteFiles(String path , long systemtime ) {
		File file = new File(path);
		File[] files = file.listFiles();

		if (files == null ) {
			return;
		}

		if (files.length != 0) {
			for (int i = 0; i < files.length; i++) {
				if (files[i].isFile()) {
					if(systemtime>0){
						if(files[i].lastModified()<systemtime){
							files[i].delete();
						}
					}else{
						files[i].delete();
					}
				} else {
					deleteFiles(files[i].getPath() , systemtime);
				}
			}
		}
	}
	public static void deleteFile(String filename ) {
		File file = new File(filename);
		if (file == null ) {
			return;
		}
		if (file.isFile()) {
			file.delete();
		}
	}
	public static void deleteDirs(String path ) {
		File dir = new File(path);
		File[] dirs = dir.listFiles();

		if (dirs == null) {
			return;
		}

		if (dirs.length != 0) {
			for (int i = 0; i < dirs.length; i++) {
				if (dirs[i].listFiles().length == 0) {
					dirs[i].delete();
				} else {
					deleteDirs(dirs[i].getPath());
				}
			}
		}
		dir.delete();
	}
	public static void MakeDir(String dirname){
	  try{
		File dir = new File(dirname);
		if(!dir.exists()){
		  dir.mkdir();
		}
	  }catch(Exception e){
		e.printStackTrace();
	  }
	}
	public static void FileMove(String fromfilename, String tofilename)
		throws Exception {

		File up1 = null;
		File up2 = null;

		up1 = new File(fromfilename);
		up2 = new File(tofilename);
		if (up1.exists()) {
			boolean rslt = up1.renameTo(up2);
		}
		if (up1.exists()) {
			up1.delete();
		}
	}
	public static void FileCopy(String fromfilename, String tofilename) throws Exception {
		File fromFile = null;
		File toFile = null;
		fromFile = new File(fromfilename);
		toFile = new File(tofilename);
	  if (!fromFile.canRead()) {
		throw new Exception(fromFile.getName() + " file can not read.");
	  }
	  FileInputStream fis = null;
	  FileOutputStream fos = null;
	  try {
		fis = new FileInputStream(fromFile);
		fos = new FileOutputStream(toFile);

		byte[] buff = new byte[4096];

		int len = 0;
		while ( (len = fis.read(buff)) != -1) {
		  fos.write(buff, 0, len);
		  fos.flush();
		}
		//System.out.println(fromFile.getName() + " ==> " + toFile.getName() + " copy success.");
	  }
	  finally {
		if (fis != null) {
		  fis.close();
		}
		if (fos != null) {
		  fos.close();
		}
	  }
	}

	
}
