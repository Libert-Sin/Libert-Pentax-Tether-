/**
 * @author Libert
 */



package lpt.usb;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;




public class USBMessage
{
    private static final char END_DELIM = '}';
    private static final String MAP_DELIM = ",";
    private static final String MAP_SEP = "=";
    private static final Pattern PATTERN = Pattern.compile("\\{typ:([^,]+),msg:([^,]*),err:([^,]*),data:(.*)\\}", Pattern.DOTALL);

    private String typ;
    private String err;
    private String msg;
    
    private final Map<String, String> m;
    private final String rawData;
    private final boolean malformed;
    


    
    public USBMessage(String errorType, String s)
    {
        typ = errorType;
        err = "Command failed to execute: " + s.replaceAll("\n", " ");
        m = new HashMap<>();
        msg = "";
        rawData = s;
        malformed = false;
    }
            


    
    public USBMessage (String s)
    {
        rawData = s;
        
        s = s.trim();
        
        typ = "";
        err = "Malformed message [" + s + "]";
        msg = "";
        m = new HashMap<>();
        
        Matcher matcher = PATTERN.matcher(s);
                
        if (matcher.find())
        {
            typ = matcher.group(1);
            msg = matcher.group(2);
            err = matcher.group(3);
            malformed = false;
            
            String mapParse = matcher.group(4);
            
            for (String chunk : mapParse.split(MAP_DELIM))
            {
                String[] kv = chunk.split(MAP_SEP);
                
                if (kv.length == 2)
                {
                    m.put(kv[0], kv[1]);
                }
            }
        }    
        else
        {
            malformed = true;
        }
    }
    

    
    
    public String getKey(String k)
    {
        return this.m.get(k);
    }
    


    
    public boolean hasKeys(String... ks)
    {
        for (String k : ks)
        {
            if (!hasKey(k))
            {
                return false;
            }
        }
        
        return true;
    }
    


    
    public boolean hasKey(String k)
    {
        return this.m.containsKey(k);
    }
    


    
    public boolean hasError()
    {
        return !"".equals(err);
    }
    


    
    public boolean hasMessage()
    {
        return !"".equals(msg);
    }
    


    
    public String getMessage()
    {
        return msg;
    }
    


    
    public String getError()
    {
        return err;
    }
    


    
    public String getType()
    {
        return typ;
    }
    


    
    public static char getMessageDelim()
    {
        return END_DELIM;
    }
    


    
    public String getRawData()
    {
        return rawData;
    }
    


    
    public boolean isMalformed()
    {
        return malformed;
    }
    
    @Override
    public String toString()
    {
        String data = (this.m.size() > 0 ? " DATA: " + this.m.toString() : "");
        
        if (this.err.length() > 0)
        {
            return "    USB " +this.typ + " ERROR " + this.err + data ;
        }
        else
        {
            return "    USB " + this.typ + " " + this.msg + data;
        }
    }
}
