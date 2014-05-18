package com.makewithmoto.makr.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

/*
 * write to the board writeSerial(cmd)
 * get data from the board onDataReceived
 * 
 * 
 */

public class MainActivity extends SerialPortActivity {

//	public TextView text;
//	RadioButton ledon, ledoff;
	
	Button distanceButton;
    TextView distanceView;
    SeekBar redBar, greenBar, blueBar, servoBar;


	private static final String TAG = "SerialReader";
	private static final String MAKR_ENABLE = "/sys/class/makr/makr/5v_enable";

	private String MAKRstr;
	private DebugFragment df;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initializeMAKR();
		
		distanceButton = (Button) findViewById(R.id.distanceButton);
		distanceView = (TextView) findViewById(R.id.distanceView);
		redBar = (SeekBar) findViewById(R.id.redBar);
		greenBar = (SeekBar) findViewById(R.id.greenBar);
		blueBar = (SeekBar) findViewById(R.id.blueBar);
		servoBar = (SeekBar) findViewById(R.id.servoBar);
		distanceButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	writeSerial("D");
//				Toast.makeText(getApplicationContext(), "Getting Distance",
//						Toast.LENGTH_LONG).show();
            }
        });
		
//		final Handler handler = new Handler();
//		final Runnable r = new Runnable()
//		{
//		    public void run() 
//		    {
//            	writeSerial("D");
//				Toast.makeText(getApplicationContext(), "Getting Distance",
//						Toast.LENGTH_LONG).show();		        
//				handler.postDelayed(this, 1000);
//		    }
//		};
//
//		handler.post(r);

		redBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener () {
			public void onProgressChanged(SeekBar seekBar, int progress,
	                boolean fromUser) {
				writeSerial("LR" + progress);
//				Toast.makeText(getApplicationContext(), "Set Red to " + progress,
//						Toast.LENGTH_LONG).show();
	        }

	        public void onStartTrackingTouch(SeekBar seekBar) {}

	        public void onStopTrackingTouch(SeekBar seekBar) {}
		});
		
		greenBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener () {
			public void onProgressChanged(SeekBar seekBar, int progress,
	                boolean fromUser) {
				writeSerial("LG" + progress);
//				Toast.makeText(getApplicationContext(), "Set Green to " + progress,
//						Toast.LENGTH_LONG).show();
	        }

	        public void onStartTrackingTouch(SeekBar seekBar) {}

	        public void onStopTrackingTouch(SeekBar seekBar) {}
		});
		
		blueBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener () {
			public void onProgressChanged(SeekBar seekBar, int progress,
	                boolean fromUser) {
				writeSerial("LB" + progress);
//				Toast.makeText(getApplicationContext(), "Set Blue to " + progress,
//						Toast.LENGTH_LONG).show();
	        }

	        public void onStartTrackingTouch(SeekBar seekBar) {}

	        public void onStopTrackingTouch(SeekBar seekBar) {}
		});
		
		servoBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener () {
			public void onProgressChanged(SeekBar seekBar, int progress,
	                boolean fromUser) {
				writeSerial("S" + progress);
//				Toast.makeText(getApplicationContext(), "Set Servo Angle to " + progress,
//						Toast.LENGTH_LONG).show();
	        }

	        public void onStartTrackingTouch(SeekBar seekBar) {}

	        public void onStopTrackingTouch(SeekBar seekBar) {}
		});

//		text = (TextView) findViewById(R.id.text);
//		ledon = (RadioButton) findViewById(R.id.ledon);
//		ledoff = (RadioButton) findViewById(R.id.ledoff);
//
//		ledon.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView,
//					boolean isChecked) {
//				if (isChecked) {
//					writeSerial("LEDON");
//					Toast.makeText(getApplicationContext(), "LEDON",
//							Toast.LENGTH_LONG).show();
//				}
//			}
//		});
//
//		ledoff.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView,
//					boolean isChecked) {
//				if (isChecked) {
//					writeSerial("LEDOFF");
//					Toast.makeText(getApplicationContext(), "LEDOFF",
//							Toast.LENGTH_SHORT).show();
//				}
//			}
//		});
//
//		df = new DebugFragment();
//		addFragment(df, R.id.f1);

	}
	

	public boolean isEnabled() {
		boolean status = false;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(MAKR_ENABLE));
			String rv = br.readLine();
			if (rv.matches("on")) {
				return true;
			}
		} catch (FileNotFoundException e) {
			Log.e(TAG, e.getMessage());
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
				}
			}
		}
		return status;
	}

	/*
	 * Turn on or off the device
	 */
	public void enable(boolean value) {
		BufferedWriter writer = null;
		try {
			FileOutputStream fos = new FileOutputStream(MAKR_ENABLE);
			OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
			writer = new BufferedWriter(osw);
			if (value)
				writer.write("on\n");
			else
				writer.write("off\n");
		} catch (FileNotFoundException e) {
			Log.e(TAG, e.getMessage());
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
				}
			}
		}
	}

	public void initializeMAKR() {

		// enable the device
		if (!isEnabled()) {
			enable(true);
		}
	}

	public void writeSerial(String cmd) {

		try {
			cmd = cmd + "\n";
			mOutputStream.write(cmd.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void onDataReceived(final byte[] buffer, final int size) {
		runOnUiThread(new Runnable() {
			public void run() {

				MAKRstr = new String(buffer, 0, size);
				distanceView.setText(MAKRstr);
				//df.adapter.addItem(MAKRstr);
			}
		});
	}

	public void addFragment(Fragment f, int fragmentPosition) {

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.add(fragmentPosition, f);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		ft.commit();

	}

}
