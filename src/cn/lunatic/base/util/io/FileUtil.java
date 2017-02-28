package cn.lunatic.base.util.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Pattern;

import cn.lunatic.base.util.Log;
import cn.lunatic.base.util.StringUtils;
import cn.lunatic.base.util.security.Base64;

public class FileUtil {
	/**
	 * 文件读取缓冲区大小
	 */
	private static final int CACHE_SIZE = 1024;
	
	/**
	 * 文件比较缓冲区大小
	 */
	private static final int COMPARE_CACHE_SIZE = 4096;
	
	/**
	 * 删除文件，文件不是文件或者不存在返回true,删除失败返回false
	 * @param file
	 * @return
	 */
	public static boolean delFile(String file) {
		if(StringUtils.isNotBlank(file)){
			return delFile(new File(file));
		}
		return true;
	}
	
	/**
	 * 删除文件，文件不是文件或者不存在返回true,删除失败返回false
	 * @param file
	 * @return
	 */
	public static boolean delFile(File file) {
		if(file.exists() && file.isFile()){
			return file.delete();
		}
		return true;
	}
	
	public static boolean delDir(String dir){
		if(StringUtils.isNotBlank(dir)){
			return delDir(new File(dir));
		}
		return true;
	}
	
	public static boolean delDir(File dir){
		clearDir(dir);
		return dir.delete();
	}
	
	/**
	 * 修改文件(夹)名
	 * 
	 * @param file
	 *            文件路径
	 * @param newName
	 *            新文件名
	 * @return
	 */
	public static boolean rename(String file, String newName) {
		return rename(new File(file), newName);
	}

	/**
	 * 修改文件(夹)名
	 * 
	 * @param file
	 *            文件对象
	 * @param newName
	 *            新文件名
	 * @return 文件不存在或者执行路径不是一个文件返回false
	 */
	public static boolean rename(File file, String newName) {
		if (!file.exists()) {
			return false;
		}
		String parentDir = file.getParent();
		File newFile = new File(parentDir + File.separator + newName);
		boolean flag = file.renameTo(newFile);
		if (flag) {
			Log.print("修改文件名成功:" + file.getAbsolutePath() + "---->" + newFile.getAbsolutePath());
		}
		return flag;
	}

	/**
	 * 递归清空目录
	 * @param dir
	 */
	public static void clearDir(String dir) {
		clearDir(new File(dir));
	}

	/**
	 * 递归清空目录
	 * @param dir
	 */
	public static void clearDir(File dir) {
		if (!dir.exists() || !dir.isDirectory()) {
			throw new RuntimeException("目录不存在或者路径指向的位置不是一个目录.");
		}
		File[] files = dir.listFiles();
		for (int i = 0; files != null && i < files.length; i++) {
			File curr = files[i];
			if (curr.isFile()) {
				if (curr.delete()) {
					Log.print("成功删除文件:" + curr.getAbsolutePath());
				}
			}
			if (curr.isDirectory()) {
				clearDir(curr);
				curr.delete();
			}
		}
	}

	/**
	 * 递归删除父目录下的指定子文件(夹)
	 * @param dir
	 * @param subDir
	 */
	public static void delDirBatch(String dir, String subDir) {
		delDirBatch(new File(dir), subDir);
	}

	/**
	 * 递归删除父目录下的子目录
	 * @param dir
	 * @param subName		子文件(夹)关键字
	 */
	public static void delDirBatch(File dir, String subName) {
		if (StringUtils.isBlank(subName)) {
			throw new RuntimeException("子目录名称不能为空.");
		}
		File[] files = dir.listFiles();
		for (int i = 0; files != null && i < files.length; i++) {
			File curr = files[i];
			if (curr.isDirectory() && curr.getName().indexOf(subName) >= 0) {
				clearDir(curr);
				if (curr.delete()) {
					Log.print("成功删除目录:" + curr.getAbsolutePath());
				}
			}else if(curr.isFile() && curr.getName().indexOf(subName) >= 0) {
				curr.deleteOnExit();
			}else if (curr.isDirectory()) {
				delDirBatch(curr, subName);
			}
		}
	}

	
	
