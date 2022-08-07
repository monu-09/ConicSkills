package com.conicskill.app.data.rest;

import com.conicskill.app.data.model.candidateLogin.BaseResponse;
import com.conicskill.app.data.model.candidateLogin.CandidateLogOut;
import com.conicskill.app.data.model.candidateLogin.CandidateLoginRequest;
import com.conicskill.app.data.model.candidateLogin.CandidateLoginResponse;
import com.conicskill.app.data.model.candidateLogin.ForgotPasswordRequest;
import com.conicskill.app.data.model.candidateLogin.ForgotPasswordResponse;
import com.conicskill.app.data.model.candidateLogin.HelpResponse;
import com.conicskill.app.data.model.candidateRegister.RegisterRequest;
import com.conicskill.app.data.model.candidateRegister.RegisterResponse;
import com.conicskill.app.data.model.carousel.HomeCarouselRequest;
import com.conicskill.app.data.model.carousel.HomeCarouselResponse;
import com.conicskill.app.data.model.chat.ChatRequest;
import com.conicskill.app.data.model.chat.GetChatResponse;
import com.conicskill.app.data.model.contact.ContactRequest;
import com.conicskill.app.data.model.contact.ContactResponse;
import com.conicskill.app.data.model.currentAffairDetails.CADetailRequest;
import com.conicskill.app.data.model.currentAffairDetails.CADetailResponse;
import com.conicskill.app.data.model.currentAffairs.CurrentAffairsRequest;
import com.conicskill.app.data.model.currentAffairs.CurrentResponse;
import com.conicskill.app.data.model.exam.ExamResponse;
import com.conicskill.app.data.model.exam.ModuleRequest;
import com.conicskill.app.data.model.examlisting.ExamListRequest;
import com.conicskill.app.data.model.examlisting.ExamListResponse;
import com.conicskill.app.data.model.help.HelpRequest;
import com.conicskill.app.data.model.home.HomeLayoutRequest;
import com.conicskill.app.data.model.home.HomeLayoutResponse;
import com.conicskill.app.data.model.jobNotification.JobDetailRequest;
import com.conicskill.app.data.model.jobNotification.JobDetailResponse;
import com.conicskill.app.data.model.jobNotification.JobNotificationResponse;
import com.conicskill.app.data.model.jobNotification.JobRequest;
import com.conicskill.app.data.model.login.LoginRequest;
import com.conicskill.app.data.model.login.LoginResponse;
import com.conicskill.app.data.model.news.Feed;
import com.conicskill.app.data.model.notes.NotesListRequest;
import com.conicskill.app.data.model.notes.NotesListResponse;
import com.conicskill.app.data.model.notes.NotesResponse;
import com.conicskill.app.data.model.payments.PaymentRequest;
import com.conicskill.app.data.model.payments.PaymentResponse;
import com.conicskill.app.data.model.playlist.PlaylistResponse;
import com.conicskill.app.data.model.playlist.VideoCourseListingRequest;
import com.conicskill.app.data.model.saveTest.SaveTestRequest;
import com.conicskill.app.data.model.testAnalysis.TestAnalysisRequest;
import com.conicskill.app.data.model.testAnalysis.TestAnalysisResponse;
import com.conicskill.app.data.model.testListing.TestListResponse;
import com.conicskill.app.data.model.update.CheckUpdateResponse;
import com.conicskill.app.data.model.update.UpdateRequest;
import com.conicskill.app.data.model.videoCouses.UpcomingCourseResponse;
import com.conicskill.app.data.model.videoCouses.VideoCoursesResponse;
import com.conicskill.app.data.model.videoCouses.VideoListResponse;
import com.conicskill.app.data.model.wordDay.WordRequest;
import com.conicskill.app.data.model.wordDay.WordResponse;
import com.conicskill.app.data.model.youtubeVideo.YoutubeChannelRequest;
import com.conicskill.app.data.model.youtubeVideo.YoutubeChannelResponse;
import com.google.gson.Gson;

import javax.inject.Inject;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

public class RepoRepository {

