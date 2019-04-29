package com.wx.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;

import lombok.extern.log4j.Log4j;

/**
 *@Title 用于处理FTP文件的上传和下载
 *@Author TomcatBbzzzs
 *@Date 2019/02/08 22:36:21
 */
@Log4j
public class FTPUtil {
	
	private static String ftpIp=PropertiesUtil.getProperty("ftp.server.ip");
	private static String ftpUser=PropertiesUtil.getProperty("ftp.user");
	private static String ftpPass=PropertiesUtil.getProperty("ftp.pass");
	
	public FTPUtil(String ip,int port,String user,String pass) {
		this.ip=ip;
		this.port=port;
		this.user=user;
		this.pass=pass;
	}
	
	//批量上传文件的方法,返回是否上传成功
	public static boolean uploadFile(List<File> fileList) {
		FTPUtil ftpUtil=new FTPUtil(ftpIp,21,ftpUser,ftpPass);
		log.info("开始连接ftp服务器");
		boolean result = false;
		try {
			result = ftpUtil.uploadFile("img", fileList);
		} catch (IOException e) {
			log.error("上传文件异常==>FTPUtil.static_uploadFile",e);
			e.printStackTrace();
		}
		log.info("上传文件到ftp服务器的结果为:["+result+"]");
		return result;
	}
	
	/**
	 * 
	 * @param remotePath 远程路径文件夹
	 * @param fileList 多文件
	 * @return
	 * @throws IOException 
	 */
	private boolean uploadFile(String remotePath,List<File> fileList) throws IOException {
		boolean uploaded=true;
		FileInputStream fis=null;
		//连接ftp服务器
		if(connectServer(this.ip, this.port, this.user, this.pass)) {
			//切换ftp文件夹
			try {
				ftpClient.changeWorkingDirectory(remotePath);
				ftpClient.setBufferSize(1024);
				ftpClient.setControlEncoding("UTF-8");
				ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
				for(File fileItem:fileList) {
					fis=new FileInputStream(fileItem);
					ftpClient.storeFile(fileItem.getName(), fis);
				}
				
			} catch (IOException e) {
				log.error("上传文件异常==>FTPUtil.uploadFile",e);
				uploaded=false;
				e.printStackTrace();
			}finally {
				fis.close();
				ftpClient.disconnect();
			}
		}
		return uploaded;
	}
	
	//连接ftp服务器
	private boolean connectServer(String ip,int port,String user,String pass) {
		boolean isSuccess=false;
		ftpClient=new FTPClient();
		log.info("TomcatBbzzzs==>准备连接ftp服务器,ip=["+ip+"],port["+port+"],用户["+user+"],密码["+pass+"]");
		try {
			ftpClient.connect(ip);
			isSuccess=ftpClient.login(user, pass);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("连接ftp服务器异常",e);
		}
		return isSuccess;
	}
	private String ip;
	private int port;
	private String user;
	private String pass;
	private FTPClient ftpClient;
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public FTPClient getFtpClient() {
		return ftpClient;
	}
	public void setFtpClient(FTPClient ftpClient) {
		this.ftpClient = ftpClient;
	}
	
}
