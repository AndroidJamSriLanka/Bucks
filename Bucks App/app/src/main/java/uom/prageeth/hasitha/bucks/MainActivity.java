package uom.prageeth.hasitha.bucks;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new CurrencyFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public static class CurrencyFragment extends Fragment {

        private ArrayAdapter<String> adapter ;

        public CurrencyFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            String[] currencyData = {
                                    "2015-04-14 USD - United States Dollor - $ 1.0000",
                                    "2015-04-14 LKR - Sri Lankan Rupee - Rs 131.0536",
                                    "2015-04-14 AUD - Australian Dollor - $ 1.2889",
                                    "2015-04-14 EUR - Euro - € 0.9371",
                                    "2015-04-14 CHF - Swiss Franc - CHF 0.9634",
                                    "2015-04-14 GBP - British Pound - £ 0.6736",
                                    "2015-04-14 JPY - Japanese Yen - ¥ 119.3536",
                                    "2015-04-14 SGD - Singapore Dollor - $ 1.3547",
                                    "2015-04-14 CHF - Swiss Franc - CHF 0.9634",
                                    "2015-04-14 GBP - British Pound - £ 0.6736",
                                    "2015-04-14 AUD - Australian Dollor - $ 1.2889"
                                    };

            List<String> currencyList = new ArrayList<String>(Arrays.asList(currencyData));
            adapter = new  ArrayAdapter<String>(getActivity(),R.layout.currency_item,R.id.currency_item_textview,currencyList);

            ListView listView = (ListView)rootView.findViewById(R.id.currency_listview);
            listView.setAdapter(adapter);
            return rootView;
        }
    }
}
