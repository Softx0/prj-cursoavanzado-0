package app.mpv.com.mpvapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class LoginActivity extends AppCompatActivity {

    //Creando & Referenciando views objects
    TextView dontHaveAccount;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.emailLogin)
    EditText emailLogin;

    @BindView(R.id.passwordLogin)
    EditText passwordLogin;

    @BindView(R.id.btnSignIn)
    Button btnSingIn;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();

        dontHaveAccount = (TextView) findViewById(R.id.dontHaveAccount);

        dontHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });


    }


    public void iniciarSesion(){

        String strEmail = emailLogin.getText().toString().trim();
        String strPassword= passwordLogin.getText().toString().trim();
        if(TextUtils.isEmpty(strEmail)){
            Toast.makeText(this, "Ingrese un correo valido", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(strPassword) && strPassword.length() < 6){
            Toast.makeText(this, "Ingrese una contraseña valida", Toast.LENGTH_SHORT).show();
            return;
        }

        //Consultando usuario
        mAuth.signInWithEmailAndPassword(strEmail, strPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();

                            Toast.makeText(LoginActivity.this, "Bienvenido!",
                                    Toast.LENGTH_SHORT).show();


                            Intent intent = new Intent(LoginActivity.this, PostActivity.class);
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.

                            //Si el usuario ya existe
                            if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                Toast.makeText(LoginActivity.this, "Ese usuario ya existe!",
                                        Toast.LENGTH_SHORT).show();
                            } else{
                                Toast.makeText(LoginActivity.this, "Credenciales incorrectas!",
                                        Toast.LENGTH_SHORT).show();
                                //TODO progressbar
                            }

                        }

                        // ...
                    }
                });
    }

    @OnClick(R.id.btnSignIn)
    public void entrar(View view) {
        iniciarSesion();

    }

}
