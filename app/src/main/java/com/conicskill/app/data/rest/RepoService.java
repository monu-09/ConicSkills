package com.conicskill.app.data.rest;

import com.conicskill.app.data.model.candidateLogin.BaseResponse;
import com.conicskill.app.data.model.candidateLogin.CandidateLoginResponse;
import com.conicskill.app.data.model.candidateLogin.ForgotPasswordResponse;
import com.conicskill.app.data.model.candidateLogin.HelpResponse;
import com.conicskill.app.data.model.candidateRegister.RegisterResponse;
import com.conicskill.app.data.model.carousel.HomeCarouselResponse;
import com.conicskill.app.data.model.chat.GetChatResponse;
import com.conicskill.app.data.model.contact.ContactRequest;
import com.conicskill.app.data.model.contact.ContactResponse;
import com.conicskill.app.data.model.currentAffairDetails.CADetailResponse;
import com.conicskill.app.data.model.currentAffairs.CurrentResponse;
import com.conicskill.app.data.model.exam.ExamResponse;
import com.conicskill.app.data.model.examlisting.ExamListResponse;
import com.conicskill.app.data.model.home.HomeLayoutResponse;
import com.conicskill.app.data.model.jobNotification.JobDetailResponse;
import com.conicskill.app.data.model.jobNotification.JobNotificationResponse;
import com.conicskill.app.data.model.login.LoginResponse;
import com.conicskill.app.data.model.news.Feed;
import com.conicskill.app.data.model.notes.NotesListResponse;
import com.conicskill.app.data.model.notes.NotesResponse;
import com.conicskill.app.data.model.payments.PaymentResponse;
import com.conicskill.app.data.model.playlist.PlaylistResponse;
import com.conicskill.app.data.model.saveTest.SaveTestRequest;
import com.conicskill.app.data.model.testAnalysis.TestAnalysisResponse;
import com.conicskill.app.data.model.testListing.TestListResponse;
import com.conicskill.app.data.model.update.CheckUpdateResponse;
import com.conicskill.app.data.model.videoCouses.UpcomingCourseResponse;
import com.conicskill.app.data.model.videoCouses.VideoCoursesResponse;
import com.conicskill.app.data.model.videoCouses.VideoListResponse;
import com.conicskill.app.data.model.wordDay.WordResponse;
import com.conicskill.app.data.model.youtubeVideo.YoutubeChannelResponse;

import io.reactivex.Single;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface RepoService {

    @POST("test/test/getModuleQuestions") @Json
    Single<Response<ExamResponse>> getModuleQuestions(@Body RequestBody moduleRequest);

    @POST("test/test/testLogin") @Json
    Single<Response<LoginResponse>> doTestLogin(@Body RequestBody loginRequest);

    @POST("candidate/candidate/getCandidateTestSeries") @Json
    Single<Response<ExamListResponse>> getTestSeries(@Body RequestBody loginRequest);

    @POST("candidate/candidate/getCandidateTestList") @Json
    Single<Response<TestListResponse>> getTestList(@Body RequestBody loginRequest);

    @POST("candidate/candidate/candidateLogin") @Json
    Single<Response<CandidateLoginResponse>> candidateLogin(@Body RequestBody candidateRequest);

    @POST("candidate/candidate/logout") @Json
    Single<Response<BaseResponse>> candidateLogOut(@Body RequestBody candidateRequest);

    @POST("candidate/common/getCurrentAffairsList") @Json
    Single<Response<CurrentResponse>> getCurrentAffairs(@Body RequestBody candidateRequest);

    @POST("candidate/common/getWordOfTheDay") @Json
    Single<Response<WordResponse>> getWordOfDay(@Body RequestBody candidateRequest);

    @POST("candidate/common/getHomeCarousel") @Json
    Single<Response<HomeCarouselResponse>> getHomeCarousel(@Body RequestBody requestBody);

    @POST("candidate/candidate/candidateSignUp") @Json
    Single<Response<RegisterResponse>> getRegisterResponse(@Body RequestBody requestBody);

    @POST("candidate/candidate/getVideoCourseList") @Json
    Single<Response<VideoCoursesResponse>> getVideoCoursesResponse(@Body RequestBody requestBody);

    @POST("candidate/candidate/getCourseVideos") @Json
    Single<Response<VideoListResponse>> getVideoListingResponse(@Body RequestBody requestBody);

    @POST("transaction/transaction/addEditTransaction") @Json
    Single<Response<PaymentResponse>> sendPurchaseInformation(@Body RequestBody requestBody);

    @POST("candidate/candidate/getLiveclass") @Json
    Single<Response<UpcomingCourseResponse>> getUpcomingClasses(@Body RequestBody requestBody);

    @POST("notification/Chat/getChatMessages") @Json
    Single<Response<GetChatResponse>> pushChatMessages(@Body RequestBody requestBody);

    @POST("notification/Chat/sendChatMessage") @Json
    Single<Response<GetChatResponse>> postChatMessage(@Body RequestBody requestBody);

    @POST("candidate/common/getCurrentAffairs") @Json
    Single<Response<CADetailResponse>> getCurrentAffairDetails(@Body RequestBody candidateRequest);

	@POST("test/test/saveTestObject") @Json
    Single<Response<SaveTestRequest>> saveTestObject(@Body RequestBody requestBody);

    @POST("test/test/getTestAnalysis")
    Single<Response<TestAnalysisResponse>> getTestAnalysis(@Body RequestBody requestBody);


    @GET @Xml
    Single<Response<Feed>> getFeed(@Url String NEWS_URL);

    @POST("candidate/company/getYouTubeChannelVideos") @Json
    Single<Response<YoutubeChannelResponse>> getYouTubeVideos(@Body RequestBody requestBody);

    @POST("course/course/getCourseCategories")
    Single<Response<PlaylistResponse>> getCourseCategories(@Body RequestBody requestBody);

    @POST("course/course/getCoursePdf")
    Single<Response<NotesResponse>> getCoursePdf(@Body RequestBody requestBody);

    @POST("misc/jobNotification/getLatestJobsNotification")
    Single<Response<JobNotificationResponse>> getJobNotificationListing(@Body RequestBody requestBody);

    @POST("misc/jobNotification/getJobNotificationDetails")
    Single<Response<JobDetailResponse>> getJobNotificationDetails(@Body RequestBody requestBody);

    @POST("candidate/candidate/forgotPassword")
    Single<Response<ForgotPasswordResponse>> getForgotPassword(@Body RequestBody requestBody);

    @POST("company/AppVersion/getLatest")
    Single<Response<CheckUpdateResponse>> getAppVersionLatest(@Body RequestBody requestBody);

    @POST("candidate/homepage/getLayout")
    Single<Response<HomeLayoutResponse>> getHomeCourses(@Body RequestBody requestBody);

    @POST("course/Misc/getCourseNotesList")
    Single<Response<NotesListResponse>> getNotesList(@Body RequestBody requestBody);

    @POST("/enquiry/enquiry/addEditEnquiry")
    Single<Response<HelpResponse>> sendEditEnquiry(@Body RequestBody requestBody);

    @POST("/company/company/getCompanyDetails")
    Single<Response<ContactResponse>> getCompanyDetails(@Body RequestBody requestBody);
}
