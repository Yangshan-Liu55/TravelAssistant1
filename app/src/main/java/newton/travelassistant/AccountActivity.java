package newton.travelassistant;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class AccountActivity extends AppCompatActivity {

    private EditText oldEmail, newEmail, password, newPassword;
    private Button btnChangeEmail, btnChangePassword, btnSendResetEmail, btnRemoveUser,
            changeEmail, changePassword, sendEmail, signOut;
    private TextView account_user_email;
    private ProgressBar progressBar;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseAuth myAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        //Get Firebase auth instance
        myAuth = FirebaseAuth.getInstance();//一定先实例化，否则onStart时add authStateListener报错

        //declare current user
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser(); //cannot set final currentUser null
                if (user == null) {//user auth state changed - user is null, launch login activity
                    Intent intent  = new Intent(AccountActivity.this, LoginActivity.class);
                    startActivity(intent);
                }

            }
        };

        //Buttons, link buttons
        btnChangeEmail = (Button) findViewById(R.id.change_email_button);
        btnChangePassword = (Button) findViewById(R.id.change_password_button);
        btnSendResetEmail = (Button) findViewById(R.id.sending_pass_reset_button);
        btnRemoveUser = (Button) findViewById(R.id.remove_user_button);
        changeEmail = (Button) findViewById(R.id.changeEmail);
        changePassword = (Button) findViewById(R.id.changePass);
        sendEmail = (Button) findViewById(R.id.send);
        signOut = (Button) findViewById(R.id.sign_out);


        //EditTextViews
        oldEmail = (EditText) findViewById(R.id.old_email);
        newEmail = (EditText) findViewById(R.id.new_email);
        password = (EditText) findViewById(R.id.password);
        newPassword = (EditText) findViewById(R.id.newPassword);

        //TextView
        account_user_email = (TextView) findViewById(R.id.account_user_email); // always visible
        if (currentUser != null) {
            account_user_email.setText("ACCOUNT: " + currentUser.getEmail().toString());
        }

        //Initialize the view, set the EditTextViews and Buttons not visible
        oldEmail.setVisibility(View.GONE);
        newEmail.setVisibility(View.GONE);
        password.setVisibility(View.GONE);
        newPassword.setVisibility(View.GONE);
        changeEmail.setVisibility(View.GONE);
        changePassword.setVisibility(View.GONE);
        sendEmail.setVisibility(View.GONE);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }

        //When user wants to change password
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldEmail.setVisibility(View.GONE);
                newEmail.setVisibility(View.GONE);
                password.setVisibility(View.GONE);
                newPassword.setVisibility(View.VISIBLE);
                changeEmail.setVisibility(View.GONE);
                changePassword.setVisibility(View.VISIBLE);
                sendEmail.setVisibility(View.GONE);
            }
        });
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                if (currentUser != null) {
                    if (newPassword.getText().toString().trim().length() < 6) {
                        newPassword.setError("Password too short, enter minimum 6 characters");
                        progressBar.setVisibility(View.GONE);
                    }
                    else {
                        // Check if user is recently login. If has logged in for long, change password unsuccessfully
                        currentUser.updatePassword(newPassword.getText().toString().trim())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(AccountActivity.this, "Password updated. Sign in with new password!", Toast.LENGTH_LONG).show();
                                            signOut();
                                            progressBar.setVisibility(View.GONE);
                                        } else {
                                            Toast.makeText(AccountActivity.this, "Failed! Check if recently signed in!", Toast.LENGTH_LONG).show();
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    }
                                });
                    }
                }

            }
        });

        //When user wants to change email adress
        btnChangeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldEmail.setVisibility(View.GONE);
                newEmail.setVisibility(View.VISIBLE);
                password.setVisibility(View.GONE);
                newPassword.setVisibility(View.GONE);
                changeEmail.setVisibility(View.VISIBLE);
                changePassword.setVisibility(View.GONE);
                sendEmail.setVisibility(View.GONE);
            }
        });
        changeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                if (currentUser != null) {
                    if (newEmail.getText().toString().trim().equals("")) {
                        newEmail.setError("Enter new email");
                        progressBar.setVisibility(View.GONE);
                    }
                    else {
                        currentUser.updateEmail(newEmail.getText().toString().trim())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(AccountActivity.this, "Email address updated. Sign in with new email!", Toast.LENGTH_LONG).show();
                                            signOut();
                                            progressBar.setVisibility(View.GONE);
                                        } else {
                                            Toast.makeText(AccountActivity.this, "Failed to update email!", Toast.LENGTH_LONG).show();
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    }
                                });
                    }
                }
            }
        });

        //Send email for resetting password
        btnSendResetEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldEmail.setVisibility(View.VISIBLE);
                oldEmail.setText(currentUser.getEmail().toString());
                newEmail.setVisibility(View.GONE);
                password.setVisibility(View.GONE);
                newPassword.setVisibility(View.GONE);
                changeEmail.setVisibility(View.GONE);
                changePassword.setVisibility(View.GONE);
                sendEmail.setVisibility(View.VISIBLE);
            }
        });
        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                if (oldEmail.getText().toString().trim().equals("")) {
                    oldEmail.setError("Enter your email");
                    progressBar.setVisibility(View.GONE);
                }
                else {
                    myAuth.sendPasswordResetEmail(oldEmail.getText().toString().trim())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(AccountActivity.this, "Reset password email is sent!", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                    } else {
                                        Toast.makeText(AccountActivity.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                }
            }
        });

        //Delete account
        btnRemoveUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AccountActivity.this);
                    builder.setTitle("Delete your account");
                    builder.setMessage("Are you sure to delete?");

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(AccountActivity.this,"Canceled", Toast.LENGTH_SHORT).show();
                        }
                    });

                    builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            progressBar.setVisibility(View.VISIBLE);

                            currentUser.delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                progressBar.setVisibility(View.GONE);
                                                startActivity(new Intent(AccountActivity.this, SignUpActivity.class));
                                                AccountActivity.this.finish();
                                            } else {
                                                Toast.makeText(AccountActivity.this, "Failed to delete your account!", Toast.LENGTH_SHORT).show();
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        }
                                    });
                        }
                    });
                    builder.create().show();
                }
                else {
                    Toast.makeText(AccountActivity.this,"Not logged in yet!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Log out
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

    }

    //sign out method //repeats in several methods above
    public void signOut() {
        myAuth.signOut();
        AccountActivity.this.finish(); //finish the activity for update all data
    }

    @Override
    public void onStart() {
        super.onStart();
        myAuth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authStateListener != null) {
            myAuth.removeAuthStateListener(authStateListener);
        }
    }
}
