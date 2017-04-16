package za.ac.up.cs.www.navup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /*goes to register page when register clicked */
    public void go_to_registerPage(View view)
    {
        Intent intent = new Intent(this, registerPage.class);
        startActivity(intent);
    }

    public void Login(View view)
    {
        //change for UP-Longsword
        /*
            Once I am done with the singleton class I will save the address there so it can be called repeatedly from there and saved once
            but for now I wrote it in Login and register
         */
        String urlAddress = "http://10.0.2.2:8080/nav-up/users/Login";

        EditText userName, password;
        final TextView errorlbl;

        userName = (EditText) findViewById(R.id.userName);
        password = (EditText) findViewById(R.id.Password);
        errorlbl = (TextView) findViewById(R.id.errorMsg);

        JSONObject userDetails = new JSONObject();
        try {
            userDetails.put("username", userName.getText().toString());
            userDetails.put("password", password.getText().toString());
            //testing purposes
            System.out.println(userDetails.toString());
            go_to_map();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //request to POST data
        if(userName.getText().toString().trim().isEmpty() || password.getText().toString().trim().isEmpty())
        {
            errorlbl.setText("Missing fields, please enter");
        }
        else
        {
            JsonObjectRequest jsonRequest  = new JsonObjectRequest(Request.Method.POST, urlAddress, userDetails,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            /*I expect a JSON object called user, if you're sending a string
                                I think you can simply chnage the response type to String
                                and change the respone listener and catch types to correspond to the string
                            */
                            /*
                                quick note the App will crash or atleast the current activity page will crash if the response is not a valid one
                                i.e. it is better not to send anything and have the request timeout than to send wrong data.
                             */
                            try{
                                response = response.getJSONObject("user");
                                String userName = response.getString("userName"),
                                        password = response.getString("password"),
                                        email = response.getString("email");
                                System.out.println("userName: "+userName+"\nPassword: "+password);
                                //Ok so how do I save this response to be carried over to
                                //a new Activity nl. the navigation page?
                                errorlbl.setText("Success");
                                errorlbl.setTextColor(Integer.parseInt("ff669900"));
                            }
                            catch(JSONException e)
                            {
                                errorlbl.setText(e.getMessage());
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
                    });

            Volley.newRequestQueue(this).add(jsonRequest);
        }


    }

    public void login_as_guest(View view)
    {
        go_to_map();
    }

    public void go_to_map()
    {
        Intent intent = new Intent(this, navigationPage.class);
        startActivity(intent);
    }
}
