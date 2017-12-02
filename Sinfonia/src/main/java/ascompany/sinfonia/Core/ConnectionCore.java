package ascompany.sinfonia.Core;

import ascompany.sinfonia.Configurazione.ConfigName;
import ascompany.sinfonia.UtilitySinfonia.Utility;
import static ascompany.sinfonia.UtilitySinfonia.Utility.convertFileToJson;
import com.google.gson.JsonObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 
 * @author m.castano
 */
public class ConnectionCore
{
    /**
     * Oggetto di tipo connection 
     */
    private Connection c = null;
    
    public ConnectionCore openConnectionToDB(String pathDbConfig) throws Exception
    {
        JsonObject DBConfigFile = convertFileToJson(pathDbConfig);
        
        Utility.validateConfigJson(DBConfigFile);
        
        Class.forName(DBConfigFile.get(ConfigName.DRIVER).getAsString());
        c = DriverManager.getConnection
        (
            (
                DBConfigFile.get(ConfigName.IP).getAsString() + ":" + 
                DBConfigFile.get(ConfigName.PORT).getAsString() +"/"+ DBConfigFile.get(ConfigName.DATABASE).getAsString() + ConfigName.PREFIX_TIMEZONE + DBConfigFile.get(ConfigName.TIMEZONE).getAsString()
            ),    
            DBConfigFile.get(ConfigName.USERNAME).getAsString(),
            DBConfigFile.get(ConfigName.PASSWORD).getAsString()
        );
        return this;
    }
    
    public ConnectionCore openConnection(String pathDbConfig) throws Exception
    {
        JsonObject DBConfigFile = convertFileToJson(pathDbConfig);
        Class.forName(DBConfigFile.get(ConfigName.DRIVER).getAsString());
        c = DriverManager.getConnection
        (
            (
                DBConfigFile.get(ConfigName.IP).getAsString() + ":" + 
                DBConfigFile.get(ConfigName.PORT).getAsString() + ConfigName.PREFIX_TIMEZONE + DBConfigFile.get(ConfigName.TIMEZONE).getAsString()
            ),    
            DBConfigFile.get(ConfigName.USERNAME).getAsString(),
            DBConfigFile.get(ConfigName.PASSWORD).getAsString()
        );
        return this;
    }
    
    /**
     * Ricava la connessione d'istanza
     * 
     * @param commitMode modalità in cui viene aperta la connessione per quanto riguarda l'auto commit
     * @throws SQLException 
     */
    public Connection fetchConnection(boolean commitMode) throws SQLException
    {
        c.setAutoCommit(commitMode);
        return getConnection();
    }

    /**
     * 
     * @return Ritorna la connessione
     */
    public Connection getConnection()
    {
        return c;
    }

    public void setC(Connection c) 
    {
        this.c = c;
    }
    
    
    /**
     * Funzione che chiude la connessione 
     */
    public void closeConnection()
    {
        try
        {
            if(c != null && !c.isClosed())
            {
                c.close();
            }
        } 
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }
    
    /**
     * Funzione per eseguire il commit
     * 
     * @throws SQLException 
     */
    public void doCommit() throws SQLException
    {
        if(c != null && !c.isClosed())
        {
            c.commit();
        }
    }
    
    /**
     * Funzione per eseguire il rollback 
     */
    public void doRollback() 
    {
        try
        {
            if(c != null && !c.isClosed())
            {
                c.rollback();
            }
        } 
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }
}
