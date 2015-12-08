package fpoly.android.quanlysinhvien;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseObject;

public class Login extends ActionBarActivity {
	EditText User;
	EditText Pass;
	CheckBox chk;
	String filename = "myfile";
	String username, password;
	boolean status;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		// Enable Local Datastore.
		Parse.enableLocalDatastore(Login.this);

		Parse.initialize(Login.this, "Ece7u7fhundI7QQmreaYIdYtxnCfYdW7fuMaLg5I", "hoIOn2frToW41aQTLCD1rJveIEBkmgly5LcsROm8");

		User=(EditText)findViewById(R.id.edtUser);
		Pass=(EditText)findViewById(R.id.edtPass);
		chk=(CheckBox)findViewById(R.id.chkStatus);		
		
	}
	
	public void Login(View v){
		username=User.getText().toString();
		password=Pass.getText().toString();
		status = chk.isChecked();
		if(username.equalsIgnoreCase("")){
			Toast.makeText(this, "Tên đăng nhập không để trống", Toast.LENGTH_LONG).show();
		}
		else if(password.equalsIgnoreCase("")){
			Toast.makeText(this, "Mật khẩu không để trống", Toast.LENGTH_LONG).show();
		}
		else if(!username.equals("admin")){
			Toast.makeText(this, "Tên đăng nhập không chính xác", Toast.LENGTH_LONG).show();
		}
		else if(!password.equals("admin")){
			Toast.makeText(this, "Mật khẩu không chính xác", Toast.LENGTH_LONG).show();
		}
		else{
			Intent home = new Intent(this, Home.class);
			home.putExtra("username", username);
			startActivity(home);
			finish();
		}
	}
	protected void onPause(){
		super.onPause();
		saveAccount();
		
	}
	protected void onResume(){
		super.onResume();		
		restoreAccount();
		status = chk.isChecked();
	}
	protected void onStop(){
		super.onStop();		
		saveAccount();
	}
	public void saveAccount(){
		SharedPreferences share = getSharedPreferences(filename, MODE_PRIVATE);
		SharedPreferences.Editor edt = share.edit();
		if(status){
			edt.putString("username", username);
			edt.putString("password", password);
			edt.putBoolean("status", status);
		}
		else{
			edt.clear();
		}
		edt.commit();
	}
	
	public void restoreAccount(){
		SharedPreferences store = getSharedPreferences(filename, MODE_PRIVATE);
		boolean kt=store.getBoolean("status", false);
		if(kt){
			User.setText(store.getString("username", ""));
			Pass.setText(store.getString("password", ""));
		}
		chk.setChecked(kt);
	}
	
	public void Thoat(View v){		
		username=User.getText().toString();
		password=Pass.getText().toString();		
		finish();
				
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
}
