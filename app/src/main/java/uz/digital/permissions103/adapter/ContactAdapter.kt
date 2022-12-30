package uz.digital.permissions103.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.digital.permissions103.R
import uz.digital.permissions103.databinding.ItemLayoutBinding
import uz.digital.permissions103.model.Contact

class ContactAdapter : ListAdapter<Contact, ContactAdapter.ContactViewHolder>(DiffCallBack()) {
    private lateinit var context: Context

    private class DiffCallBack : DiffUtil.ItemCallback<Contact>() {
        override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        context = parent.context
        return ContactViewHolder(
            ItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ContactViewHolder(private val binding: ItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(contact: Contact) {
            binding.apply {
                image.text = "${contact.name[0]}"
                name.text = contact.name
                number.text = contact.number

                image.setBackgroundResource(randomView())
            }
        }
    }

    @ColorRes
    private fun randomView(): Int {
        val list = listOf(
            R.drawable.text_stroke,
            R.drawable.text_stroke2,
            R.drawable.text_stroke3,
            R.drawable.text_stroke1
        )
        return list.random()
    }
}