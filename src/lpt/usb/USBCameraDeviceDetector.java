/**
 * @author Libert
 */



package lpt.usb;

import com.ricoh.camera.sdk.wireless.api.CameraDevice;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;




public class USBCameraDeviceDetector
{
    private static USBInterface instance;
    public static Class<?> PF_USB_BRIDGE = USBBridge.class;
    


    
    private static USBInterface getInstance(Class<?> cls)
    {
        if (instance == null)
        {
            if(USBInterface.class.isAssignableFrom(cls))
            {
                try
                {
                    Constructor<?> ctor = cls.getConstructor();
                    instance = (USBInterface) ctor.newInstance();
                }
                catch (Exception e)
                {
                    System.err.println("Invalid interface requested");
                    System.err.println(e);
                }
            }
            else
            {
                System.err.println("Invalid interface requested");
            }
        }
        return instance;
    }
    


    
    public static List<CameraDevice> detect(Class<?> cls)
    {
        USBInterface iface = getInstance(cls);
        
        if (iface != null)
        {
            return iface.detectDevices();
        }
        
        return new ArrayList<>();
    }
}
