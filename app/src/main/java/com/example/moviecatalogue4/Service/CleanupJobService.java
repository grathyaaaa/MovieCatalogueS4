package com.example.moviecatalogue4.Service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

public class CleanupJobService extends JobService {

    private static final String TAG = CleanupJobService.class.getSimpleName();
    private static final String CONTENT_AUTHORITY = "com.example.moviecatalogue4";
    private static final String TABLE_NAME = "favorite";
    public static final Uri CONTENT_URI = new Uri.Builder().scheme("content")
            .authority(CONTENT_AUTHORITY)
            .appendPath(TABLE_NAME)
            .build();

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d(TAG, "Cleanup job started");
        new CleanupTask().execute(jobParameters);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

    private class CleanupTask extends AsyncTask<JobParameters, Void, JobParameters> {

        @Override
        protected JobParameters doInBackground(JobParameters... jobParameters) {
            String where = String.format("%s", "is_complete");
            String[] args = {"1"};

            int count = getContentResolver().delete(CONTENT_URI, where, args);
            Log.d(TAG, "Cleaned up " + count + "completed tasks");
            return jobParameters[0];
        }

        @Override
        protected void onPostExecute(JobParameters jobParameters) {
            jobFinished(jobParameters, false);
        }
    }
}
