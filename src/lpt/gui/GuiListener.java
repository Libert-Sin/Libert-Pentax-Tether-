/**
 * @author Libert
 */



package lpt.gui;

import com.ricoh.camera.sdk.wireless.api.CameraDevice;
import com.ricoh.camera.sdk.wireless.api.CameraEventListener;
import com.ricoh.camera.sdk.wireless.api.CameraImage;
import com.ricoh.camera.sdk.wireless.api.Capture;
import lpt.Listener;
import java.io.File;



public class GuiListener extends CameraEventListener
{
    private final Listener g;
    
    public GuiListener(Listener g)
    {
        this.g = g;
    }
    
    @Override
    public void imageStored(CameraDevice sender, CameraImage image)
    {
        new Thread(() ->
        {
            System.out.printf("사진이 저장되었습니다. %s%n", image.getName());

            g.imageStored(image);
        }).start();
    }
    
    public void imageDownloaded(CameraImage image, File f, boolean isThumbnail)
    {  
        new Thread(() ->
        {
            g.imageDownloaded(image, f, isThumbnail);
        }).start();
    }

    @Override
    public void captureComplete(CameraDevice sender, Capture capture)
    {      
        new Thread(() ->
        {
            if (sender != null && capture != null)
            {
                System.out.printf("모든 프로세스가 완료되었습니다. %s%n", capture.getId());
            }

        }).start();          
    }

    @Override
    public void deviceDisconnected(CameraDevice sender)
    {   
        new Thread(() ->
        {
            System.out.println("연결이 끊어졌습니다.");

            g.disconnect();
        }).start();
    }
    


}

