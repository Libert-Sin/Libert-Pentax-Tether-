/**
 *
 * @author Libert
 */
package lpt.gui;

import com.ricoh.camera.sdk.wireless.api.CameraImage;
import com.ricoh.camera.sdk.wireless.api.ImageFormat;
import com.ricoh.camera.sdk.wireless.api.setting.capture.CaptureSetting;
import com.ricoh.camera.sdk.wireless.api.setting.capture.FNumber;
import com.ricoh.camera.sdk.wireless.api.setting.capture.ISO;
import com.ricoh.camera.sdk.wireless.api.setting.capture.ShutterSpeed;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.prefs.Preferences;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import java.net.URI; 
import java.net.URISyntaxException;
import java.util.Date;
import java.text.SimpleDateFormat;


import lpt.CameraControll;
import lpt.CameraControll.CONNECTION_MODE;
import lpt.CameraException;
import lpt.Listener;


import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;


public class MainWindow extends javax.swing.JFrame implements Listener
{

    private CameraControll m;        
    private String saveFilePath;
    private Boolean doTransferFiles;
    private Boolean doTransferRawFiles;
    private Boolean doSyncCameraSettings;
    private Boolean doAutoReconnect;
    private Boolean doFocus;
    private Boolean bypassReconnect; 
    private ScheduledExecutorService pool;
    private Boolean initializing;
    private final GuiListener gl;
    private final Preferences prefs;       

    public static final String ICON = "resources/logo.png";
    public static final String DEFAULT_PATH = System.getProperty("user.dir");
    public static final ComboItem DEFAULT_COMBO_ITEM = new ComboItem("--", null);
    public static final int STATUS_REFRESH = 500;
    public static final String VERSION_NUMBER = "1.0";
    public static final String SW_NAME = "Libert Pentax Tether";
    

    
    public MainWindow(CONNECTION_MODE mode)
    {             
        m = new CameraControll(mode);     
        initComponents();
        
        setVisible(true);
        

        prefs = Preferences.userRoot().node(this.getClass().getName());
        pool = Executors.newScheduledThreadPool(1);
        gl = new GuiListener(
        		this);
        bypassReconnect = false;        

        connect();             

        ImageIcon img = new ImageIcon(getClass().getClassLoader().getResource(MainWindow.ICON));
        this.setIconImage(img.getImage());        

        initLabels();     
        loadPrefs();
        
        getContentPane().setBackground(Color.WHITE);

        

        JMenuItem focusItem = new JMenuItem("AF");
        focusItem.addActionListener((ActionEvent e) ->
        {     
            (new Thread(() -> 
            {  
                try
                {
                    m.focus();
                }
                catch (CameraException ex)
                {
                    JOptionPane.showMessageDialog(null, "AF실패. 카메라 또는 렌즈가 MF상태인지 확인하세요.");
                }
            })).start();        
        });       
        
        JMenuItem captureItem = new JMenuItem("촬영 세팅");
        captureItem.addActionListener((ActionEvent e) ->
        {     
            (new Thread(() -> 
            {  
                captureButtonActionPerformed(null); 
            })).start();        
        });   
        setVisible(true);
    }
   
    

    private boolean validatePath(String checkPath)
    {
        File path = new File(checkPath);        
        return path.exists() && path.isDirectory();
    }
    
    

    private void loadPrefs()
    {
        doSyncCameraSettings = prefs.getBoolean("doSyncCameraSettings", true);
        doTransferFiles = prefs.getBoolean("doTransferFiles", false);
        doTransferRawFiles = prefs.getBoolean("doTransferRawFiles", false);
        saveFilePath = prefs.get("saveFilePath", DEFAULT_PATH);
        doAutoReconnect = prefs.getBoolean("doAutoReconnect", true);        
        doFocus = prefs.getBoolean("doFocus", false);        
              
        if (!validatePath(saveFilePath))
        {
            saveFilePath = DEFAULT_PATH;
        }
        
        this.focus.setSelected(doFocus);
        this.transferRawFiles.setSelected(doTransferRawFiles);
        this.transferFiles.setSelected(doTransferFiles);      
    }
    
    

