package me.prathamesh.careertrack;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class QueryFragment extends Fragment {

    private FirebaseFirestore db;
    private ProgressDialog dialog;

    public QueryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_query, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        this.db = FirebaseFirestore.getInstance();
        dialog = new ProgressDialog(getContext());
        dialog.setTitle("Sending");
        dialog.setMessage("Please wait...");
        getView().findViewById(R.id.submitBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });
    }

    private void validate() {
        EditText nameTl = (EditText) getView().findViewById(R.id.nameEt);
        String name = nameTl.getText().toString();

        EditText mobTl = (EditText) getView().findViewById(R.id.mobileEt);
        String mob = mobTl.getText().toString();

        EditText courseTl = (EditText) getView().findViewById(R.id.courseEt);
        String course = courseTl.getText().toString();

        EditText cityTl = (EditText) getView().findViewById(R.id.cityEt);
        String city = cityTl.getText().toString();

        EditText countryTl = (EditText) getView().findViewById(R.id.countryEt);
        String country = countryTl.getText().toString();

        if (name.equals("PrathameshAuti")) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/prathamesh-a")));
            clearFields();
            return;
        }

        if (TextUtils.isEmpty(name)) nameTl.setError("Enter name");
        else if (TextUtils.isEmpty(mob)) mobTl.setError("Enter mobile");
        else if (TextUtils.isEmpty(course)) courseTl.setError("Enter course name");
        else if (TextUtils.isEmpty(city)) cityTl.setError("Enter city and state");
        else if (TextUtils.isEmpty(country)) countryTl.setError("Enter country");
        else addToDB(name, mob, course, city, country);
    }

    private void addToDB(String name, String mob, String course, String city, String country) {
        dialog.show();
        Map<String, Object> queryData = new HashMap<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        queryData.put("name", name);
        queryData.put("mobile", mob);
        queryData.put("course", course);
        queryData.put("city", city);
        queryData.put("country", country);

        db.collection("queryData").document(user.getEmail()).set(queryData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                dialog.dismiss();
                Toast.makeText(getContext(), "Query Sent Successfully!", Toast.LENGTH_SHORT).show();
                clearFields();
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Toast.makeText(getContext(), "Failed to query, contact developer.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearFields() {
        EditText nameTl = (EditText) getView().findViewById(R.id.nameEt);
        nameTl.getText().clear();

        EditText mobTl = (EditText) getView().findViewById(R.id.mobileEt);
        mobTl.getText().clear();

        EditText courseTl = (EditText) getView().findViewById(R.id.courseEt);
        courseTl.getText().clear();

        EditText cityTl = (EditText) getView().findViewById(R.id.cityEt);
        cityTl.getText().clear();

        EditText countryTl = (EditText) getView().findViewById(R.id.countryEt);
        countryTl.getText().clear();
    }

}