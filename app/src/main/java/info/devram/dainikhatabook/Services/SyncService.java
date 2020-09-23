package info.devram.dainikhatabook.Services;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;

import java.util.List;

public class SyncService {

    public static final int JOB_ID = 1;
    public static final long JOB_24HOUR_INTERVAL = 24 * 60 * 60 * 1000;
    //private static final String TAG = "SyncService";
    private Context context;
    //public static final long JOB_15MINUTE_INTERVAL = 15 * 60 * 1000;
    private JobScheduler jobScheduler;

    public SyncService(Context context) {
        this.context = context;
        if (jobScheduler == null) {
            jobScheduler = (JobScheduler) context.getSystemService(
                    Context.JOB_SCHEDULER_SERVICE);
        }
    }

    public void scheduleJob() {
        ComponentName componentName = new ComponentName(context,BackupJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID,componentName);
        builder.setPersisted(true);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED);
        builder.setPeriodic(JOB_24HOUR_INTERVAL);
        JobInfo jobInfo = builder.build();
        jobScheduler.schedule(jobInfo);

    }

    public void cancelJob() {
        jobScheduler.cancelAll();
        jobScheduler = null;
    }

    public List<JobInfo> getAllJobs() {
        return jobScheduler.getAllPendingJobs();
    }
}
