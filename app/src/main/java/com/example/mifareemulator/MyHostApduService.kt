package com.example.mifareemulator

import android.nfc.cardemulation.HostApduService
import android.os.Bundle


class MyHostApduService: HostApduService {
    override fun processCommandApdu(commandApdu: ByteArray, extras: Bundle?): ByteArray {
        TODO("Not yet implemented")
    }

    override fun onDeactivated(reason: Int) {
        TODO("Not yet implemented")
    }
}
