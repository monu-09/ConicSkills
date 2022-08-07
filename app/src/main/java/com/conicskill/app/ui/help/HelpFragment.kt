package com.conicskill.app.ui.help

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModelProvider
import butterknife.BindView
import com.conicskill.app.BuildConfig
import com.conicskill.app.R
import com.conicskill.app.base.BaseFragment
import com.conicskill.app.data.model.help.HelpData
import com.conicskill.app.data.model.help.HelpRequest
import com.conicskill.app.di.util.ViewModelFactory
import com.conicskill.app.util.Constants
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import javax.inject.Inject

class HelpFragment : BaseFragment() {

    @BindView(R.id.buttonRegister)
    lateinit var buttonRegister: AppCompatButton

    @BindView(R.id.textLayoutPhone)
    lateinit var textLayoutPhone: TextInputLayout

    @BindView(R.id.textInputPhone)
    lateinit var textInputPhone: TextInputEditText

    @BindView(R.id.textLayoutFirstname)
    lateinit var textLayoutFirstname: TextInputLayout

    @BindView(R.id.textInputFirstname)
    lateinit var textInputFirstname: TextInputEditText

    @BindView(R.id.textLayoutEmail)
    lateinit var textLayoutEmail: TextInputLayout

    @BindView(R.id.textInputEmail)
    lateinit var textInputEmail: TextInputEditText

    @BindView(R.id.textInputQuery)
    lateinit var textInputQuery: TextInputEditText

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: HelpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun layoutRes(): Int {
        return R.layout.fragment_help
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(HelpViewModel::class.java)
        observeViewModel()
        textInputFirstname.setText(viewModel.tinyDB.getString(Constants.NAME))
        textInputEmail.setText(viewModel.tinyDB.getString(Constants.EMAIL))
        textInputPhone.setText(viewModel.tinyDB.getString(Constants.PHONE_NUMBER))

        val helpData = HelpData(BuildConfig.COMPANY_ID, textInputFirstname.text.toString(),
            textInputPhone.text.toString(),
            textInputQuery.text.toString(),
            0,
            "Android App",
            viewModel.tinyDB.getString(Constants.CANDIDATE_ID),
            textInputEmail.text.toString(),
            "1")

        val helpRequest = HelpRequest(helpData, viewModel.tinyDB.getString(Constants.AUTH_TOKEN))
        buttonRegister.setOnClickListener {
            viewModel.callHelpApi(helpRequest)
        }
    }

    private fun observeViewModel() {
        viewModel.helpResponse.observe(viewLifecycleOwner, {
            Toast.makeText(requireContext(), "Query Submitted", Toast.LENGTH_SHORT).show()
            textInputFirstname.setText("")
            textInputEmail.setText("")
            textInputPhone.setText("")
            textInputQuery.setText("")
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            HelpFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}