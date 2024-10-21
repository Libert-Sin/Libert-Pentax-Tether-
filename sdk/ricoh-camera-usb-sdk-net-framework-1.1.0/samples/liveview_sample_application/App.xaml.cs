using System;
using System.Windows;
using System.Windows.Threading;

namespace LiveViewSampleApplication
{
    public partial class App : Application
    {
        private void Application_DispatcherUnhandledException(object sender, DispatcherUnhandledExceptionEventArgs e)
        {
            MessageBox.Show(e.Exception.Message, e.Exception.GetType().ToString(), MessageBoxButton.OK, MessageBoxImage.Error);
            e.Handled = true;
        }
    }
}
