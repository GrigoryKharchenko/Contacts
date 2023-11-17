package contacts.app

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import contacts.app.databinding.ActivityMainBinding
import contacts.app.databinding.DialogContactBinding

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private var currentContact: ContactModel? = null

    private lateinit var binding: ActivityMainBinding
    private lateinit var contactDialogBinding: DialogContactBinding

    private val contactService = ContactService()
    private val adapter = ContactAdapter(onClickItem = { contact ->
        currentContact = contact
        showChangeContactDialog(contact)
    },
        onChecked = { contactModel, boolean ->
            contactService.onCheckContact(contactModel, boolean)
        }
    )

    private val addContactDialog: AlertDialog by lazy {
        AlertDialog.Builder(this).setView(contactDialogBinding.root)
            .setPositiveButton(getString(R.string.add_contact)) { _, _ ->
                with(contactDialogBinding) {
                    val contact = ContactModel(
                        currentContact?.id ?: idContact,
                        etName.text.toString(),
                        etLastname.text.toString(),
                        etPhoneNumber.text.toString(),
                        isViewedCheckBox = false,
                        isChecked = false
                    )
                    currentContact?.let { contactService.replaceContact(contact) }
                        ?: contactService.addContact(contact)
                }
            }.setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                currentContact = null
                dialog.cancel()
            }.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        contactDialogBinding = DialogContactBinding.inflate(layoutInflater)
        with(binding) {
            setContentView(root)
            rvContacts.adapter = adapter

            btnAddContact.setOnClickListener {
                showAddContactDialog()
            }
            btnConfirmDelete.setOnClickListener {
                contactService.deleteAllChecked()
            }
        }
        contactService.contacts.observe(this) { contacts ->
            adapter.submitList(contacts)
        }

        showDeleteButtons()
        cancelDelete()
    }

    private fun showDeleteButtons() {
        with(binding) {
            btnDelete.setOnClickListener {
                btnAddContact.isInvisible = true
                btnCancelDelete.isVisible = true
                btnConfirmDelete.isVisible = true
                contactService.updateContactModel()
            }
        }
    }

    private fun cancelDelete() {
        with(binding) {
            btnCancelDelete.setOnClickListener {
                btnAddContact.isInvisible = false
                btnCancelDelete.isVisible = false
                btnConfirmDelete.isVisible = false
                contactService.updateContactModel()
            }
        }
    }

    private fun showAddContactDialog() {
        with(contactDialogBinding) {
            etLastname.text = null
            etName.text = null
            etPhoneNumber.text = null
            addContactDialog.show()
        }
    }

    private fun showChangeContactDialog(contactModel: ContactModel) {
        with(contactDialogBinding) {
            etLastname.setText(contactModel.lastname)
            etName.setText(contactModel.name)
            etPhoneNumber.setText(contactModel.phone)
            addContactDialog.show()
        }
    }
}
