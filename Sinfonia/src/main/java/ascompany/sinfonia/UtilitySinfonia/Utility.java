/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ascompany.sinfonia.UtilitySinfonia;

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
}
