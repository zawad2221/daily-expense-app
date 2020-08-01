package info.devram.dainikhatabook.Services;

import android.app.job.JobParameters;
import android.app.job.JobService;
//import android.util.Log;
import java.util.List;

import info.devram.dainikhatabook.Controllers.PostJsonData;
import info.devram.dainikhatabook.Controllers.PostRawData;
import info.devram.dainikhatabook.Models.Expense;
import info.devram.dainikhatabook.ViewModel.MainActivityViewModel;

public class BackupJobService extends JobService implements PostJsonData.OnReponseAvailableListener {

    //private static final String TAG = "BackupJobService";

    private MainActivityViewModel mainActivityViewModel = new MainActivityViewModel(getBaseContext());
    private List<Expense> syncList;
    private JobParameters mParams;


    @Override
    public boolean onStartJob(JobParameters params) {
        mainActivityViewModel.init();
        mParams = params;

        String url = "http://192.168.0.101/expenses";
        PostJsonData postJsonData = new PostJsonData(this,url);

        syncList = mainActivityViewModel.getExpenseSyncList();
        if (syncList != null && syncList.size() > 0) {
            postJsonData.parseJson(syncList);
            return true;
        }else {
            jobFinished(params,false);
            return false;
        }

    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

    @Override
    public void onResponseAvailable(String httpMessage, PostRawData.UploadStatus status) {
        if (httpMessage.equalsIgnoreCase("201") && status == PostRawData.UploadStatus.OK) {
            mainActivityViewModel.updateExpenseSyncListWithDb(syncList);
            jobFinished(mParams,false);
        }
    }
}
