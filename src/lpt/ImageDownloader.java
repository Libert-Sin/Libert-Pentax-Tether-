/**
 * @author Libert
 */


package lpt;

import com.ricoh.camera.sdk.wireless.api.CameraImage;
import com.ricoh.camera.sdk.wireless.api.response.ErrorCode;
import com.ricoh.camera.sdk.wireless.api.response.Response;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class ImageDownloader
{
    private final Map<CameraImage, FileOutputStream> toDownloadImages;
    private final Map<CameraImage, File> downloadedImages;
    
    private ExecutorService downloadPool;
    private int numImagesProcessing;
    

    public static final int NUM_DOWNLOAD_THREADS = 1;
    

    
    public ImageDownloader(CameraControll m)
    {
        numImagesProcessing = 0;
        toDownloadImages = new HashMap<>();
        downloadedImages = new HashMap<>();
        downloadPool = Executors.newFixedThreadPool(NUM_DOWNLOAD_THREADS);
    }
    

    
    synchronized public void abortDownloading(Listener l)
    {
        downloadPool.shutdownNow();
        downloadPool = Executors.newFixedThreadPool(NUM_DOWNLOAD_THREADS);
        numImagesProcessing = 0;
                
        ensureStreamsClosed();        
        
        if (l != null)
        {
            new Thread(() -> {
                l.imageDownloaded(null, null, false);
            }).start();
        }
    }
    

    
    private void ensureStreamsClosed()
    {
        List<FileOutputStream> toCheck = new LinkedList<>();
        
        toCheck.addAll(this.toDownloadImages.values());
        
        toCheck.stream().filter((s) -> (s != null)).forEachOrdered((s) ->
        {
            try
            {
                s.close();
            }
            catch (IOException ex)
            {
                
            }
        });
    }
    


    
    synchronized public int getNumProcessingAll()
    {
        return getNumProcessing(true) + getNumProcessing(false);
    }
    

    
    synchronized public int getNumProcessing(boolean isThumbnail)
    {	
            return numImagesProcessing;
     
    }
    

    public void downloadImage(String savePath, CameraImage i, Listener l)
    {
        doDownloadImage(savePath, i, false, l);
    }
    

    
    private void doDownloadImage(String savePath, CameraImage i, boolean isThumbnail, Listener l)
    {        
        
        this.downloadPool.submit(
            new Thread(() -> 
            {
                FileOutputStream outputStream = null;
                File f = null;
                boolean error = false;

                try
                {
                    if (savePath == null)
                    {
                        f = File.createTempFile(isThumbnail ? "thumb" : "image", i.getName());
                        
                        f.deleteOnExit();
                    }
                    else
                    {
                        f = new File(savePath + "/" + i.getName());
                        

                        if (f.exists())
                        {
                            f.delete();
                        }
                    }
                    
                    Response response;
                    outputStream = new FileOutputStream(f);
                    {
                        toDownloadImages.put(i, outputStream);
                        response = i.getData(outputStream);
                        
                        if (!response.getErrors().isEmpty())
                        {
                      
                            if (ErrorCode.IMAGE_NOT_FOUND == response.getErrors().get(0).getCode())
                            {
                                toDownloadImages.remove(i);
                            }
                            
                            throw new IOException("파일 다운로드 실패: " + response.getErrors().get(0).getMessage());
                        }
                        else
                        {                     
                            downloadedImages.put(i, f);
                            toDownloadImages.remove(i);
                        }
                    }

                    if (l != null)
                    {
                        final CameraImage img = i;
                        final File fil = f;
                        final boolean isThumb = isThumbnail;
                        
                        new Thread(() -> {
                            l.imageDownloaded(img, fil, isThumb);    
                        }).start();
                    }
                }
                catch (IOException e)
                {     
                    if (l != null)
                    {
                        final CameraImage img = i;
                        final boolean isThumb = isThumbnail;
                        
                        new Thread(() -> {
                            l.imageDownloaded(img, null, isThumb);    
                        }).start();    
                    }
                    
                    error = true;
                    
                    System.out.println(e.toString());
                }
           
                finally
                {
                    if (outputStream != null)
                    {
                        try
                        {
                            outputStream.close();
                        }
                        catch (IOException e)
                        {
                         
                        }
                    }
                    
                 
                    if (error)
                    {
                        if (f != null)
                        {                            
                            f.delete();
                        }
                    }
                }
            })
        );
    }
    
  

    public File getDownloadedImage(CameraImage i)
    {
        return this.downloadedImages.get(i);
    }
}
