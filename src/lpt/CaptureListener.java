/**
 * @author Libert
 */


package lpt;

import com.ricoh.camera.sdk.wireless.api.CameraImage;
import java.io.File;



public interface CaptureListener
{  


    public void imageCaptureComplete(boolean captureOk, int remaining);
    


    public void disconnect();
    


    public void imageDownloaded(CameraImage i, File f, boolean isThumbnail);
    


    public void imageStored(CameraImage i);
    
}
