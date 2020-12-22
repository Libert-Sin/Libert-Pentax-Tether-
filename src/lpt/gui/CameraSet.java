/**
 * @author Libert
 */


package lpt.gui;

import com.ricoh.camera.sdk.wireless.api.setting.capture.CaptureSetting;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import javax.swing.table.DefaultTableModel;


public class CameraSet extends DefaultTableModel implements java.io.Serializable
{
    public static final String FOCUS_ITEM_NAME = "Focus";
    
    public CameraSet (Object[][] o, String[] s)
    {
        super(o, s);
    }
    

    public static boolean serialize(String filePath, CameraSet m)
    {
        String output = "";
        

        try
        {
            if (m != null)
            {
                for (int row = 0; row < m.getRowCount(); row++)
                {
                    output = output 
                        + m.getValueAt(row, 0).toString() + ","
                        + m.getValueAt(row, 1).toString() + ","
                        + m.getValueAt(row, 2).toString() + ","
                        + m.getValueAt(row, 3).toString() + ","
                        + m.getValueAt(row, 4).toString() + "\n";
                }

                FileOutputStream fileOut =
                    new FileOutputStream(filePath);
                fileOut.write(output.getBytes());
                fileOut.close();
            }
            
            return true;
        }
        catch (IOException i)
        {
           i.printStackTrace();
        }
        catch (NullPointerException i)
        {
           System.err.println("Did not save unitialized UI state");
        }

        return false;
    }
    

    public boolean unserialize(String filePath, List<CaptureSetting> avs, List<CaptureSetting> tvs, List<CaptureSetting> isos, List<CaptureSetting> evs)
    {        
        try
        {
           Scanner input = new Scanner(new File(filePath)).useDelimiter("\n");
           
           while (this.getRowCount() > 0)
           {
                this.removeRow(0);
           }
           
           while (input.hasNext())
           {             
               String[] fragments = input.next().split(",");
               
               if (fragments.length == 5)
               {
                   ComboItem av = MainWindow.DEFAULT_COMBO_ITEM;
                   
                   for (CaptureSetting s : avs)
                   {
                       if (s.getValue().toString().equals(fragments[0].trim()))
                       {
                           av = new ComboItem(s.getValue().toString(), (Object) s);
                           break;
                       }
                   }
                   
                   ComboItem tv = MainWindow.DEFAULT_COMBO_ITEM;
                   
                   for (CaptureSetting s : tvs)
                   {
                       if (s.getValue().toString().equals(fragments[1]))
                       {
                           tv = new ComboItem(s.getValue().toString(), (Object) s);
                           break;
                       }
                   }
                   
                   ComboItem iso = MainWindow.DEFAULT_COMBO_ITEM;
                   
                   for (CaptureSetting s : isos)
                   {
                       if (s.getValue().toString().equals(fragments[2]))
                       {
                           iso = new ComboItem(s.getValue().toString(), (Object) s);
                           break;
                       }
                   }
                   
                   ComboItem ev = MainWindow.DEFAULT_COMBO_ITEM;
                   
                   for (CaptureSetting s : evs)
                   {
                       if (s.getValue().toString().equals(fragments[3]))
                       {
                           ev = new ComboItem(s.getValue().toString(), (Object) s);
                           break;
                       }
                   }
                   
                   ComboItem focus = getFocusItem("Yes".equals(fragments[4]));
                   
                   this.addRow(new Object[]{av, tv, iso, ev, focus});
               }
           }
           
           return true; 
        }
        catch (IOException i)
        {
           System.out.println("Failed to parse save file.");
        }
      
        return false;
    }    
    
    public static ComboItem getFocusItem(boolean focus)
    {
        return new ComboItem(focus ? "Yes" : "No", FOCUS_ITEM_NAME);
    }
    
  
    @Override
    public boolean isCellEditable(int row, int column)
    {
       return false;
    }
}
