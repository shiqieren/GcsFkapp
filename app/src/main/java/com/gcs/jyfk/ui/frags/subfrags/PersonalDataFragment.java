package com.gcs.jyfk.ui.frags.subfrags;

import android.view.View;

import com.gcs.jyfk.R;
import com.gcs.jyfk.ui.atys.SimpleBackActivity;
import com.gcs.jyfk.ui.frags.BaseFragment;


public class PersonalDataFragment extends BaseFragment {


  /*  @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        initView(view);
        initData();
        return view;
    }*/

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_personal_data;
    }


    @Override
    public void initView(View view) {
        ((SimpleBackActivity) getActivity()).setToolBarTitle(R.string.personal_data);
    }

    @Override
    public void initData() {

    }

    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {

            default:
                break;
        }
    }
}
