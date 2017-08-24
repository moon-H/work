package com.cssweb.mytest.nfc;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cssweb.mytest.R;

import java.io.UnsupportedEncodingException;

/**
 * Created by lenovo on 2016/5/17.
 */
public class NfcTag extends Activity {
    TextView nfcTView;
    NfcAdapter nfcAdapter;
    static final String TAG = "NfcTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        nfcTView = (TextView) findViewById(R.id.info_tv);
        findViewById(R.id.write).setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
                                                                //                    Tag tag = getIntent().getParcelableExtra(NfcAdapter.EXTRA_TAG);
                                                                //                    Ndef ndef = Ndef.get(tag);
                                                                //                    try {
                                                                //                        ndef.connect();
                                                                //                        NdefRecord ndefRecord = createTextRecord(data, Locale.US, true);
                                                                //                        NdefRecord[] records = {ndefRecord};
                                                                //                        NdefMessage ndefMessage = new NdefMessage(records);
                                                                //                        ndef.writeNdefMessage(ndefMessage);
                                                                //                    } catch (IOException e1) {
                                                                //                        // TODO Auto-generated catch block
                                                                //                        e1.printStackTrace();
                                                                //                    } catch (FormatException e) {
                                                            }
                                                        }
                                                    }

        );

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null)

        {
            nfcTView.setText("设备不支持NFC！");
            finish();
            return;
        }

        if (nfcAdapter != null && !nfcAdapter.isEnabled())

        {
            nfcTView.setText("请在系统设置中先启用NFC功能！");
            finish();
            return;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            Log.d(TAG, "onResume>>>>>>>>>>>>>>");
            readFromTag(getIntent());
        } else
            Log.d(TAG, "onResume22222222222 = " + getIntent().getAction());

    }

    private boolean readFromTag(Intent intent) {
        Parcelable[] rawArray = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        NdefMessage mNdefMsg = (NdefMessage) rawArray[0];
        NdefRecord mNdefRecord = mNdefMsg.getRecords()[0];
        try {
            if (mNdefRecord != null) {
                String readResult = new String(mNdefRecord.getPayload(), "UTF-8");
                Toast.makeText(NfcTag.this, "liwx == " + readResult, Toast.LENGTH_SHORT).show();
                return true;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Toast.makeText(NfcTag.this, "liwx == " + e.getMessage(), Toast.LENGTH_SHORT).show();

        }
        return false;
    }
}
