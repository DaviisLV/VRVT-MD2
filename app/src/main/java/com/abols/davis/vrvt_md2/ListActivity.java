package com.abols.davis.vrvt_md2;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ListActivity extends AppCompatActivity {
    private ListView lv;

    ArrayList<HashMap<String, String>> contactList;
    private String TAG = "debug";
    String JSONUrl = "https://api.github.com/repositories";
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        pDialog = new ProgressDialog(this);
        contactList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);

        new GetContacts().execute();
    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog.setMessage("Notiek JSON ielāde...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            HttpHandler sh = new HttpHandler();

            String url = JSONUrl;
            String jsonStr = sh.makeServiceCall(url);

            if (jsonStr != null) {
                try {

                    JSONArray jsonArray = new JSONArray(jsonStr);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject c = jsonArray.getJSONObject(i);
                        JSONObject owner = c.getJSONObject("owner");

                        String user = owner.getString("login");
                        String name = c.getString("name");

                        HashMap<String, String> contact = new HashMap<>();

                        contact.put("user", user);
                        contact.put("name", name);

                        contactList.add(contact);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ListAdapter adapter = new SimpleAdapter(ListActivity.this, contactList,
                    R.layout.list_item, new String[]{"name", "user"},
                    new int[]{R.id.email, R.id.mobile});
            lv.setAdapter(adapter);
            pDialog.dismiss();
        }
    }
}

