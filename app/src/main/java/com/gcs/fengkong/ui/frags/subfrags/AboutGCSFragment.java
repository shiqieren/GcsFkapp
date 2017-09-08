package com.gcs.fengkong.ui.frags.subfrags;

import android.view.View;
import android.widget.TextView;

import com.gcs.fengkong.R;
import com.gcs.fengkong.ui.frags.BaseFragment;
import com.gcs.fengkong.ui.ShowUIHelper;
import com.gcs.fengkong.utils.TDevice;


public class AboutGCSFragment extends BaseFragment {

    private TextView mTvVersionName;

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
        return R.layout.fragment_about;
    }


    @Override
    public void initView(View view) {
        mTvVersionName =  view.findViewById(R.id.tv_version_name);
        view.findViewById(R.id.tv_oscsite).setOnClickListener(this);
        view.findViewById(R.id.tv_knowmore).setOnClickListener(this);
    }

    @Override
    public void initData() {
        mTvVersionName.setText(TDevice.getVersionName());
    }

    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.tv_oscsite:
                ShowUIHelper.openInternalBrowser(getActivity(), "https://www.oschina.net");
                break;
            case R.id.tv_knowmore:
                ShowUIHelper.openInternalBrowser(getActivity(),
                        "https://www.oschina.net/home/aboutosc");
                break;
            default:
                break;
        }
    }
}
