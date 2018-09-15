package app.mpv.com.mpvapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static app.mpv.com.mpvapp.app.mpv.com.mpvapp.helper.Constant.CREDENCIALES_INCORRECTAS;
import static app.mpv.com.mpvapp.app.mpv.com.mpvapp.helper.Constant.ESE_USUARIO_YA_EXISTE;

public class LoginActivity extends AppCompatActivity {


    //Initialise Views and objects
    @BindView(R.id.dontHaveAccount)
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
    }

    @OnClick(R.id.dontHaveAccount)
    public void sendToRegister(View view) {
        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    public void iniciarSesion() {

        final String strEmail = emailLogin.getText().toString().trim();
        String strPassword = passwordLogin.getText().toString().trim();
        if (TextUtils.isEmpty(strEmail)) {
            Toast.makeText(this, "Ingrese un correo valido", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(strPassword)) {
            Toast.makeText(this, "La Contraseña no puede estar vacia", Toast.LENGTH_SHORT).show();
            return;
        }

        //Initialise User
        mAuth.signInWithEmailAndPassword(strEmail, strPassword)
                .addOnCompleteListener(this, task -> {

                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();

                        Toast.makeText(LoginActivity.this, "Bienvenido " + strEmail + "!",
                                Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(LoginActivity.this,
                                PostActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        //If user exist
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(LoginActivity.this, ESE_USUARIO_YA_EXISTE,
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, CREDENCIALES_INCORRECTAS,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @OnClick(R.id.btnSignIn)
    public void entrar(View view) {
        iniciarSesion();
    }
}