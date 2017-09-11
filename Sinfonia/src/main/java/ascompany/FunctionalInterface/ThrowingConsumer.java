/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ascompany.FunctionalInterface;

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
