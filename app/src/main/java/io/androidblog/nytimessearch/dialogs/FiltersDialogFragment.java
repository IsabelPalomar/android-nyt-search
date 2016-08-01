package io.androidblog.nytimessearch.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.androidblog.nytimessearch.R;
import io.androidblog.nytimessearch.activities.SearchActivity;

public class FiltersDialogFragment extends DialogFragment {
    View view;
    Resources res;
    @BindView(R.id.etBeginDate)
    EditText etBeginDate;
    @BindView(R.id.spSortOrder)
    Spinner spSortOrder;
    @BindView(R.id.cbTopicArts)
    CheckBox cbTopicArts;
    @BindView(R.id.cbTopicFashion)
    CheckBox cbTopicFashion;
    @BindView(R.id.cbTopicSports)
    CheckBox cbTopicSports;

    String beginDate = "";
    String sortOrder = "";
    String newsDesk = "";


    public FiltersDialogFragment() {
    }

    public static FiltersDialogFragment newInstance(String title) {
        FiltersDialogFragment frag = new FiltersDialogFragment();
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        res = getResources();

        view = getActivity().getLayoutInflater().inflate(R.layout.fragment_filter_options, null);
        ButterKnife.bind(this, view);

        etBeginDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);

                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {

                        mcurrentDate.set(Calendar.YEAR, selectedyear);
                        mcurrentDate.set(Calendar.MONTH, selectedmonth);
                        mcurrentDate.set(Calendar.DAY_OF_MONTH, selectedday);

                        String myFormat = "yyyy/MM/dd";
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                        etBeginDate.setText(sdf.format(mcurrentDate.getTime()));

                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select date");
                mDatePicker.show();
            }
        });

        builder.setView(view)
                .setTitle(res.getString(R.string.app_message_filter_dialog_title))
                .setPositiveButton(res.getString(R.string.app_message_apply),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                beginDate =  (etBeginDate.getText().toString()).replace("/", "");
                                sortOrder = spSortOrder.getSelectedItem().toString();
                                if (cbTopicArts.isChecked()) {
                                    newsDesk += " \"Arts\" ";
                                }
                                if (cbTopicFashion.isChecked()) {
                                    newsDesk += " \"Fashion\" ";
                                }
                                if (cbTopicSports.isChecked()) {
                                    newsDesk += " \"Sports\" ";
                                }


                                if(!beginDate.equals(""))
                                    ((SearchActivity) getActivity()).mBeginDate = beginDate;
                                if(!sortOrder.equals(""))
                                    ((SearchActivity) getActivity()).mSortOrder = sortOrder;
                                if(!newsDesk.equals("")){
                                    ((SearchActivity) getActivity()).mNewsDesk = "news_desk:(" + newsDesk +  ")";
                                    ((SearchActivity) getActivity()).mQuery = "";
                                }

                                ((SearchActivity) getActivity()).fetchArticles("");
                                dismiss();
                            }
                        })
                .setNegativeButton(res.getString(R.string.app_message_cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                getDialog().dismiss();
                            }
                        });

        return builder.create();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

}
