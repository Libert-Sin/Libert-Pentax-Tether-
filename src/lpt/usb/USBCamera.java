/**
 * @author Libert
 */




package lpt.usb;

import com.ricoh.camera.sdk.wireless.api.CameraDevice;
import com.ricoh.camera.sdk.wireless.api.CameraEventListener;
import com.ricoh.camera.sdk.wireless.api.CameraImage;
import com.ricoh.camera.sdk.wireless.api.CameraStatus;
import com.ricoh.camera.sdk.wireless.api.CameraStorage;
import com.ricoh.camera.sdk.wireless.api.DeviceInterface;
import com.ricoh.camera.sdk.wireless.api.response.Response;
import com.ricoh.camera.sdk.wireless.api.response.Result;
import com.ricoh.camera.sdk.wireless.api.response.Error;
import com.ricoh.camera.sdk.wireless.api.response.ErrorCode;

import com.ricoh.camera.sdk.wireless.api.response.StartCaptureResponse;
import com.ricoh.camera.sdk.wireless.api.setting.camera.CameraDeviceSetting;
import com.ricoh.camera.sdk.wireless.api.setting.capture.CaptureSetting;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;





public final class USBCamera implements CameraDevice
{
    private final USBInterface iface;
    private final String fw;
    private final String model;
    private final String serial;
    private final String manu;
    private final int index;
    private boolean connected;
    private final List<CameraEventListener> listeners;
    private static final int SOCKET_BUFFER_SIZE = 50000;
    private ServerSocket sock;
    ExecutorService liveViewExec;
    


    
    public USBCamera(int index, USBInterface iface, String manu, String model, String serial, String fw)
    {
        this.index = index;
        this.iface = iface;
        this.manu = manu;
        this.model = model;
        this.serial = serial;
        this.fw = fw;
        this.connected = false;
        this.listeners = new LinkedList<>();
        
        this.addEventListener(new CameraEventListener()
        {
            @Override
            public void deviceDisconnected(CameraDevice sender)
            {   
                connected = false;
            }
        });
    }
        
    @Override
    public String getManufacturer()
    {
        return this.manu;
    }

    @Override
    public String getModel()
    {
        return this.model;
    }

    @Override
    public String getFirmwareVersion()
    {
        return this.fw;
    }

    @Override
    public String getSerialNumber()
    {
        return this.serial;
    }

    @Override
    public void addEventListener(CameraEventListener cl) {
        this.listeners.add(cl);
    }

    @Override
    public void removeEventListener(CameraEventListener cl) {
        this.listeners.remove(cl);
    }

    @Override
    public CameraEventListener[] getEventListeners() {
        return this.listeners.toArray(new CameraEventListener[this.listeners.size()]);
    }

    @Override
    public List<CameraStorage> getStorages() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public CameraStatus getStatus()
    {
        return this.iface.getStatus();
    }

    @Override
    public List<CameraImage> getImages() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Response updateImages() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    synchronized public Response connect(DeviceInterface di)
    {
        if (!this.iface.isConnected())
        {
            if (!this.iface.connect())
            {
                return new Response(
                    Result.ERROR,
                    new Error(ErrorCode.NETWORK_ERROR, "Failed to connect")
                ); 
            }
            
        }
        
        if (this.iface.connectCamera(index) && this.iface.processCallBacks(this, listeners))
        {
            connected = true;

            return new Response(
                Result.OK
            );
        }
        else
        {
            return new Response(
                Result.ERROR,
                new Error(ErrorCode.NETWORK_ERROR, "Failed to connect")
            );
        }
    }

    @Override
    synchronized public Response disconnect(DeviceInterface di)
    {    
        connected = false;
        
        if (!this.iface.isConnected())
        {
            return new Response(
                Result.ERROR,
                new Error(ErrorCode.NETWORK_ERROR, "USB not connected.")
            );
        }
        else if (!this.connected)
        {
            return new Response(
                Result.ERROR,
                new Error(ErrorCode.DEVICE_NOT_FOUND, "Camera not connected.")
            );
        }
        else
        {
            if (this.iface.disconnectCamera(index))
            {
                connected = false;
                return new Response(
                    Result.OK
                );
            }
            else
            {
                return new Response(
                    Result.ERROR,
                    new Error(ErrorCode.NETWORK_ERROR, "Failed to disconnect")
                );
            }    
        }
    }

