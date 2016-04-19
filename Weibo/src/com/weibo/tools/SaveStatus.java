package com.weibo.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import android.os.AsyncTask;
import android.os.Environment;

public class SaveStatus extends AsyncTask<String, Void, String>{

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		String data = params[0];
		String filename = params[1];
		File filedire = new File(MyApplication.getContext().getFilesDir().getAbsolutePath()+MyApplication.PATH_OF_SAVESTATUS);
		if(!filedire.exists()){
			filedire.mkdir();
		}
		try {
			File file = new File(filedire.getPath()+"/"+filename+".txt");
			System.out.println(file.getPath());
			if(file.exists()){
				file.delete();
				file.createNewFile();
			}
			FileWriter fileWriter = new FileWriter(file, false);
			fileWriter.write(data);
			fileWriter.flush();
			fileWriter.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

}
