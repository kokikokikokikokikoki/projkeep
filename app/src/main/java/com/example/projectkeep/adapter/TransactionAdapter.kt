package com.example.projectkeep.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.projectkeep.R
import com.example.projectkeep.model.Transactions
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TransactionAdapter(private val transactionList: ArrayList<Transactions>) : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {
    val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_transaction, parent, false)
        return TransactionViewHolder(view)
    }







    fun capitalizeTitle(title: String): String {
        return title.capitalize()
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
    val transaction : Transactions = transactionList[position]
       holder.amount.text = transaction.amount.toString()
        val title = transaction.title
        val capitalizedTitle = title?.let { capitalizeTitle(it) }
        holder.title.text = capitalizedTitle

        holder.categoryType.text = transaction.categoryType
        //capitalize category type
        val categoryType = transaction.categoryType
        val capitalizedCategoryType = categoryType?.let { capitalizeTitle(it) }
        holder.categoryType.text = capitalizedCategoryType

       // println(transaction.title)
        println(transaction.categoryType)




        //holder.categoryType.text = transaction.categoryType
        holder.date.text = dateFormat.format(transaction.date?.toDate())
        //holder.date.text = transaction.date?.toDate()?.toLocaleString()
if(transaction.transactionType == "Income"){
    val formatter = NumberFormat.getInstance(Locale.US)
              val formatted = formatter.format(holder.amount.text.toString().toDouble())
                holder.amount.text = "$formatted ฿"
    holder.amount.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.green))

} else {

              val formatter = NumberFormat.getInstance(Locale.US)
              val amountString = holder.amount.text.toString()
              val amount = java.lang.Double.parseDouble(amountString)
              val formatted = formatter.format(amount)
             // holder.amount.text = "- $formatted"

              // add baht sign
              holder.amount.text = "- $formatted ฿"


              //holder.amount.text = "- ${transaction.amount}"
              holder.amount.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.red))



}


// when category is pressed also change the image accordingly.
        when (transaction.categoryType) {
            "Food" -> holder.categoryImage.setImageResource(R.drawable.ic_food)
            "Transport" -> holder.categoryImage.setImageResource(R.drawable.ic_transport)
            "Shopping" -> holder.categoryImage.setImageResource(R.drawable.ic_shopping)
            "Education" -> holder.categoryImage.setImageResource(R.drawable.ic_education)
            "Medical" -> holder.categoryImage.setImageResource(R.drawable.ic_medical)
            "Other" -> holder.categoryImage.setImageResource(R.drawable.ic_baseline_category_24)
            "Stocks" -> holder.categoryImage.setImageResource(R.drawable.ic_stocks)
            "Savings" -> holder.categoryImage.setImageResource(R.drawable.ic_savings)
        }








    }




//        holder.bind()


    override fun getItemCount(): Int {
        return transactionList.size
    }



        public class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        fun bind() {
//            itemView.setOnClickListener {
//                val intent = Intent(itemView.context, TransactionDetailsActivity::class.java)
//                itemView.context.startActivity(intent)
//            }
//        }

        val categoryType : TextView = itemView.findViewById(R.id.tv_transaction_title)
        val date : TextView = itemView.findViewById(R.id.tv_transaction_description)
        val amount : TextView = itemView.findViewById(R.id.tv_transaction_amount)
        val title : TextView = itemView.findViewById(R.id.tv_transaction_title)
        val categoryImage : ImageView = itemView.findViewById(R.id.iv_transaction_icon)



    }


}
