package za.ac.up.cs.www.navup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.ReferenceQueue;
import java.util.HashMap;
import java.util.Map;


public class registerPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        System.out.println("Register-page");

        TextView errorlbl = (TextView) findViewById(R.id.errorMsg);

    }

    public void registerUser(View view)
    {
        //change this for UP-longsword server
        String urlAddress = "http://10.0.2.2:8080/nav-up/users/registerAsUser";

        EditText username, password,email;
        final TextView errorlbl;

        username = (EditText) findViewById(R.id.userName);
        password = (EditText) findViewById(R.id.userPassword);
        email = (EditText) findViewById(R.id.userEmail);
        errorlbl = (TextView) findViewById(R.id.errorMsg);

        JSONObject userDetails = new JSONObject();
        try {
            userDetails.put("username", username.getText().toString());
            userDetails.put("password", password.getText().toString());
            userDetails.put("email", email.getText().toString());

            //testing purposes
            System.out.println(userDetails.toString());


        } catch (JSONException e) {
            e.printStackTrace();
        }


        //the Json requestHandler
        if(username.getText().toString().trim().isEmpty() || password.getText().toString().trim().isEmpty() || email.getText().toString().trim().isEmpty())
        {
            errorlbl.setText("Missing fields, please enter");
        }
        else
        {
            JsonObjectRequest jsonRequest  = new JsonObjectRequest(Request.Method.POST, urlAddress, userDetails,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response)
                        {
                            /*
                                The JSON Object in the response will look as follows:
                                {"Success":true} if the user was successfully registered
                                or
                                {"Success":false} if the user already exists
                             */

                            try
                            {
                                Boolean success = Boolean.parseBoolean(response.getString("Success"));
                                if(!success)
                                {
                                    // Some error message
                                    errorlbl.setText("User already exists");
                                }
                                else
                                {
                                    // User is registered
                                    errorlbl.setText("Successfully Registered");
                                    go_to_login();
                                }
                            }
                            catch(Exception e)
                            {
                                errorlbl.setText(e.toString());
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Error handling
                            System.out.println("Something went wrong!");
                            errorlbl.setText(error.getMessage());
                            error.printStackTrace();
                        }
                    }){
                @Override
                public Map<String, String> getHeaders() {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    //headers.put("Content-Type", "application/json;");
                    return headers;
                }
                @Override
                public String getBodyContentType() {
                    return "application/json";
                }
            };
            Volley.newRequestQueue(this).add(jsonRequest);
        }
    }

    public void go_to_login()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}