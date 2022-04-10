package com.example.sutdfolio;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditProfileFragment extends Fragment {

    EditText aboutme;
    EditText classof;
    EditText pillar;
    ImageView avatar;

    String oriAboutMe;
    String oriClassOf;
    String oriPillar;
    String oriAvatar;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditProfileFragment newInstance(String param1, String param2) {
        EditProfileFragment fragment = new EditProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            oriAboutMe = getArguments().getString("aboutme");
            oriClassOf = getArguments().getString("classof");
            oriPillar = getArguments().getString("pillar");
            oriAvatar = getArguments().getString("avatar");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_edit_profile, container, false);

        aboutme = view.findViewById(R.id.Edit_About_Me);
        classof = view.findViewById(R.id.Edit_Class_Of);
        pillar = view.findViewById(R.id.Edit_Pillar);
        avatar = view.findViewById(R.id.AvatarEdit);

        if (oriAvatar!=null){
            Glide
                    .with(getActivity())
                    .load(oriAvatar)
                    .centerCrop()
//                .placeholder(R.drawable.loading_spinner)
                    .into(avatar);
        }
            
        

        return view;
    }
}