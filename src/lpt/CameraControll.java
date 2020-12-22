/**
 * @author Libert
 */

package lpt;

import com.ricoh.camera.sdk.wireless.api.CameraDevice;
import com.ricoh.camera.sdk.wireless.api.CameraEventListener;
import com.ricoh.camera.sdk.wireless.api.CameraImage;
import com.ricoh.camera.sdk.wireless.api.Capture;
import com.ricoh.camera.sdk.wireless.api.DeviceInterface;
import com.ricoh.camera.sdk.wireless.api.response.Response;
import com.ricoh.camera.sdk.wireless.api.response.Result;
import com.ricoh.camera.sdk.wireless.api.response.StartCaptureResponse;
import com.ricoh.camera.sdk.wireless.api.setting.capture.CaptureSetting;
import com.ricoh.camera.sdk.wireless.api.setting.capture.ExposureCompensation;
import com.ricoh.camera.sdk.wireless.api.setting.capture.FNumber;
import com.ricoh.camera.sdk.wireless.api.setting.capture.ISO;
import com.ricoh.camera.sdk.wireless.api.setting.capture.ShutterSpeed;
import lpt.usb.USBCameraDeviceDetector;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CameraControll
{
    private CameraDevice cam;    
    private CaptureSetting tv;
    private CaptureSetting av;
    private CaptureSetting ev;
    private CaptureSetting iso;

    private final Map<String, Boolean> captureState;
    private final List<CameraImage> capturedImages;
    private final ImageDownloader dm;
    private final CameraEventListener cl;    

    public static enum CONNECTION_MODE {MODE_USB};
    public final CONNECTION_MODE mode;    

    public static final int MIN_TIMEOUT = 15000;    
    public static final int KEEPALIVE = 45000;  
    public static final int STARTUP_RETRY = 5000;
        

    
    public CameraControll(CONNECTION_MODE mode)
    {
        this.capturedImages = new ArrayList<>();
        this.captureState = new HashMap<>();
        this.dm = new ImageDownloader(this);
        this.mode = mode;
        
        this.cl = new CameraEventListener()
        {
            @Override
            public void captureComplete(CameraDevice sender, Capture capture)
            {     
                new Thread(() ->
                {
                    if (capture != null)
                    {
                        captureState.put(capture.getId(), true);
                    }
                }).start();
            }   

            @Override
            public void imageStored(CameraDevice sender, CameraImage image)
            {
                new Thread(() ->
                {
                    capturedImages.add(image);
                }).start();
            }

            @Override
            public void deviceDisconnected(CameraDevice sender)
            {   
                new Thread(() ->
                {
                    disconnect();
                }).start();
            }
        };
    }
    
  
    
    
    public ImageDownloader getDownloadManager()
    {
        return dm;
    }
    
    
    
        
    
    
    public CameraImage getLastImage()
    {
        if (!this.capturedImages.isEmpty())
        {
            return this.capturedImages.get(this.capturedImages.size() - 1);
        }
                
        return null;
    }
        
    
    
    public List<CameraImage> getCapturedImages()
    {
        return this.capturedImages;
    }
    
    
    
    
    public List<CameraImage> getAllImagesOnCamera() throws CameraException
    {
        if (isConnected())
        {
            return getCam().getImages();
        }
        else
        {
            throw new CameraException("연결되지 않았습니다.");
        }
    }
    
  
    
    
    @Override
    public String toString()
    {
        if (isConnected())
        {
            return String.format(this.getClass().getSimpleName() + ": %s %s %s %s %s", getCameraModel(), tv, av, ev, iso);
        }
        else
        {
            return this.getClass().getSimpleName() + ": 연결되지 않았습니다.";
        }
    }
    

    

    synchronized public void refreshCurrentSettings() throws CameraException
    {       
        List<CaptureSetting> l = Arrays.asList(new ShutterSpeed(), new ExposureCompensation(), new FNumber(), new ISO());
        
        Response r = getCam().getCaptureSettings(l);

        if (!r.getErrors().isEmpty())
        {
            throw new CameraException(r.getErrors().toString());
        }         
        this.tv = l.get(0);
        this.ev = l.get(1);
        this.av = l.get(2);
        this.iso = l.get(3);
    }    


    
    public Capture captureImageWithSettings(Boolean focus, List<CaptureSetting> settings) throws CameraException
    {
        setCaptureSettings(settings);
                
        return captureStillImage(focus);
    }
    
    
    
    private CameraDevice getCam() throws CameraException
    {
        if (cam == null)
        {
            throw new CameraException("카메라가 연결되지 않았습니다.");
        }
        
        return cam;
    }
    


    
    synchronized public void setCaptureSettings(List<CaptureSetting> settings) throws CameraException
    {    
        List<CaptureSetting> toChange = new ArrayList<>();
        
        if (isConnected())
        {  

            toChange.addAll(settings);

            if (!toChange.isEmpty())
            {
                
               
                {
                	Response r = getCam().setCaptureSettings(toChange);

                    if (r != null)
                    {
                        if (r.getResult() == Result.ERROR)
                        {
                            throw new CameraException("설정 구성 실패: " + r.getErrors().get(0).getMessage());
                        }
                    }
                    else
                    {
                        throw new CameraException("설정 구성 실패: 카메라에서 응답이 없습니다.");
                    }
                }
            }
        }
        else
        {
            throw new CameraException("연결되지 않았습니다.");
        }
        

    }


    
    synchronized public final void connect(CameraEventListener el) throws CameraException
    {
        disconnect();
        
        List<CameraDevice> detectedDevices;
        
       
        detectedDevices = USBCameraDeviceDetector.detect(USBCameraDeviceDetector.PF_USB_BRIDGE);

        
        if (!detectedDevices.isEmpty())
        {
            cam = detectedDevices.get(0);  
            
            Response r = cam.connect(DeviceInterface.WLAN);
                        
            if (!r.getErrors().isEmpty())
            {
                disconnect();
            
                
                if (!isConnected())
                {
                    throw new CameraException(r.getErrors().toString());
                }
            }
                        
            refreshCurrentSettings();
                    

            this.removeListener(cl);
            this.addListener(cl);
            
            if (el != null)
            {
                this.removeListener(el);
                this.addListener(el);
            }
            

            
        }    
        
        if (!isConnected())
        {
            throw new CameraException("카메라를 찾을 수 없습니다.");
        }
    }


    synchronized public void disconnect()
    {
        if (isConnected())
        {
            CameraDevice c = cam;
            cam = null;            
            c.disconnect(DeviceInterface.WLAN);
        }
    }
    

    public boolean isConnected()
    {
        return cam != null;
    }
    

    synchronized public void focus() throws CameraException
    {
        if (isConnected())
        {        
            try
            {
                Response r = getCam().focus();

                if (r.getResult() != Result.OK)
                {
                    throw new CameraException("AF 실패: " + r.getErrors().get(0).getMessage());
                }
            }
            catch (UnsupportedOperationException e)
            {
                throw new CameraException("현 상황에서 AF는 지원되지 않습니다.");
            }
        }
        else
        {
            throw new CameraException("연결되지 않았습니다.");
        }
    }
    

    synchronized public Capture captureStillImage(boolean focus) throws CameraException
    {
        if (!isConnected())
        {
            throw new CameraException("연결되지 않았습니다.");
        }
        

        
        StartCaptureResponse startCaptureResponse = null;
        
        try
        {
            startCaptureResponse = cam.startCapture(focus);
        }
        catch (UnsupportedOperationException e)
        {
            throw new CameraException("이 카메라는 지원하지 않습니다.");
        }
        
        if (startCaptureResponse.getResult() == Result.OK)
        {
            System.out.printf("촬영이 시작되었습니다.",
                startCaptureResponse.getCapture().getId());
            
            return startCaptureResponse.getCapture();
        }
        else
        {    
            throw new CameraException("촬영 실패: " + startCaptureResponse.getErrors().get(0).getMessage());            
        }
    }
    

    
    public final void addListener(CameraEventListener e)
    {
        if (isConnected())
        {
            cam.addEventListener(e);
        }
    }
    
    public void removeListener(CameraEventListener e)
    {
        if (isConnected())
        {
            cam.removeEventListener(e);
        }
    }
    

    
    public String getCameraModel()
    {
        if (isConnected())
        {
            return this.cam.getModel();
        }      
        
        return "";
    }    
    
    public int getCameraBattery()
    {        
        if (isConnected())
        {
            return this.cam.getStatus().getBatteryLevel();
        }
        
        return 0;
    }
    
    public String getCameraSerial()
    {
        if (isConnected())
        {
            return this.cam.getSerialNumber();
        }      
        
        return "";
    }  
    
    public String getCameraManufacturer()
    {
        if (isConnected())
        {
            return this.cam.getManufacturer();
        }      
        
        return "";
    }  
    
    public String getCameraFirmware()
    {
        if (isConnected())
        {
            return this.cam.getFirmwareVersion();
        }      
        
        return "";
    }  
    

    
    public List<CaptureSetting> genCaptureSettings(FNumber nAv)
    {
        return Arrays.asList(
            nAv
        );
    }
    
    public List<CaptureSetting> genCaptureSettings(ShutterSpeed nTv)
    {
        return Arrays.asList(
            nTv
        );
    }
    
    public List<CaptureSetting> genCaptureSettings(ISO nIso)
    {
        return Arrays.asList(
            nIso
        );
    }
    
    public List<CaptureSetting> genCaptureSettings(ExposureCompensation nEv)
    {
        return Arrays.asList(
            nEv
        );
    }
    
    public List<CaptureSetting> genCaptureSettings(ExposureCompensation nEv, ISO nIso)
    {
        return Arrays.asList(
            nEv, nIso
        );
    }
    
    public List<CaptureSetting> genCaptureSettings(ShutterSpeed nTv, ExposureCompensation nEv, ISO nIso)
    {
        return Arrays.asList(
            nTv, nEv, nIso
        );
    }
    
    public List<CaptureSetting> genCaptureSettings(FNumber nAv, ExposureCompensation nEv, ISO nIso)
    {
        return Arrays.asList(
            nAv, nEv, nIso
        );
    }
    
    public List<CaptureSetting> genCaptureSettings(FNumber nAv, ShutterSpeed nTv)
    {
        return Arrays.asList(
            nTv, nAv
        );
    }
    
    public List<CaptureSetting> genCaptureSettings(FNumber nAv, ShutterSpeed nTv, ISO nIso)
    {
        return Arrays.asList(
            nTv, nAv, nIso
        );
    }
    

    
    public List<CaptureSetting> getAvailableAv()
    {
        if (this.av != null)
        {
            return this.av.getAvailableSettings();
        }
        
        return new ArrayList<>();
    }
    
    public List<CaptureSetting> getAvailableTv()
    {
        if (this.tv != null)
        {
            return this.tv.getAvailableSettings();
        }
        
        return new ArrayList<>();
    }
    
    public List<CaptureSetting> getAvailableISO()
    {
        if (this.iso != null)
        {
            return this.iso.getAvailableSettings();
        }
        
        return new ArrayList<>();
    }
    
    public List<CaptureSetting> getAvailableEV()
    {
        if (this.ev != null)
        {
            return this.ev.getAvailableSettings();
        }
        
        return new ArrayList<>();
    }
    

    
    public CaptureSetting getTv()
    {
        return tv;
    }
    
    public CaptureSetting getEv()
    {
        return ev;
    }
    
    public CaptureSetting getAv()
    {
        return av;
    }
    
    public CaptureSetting getISO()
    {
        return iso;
    }
}
