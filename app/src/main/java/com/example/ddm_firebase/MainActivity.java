package com.example.ddm_firebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

import org.w3c.dom.Text;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private final FirebaseAuth usuarios = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView txtRegister = findViewById(R.id.txtRegister);
        EditText txtEmailLogin = findViewById(R.id.txtEmailLogin);
        EditText txtPasswordLogin = findViewById(R.id.txtPasswordLogin);
        Button btnLogin = findViewById(R.id.btnLogin);

        txtRegister.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(i);
        });

        btnLogin.setOnClickListener(view -> {
            String email = txtEmailLogin.getText().toString();
            String password = txtPasswordLogin.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(MainActivity.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            } else {
                usuarios.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Login efetuado com sucesso", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(MainActivity.this, HomeActivity.class);
                        startActivity(i);
                    } else {
                        String erro;
                        try {
                            throw Objects.requireNonNull(task.getException());
                        } catch (Exception e) {
                            erro = "Erro ao efetuar login";
                        }
                        Toast.makeText(MainActivity.this, erro, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}