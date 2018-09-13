package app.mpv.com.mpvapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {

    //Message error authenticate
    final String MSG_ERROR = "Authenticated Failed.";
    //duration of register
    private final int DURACION_REGISTRO = 2000; // 3 segundos
    //Initialize Views objects
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private TextView link_login;
    @BindView(R.id.input_email)
    EditText email;
    @BindView(R.id.input_password)
    EditText password;
    @BindView(R.id.input_name)
    EditText user;
    @BindView(R.id.btn_sign_up)
    Button btn_sign_up;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        link_login = (TextView) findViewById(R.id.link_login);

        link_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    public void registroUsuario() {

        String strEmail = email.getText().toString().trim();
        String strPassword = password.getText().toString().trim();
        final String strUser = user.getText().toString().trim();

        if (TextUtils.isEmpty(strEmail)) {
            Toast.makeText(this, "Ingrese un correo valido", Toast.LENGTH_SHORT).show();

        }
        if (TextUtils.isEmpty(strPassword) && strPassword.length() < 8 && strPassword.contains("*")) {
            Toast.makeText(this, "Ingrese una contraseña valida", Toast.LENGTH_SHORT).show();

        }

        //Creado nuevo usuario
        Task<AuthResult> authResultTask = mAuth.createUserWithEmailAndPassword(strEmail, strPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(RegisterActivity.this, "Usuario " + strUser + " Creado!",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.

                            //Si el usuario ya existe
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(RegisterActivity.this, "Ese usuario ya existe!",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RegisterActivity.this, MSG_ERROR,
                                        Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                                startActivity(intent);

                                //TODO progressbar
                            }

                        }

                        // ...
                    }
                });
    }


    @OnClick(R.id.btn_sign_up)
    public void signUp(View view) {

        registroUsuario();

        //3 segundos antes de pasar al Login despues de registrarse
        new Handler().postDelayed(new Runnable() {
            public void run() {

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, DURACION_REGISTRO);
    }
}