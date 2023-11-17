package contacts.app

import io.github.serpro69.kfaker.Faker

var idContact: Int = 0
    get() {
        field++
        return field
    }
    private set

fun generateContacts(): List<ContactModel> {
    val contacts = mutableListOf<ContactModel>()
    for (i in 1..100) {
        contacts.add(createContact(idContact))
    }
    return contacts
}

fun createContact(id: Int): ContactModel {
    val faker = Faker()
    return ContactModel(
        id = id,
        name = faker.name.firstName(),
        lastname = faker.name.lastName(),
        phone = faker.phoneNumber.phoneNumber(),
        isViewedCheckBox = false,
        isChecked = false
    )
}
