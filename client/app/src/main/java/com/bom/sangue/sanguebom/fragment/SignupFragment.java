package com.bom.sangue.sanguebom.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bom.sangue.sanguebom.R;
import com.bom.sangue.sanguebom.Utils.Constants;
import com.bom.sangue.sanguebom.Utils.HttpManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by alan on 29/11/15.
 */
public class SignupFragment extends Fragment {

    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.signup, container, false);
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(getActivity()
                        , R.array.categoria_sangue,
                        android.R.layout.simple_spinner_item);

        ((Spinner) rootView.findViewById(R.id.signup_blood_type)).setAdapter(adapter);
        ImageButton btn = (ImageButton) rootView.findViewById(R.id.signup_btn);
        btn.setOnClickListener(mSignUpUserListener);

        return rootView;
    }

    private View.OnClickListener mSignUpUserListener = new View.OnClickListener() {
        public void onClick(View v) {
            final String login = ((EditText) rootView.findViewById(R.id.signup_login)).getText().toString();
            final String password = ((EditText) rootView.findViewById(R.id.signup_password)).getText().toString();
            final String email = ((EditText) rootView.findViewById(R.id.signup_email)).getText().toString();

            // TODO Validade data.
            // TODO create a user to send.

           /* DatePicker datePicker = (DatePicker) rootView.findViewById(R.id.signup_birth_date);
            int day = datePicker.getDayOfMonth();
            int month = datePicker.getMonth();
            int year = datePicker.getYear();

            // TODO Validate the birthDate only +18 !

            Date birthDate = new Date();
            Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
            cal.set(year, month, day, 0, 0, 0);
            String date = cal.getTime().getTime()
*/

            Spinner spinner = (Spinner) rootView.findViewById(R.id.signup_blood_type);
            String bloodType = spinner.getSelectedItem().toString();

            try {
                final JSONObject user = new JSONObject();
                JSONObject profile = new JSONObject();
                user.put("username", login);
                user.put("password", password);
                user.put("email", email);
                user.put("userProfile", profile);
                profile.put("birth_date", "20-11-2002");
                profile.put("blood_type", bloodType);

                JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, Constants.URL_SIGNUP, user,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                redirectToLogin();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("SANGUE_BOM REQUEST", "onErrorResponse " + error.getMessage());
                            }
                        }) {
                    // FIXME SECURITY FAILURE, this route has a admin token to register new users remove this dependence.
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Authorization", "Token 385fa1bcb4372c5262a5d51f291016310d47a5bd");
                        return headers;
                    }
                };

                HttpManager.getInstance(getActivity().getApplicationContext()).addToRequestQueue(stringRequest);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private void redirectToLogin() {
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.container, new MyProfileFragment());
        ft.commit();
    }
}