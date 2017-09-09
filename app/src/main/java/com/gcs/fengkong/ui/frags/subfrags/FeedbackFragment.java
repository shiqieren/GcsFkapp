package com.gcs.fengkong.ui.frags.subfrags;

import android.view.View;
import android.widget.TextView;

import com.gcs.fengkong.R;
import com.gcs.fengkong.ui.ShowUIHelper;
import com.gcs.fengkong.ui.atys.SimpleBackActivity;
import com.gcs.fengkong.ui.frags.BaseFragment;
import com.gcs.fengkong.utils.TDevice;


public class FeedbackFragment extends BaseFragment {


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
        return R.layout.fragment_feed_back;
    }


    @Override
    public void initView(View view) {
        ((SimpleBackActivity)getActivity()).setToolBarTitle(R.string.feedback);
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
