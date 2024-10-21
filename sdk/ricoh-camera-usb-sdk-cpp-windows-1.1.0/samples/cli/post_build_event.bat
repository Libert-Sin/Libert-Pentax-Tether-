rem /*      %0                                  %1             %2               %3          %4            %5        %6                */
rem /* call $(SolutionDir)\post_build_event.bat $(SolutionDir) $(Configuration) $(Platform) $(ProjectDir) $(OutDir) $(TargetFileName) */

if %3 == Win32 (
  if %2 == Release (
    copy "%1..\..\lib\release\x86\camera-usb-sdk-cpp.dll" "%1build\%2"
  ) else (
    copy "%1..\..\lib\debug\x86\camera-usb-sdk-cpp.dll" "%1build\%2"
  )
) else (
  if %2 == Release (
    copy "%1..\..\lib\release\%3\camera-usb-sdk-cpp.dll" "%1build\%3\%2"
  ) else (
    copy "%1..\..\lib\debug\%3\camera-usb-sdk-cpp.dll" "%1build\%3\%2"
  )
)
