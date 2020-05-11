package com.terryyessfung.whatsins.Fragments;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.terryyessfung.whatsins.Adapters.SearchUserListAdapter;
import com.terryyessfung.whatsins.DataManager;
import com.terryyessfung.whatsins.Model.User;
import com.terryyessfung.whatsins.Model.UserList;
import com.terryyessfung.whatsins.R;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private SearchUserListAdapter mSearchUserListAdapter;
    private List<User> mUsers;

    EditText search_text;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search,container,false);

        mRecyclerView = view.findViewById(R.id.search_rcyc);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        search_text = view.findViewById(R.id.search_EditBar);

        mUsers = new ArrayList<>();
        mSearchUserListAdapter = new SearchUserListAdapter(getContext(),mUsers);
        mRecyclerView.setAdapter(mSearchUserListAdapter);

        //readUsers();
        search_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchUsers(s.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    private void searchUsers(String text){
        mUsers.clear();
        mSearchUserListAdapter.notifyDataSetChanged();
        Call<UserList> call = DataManager.getInstance().getAPIService().getUsersByKeyWords(text);
        call.enqueue(new Callback<UserList>() {
            @Override
            public void onResponse(Call<UserList> call, Response<UserList> response) {
                if (response.isSuccessful()){
                    UserList userList = response.body();
                    for(User user:userList.getUsers()){
                        mUsers.add(user);
                    }
                    mSearchUserListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<UserList> call, Throwable t) {

            }
        });
    }


}