    private final RepoService repoService;
    @Inject
    public RepoRepository(RepoService repoService) {
        this.repoService = repoService;
    }

    public Single<Response<ExamResponse>> fetchModuleQuestions(ModuleRequest moduleRequest) {
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("body",new Gson().toJson(moduleRequest))
                .build();
        return repoService.getModuleQuestions(body);
    }

    public Single<Response<LoginResponse>> testLogin(LoginRequest moduleRequest) {
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("body",new Gson().toJson(moduleRequest))
                .build();
        return repoService.doTestLogin(body);
    }

    public Single<Response<ExamListResponse>> getTestSeries(ExamListRequest moduleRequest) {
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("body",new Gson().toJson(moduleRequest))
                .build();
        return repoService.getTestSeries(body);
    }

    public Single<Response<TestListResponse>> getTestList(ExamListRequest moduleRequest) {
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("body",new Gson().toJson(moduleRequest))
                .build();
        return repoService.getTestList(body);
    }

    public Single<Response<CandidateLoginResponse>> doCandidateLogin(CandidateLoginRequest candidateLoginRequest) {
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("body",new Gson().toJson(candidateLoginRequest))
                .build();
        return repoService.candidateLogin(body);
    }

    public Single<Response<BaseResponse>> doCandidateLogout(CandidateLogOut candidateLoginRequest) {
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("body",new Gson().toJson(candidateLoginRequest))
                .build();
        return repoService.candidateLogOut(body);
    }

    public Single<Response<CurrentResponse>> fetchCurrentAffairs(CurrentAffairsRequest currentAffairsRequest) {
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("body", new Gson().toJson(currentAffairsRequest))
                .build();
        return repoService.getCurrentAffairs(body);
    }

    public Single<Response<WordResponse>> fetchWordOfDay(WordRequest wordRequest) {
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("body", new Gson().toJson(wordRequest))
                .build();
        return repoService.getWordOfDay(body);
    }

    public Single<Response<HomeCarouselResponse>> fetchHomeCarousel(HomeCarouselRequest homeCarouselRequest) {
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("body", new Gson().toJson(homeCarouselRequest))
                .build();
        return repoService.getHomeCarousel(body);
    }

    public Single<Response<RegisterResponse>> callRegisterCandidate(RegisterRequest registerRequest) {
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("body", new Gson().toJson(registerRequest))
                .build();
        return repoService.getRegisterResponse(body);
    }

    public Single<Response<VideoCoursesResponse>> callGetVideoCourses(CurrentAffairsRequest currentAffairsRequest) {
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("body", new Gson().toJson(currentAffairsRequest))
                .build();
        return repoService.getVideoCoursesResponse(body);
    }

    public Single<Response<VideoListResponse>> callGetVideoListForCourse(VideoCourseListingRequest currentAffairsRequest) {
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("body", new Gson().toJson(currentAffairsRequest))
                .build();
        return repoService.getVideoListingResponse(body);
    }

    public Single<Response<PaymentResponse>> callPurchaseInformation(PaymentRequest paymentRequest) {
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("body", new Gson().toJson(paymentRequest))
                .build();
        return repoService.sendPurchaseInformation(body);
    }

    public  Single<Response<PlaylistResponse>> callGetCourseCategories(CurrentAffairsRequest paymentRequest)
    {
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("body", new Gson().toJson(paymentRequest))
                .build();
        return repoService.getCourseCategories(body);
    }
    public Single<Response<UpcomingCourseResponse>> callGetUpcomingClasses(CurrentAffairsRequest paymentRequest) {
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("body", new Gson().toJson(paymentRequest))
                .build();
        return repoService.getUpcomingClasses(body);
    }

    public Single<Response<GetChatResponse>> getChatMessage(ChatRequest paymentRequest) {
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("body", new Gson().toJson(paymentRequest))
                .build();
        return repoService.pushChatMessages(body);
    }

    public Single<Response<GetChatResponse>> sendChatMessage(ChatRequest paymentRequest) {
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("body", new Gson().toJson(paymentRequest))
                .build();
        return repoService.postChatMessage(body);
    }

