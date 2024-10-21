#region Copyright
/* ************************************************************
 * © 2017 Ricoh Company, Ltd.
 * ************************************************************/
#endregion

using System;
using System.IO;
using System.Linq;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Media.Imaging;

using Ricoh.CameraController;

namespace LiveViewSampleApplication
{
    public partial class MainWindow : Window
    {
        private CameraDevice camera;
        private static IProgress<string> progress;
        private static IProgress<byte[]> frameProgress;

        private class EventListener : CameraEventListener
        {
            public override void ImageAdded(CameraDevice sender, CameraImage image)
            {
                progress.Report("Image(" + image.Name + ") has been added.");
            }
            public override void LiveViewFrameUpdated(CameraDevice sender, byte[] liveViewFrame)
            {
                frameProgress.Report(liveViewFrame);
            }
        }

        public MainWindow()
        {
            InitializeComponent();
            progress = new Progress<string>((string p) =>
            {
                text.Text = p;
                if ("LiveView has been stopped.".Equals(p))
                {
                    image.Source = null;
                }
            });
            frameProgress = new Progress<byte[]>((byte[] frameImage) =>
            {
                var memoryStream = new MemoryStream(frameImage);
                var bitmapImage = new BitmapImage();
                bitmapImage.BeginInit();
                bitmapImage.StreamSource = memoryStream;
                bitmapImage.EndInit();
                image.Source = bitmapImage;

            });
            progress.Report("Disconnected.");
        }

        protected override void OnClosed(EventArgs e)
        {
            if (camera == null) { return; }
            camera.Disconnect(DeviceInterface.USB);
        }

        private async void btnConnectOnClick(object sender, RoutedEventArgs e)
        {
            progress.Report("Connecting...");
            await Task.Run(() => {
                camera = CameraDeviceDetector.Detect(DeviceInterface.USB).FirstOrDefault();
                if (camera != null)
                {
                    if (camera.EventListeners.Count == 0)
                    {
                        camera.EventListeners.Add(new EventListener());
                    }
                    var response = camera.Connect(DeviceInterface.USB);
                    if (response.Equals(Response.OK))
                    {
                        progress.Report("Connected. Model: " + camera.Model + ", SerialNumber:" + camera.SerialNumber);
                    }
                    else
                    {
                        progress.Report("Connection is failed.");
                    }
                }
                else
                {
                    progress.Report("Device has not found.");
                }
            });
        }
        private async void btnDisconnectOnClick(object sender, RoutedEventArgs e)
        {
            if (camera == null) { return; }
            progress.Report("Disconnecting...");
            await Task.Run(() =>
            {
                camera.Disconnect(DeviceInterface.USB);
                progress.Report("Disconnected.");
            });

        }
        private async void btnStartOnClick(object sender, RoutedEventArgs e)
        {
            if (camera == null) { return; }
            progress.Report("LiveView is starting...");
            await Task.Run(() =>
            {
                var liveViewRes = camera.StartLiveView();
                string liveViewResultStr = liveViewRes.Result == Result.OK ? "LiveView has been started. "
                        : "StartLiveView " + "result: " + liveViewRes.Result.ToString() + ", Code: " + liveViewRes.Errors.First().Code.ToString() + ", Message : " + liveViewRes.Errors.First().Message + ". ";

                progress.Report(liveViewResultStr);
            });
        }
        private async void btnStopOnClick(object sender, RoutedEventArgs e)
        {
            if (camera == null) { return; }
            progress.Report("StopLiveView.");
            await Task.Run(() =>
            {
                var response = camera.StopLiveView();
                if (response.Result == Result.OK)
                {
                    progress.Report("LiveView has been stopped.");
                }
                else
                {
                    reportResult("StopLiveView", response);
                }
            });
        }
        private async void btnStartCaptureOnClick(object sender, RoutedEventArgs e)
        {
            if (camera == null) { return; }
            progress.Report("StartCapture is executing...");
            await Task.Run(() =>
            {
                var response = camera.StartCapture();
                reportResult("StartCapture.", response);
            });
        }
        private async void btnStartCaptureWithoutFocusOnClick(object sender, RoutedEventArgs e)
        {
            if (camera == null) { return; }
            progress.Report("StartCapture without focus is executing...");
            await Task.Run(() =>
            {
                var response = camera.StartCapture(false);
                reportResult("StartCapture without focus.", response);
            });
        }
        private async void btnStopCaptureOnClick(object sender, RoutedEventArgs e)
        {
            if (camera == null) { return; }
            progress.Report("StopCapture.");
            await Task.Run(() =>
            {
                camera.StopCapture();
                progress.Report("Capture has been stopped.");
            });
        }

        private void reportResult(string name, Response response)
        {
            progress.Report(name + " result: " + response.Result.ToString() + ((response.Result == Result.Error) ? ", Code: "+response.Errors.First().Code.ToString()+", Message : "+response.Errors.First().Message : ""));
        }

        private void aboutOnClick(object sender, RoutedEventArgs e)
        {
            MessageBoxResult result = MessageBox.Show(
                "RICOH Camera USB SDK for Microsoft® .NET Framework\n" +
                "LiveView Sample Application\n\n" +
                "Copyright (c) 2017 Ricoh Company, Ltd. All Rights Reserved.\n",
                "About This Application");
        }
    }
}
