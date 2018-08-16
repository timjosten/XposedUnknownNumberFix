package ru.timjosten.unknownnumberfix;

import de.robv.android.xposed.*;
import de.robv.android.xposed.callbacks.*;

public class UnknownNumberFix implements IXposedHookLoadPackage
{
  private static final boolean DEBUG = false;
  private static final String TAG = UnknownNumberFix.class.getSimpleName() + ": ";

  public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam)
  throws Throwable
  {
    if(!lpparam.packageName.equals("com.android.phone"))
      return;

    try
    {
      XposedHelpers.findAndHookMethod("com.android.internal.telephony.Connection", lpparam.classLoader, "getAddress",
      new XC_MethodHook()
      {
        @Override
        protected void afterHookedMethod(MethodHookParam param)
        throws Throwable
        {
          try
          {
            String mAddress = (String)param.getResult();

            if(mAddress == null || mAddress.isEmpty())
            {
              String mDialString = (String)XposedHelpers.callMethod(param.thisObject, "getOrigDialString");

              if(DEBUG)
                XposedBridge.log(TAG + "mDialString=" + mDialString);

              param.setResult(mDialString);
            }
          }
          catch(Throwable t)
          {
            XposedBridge.log(TAG + t);
          }
        }
      });
    }
    catch(Throwable t)
    {
      XposedBridge.log(TAG + t);
    }
  }
}
