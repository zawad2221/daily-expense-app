package info.devram.dailyexpenses.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import info.devram.dailyexpenses.Adapters.IncomeRecyclerAdapter;
import info.devram.dailyexpenses.R;
import info.devram.dailyexpenses.ViewModel.MainActivityViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IncomePageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IncomePageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private MainActivityViewModel mainActivityViewModel;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public IncomePageFragment(MainActivityViewModel viewModel) {
        // Required empty public constructor
        this.mainActivityViewModel = viewModel;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment IncomePageFragment.
     */
    // TODO: Rename and change types and number of parameters
//    public static IncomePageFragment newInstance(String param1, String param2) {
//        IncomePageFragment fragment = new IncomePageFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_income_page,
                container, false);

        RecyclerView incomeRecyclerView = view.findViewById(R.id.inc_recycler_view);

        IncomeRecyclerAdapter incomeRecyclerAdapter = new IncomeRecyclerAdapter(view.getContext(),
                mainActivityViewModel.getIncomes().getValue());

        incomeRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        incomeRecyclerView.setAdapter(incomeRecyclerAdapter);

        return view;
    }
}