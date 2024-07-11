package com.sipl.apkdownloader;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DownloadHelper {
    private static final String TAG = "DownloadHelper";

    private static final int BUFFER_SIZE = 4096;

    public static void downloadFile(String fileUrl, String destinationPath) {
        Log.i(TAG, "downloadFile: destination path : " + destinationPath);
        Log.i(TAG, "downloadFile: in download method <<START>>");
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        Call<ResponseBody> call = apiService.downloadFileWithDynamicUrlSync(fileUrl);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e(TAG, "onResponse: response code : " + response.code() );
                Log.e(TAG, "onResponse: response raw : " + response.raw() );
                if (response.isSuccessful()) {
                    boolean isWrittenToDisk = writeResponseBodyToDisk(response.body(), destinationPath);
                    if (isWrittenToDisk) {
                        Log.i(TAG, "onResponse: file download success fully");
                        // File successfully downloaded
                    } else {
                        Log.e(TAG, "onResponse: download fail" );
                        // Failed to save the file
                    }
                } else {
                    Log.e(TAG, "onResponse: unhandle response" );
                    // Handle unsuccessful response
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Handle failure
                Log.e(TAG, "onFailure: failure case", t);
            }
        });
    }

    private static boolean writeResponseBodyToDisk(ResponseBody body, String destinationPath) {
        try {
            File destinationFile = new File(destinationPath);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[BUFFER_SIZE];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(destinationFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    // Log progress if needed
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }
}
