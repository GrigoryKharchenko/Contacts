package contacts.app

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class ContactService {

    private val _contacts = MutableLiveData(generateContacts())
    val contacts: LiveData<List<ContactModel>> = _contacts

    private val isCheckedSetId: HashSet<Int> = hashSetOf()

    fun addContact(contact: ContactModel) {
        if (_contacts.value == null) return

        val list = requireNotNull(_contacts.value).toMutableList()
        list.add(0, contact)
        _contacts.value = list
    }

    fun replaceContact(newContact: ContactModel) {
        if (_contacts.value == null) return

        val list = requireNotNull(_contacts.value).toMutableList()
        val indexContact = list.indexOfFirst { oldContact ->
            oldContact.id == newContact.id
        }
        if (indexContact == -1) return
        indexContact.let { id ->
            list.removeAt(id)
            list.add(id, newContact)
        }
        _contacts.value = list
    }

    fun deleteAllChecked() {
        if (_contacts.value == null) return

        val list = requireNotNull(_contacts.value).toMutableList()
        list.removeIf { isCheckedSetId.contains(it.id) }
        isCheckedSetId.clear()
        _contacts.value = list
    }

    fun updateContactModel() {
        if (_contacts.value == null) return

        val updateList =
            requireNotNull(_contacts.value).map { it.copy(isViewedCheckBox = !it.isViewedCheckBox) }
        _contacts.value = updateList
    }

    fun onCheckContact(contact: ContactModel, isChecked: Boolean) {
        if (_contacts.value == null) return

        if (isChecked) isCheckedSetId.add(contact.id) else isCheckedSetId.remove(contact.id)
        replaceContact(contact)
    }
}

