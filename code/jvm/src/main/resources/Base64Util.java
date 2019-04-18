import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * Base64工具类（文件加密示例）
 * 
 */
public class Base64Util {

	static BASE64Encoder encoder = new BASE64Encoder();   //加密
	static BASE64Decoder decoder = new BASE64Decoder();   //解密
	
    public static String fileToBase64(String fileName) throws IOException {
        String strBase64 = null;
        InputStream in = null;
        try {
            in = new FileInputStream(fileName);
            byte[] bytes = new byte[in.available()];
            in.read(bytes);
            strBase64 = encoder.encodeBuffer(bytes).trim();
        } catch (FileNotFoundException fe) {
            fe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }finally{
            if(in!=null)
                in.close();     
        }
        return strBase64;
    }
    
    /**
     * 加密文件	示例
     * @param srcFile	初始文件名称
     * @param encFile	加密文件名称
     * @throws IOException
     */
    public static void EncFile(String srcFile, String encFile) throws IOException{
    	String strBase64 = null;
    	OutputStream fos = null;
		try {
			strBase64 = fileToBase64(srcFile);
			if(strBase64 != null && !"".equals(strBase64.trim())){
	    		byte[] bytes = strBase64.getBytes();      
	    		fos = new FileOutputStream(encFile);
	    		fos.write(bytes);
	    		
	    		fos.flush();
	    	}
			System.out.println("加密完成：" + srcFile + "=====>>>" + encFile);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
            if(fos != null)
            	fos.close();     
        }
    	
    }
    
    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
		//文件加密
//    	EncFile("H:/Snap/JPG/20171211/270171201858232448.jpg", "H:/Snap/JPG/20171213/270171201858232448.jpg");
		//文件解密
//		DecFile("H:/Snap/JPG/20171213/270171201858232448.jpg", "H:/Snap/JPG2/20171213/270171201858232448.jpg");
		//对目录下文件进行解密
//		DecDirFile(args[0], args[1]);
    	
//    	判定参数个数
    	if (args != null){
    		if(args.length == 2){
    			DecDirFile(args[0], args[1]);
    		}else if(args.length == 3){
    			DecDirFile(args[0], args[1], args[2]);
    		}else if(args.length == 4){
    			DecDirFile(args[0], args[1], args[2], args[3]);
    		}else{
    			System.out.println("参数输入个数为【" + args.length + "】个，没有对应方法！");
    		}
    	}
    	
//		DecDirFile("H:/Snap/JPG/renzheng", "H:/Snap/JPG/renzheng-dec");
//		DecDirFile("H:/Snap/JPG/renzheng", "H:/Snap/JPG/renzheng-dec", 10);
//		DecDirFile("H:/Snap/JPG/renzheng-jm", "H:/Snap/JPG/renzheng-jm1");
    }
    

	//=================以下为文件解密==========================

	private static SimpleDateFormat dateFormat = new SimpleDateFormat();
    /**
     * 删除逾期加密文件
     * 
     * @param decDir 解密文件放置目录
     * @param overdueDate 逾期时间，小于1不删除
     * 
     * @author yueyuanqing
     */
    public static void delOverdueFiles(String encDir, String decDir, String overdueDate){
    	try {
    		int delFileNum = 0;
        	//小于1不删除
    		if (overdueDate != null && isNumeric(overdueDate)){
        		//所有解密文件名称集合txt
            	String decFileList = decDir + File.separator + "dec_file_name.txt";
            	File decFile = new File(decFileList);
            	if (decFile.exists()){
            		File file = new File(encDir);

            		List<File> fileList = new ArrayList<File>();
                	// 获取目录下的所有文件或文件夹 
                	File[] files = file.listFiles();
                	
                	// 如果目录为空，直接退出  
                	if (files == null) {
                        return;  
                    }
                	
                	// 遍历目录（包含子目录）下的所有文件
                    for (File f : files) {  
                        if (f.isFile()) {  
                            fileList.add(f);  
                        } else if (f.isDirectory()) {  
                            DecDirFile(f.getAbsolutePath(), decDir); 
                        }  
                    }
                	
                    List<String> list = new ArrayList<String>();
                    // 正解
                    List<String> list2 = new ArrayList<String>();
                    list2.add("");
                    BufferedReader reader = new BufferedReader(new FileReader(decFile));
                    String str = "";
                    while ((str = reader.readLine()) != null){
                    	list.add(str);
                    }
                    list.removeAll(list2);
                    
                    //获取逾期时间
            		dateFormat.applyPattern("yyyyMMdd");
//            		String overDate = dateFormat.format(date);

            	    Calendar calendar = Calendar.getInstance();
            	    calendar.add(Calendar.DATE, -Integer.parseInt(overdueDate));
            	    
            	    List<String> fileNameList = new ArrayList<String>(); 
            	    
            	    //获取加密文件集合与解密文件集合交集
            	    for (File f : fileList){
            	    	fileNameList.add(f.getName());
            	    }
            	    fileNameList.retainAll(list);
            	    
            	    List<File> lists = new ArrayList<File>();
            	    for (File f : fileList){
            	    	for (String s : fileNameList){
            	    		if (f.getName().equals(s)){
            	    			lists.add(f);
            	    		}
            	    	}
            	    }
            	    
            	    //删除已解密的加密文件名称
            	    List<String> needDelDecFileList = new ArrayList<String>();
            	    
                    //删除加密文件：判定逾期文件是否被解密，否则不删除
                    for (File f : lists){
                    	long modifyLongDate = f.lastModified();
                    	Date modifyDate = new Date(modifyLongDate);
                    	if (modifyDate.before(calendar.getTime())){
                            
                            //删除解密文件列表
                    		if (f.delete()){
                    			needDelDecFileList.add(f.getName());
                    			delFileNum ++;
                    		}
                    	}
                    }
                    
                  //未删除加密文件名称
                    fileNameList.removeAll(needDelDecFileList);
                    String writeContent = "";
                    for (String s : fileNameList){
                    	writeContent += s + System.getProperty("line.separator");
                    }
                    
                    //未删除加密文件名称重新分行写入txt中
                    FileOutputStream fileOutputStream=null;
                    try {
                        fileOutputStream = new FileOutputStream(decFileList);
                        fileOutputStream.write(writeContent.getBytes());
                        fileOutputStream.close();
                    } catch (Exception e) {
                        System.out.println("未删除加密文件名称重新分行写入txt中失败，原因：" + e.getMessage());
                    }
                    
                    System.out.println("从【" + encDir + "】加密目录下，共计删除【" + overdueDate + "】天前已解密的【" + delFileNum + "】个加密文件！");
                    System.out.println("从【" + decFileList + "】中读取已解密的加密文件列表，共计删除文件【" + needDelDecFileList.size() + "】个！");
            	}
        	}
		} catch (Exception e) {
			System.out.println("删除逾期文件失败，原因：" + e.getMessage());
		}
    }
    
    /*
     * 对目录下所有文件进行解密
     * （加密目录下包含子目录则全部解密到解密目录下，文件名称相同则会覆盖）
     * 解密后删除原加密目录下过期文件
     * encDir：加密目录
     * decDir：解密目录
     */
    private static void DecDirFile(String encDir, String decDir) {

    	//删除过期加密文件
    	Base64Util.delOverdueFiles(encDir, decDir, "100");
    	
    	//解密文件数量
    	int decFileNum = 0;
    	try {
        	System.out.println("加密目录："+encDir); 
        	System.out.println("解密目录："+decDir); 
    		List<File> fileList = new ArrayList<File>();
        	File file = new File(encDir);
        	
        	// 获取目录下的所有文件或文件夹 
        	File[] files = file.listFiles();
        	
        	// 如果目录为空，直接退出  
        	if (files == null) {
                return;  
            }

        	// 遍历目录（包含子目录）下的所有文件
            for (File f : files) {  
                if (f.isFile()) {  
                    fileList.add(f);  
                } else if (f.isDirectory()) {  
                    DecDirFile(f.getAbsolutePath(), decDir); 
                }  
            }

            /**
             * 文件解密：
             * 1、解密前判断解密目录下是否包含此文件；
             * 2、txt文件解密内容，其他文件对文件解密；
             */
            //获取需要解密的文件列表
            fileList = Base64Util.getEncFile(fileList, decDir);

        	if (fileList != null){
        		
        		System.out.println("加密目录："+encDir + " 下共有 " + fileList.size() + " 个文件需要解密！");
        		System.out.println("解密文件开始......");
        		
        		//每100条打印一次
        		int once = 100;
        		for (File f1 : fileList) {
                	if (f1.getName() != null){
                		
                		//文件分日期目录存储
    					long modifyTime = f1.lastModified();
    					Date modifyDate = new Date(modifyTime);
    					String fileDate = new SimpleDateFormat("yyyyMMdd").format(modifyDate);
    					File fileDateName = new File(decDir + File.separator + fileDate);
    					if (fileDateName.mkdirs()){
    						System.out.println("创建目录【" + fileDateName.getAbsolutePath() + "】成功！");
    					}
    					String fileType = f1.getName().substring(f1.getName().lastIndexOf(".") + 1);
    					String fileName = fileDateName + File.separator + f1.getName();
    					if ("txt".equals(fileType)){
    						DecFileContent(f1.getCanonicalPath(), fileName);
    					}else{
    						DecFile(f1.getCanonicalPath(), fileName);
    					}
    					
    					//解密后，需要将文件名称分行写入解密文件列表txt中
    		            Base64Util.writeFileOfDec(encDir, decDir, f1.getName());
    		            
    					//加解密目录不相同不能删除文件
    					if ( !encDir.equals(decDir)){
    						//解密后删除加密文件
    						//f1.delete();
    					}
    					decFileNum ++;
    					if (decFileNum % once == 0 && decFileNum != 0){
    						System.out.println("成功解密 " + decFileNum + " 个文件......");
    					}
                	}
                }
        		System.out.println("解密文件结束......");
        		System.out.println("加密目录："+encDir + " 下共有 " + fileList.size() + " 个文件需要解密，" + "成功解密 " + decFileNum + " 个文件！");
        	}
            
		} catch (Exception e) {
			System.out.println("文件解密失败，共成功解密 " + decFileNum + " 个文件！");
		}
  	}
    
    /*
     * 对目录下所有文件进行解密
     * （加密目录下包含子目录则全部解密到解密目录下，文件名称相同则会覆盖）
     * 解密后删除原加密目录下过期文件
     * encDir：加密目录
     * decDir：解密目录
     * overdueDate 过期时间
     */
    private static void DecDirFile(String encDir, String decDir, String overdueDate) {
    	
    	//删除过期加密文件
    	Base64Util.delOverdueFiles(encDir, decDir, overdueDate);
    	
    	//解密文件数量
    	int decFileNum = 0;
    	try {
    		System.out.println("加密目录："+encDir); 
    		System.out.println("解密目录："+decDir); 
    		List<File> fileList = new ArrayList<File>();
    		File file = new File(encDir);
    		
    		// 获取目录下的所有文件或文件夹 
    		File[] files = file.listFiles();
    		
    		// 如果目录为空，直接退出  
    		if (files == null) {
    			return;  
    		}
    		
    		// 遍历目录（包含子目录）下的所有文件
    		for (File f : files) {  
    			if (f.isFile()) {  
    				fileList.add(f);  
    			} else if (f.isDirectory()) {  
    				DecDirFile(f.getAbsolutePath(), decDir); 
    			}  
    		}
    		
    		/**
    		 * 文件解密：
    		 * 1、解密前判断解密目录下是否包含此文件；
    		 * 2、txt文件解密内容，其他文件对文件解密；
    		 */
    		//获取需要解密的文件列表
    		fileList = Base64Util.getEncFile(fileList, decDir);
    		
    		if (fileList != null){

        		System.out.println("加密目录："+encDir + " 下共有 " + fileList.size() + " 个文件需要解密！");
        		System.out.println("解密文件开始......");
        		
        		//每100条打印一次
        		int once = 100;
        		
    			for (File f1 : fileList) {
    				if (f1.getName() != null){
    					
    					//文件分日期目录存储
    					long modifyTime = f1.lastModified();
    					Date modifyDate = new Date(modifyTime);
    					String fileDate = new SimpleDateFormat("yyyyMMdd").format(modifyDate);
    					File fileDateName = new File(decDir + File.separator + fileDate);
    					if (fileDateName.mkdirs()){
    						System.out.println("创建目录【" + fileDateName.getAbsolutePath() + "】成功！");
    					}
    					String fileType = f1.getName().substring(f1.getName().lastIndexOf(".") + 1);
    					String fileName = fileDateName + File.separator + f1.getName();
    					if ("txt".equals(fileType)){
    						DecFileContent(f1.getCanonicalPath(), fileName);
    					}else{
    						DecFile(f1.getCanonicalPath(), fileName);
    					}
    					
    					//解密后，需要将文件名称分行写入解密文件列表txt中
    					Base64Util.writeFileOfDec(encDir, decDir, f1.getName());
    					
    					//加解密目录不相同不能删除文件
    					if ( !encDir.equals(decDir)){
    						//解密后删除加密文件
    						//f1.delete();
    					}
    					decFileNum ++;
    					if (decFileNum % once == 0 && decFileNum != 0){
    						System.out.println("成功解密 " + decFileNum + " 个文件......");
    					}
    				}
    			}
    			System.out.println("加密目录："+encDir + " 下共有 " + fileList.size() + " 个文件需要解密，" + "成功解密 " + decFileNum + " 个文件！");
    		}
    		
    	} catch (Exception e) {
    		System.out.println("文件解密失败，共成功解密 " + decFileNum + " 个文件！");
    	}
    }
    
    /*
     * 对目录下某个文件进行解密
     * （加密目录下包含子目录则全部解密到解密目录下，文件名称相同则会覆盖）
     * 解密后删除原加密目录下过期文件
     * encDir：加密目录
     * decDir：解密目录
     * encFileName：加密文件名称
     * overdueDate：过期时间
     */
    private static void DecDirFile(String encDir, String decDir, String encFileName, String overdueDate) {
    	
    	//删除过期加密文件
    	Base64Util.delOverdueFiles(encDir, decDir, overdueDate);
    	
    	try {
    		System.out.println("加密目录：" + encDir); 
    		System.out.println("加密文件：" + encFileName); 
    		System.out.println("解密目录：" + decDir); 
    		
    		if (encDir != null && encFileName != null){
    			String encFile = encDir + File.separator + encFileName;
    			String fileName = decDir + File.separator + encFileName;
        		File encFileEnd = new File(encFile);
				String fileType = encFileEnd.getName().substring(encFileEnd.getName().lastIndexOf(".") + 1);
				if ("txt".equals(fileType)){
					DecFileContent(encFileEnd.getCanonicalPath(), fileName);
				}else{
					DecFile(encFileEnd.getCanonicalPath(), fileName);
				}
    		}
    		
			System.out.println("加密目录："+encDir + "，解密文件【" + encFileName + "】成功！");
    		
    	} catch (Exception e) {
			System.out.println("加密目录："+encDir + "，解密文件【" + encFileName + "】失败！");
    	}
    }
    
    /**
     * 获取所有需要解密的加密文件
     * 
     * PS：add by author：yueyuanqing；date：2018-02-26
     * @param fileList	所有加密文件列表
     * @param decDir	解密文件放置目录
     * 
     * @author yueyuanqing
     */
	private static List<File> getEncFile(List<File> fileList, String decDir){
    	FileInputStream fis = null;  
    	InputStreamReader isr = null;
    	
    	//用于包装InputStreamReader，提高处理性能。因为BufferedReader有缓冲的，而InputStreamReader没有。
    	BufferedReader br = null; 
    	try {
            if (fileList != null && fileList.size() > 0){
   		      	
            	//所有需要解密的加密文件
            	List<File> needDecFileList = new ArrayList<File>();
            	List<String> fileNameList = new ArrayList<String>();
            	List<String> tempList = new ArrayList<String>();
            	
            	//所有解密文件名称集合txt
            	String decFileList = decDir + File.separator + "dec_file_name.txt";
            	File decFile = new File(decFileList);
            	if (!decFile.exists()){
            		try {    
            			decFile.createNewFile();    
            		} catch (IOException e) {    
            			System.out.println("创建文件【" + decFileList + "】失败，原因：" + e.getMessage());    
            		} 
            	}

   		        fis = new FileInputStream(decFile);
   		     
   		        // 从文件系统中的某个文件中获取字节
   		        // InputStreamReader 是字节流通向字符流的桥梁
   		        isr = new InputStreamReader(fis);
   		      
   		        // 从字符输入流中读取文件中的内容,封装了一个new InputStreamReader的对象
   		        br = new BufferedReader(isr);
   		      
   		        String str = "";
   		        while ((str = br.readLine()) != null) {
   		        	fileNameList.add(str);
   		     	}
            	
   		        if (fileNameList.size() < 1){
   		        	return fileList;
   		        }
   		        
   		        //去除重复数据
   		        /*Set set = new  HashSet(); 
   		        for (String cd : fileNameList) {
	   	            if(set.add(cd)){
	   	            	tempList.add(cd);
	   	            }
	   	        }*/
   		        
            	for (File f : fileList){
            		tempList.add(f.getName());
            	}
            	tempList.removeAll(fileNameList);
            	for (File f : fileList){
                	for (String name : tempList){
                		if (name.equals(f.getName())){
                			needDecFileList.add(f);
                		}
                	}
            	}
            	return needDecFileList;
            }
		} catch (Exception e) {
			System.out.println("获取所有需要解密的加密文件失败，原因：" + e.getMessage());
		} finally {
			try {
				 //关闭的时候最好按照先后顺序关闭，最后开的先关闭，所以先关s，再关n，最后关m
				if (br != null){
				       br.close();  
				}
				if (isr != null){
					isr.close();  
				}
				if (fis != null){
					fis.close();  
				}
			} catch (Exception e2) {
				System.out.println("关闭流失败，原因：" + e2.getMessage());
			}
	    }
		return null;
    }
    
    /**
     * 将解密文件名称分行写入文件中
     * 
     * PS：add by author：yueyuanqing；date：2018-02-26
     * @param encDir
     * @param decDir	解密文件放置目录
     * @param fileName	解密文件名称
     * 
     * @author yueyuanqing
     */
    private static void writeFileOfDec(String encDir, String decDir, String fileName){
    	
    	//所有解密文件名称集合txt
    	String decFileList = decDir + File.separator + "dec_file_name.txt";
    	
    	try {
            File decFile = new File(decFileList);
            if (!decFile.exists()){
            	try {    
            		decFile.createNewFile();    
            	} catch (IOException e) {    
            		System.out.println("创建文件【" + decFileList + "】失败，原因：" + e.getMessage());    
            	} 
            }
		    
	      	BufferedReader reader = new BufferedReader(new FileReader(decFile));
	      	String tempString = null;
	      	boolean isHave = false;
	      	while ((tempString = reader.readLine()) != null) {  
	      		if (tempString.equals(fileName)){
	    	      	isHave = true;
	    	      	break;
	      		}
	      	}
	      	if (!isHave){
		      	//写入文件
	            FileWriter fw = new FileWriter(decFile.getAbsolutePath(), true);
	            fw.write(fileName + System.getProperty("line.separator"));
				fw.flush();
				fw.close();	
	      	}
			
		} catch (Exception e) {
			System.out.println("写入文件失败，原因：" + e.getMessage());    
	    } finally {
	    	
	    }
    }
    
    /**
     * 分行写入文件
     * @param encDir	加密文件目录
     * @param decDir	解密文件放置目录
     * @param fileList	加密文件列表
     * 
     * 解密文件步骤：
     * 1、加、解密目录下生成用于存储文件名称txt；
     * 2、解密文件前判定存储文件名称txt中是否包含所有加密文件；若无，则添加；
     * 3、定期删除规定时间外的文件，存储文件名称与目录中文件数目保持一致；
     * 4、解密文件后，将文件名称分行保存在解密目录下的文件名称txt中；
     * 5、循环执行解密脚本命令，避免现场执行linux命令超时；
     * 
     * @author yueyuanqing
     */
    public static void writeFile(String encDir, String decDir, File[] fileList){
    	
    	//所有加密文件名称集合txt
    	String encFileList = encDir + File.separator + "enc_file_name.txt";
    	
    	//所有解密文件名称集合txt
    	String decFileList = decDir + File.separator + "dec_file_name.txt";
    	
    	FileInputStream fis = null;  
    	InputStreamReader isr = null;
    	//用于包装InputStreamReader，提高处理性能。因为BufferedReader有缓冲的，而InputStreamReader没有。
    	BufferedReader br = null; 
    	try {
            File encFile = new File(encFileList);
            File decFile = new File(decFileList);
            if (!encFile.exists()){
            	try {    
            		encFile.createNewFile();    
                } catch (IOException e) {    
                    System.out.println("创建文件【" + encFileList + "】失败，原因：" + e.getMessage());    
                } 
            }
            if (!decFile.exists()){
            	try {    
            		decFile.createNewFile();    
            	} catch (IOException e) {    
            		System.out.println("创建文件【" + decFileList + "】失败，原因：" + e.getMessage());    
            	} 
            }
            
		     // FileInputStream
		     OutputStream fos = null;
		     fis = new FileInputStream(decFile);        
		     // 从文件系统中的某个文件中获取字节
		     // InputStreamReader 是字节流通向字符流的桥梁
		      isr = new InputStreamReader(fis);
		      // 从字符输入流中读取文件中的内容,封装了一个new InputStreamReader的对象
		      br = new BufferedReader(isr);
              String str = "";
		      String str1 = "";
		      	while ((str = br.readLine()) != null) {
				  str1 += str;
			    }
		      	System.out.println("文件内容str1：" + str1);
		      	
				//将加密文件名称分行存储在txt文件中
				//分行存储文件名称
				for (File f1 : fileList) {
					str1 += f1.getName() + System.getProperty("line.separator");
				}
				
				System.out.println("文件名称：" + str1);
				byte[] bytes1 = decoder.decodeBuffer(str1);
				if (fos == null){
					  fos = new FileOutputStream(decFile);
				}
				fos.write(bytes1);
				fos.flush();
				
		} catch (Exception e) {
			System.out.println("写入文件失败，原因：" + e.getMessage());    
	    } finally {  
		     try {
				 //关闭的时候最好按照先后顺序关闭，最后开的先关闭，所以先关s，再关n，最后关m
				if (br != null){
				       br.close();  
				}
				if (isr != null){
					isr.close();  
				}
				if (fis != null){
					fis.close();  
				}
			} catch (IOException e) {  
		    	 e.printStackTrace();  
		     }  
	    }
    }
    
    public static String base64ToFile(String strBase64, String fileName) throws IOException {
        ByteArrayInputStream in = null;
        FileOutputStream out = null;
        try {
            byte[] bytes = decoder.decodeBuffer(strBase64);      
            in = new ByteArrayInputStream(bytes);
            byte[] buffer = new byte[1024];
            out = new FileOutputStream(fileName);
            int byteread = 0;
            while ((byteread = in.read(buffer)) != -1) {
                out.write(buffer, 0, byteread); // 文件写操作
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }finally{
            if(out!=null)
                out.close();
            if(in!=null)
                in.close();
        }
        return fileName;
    }

    /**
     * 解密文件每一行
     * @param encFile	加密文件路径
     * @param decFile	解密文件路径
     */
    private static void DecFileContent(String encFile,String decFile){
    	FileInputStream fis = null;  
	    InputStreamReader isr = null;
	    //用于包装InputStreamReader,提高处理性能。因为BufferedReader有缓冲的，而InputStreamReader没有。
	    BufferedReader br = null;   
	    try {  
	    	File encFileEnd = new File(decFile);
		     String str = "";
		     // FileInputStream
		     OutputStream fos = null;
		     fis = new FileInputStream(encFile);        
		     // 从文件系统中的某个文件中获取字节
		     // InputStreamReader 是字节流通向字符流的桥梁,
		      isr = new InputStreamReader(fis);
		      // 从字符输入流中读取文件中的内容,封装了一个new InputStreamReader的对象
		      br = new BufferedReader(isr);
		      String str1 = "";
			while ((str = br.readLine()) != null) {
				  str1 += str;
			  }
			byte[] bytes1 = decoder.decodeBuffer(str1);
			  if (fos == null)
			  {
				  fos = new FileOutputStream(encFileEnd);
			  }
			  fos.write(bytes1);
			  fos.flush();
			  
	   		 //System.out.println("解密完成：" + encFile + " =====>>> " + decFile);
	    } catch (FileNotFoundException e) {  
	    	System.out.println("找不到指定文件！");  
	    } catch (IOException e) {  
	    	System.out.println("读取文件失败！");  
	    } finally {  
		     try {
				 //关闭的时候最好按照先后顺序关闭，最后开的先关闭，所以先关s，再关n，最后关m
				if (br != null){
				       br.close();  
				}
				if (isr != null){
					isr.close();  
				}
				if (fis != null){
					fis.close();  
				}
			} catch (IOException e) {  
		    	 e.printStackTrace();  
		     }  
	    }  
    }

    /**
     * 解密
     * @param encFile	加密文件路径
     * @param decFile	解密文件路径
     */
	private   static void DecFile(String encFile,String decFile){      
        try {      
        	InputStream fis  = new FileInputStream(encFile);        	
            byte[] bytes1 = decoder.decodeBuffer(fis);      
            
            OutputStream fos = new FileOutputStream(decFile);
            fos.write(bytes1);
   		 
	   		 fis.close();
	   		 fos.flush();
	   		 fos.close();

        } catch (IOException e) {      
            e.printStackTrace();      
        }
   
    } 
	
	//方法一：用JAVA自带的函数
	public static boolean isNumeric(String str){
	    for (int i = str.length();--i>=0;){  
	        if (!Character.isDigit(str.charAt(i))){
	            return false;
	        }
	    }
	    return true;
	}
}