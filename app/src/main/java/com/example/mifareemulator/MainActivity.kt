package com.example.mifareemulator

import android.content.Intent
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.nfc.NfcAdapter
import android.nfc.NdefMessage
import android.nfc.Tag
import android.nfc.tech.MifareClassic
import android.util.Log
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private val tag: String = "MainActivityTag"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        if (NfcAdapter.ACTION_TECH_DISCOVERED == intent.action) {
            var tagFromIntent: Tag? = intent?.getParcelableExtra(NfcAdapter.EXTRA_TAG)
            Log.i(tag, tagFromIntent.toString())
            Log.i(tag, "UID " + tagFromIntent?.id)

            if (tagFromIntent != null) {
                tagFromIntent.techList.forEach { tech ->
                    Log.i(tag, tech)
                }

                MifareClassic.get(tagFromIntent)?.use { mifare: MifareClassic ->
                    when (mifare.size) {
                        320 -> Log.i(tag, "SIZE MINI")
                        1024 -> Log.i(tag, "SIZE 1K")
                        2048 -> Log.i(tag, "SIZE 2K")
                        4096 -> Log.i(tag, "SIZE 4K")
                    }
                    when(mifare.type) {
                        0 -> Log.i(tag, "TYPE CLASSIC")
                        1 -> Log.i(tag, "TYPE PLUS")
                        2 -> Log.i(tag, "TYPE PRO")
                        -1 -> Log.i(tag, "TYPE UNKNOWN")
                    }
                    mifare.connect()

                    if (mifare.isConnected ) {
                        // Authentication
                        // val key: ByteArray = ByteArray(6){ 0x0 }
                        // var currentByte = 0
                        // var ioEnabled = false
                        // while(key[5].toInt() != 0xff || !ioEnabled) {
                        //     try {
                        //         if (mifare.authenticateSectorWithKeyA(0, key)) {
                        //             ioEnabled = true;
                        //         } else {
                        //             key[currentByte]++

                        //             if (key[currentByte].toInt() == 0xff) {
                        //                 currentByte++
                        //             }
                        //         }
                        //     } catch(e: Exception) {
                        //         Log.w(tag, e?.message.toString())
                        //         if (mifare.isConnected) {
                        //             mifare.close()
                        //         }

                        //         mifare.connect()
                        //     }
                        // }

                        // if (ioEnabled) {
                        //     Log.i(tag, "Successful authentication !")
                        //     // var manufacturerBlock = mifare.readBlock(0)
                        //     // var uidByteArr = manufacturerBlock.sliceArray(0..7)
                        //     // Log.i(tag, "UID : " + uidByteArr.toString())
                        // } else {
                        //     Log.i(tag, "Failed to authenticate !")
                        // }

                        mifare.close()
                    }
                }
            }
        }

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.i(tag, "New intent")

        intent?.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)?.also { rawMessages ->
            val messages: List<NdefMessage> = rawMessages.map { it as NdefMessage }
            // Process the messages array.
            print(messages)
        }
    }
}