/**  
* @Project: dzfp_qz-common
* @Title: FileUtil.java
* @Package com.holytax.dzfp_qz.common.utils
* @Description: TODO
* @author 李超超
* @date 2017年12月8日 上午10:55:40
* @Copyright: 2017 
* @version V1.0  
*/
package com.zzhb.zzoa.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;

/**
 * @ClassName FileUtil
 * @date 2017年12月8日
 */
public class FileUtil {

	public static boolean delete(String fileName) {
		File file = new File(fileName);
		if (!file.exists()) {
			return false;
		} else {
			if (file.isFile()) {
				return deleteFile(fileName);
			} else {
				return deleteDirectory(fileName);
			}
		}
	}

	public static boolean deleteFile(String fileName) {
		File file = new File(fileName);
		if (file.exists() && file.isFile()) {
			if (file.delete()) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public static List<String> readFilePath(String dir, String key) {
		List<String> fileNames = new ArrayList<>();
		File file = new File(dir);
		File[] listFiles = file.listFiles();
		for (File file2 : listFiles) {
			String fileName = file2.getName();
			if (fileName.indexOf(key) != -1) {
				fileNames.add(fileName);
			}
		}
		return fileNames;
	}

	public static boolean deleteDirectory(String dir) {
		// 如果dir不以文件分隔符结尾，自动添加文件分隔符
		if (!dir.endsWith(File.separator))
			dir = dir + File.separator;
		File dirFile = new File(dir);
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
			return false;
		}
		boolean flag = true;
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				flag = FileUtil.deleteFile(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
			// 删除子目录
			else if (files[i].isDirectory()) {
				flag = FileUtil.deleteDirectory(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
		}
		if (!flag) {
			return false;
		}
		// 删除当前目录
		if (dirFile.delete()) {
			return true;
		} else {
			return false;
		}
	}

	public static void createFile(String path, String content) throws UnsupportedEncodingException, IOException {
		StringBuffer sb = new StringBuffer();
		sb.append(content);
		FileOutputStream fos = null;
		File file = new File(path);
		fos = new FileOutputStream(file);
		// 将编码设置为UTF-8格式
		fos.write(sb.toString().getBytes("UTF-8"));
		if (fos != null) {
			fos.close();
		}
	}

	/**
	 * @Method: deleteDir
	 * @Description: 删除目录及目录下所有文件
	 * @param dir
	 *            要删除的目标目录
	 * @return
	 */
	public static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			// 递归删除目录中的子目录下
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		// 目录此时为空，可以删除
		return dir.delete();
	}

	/**
	 * 将文件流输出文件
	 * 
	 * @param bytes
	 * @param fileName
	 */
	public static void getFile(byte[] bytes, String fileName) {
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		File file = null;
		try {
			file = new File(fileName);
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(bytes);
			bos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.flush();
					fos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	public static void saveFile(byte[] bytes, String dir, String filename) throws Exception {
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		File file = null;
		try {
			file = new File(dir);
			if (!file.exists()) {
				file.mkdirs();
			}
			file = new File(file, filename);
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(bytes);
			bos.flush();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception();
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.flush();
					fos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	/**
	 * 
	 * @Method: ioReader
	 * @Description: TODO 获取文件流
	 * @param path
	 * @return
	 *
	 */
	public static byte[] ioReader(String path) {
		FileInputStream fis = null;
		ByteArrayOutputStream bos = null;
		byte[] bs = null;
		try {
			fis = new FileInputStream(new File(path));
			bs = new byte[1024];
			bos = new ByteArrayOutputStream();
			int len = -1;
			while ((len = fis.read(bs)) != -1) {
				bos.write(bs, 0, len);
			}
			bs = bos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (fis != null) {
			try {
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (bos != null) {
			try {
				bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				bos.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bs;
	}

	public static String readFile(String path) throws Exception {
		String fileStr = "";
		FileInputStream fis = null;
		ByteArrayOutputStream bos = null;
		byte[] bs = null;
		try {
			fis = new FileInputStream(new File(path));
			bs = new byte[5 * 1024];
			bos = new ByteArrayOutputStream();
			int len = -1;
			while ((len = fis.read(bs)) != -1) {
				bos.write(bs, 0, len);
			}
			bs = bos.toByteArray();
			fileStr = Base64.encodeBase64String(bs);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception();
		}
		if (fis != null) {
			try {
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (bos != null) {
			try {
				bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				bos.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return fileStr;
	}

	/**
	 * 
	 * @Method: ioReader
	 * @Description: TODO 获取文件流
	 * @param path
	 * @return
	 *
	 */
	public static byte[] readFileAndDelete(String path) {
		FileInputStream fis = null;
		ByteArrayOutputStream bos = null;
		byte[] bs = null;
		File file = new File(path);
		try {
			fis = new FileInputStream(file);
			bs = new byte[5 * 1024];
			bos = new ByteArrayOutputStream();
			int len = -1;
			while ((len = fis.read(bs)) != -1) {
				bos.write(bs, 0, len);
			}
			bs = bos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (bos != null) {
				try {
					bos.flush();
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (file.exists()) {
				file.delete();
			}
		}

		return bs;
	}

	/**
	 * 
	 * @Method: move
	 * @Description: TODO 移动文件
	 * @param srcFile
	 * @param destPath
	 * @return
	 * @throws IOException
	 *
	 */
	public static boolean move(String srcFile, String destPath) throws IOException {
		File file = new File(srcFile);
		File dir = new File(destPath);
		// 判断文件大小
		FileInputStream fis = new FileInputStream(file);
		if (fis.available() < 10) {
			file.delete();// 删除不符合的pdf文件
			fis.close();
			return false;
		} else {
			fis.close();
			return file.renameTo(new File(dir, file.getName()));
		}
	}

	public static void createDir(String dirPath) {
		File dir = new File(dirPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}

	public static void saveFileFromInputStream(InputStream stream, String path, String filename) {
		FileOutputStream fs = null;
		try {
			fs = new FileOutputStream(path + "/" + filename);
			byte[] buffer = new byte[1024 * 1024];
			int byteread = 0;
			while ((byteread = stream.read(buffer)) != -1) {
				fs.write(buffer, 0, byteread);
				fs.flush();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fs != null) {
					fs.close();
				}
				if (stream != null) {
					stream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public static String readFileFromInputStream(InputStream in) {
		String fileStr = "";
		ByteArrayOutputStream bos = null;
		byte[] bs = null;
		try {
			bos = new ByteArrayOutputStream();
			bs = new byte[5 * 1024];
			int len = -1;
			while ((len = in.read(bs)) != -1) {
				bos.write(bs, 0, len);
				bos.flush();
			}
			bs = bos.toByteArray();
			fileStr = Base64.encodeBase64String(bs);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try {
				if (bos != null) {
					bos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return fileStr;
	}

	public static Boolean copyFile(String source, String dest) {
		Boolean flag = false;
		BufferedInputStream inputStream = null;
		BufferedOutputStream outputStream = null;
		try {
			inputStream = new BufferedInputStream(new FileInputStream(source));
			outputStream = new BufferedOutputStream(new FileOutputStream(dest));
			int len = -1;
			byte[] bs = new byte[5 * 1024];
			while ((len = inputStream.read(bs)) != -1) {
				outputStream.write(bs, 0, len);
			}
			outputStream.flush();
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		} finally {
			try {
				inputStream.close();
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return flag;
	}

	public static void main(String[] args) {
		System.out.println(readFilePath("c:/tmpzzoa", "201901211721278"));
	}
}