    @Override
    synchronized public void imageStored(CameraImage image)
    {     
        if (   
            ((image.getFormat() == ImageFormat.PEF || image.getFormat() == ImageFormat.DNG || image.getFormat() == ImageFormat.TIFF) && this.doTransferRawFiles) ||
            (image.getFormat() == ImageFormat.JPEG && doTransferFiles)
            )
        {
        	 new Thread(() -> {
        		 
        		 Date today = new Date();
        		 System.out.println(today);      		        
        		 SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd");

        		 String fp = saveFilePath+"\\"+date.format(today);
        		 File Folder = new File(fp);
        		 
        		 try
        		 {
        			 Folder.mkdirs();        				    
        		 } 
        		 catch(Exception e)
        		 {
        			 e.getStackTrace();
        		 }        		 
        		 
        		 
                 this.m.getDownloadManager().downloadImage(fp, image, this);             
             }).start();
        }        	
    }
         

    
    @Override
    public synchronized void imageDownloaded(CameraImage image, File f, boolean isThumbnail)
    {        
        if (f == null)
        {
            if (image != null)
            {
                JOptionPane.showMessageDialog(this, "이미지 다운로드 실패. " + (isThumbnail ? "thumbnail" : "image") + " " + image.getName());
            }
            else
            {
                JOptionPane.showMessageDialog(this, "이미지 다운로드 중단.");
            }    
        }
        else
        {            
            try
            {
                Desktop.getDesktop().open(new File(f.getAbsolutePath()));
            	System.out.println("다운로드 완료 : " + f.getAbsolutePath());
            }
            catch (IOException ex)
            {
            	System.out.println("다운로드 완료 : " + f.getAbsolutePath());
            }       
        } 
        
    }
    
     
    

    @Override
    synchronized public void imageCaptureComplete(boolean captureOk, int remaining)
    {        
                
        if (!captureOk)
        {
            if (this.doAutoReconnect)
            {
                if (!bypassReconnect)
                {
                    System.out.println(String.format("촬영 중단. 재연결 시작."));
                    new Thread(() -> 
                    {
                        m.disconnect();
                        autoReconnect(false); 
                        updateBattery();
                    }).start();
                }                 
                bypassReconnect = false;
            }
            else
            {
                JOptionPane.showMessageDialog(this, 
                    String.format("시간 초과로 촬영이 중단되었습니다. 다시 연결 해 주세요.")
                );   
            }
        }
    }
   
    
    
    
    synchronized private void autoReconnect(boolean restartDownloads)
    {
        JOptionPane pane = new JOptionPane("자동으로 재연결을 시도합니다. 프로그램을 종료하시려면 확인을 눌러주세요.",  JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION);
        JDialog getTopicDialog =  pane.createDialog(this, "연결 끊김.");

        if (restartDownloads)
        {
            abortTransfer();
        }
        
        (new Thread( () ->
        {  
            while (!m.isConnected())
            {
                try
                {                        
                    if (!isVisible())
                    {
                        doExit();
                    }
                    m.connect(gl);
                } 
                catch (CameraException ex)
                {
                    System.out.println(ex.toString());
                    try
                    {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex1) { }
                }
            }
            pane.setValue(JOptionPane.CANCEL_OPTION);
            getTopicDialog.dispose();            
        })).start();

        getTopicDialog.setVisible(true);

        if(null == pane.getValue())
        {
            doExit();
        }
        else
        {
            switch(((Integer)pane.getValue()))
            {
                case JOptionPane.OK_OPTION:
                    doExit();
                    break;
                case JOptionPane.CANCEL_OPTION:  
                    break;
                default:
                    break;
            }
        }              
    }

    

    @Override
    public void disconnect()
    {        
        if (doAutoReconnect)
        {
            autoReconnect(true);   
        }
        else
        {
            connectFailed();
        }
    }
    
    

    synchronized private void connectFailed()
    {
        int choice = JOptionPane.showConfirmDialog(this, "카메라와 연결에 실패했습니다. 정상적으로 연결 되었는지 확인하세요. USB 연결모드가 PTP로 설정되어 있습니까?" + "  다시 시도 하시겠습니까?", "오류", JOptionPane.YES_OPTION);
                
        if (choice == JOptionPane.YES_OPTION)
        {
            try
            {
                if (!m.isConnected())
                {
                    this.m.connect(gl);
                }
            }
            catch (CameraException ex)
            {                
                connectFailed();
                return;
            }           
        }
        else
        {
            doExit();
        }
    }
    
    

    private void connect()
    {
        try
        {
            this.m.connect(gl);               
        }
        catch (CameraException ex)
        {
            connectFailed();
        }
    }
    
    