	/**
	 * 往文件中追加内容
	 * @param filePath
	 * @param appendStr
	 * @throws IOException 
	 */
	public static void append(String filePath, String appendStr) throws IOException {
		if(StringUtils.isBlank(filePath)){
			throw new RuntimeException("文件路径为空,非法操作.");
		}
		append(new File(filePath), appendStr);
	}

	/**
	 * 往文件中追加内容
	 * @param 	file
	 * @param 	appendStr
	 * @throws IOException 
	 */
	public static void append(File file, String appendStr) throws IOException {
		createFile(file);
		if (!file.isFile()) {
			throw new IOException("文件路径指向的是不是一个文件,非法操作.");
		}
		BufferedWriter writer = null;
		try {
//			long l = System.currentTimeMillis();
			writer = new BufferedWriter(new FileWriter(file, true));
			writer.write(appendStr);
//			Log.print("往文件[" + file.getAbsolutePath() + "]追加内容(" + (appendStr.getBytes().length/1000) + "K)成功.耗时(" + (System.currentTimeMillis()-l) + "毫秒)");
		} finally {
			writer.close();
		}
	}

	/**
	 * 往文件中写内容
	 * @param filePath
	 * @param appendStr
	 * @throws IOException 
	 */
	public static void write(String filePath, String str) throws IOException {
		if(StringUtils.isBlank(filePath)){
			throw new RuntimeException("文件路径为空,非法操作.");
		}
		write(new File(filePath), str);
	}

	/**
	 * 往文件中写内容
	 * 
	 * @param file
	 * @param appendStr
	 * @throws IOException 
	 */
	public static void write(File file, String str) throws IOException {
		createFile(file);
		if (!file.isFile()) {
			throw new IOException("文件路径指向的是不是一个文件,非法操作.");
		}
		BufferedWriter writer = null;
		try {
//			long l = System.currentTimeMillis();
			writer = new BufferedWriter(new FileWriter(file, false));
			writer.write(str);
//			Log.print("往文件[" + file.getAbsolutePath() + "]写入内容(" + (str.getBytes().length/1000) + "K)成功.耗时(" + (System.currentTimeMillis()-l) + "毫秒)");
		} finally {
			writer.close();
		}
	}

	/**
	 * 读取文件为为BASE64字符串,大文件慎用
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public static String encodeFile(String filePath) throws IOException {
		byte[] bytes = fileToByte(filePath);
		return Base64.encodeBytes(bytes);
	}

	/**
	 * BASE64串转为byte写入文件中
	 * 
	 * @param filePath
	 *            文件绝对路径
	 * @param base64
	 *            编码字符串
	 * @throws Exception
	 */
	public static void decodeToFile(String filePath, String base64) throws IOException {
		byte[] bytes = Base64.decode(base64);
		byteArrayToFile(bytes, filePath);
	}

	/**
	 * 读取文件为二进制数组,大文件慎用
	 * 
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public static byte[] fileToByte(String filePath) throws IOException {
		byte[] data = new byte[0];
		File file = new File(filePath);
		if (file.exists()) {
			FileInputStream in = new FileInputStream(file);
			ByteArrayOutputStream out = new ByteArrayOutputStream(2048);
			byte[] cache = new byte[CACHE_SIZE];
			int nRead = 0;
			while ((nRead = in.read(cache)) != -1) {
				out.write(cache, 0, nRead);
				out.flush();
			}
			out.close();
			in.close();
			data = out.toByteArray();
		}
		return data;
	}

	/**
	 * 二进制数据写入文件
	 * @param bytes
	 * @param filePath
	 * @throws Exception
	 */
	public static void byteArrayToFile(byte[] bytes, String filePath) throws IOException {
		InputStream in = new ByteArrayInputStream(bytes);
		File destFile = new File(filePath);
		createFile(destFile);
		write(destFile, "");
		OutputStream out = new FileOutputStream(destFile);
		byte[] cache = new byte[CACHE_SIZE];
		int nRead = 0;
		while ((nRead = in.read(cache)) != -1) {
			out.write(cache, 0, nRead);
			out.flush();
		}
		out.close();
		in.close();
	}
	
