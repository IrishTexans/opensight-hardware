package com.makewithmoto.makr.example;

import java.util.ArrayList;
import java.util.TreeSet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ShowToast")
public class DebugFragment extends Fragment {

	private String TAG = "qq";
	private MainActivity c;
	ListView lv;
	View v;
	ArrayList<Info> list = new ArrayList<Info>();
	CustomAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		v = inflater.inflate(R.layout.fragment_debug, container, false);

		return v;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		c = (MainActivity) getActivity();
		adapter = new CustomAdapter(getActivity(), list);

		// UI binding
		bindButton(R.id.e, 0); // 40);
		bindButton(R.id.a, 1); // 45);
		bindButton(R.id.d, 2); // 50);
		bindButton(R.id.g, 3); // 55);
		bindButton(R.id.b, 4); // 59);
		bindButton(R.id.ee, 5); // 64);

		lv = (ListView) v.findViewById(R.id.logWindow);
		lv.setAdapter(adapter);
		lv.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		lv.setStackFromBottom(true);

		final EditText cmdEditText = (EditText) v.findViewById(R.id.pdName);

		Button btn = (Button) v.findViewById(R.id.pdSend);
		btn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String cmd = cmdEditText.getText().toString();
				Log.d(TAG, cmd);

				c.writeSerial(cmd);
				adapter.addSeparatorItem(cmd);

			}
		});

		adapter.addItem("hola 1");
		adapter.addItem("hola 1");
		adapter.addSeparatorItem("qq 2");
		adapter.addItem("hola 1");
	}

	/*
	public void addText(final String text, final boolean left) {
		Log.d(TAG, "adding.....");
		if (getActivity() != null) {
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					Log.d(TAG, "add");
					list.add(new Info(text));

					adapter.notifyDataSetChanged();
					lv.invalidateViews();
					Log.d(TAG, "added");
					// listview.scrollBy(0, 0);
				}
			});
		}

	}
	*/

	public void bindButton(int resource, final int value) {
		Button btn = (Button) v.findViewById(resource);
		btn.setSoundEffectsEnabled(false);

		btn.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {

				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					Log.d("qq", "action_down");

				} else {

				}
				return false;
			}
		});

	}

	private Toast toast = null;

	private void toast(final String msg) {
		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (toast == null) {
					toast = Toast.makeText(getActivity()
							.getApplicationContext(), "", Toast.LENGTH_SHORT);
				}
				toast.setText(TAG + ": " + msg);
				toast.show();
			}
		});
	}

	public void clear() {
		list.clear();
		adapter.notifyDataSetChanged();

	}

	class CustomAdapter extends BaseAdapter {

		private static final int TYPE_ITEM = 0;
		private static final int TYPE_SEPARATOR = 1;
		private static final int TYPE_MAX_COUNT = TYPE_SEPARATOR + 1;

		ArrayList<Info> list_;
        private TreeSet mSeparatorsSet = new TreeSet();


		public CustomAdapter(Context context, ArrayList<Info> list) {
			super();

			list_ = list;
			// L.d("qq", "custom adapter create");
			// for (int i = 0; i < list.size(); ++i) {
			// mIdMap.put(list.get(i), i);
			// }

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			ViewHolder holder;
			// L.d("view", "getting view");
			int type = getItemViewType(position);
			Log.d("view", "" + type);

			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getActivity()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				holder = new ViewHolder();

				switch (type) {
				case TYPE_ITEM:
					v = vi.inflate(R.layout.view_textview_receive, null);

					break;

				case TYPE_SEPARATOR:
					v = vi.inflate(R.layout.view_textview_send, null);

					break;

				default:
					break;
				}

				holder.content = (TextView) v.findViewById(R.id.content);
				v.setTag(holder);
			} else {
				holder = (ViewHolder) v.getTag();
			}

			
			Info info = (Info) list_.get(position);
			if (info != null) {
				holder.content.setText(info.content);
				// holder.item2.setText(custom.getSecond());
			}
			
			
			return v;
		}

		public void addItem(final String text) {
			if (getActivity() != null) {
				getActivity().runOnUiThread(new Runnable() {

					@Override
					public void run() {
						list.add(new Info(text));
						adapter.notifyDataSetChanged(); 
					} 
				}); 
			}
		}

		public void addSeparatorItem(final String item) {
			if (getActivity() != null) {
				getActivity().runOnUiThread(new Runnable() {

					@Override
					public void run() {
						list_.add(new Info(item));
						// save separator position
						mSeparatorsSet.add(list_.size() - 1);
						notifyDataSetChanged(); 
					} 
				});
			}
		}

		@Override
		public int getItemViewType(int position) {
			return mSeparatorsSet.contains(position) ? TYPE_SEPARATOR
					: TYPE_ITEM;
		}

		@Override
		public int getViewTypeCount() {
			return TYPE_MAX_COUNT;
		}

		@Override
		public int getCount() {
			return list_.size();
		}

		
		
		@Override
		public Object getItem(int position) {
			return list_.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		
		@Override
		public boolean hasStableIds() {
			return true;
		}

		

	}

	class Info {
		String content;

		public Info(String soundName) {
			this.content = soundName;

		}
	}

	private class ViewHolder {
		TextView content;
	}

}
