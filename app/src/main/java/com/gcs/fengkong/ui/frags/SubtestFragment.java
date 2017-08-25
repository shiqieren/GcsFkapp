package com.gcs.fengkong.ui.frags;

import android.view.View;
import android.widget.TextView;

import com.gcs.fengkong.R;
import com.gcs.fengkong.ui.account.AccountHelper;
import com.gcs.fengkong.ui.account.atys.LoginActivity;


/**
 * Created by Ivan on 15/9/22.
 */
public class SubtestFragment extends BaseFragment implements View.OnClickListener{

    private TextView login;
  /*  @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container,
                false);
        ButterKnife.bind(this, view);
        initView(view);
        initData();
        return view;
    }*/


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    public void initView(View view) {
        login = view.findViewById(R.id.txt_username);
        setListener();
    }
    @Override
    public void initData() {

    }

    private void setListener(){
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.txt_username:
            //判断是否已经登录
                if (!AccountHelper.isLogin()) {
                    LoginActivity.show(getContext());
                    return;
                }
            default:
                break;


        }

    }


}
