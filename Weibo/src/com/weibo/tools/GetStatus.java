package com.weibo.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import android.os.AsyncTask;
import android.os.Environment;

public class GetStatus extends AsyncTask<String, Void, String>{

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		String filename = params[0];
		String result = "";
		File filedire = new File(MyApplication.getContext().getFilesDir().getAbsolutePath()+MyApplication.PATH_OF_SAVESTATUS);
		if(!filedire.exists()){
			System.out.println(filedire+" not exits");
			return null;
		}
		try {
			File file = new File(filedire.getPath()+"/"+filename+".txt");
			if(file.exists()){
				//System.out.println(file.getPath()+" getdata");
				FileReader fileReader = new FileReader(file);
				BufferedReader bufferedReader = new BufferedReader(fileReader);
				String line = bufferedReader.readLine();
				if(line != null){
					//System.out.println(line);
					result += line;
					line = bufferedReader.readLine();
				}
				return result;
			}
			else{
				return null;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}

}
