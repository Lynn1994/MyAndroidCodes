package com.bali.simpledownload;

import android.os.AsyncTask;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Lynn on 2015/12/5.
 * This class for downloading file from webService
 * and saving in phone memory
 */
public class DownLoadFile {
    private String filePath = "";
    private String fileName ="";
    private boolean deleted;

    public DownLoadFile(String fileURL,String filePath,String fileName) {
        this.filePath = filePath;
        this.fileName = fileName;
        new LoadAndSave().execute(fileURL);
    }

    public void release() {
        File file = new File("file://" + filePath);
        if (file.exists()) {
            deleted = file.delete();
        }
    }

    class LoadAndSave extends AsyncTask<String, Void, Void> {

                @Override
        protected Void doInBackground(String... params) {
            InputStream is = null;
            OutputStream os = null;
            HttpURLConnection connection = null;

            try {
                URL url = new URL(params[0]);
                try {
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5 * 1000);
                    connection.setReadTimeout(5 * 1000);
                    connection.connect();
                    is = connection.getInputStream();
                    File checker = new File(filePath);
                    if (!checker.exists()) {
                        checker.mkdirs();
                    }
                    os = new FileOutputStream(filePath+fileName);
                    byte data[] = new byte[4096];
                    int count;
                    while ((count = is.read(data)) != -1) {
                        os.write(data, 0, count);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                    if (os != null){
                        os.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (connection!= null){
                    connection.disconnect();
                }
            }
            return null;
        }
    }
}
