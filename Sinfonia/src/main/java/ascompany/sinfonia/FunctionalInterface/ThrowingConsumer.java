package ascompany.sinfonia.FunctionalInterface;

import java.util.function.Consumer;

/**
 *
 * @author m.castano
 */
@FunctionalInterface
public interface ThrowingConsumer <T> extends Consumer<T>
{
    @Override
    default void accept(final T elem) 
    {
        try 
        {
            acceptThrows(elem);
        } 
        catch (final Exception e) 
        {
            throw new RuntimeException(e);
        }
    }

    void acceptThrows(T elem) throws Exception;
}
