package com.example.proyectointegradora

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    private var m_bluetoothAdapter: BluetoothAdapter? = null
    private lateinit var m_pairedDivices: Set<BluetoothDevice>
    private var REQUEST_ENABLE_BLUETOOTH = 1


    companion object{
        val EXTRA_ADORESS : String = "Device_address"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        m_bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if(m_bluetoothAdapter == null){
            Toast.makeText(this, "this divice doesn´t support bluetooth", Toast.LENGTH_SHORT).show()
            return
        }
        if(!m_bluetoothAdapter!!.isEnabled){
            val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BLUETOOTH)
        }

        select_device_refresh.setOnClickListener { pairedDeviceList() }

    }

        private fun pairedDeviceList() {
            m_pairedDivices = m_bluetoothAdapter!!.bondedDevices
            val list : ArrayList<BluetoothDevice> = ArrayList()

            if(m_pairedDivices.isEmpty()){
                for(device : BluetoothDevice in m_pairedDivices){
                    list.add(device)
                    Log.i("device",""+device)
                }
            }else{
                Toast.makeText(this, "no paired bluetooth diveces found", Toast.LENGTH_SHORT).show()
            }

            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
            select_device_list.adapter = adapter
            select_device_list.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                val device: BluetoothDevice = list[position]
                val address: String = device.address

                val Intent = Intent(this, ControlActivity::class.java)
                intent.putExtra(EXTRA_ADORESS, address)
                startActivity(intent)
            }


        }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_ENABLE_BLUETOOTH){
            if(resultCode == Activity.RESULT_OK){
                if(m_bluetoothAdapter!!.isEnabled){
                    Toast.makeText(this, "Bluetooth has been enabled", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, "bluetooth has been disabled", Toast.LENGTH_SHORT).show()
                }
            }else if(resultCode == Activity.RESULT_CANCELED){
                Toast.makeText(this, "bluetooth enabling has been canceled", Toast.LENGTH_SHORT).show()
            }
        }
    }

}


