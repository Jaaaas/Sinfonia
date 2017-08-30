/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FunctionalInterface;

import java.util.function.BiConsumer;


/**
 *
 * @author m.castano
 */
@FunctionalInterface
public interface ThrowingBiconsumer <T,U> extends BiConsumer<T,U>
{
    @Override
    default void accept(final T elem1, final U elem2) 
    {
        try 
        {
            acceptThrows(elem1, elem2);
        } 
        catch (final Exception e) 
        {
            throw new RuntimeException(e.getMessage());
        }
    }

    void acceptThrows(T elem, U elem2) throws Exception;
        
}
