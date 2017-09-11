/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ascompany.sinfonia.UtilitySinfonia;

import ascompany.sinfonia.Configurazione.ConfigName;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.FileReader;

/**
 *
 * @author m.castano
 */
public class Utility 
{
    /**
     * Funzione che viene utilizzata per trasformare un file json in un JsonObject, passando come parametro il percorso
     * 
     * @param filename path del file che bisogna convertire in JsonObject
     * @return 
     */
    public static JsonObject convertFileToJson(String filename)
    {   
        try
        {            
            BufferedReader br = new BufferedReader(new FileReader(filename));     
            if (br.readLine() == null) 
            {
                return null;
            }
            return new JsonParser().parse(new FileReader(filename)).getAsJsonObject();
        } 
        catch (Exception ex)
        {
            ex.printStackTrace();
            return null;
        }
    }
    
    public static boolean validateConfigJson(JsonObject DBConfigFile) throws Exception
    {
        if(!DBConfigFile.has(ConfigName.DRIVER))
        {
            throw new Exception("Propety " + ConfigName.DRIVER + " missing");
        }
        if(!DBConfigFile.has(ConfigName.IP))
        {
            throw new Exception("Propety " + ConfigName.IP + " missing");
        }
        if(!DBConfigFile.has(ConfigName.PORT))
        {
            throw new Exception("Propety " + ConfigName.PORT + " missing");
        }
        if(!DBConfigFile.has(ConfigName.USERNAME))
        {
            throw new Exception("Propety " + ConfigName.USERNAME + " missing");
        }
        if(!DBConfigFile.has(ConfigName.PASSWORD))
        {
            throw new Exception("Propety " + ConfigName.PASSWORD + " missing");
        }
        return true;
    }
}