	/**
	 * 获取目录下所有文件列表（递归子目录）
	 * @param dir 目录
	 * @param relativePathFlag  相对路径标识,true:相对路径(去除目录路径),false:绝对路径
	 * @param files 接收返回列表对象
	 */
	public static void listAllSubFiles(File dir, boolean relativePathFlag, List<String> files){
		listFiles(dir, relativePathFlag, files);
		if(relativePathFlag){
			files.remove(0);
		}
	}
	
	/**
	 * 获取目录下所有文件列表（递归子目录）
	 * @param dir 				目录
	 * @param relativePathFlag  相对路径标识,true:相对路径(去除目录路径),false:绝对路径
	 * @param files 			接收返回列表对象
	 */
	public static void listAllSubFiles(String dir, boolean relativePathFlag, List<String> files){
		if(StringUtils.isBlank(dir)){
			throw new RuntimeException("目录为空.");
		}
		File file = new File(dir);
		listAllSubFiles(file, relativePathFlag, files);
	}
	
	/**
	 * 获取目录下所有文件列表（递归子目录）
	 * @param dir 				目录
	 * @param relativePathFlag  相对路径标识,true:相对路径(去除目录路径),false:绝对路径
	 * @param files 			接收返回列表对象
	 */
	private static void listFiles(File dir, boolean relativePathFlag, List<String> files){
		if(relativePathFlag && files.size() == 0){
			files.add(dir.getAbsolutePath());
		}
		if(dir.isDirectory()){
			File[] fileList = dir.listFiles();
			for (File file2 : fileList) {
				listFiles(file2, relativePathFlag, files);
			}
		}else{
			if(relativePathFlag){
				files.add(dir.getAbsolutePath().replace(files.get(0), ""));
			}else{
				files.add(dir.getAbsolutePath());
			}
		}
	}

	/**
	 * 比较两个文件内容是否一致
	 * @param filePath1
	 * @param filePath2
	 * @return	0:一致,1:大小不一致,2:内容不一致
	 * @throws IOException 
	 */
	public static String compareFile(String filePath1, String filePath2) throws IOException {
		if(StringUtils.isBlank(filePath1, filePath2)){
			throw new IOException("比较文件路径为空.("+filePath1+")("+filePath2+")");
		}
		return compareFile(new File(filePath1), new File(filePath2));
	}
	
	/**
	 * 比较两个文件内容是否一致
	 * @param file1
	 * @param file2
	 * @return	0:一致,1:大小不一致,2:内容不一致
	 * @throws IOException 
	 */
	public static String compareFile(File file1, File file2) throws IOException {
		Log.print("比较文件["+file1.getAbsolutePath()+"]和["+file2.getAbsolutePath()+"]开始...");
		long start = System.currentTimeMillis();
		if(file1.length() != file2.length()){
			Log.print("比较结束{文件大小不一致},耗时("+((System.currentTimeMillis() - start))+"毫秒)");
			return "1";
		}
		FileInputStream in1 = null;
		FileInputStream in2 = null;
		String flag = "2";
		try {
			in1 = new FileInputStream(file1);
			in2 = new FileInputStream(file2);
			byte[] cache1 = new byte[COMPARE_CACHE_SIZE];
			byte[] cache2 = new byte[COMPARE_CACHE_SIZE];
			int read1 = in1.read(cache1);
			int read2 = in2.read(cache2);
			while (true) {
				if(read1 == -1 && read2 == -1){
					flag = "0";
					break;
				}else if(read1 == -1 || read2 == -1){
					flag = "2";
					break;
				}else{
					if(!Arrays.equals(cache1, cache2)){
						flag = "2";
						break;
					}
				}
				read1 = in1.read(cache1);
				read2 = in2.read(cache2);
			}
		} finally {
			if (in1 != null) {
				in1.close();
			}
			if (in2 != null) {
				in2.close();
			}
		}
		String tmp = "0".equals(flag)?"文件内容一致":"1".equals(flag)?"文件大小不一致":"文件内容不一致";
		Log.print("比较结束{" + tmp + "},耗时["+((System.currentTimeMillis() - start))+"毫秒]");
		return flag;
	}