    @Override
    synchronized public boolean isConnected(DeviceInterface di) {
        return connected;
    }
    
   
    @Override
    public Response focus() {
        if (this.iface.focus())
        {
            return new Response(
                Result.OK
            );
        }
        else
        {
            return new Response(
                Result.ERROR,
                new Error(ErrorCode.NETWORK_ERROR, "Failed to focus")
            );
        }    
    }
    
    public Response focus(int adjustment)
    {
        if (this.iface.focus(adjustment))
        {
            return new Response(
                Result.OK
            );
        }
        else
        {
            return new Response(
                Result.ERROR,
                new Error(ErrorCode.NETWORK_ERROR, "Failed to focus")
            );
        }    
    }

    @Override
    public StartCaptureResponse startCapture(boolean focus)
    {  
        return this.iface.capture(focus);
    }

    @Override
    public Response stopCapture() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public Response getCaptureSettings(List<CaptureSetting> list)
    {
        boolean status = this.iface.getSettings(list);
        
        if (status)
        {
            return new Response(
                Result.OK
            ); 
        }
        
        return new Response(
            Result.ERROR,
            new Error(ErrorCode.INVALID_ARGUMENT, "Error getting settings")
        );       
    }

    @Override
    public Response setCaptureSettings(List<CaptureSetting> list)
    {
        boolean status = this.iface.setSettings(list);
        
        if (status)
        {
            return new Response(
                Result.OK
            ); 
        }
        
        return new Response(
            Result.ERROR,
            new Error(ErrorCode.INVALID_ARGUMENT, "Error setting settings")
        );       
    }

    
    

    @Override
    public Response getCameraDeviceSettings(List<CameraDeviceSetting> list) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Response setCameraDeviceSettings(List<CameraDeviceSetting> list) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }    
    
    
    
    
    
    @Override
    public Response startLiveView()
    {            
        try
        {
            final ServerSocket serverSocket = new ServerSocket(0);
            sock = serverSocket;
                        
            if (this.iface.startLiveView(serverSocket.getLocalPort()))
            {
                if (liveViewExec != null)
                {
                    liveViewExec.shutdownNow();
                }
                
                liveViewExec = Executors.newSingleThreadExecutor();
                liveViewExec.submit(
                
                    new Thread(() ->
                    {
                        try 
                        {   
                            Socket socket = serverSocket.accept();
                            InputStream inputStream = socket.getInputStream();

                            while (true)
                            {                        
                                // Read a fixed amount of data
                                byte[] sizeAr = new byte[4];
                                inputStream.read(sizeAr);
                                byte[] imageAr = new byte[SOCKET_BUFFER_SIZE];
                                inputStream.read(imageAr);

                                try
                                {
                                    int size = ByteBuffer.wrap(sizeAr).asIntBuffer().get();

                                    final byte[] imageAr2 = Arrays.copyOfRange(imageAr, 0, size);

                                    if (imageAr[size - 1] == -39)
                                    {                        
                                        new Thread(() -> {
                                            listeners.forEach((CameraEventListener cel) ->
                                            { 
                                                cel.liveViewFrameUpdated(this, imageAr2);
                                            });
                                        }, "USBCamera liveViewFrameUpdated").start();
                                    }
                                }
                                catch(java.lang.ArrayIndexOutOfBoundsException | java.lang.IllegalArgumentException e)
                                {

                                }
                            }
                        }
                        catch (Exception ex)
                        {    
                            try
                            {
                                serverSocket.close();
                            } 
                            catch (IOException ex1)
                            {

                            }
                        }                
                    }, "USBCamera startLiveView")
                );
                
                return new Response(
                    Result.OK
                );
            }
            else
            {
                serverSocket.close();
                
                return new Response(
                    Result.ERROR,
                    new Error(ErrorCode.NETWORK_ERROR, "Failed to start live view")
                );
            } 
        }
        catch (IOException ex)
        {
            return new Response(
                Result.ERROR,
                new Error(ErrorCode.NETWORK_ERROR, "Failed to start live view due to socket error.")
            );
        }
    }

    @Override
    public Response stopLiveView()
    {    
        try 
        {
            if (sock != null)
            {
                sock.close();
            }
        } catch (IOException ex) {}
        
        if (this.iface.stopLiveView())
        {
            return new Response(
                Result.OK
            );
        }
        else
        {
            return new Response(
                Result.ERROR,
                new Error(ErrorCode.NETWORK_ERROR, "Failed to stop live view")
            );
        }    
    }

}

