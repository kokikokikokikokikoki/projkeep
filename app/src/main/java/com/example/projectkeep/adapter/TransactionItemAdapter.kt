package com.example.projectkeep.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.projectkeep.R
import com.example.projectkeep.databinding.ListItemTransactionBinding
import com.example.projectkeep.model.TransactionCategoryModel
import com.example.projectkeep.model.TransactionModel
import kotlinx.android.synthetic.main.list_item_transaction.view.*
import kotlinx.coroutines.flow.MutableStateFlow
import java.text.SimpleDateFormat

// transactions recycler view adapter

class TransactionItemAdapter:
    RecyclerView.Adapter<(TransactionItemAdapter.TransactionViewHolder)>(){
    inner class TransactionViewHolder(val binding: ListItemTransactionBinding) :
        RecyclerView.ViewHolder(binding.root)

//    private var _currency = MutableStateFlow("THB")
//    var currency = _currency.value

    // connect and get transaction from database
    private val differCallback = object : DiffUtil.ItemCallback<TransactionModel>() {
        override fun areItemsTheSame(
            oldItem: TransactionModel,
            newItem: TransactionModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: TransactionModel,
            newItem: TransactionModel
        ): Boolean {
            return oldItem == newItem
        }
    }

    // get transaction from database
    val differ = AsyncListDiffer(this, differCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding =
            ListItemTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionViewHolder(binding)
    }

// get transaction from database
    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    // get transaction from database
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val item = differ.currentList[position]

        holder.binding.apply {
            tvTransactionTitle.text = item.categoryType.toString()
            tvTransactionDescription.text = item.date.toString()
            tvTransactionAmount.text = item.amount.toString()
            ivTransactionIcon.setImageResource(item.categoryType.toString().toInt())



        }

        // set transaction type color
        when (item.transactionType) {
            "Income" -> {
                holder.binding.tvTransactionAmount.setTextColor(
                    ContextCompat.getColor(
                        holder.binding.root.context,
                        android.R.color.holo_green_dark
                    )
                )





            }


            "Expense" -> {
                holder.binding.tvTransactionAmount.setTextColor(
                    ContextCompat.getColor(
                        holder.binding.root.context,
                        android.R.color.holo_red_dark
                    )
                )
            }
        }
        // set transaction type icon
        when (item.transactionType) {
            "Income" -> {
                holder.binding.ivTransactionIcon.setImageResource(R.drawable.ic_income)
            }
            "Expense" -> {
                holder.binding.ivTransactionIcon.setImageResource(R.drawable.ic_expense)
            }
        }

        // set transaction category icon
        when (item.categoryType.toString()) {


            "Food" -> {
                holder.binding.ivTransactionIcon.setImageResource(R.drawable.ic_food)
            }
            "Education" -> {
                holder.binding.ivTransactionIcon.setImageResource(R.drawable.ic_baseline_auto_stories_24)
            }


            "Medical" -> {
                holder.binding.ivTransactionIcon.setImageResource(R.drawable.ic_medical)
            }
            "Other" -> {
                holder.binding.ivTransactionIcon.setImageResource(R.drawable.ic_baseline_category_24)
            }
            "Transport" -> {
                holder.binding.ivTransactionIcon.setImageResource(R.drawable.ic_transport)
            }
            "Shopping" -> {
                holder.binding.ivTransactionIcon.setImageResource(R.drawable.ic_baseline_shopping_cart_24)
            }
        }
        // set transaction category color
        when (item.categoryType.toString()) {

            "Food" -> {
                holder.binding.tvTransactionAmount.setTextColor(
                    ContextCompat.getColor(
                        holder.binding.root.context,
                        android.R.color.holo_green_dark
                    )
                )
            }
            "Education" -> {
                holder.binding.tvTransactionAmount.setTextColor(
                    ContextCompat.getColor(
                        holder.binding.root.context,
                        android.R.color.holo_green_dark
                    )
                )
            }


            "Medical" -> {
                holder.binding.tvTransactionAmount.setTextColor(
                    ContextCompat.getColor(
                        holder.binding.root.context,
                        android.R.color.holo_green_dark
                    )
                )
            }
            "Other" -> {
                holder.binding.tvTransactionAmount.setTextColor(
                    ContextCompat.getColor(
                        holder.binding.root.context,
                        android.R.color.holo_green_dark
                    )
                )
            }
            "Transport" -> {
                holder.binding.tvTransactionAmount.setTextColor(
                    ContextCompat.getColor(
                        holder.binding.root.context,
                        android.R.color.holo_green_dark
                    )
                )
            }
            "Shopping" -> {
                holder.binding.tvTransactionAmount.setTextColor(
                    ContextCompat.getColor(
                        holder.binding.root.context,
                        android.R.color.holo_green_dark
                    )
                )
            }
        }





    }

    fun setTransactionList(transactionList: MutableList<TransactionModel>) {
        differ.submitList(transactionList)

    }

}