	/**
	 * 比较两个文件夹内容是否一致
	 * @param dir1
	 * @param dir2
	 * @return	map(fileNmae:文件名;filePath:文件路径;result:文件对比结果[0:一致,1:大小不一致,2:内容不一致,3:目录1不存在,4:目录2不存在])
	 * @throws IOException 
	 */
	public static List<SortedMap<String, String>> compareDir(String dir1, String dir2) throws IOException{
		List<String> files1 = new ArrayList<String>();
		List<String> files2 = new ArrayList<String>();
		listAllSubFiles(dir1, true, files1);
		listAllSubFiles(dir2, true, files2);
		List<SortedMap<String, String>> ret = new ArrayList<SortedMap<String,String>>();
		
		Iterator<String> iterator1 = files1.iterator();
		Iterator<String> iterator2 = files2.iterator();
		//目录2存在,目录1不存在
		while(iterator2.hasNext()){
			String file = iterator2.next();
			if(!files1.contains(file)){
				SortedMap<String, String> curr = new TreeMap<String, String>();
				curr.put("fileNmae", file.substring((file.lastIndexOf("/")+1), file.length()));
				curr.put("filePath", file);
				curr.put("result", "3");
				ret.add(curr);
				iterator2.remove();
			}
		}
		//目录1存在,目录2不存在
		while(iterator1.hasNext()){
			String file = iterator1.next();
			if(!files2.contains(file)){
				SortedMap<String, String> curr = new TreeMap<String, String>();
				curr.put("fileNmae", file.substring((file.lastIndexOf("/")+1), file.length()));
				curr.put("filePath", file);
				curr.put("result", "4");
				ret.add(curr);
				iterator1.remove();
			}
		}
		//剩下的都是两个目录都有的文件,对比文件是否一致(使用同样的规则进行List排序)
		Collections.sort(files1);
		Collections.sort(files2);
		for(int i = 0,len = files1.size(); i<len;i++){
			String fileRelativePath = files1.get(i);
			String file1 = getAbsolutePath(dir1, fileRelativePath);
			String file2 = getAbsolutePath(dir2, fileRelativePath);
			String result = compareFile(file1, file2);
			SortedMap<String, String> curr = new TreeMap<String, String>();
			curr.put("fileNmae", fileRelativePath.substring((fileRelativePath.lastIndexOf("/")+1), fileRelativePath.length()));
			curr.put("filePath", fileRelativePath);
			curr.put("result", result);
			ret.add(curr);
		}
		return ret;
	}
	
	/**
	 * 获取路径的绝对路径
	 * @param relativePathPath
	 * @return
	 */
	public static String getAbsolutePath(String relativePathPath){
		return new File(relativePathPath).getAbsolutePath();
	}

	/**
	 * 获取文件的绝对路径
	 * @param dir
	 * @param fileRelativePath
	 * @return
	 */
	public static String getAbsolutePath(String dir, String fileRelativePath){
		dir = dir.replaceAll("\\\\", "/");
		if(!dir.endsWith("/")){
			dir = dir + "/";
		}
		if(fileRelativePath.startsWith("/")){
			fileRelativePath = fileRelativePath.substring(1);
		}
		return dir + fileRelativePath;
	}
	
	/**
	 * 复制文件(夹)
	 * @param sourceFile
	 * @param targetFile
	 * @throws IOException 
	 */
	public static void copy(String sourceFile, String targetFile) throws IOException {
		if(StringUtils.isBlank(sourceFile,targetFile)){
			throw new IOException("原始文件或目标文件,文件名为空");
		}
		copy(new File(sourceFile), new File(targetFile));
	}
	
