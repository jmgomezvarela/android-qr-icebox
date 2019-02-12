package tesina.qr;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Fragment;
import android.app.FragmentTransaction;
 
public class TabsListener implements ActionBar.TabListener {
 
    Fragment fragment;
 
    public TabsListener(Fragment fragment) {
        // TODO Auto-generated constructor stub
        this.fragment = fragment;
    }
 
    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        // TODO Auto-generated method stub
    	//Cache.getInstance();
        ft.replace(R.id.fragment_container, fragment);
    }
 
    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
        // TODO Auto-generated method stub
        ft.remove(fragment);
    }
 
    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
        // TODO Auto-generated method stub
 
    }
}