    private void initLabels()
    {
        initializing = true;
        
        DefaultComboBoxModel model = (DefaultComboBoxModel) avMenu.getModel();
        model.removeAllElements();
        model.addElement(DEFAULT_COMBO_ITEM);
        
        for (CaptureSetting f : this.m.getAvailableAv())
        {
            model.addElement(new ComboItem(((FNumber) f).getValue().toString(), (FNumber) f));    
       
            if (f.equals(this.m.getAv()))
            {
                avMenu.setSelectedIndex(model.getSize() - 1);
            }
        }        
        model = (DefaultComboBoxModel) tvMenu.getModel();
        model.removeAllElements();
        model.addElement(DEFAULT_COMBO_ITEM);
        
        for (CaptureSetting f : this.m.getAvailableTv())
        {        
            model.addElement(new ComboItem(((ShutterSpeed) f).getValue().toString(), (ShutterSpeed) f)); 
            
            if (f.equals(this.m.getTv()))
            {
                tvMenu.setSelectedIndex(model.getSize() - 1);
            }
        }        
        model = (DefaultComboBoxModel) isoMenu.getModel();
        model.removeAllElements();
        model.addElement(DEFAULT_COMBO_ITEM);
        
        for (CaptureSetting f : this.m.getAvailableISO())
        {
            model.addElement(new ComboItem(((ISO) f).getValue().toString(), (ISO) f));   
            
            if (f.equals(this.m.getISO()))
            {
                isoMenu.setSelectedIndex(model.getSize() - 1);
            }
        }             
        updateBattery();        
        initializing = false;
    }
    
    

    private void updateBattery()
    {
        int battery = this.m.getCameraBattery();
        this.batteryLevel.setBackground(Color.WHITE);
        
        if (battery > 67)
        {
            this.batteryLevel.setForeground(Color.GREEN);
        }
        else if (battery > 34)
        {
            this.batteryLevel.setForeground(Color.YELLOW);
        }
        else
        {
            this.batteryLevel.setForeground(Color.RED);
        }        
        this.batteryLevel.setToolTipText(String.format("배터리 잔량: %d%%", battery));
        this.batteryLevel.setValue(battery);
        this.batteryLevel.setString("");
        this.batteryLevel.setStringPainted(true);        
    }

    