    public Single<Response<CADetailResponse>> fetchCurrentAffairDetails(CADetailRequest caDetailRequest){
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("body",new Gson().toJson(caDetailRequest)).build();
        return repoService.getCurrentAffairDetails(body);
    }

    public Single<Response<Feed>> getNewsFeed(String URL) {
//        String NEWS_URL = "https://www.hindustantimes.com/rss/india/rssfeed.xml";
        return repoService.getFeed(URL);
    }

    public Single<Response<YoutubeChannelResponse>> getYoutubeVideo(YoutubeChannelRequest youtubeChannelRequest) {
        /*String URL = "";
        if(nextPageId.isEmpty()) {
            URL = "https://www.googleapis.com/youtube/v3/search?key=AIzaSyBeGbKp-SBB3zpz5PE7kvcNA3W5pgj5Opo&channelId=UCMXchPbl1OXib77N0tJLAEw&part=snippet,id&order=date&maxResults=20";
        } else {
            URL = "https://www.googleapis.com/youtube/v3/search?key=AIzaSyBeGbKp-SBB3zpz5PE7kvcNA3W5pgj5Opo&channelId=UCMXchPbl1OXib77N0tJLAEw&part=snippet,id&order=date&maxResults=20&pageToken=" + nextPageId;
        }*/

        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("body",new Gson().toJson(youtubeChannelRequest)).build();
        return repoService.getYouTubeVideos(body);
    }

    public Single<Response<SaveTestRequest>> callSaveTestObject(SaveTestRequest saveTestRequest) {
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("body", new Gson().toJson(saveTestRequest))
                .build();
        return repoService.saveTestObject(body);
    }

    public Single<Response<TestAnalysisResponse>> callTestAnalysisAPI(TestAnalysisRequest testAnalysisRequest) {
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("body", new Gson().toJson(testAnalysisRequest))
                .build();
        return repoService.getTestAnalysis(body);
    }

    public  Single<Response<NotesResponse>> callgetNotes(VideoCourseListingRequest videoCourseListingRequest)
    {
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("body",new Gson().toJson(videoCourseListingRequest))
                .build();
        return  repoService.getCoursePdf(body);
    }

    public  Single<Response<JobNotificationResponse>> callGetJobNotificationListing(JobRequest jobRequest) {
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("body",new Gson().toJson(jobRequest))
                .build();
        return  repoService.getJobNotificationListing(body);
    }

    public  Single<Response<JobDetailResponse>> callGetJobDetails(JobDetailRequest jobRequest) {
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("body",new Gson().toJson(jobRequest))
                .build();
        return  repoService.getJobNotificationDetails(body);
    }

    public  Single<Response<ForgotPasswordResponse>> callForgotPasswordApi(ForgotPasswordRequest forgotPasswordRequest) {
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("body",new Gson().toJson(forgotPasswordRequest))
                .build();
        return  repoService.getForgotPassword(body);
    }

    public  Single<Response<CheckUpdateResponse>> callCheckUpdate(UpdateRequest forgotPasswordRequest) {
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("body",new Gson().toJson(forgotPasswordRequest))
                .build();
        return  repoService.getAppVersionLatest(body);
    }

    public  Single<Response<HomeLayoutResponse>> callGetHomeCoursesApi(HomeLayoutRequest homeLayoutRequest) {
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("body",new Gson().toJson(homeLayoutRequest))
                .build();
        return  repoService.getHomeCourses(body);
    }

    public  Single<Response<NotesListResponse>> callGetNotesList(NotesListRequest homeLayoutRequest) {
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("body",new Gson().toJson(homeLayoutRequest))
                .build();
        return  repoService.getNotesList(body);
    }

    public  Single<Response<HelpResponse>> callHelpRequest(HelpRequest helpRequest) {
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("body",new Gson().toJson(helpRequest))
                .build();
        return  repoService.sendEditEnquiry(body);
    }
    public Single<Response<ContactResponse>> callContactRequest(ContactRequest contactRequest) {
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("body",new Gson().toJson(contactRequest))
                .build();
        return  repoService.getCompanyDetails(body);
    }
}
