package ascompany.sinfonia.Core;

import FunctionalInterface.ThrowingBiconsumer;
import FunctionalInterface.ThrowingConsumer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 
 * @author m.castano
 * @param <T> ResultSet
 * @param <K> Tipo di ritorno
 * @param <V> Tipo a cui viene mappato il ResultSet
 */
public class QueryCore<T,K,V>
{
    /**
     * Stringa utilizzata per settare la query da eseguire 
     */
    private String query;
    
    /**
     * Lista dei parametri che vengono utilizzati per rimpizziare i "?" (Ricordare di passarli in ordine) 
     */
    private List<Object> l = null;
    
    /**
     * Prepared statement utilizzato per effettuare la query 
     */
    private PreparedStatement prepStm;
    
    /**
     * ResultSet della query eseguita 
     */
    private ResultSet rs;
    
    /**
     * Alla fine risulta essere o il JsonElement o la model 
     */
    private K response;
    
    /**
     * Key rappresenta l'id inserito dopo aver effettuato una query 
     */
    private int key;
    
    /**
     * C rappresenta la classe con la quale verrà mappato il resultset 
     */
    private Class c;
    
    /**
     * Costruttore con query e la lista dei parametri da settare in ordine
     * 
     * @param query Query da eseguire
     * @param l Lista dei parametri dei prepared statement
     */
    public QueryCore(String query,List<Object> l)
    {
        this.query = query;
        this.l = l;
    }
    
    /**
     * Costruttore solo con query dove non bisogna settare nessun parametro
     * 
     * @param query Query da eseguire
     */
    public QueryCore(String query)
    {
        this.query = query;
    }
    
    /**
     * Costruttore vuoto
     */
    public QueryCore()
    {
        
    }
    
    
    
    /**
     * La funzione di init viene utilizzata per inizializzare il prepared statement data una connessione come parametro
     * 
     * @param c Connessione
     * @return Istanza della classe
     * @throws SQLException 
     */
    public QueryCore init(Connection c) throws SQLException
    {
        prepStm = c.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
        return this;
    }
    
    /**
     * La funzione setta la stringa della query con i relativi parametri per i "?". Ricordare l'ordine per i "?".
     * 
     * 
     * @param query
     * @param l
     * @return 
     */
    public QueryCore loadQuery(String query, List<Object> l)
    {
        this.query = query;
        this.l = l;
        
        return this;
    }
    
    
    /**
     * La funzione build query viene utilizzata per rimpiazzare i "?" con i parametri della lista
     * 
     * @return Istanza della classe
     * @throws SQLException 
     */
    public QueryCore buildQuery() throws SQLException
    {
        if(l != null)
        {
            for(int i=0;i<l.size();i++)
            {
                prepStm.setObject(i+1,l.get(i));
            }
        }
        return this;
    }
    
    /**
     * La funzione viene utilizzata per sapere a quale classe bisogno trasformare il Json
     * 
     * @param c Classe
     * @return Istanza della classe
     */
    public QueryCore to(Class c)
    {
        this.c = c;
        return this;
    }
    
    /**
     * Il metodo execution viene utilizzato per eseguire la query di select
     * 
     * @return
     * @throws SQLException 
     */
    public QueryCore executionQ() throws SQLException
    {
        rs = prepStm.executeQuery();
        return this;
    }
    
    /**
     * Il metodo execution viene utilizzato per eseguire la query di select potendo passare un validatore 
     * 
     * @param throwingConsumer
     * @return
     * @throws SQLException 
     */
    public QueryCore executionQ(ThrowingConsumer throwingConsumer) throws SQLException
    {
        rs = prepStm.executeQuery();
        throwingConsumer.accept(rs);
        return this;
    }
    
    /**
     * Il metodo execution viene utilizzato per eseguire la query di select potendo passare un validatore e un messaggio d'errore
     * 
     * @param throwingConsumer
     * @return
     * @throws SQLException 
     */
    public QueryCore executionQ(ThrowingBiconsumer throwingConsumer, String e) throws SQLException
    {
        rs = prepStm.executeQuery();
        throwingConsumer.accept(rs,e);
        return this;
    }
    
    /**
     * Il metodo execution viene utilizzato per eseguire la query di update
     * 
     * @return Istanza della classe
     * @throws SQLException 
     */
    public QueryCore executionU() throws SQLException
    {
        prepStm.executeUpdate();
        ResultSet rsu = prepStm.getGeneratedKeys();
        if(rsu.next())
        {
            key = rsu.getInt(1);
        }
        return this;
    }
    
    /**
     * La funzione mapping viene utilizzata per mappare il resultset in JsonElement o model
     * 
     * @param map Funzione di mapping
     * @return 
     */
    public QueryCore mapping(Function<T,K> map)
    {
        response =  map.apply((T) rs);
        return this;
    }
    
    /**
     * La funzione mapping viene utilizzata per mappare il resultset in JsonElement o model
     * 
     * @param map Funzione di mapping
     * @return 
     */
    public QueryCore mapping(BiFunction<T,V,K> map)
    {
        response =  map.apply((T) rs,(V) c);
        return this;
    }
    
    /**
     * Il metodo destroy viene utilizzato per chiudere il resultset e il prepared statement se ancora non sono stati chiusi
     * 
     * @return Istanza della classe
     */
    public QueryCore destroy()
    {
        try
        {
            if(rs != null && !rs.isClosed())
            {
                rs.close();
            }
            if(prepStm != null && !prepStm.isClosed())
            {
                prepStm.close();
            }
        } 
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        
        return this;
    }
    
    /**
     * Funzione utilizzata per semplificare la chain
     * 
     * @param query query da dover eseguire
     * @param l lista dei parametri con cui vengono sostituiti "?"
     * @param c connessione
     * @return classe d'istanza
     * @throws SQLException 
     */
    public QueryCore setup(String query, List<Object> l,Connection c) throws SQLException
    {
        loadQuery(query,l).init(c);
        return this;
    }
    
    /**
     * Funzione utilizzata per semplificare la chain
     * 
     * @return classe d'istanza
     */
    public K release()
    {
        return (K) destroy().getResponse();
    }
    
    /**
     * 
     * @return query
     */
    public String getQuery()
    {
        return query;
    }

    /**
     * 
     * @param query query da eseguire
     */
    public void setQuery(String query)
    {
        this.query = query;
    }

    /**
     * 
     * @return lista di prepared statement
     */
    public List<Object> getL()
    {
        return l;
    }

    /**
     * 
     * @param l lista di prepared statement
     */
    public void setL(List<Object> l)
    {
        this.l = l;
    }

    /**
     * 
     * @return prepared statement
     */
    public PreparedStatement getPrepStm()
    {
        return prepStm;
    }

    /**
     * 
     * @param prepStm prepared statement
     */
    public void setPrepStm(PreparedStatement prepStm)
    {
        this.prepStm = prepStm;
    }

    /**
     * 
     * @return result set
     */
    public ResultSet getRs()
    {
        return rs;
    }
    
    /**
     * 
     * @param rs result set
     */
    public void setRs(ResultSet rs)
    {
        this.rs = rs;
    }

    /**
     * 
     * @return response
     */
    public K getResponse()
    {
        return response;
    }

    /**
     * 
     * @param response response
     */
    public void setResponse(K response)
    {
        this.response = response;
    }

    /**
     * 
     * @return key inserita
     */
    public int getKey()
    {
        return key;
    }

    /**
     * 
     * @param key key inserita
     */
    public void setKey(int key)
    {
        this.key = key;
    }
}
