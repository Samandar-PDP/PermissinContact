package uz.digital.permissions103

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.ContactsContract
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import uz.digital.permissions103.adapter.ContactAdapter
import uz.digital.permissions103.databinding.ActivityMainBinding
import uz.digital.permissions103.model.Contact

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val contactAdapter by lazy { ContactAdapter() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            checkPermission()
        }
        binding.rv.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = contactAdapter
        }
    }

    private fun checkPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), 100)
        } else {
            readContacts()
        }
    }

    private fun readContacts() {
        binding.button.isVisible = false
        binding.shimmerLayout.isVisible = true
        Handler(mainLooper).postDelayed({
            readAllContacts()
            binding.button.isVisible = false
            binding.shimmerLayout.isVisible = false
        }, 2000)
    }

    private fun readAllContacts() {
        val contactList = mutableListOf<Contact>()
        val contactQuery = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null,
            null
        )
        while (contactQuery?.moveToNext()!!) {
            val name =
                contactQuery.getString(contactQuery.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            val number =
                contactQuery.getString(contactQuery.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
            contactList.add(Contact(name, number))
        }
        contactQuery.close()
        contactAdapter.submitList(contactList)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            readContacts()
        } else {
            AlertDialog.Builder(this).apply {
                setTitle("Open settings")
                setMessage("You must give permission to read contacts!")
                setPositiveButton("Ok") { di, _ ->
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                    di.dismiss()
                }
            }.show()
        }
    }
}