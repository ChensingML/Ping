package han.Chensing.Ping;

import android.os.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import android.content.*;
import java.io.*;
import android.util.*;
import android.graphics.*;
import android.support.v4.widget.*;
//import android.support.design.widget.*;
//import android.support.v7.app.*;
import android.app.*;

public class MainActivity extends Activity
{
	static boolean isrunning=false;
	
	static Task task=new Task();
	static Handler han=new Handler(){
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			if(msg.what==0x1001){
				change();
			}
		}
	};
	static Button btn;
	static Button btn2;
	static String cmd;
	static ListView lv;
	static ArrayList<String> al=new ArrayList<>();
	static CheckBox rtodi;
	static Context mainc;
	boolean menuopend=false;
	Runtime rt=Runtime.getRuntime();
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		try{
        	//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
			//getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
			android.app.ActionBar ab=MainActivity.this.getActionBar();
			ab.setCustomView(R.layout.action);
			ab.setDisplayShowCustomEnabled(true);
			ab.setDisplayHomeAsUpEnabled(false);
			ab.show();
        	setContentView(R.layout.main);
			han=new Handler();
			mainc=MainActivity.this;
			lv=(ListView)findViewById(R.id.list);
			btn=(Button)findViewById(R.id.create);
			btn2=(Button)findViewById(R.id.stopt);
			rtodi=(CheckBox)findViewById(R.id.rowtodi);
			final DrawerLayout dl=(DrawerLayout)findViewById(R.id.Drawerlayout);
			final ImageButton imab=(ImageButton)findViewById(R.id.set);
			CheckBox clearc=(CheckBox)findViewById(R.id.clearatstart);
			clearc.setChecked(true);
			rtodi.setChecked(true);
			imab.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v){
					if(!menuopend)
						dl.openDrawer(findViewById(R.id.mainFrameLayout));
					else
						dl.closeDrawer(findViewById(R.id.mainFrameLayout));
				}
			});
			dl.setDrawerListener(new DrawerLayout.DrawerListener(){
				@Override
				public void onDrawerSlide(View drawerView, float slideOffset) {
				}
				@Override
				public void onDrawerOpened(View drawerView) {
					imab.setImageResource(R.drawable.ic_close_black_48dp);
					menuopend=true;
				}

				@Override
				public void onDrawerClosed(View drawerView) {
					imab.setImageResource(R.drawable.ic_menu_black_48dp);
					menuopend=false;
				}
				@Override
				public void onDrawerStateChanged(int newState) {
				}
			});
			change();
		}catch(Exception e){
			e.printStackTrace();
			Toast.makeText(MainActivity.this, e.getCause().getMessage(),Toast.LENGTH_LONG).show();
		}
    }
	
	public static void add(final String str){
		han.post(new Runnable(){
			@Override
			public void run(){
				al.add(str);
				ArrayAdapter<String> aa=new ArrayAdapter<String>(mainc,R.layout.list,al);
				lv.setAdapter(aa);
				if(rtodi.isChecked()){
					lv.setSelection(lv.getBottom());
				}
			}
		});
	}
	
	public void clear(){
		al.clear();
		lv.setAdapter(new ArrayAdapter<String>(MainActivity.this,R.layout.list,al));
	}
	
	public void create(View v){
		try{
			View vi=LayoutInflater.from(MainActivity.this).inflate(R.layout.a1,null);
			AlertDialog.Builder ab1=new AlertDialog.Builder(MainActivity.this);
			final AlertDialog a1;
			final RadioButton stard=(RadioButton)vi.findViewById(R.id.a1stand);
			final ProgressBar pb=(ProgressBar)vi.findViewById(R.id.a1pb);
			final Button start1=(Button)vi.findViewById(R.id.a1start);
			ab1.setTitle("创建任务");
			ab1.setView(vi);
			a1=ab1.show();
			stard.setChecked(true);
			start1.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v){
					if(stard.isChecked()){
						try{
						View vi2=LayoutInflater.from(MainActivity.this).inflate(R.layout.a2,null);
						start1.setVisibility(View.GONE);
						pb.setVisibility(View.VISIBLE);
						a1.dismiss();
						AlertDialog.Builder ab2=new AlertDialog.Builder(MainActivity.this);
						final AlertDialog a2;
						final EditText
							ipe=(EditText)vi2.findViewById(R.id.a2ipe),
							sizee=(EditText)vi2.findViewById(R.id.a2sizee),
							timese=(EditText)vi2.findViewById(R.id.a2timese);
						final CheckBox susc=(CheckBox)vi2.findViewById(R.id.a2susc);
						final Button start2=(Button)vi2.findViewById(R.id.a2start);
						ab2.setTitle("向导");
						ab2.setView(vi2);
						a2=ab2.show();
						start2.setOnClickListener(new View.OnClickListener(){
							@Override
							public void onClick(View v2){
								StringBuilder sb=new StringBuilder("/system/bin/ping ");
								if(!susc.isChecked()){
									sb.append("-c ");
									sb.append(timese.getText().toString());
									sb.append(" ");
								}
								sb.append("-s ");
								sb.append(sizee.getText().toString());
								sb.append(" ");
								sb.append(ipe.getText().toString());
								cmd=sb.toString();
								start2run();
								change();
								a2.dismiss();
							}
						});
						susc.setOnClickListener(new View.OnClickListener(){
							@Override
							public void onClick(View v){
								if(susc.isChecked()){
									timese.setEnabled(false);
								}else{
									timese.setEnabled(true);
								}
							}
						});
						}catch(Exception e){
							e.printStackTrace();
						}
					}else{
						View vi3=LayoutInflater.from(MainActivity.this).inflate(R.layout.a3,null);
						start1.setVisibility(View.GONE);
						pb.setVisibility(View.VISIBLE);
						a1.dismiss();
						AlertDialog.Builder ab3=new AlertDialog.Builder(MainActivity.this);
						final AlertDialog a3;
						ab3.setView(vi3);
						ab3.setTitle("命令");
						a3=ab3.show();
						Button start3=(Button)vi3.findViewById(R.id.a3start);
						final EditText et3=(EditText)vi3.findViewById(R.id.a3cmde);
						start3.setOnClickListener(new View.OnClickListener(){
							@Override
							public void onClick(View v){
								cmd=et3.getText().toString();
								start2run();
								change();
								a3.dismiss();
							}
						});
					}
				}
			});
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void stoptask(View v){
		task.close();
		change();
	}
	
	public void clean(View v){
		clear();
	}
	
	public void about(View v){
		AlertDialog.Builder ab=new AlertDialog.Builder(this);
		ab.setTitle("关于");
		ab.setView(R.layout.about);
		ab.show();
	}
	
	
	private void start2run(){
		CheckBox cl=(CheckBox)findViewById(R.id.clearatstart);
		if(cl.isChecked()){
			clear();
		}
		change();
		try{
			task.exec(cmd);
			Geter get=new Geter();
			get.run(task.getPro().getInputStream(),task.getPro().getErrorStream(),task.getPro());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void change(){
		//android:j="#C6C6C6"-->
		if(task.isopen()){
			btn.setEnabled(false);
			btn2.setTextColor(Color.RED);
			btn2.setEnabled(true);
		}else{
			btn.setEnabled(true);
			btn2.setTextColor(Color.GRAY);
			btn2.setEnabled(false);
		}
	}
}

class Task{
	private java.lang.Process pro;
	private boolean isOpen=false;
	
	public Task(){
	}
	
	public java.lang.Process exec(String exe) throws IOException{
		isOpen=true;
		if(isOpen)
			return pro=Runtime.getRuntime().exec(exe);
		else
			return null;
	}
	
	public void close(){
		pro.destroy();
		isOpen=false;
	}
	
	public java.lang.Process getPro(){
		return pro;
	}
	
	public boolean isopen(){
		return isOpen;
	}
}
