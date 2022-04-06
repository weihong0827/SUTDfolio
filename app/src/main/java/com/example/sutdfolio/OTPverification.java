package com.example.sutdfolio;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sutdfolio.data.model.Posts;
import com.example.sutdfolio.data.model.ReadPost;
import com.example.sutdfolio.data.model.User;
import com.example.sutdfolio.databinding.FragmentLogin2Binding;
import com.example.sutdfolio.databinding.FragmentOTPverificationBinding;
import com.example.sutdfolio.ui.login.OTPFormState;
import com.example.sutdfolio.utils.APIRequest;
import com.example.sutdfolio.utils.Listener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OTPverification#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OTPverification extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String DETAILS = "details";
    private static final String EMAIL = "email";
    private FragmentOTPverificationBinding binding;

    // TODO: Rename and change types of parameters
    private String getDetails;
    private String getEmail;
    private Integer getOtp;


    public OTPverification() {
        // Required empty public constructor
    }
    EditText otp;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment OTPverification.
     */
    // TODO: Rename and change types and number of parameters
    public static OTPverification newInstance() {
        OTPverification fragment = new OTPverification();
        return fragment;
    }

    String status = "";
    String token = "";
    SharedPreferences pref;

    Button otpverification;
    EditText otpEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pref = this.getActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        if (getArguments() != null) {
            getDetails = getArguments().getString(DETAILS);
            getEmail = getArguments().getString(EMAIL);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentOTPverificationBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        otpEditText = binding.otpfield;
        otpverification = binding.verify;

        //for OTP field checking
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // check whether both the fields are empty or not
                otpverification.setEnabled(s!=null && s.length()==6);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };

        otpEditText.addTextChangedListener(textWatcher);
        otpverification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getOtp = Integer.valueOf(otpEditText.getText().toString());

                APIRequest api = APIRequest.getInstance();
                api.verify(new Listener<JSONObject>() {
                    @Override
                    public void getResult(JSONObject object) {
                        try {

                            status = object.getString("Status");
                            token = object.getString("Token");
                                SharedPreferences.Editor prefEditor = pref.edit();
                                prefEditor.putString("token", token);

                                Log.d("otp", token);

                                prefEditor.apply();
                                prefEditor.commit();
                                Log.d("tokenput", pref.getString("token", ""));
                                NavController navController = Navigation.findNavController(view);
                                navController.navigate(R.id.action_OTPverification_to_profileFragment);

                                //TODO navigate to the logged in profile page
                                //todo store jwt token on the phone


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("otp","wrong otp");
                            Toast.makeText(getActivity(), "wrong otp please try again", Toast.LENGTH_LONG).show();
                        }
                    }
                }, getOtp, getDetails, getEmail);
            }

        });
//        if (status.equals("Success")){
//            APIRequest api = APIRequest.getInstance();
//            api.getUser(new Listener<JSONObject>() {
//                @Override
//                public void getResult(JSONObject object) {
//                    try {
//                        user = object.getString("User");
//                        posts = object.getString("Posts");
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    final Gson gson = new Gson();
//                    userObj = gson.fromJson(user, User.class);
//                    postsObj = gson.fromJson(posts, Posts[].class);
//                        //object.getString("Profile");
//                    }
//            }, token);
//        }


    }}
