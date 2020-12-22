/**
 * @author Libert
 */



package lpt.gui;

import java.awt.Component;



public class ComboItem extends Component
{
    private final String key;
    private final Object value;

    public ComboItem(String key, Object value)
    {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString()
    {
        return key;
    }

    public String getKey()
    {
        return key;
    }

    public Object getValue()
    {
        return value;
    } 
}