    private void initComponents() 
    {
        jMenuItem1 = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        jMenuItem1.setText("jMenuItem1");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(SW_NAME);
        setBackground(new java.awt.Color(255, 255, 255));
        addWindowListener(new java.awt.event.WindowAdapter() 
        {
            public void windowClosed(java.awt.event.WindowEvent evt) 
            {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) 
            {
                formWindowClosing(evt);
            }
        });

        

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup
        (
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        );
        jPanel1Layout.setVerticalGroup
        (
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        );
        
        
        
        
        JPanel panel = new JPanel();
        
        JPanel panel_1 = new JPanel();

        
                
                
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        layout.setHorizontalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(layout.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(panel, GroupLayout.PREFERRED_SIZE, 438, GroupLayout.PREFERRED_SIZE)
        			.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        		.addGroup(Alignment.TRAILING, layout.createSequentialGroup()
        			.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        			.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 438, GroupLayout.PREFERRED_SIZE)
        			.addContainerGap())
        );
        layout.setVerticalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(layout.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(panel, GroupLayout.PREFERRED_SIZE, 155, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 37, Short.MAX_VALUE)
        			.addContainerGap())
        );
        panel_1.setLayout(null);
        
        photoguraphy = new JLabel("Make by Libert PHOTOGuRAPHY.");
        photoguraphy.setFont(new Font("Majesticer_R", Font.PLAIN, 16));
        photoguraphy.setBounds(103, 7, 249, 25);
        photoguraphy.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
            	try
                {
                    Desktop.getDesktop().browse(new URI("https://www.photoguraphy.com/"));
                }
                catch (IOException ex)
                {
                	
                }       
        		catch(URISyntaxException e)
            	{
            		
        		};
            }
        });
            
        panel_1.add(photoguraphy);
        panel.setLayout(null);
        
        isoMenu = new javax.swing.JComboBox<>();
        isoMenu.setBounds(296, 12, 130, 44);
        panel.add(isoMenu);
        isoMenu.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        
        tvMenu = new javax.swing.JComboBox<>();
        tvMenu.setBounds(12, 12, 130, 44);
        panel.add(tvMenu);
        tvMenu.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        
        avMenu = new javax.swing.JComboBox<>();
        avMenu.setBounds(154, 12, 130, 44);
        panel.add(avMenu);
        avMenu.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        
        batteryLevel = new javax.swing.JProgressBar();        
        batteryLevel.setBounds(154, 61, 130, 25);
        panel.add(batteryLevel);
        batteryLevel.setBackground(new java.awt.Color(0, 0, 0));
        batteryLevel.setFont(new java.awt.Font("Tahoma", 1, 15)); 
        batteryLevel.setForeground(new java.awt.Color(0, 0, 0));
        batteryLevel.setToolTipText("배터리 잔량");
        batteryLevel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        batteryLevel.setMaximumSize(new java.awt.Dimension(148, 16));
        batteryLevel.setMinimumSize(new java.awt.Dimension(148, 16));
        batteryLevel.setRequestFocusEnabled(false);
        
        selectFilePath = new javax.swing.JButton();        
        selectFilePath.setBounds(12, 120, 130, 24);
        panel.add(selectFilePath);
        selectFilePath.setText("저장 경로 선택");
        
        OpenFolder = new javax.swing.JButton();        
        OpenFolder.setBounds(154, 120, 130, 24);
        panel.add(OpenFolder);
        OpenFolder.setText("폴더 열기");
        
        captureButton = new javax.swing.JButton();        
        captureButton.setBounds(296, 61, 130, 83);
        panel.add(captureButton);
        captureButton.setText("촬영");
        
        transferFiles = new javax.swing.JCheckBox();        
        transferFiles.setBounds(12, 90, 130, 22);
        panel.add(transferFiles);
        transferFiles.setText("JPG");
        
        transferRawFiles = new javax.swing.JCheckBox();        
        transferRawFiles.setBounds(12, 64, 130, 22);
        panel.add(transferRawFiles);
        transferRawFiles.setText("RAW");
        
        focus = new javax.swing.JCheckBox();        
        focus.setBounds(154, 90, 130, 22);
        panel.add(focus);
        focus.setText("AF");
        focus.addActionListener(new java.awt.event.ActionListener() 
        {
            public void actionPerformed(java.awt.event.ActionEvent evt) 
            {
                focusActionPerformed(evt);
            }
        });
        transferRawFiles.addActionListener(new java.awt.event.ActionListener() 
        {
            public void actionPerformed(java.awt.event.ActionEvent evt) 
            {
                transferRawFilesActionPerformed(evt);
            }
        });
        transferFiles.addActionListener(new java.awt.event.ActionListener() 
        {
            public void actionPerformed(java.awt.event.ActionEvent evt) 
            {
                transferFilesActionPerformed(evt);
            }
        });
        captureButton.addActionListener(new java.awt.event.ActionListener() 
        {
            public void actionPerformed(java.awt.event.ActionEvent evt) 
            {
                captureButtonActionPerformed(evt);
            }
        });
        OpenFolder.addActionListener(new java.awt.event.ActionListener() 
        {
            public void actionPerformed(java.awt.event.ActionEvent evt) 
            {
                OpenFolderActionPerformed(evt);
            }
        });
        selectFilePath.addActionListener(new java.awt.event.ActionListener() 
        {
            public void actionPerformed(java.awt.event.ActionEvent evt) 
            {
                selectFilePathActionPerformed(evt);
            }
        });
        avMenu.addItemListener(new java.awt.event.ItemListener() 
        {
            public void itemStateChanged(java.awt.event.ItemEvent evt) 
            {
                dropDownStateChanged(evt);
            }
        });
        tvMenu.addItemListener(new java.awt.event.ItemListener() 
        {
            public void itemStateChanged(java.awt.event.ItemEvent evt) 
            {
                dropDownStateChanged(evt);
            }
        });
        isoMenu.addItemListener(new java.awt.event.ItemListener() 
        {
            public void itemStateChanged(java.awt.event.ItemEvent evt) 
            {
                dropDownStateChanged(evt);
            }
        });
        getContentPane().setLayout(layout);
        pack();
    }
            
    
    private List <CaptureSetting> getSettings(Object src)
    {
        List <CaptureSetting> l = new ArrayList<>();        
        Object c;        
        ComboItem source = null;
        
        if (src != null)
        {   
            source = (ComboItem) src;
        }
        
        try
        {
            c = ((ComboItem) this.avMenu.getSelectedItem()).getValue();
            if (c != null && (source == null || c == source.getValue()))
            {
                l.add((CaptureSetting) c);
            }       
        }
        catch (Exception e)
        {}    
        
        try
        {
            c = ((ComboItem) this.tvMenu.getSelectedItem()).getValue();
            if (c != null && (source == null || c == source.getValue()))
            {
                l.add((CaptureSetting) c);
            }
        }
        catch (Exception e)
        {}
        
        try
        {
            c = ((ComboItem) this.isoMenu.getSelectedItem()).getValue();
            if (c != null && (source == null || c == source.getValue()))
            {
                l.add((CaptureSetting) c);
            }
        }        
        catch (Exception e)
        {}         
        return l;
    }
    

    
    private void sendSettingsToCamera(Object source)
    {
        if (!this.initializing)
        {
            (new Thread(() -> 
            {   
                try
                {
                    m.setCaptureSettings(getSettings(source));
                }
                catch (CameraException ex)
                {
                    System.out.println(ex.toString());
                }
            })).start();    
        }
    }
    
    
    
    private void captureButtonActionPerformed(java.awt.event.ActionEvent evt) {
                          
        captureButton.setEnabled(false);                
        final boolean focusSetting = doFocus;
        
        Runnable r = new Thread(() -> 
        {
            try
            {
                if (evt != null)
                {
                    m.setCaptureSettings(getSettings(null));
                }                
                m.captureStillImage(focusSetting);                
            }
            catch (CameraException ex)
            {
            }
            finally
            {
                captureButton.setEnabled(true);
            }
        });        
            pool.submit(r);  
    }



    private void formWindowClosed(java.awt.event.WindowEvent evt) {
        doExit(); 
    }
    
    
    
    synchronized private void doExit()
    {
        setVisible(false);                
  
        (new Thread(() ->
        {
            m.disconnect();    
        }, "연결 해제.")).start();
        
        (new Thread(() ->
        {           
            if (m.isConnected())
            {
                try
                {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) { }
            }
            System.exit(0);            
        })).start();
    }
    
    
    
    private void formWindowClosing(java.awt.event.WindowEvent evt) 
    {
        doExit();
    }

    
    
    private void selectFilePathActionPerformed(java.awt.event.ActionEvent evt) 
    {
        JFileChooser f = new JFileChooser();
        f.setCurrentDirectory(new File(this.saveFilePath));
        f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        f.showSaveDialog(null);
        
        if (f.getSelectedFile() != null)
        {
            String path = f.getSelectedFile().getAbsolutePath();
            if (!validatePath(path))
            {
                JOptionPane.showMessageDialog(this, "저장 경로가 잘못되었습니다.");
            }
            else
            {
                prefs.put("saveFilePath", path);
                loadPrefs();
            }
        }
    }

    
    
    private void transferFilesActionPerformed(java.awt.event.ActionEvent evt) {

        if (this.m.getDownloadManager().getNumProcessing(false) == 0)
        {
            prefs.putBoolean("doTransferFiles", this.transferFiles.isSelected());
        }
        loadPrefs();
    }


    
    private void OpenFolderActionPerformed(java.awt.event.ActionEvent evt) 
    {
    	try
        {
            Desktop.getDesktop().open(new File(this.saveFilePath));
        }
        catch (IOException ex)
        {
            JOptionPane.showMessageDialog(this, ex.toString());
        }
    }

    
    
    private void transferRawFilesActionPerformed(java.awt.event.ActionEvent evt)
    {
    	if (this.m.getDownloadManager().getNumProcessing(false) == 0)
        {
            prefs.putBoolean("doTransferRawFiles", this.transferRawFiles.isSelected());
        }
        loadPrefs();
    }

    
    
    synchronized private void abortTransfer()
    {
        if (this.m.getDownloadManager().getNumProcessingAll() != 0)
        {
            this.m.getDownloadManager().abortDownloading(this);
        }
    }

    
    
    private void dropDownStateChanged(java.awt.event.ItemEvent evt) 
    {        
    	if (evt.getStateChange() == ItemEvent.SELECTED && doSyncCameraSettings != null && doSyncCameraSettings)
        {
            sendSettingsToCamera(evt.getItem());
        }
    }


    
    private void focusActionPerformed(java.awt.event.ActionEvent evt) 
    {
    	prefs.putBoolean("doFocus", this.focus.isSelected());
    	this.focus.isSelected();
    	loadPrefs();
    }
    
    
    
    
    private javax.swing.JComboBox<String> avMenu;
    private javax.swing.JComboBox<String> tvMenu;
    private javax.swing.JComboBox<String> isoMenu;
    private javax.swing.JProgressBar batteryLevel;
    private javax.swing.JButton captureButton;
    private javax.swing.JCheckBox focus;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton selectFilePath;
    private javax.swing.JCheckBox transferFiles;
    private javax.swing.JCheckBox transferRawFiles;
    private javax.swing.JButton OpenFolder;
    private JLabel photoguraphy;
}
