rem /*      %0                                  %1             %2               %3          %4            %5        %6                */
rem /* call $(SolutionDir)\post_build_event.bat $(SolutionDir) $(Configuration) $(Platform) $(ProjectDir) $(OutDir) $(TargetFileName) */

if %3 == Win32 (
  if %2 == Release (
    copy "%1..\..\lib\release\x86\camera-usb-sdk-cpp.dll" "%1build\%2"
    copy "%1lib\vc140_dll\wxbase30u_vc140.dll" "%1build\%2" 
    copy "%1lib\vc140_dll\wxmsw30u_core_vc140.dll" "%1build\%2"
  ) else (
    copy "%1..\..\lib\debug\x86\camera-usb-sdk-cpp.dll" "%1build\%2"
    copy "%1lib\vc140_dll\wxbase30ud_vc140.dll" "%1build\%2"
    copy "%1lib\vc140_dll\wxmsw30ud_core_vc140.dll" "%1build\%2"
  )
) else (
  if %2 == Release (
    copy "%1..\..\lib\release\%3\camera-usb-sdk-cpp.dll" "%1build\%3\%2"
    copy "%1lib\vc140_x64_dll\wxbase30u_vc140_x64.dll" "%1build\%3\%2"
    copy "%1lib\vc140_x64_dll\wxmsw30u_core_vc140_x64.dll" "%1build\%3\%2"
  ) else (
    copy "%1..\..\lib\debug\%3\camera-usb-sdk-cpp.dll" "%1build\%3\%2"
    copy "%1lib\vc140_x64_dll\wxbase30ud_vc140_x64.dll" "%1build\%3\%2"
    copy "%1lib\vc140_x64_dll\wxmsw30ud_core_vc140_x64.dll" "%1build\%3\%2"
  )
)