	/**
	 * 复制文件(夹)
	 * @param sourceFile
	 * @param targetFile
	 * @throws IOException 
	 */
	public static void copy(File sourceFile, File targetFile) throws IOException {
		if(!sourceFile.exists()){
			throw new IOException("复制的文件(夹)不存在.[" + sourceFile + "]");
		}
		Long l = System.currentTimeMillis();
		if(sourceFile.isDirectory()){
			copyDir(sourceFile, targetFile);
			Log.print("复制文件夹成功.耗时:"+(System.currentTimeMillis()-l)+"毫秒");
		}else if(sourceFile.isFile()){
			copyFile(sourceFile, targetFile);
			Log.print("复制文件成功.耗时:"+(System.currentTimeMillis()-l)+"毫秒");
		}
	}
	
	/**
	 * 复制文件
	 * @param sourceFile
	 * @param targetFile
	 * @throws IOException 
	 */
	public static void copyFile(String sourceFile, String targetFile) throws IOException{
		if(StringUtils.isBlank(sourceFile,targetFile)){
			throw new IOException("原始文件或目标文件,文件名为空");
		}
		copyFile(new File(sourceFile), new File(targetFile));
	}

	
	/**
	 * 复制文件
	 * @param sourceFile
	 * @param targetFile
	 * @throws IOException 
	 */
	public static void copyFile(File sourceFile, File targetFile) throws IOException{
		if(sourceFile == null || !sourceFile.exists()){
			throw new IOException("原始文件不存在.["+sourceFile+"]");
		}
		if(!sourceFile.isFile()){
			throw new IOException("原始文件不是一个文件.["+sourceFile+"]");
		}
		createFile(targetFile);
		FileInputStream in = new FileInputStream(sourceFile);
		FileOutputStream out = new FileOutputStream(targetFile);
		byte[] cache = new byte[CACHE_SIZE];
		int read = 0;
		while ((read = in.read(cache)) != -1) {
			out.write(cache, 0, read);
			out.flush();
		}
		in.close();
		out.close();
	}

	
	/**
	 * 复制文件夹
	 * @param sourceFile
	 * @param targetFile
	 * @param dirName
	 * @throws IOException
	 */
	public static void copyDir(String sourceDir, String targetDir) throws IOException{
		if(StringUtils.isBlank(sourceDir,targetDir)){
			throw new IOException("源文件夹或目的文件夹为空.");
		}
		copyDir(new File(sourceDir), new File(targetDir));
	}

	
	/**
	 * 复制文件夹
	 * @param sourceFile
	 * @param targetFile
	 * @param dirName
	 * @throws IOException
	 */
	public static void copyDir(File sourceDir, File targetDir) throws IOException{
		if(!sourceDir.exists()){
			throw new IOException("复制的文件夹不存在.["+sourceDir+"]");
		}
		if(!sourceDir.isDirectory()){
			throw new IOException("复制的文件夹不是文件夹.");
		}
		List<String> files = new ArrayList<String>();
		listAllSubFiles(sourceDir, true, files);
		for(String file : files){
			copyFile(getAbsolutePath(sourceDir.getAbsolutePath(), file), getAbsolutePath(targetDir.getAbsolutePath(), file));
		}
	}
	

	/**
	 * 移动文件(夹)
	 * @param sourceFile
	 * @param targetFile
	 * @throws IOException 
	 */
	public static void move(String sourceFile, String targetFile) throws IOException {
		if(StringUtils.isBlank(sourceFile,targetFile)){
			throw new IOException("原始文件或目标文件,文件名为空");
		}
		move(new File(sourceFile), new File(targetFile));
	}

	
	/**
	 * 移动文件(夹)
	 * @param sourceFile
	 * @param targetFile
	 * @throws IOException 
	 */
	public static void move(File sourceFile, File targetFile) throws IOException {
		if(!sourceFile.exists()){
			throw new IOException("移动的文件(夹)不存在.[" + sourceFile + "]");
		}
		Long l = System.currentTimeMillis();
		if(sourceFile.isDirectory()){
			moveDir(sourceFile, targetFile);
			Log.print("移动文件夹成功.耗时:"+(System.currentTimeMillis()-l)+"毫秒");
		}else if(sourceFile.isFile()){
			moveFile(sourceFile, targetFile);
			Log.print("移动文件成功.耗时:"+(System.currentTimeMillis()-l)+"毫秒");
		}
	}

