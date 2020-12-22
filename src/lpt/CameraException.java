/**
 * @author Libert
 */


package lpt;


public class CameraException extends Exception 
{
    private String m;
    
    public CameraException(String m)
    {
        this.m = m;
    }
    
    @Override
    public String toString()
    {
        return this.m;
    }
}
