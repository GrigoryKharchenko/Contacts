package contacts.app

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import contacts.app.databinding.ItemContactBinding

class ContactAdapter(
    private val onClickItem: (ContactModel) -> Unit,
    private val onChecked: (ContactModel, Boolean) -> Unit
) :
    ListAdapter<ContactModel, ContactViewHolder>(ContactDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder =
        ContactViewHolder(
            ItemContactBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) =
        holder.bind(getItem(position), onClickItem, onChecked)
}

class ContactViewHolder(private val binding: ItemContactBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        contactModel: ContactModel,
        onClickItem: (ContactModel) -> Unit,
        onChecked: (ContactModel, Boolean) -> Unit
    ) {
        with(binding) {
            tvName.text = itemView.context.getString(
                R.string.name_contact,
                contactModel.name,
                contactModel.lastname
            )
            tvNumber.text = contactModel.phone
            root.setOnClickListener {
                onClickItem(contactModel)
            }
            cbDelete.isVisible = contactModel.isViewedCheckBox
            cbDelete.setOnCheckedChangeListener { _, b ->
                onChecked(contactModel, b)
            }
        }
    }
}

class ContactDiffUtil : DiffUtil.ItemCallback<ContactModel>() {

    override fun areItemsTheSame(oldItem: ContactModel, newItem: ContactModel): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: ContactModel, newItem: ContactModel): Boolean =
        oldItem == newItem
}
