package han.Chensing.Ping;

import android.app.*;
import android.os.*;
import android.content.*;
import android.widget.*;
import android.view.*;

public class Welcome extends Activity
{
	
	volatile boolean isJump=false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		setContentView(R.layout.welcome);
		Button jump=(Button)findViewById(R.id.jump);
		
		jump.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				jump();
			}
		});
		
		Handler han=new Handler();
		han.postDelayed(new Runnable(){
			@Override
			public void run(){
				jump();
			}
		},2000);
	}
	
	private void jump()
	{
		if(!isJump){
			isJump=true;
			Intent in=new Intent(Welcome.this,MainActivity.class);
			startActivity(in);
			Welcome.this.finish();
		}
	}
}
