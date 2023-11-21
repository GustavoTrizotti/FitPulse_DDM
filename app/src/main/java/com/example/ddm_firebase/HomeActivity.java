package com.example.ddm_firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity {
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

    private EditText txtName, txtWeight, txtAge, txtHeight, txtSex;
    private RadioGroup radioGroup;
    private Button btnSaveTrains;
    private ListView listView;
    private List<Train> trainList = new ArrayList<>();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        txtName = findViewById(R.id.txtName);
        txtWeight = findViewById(R.id.txtWeight);
        txtHeight = findViewById(R.id.txtHeight);
        txtAge = findViewById(R.id.txtAge);
        txtSex = findViewById(R.id.txtSex);
        radioGroup = findViewById(R.id.radioGroup);
        btnSaveTrains = findViewById(R.id.btnSave);

        listView = findViewById(R.id.list_trains);

        TrainAdapter adapter = new TrainAdapter(this, (ArrayList<Train>) trainList);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        btnSaveTrains.setOnClickListener(v -> saveTrain());

        DatabaseReference data = dbRef.child("trains");
        FirebaseListener listener = new FirebaseListener(trainList, adapter);
        data.addValueEventListener(listener);
    }

    private void saveTrain() {
        String name = txtName.getText().toString().trim();
        String weightStr = txtWeight.getText().toString().trim();
        String heightStr = txtHeight.getText().toString().trim();
        String ageStr = txtAge.getText().toString().trim();
        String sex = txtSex.getText().toString().trim();

        if (!name.isEmpty() && !weightStr.isEmpty() && !heightStr.isEmpty() && !sex.isEmpty()) {
            double weight = Double.parseDouble(weightStr);
            int age = Integer.parseInt(ageStr);
            int height = Integer.parseInt(heightStr);

            int selectedRadioId = radioGroup.getCheckedRadioButtonId();
            Day day = getDayFromId(selectedRadioId);

            // Gere uma chave única para cada treino
            String key = dbRef.child("trains").push().getKey();

            // Crie um nó com a chave gerada e defina os valores do treino nesse nó
            dbRef.child("trains").child(key).setValue(new Train(name, weight, height, age, sex, day));

            txtName.setText("");
            txtWeight.setText("");
            txtHeight.setText("");
            txtSex.setText("");

            Toast.makeText(this, "Treino salvo com sucesso!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
        }
    }

    private Day getDayFromId(int selectedRadioId) {
        if (R.id.radioSunday == selectedRadioId) {
            return Day.SUNDAY;
        } else if (R.id.radioMonday == selectedRadioId) {
            return Day.MONDAY;
        } else if (R.id.radioTuesday == selectedRadioId) {
            return Day.TUESDAY;
        } else if (R.id.radioWed == selectedRadioId) {
            return Day.WEDNESDAY;
        } else if (R.id.radioThursday == selectedRadioId) {
            return Day.THURSDAY;
        } else if (R.id.radioFriday == selectedRadioId) {
            return Day.FRIDAY;
        } else if (R.id.radioSaturday == selectedRadioId) {
            return Day.SATURDAY;
        } else {
            return null;
        }
    }

    private static class FirebaseListener implements ValueEventListener {
        private final List<Train> trainList;
        private final TrainAdapter adapter;

        public FirebaseListener(List<Train> trainList, TrainAdapter adapter) {
            this.trainList = trainList;
            this.adapter = adapter;
        }

        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists()) {
                trainList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (ds != null && ds.getValue() != null) {
                        if (ds.getValue() instanceof HashMap) {
                            HashMap<String, Object> data = (HashMap<String, Object>) ds.getValue();
                            Train train = new Train();
                            train.setName((String) data.get("name"));
                            train.setWeight(getDouble(data.get("weight")));
                            train.setHeight(((Long) Objects.requireNonNull(data.get("height"))).intValue());
                            train.setSex((String) data.get("sex"));
                            train.setDay(Day.valueOf((String) data.get("day")));
                            trainList.add(train);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
                Log.d("FirebaseListener", "Data changed. New data: " + trainList);
            }
        }

        private double getDouble(Object value) {
            if (value instanceof Long) {
                return ((Long) value).doubleValue();
            } else if (value instanceof Double) {
                return (Double) value;
            } else {
                // Handle other cases or return a default value
                return 0.0;
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    }

}