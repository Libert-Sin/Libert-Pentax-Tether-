/**
 * @author Libert
 */



package lpt.usb;

import com.ricoh.camera.sdk.wireless.api.setting.capture.CaptureSetting;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class USBCameraSetting <T extends CaptureSetting> extends CaptureSetting
{
    public static final String LIST_DELIM = "\\|";

    
    public static <T extends CaptureSetting> USBCameraSetting<T> getUSBSetting(String current, String available, Class<T> cls)
    {
        if (current == null)
        {
            return null;
        }
        
    
        T cur = getMatchingSetting(current.trim(), cls);
        
   
        List<CaptureSetting> settings = new ArrayList<>();
        
        if (available != null)
        {
            for (String s : available.split(LIST_DELIM))
            {            
                T cand = getMatchingSetting(s.trim(), cls);

                if (cand != null)
                {
                    settings.add(cand);
                }
            }
        }
        
     
        if (cur != null)
        {
            return new USBCameraSetting<>(cur, settings);
        }
        
        return null;
    }
    


    
    private USBCameraSetting(T current, List<CaptureSetting> availableSettings)
    {
        super(current.getName(), current.getValue());
              
        this.availableSettings.addAll(availableSettings);
    }
    


    
    public String toStringDebug()
    {
        return this.toString() + " Available: " + this.availableSettings.toString();
    }
   
    public static <T extends CaptureSetting> T getMatchingSetting(String candidate, Class<T> cls)
    {        
        

        Field[] declaredFields = cls.getDeclaredFields();
        List<Field> staticFields = new ArrayList<>();
        
        for (Field field : declaredFields)
        {
            if (java.lang.reflect.Modifier.isStatic(field.getModifiers())
                    && java.lang.reflect.Modifier.isPublic(field.getModifiers())) {
                staticFields.add(field);
            }
        }
        


        
        for (Field f : staticFields)
        {
            T test;
            
            try
            {
                test = (T) f.get(cls);
            }
            catch (IllegalArgumentException | IllegalAccessException ex)
            {
                Logger.getLogger(USBCameraSetting.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
            
            if (test != null && test.getValue().toString().equals(candidate))
            {            
                return test;
            }         
        }
        
        return null;
    }    
}
