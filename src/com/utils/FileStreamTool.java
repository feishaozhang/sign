package com.utils;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import com.sun.org.apache.xml.internal.security.utils.Base64;


public class FileStreamTool
{
    public static void main(String[] args) throws MalformedURLException, Exception {                //图片大小
        String fileStream = getPdf("C:\\001\\liubo.pdf");
        System.out.println(fileStream);
    }
    
    /**
     * 获取图片的Base64
     * @param filepath 文件路径
     * @return
     */
    public static String getImage(String filepath) {
    	String image = "";
    	
        File file =new File(filepath);
        if(filepath==null || filepath.equals("")){
          throw new NullPointerException("无效的文件路径");
        }
        
        long len = file.length();
        byte[] bytes = new byte[(int)len];

		BufferedInputStream bufferedInputStream = null;
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(file);
			bufferedInputStream = new BufferedInputStream(fileInputStream);
			int r = bufferedInputStream.read(bytes);
			if (r != len){
				throw new IOException("读取文件不正确");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bufferedInputStream != null) {
					bufferedInputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			image = Base64.encode(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return image;
	}
    
    
    /**
     * 获取PDF返回Base64
     * @param filepath
     * @return
     */
    public static String getPdf(String filepath) {
        File file = new File(filepath);
        if(filepath==null || filepath.equals("")) {
          throw new NullPointerException("无效的文件路径");
        }
        long len = file.length();
        byte[] bytes = new byte[(int)len];

        BufferedInputStream bufferedInputStream = null;
		try {
			bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
	        int r = bufferedInputStream.read( bytes );
	        if (r != len){
	        	throw new IOException("读取文件不正确");
	        }
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bufferedInputStream != null) {
					bufferedInputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
        String fileBaseb4 = Base64.encode(bytes);
        return fileBaseb4;

    }
    
    /**
     * 获取PDF返回byte流
     * @param filepath
     * @return
     */
    public static byte[] getPdfByte(String filepath) {

        File file = new File(filepath);
        if(filepath==null || filepath.equals(""))
        {
          throw new NullPointerException("无效的文件路径");
        }
        long len = file.length();
        byte[] bytes = new byte[(int)len];

        BufferedInputStream bufferedInputStream = null;
		try {
			bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
	        int r = bufferedInputStream.read( bytes );
	        if (r != len){
	        	throw new IOException("读取文件不正确");
	        }
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				if (bufferedInputStream != null) {
					bufferedInputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
        return bytes;
    }

    
    /**
     * 获取PDF返回Base64
     * @param filepath
     * @return
     */
    public static String decodeByteToBase64(byte[] bytes) {
    	if(bytes == null || bytes.length == 0){
    		return "";
    	}
        String fileBaseb4 = Base64.encode(bytes);
        return fileBaseb4;
    }
    
	/**
     * Description: 将base64编码内容转换为Pdf
     * @param  base64编码内容，文件的存储路径（含文件名）
     * @Author fuyuwei
     * Create Date: 2015年7月30日 上午9:40:23
     */
    public static void base64StringToPdf(String base64Content,String filePath){
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;

        try {
            byte[] bytes = Base64.decode(base64Content);//base64编码内容转换为字节数组
            ByteArrayInputStream byteInputStream = new ByteArrayInputStream(bytes);
            bis = new BufferedInputStream(byteInputStream);
            File file = new File(filePath);
            File path = file.getParentFile();
            if(!path.exists()){
                path.mkdirs();
            }
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);

            byte[] buffer = new byte[1024];
            int length = bis.read(buffer);
            while(length != -1){
                bos.write(buffer, 0, length);
                length = bis.read(buffer);
            }
            bos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
        	try {
        		if(bis != null && bis.available() > 0){
            		bis.close();
            	}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
        	
        	try {
        		if(fos != null){
        			fos.close();
            	}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
        	
        	try {
        		if(bos != null){
        			bos.close();
            	}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
        }
    }
}
