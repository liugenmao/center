/**
 * 
 */
package utils;

import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 文件工具类
 * 
 * @author John zhang
 * @version 0.1
 */
public class FileUtils {

	private static SimpleDateFormat mFileGenerator = new SimpleDateFormat ( "yyMMddHHmmssSSS" );
	private static DecimalFormat mFileSuffixFmt = new DecimalFormat ( "0000" );
	private static final AtomicInteger mFileSuffix = new AtomicInteger ( 0 );

	/**
	 * 根据文件绝对路径获取文件名
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getFileName( String filePath ) {
		if ( ValidatorUtils.isEmpty ( filePath ) )
			return "";
		return filePath.substring ( filePath.lastIndexOf ( File.separator ) + 1 );
	}

	/**
	 * 获取文件大小
	 * 
	 * @param size 字节
	 * @return
	 */
	public static String getFileSize( long size ) {
		if ( size <= 0 )
			return "0";
		DecimalFormat df = new DecimalFormat ( "##.##" );
		float temp = (float) size / 1024;
		if ( temp >= 1024 ) {
			return df.format ( temp / 1024 ) + "M";
		} else {
			return df.format ( temp ) + "K";
		}
	}

	/**
	 * 转换文件大小
	 *
	 * @param fileS
	 * @return B/KB/MB/GB
	 */
	public static String formatFileSize( long fileS ) {
		DecimalFormat df = new DecimalFormat ( "#.00" );
		String fileSizeString = "";
		if ( fileS < 1024 ) {
			fileSizeString = df.format ( (double) fileS ) + "B";
		} else if ( fileS < 1048576 ) {
			fileSizeString = df.format ( (double) fileS / 1024 ) + "KB";
		} else if ( fileS < 1073741824 ) {
			fileSizeString = df.format ( (double) fileS / 1048576 ) + "MB";
		} else {
			fileSizeString = df.format ( (double) fileS / 1073741824 ) + "G";
		}
		return fileSizeString;
	}

	/**
	 * 获取目录文件大小
	 * 
	 * @param dir
	 * @return
	 */
	public static long getDirSize( File dir ) {
		if ( dir == null ) {
			return 0;
		}
		if ( !dir.isDirectory () ) {
			return 0;
		}
		long dirSize = 0;
		File[] files = dir.listFiles ();
		for ( File file : files ) {
			if ( file.isFile () ) {
				dirSize += file.length ();
			} else if ( file.isDirectory () ) {
				dirSize += file.length ();
				dirSize += getDirSize ( file ); // 递归调用继续统计
			}
		}
		return dirSize;
	}

	public static String uniqueFilename( String filename ) {
		int indexOf = filename.lastIndexOf ( "." );
		String ext = indexOf >= 0 ? filename.substring ( indexOf, filename.length () ) : "";
		String prefix = mFileGenerator.format ( new Date () );
		if ( mFileSuffix.addAndGet ( 1 ) > 9999 ) {
			mFileSuffix.set ( 0 );
		}
		String newFilename = prefix + mFileSuffixFmt.format ( mFileSuffix.get () ) + ext;
		return newFilename;
	}

	public static String getFileExt( String filename ) {
		if ( ValidatorUtils.isEmpty ( filename ) )
			return "";
		return filename.substring ( filename.lastIndexOf ( "." ) + 1 );
	}

	public static String generateDateFolder() {
		SimpleDateFormat sdf = new SimpleDateFormat ( "/yyyy/MM/dd" );
		return sdf.format ( System.currentTimeMillis () );
	}

	public static void write2File( String filePath, String data ) {
		try {

			File file = new File ( filePath );

			if ( !file.exists () ) {
				file.createNewFile ();
			}

			// true = append file
			// FileWriter fileWritter = new FileWriter ( file, true );
			// BufferedWriter bufferWritter = new BufferedWriter ( fileWritter
			// );
			// bufferWritter.write ( data );
			// bufferWritter.close ();

			FileWriter fw = new FileWriter ( file.getAbsoluteFile () );
			BufferedWriter bw = new BufferedWriter ( fw );
			bw.write ( data );
			bw.close ();

		} catch ( IOException e ) {
			e.printStackTrace ();
		}
	}

	/**
	 * Copy file to file.
	 * 
	 * @since 1.3.22
	 * @param fromFile the File to copy (readable, non-null file)
	 * @param toFile the File to copy to (non-null, parent dir exists)
	 * @throws IOException
	 */
	public static void copyValidFiles( File fromFile, File toFile ) throws IOException {
		FileInputStream in = null;
		FileOutputStream out = null;
		try {
			in = new FileInputStream ( fromFile );
			out = new FileOutputStream ( toFile );
			copyStream ( in, out );
		} finally {
			if ( out != null ) {
				out.close ();
			}
			if ( in != null ) {
				in.close ();
			}
		}
	}
	
	/**
	 * @since 1.3.22
	 * @param in
	 * @param out
	 * @throws IOException
	 */
	public static void copyStream( InputStream in, OutputStream out ) throws IOException {
		final int MAX = 4096;
		byte[] buf = new byte[MAX];
		for ( int bytesRead = in.read ( buf, 0, MAX ); bytesRead != -1; bytesRead = in.read ( buf, 0, MAX ) ) {
			out.write ( buf, 0, bytesRead );
		}
	}
	
	/**
	 * @since 1.3.22
	 * @param in
	 * @param out
	 * @throws IOException
	 */
	public static void copyStream( Reader in, Writer out ) throws IOException {
		final int MAX = 4096;
		char[] buf = new char[MAX];
		for ( int bytesRead = in.read ( buf, 0, MAX ); bytesRead != -1; bytesRead = in.read ( buf, 0, MAX ) ) {
			out.write ( buf, 0, bytesRead );
		}
	}

	public static void main( String[] args ) {
		for ( int i = 0; i < 100000; i++ )
			System.out.println ( uniqueFilename ( "1.gpg" ) );
	}

}
