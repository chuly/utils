package com.bbq.util.pdf;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.swing.JOptionPane;

public class PDFUnzip {
	public static void main(String[] args) throws Exception {
		File inPath = new File("D:/tmp/51099");
//		String outPath = "D:/tmp/51099-unzip";
		String outPath = "D:/工作/pdf/51099/51099-unzip-all";
		int dCount = 0;
		int all = 0;
		for(File inPathSub : inPath.listFiles()){
			if(!inPathSub.isDirectory()){
				System.out.println("忽略输入的一级目录(不是目录)："+inPathSub);
			}else{
				dCount++;
				int fCount = 0;
//				String outPathSub = outPath+"/"+inPathSub.getName();
				for(File inFilePathName : inPathSub.listFiles()){
					all++;
					if(inFilePathName.isDirectory()){
						System.out.println("忽略输入的二级文件(不是文件)："+inFilePathName);
					}else{
						fCount++;
						System.out.print(all+",目录："+dCount+"，文件："+fCount+"------");
						String inFilePathNameStr = inFilePathName.getAbsolutePath();
						String houzhuiInfile = getFileHouzhui(inFilePathNameStr);
						if(".pdf".equalsIgnoreCase(houzhuiInfile)){
							try {
								System.out.println(inFilePathNameStr);
								copyFile(inFilePathNameStr, outPath+"/"+all+"-"+inFilePathName.getName(), false);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}else if(".zip".equalsIgnoreCase(houzhuiInfile)){
							try {
								System.out.println(inFilePathNameStr);
								unzip(inFilePathNameStr, outPath, ".pdf", all+"-");
							} catch (Exception e) {
								e.printStackTrace();
							}
						}else{
							System.out.println("不合法的文件后缀："+inFilePathNameStr);
						}
						
					}
				}
			}
		}
		
//		String inFilePathName = "D:\\tmp\\51099\\社会科学类 8";// 输入源zip路径
//		for(File f : new File(inFilePathName).listFiles()){
//			String outPath = "D:/tmp/1"; // 输出路径（文件夹目录）
//			System.out.println(f.getAbsolutePath());
//			unzip(f.getAbsolutePath(), outPath, ".pdf");
//		}
	}

	/**
	 * 解压zip文件
	 * @param inFilePathName
	 * @param outPath
	 * @param fileEndName
	 * @throws Exception
	 */
	private static void unzip(String inFilePathName, String outPath, String fileEndName,String outFilePre) throws Exception {
		File inFile = new File(inFilePathName);
		String inFileName = inFile.getName();
		if(inFileName.endsWith(".zip")){
			inFileName = inFileName.substring(0, inFileName.length()-4);
		}
		ZipInputStream zin = new ZipInputStream(new FileInputStream(inFile), Charset.forName("GBK"));// 输入源zip路径
		BufferedInputStream bin = new BufferedInputStream(zin);

		try {
			File fileOut = null;
			ZipEntry entry;
			SimpleDateFormat f = new SimpleDateFormat("yyyy-mm-dd_HH:mm:ss");
			while ((entry = zin.getNextEntry()) != null && !entry.isDirectory()) {
				if(!entry.getName().endsWith(fileEndName)){
					continue;
				}
				String fileHouzhui = getFileHouzhui(entry.getName());//得到文件后缀
				fileOut = new File(outPath, outFilePre+inFileName+fileHouzhui);
				int i = 2;
				while (fileOut.exists()) {
					fileOut = new File(outPath, outFilePre+inFileName+"-"+(i++)+"-"+f.format(new Date())+fileHouzhui);
				}
				new File(fileOut.getParent()).mkdirs();
				FileOutputStream out = new FileOutputStream(fileOut);
				BufferedOutputStream Bout = new BufferedOutputStream(out);
				int b;
				while ((b = bin.read()) != -1) {
					Bout.write(b);
				}
				Bout.close();
				out.close();
			}
		} finally{
			bin.close();
			zin.close();
		}
	}
	private static String getFileHouzhui(String url){
		String[] arr = url.split("\\.");
		return "."+arr[arr.length-1];
	}
	
	/** 
     * 复制单个文件 
     *  
     * @param srcFileName 
     *            待复制的文件名 
     * @param descFileName 
     *            目标文件名 
     * @param overlay 
     *            如果目标文件存在，是否覆盖 
     * @return 如果复制成功返回true，否则返回false 
     */  
    public static boolean copyFile(String srcFileName, String destFileName,  
            boolean overlay) {  
        File srcFile = new File(srcFileName);  
        String msg = null;
        // 判断源文件是否存在  
        if (!srcFile.exists()) {  
            msg = "源文件：" + srcFileName + "不存在！";  
            System.out.println(msg);  
            return false;  
        } else if (!srcFile.isFile()) {  
            msg = "复制文件失败，源文件：" + srcFileName + "不是一个文件！";  
            System.out.println(msg);  
            return false;  
        }  
  
        // 判断目标文件是否存在  
        File destFile = new File(destFileName);  
        if (destFile.exists()) {  
            // 如果目标文件存在并允许覆盖  
            if (overlay) {  
                // 删除已经存在的目标文件，无论目标文件是目录还是单个文件  
                new File(destFileName).delete();  
            }  
        } else {  
            // 如果目标文件所在目录不存在，则创建目录  
            if (!destFile.getParentFile().exists()) {  
                // 目标文件所在目录不存在  
                if (!destFile.getParentFile().mkdirs()) {  
                    // 复制文件失败：创建目标文件所在目录失败  
                    return false;  
                }  
            }  
        }  
  
        // 复制文件  
        int byteread = 0; // 读取的字节数  
        InputStream in = null;  
        OutputStream out = null;  
  
        try {  
            in = new FileInputStream(srcFile);  
            out = new FileOutputStream(destFile);  
            byte[] buffer = new byte[1024];  
  
            while ((byteread = in.read(buffer)) != -1) {  
                out.write(buffer, 0, byteread);  
            }  
            return true;  
        } catch (FileNotFoundException e) {  
            return false;  
        } catch (IOException e) {  
            return false;  
        } finally {  
            try {  
                if (out != null)  
                    out.close();  
                if (in != null)  
                    in.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
    }  
}
