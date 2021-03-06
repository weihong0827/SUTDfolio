package com.example.sutdfolio.ui.login;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.sutdfolio.databinding.FragmentLogin2Binding;

import com.example.sutdfolio.R;
import com.example.sutdfolio.utils.APIRequest;
import com.example.sutdfolio.utils.Listener;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

public class LoginFragment extends Fragment {

    private LoginViewModel loginViewModel;
    private FragmentLogin2Binding binding;
    SharedPreferences pref;
    NavController navController;
    String token = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentLogin2Binding.inflate(inflater, container, false);
//        if (checkToken()){
//            navController = Navigation.findNavController(binding.getRoot());
//            navController.navigate(R.id.action_loginFragment_to_profileFragment);
//        }
        return binding.getRoot();
    }

//    private boolean checkToken(){
//        pref = this.getActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
//        token = pref.getString("token", "");
//        if (token.equals("")){
//            return false;
//        }else{return true;}
//
//    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        super.onCreate(savedInstanceState);
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);
        TextView errorText = getView().findViewById(R.id.ErrorMessage);
        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;
        final CircularProgressButton loginButton = binding.login;

        final Button registerButton = binding.register;

        loginViewModel.getLoginFormState().observe(getViewLifecycleOwner(), new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(getViewLifecycleOwner(), new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }

                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.startAnimation();
                APIRequest api =APIRequest.getInstance();
//                Toast.makeText(getActivity(),"pressed",Toast.LENGTH_LONG).show();
                api.login(new Listener<JSONObject>() {
                    @Override
                    public void getResult(JSONObject object) {
                        try {
                            Toast.makeText(getActivity(), "Email Sent", Toast.LENGTH_LONG).show();
                            String details = object.getString("Details");
                            Bundle bundle = new Bundle();
                            bundle.putString("details", details);
                            bundle.putString("email", usernameEditText.getText().toString());
                            NavController navController = Navigation.findNavController(v);
                            navController.navigate(R.id.action_loginFragment_to_OTPverification, bundle);
                        } catch (JSONException e) {
                            loginButton.revertAnimation();
                            e.printStackTrace();
                            Log.d("login", "Wrong credentials");
                            Toast.makeText(getActivity(), "Wrong credentials, please try again", Toast.LENGTH_LONG).show();
                        }
                    }
                }, usernameEditText.getText().toString(), passwordEditText.getText().toString(), new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                                loginButton.revertAnimation();
                                errorText.setVisibility(View.VISIBLE);
                                if (null != error.networkResponse) {
                                    Log.d("LOGIN" + ": ", "Error Response code: " + error.networkResponse.statusCode);
                                    Log.d("LOGIN" + ": ", "Error Message: " + error.getMessage());
                            }

                    }
                }, view);

            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_loginFragment_to_registerFragment);
            }
        });
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        if (getContext() != null && getContext().getApplicationContext() != null) {
            Toast.makeText(getContext().getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
        }
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        if (getContext() != null && getContext().getApplicationContext() != null) {
            Toast.makeText(
                    getContext().getApplicationContext(),
                    errorString,
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}