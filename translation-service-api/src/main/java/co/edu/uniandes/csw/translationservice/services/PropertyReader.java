/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.translationservice.services;

import java.io.*;
import java.util.*;
/**
 *
 * @author jhony
 */
public class PropertyReader {
    
    private static Properties prop = null;

    public static void initializePropertyReader(){

        InputStream is = null;
        try {
            prop = new Properties();

            is = Thread.currentThread().getContextClassLoader().getResourceAsStream("mail.properties");
            //String log4jConfPath = System.getProperty("user.dir") + "/config/log.properties";
            if(is == null) {
                System.out.println("No se puedo cargar el archivo mail.properties");
            }
            else {
                System.out.println("is: "+is);
                prop.load(is);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Set<Object> getAllKeys(){
        Set<Object> keys = prop.keySet();
        return keys;
    }

    public static String getPropertyValue(String key){
        return prop.getProperty(key);
    }

    public static Properties getProperties() {
        return prop;
    }

}
