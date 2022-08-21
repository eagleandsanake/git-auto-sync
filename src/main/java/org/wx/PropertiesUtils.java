package org.wx;

import lombok.Data;

import java.io.IOException;
import java.io.InputStream;
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




}
