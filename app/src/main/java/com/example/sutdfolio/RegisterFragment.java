package com.example.sutdfolio;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sutdfolio.utils.APIRequest;
import com.example.sutdfolio.utils.Listener;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    EditText emailReg;
    EditText passwordReg;
    EditText nameReg;
    EditText studentIDReg;
    Button register;
    String userID;

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        emailReg = view.findViewById(R.id.EmailReg);
        passwordReg = view.findViewById(R.id.PasswordReg);
        nameReg = view.findViewById(R.id.NameReg);
        studentIDReg = view.findViewById(R.id.StudentIDReg);
        register = view.findViewById(R.id.registration);


        //TODO handle exceptions, invalid email, email already registered, null field
        // for all the 4 fields
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                APIRequest api = APIRequest.getInstance();
                api.register(new Listener<JSONObject>() {
                    @Override
                    public void getResult(JSONObject object) {
                        Toast.makeText(getActivity(),"Registered, please log in.",Toast.LENGTH_LONG).show();
                        NavController navController = Navigation.findNavController(view);
                        navController.navigate(R.id.action_registerFragment_to_loginFragment);
                    }
                }, emailReg.getText().toString(), passwordReg.getText().toString(), nameReg.getText().toString(), Integer.valueOf(studentIDReg.getText().toString()));
            }
        });


    }
}