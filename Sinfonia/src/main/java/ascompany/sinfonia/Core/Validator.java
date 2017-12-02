package ascompany.sinfonia.Core;

import FunctionalInterface.ThrowingBiconsumer;
import FunctionalInterface.ThrowingConsumer;
import java.sql.ResultSet;

/**
 *
 * @author m.castano
 */
public class Validator
{
    /**
     * Consumer che controlla se una lista non è vuota 
     */
    public ThrowingConsumer notEmpty = (x) ->
    {   
        if(x != null)
        {
            ResultSet rs = (ResultSet)x;
            if(!rs.next())
            {
                throw new Exception();
            }
            else
            {
                rs.previous();
            }
        }
        else
        {
            throw new Exception();
        }
    };
    
    /**
     * Consumer che controlla se una lista non è vuota con la possibilità di poter passare un messaggio d'errore
     */
    public ThrowingBiconsumer notEmptyWithError = (x,y) ->
    {
        if(x != null && y != null)
        {
            ResultSet rs = (ResultSet)x;
            if(!rs.next())
            {
                throw new Exception((String) y);
            }
            else
            {
                rs.previous();
            }
        }
        else
        {
            throw new Exception();
        }
    };
}
