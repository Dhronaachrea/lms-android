package com.tabbar;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

public class SpinnerICS 
{
    private Spinner spinner = null;
    private View spinnerICS = null;
    private Method setAdapterICS = null;
    private Method setSelectionICS = null;
    private Method getAdapterICS = null;
    private Method getSelectedItemPositionICS = null;
    private Method getSelectedItemICS = null;
    private Class<?> OnItemSelectedListenerICS = null;
    private Method setOnItemSelectedListenerICS = null;

    public SpinnerICS(Context context)
    {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            try {
                Class<?> c = Class.forName("android.support.v7.internal.widget.SpinnerICS");            
                Constructor<?> ctor = c.getDeclaredConstructor(Context.class, AttributeSet.class, int.class);
                ctor.setAccessible(true);
                setAdapterICS = c.getMethod("setAdapter", SpinnerAdapter.class);
                setSelectionICS = c.getMethod("setSelection", int.class);
                getAdapterICS = c.getMethod("getAdapter");
                getSelectedItemPositionICS = c.getMethod("getSelectedItemPosition");
                getSelectedItemICS = c.getMethod("getSelectedItem");
                OnItemSelectedListenerICS = Class.forName("android.support.v7.internal.widget.AdapterViewICS$OnItemSelectedListener");
                setOnItemSelectedListenerICS = c.getMethod("setOnItemSelectedListener", OnItemSelectedListenerICS);             
                spinnerICS = (View)ctor.newInstance(context, null, android.support.v7.appcompat.R.attr.actionDropDownStyle);                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (spinnerICS == null) {
            spinner = new Spinner(context);
        }
    }

    public View getView()
    {
        return (spinnerICS != null ? spinnerICS : spinner);
    }

    public void setAdapter(SpinnerAdapter adapter)
    {
        if (spinnerICS != null) {
            try {
                setAdapterICS.invoke(spinnerICS, adapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (spinner != null) {
            spinner.setAdapter(adapter);
        }
    }

    public void setSelection(int position)
    {
        if (spinnerICS != null) {
            try {
                setSelectionICS.invoke(spinnerICS, position);
            } catch (Exception e) {
                e.printStackTrace();
            }           
        } else if (spinner != null) {
            spinner.setSelection(position);
        }
    }

    public SpinnerAdapter getAdapter()
    {
        if (spinnerICS != null) {
            try {
                return (SpinnerAdapter)getAdapterICS.invoke(spinnerICS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (spinner != null) {
            return spinner.getAdapter();
        }
        return null;
    }

    public int getSelectedItemPosition()
    {
        if (spinnerICS != null) {
            try {
                return (Integer)getSelectedItemPositionICS.invoke(spinnerICS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (spinner != null) {
            return spinner.getSelectedItemPosition();
        }
        return -1;
    }

    public Object getSelectedItem()
    {
        if (spinnerICS != null) {
            try {
                return getSelectedItemICS.invoke(spinnerICS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (spinner != null) {
            return spinner.getSelectedItem();
        }
        return null;        
    }

    public class OnItemSelectedListenerProxyListener implements java.lang.reflect.InvocationHandler
    {
        private OnItemSelectedListener listener;

        public OnItemSelectedListenerProxyListener(OnItemSelectedListener listener)
        {
            this.listener = listener; 
        }    

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
        {
            if (method.getName().equals("onItemSelected")) {
                listener.onItemSelected(null, (View)args[1], (Integer)args[2], (Long)args[3]);
            } else if (method.getName().equals("onNothingSelected")) {
                listener.onNothingSelected(null);
            }
            return null;
        }
    }


    public void setOnItemSelectedListener(OnItemSelectedListener listener)
    {
        if (spinnerICS != null) {
            try {               
                Object obj = Proxy.newProxyInstance(OnItemSelectedListenerICS.getClassLoader(), new Class<?>[] { OnItemSelectedListenerICS } , new OnItemSelectedListenerProxyListener(listener));
                setOnItemSelectedListenerICS.invoke(spinnerICS, obj);
            } catch (Exception e) {
                e.printStackTrace();
            }           
        } else if (spinner != null) {
            spinner.setOnItemSelectedListener(listener);
        }
    }   
}