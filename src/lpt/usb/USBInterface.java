/**
 * @author Libert
 */



package lpt.usb;

import com.ricoh.camera.sdk.wireless.api.CameraDevice;
import com.ricoh.camera.sdk.wireless.api.CameraEventListener;
import com.ricoh.camera.sdk.wireless.api.CameraStatus;
import com.ricoh.camera.sdk.wireless.api.response.StartCaptureResponse;
import com.ricoh.camera.sdk.wireless.api.setting.capture.CaptureSetting;
import java.util.List;




interface USBInterface
{


	
    public boolean connectCamera(int index);
    


    
    public boolean disconnectCamera(int index);
        


    
    public boolean isConnected();
    


    
    public boolean connect();
                


    
    public void disconnect();
    


    
    public StartCaptureResponse capture(boolean focus);
        


    
    public boolean getSettings(List<CaptureSetting> s);
    


    
    public boolean setSettings(List<CaptureSetting> s);
    


    
    public boolean processCallBacks(CameraDevice c, List<CameraEventListener> l);



    
    public CameraStatus getStatus();
    


    
    public List<CameraDevice> detectDevices();
    


    
    public boolean focus();
    


    
    public boolean focus(int adjustment);
    


    
    public boolean isBusy();    
    
    
    public boolean startLiveView(int port);
    public boolean stopLiveView();

 
}
