#region Copyright
/* ************************************************************
 * © 2017 Ricoh Company, Ltd.
 * ************************************************************/
#endregion

using System;
using System.IO;
using System.Collections.Generic;
using System.Runtime.InteropServices;
using System.Linq;
using System.Threading.Tasks;
using Ricoh.CameraController;
using System.Windows;

namespace ConsoleSampleApplication
{
    class CLI
    {
        static List<CameraDevice> detectedCameraDevices;

        class EventListener : CameraEventListener
        {
            public CameraDevice CameraDevice { get; set; }

            public EventListener(CameraDevice cameraDevice)
            {
                this.CameraDevice = cameraDevice;
            }

            public override void ImageAdded(CameraDevice sender, CameraImage image)
            {
                Console.Write("\n[Device({0})] Image Added. ", detectedCameraDevices.IndexOf(this.CameraDevice));
                Console.WriteLine("Name: {0}, Type: {1}, Format: {2}, ID: {3}", image.Name, image.Type, image.Format, image.ID);
                if (image.Type == ImageType.StillImage)
                {
                    if (image.HasThumbnail)
                    {
                        var imageNameWithoutExtension = Path.GetFileNameWithoutExtension(image.Name);
                        using (FileStream fs = new FileStream(
                                        Environment.CurrentDirectory + Path.DirectorySeparatorChar + imageNameWithoutExtension + "_thumb.JPG",
                                        FileMode.Create, FileAccess.Write))
                        {
                            var imageGetResponse = image.GetThumbnail(fs);
                            Console.WriteLine("Get Thumbnail is " + (imageGetResponse.Result == Result.OK ? "SUCCEED." : "FAILED."));
                            if (imageGetResponse.Result == Result.OK)
                            {
                                Console.WriteLine("Image Path: {0}", Environment.CurrentDirectory + Path.DirectorySeparatorChar + imageNameWithoutExtension + "_thumb.JPG");
                            }
                            else
                            {
                                foreach (var error in imageGetResponse.Errors)
                                {
                                    Console.WriteLine("Error Code: " + error.Code.ToString() + " / Error Message: " + error.Message);
                                }
                            }
                        }
                    }
                    using (FileStream fs = new FileStream(
                                    Environment.CurrentDirectory + Path.DirectorySeparatorChar + image.Name,
                                    FileMode.Create, FileAccess.Write))
                    {
                        var imageGetResponse = image.GetData(fs);
                        Console.WriteLine("Get Image is " + (imageGetResponse.Result == Result.OK ? "SUCCEED." : "FAILED."));
                        if (imageGetResponse.Result == Result.OK)
                        {
                            Console.WriteLine("Image Path: {0}", Environment.CurrentDirectory + Path.DirectorySeparatorChar + image.Name);
                        }
                        else
                        {
                            foreach (var error in imageGetResponse.Errors)
                            {
                                Console.WriteLine("Error Code: " + error.Code.ToString() + " / Error Message: " + error.Message);
                            }
                        }
                    }
                }
                Console.Write("> ");
            }
            public override void ImageStored(CameraDevice sender, CameraImage image)
            {
                Console.Write("\n[Device({0})] Image Stored. ", detectedCameraDevices.IndexOf(this.CameraDevice));
                Console.WriteLine("Storage: {0}, Name: {1}, Type: {2}, Format: {3}, Size: {4}, ID: {5}", this.CameraDevice.Storages.IndexOf(image.Storage), image.Name, image.Type, image.Format, image.Size, image.ID);
                Console.Write("> ");
            }
            public override void CaptureComplete(CameraDevice sender, Capture capture)
            {
                Console.WriteLine("\n[Device({0})] Capture Complete. Capture ID: {1}", detectedCameraDevices.IndexOf(this.CameraDevice), capture.ID);
                Console.Write("> ");
            }
            public override void DeviceDisconnected(CameraDevice sender, DeviceInterface deviceInterface)
            {
                Console.WriteLine("\nDevice({0}) Disconnected.", detectedCameraDevices.IndexOf(this.CameraDevice));
                Console.Write("> ");
            }
            public override void CaptureSettingsChanged(CameraDevice sender, List<CaptureSetting> newSettings)
            {
                Console.WriteLine("\nDevice({0}) CaptureSettingsChanged:");
                foreach (var setting in newSettings)
                {
                    if (setting is ShutterSpeed) { Console.WriteLine("  {0}", setting.ToString()); }
                    else if (setting is WhiteBalance) { Console.WriteLine("  {0}", setting.ToString()); }
                    else if (setting is FNumber) { Console.WriteLine("  {0}", setting.ToString()); }
                    else if (setting is ISO) { Console.WriteLine("  {0}", setting.ToString()); }
                    else if (setting is ExposureCompensation) { Console.WriteLine("  {0}", setting.ToString()); }
                    else if (setting is ExposureProgram) { Console.WriteLine("  {0}", setting.ToString()); }
                    else if (setting is HyperOperationEnable) { Console.WriteLine("  {0}", setting.ToString()); }
                    else if (setting is UserCaptureSettingsMode) { Console.WriteLine("  {0}", setting.ToString()); }
                    else { Console.WriteLine(" Unknown CaptureSetting: {0}", setting.ToString()); }
                }
                Console.Write("> ");
            }
        }
        static private void displaySelection() {
            Console.WriteLine("\n");
            Console.WriteLine("RICOH Camera USB SDK for Microsoft(R) .NET Framework Sample Application");
            Console.WriteLine("=======================================\n");
            Console.WriteLine("0.  Detect Device");
            Console.WriteLine("1.  Connect Device");
            Console.WriteLine("2.  Start Capture");
            Console.WriteLine("3.  List Images");
            Console.WriteLine("4.  Get Image");
            Console.WriteLine("5.  Check Capture State");
            Console.WriteLine("6.  Check Camera Status");
            Console.WriteLine("7.  Check Storages");
            Console.WriteLine("8.  Delete Image");
            Console.WriteLine("9.  Get FNumber");
            Console.WriteLine("10. Set FNumber");
            Console.WriteLine("11. Get ExposureCompensation");
            Console.WriteLine("12. Set ExposureCompensation");
            Console.WriteLine("13. Get ISO");
            Console.WriteLine("14. Set ISO"); 
            Console.WriteLine("15. Get Thumbnail Image");
            Console.WriteLine("16. Get White Balance");
            Console.WriteLine("17. Set White Balance");
            Console.WriteLine("18. Get Shutter Speed");
            Console.WriteLine("19. Set Shutter Speed");
            Console.WriteLine("20. Get Camera Time");
            Console.WriteLine("21. Set Camera Time(Set Current Time)");
            Console.WriteLine("22. Focus");
            Console.WriteLine("23. Start Capture Without Focus");
            Console.WriteLine("24. Get Storage Writing");
            Console.WriteLine("25. Set Storage Writing");
            Console.WriteLine("26. Connect All Devices");
            Console.WriteLine("27. Focus of All Connecting Cameras");
            Console.WriteLine("28. Start Capture Without Focus of All Connecting Cameras");
            Console.WriteLine("29. Get ExposureProgram, HyperOperationEnable, UserCaptureSettingsMode");
            Console.WriteLine("30. Stop Capture");
            Console.WriteLine("31. Focus on Specified Position");
            Console.WriteLine("32. Start Capture With Focusing on Specified Position");
            Console.WriteLine("33. Get LiveView Specification");
            Console.WriteLine("34. Get Still Image Capture Format");
            Console.WriteLine("35. Set Still Image Capture Format");
            Console.WriteLine("36. Get Still Image Quality");
            Console.WriteLine("37. Set Still Image Quality");
            Console.WriteLine("38. Get Capture Method");
            Console.WriteLine("90. Disconnect Device");
            Console.WriteLine("99. Exit");
            Console.Write("> ");
        }
        static void DoMenu()
        {
            uint selectionIndex = 0;

            Response response;
            CameraDevice camera = null;

            var needDetected = new List<int>() { };
            for (var i = 2; i < 26; i++){ needDetected.Add(i); }
            for (var i = 27; i < 38; i++) { needDetected.Add(i); }

            Console.WriteLine("RICOH Camera USB SDK for Microsoft(R) .NET Framework Sample Application");
            Console.WriteLine("Copyright (c) 2017 Ricoh Company, Ltd. All Rights Reserved.");

            while (selectionIndex != 99)
            {
                int imageIndex = 0;

                displaySelection();
                bool result = uint.TryParse(Console.ReadLine(), out selectionIndex);
                if (result)
                {
                    if (needDetected.Any(num => num == selectionIndex) && camera == null)
                    {
                        Console.WriteLine("Device was not found.");
                        continue;
                    }

                    switch (selectionIndex)
                    {
                        case 0:
                            detectedCameraDevices = CameraDeviceDetector.Detect(DeviceInterface.USB);
                            var detectedCameraDevicesCount = detectedCameraDevices.Count;
                            if (detectedCameraDevicesCount > 0)
                            {
                                Console.WriteLine("{0} Device(s) has been detected.", detectedCameraDevicesCount);
                                Console.WriteLine("Detected Device(s):");
                                for (var i = 0; i < detectedCameraDevicesCount; i++)
                                {
                                    var detectedCameraDevice = detectedCameraDevices[i];
                                    Console.WriteLine("  [{0}]", i);
                                    Console.WriteLine("    Manufacturer    : {0}", detectedCameraDevice.Manufacturer);
                                    Console.WriteLine("    Model           : {0}", detectedCameraDevice.Model);
                                    Console.WriteLine("    Firmware Version: {0}", detectedCameraDevice.FirmwareVersion);
                                    Console.WriteLine("    Serial Number   : {0}", detectedCameraDevice.SerialNumber);
                                    Console.WriteLine("    USB Connection  : {0}", detectedCameraDevice.IsConnected(DeviceInterface.USB) ? "Connected" : "Disconnected");
                                }
                            }
                            else
                            {
                                Console.WriteLine("Device was not found.");
                            }
                            break;
                        case 1:
                            detectedCameraDevices = CameraDeviceDetector.Detect(DeviceInterface.USB);
                            camera = detectedCameraDevices.FirstOrDefault();
                            if (camera != null)
                            {
                                if (camera.EventListeners.Count == 0)
                                {
                                    camera.EventListeners.Add(new EventListener(camera));
                                }
                                response = camera.Connect(DeviceInterface.USB);
                                Console.WriteLine("Device connection is " + (response.Result == Result.OK ? "SUCCEED." : "FAILED."));
                                if (response.Result == Result.OK) {
                                    Console.WriteLine("Connect Device:");
                                    Console.WriteLine("  Manufacturer    : {0}", camera.Manufacturer);
                                    Console.WriteLine("  Model           : {0}", camera.Model);
                                    Console.WriteLine("  Firmware Version: {0}", camera.FirmwareVersion);
                                    Console.WriteLine("  Serial Number   : {0}", camera.SerialNumber);
                                }
                                else
                                {
                                    writeErrorsInfo(response.Errors);
                                }
                            }
                            else
                            {
                                Console.WriteLine("Device has not found.");
                            }
                            break;
                        case 2:
                            var captureResponse = camera.StartCapture();
                            if (captureResponse.Result == Result.OK)
                            {
                                Console.WriteLine("Capturing has started. Capture ID: {0}", captureResponse.Capture.ID);
                            }
                            else
                            {
                                Console.WriteLine("Capturing is FAILED. detail: {0}", captureResponse.Errors[0].Message);
                            }
                            break;
                        case 3:
                            for (var i = 0; i < camera.Storages.Count; i++)
                            {
                                var storage = camera.Storages[i];
                                Console.WriteLine("\n[{0}]\n  Storage ID: 0x{1}", i, uint.Parse(storage.ID).ToString("x"));
                                Console.WriteLine("  StorageListImagesState: {0}", storage.ListImagesState.ToString());
                            }
                            Console.WriteLine("Images:");
                            for (var i = 0; i < camera.Images.Count; i++)
                            {
                                var image = camera.Images[i];
                                Console.WriteLine("  [{0}] Storage: {1}, Name: {2}, Type: {3}, Format: {4}, Size: {5}, ID: {6}, HasThumbnail: {7}, Date: {8}", i, camera.Storages.IndexOf(image.Storage), image.Name, image.Type, image.Format, image.Size, image.ID, image.HasThumbnail, image.DateTime);
                            }
                            break;
                        case 4:
                            Console.WriteLine("");
                            Console.Write("Select Image[0-{0}] > ", camera.Images.Count - 1);
                            imageIndex = 0;
                            result = int.TryParse(Console.ReadLine(), out imageIndex);
                            if (result) {
                                var image = camera.Images[imageIndex];
                                using (FileStream fs = new FileStream(
                                    Environment.CurrentDirectory + Path.DirectorySeparatorChar + image.Name,
                                    FileMode.Create, FileAccess.Write))
                                {
                                    var imageGetResponse = image.GetData(fs);
                                    Console.WriteLine("Get Image is " + (imageGetResponse.Result == Result.OK ? "SUCCEED." : "FAILED."));
                                    if (imageGetResponse.Result == Result.OK)
                                    {
                                        Console.WriteLine("Image Path: {0}", Environment.CurrentDirectory + Path.DirectorySeparatorChar + image.Name);
                                    }
                                    else
                                    {
                                        writeErrorsInfo(imageGetResponse.Errors);
                                    }
                                }
                            }
                            break;
                        case 5:
                            var capture = camera.Status.CurrentCapture;
                            if (capture == null)
                            {
                                Console.WriteLine("Capture is NOT executing.");
                            }
                            else
                            {
                                Console.WriteLine("Capture State:");
                                Console.WriteLine("  ID    : {0}", capture.ID);
                                Console.WriteLine("  Method: {0}", capture.Method.Value);
                                Console.WriteLine("  State : {0}", capture.State);
                            }
                            break;
                        case 6:
                            var status = camera.Status;
                            Console.WriteLine("Camera status:");
                            Console.WriteLine("  BatteryLevel       : {0}", status.BatteryLevel);
                            break;
                        case 7:
                            var storages = camera.Storages;
                            Console.WriteLine("Storages:");
                            for (var i = 0; i < storages.Count; i++)
                            {
                                var storage = storages[i];
                                Console.WriteLine("[{0}]", i);
                                Console.WriteLine("  StorageID: 0x{0}", uint.Parse(storage.ID).ToString("x"));
                                Console.WriteLine("    Type             : {0}", storage.Type.ToString());
                                Console.WriteLine("    IsAvailable      : {0}", storage.IsAvailable.ToString());
                                Console.WriteLine("    MaxCapacity      : {0}", storage.MaxCapacity.ToString());
                                Console.WriteLine("    Permission       : {0}", storage.Permission.ToString());
                                Console.WriteLine("    RemainingPictures: {0}", storage.RemainingPictures.ToString());
                                Console.WriteLine("    FreeSpace        : {0}", storage.FreeSpace.ToString());
                            }
                            break;
                        case 8:
                            Console.WriteLine("");
                            Console.Write("Select Deleting Image[0-{0}] > ", camera.Images.Count - 1);
                            imageIndex = 0;
                            result = int.TryParse(Console.ReadLine(), out imageIndex);
                            if (result)
                            {
                                var deleteTarget = camera.Images[imageIndex];
                                response = deleteTarget.Delete();
                                Console.WriteLine("Delete Image({0}) is {1}", deleteTarget.Name, (response.Result == Result.OK ? "SUCCEED." : "FAILED."));
                            }
                            break;
                        case 9:
                            var fNumber = new FNumber();
                            response = camera.GetCaptureSettings(new List<CaptureSetting>() { fNumber });
                            if (response.Result == Result.OK)
                            {
                                Console.WriteLine(fNumber.ToString());
                                Console.WriteLine("FNumber Available:");
                                foreach (var fn in fNumber.AvailableSettings) { Console.Write(fn.Value.ToString() + ", "); }
                            }
                            else
                            {
                                Console.WriteLine("Get FNumber is FAILED.");
                                writeErrorsInfo(response.Errors);
                            }
                            break;
                        case 10:
                            fNumber = new FNumber();
                            response = camera.GetCaptureSettings(new List<CaptureSetting>() { fNumber });
                            if (response.Result == Result.OK)
                            {
                                Console.Write("FNumber [");
                                foreach (var fn in fNumber.AvailableSettings) { Console.Write(fn.Value.ToString() + ", "); }
                                Console.Write("] > ");
                                string inputFNumber = Console.ReadLine();
                                CaptureSetting setFNumber = null;
                                foreach (var fn in fNumber.AvailableSettings)
                                {
                                    if (fn.Value.ToString() == inputFNumber)
                                    {
                                        setFNumber = fn;
                                        break;
                                    }
                                }
                                if (setFNumber == null)
                                {
                                    Console.WriteLine("Invalid value");
                                    break;
                                }
                                response = camera.SetCaptureSettings(new List<CaptureSetting>() { setFNumber });
                                Console.WriteLine("FNumber setting is " + (response.Result == Result.OK ? "SUCCEED." : "FAILED."));
                            }
                            else
                            {
                                Console.WriteLine("FNumber Available List is not exist.");
                                writeErrorsInfo(response.Errors);
                            }
                            break;
                        case 11:
                            var exposureCompensation = new ExposureCompensation();
                            response = camera.GetCaptureSettings(new List<CaptureSetting>() { exposureCompensation });
                            if (response.Result == Result.OK)
                            {
                                Console.WriteLine(exposureCompensation.ToString());
                                Console.WriteLine("ExposureCompensation Available:");
                                foreach (var ec in exposureCompensation.AvailableSettings) { Console.Write(ec.Value.ToString() + ", "); }
                            }
                            else
                            {
                                Console.WriteLine("Get ExposureCompensation is FAILED.");
                                writeErrorsInfo(response.Errors);
                            }
                            break;
                        case 12:
                            exposureCompensation = new ExposureCompensation();
                            response = camera.GetCaptureSettings(new List<CaptureSetting>() { exposureCompensation });
                            if (response.Result == Result.OK)
                            {
                                Console.Write("ExposureCompensation [");
                                foreach (var ec in exposureCompensation.AvailableSettings) { Console.Write(ec.Value.ToString() + ", "); }
                                Console.Write("] > ");
                                string inputExposureCompensation = Console.ReadLine();
                                CaptureSetting setExposureCompensation = null;
                                foreach (var ec in exposureCompensation.AvailableSettings)
                                {
                                    if (ec.Value.ToString() == inputExposureCompensation)
                                    {
                                        setExposureCompensation = ec;
                                        break;
                                    }
                                }
                                if (setExposureCompensation == null)
                                {
                                    Console.WriteLine("Invalid value");
                                    break;
                                }
                                response = camera.SetCaptureSettings(new List<CaptureSetting>() { setExposureCompensation });
                                Console.WriteLine("ExposureCompensation setting is " + (response.Result == Result.OK ? "SUCCEED." : "FAILED."));
                            }
                            else
                            {
                                Console.WriteLine("ExposureCompensation Available List is not exist.");
                                writeErrorsInfo(response.Errors);
                            }
                            break;
                        case 13:
                            var iso = new ISO();
                            response = camera.GetCaptureSettings(new List<CaptureSetting>() { iso });
                            if (response.Result == Result.OK)
                            {
                                Console.WriteLine(iso.ToString());
                                Console.WriteLine("ISO Available:");
                                foreach (var i in iso.AvailableSettings) { Console.Write(i.Value.ToString() + ", "); }
                            }
                            else
                            {
                                Console.WriteLine("Get ISO is FAILED.");
                                writeErrorsInfo(response.Errors);
                            }
                            break;
                        case 14:
                            iso =  new ISO();
                            response = camera.GetCaptureSettings(new List<CaptureSetting>() { iso });
                            if (response.Result == Result.OK)
                            {
                                Console.Write("ISO [");
                                foreach (var i in iso.AvailableSettings) { Console.Write(i.Value.ToString() + ", "); }
                                Console.Write("] > ");
                                string inputISO = Console.ReadLine();
                                CaptureSetting setISO = null;
                                foreach (var i in iso.AvailableSettings)
                                {
                                    if (i.Value.ToString() == inputISO)
                                    {
                                        setISO = i;
                                        break;
                                    }
                                }
                                if (setISO == null)
                                {
                                    Console.WriteLine("Invalid value");
                                    break;
                                }
                                response = camera.SetCaptureSettings(new List<CaptureSetting>() { setISO });
                                Console.WriteLine("ISO setting is " + (response.Result == Result.OK ? "SUCCEED." : "FAILED."));
                            }
                            else
                            {
                                Console.WriteLine("ISO Available List is not exist.");
                                writeErrorsInfo(response.Errors);
                            }
                            break;
                        case 15:
                            Console.WriteLine("");
                            Console.Write("Select Image[0-{0}] > ", camera.Images.Count - 1);
                            imageIndex = 0;
                            result = int.TryParse(Console.ReadLine(), out imageIndex);
                            if (result)
                            {
                                var image = camera.Images[imageIndex];
                                var fileNameWithoutExtension = Path.GetFileNameWithoutExtension(image.Name);
                                using (FileStream fs = new FileStream(
                                    Environment.CurrentDirectory + Path.DirectorySeparatorChar + fileNameWithoutExtension + "_thumb.JPG",
                                    FileMode.Create, FileAccess.Write))
                                {
                                    var imageGetResponse = image.GetThumbnail(fs);
                                    Console.WriteLine("Get Thumbnail Image is " + (imageGetResponse.Result == Result.OK ? "SUCCEED." : "FAILED."));
                                    if (imageGetResponse.Result == Result.OK)
                                    {
                                        Console.WriteLine("Image Path: {0}", Environment.CurrentDirectory + Path.DirectorySeparatorChar + fileNameWithoutExtension + "_thumb.JPG");
                                    }
                                    else
                                    {
                                        writeErrorsInfo(imageGetResponse.Errors);
                                    }
                                }
                            }
                            break;
                        case 16:
                            var getWB = new WhiteBalance();
                            response = camera.GetCaptureSettings(new List<CaptureSetting>() { getWB });
                            if (response.Result == Result.OK)
                            {
                                Console.WriteLine(getWB.ToString());
                            }
                            else
                            {
                                Console.WriteLine("Get White Balance is FAILED.");
                                writeErrorsInfo(response.Errors);
                            }
                            break;
                        case 17:
                            getWB = new WhiteBalance();
                            response = camera.GetCaptureSettings(new List<CaptureSetting>() { getWB });
                            if (response.Result == Result.OK)
                            {
                                Console.Write("White Balance [");
                                foreach (var wb in getWB.AvailableSettings) { Console.Write(wb.Value.ToString() + ", "); }
                                Console.Write("] > ");
                                string inputWhiteBalance = Console.ReadLine();
                                CaptureSetting setWhiteBalance = null;
                                foreach (var wb in getWB.AvailableSettings)
                                {
                                    if (wb.Value.ToString().Equals(inputWhiteBalance))
                                    {
                                        setWhiteBalance = wb;
                                        break;
                                    }
                                }
                                if (setWhiteBalance == null)
                                {
                                    Console.WriteLine("Invalid value");
                                    break;
                                }
                                response = camera.SetCaptureSettings(new List<CaptureSetting>() { setWhiteBalance });
                                Console.WriteLine("Set WhiteBalance is " + (response.Result == Result.OK ? "SUCCEED." : "FAILED."));
                                if (response.Result == Result.Error)
                                {
                                    writeErrorsInfo(response.Errors);
                                }
                            }
                            else
                            {
                                Console.WriteLine("White Balance Available List is not exist.");
                                writeErrorsInfo(response.Errors);
                            }
                            break;
                        case 18:
                            var shutterSpeed = new ShutterSpeed();
                            response = camera.GetCaptureSettings(new List<CaptureSetting>() { shutterSpeed });
                            if (response.Result == Result.OK)
                            {
                                Console.WriteLine(shutterSpeed.ToString());
                                Console.WriteLine("ShutterSpeed Available:");
                                foreach (var i in shutterSpeed.AvailableSettings) { Console.Write(i.Value.ToString() + ", "); }
                            }
                            else
                            {
                                Console.WriteLine("Get Shutter Speed is FAILED.");
                                writeErrorsInfo(response.Errors);
                            }
                            break;
                        case 19:
                            shutterSpeed = new ShutterSpeed();
                            response = camera.GetCaptureSettings(new List<CaptureSetting>() { shutterSpeed });
                            if (response.Result == Result.OK)
                            {
                                Console.Write("ShutterSpeed [");
                                foreach (var ss in shutterSpeed.AvailableSettings) { Console.Write(ss.Value.ToString() + ", "); }
                                Console.Write("] > ");
                                string inputShutterSpeed = Console.ReadLine();
                                CaptureSetting setShutterSpeed = null;
                                foreach (var ss in shutterSpeed.AvailableSettings)
                                {
                                    if (ss.Value.ToString() == inputShutterSpeed)
                                    {
                                        setShutterSpeed = ss;
                                        break;
                                    }
                                }
                                if (setShutterSpeed == null)
                                {
                                    Console.WriteLine("Invalid value");
                                    break;
                                }
                                response = camera.SetCaptureSettings(new List<CaptureSetting>() { setShutterSpeed });
                                Console.WriteLine("ShutterSpeed setting is " + (response.Result == Result.OK ? "SUCCEED." : "FAILED."));
                            }
                            else
                            {
                                Console.WriteLine("Shutter Speed Available List is not exist.");
                                writeErrorsInfo(response.Errors);
                            }
                            break;
                        case 20:
                            var getCameraTime = new CameraTime();
                            response = camera.GetCameraDeviceSettings(new List<CameraDeviceSetting>() { getCameraTime });
                            if (response.Result == Result.OK)
                            {
                                Console.WriteLine(getCameraTime.ToString());
                            }
                            else
                            {
                                writeErrorsInfo(response.Errors);
                            }
                            break;
                        case 21:
                            DateTime dateTime = DateTime.Now;
                            Console.WriteLine("Set Current time: " + dateTime.ToString());
                            var setCameraTime = new CameraTime(dateTime);
                            response = camera.SetCameraDeviceSettings(new List<CameraDeviceSetting>() { setCameraTime });
                            Console.WriteLine("Set DateTime is " + (response.Result == Result.OK ? "SUCCEED." : "FAILED."));
                            if(response.Result == Result.Error)
                            {
                                writeErrorsInfo(response.Errors);
                            }
                            break;
                        case 22:
                            response = camera.Focus();
                            if (response.Equals(Response.OK))
                            {
                                Console.WriteLine("Focus has started.");
                            }
                            else
                            {
                                Console.WriteLine("Focus is FAILED. detail: {0}", response.Errors[0].Message);
                            }
                            break;
                        case 23:
                            captureResponse = camera.StartCapture(false);
                            if (captureResponse.Result == Result.OK)
                            {
                                Console.WriteLine("Capturing without focus has started. Capture ID: {0}", captureResponse.Capture.ID);
                            }
                            else
                            {
                                Console.WriteLine("Capturing without focus is FAILED. detail: {0}", captureResponse.Errors[0].Message);
                            }
                            break;

                        case 24:
                            var storageWriting = new StorageWriting();
                            camera.GetCaptureSettings(new List<CaptureSetting>() { storageWriting });
                            Console.WriteLine(storageWriting.ToString());
                            Console.Write("Available: [");
                            foreach (var sw in storageWriting.AvailableSettings) { Console.Write(sw.Value.ToString() + ", "); }
                            Console.Write("] >");
                            break;
                        case 25:
                            storageWriting = new StorageWriting();
                            camera.GetCaptureSettings(new List<CaptureSetting>() { storageWriting });
                            Console.Write("StorageWriting [");
                            foreach (var sw in storageWriting.AvailableSettings) { Console.Write(sw.Value.ToString() + ", "); }
                            Console.Write("] >");
                            string inputStorageWriting = Console.ReadLine();
                            bool isSave;
                            try
                            {
                                isSave = Convert.ToBoolean(inputStorageWriting);
                            }
                            catch
                            {
                                Console.WriteLine("Invalid value");
                                break;
                            }
                            CaptureSetting setStorageWriting = isSave ? StorageWriting.True : StorageWriting.False;
                            response = camera.SetCaptureSettings(new List<CaptureSetting>() { setStorageWriting });
                            Console.WriteLine("StorageWriting setting is " + (response.Result == Result.OK ? "SUCCEED." : "FAILED."));
                            break;
                        case 26:
                            detectedCameraDevices = CameraDeviceDetector.Detect(DeviceInterface.USB);
                            if (camera == null && detectedCameraDevices.Count > 0) {
                                camera = detectedCameraDevices.First();
                            }
                            for (var i = 0; i < detectedCameraDevices.Count; i++)
                            {
                                var detectedCameraDevice = detectedCameraDevices[i];
                                if (detectedCameraDevice.EventListeners.Count == 0)
                                {
                                    detectedCameraDevice.EventListeners.Add(new EventListener(detectedCameraDevice));
                                }
                                response = detectedCameraDevice.Connect(DeviceInterface.USB);
                                Console.WriteLine("Connect Device({0}) is {1}", i, (response.Result == Result.OK ? "SUCCEED." : "FAILED."));
                            }
                            break;
                        case 27:
                            var connectingCameraDevices = detectedCameraDevices.FindAll(cameraDevice => cameraDevice.IsConnected(DeviceInterface.USB));
                            foreach (var cameraDevice in connectingCameraDevices) { cameraDevice.Focus(); }
                            break;
                        case 28:
                            connectingCameraDevices = detectedCameraDevices.FindAll(cameraDevice => cameraDevice.IsConnected(DeviceInterface.USB));
                            foreach (var cameraDevice in connectingCameraDevices) { StartCaptureAsync(cameraDevice, false); }
                            break;
                        case 29:
                            var exposureProgram = new ExposureProgram();
                            var hyperOperationEnable = new HyperOperationEnable();
                            var userCaptureSettingsMode = new UserCaptureSettingsMode();
                            camera.GetCaptureSettings(new List<CaptureSetting>() { exposureProgram, hyperOperationEnable, userCaptureSettingsMode });
                            Console.WriteLine(exposureProgram.ToString());
                            Console.WriteLine(hyperOperationEnable.ToString());
                            Console.WriteLine(userCaptureSettingsMode.ToString());
                            break;
                        case 30:
                            response = camera.StopCapture();
                            Console.WriteLine("StopCapture is " + (response.Result == Result.OK ? "SUCCEED." : "FAILED."));
                            if (response.Result == Result.Error)
                            {
                                writeErrorsInfo(response.Errors);
                            }
                            break;
                        case 31:
                            var liveViewSpecificationString = GetLiveViewSpecificationString(camera);
                            if (liveViewSpecificationString.Equals(String.Empty)) { break; }
                            Console.WriteLine(liveViewSpecificationString.Substring(liveViewSpecificationString.IndexOf("FocusArea")));

                            Console.Write("x position > ");
                            string inputX = Console.ReadLine();
                            double positionX;
                            result = Double.TryParse(inputX, out positionX);
                            if (!result || (positionX < 0.0) || (positionX > 1.0))
                            {
                                Console.WriteLine("Invalid value");
                                break;
                            }

                            Console.Write("y position > ");
                            string inputY = Console.ReadLine();
                            double positionY;
                            result = Double.TryParse(inputY, out positionY);
                            if (!result || (positionY < 0.0) || (positionY > 1.0))
                            {
                                Console.WriteLine("Invalid value");
                                break;
                            }

                            response = camera.Focus(new Vector(positionX, positionY));
                            if (response.Equals(Response.OK))
                            {
                                Console.WriteLine("Focus has started.");
                            }
                            else
                            {
                                Console.WriteLine("Focus is FAILED. detail: {0}", response.Errors[0].Message);
                            }
                            break;
                        case 32:
                            liveViewSpecificationString = GetLiveViewSpecificationString(camera);
                            if (liveViewSpecificationString.Equals(String.Empty)) { break; }
                            Console.WriteLine(liveViewSpecificationString.Substring(liveViewSpecificationString.IndexOf("FocusArea")));

                            Console.Write("x position > ");
                            inputX = Console.ReadLine();
                            result = Double.TryParse(inputX, out positionX);
                            if (!result || (positionX < 0.0) || (positionX > 1.0))
                            {
                                Console.WriteLine("Invalid value");
                                break;
                            }

                            Console.Write("y position > ");
                            inputY = Console.ReadLine();
                            result = Double.TryParse(inputY, out positionY);
                            if (!result || (positionY < 0.0) || (positionY > 1.0))
                            {
                                Console.WriteLine("Invalid value");
                                break;
                            }

                            captureResponse = camera.StartCapture(new Vector(positionX, positionY));
                            if (captureResponse.Result == Result.OK)
                            {
                                Console.WriteLine("Capturing has started. Capture ID: {0}", captureResponse.Capture.ID);
                            }
                            else
                            {
                                Console.WriteLine("Capturing is FAILED. detail: {0}", captureResponse.Errors[0].Message);
                            }
                            break;
                        case 33:
                            liveViewSpecificationString = GetLiveViewSpecificationString(camera);
                            if (!liveViewSpecificationString.Equals(String.Empty))
                            {
                                Console.WriteLine(liveViewSpecificationString);
                            }
                            break;
                        case 34:
                            var stillImageCaptureFormat = new StillImageCaptureFormat();
                            response = camera.GetCaptureSettings(new List<CaptureSetting>() { stillImageCaptureFormat });
                            if (response.Result == Result.OK)
                            {
                                Console.WriteLine(stillImageCaptureFormat.ToString());
                                Console.WriteLine("StillImageCaptureFormat Available:");
                                foreach (var format in stillImageCaptureFormat.AvailableSettings) { Console.Write(format.Value.ToString() + ", "); }
                            }
                            else
                            {
                                Console.WriteLine("Get Still Image Capture Format is FAILED.");
                                writeErrorsInfo(response.Errors);
                            }
                            break;
                        case 35:
                            stillImageCaptureFormat = new StillImageCaptureFormat();
                            camera.GetCaptureSettings(new List<CaptureSetting>() { stillImageCaptureFormat });
                            Console.Write("StillImageCaptureFormat [");
                            foreach (var format in stillImageCaptureFormat.AvailableSettings) { Console.Write(format.Value.ToString() + ", "); }
                            Console.Write("] >");
                            string inputStillImageCaptureFormat = Console.ReadLine();
                            CaptureSetting setStillImageCaptureFormat = null;
                            foreach (var format in stillImageCaptureFormat.AvailableSettings)
                            {
                                if (format.Value.ToString() == inputStillImageCaptureFormat)
                                {
                                    setStillImageCaptureFormat = format;
                                    break;
                                }
                            }
                            if (setStillImageCaptureFormat == null)
                            {
                                Console.WriteLine("Invalid value");
                                break;
                            }
                            response = camera.SetCaptureSettings(new List<CaptureSetting>() { setStillImageCaptureFormat });
                            Console.WriteLine("StillImageCaptureFormat setting is " + (response.Result == Result.OK ? "SUCCEED." : "FAILED."));
                            break;
                        case 36:
                            var stillImageQuality = new StillImageQuality();
                            response = camera.GetCaptureSettings(new List<CaptureSetting>() { stillImageQuality });
                            if (response.Result == Result.OK)
                            {
                                Console.WriteLine(stillImageQuality.ToString());
                                Console.WriteLine("StillImageQuality Available:");
                                foreach (var format in stillImageQuality.AvailableSettings) { Console.Write(format.Value.ToString() + ", "); }
                            }
                            else
                            {
                                Console.WriteLine("Get Still Image Quality is FAILED.");
                                writeErrorsInfo(response.Errors);
                            }
                            break;
                        case 37:
                            stillImageQuality = new StillImageQuality();
                            camera.GetCaptureSettings(new List<CaptureSetting>() { stillImageQuality });
                            Console.Write("StillImageQuality [");
                            foreach (var format in stillImageQuality.AvailableSettings) { Console.Write(format.Value.ToString() + ", "); }
                            Console.Write("] >");
                            string inputStillImageQuality = Console.ReadLine();
                            CaptureSetting setStillImageQuality = null;
                            foreach (var format in stillImageQuality.AvailableSettings)
                            {
                                if (format.Value.ToString() == inputStillImageQuality)
                                {
                                    setStillImageQuality = format;
                                    break;
                                }
                            }
                            if (setStillImageQuality == null)
                            {
                                Console.WriteLine("Invalid value");
                                break;
                            }
                            response = camera.SetCaptureSettings(new List<CaptureSetting>() { setStillImageQuality });
                            Console.WriteLine("StillImageQuality setting is " + (response.Result == Result.OK ? "SUCCEED." : "FAILED."));
                            break;
                        case 38:
                            var captureMethod = new CaptureMethod();
                            response = camera.GetCaptureSettings(new List<CaptureSetting>() { captureMethod });
                            if (response.Result == Result.OK)
                            {
                                Console.WriteLine(captureMethod.ToString());
                            }
                            else
                            {
                                Console.WriteLine("Get Capture Method is FAILED.");
                                writeErrorsInfo(response.Errors);
                            }
                            break;
                        case 90:
                            if (detectedCameraDevices != null)
                            {
                                connectingCameraDevices = detectedCameraDevices.FindAll(cameraDevice => cameraDevice.IsConnected(DeviceInterface.USB));
                                foreach (var connectingCameraDevice in connectingCameraDevices)
                                {
                                    response = connectingCameraDevice.Disconnect(DeviceInterface.USB);
                                    Console.WriteLine("Disconnect Device({0}) is {1}", connectingCameraDevices.IndexOf(connectingCameraDevice), (response.Result == Result.OK ? "SUCCEED." : "FAILED."));
                                }
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
            if (detectedCameraDevices != null) {
                foreach (var connectingCameraDevice in detectedCameraDevices.FindAll(cameraDevice => cameraDevice.IsConnected(DeviceInterface.USB)))
                {
                    response = connectingCameraDevice.Disconnect(DeviceInterface.USB);
                }
            }
        }
        static async void StartCaptureAsync(CameraDevice cameraDevice, bool withFocus = true)
        {
            await Task.Run(() => cameraDevice.StartCapture(withFocus));
        }
        static private void writeErrorsInfo(List<Error> errors)
        {
            foreach (var error in errors)
            {
                Console.WriteLine("Error Code: " + error.Code.ToString() + " / Error Message: " + error.Message);
            }
        }
        static private String GetLiveViewSpecificationString(CameraDevice cameraDevice)
        {
            String liveViewSpecificationString = String.Empty;

            var liveViewSpecification = new LiveViewSpecification();
            var response = cameraDevice.GetCameraDeviceSettings(new List<CameraDeviceSetting>() { liveViewSpecification }); ;
            if (response.Result == Result.OK)
            {
                var data = (LiveViewSpecificationValue)liveViewSpecification.Value;
                liveViewSpecificationString = data.Get().ToString();
            }
            else
            {
                Console.WriteLine("Get LiveView Specification is FAILED.");
                writeErrorsInfo(response.Errors);
            }
            return liveViewSpecificationString;
        }

        static void Main(string[] args)
        {
            handler = new ConsoleEventDelegate(ConsoleEventCallback);
            SetConsoleCtrlHandler(handler, true);

            DoMenu();
        }

        static bool ConsoleEventCallback(int eventType)
        {
            switch (eventType)
            {
                //Ctrl+C
                case 0:
                //Close
                case 2:
                    if (detectedCameraDevices != null)
                    {
                        foreach (var connectingCameraDevice in detectedCameraDevices.FindAll(cameraDevice => cameraDevice.IsConnected(DeviceInterface.USB)))
                        {
                            connectingCameraDevice.Disconnect(DeviceInterface.USB);
                        }
                    }
                    break;
            }
            return false;
        }
        static ConsoleEventDelegate handler; 
        private delegate bool ConsoleEventDelegate(int eventType);
        [DllImport("kernel32.dll", SetLastError = true)]
        private static extern bool SetConsoleCtrlHandler(ConsoleEventDelegate callback, bool add);
    }
}
