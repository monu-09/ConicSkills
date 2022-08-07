package com.conicskill.app.di.module;

import com.conicskill.app.di.util.ViewModelFactory;
import com.conicskill.app.di.util.ViewModelKey;
import com.conicskill.app.ui.PaymentViewModel;
import com.conicskill.app.ui.courseDetail.CourseDetailViewModel;
import com.conicskill.app.ui.currentAffairs.CurrentAffairsViewModel;
import com.conicskill.app.ui.exam.ExamViewModel;
import com.conicskill.app.ui.exam.ResultViewModel;
import com.conicskill.app.ui.help.HelpViewModel;
import com.conicskill.app.ui.home.ForgotPasswordViewModel;
import com.conicskill.app.ui.home.HomeViewModel;
import com.conicskill.app.ui.home.JobNotificationViewModel;
import com.conicskill.app.ui.home.MockTestViewModel;
import com.conicskill.app.ui.home.SupportViewModel;
import com.conicskill.app.ui.login.LoginViewModel;
import com.conicskill.app.ui.news.NewsViewModel;
import com.conicskill.app.ui.pdf.PdfViewModel;
import com.conicskill.app.ui.splash.SplashViewModel;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModelProvider;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel.class)
    abstract AndroidViewModel bindHomeViewModel(HomeViewModel homeViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel.class)
    abstract AndroidViewModel bindLoginViewModel(LoginViewModel loginViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel.class)
    abstract AndroidViewModel bindSplashViewModel(SplashViewModel splashViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ExamViewModel.class)
    abstract AndroidViewModel bindExamViewModel(ExamViewModel examViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MockTestViewModel.class)
    abstract AndroidViewModel bindMockTestViewModel(MockTestViewModel mockTestViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ResultViewModel.class)
    abstract AndroidViewModel bindResultViewModel(ResultViewModel resultViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SupportViewModel.class)
    abstract AndroidViewModel bindSupportViewModel(SupportViewModel resultViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(NewsViewModel.class)
    abstract AndroidViewModel bindNewsViewModel(NewsViewModel resultViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(CurrentAffairsViewModel.class)
    abstract AndroidViewModel bindCurrentAffairsViewModel(CurrentAffairsViewModel resultViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(PaymentViewModel.class)
    abstract AndroidViewModel bindPaymentViewModel(PaymentViewModel resultViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(PdfViewModel.class)
    abstract AndroidViewModel bindPdfViewModel(PdfViewModel pdfViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(CourseDetailViewModel.class)
    abstract AndroidViewModel bindCourseDetailViewModel(CourseDetailViewModel courseDetailViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(JobNotificationViewModel.class)
    abstract AndroidViewModel bindJobNotificationViewModel(JobNotificationViewModel jobNotificationViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ForgotPasswordViewModel.class)
    abstract AndroidViewModel bindForgotPasswordModel(ForgotPasswordViewModel forgotPasswordViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(HelpViewModel.class)
    abstract AndroidViewModel bindHelpViewModel(HelpViewModel resultViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);
}
