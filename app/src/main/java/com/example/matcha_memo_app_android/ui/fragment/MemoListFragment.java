package com.example.matcha_memo_app_android.ui.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.matcha_memo_app_android.DBHandler;
import com.example.matcha_memo_app_android.DTO.Memo;
import com.example.matcha_memo_app_android.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MemoListFragment extends Fragment {

    private ViewGroup mRootView;

    private RecyclerView mRecyclerView;
    private DBHandler dbHandler;
    private Context mContext;

    SimpleDateFormat sdf = new SimpleDateFormat( "yyyy/MM/dd HH:mm" );

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mRootView = (ViewGroup) inflater.inflate(R.layout.fragment_memo_list, container, false);
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // 画面要素を取得
        mRecyclerView = mRootView.findViewById(R.id.listView);
        LinearLayout addMemoBt = mRootView.findViewById(R.id.addMemoBt);

        mContext = mRootView.getContext();
        dbHandler = new DBHandler(mContext);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        addMemoBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mRecyclerView.getContext());
                builder.setTitle("メモをする");
                View view = getLayoutInflater().inflate(R.layout.dialog_memo_add, null);
                final EditText memoName = view.findViewById(R.id.evMemo);
                builder.setView(view);

                builder.setPositiveButton("追加", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (memoName.getText().toString().length() > 0) {
                            Memo memo = new Memo();
                            memo.setName(memoName.getText().toString());
                            dbHandler.addMemo(memo);
                            refreshList();
                        }
                    }
                });
                builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
            }
        });

    }

    @Override
    public void onResume() {
        refreshList();
        super.onResume();
    }

    public void refreshList() {
        mRecyclerView.setAdapter(new MemoAdapter(mContext, dbHandler.getMemos(), dbHandler.getCreateTimes()));
    }

    public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.ViewHolder> {
        ArrayList<Memo> memoList;
        ArrayList<Memo> dateList;
        Context context;

        public MemoAdapter(Context context, ArrayList<Memo> list, ArrayList<Memo> dateList) {
            this.memoList = list;
            this.context = context;
            this.dateList = dateList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.fragment_memo_row, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
            holder.tvMemo.setText(memoList.get(position).getName());
            holder.tvDate.setText(sdf.format(dateList.get(position).getCreatedAt()));

            holder.tvMemo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("削除しますか？");
                    builder.setPositiveButton("削除", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            new DBHandler(context).deleteToDo(memoList.get(position).getId());
                            refreshList();
                        }
                    });
                    builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return memoList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvMemo;
            TextView tvDate;

            ViewHolder(View v) {
                super(v);
                tvMemo = v.findViewById(R.id.tvMemo);
                tvDate = v.findViewById(R.id.tvDate);
            }
        }
    }
}