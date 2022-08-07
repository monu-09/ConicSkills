package com.conicskill.app.ui.home;

import android.app.Application;

import com.conicskill.app.R;
import com.conicskill.app.data.model.SupportModel;
import com.conicskill.app.data.model.candidateLogin.HelpResponse;
import com.conicskill.app.data.model.contact.ContactRequest;
import com.conicskill.app.data.model.contact.ContactResponse;
import com.conicskill.app.data.model.help.HelpRequest;
import com.conicskill.app.data.rest.RepoRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class SupportViewModel extends AndroidViewModel {
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private final MutableLiveData<List<SupportModel>> contacts = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loginError = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loginLoading = new MutableLiveData<>();
    MutableLiveData<Response<ContactResponse>> contactResponse = new MutableLiveData<>();
    private RepoRepository repoRepository;
    private CompositeDisposable disposable;

    @Inject
    public SupportViewModel(Application application, RepoRepository repoRepository){
        super(application);
        this.repoRepository = repoRepository;
        this.disposable = new CompositeDisposable();
        setContacts();
    }

    LiveData<List<SupportModel>> getContacts(){
        return contacts;
    }

    private void setContacts(){
        List<SupportModel> supportModelsList = new ArrayList<>();

        SupportModel supportModelAddress = new SupportModel().setHeading(R.string.app_name).setDescription(R.string.cprep_address)
                .setIcon(R.drawable.ic_outline_location_on_24px)
                .setSubDescription(R.string.text_click_here_to_view_map)
                .setId(1);

        SupportModel supportModelPhone = new SupportModel().setHeading(R.string.text_phone_number).setDescription(R.string.cprep_phone)
                .setIcon(R.drawable.ic_phonelink_ring_white_24dp)
                .setSubDescription(R.string.text_click_to_call)
                .setId(2);

        SupportModel supportModelPhone2 = new SupportModel().setHeading(R.string.text_alternate_phone).setDescription(R.string.cprep_phone)
                .setIcon(R.drawable.ic_phonelink_ring_white_24dp)
                 .setSubDescription(R.string.text_click_to_call)
                .setId(4);

        SupportModel supportModelEmail = new SupportModel().setHeading(R.string.text_email).setDescription(R.string.cprep_email)
                .setIcon(R.drawable.ic_outline_email_24px)
                .setSubDescription(R.string.text_click_to_compose)
                .setId(3);

//        SupportModel supportModelWebsite = new SupportModel().setHeading(R.string.text_buy_now).setDescription(R.string.cprep_buy)
//                .setIcon(R.drawable.ic_language_white_24dp)
//                .setSubDescription(R.string.text_click_to_compose)
//                .setId(4);

        supportModelsList.add(supportModelAddress);
        supportModelsList.add(supportModelPhone);
//        supportModelsList.add(supportModelPhone2);
        supportModelsList.add(supportModelEmail);
//        supportModelsList.add(supportModelWebsite);

        contacts.postValue(supportModelsList);
    }
    public void callContactApi(ContactRequest currentAffairsRequest) {
        loginLoading.setValue(true);
        disposable.add(repoRepository.callContactRequest(currentAffairsRequest).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Response<ContactResponse>>() {
                    /**
                     * Notifies the SingleObserver with a single item and that the {@link } has finished sending
                     * push-based notifications.
                     * <p>
                     * The {@link =} will not call this method if it calls {@link #onError}.
                     *
                     * @param responseOfFeedFromServer the item emitted by the Single
                     */
                    @Override
                    public void onSuccess(@NonNull Response<ContactResponse> responseOfFeedFromServer) {
                        loginLoading.setValue(false);
                        loginError.setValue(false);
                        contactResponse.setValue(responseOfFeedFromServer);
                    }

                    /**
                     * Notifies the SingleObserver that the {@link } has experienced an error condition.
                     * <p>
                     * If the {@link } calls this method, it will not thereafter call {@link #onSuccess}.
                     *
                     * @param e the exception encountered by the Single
                     */
                    @Override
                    public void onError(Throwable e) {
                        loginError.setValue(true);
                        loginLoading.setValue(false);
                    }
                }));
    }
}
