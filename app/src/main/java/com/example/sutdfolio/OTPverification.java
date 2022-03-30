package com.example.sutdfolio;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.sutdfolio.databinding.FragmentLogin2Binding;
import com.example.sutdfolio.databinding.FragmentOTPverificationBinding;
import com.example.sutdfolio.utils.APIRequest;
import com.example.sutdfolio.utils.Listener;

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
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            getDetails = getArguments().getString(DETAILS);
            getEmail = getArguments().getString(EMAIL);
        }
    }

    String status = "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentOTPverificationBinding.inflate(inflater, container, false);
        final EditText otpEditText = binding.otpfield;
        final Button otpverification = binding.verify;


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
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, getOtp, getDetails, getEmail);
            }
        });


        return binding.getRoot();


    }




    }
}