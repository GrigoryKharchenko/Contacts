package contacts.app

data class ContactModel(
    val id: Int,
    val name: String,
    val lastname: String,
    val phone: String,
    val isViewedCheckBox: Boolean,
    val isChecked: Boolean
)
