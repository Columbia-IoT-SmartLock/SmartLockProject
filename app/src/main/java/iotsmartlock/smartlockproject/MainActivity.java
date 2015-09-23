package iotsmartlock.smartlockproject;

import android.content.Intent;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.RelativeLayout;
import java.security.Security;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public final static String LOGIN_DATA = "com.iotsmartlock.smartlockproject.MESSAGE";
    public boolean authenticated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        RelativeLayout main_view = (RelativeLayout) findViewById(R.id.main_view);

        Intent loggedInAct = new Intent(this, loggedInActivity.class);
        Intent alertsAct = new Intent(this, alertsActivity.class);
        Intent controlAct = new Intent(this, controlActivity.class);
        Intent newUserAct = new Intent(this, newuserActivity.class);

        switch(item.getItemId()) {

            case R.id.menu_login:
                // Already on login page
                return true;

            case R.id.menu_lockstatus:
                startActivity(controlAct);
                return true;

            case R.id.menu_newuser:
                startActivity(newUserAct);
                return true;

            case R.id.menu_alerts:
                startActivity(alertsAct);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }

    }

    /** Called when the login button has been clicked
     *  Retrieves username and password information (save to a variable)
     *  Compares login information with Parse/Atmel (TBD)
     *  If login information is correct, start logged in activity
     *  Otherwise, display error message  **/
    public void authenticateUser(View view) {

        Intent intent = new Intent(this, loggedInActivity.class);

        EditText username = (EditText) findViewById(R.id.usernameEntry);
        EditText password = (EditText) findViewById(R.id.passwordEntry);
        TextView error = (TextView) findViewById(R.id.loginStatus);
        //TextView hashText = (TextView) findViewById(R.id.hashValue); for debug
        String usrMessage = username.getText().toString();
        String pwdMessage = password.getText().toString();

        intent.putExtra(LOGIN_DATA, usrMessage);
        intent.putExtra(LOGIN_DATA, pwdMessage);

        // Generate a random number for authentication
        Random randNum = new Random();
        String random = new Integer(randNum.nextInt()).toString();

        String hashed = getSHA256(pwdMessage, random);
        //hashText.setText(hashed);  // see hash for debug

        // Check if the user is authenticated
        if (authenticated){
            error.setText("Welcome Back!");
            startActivity(intent);
        } else {
            error.setText("Login Information Incorrect");
        }

    }

    /* Compute SHA-256 hash value for given string */
    public String getSHA256(String password, String randNum) {

        MessageDigest md = null;
        byte[] output;
        String s = randNum.concat(password);

        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }

        md.reset();
        md.update(s.getBytes());
        output = md.digest();

        StringBuffer hexString = new StringBuffer();
        for(int i=0; i<output.length;i++){
            hexString.append(Integer.toHexString(0xFF & output[i]));
        }

        return hexString.toString();
    }

    /* Update Parse with specified information
     */
    public void sendParse(){

    }



}
