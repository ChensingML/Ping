package han.Chensing.Ping;
import java.io.*;

public class Geter
{
	Thread get,err,end;
	java.lang.Process pro;
	volatile boolean closed=false;
	
	public void run(final InputStream is,final InputStream errs,final java.lang.Process pro){
		this.pro=pro;
		
		err=new Thread(new Runnable(){
			
			String line;
			
			@Override
			public void run(){
				LineNumberReader br=new LineNumberReader(new InputStreamReader(errs));
				try{
					while(!closed){
						while((line=br.readLine())!=null)
							MainActivity.add("Error:"+line);
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
		
		get=new Thread(new Runnable(){
			
			String line;
			
			@Override
			public void run(){
				LineNumberReader br=new LineNumberReader(new InputStreamReader(is));
				try{
					while(!closed){
						while((line=br.readLine())!=null)
						MainActivity.add(line);
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
		
		end=new Thread(new Runnable(){
			@Override
			public void run(){
				try{
					pro.waitFor();
					close();
					MainActivity.task.close();
					MainActivity.han.post(new Runnable(){
						@Override
						public void run(){
							MainActivity.change();
						}
					});
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
		
		err.start();
		get.start();
		end.start();
	}
	
	public void close(){
		pro.destroy();
		closed=true;
	}
}
