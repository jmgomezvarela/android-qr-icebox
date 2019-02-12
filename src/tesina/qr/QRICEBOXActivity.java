package tesina.qr;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

public class QRICEBOXActivity extends Activity {
    /** Called when the activity is first created. */
	// Declare Tab Variable
    ActionBar.Tab TabAlta, TabListado;
    Fragment fragmentTabListado = new FragmentTabListado();
    Fragment fragmentTabAlta = new FragmentTabAlta();
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //creacion de Prestañas
        ActionBar actionBar = getActionBar();
        
        // Hide Actionbar Icon
        actionBar.setDisplayShowHomeEnabled(false);
 
        // Hide Actionbar Title
        actionBar.setDisplayShowTitleEnabled(false);
 
        // Create Actionbar Tabs
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
 
        // Set Tab Icon and Titles
        TabAlta = actionBar.newTab().setText("Alta");
        TabListado = actionBar.newTab().setText("Listado");

 
        // Set Tab Listeners
        TabListado.setTabListener(new TabsListener(fragmentTabListado));
        TabAlta.setTabListener(new TabsListener(fragmentTabAlta));
     
 
        // Add tabs to actionbar
        actionBar.addTab(TabListado);
        actionBar.addTab(TabAlta);
       
        
    }
    }


