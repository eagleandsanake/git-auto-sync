package org.wx.utils;

import java.io.*;
import java.util.Properties;

/**
 * @author wuxin
 * @date 2022/08/20 20:46:58
 */
public class PropertiesUtils {

    /**
     * read properties file from classPath
     * @param targetFile
     * @return
     * @throws IOException
     */
    public static Properties getPropertiesFromCP(String targetFile) throws IOException {
        Properties properties = new Properties();
        InputStream in = null;
        try {
            in = ClassLoader.getSystemClassLoader().getResourceAsStream(targetFile);
            properties.load(in);
        } catch (Exception e){

        } finally {
            if(in != null){
                in.close();
            }
        }
        return properties;
    }


    public static Properties getPropertiesFromDisk(String diskPath) throws IOException {
        Properties properties = new Properties();
        File file = new File(diskPath);
        FileInputStream fileInputStream = null;
        InputStreamReader inputStreamReader = null;
        try {
             fileInputStream = new FileInputStream(file);
             inputStreamReader = new InputStreamReader(fileInputStream,"utf-8");
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            properties.load(bufferedReader);
        } catch (Exception e) {

        } finally {
            if(fileInputStream != null){
                fileInputStream.close();
            }
        }
        return properties;
    }


}