	/**
	 * 移动文件
	 * @param sourceFile
	 * @param targetFile
	 * @throws IOException 
	 */
	public static void moveFile(String sourceFile, String targetFile) throws IOException{
		if(StringUtils.isBlank(sourceFile,targetFile)){
			throw new IOException("原始文件或目标文件,文件名为空");
		}
		moveFile(new File(sourceFile), new File(targetFile));
	}

	/**
	 * 移动文件
	 * @param sourceFile
	 * @param targetFile
	 * @throws IOException 
	 */
	public static void moveFile(File sourceFile, File targetFile) throws IOException{
		copyFile(sourceFile, targetFile);
		delFile(sourceFile);
	}
	
	/**
	 * 移动文件夹
	 * @param sourceFile
	 * @param targetFile
	 * @param dirName
	 * @throws IOException
	 */
	public static void moveDir(String sourceDir, String targetDir) throws IOException{
		if(StringUtils.isBlank(sourceDir,targetDir)){
			throw new IOException("移动文件夹或目的文件夹为空.");
		}
		moveDir(new File(sourceDir), new File(targetDir));
	}
	
	/**
	 * 移动文件夹
	 * @param sourceFile
	 * @param targetFile
	 * @param dirName
	 * @throws IOException
	 */
	public static void moveDir(File sourceDir, File targetDir) throws IOException{
		if(!sourceDir.exists()){
			throw new IOException("移动的文件夹不存在.["+sourceDir+"]");
		}
		if(!sourceDir.isDirectory()){
			throw new IOException("移动的文件夹不是文件夹.");
		}
		copyDir(sourceDir, targetDir);
		delDir(sourceDir);
	}
	
	/**
	 * 创建文件,不存在父目录时自动创建
	 * @param file
	 * @return
	 * @throws IOException 
	 */
	public static boolean createFile(String file) throws IOException{
		if(StringUtils.isBlank(file)){
			throw new IOException("要创建的文件名不能为空.");
		}
		return createFile(new File(file));
	}

	/**
	 * 创建文件,不存在父目录时自动创建
	 * @param file
	 * @return
	 * @throws IOException 
	 */
	public static boolean createFile(File file) throws IOException{
		if(file.exists() && file.isFile()){
			return true;
		}
		File parentDir = file.getParentFile();
		if(!parentDir.exists() || !parentDir.isDirectory()){
			parentDir.mkdirs();
		}
		return file.createNewFile();
	}
	/**
	 * 创建文件夹,不存在父目录时自动创建
	 * @param file
	 * @return
	 * @throws IOException 
	 */
	public static boolean createDir(String dir) throws IOException{
		if(StringUtils.isBlank(dir)){
			throw new IOException("要创建的文件夹名不能为空.");
		}
		return createDir(new File(dir));
	}
	
	/**
	 * 创建文件夹,不存在父目录时自动创建
	 * @param file
	 * @return
	 * @throws IOException 
	 */
	public static boolean createDir(File dir) throws IOException{
		if(dir.exists() && dir.isDirectory()){
			return true;
		}
		return dir.mkdirs();
	}
	
	
	
	/**
	 * 搜索指定文件下符合reg的某一行数据
	 * @param filePath
	 * @param regex
	 * @return
	 */
	public static List<String> searchLine(String filePath, String regex, int head) throws IOException{
		File file = new File(filePath);
		if(!file.exists() || !file.isFile()){
			Log.print("指定文件不存在或者路径指向不是一个文件.["+filePath+"]");
			return null;
		}
		
		List<String> ret = new ArrayList<String>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String line = reader.readLine();
			int i = 0;
			while (line != null) {
				if(i++ < head){
					line = reader.readLine();
					continue;
				}
				
				if(Pattern.matches(regex, line)){
					ret.add(line);
				}
				line = reader.readLine();
			}
			return ret;
		} finally{
			reader.close();
		}
		
	}
}
	
