package util.process;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

public class ZipUtils {
	public static final String FILE_SEPARATOR = System.getProperty("file.separator");
	
	public static byte[] zipFiles(File directory) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipArchiveOutputStream zos = new ZipArchiveOutputStream(baos);

		zos.setEncoding("EUC-KR");

		searchDirectory(directory, directory, zos);
		
		zos.flush();
        baos.flush();
        zos.close();
        baos.close();
        return baos.toByteArray();
	}
	
	private static void compressFile(File parentDir, String fileName, ZipArchiveOutputStream zos){
		byte bytes[] = new byte[2048];
		FileInputStream fis;
		try {
			fis = new FileInputStream(parentDir.getPath() + FILE_SEPARATOR + fileName);
			BufferedInputStream bis = new BufferedInputStream(fis);

	        zos.putArchiveEntry(new ZipArchiveEntry( parentDir.getName()+ FILE_SEPARATOR + fileName));
	        int bytesRead;
            while ((bytesRead = bis.read(bytes)) != -1) {
                zos.write(bytes, 0, bytesRead);
            }
            zos.closeArchiveEntry();
            bis.close();
            fis.close();
	        
		} catch (Exception e) {

			e.printStackTrace();
		}
	}
	
	private static void searchDirectory(File file, File parentDir ,ZipArchiveOutputStream zos) {
		if( file.isDirectory()) {
			
			File[] childrenFile = file.listFiles();
			
			for( File child : childrenFile ) {
				searchDirectory(child, file ,zos);
			}

		}else {
			compressFile(parentDir, file.getName(), zos);
		}
	}
	
	public static boolean deleteFiles(File directory) {
		
		File[] fileList = directory.listFiles();
        for(File f : fileList) {
        	if( f.isDirectory())
        		deleteFiles(f);
        	else {
        		if(!f.delete())
            		return false;
        	}
        }
        
        if(directory.delete()) {
        	return true;
        }else {
        	return false;
        }
	}
}
