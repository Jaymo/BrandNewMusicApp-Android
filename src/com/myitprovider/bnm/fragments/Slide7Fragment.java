package com.myitprovider.bnm.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.myitprovider.bnm.BNMlogin;
import com.myitprovider.bnm.R;

public class Slide7Fragment extends Fragment {
	

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.layout_bnm_slide7,
				container, false);

		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
      
		Intent intent = new Intent(getActivity(), BNMlogin.class);
        startActivity(intent);
        getActivity().finish();

	}


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onDetach() {
		super.onDetach();

	}

	@Override
	public void onPause() {
		super.onPause();

	}

	public void onResume() {
		super.onResume();
	}

}


