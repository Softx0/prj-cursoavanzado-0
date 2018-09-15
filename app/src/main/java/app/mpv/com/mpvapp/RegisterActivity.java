package app.mpv.com.mpvapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

import app.mpv.com.mpvapp.app.mpv.com.mpvapp.helper.Constant;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static app.mpv.com.mpvapp.app.mpv.com.mpvapp.helper.Constant.ESE_USUARIO_YA_EXISTE;
import static app.mpv.com.mpvapp.app.mpv.com.mpvapp.helper.Constant.INGRESE_UNA_CONTRASEÑA_VALIDA;
import static app.mpv.com.mpvapp.app.mpv.com.mpvapp.helper.Constant.INGRESE_UN_CORREO_VALIDO;
import static app.mpv.com.mpvapp.app.mpv.com.mpvapp.helper.Constant.LA_CONTRASEÑA_DEBE_TENER_CARACTERES_ESPECIALES;
import static app.mpv.com.mpvapp.app.mpv.com.mpvapp.helper.Constant.LA_CONTRASEÑA_NO_PUEDE_SER_TAN_CORTA;
import static app.mpv.com.mpvapp.app.mpv.com.mpvapp.helper.Constant.MSG_ERROR;

public class RegisterActivity extends AppCompatActivity {


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

    //Instance from Authentication
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        link_login = findViewById(R.id.link_login);

        link_login.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    public void registroUsuario() {

        String strEmail = email.getText().toString().trim();
        String strPassword = password.getText().toString().trim();
        final String strUser = user.getText().toString().trim();

        if (TextUtils.isEmpty(strEmail)) {
            Toast.makeText(this, INGRESE_UN_CORREO_VALIDO, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(strPassword)) {
            Toast.makeText(this, INGRESE_UNA_CONTRASEÑA_VALIDA, Toast.LENGTH_SHORT).show();
            return;
        }
        if (strPassword.length() < 8) {
            Toast.makeText(this, LA_CONTRASEÑA_NO_PUEDE_SER_TAN_CORTA, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!(strPassword.contains("*") || strPassword.contains("/"))) {
            Toast.makeText(this, LA_CONTRASEÑA_DEBE_TENER_CARACTERES_ESPECIALES, Toast.LENGTH_SHORT).show();
            return;
        }

        //Create new User
        Task<AuthResult> authResultTask = mAuth.createUserWithEmailAndPassword(strEmail, strPassword)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(RegisterActivity.this, "Usuario " + strUser + " Creado!",
                                Toast.LENGTH_SHORT).show();
                    } else {


                        //If the user don't exist
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(RegisterActivity.this, ESE_USUARIO_YA_EXISTE,
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(RegisterActivity.this, MSG_ERROR,
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                            startActivity(intent);
                            finish();

                            //TODO progressbar
                        }

                    }

                    // ...
                });
    }

    //Button for sign up in app
    @OnClick(R.id.btn_sign_up)
    public void signUp(View view) {
        registroUsuario();

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }, Constant.DURACION_REGISTRO);
    }
}