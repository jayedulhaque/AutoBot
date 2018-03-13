Set WshShell = WScript.CreateObject("WScript.Shell")
WshShell.AppActivate "ProxyRental.Client"
WshShell.SendKeys "{ENTER}"
WshShell.SendKeys "^c"