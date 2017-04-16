import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Idrian on 2017-04-04.
 */

/*
    I'm stil busy implimenting the singleton class (Like what type of function should/could I add etc to make future calls simpler), but for now I coded the login and register responses in
    the respective Activities
 */

class requestQueueSingleton {
    //private static final requestQueueSingleton ourInstance = new requestQueueSingleton();

   /* static requestQueueSingleton getInstance() {
        return ourInstance;
    }*/

    private RequestQueue requestQueue;

    private static Context mContext;

    private requestQueueSingleton(Context context) {
        mContext = context;
        requestQueue = getRequestQueue();

    }

    public RequestQueue getRequestQueue()
    {
        if(requestQueue == null)
        {
            requestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag)
    {
        req.setTag(tag);
        getRequestQueue().add(req);
    }
}
