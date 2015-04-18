package uom.prageeth.hasitha.bucks;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CurrencyFragment extends Fragment {

    private ArrayAdapter<String> adapter ;

    public CurrencyFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        inflater.inflate(R.menu.currencyfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            FetchCurrencyRatesTask currencyRatesTask = new FetchCurrencyRatesTask();
            currencyRatesTask.execute();
            return true;
        }
        return super.onOptionsItemSelected(item);
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

    public class FetchCurrencyRatesTask extends AsyncTask<Void,Void,String[]> {

        private final String LOG_TAG = FetchCurrencyRatesTask.class.getSimpleName();
        private String[] currencyTypeArray = {"USD","EUR","GBP","AUD","JPY","CAD","CHF","CNY","SGD","LKR"};

        private String getReadableDateTimeString(long time){
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("EEE,dd MMM yyyy hh:mm");
            return dateTimeFormat.format(time);
        }

        private String[] getCurrencyRatesFromJson(String currencyJsonString)
                throws JSONException {

            final String GER_RATE = "rate";
            final String GER_NAME = "name";

            JSONObject currencyJson = new JSONObject(currencyJsonString);

            Time timeObj = new Time(Time.getCurrentTimezone());
            timeObj.setToNow();
            String time = getReadableDateTimeString(timeObj.toMillis(false));

            String[] resultStrs = new String[currencyTypeArray.length];
            int i = 0;

            for(String key :currencyTypeArray){

                JSONObject keyJsonObject = currencyJson.getJSONObject(key);

                Double rate = keyJsonObject.getDouble(GER_RATE);
                String name = keyJsonObject.getString(GER_NAME);

                resultStrs[i++] =  time + " - " + key + " - " + name + " - " + " - " + rate ;
            }

        //           symbol
            for (String s : resultStrs) {
                Log.v(LOG_TAG, "Currency entry: " + s);
            }
            return resultStrs;
        }

        @Override
        protected String[] doInBackground(Void... params) {

            if (currencyTypeArray.length == 0) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Raw JSON response as a string
            String currencyJsonString = null;

            String includeCurrencyNames = "names";

            StringBuilder currencyType = new StringBuilder();

            for(String currency :currencyTypeArray){
                currencyType.append(currency+",");
            }

            try {
                // http://www.getexchangerates.com/api/
                final String CURRENCY_RATES_BASE_URL = "http://www.getexchangerates.com/api/latest.json?";
                final String CURRENCY_NAMES_PARAM = "include";
                final String CURRENCY_TYPES_PARAM = "currencies";

                Uri builtUri = Uri.parse(CURRENCY_RATES_BASE_URL).buildUpon()
                        .appendQueryParameter(CURRENCY_NAMES_PARAM, includeCurrencyNames)
                        .appendQueryParameter(CURRENCY_TYPES_PARAM, currencyType.toString())
                        .build();

                URL url = new URL(builtUri.toString());

                // Create the request to Get Exchange Rates, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();

                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuffer buffer = new StringBuffer();
                String line;

                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                currencyJsonString = buffer.toString();

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;

            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            try{
                return getCurrencyRatesFromJson(currencyJsonString);
            }catch (JSONException e){
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }
    }
}