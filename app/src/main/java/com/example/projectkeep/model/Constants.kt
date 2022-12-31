package com.example.projectkeep.model

import android.content.Context
import com.example.projectkeep.R


// constants for transaction type, and transaction category

class Constants(context: Context) {
    val TRANSACTION_TYPE = listOf(
        context.getString(R.string.income),
        context.getString(R.string.expenses)
    )

    val TRANSACTION_CATEGORY = arrayListOf(
        context.getString(TransactionCategoryModel.Bills.description),
        context.getString(TransactionCategoryModel.Food.description),
        context.getString(TransactionCategoryModel.Education.description),
        context.getString(TransactionCategoryModel.Entertainment.description),
        context.getString(TransactionCategoryModel.Housing.description),
        context.getString(TransactionCategoryModel.Health.description),
        context.getString(TransactionCategoryModel.Travel.description),
        context.getString(TransactionCategoryModel.Transportation.description),
        context.getString(TransactionCategoryModel.Shopping.description),
        context.getString(TransactionCategoryModel.Salary.description),

        context.getString(TransactionCategoryModel.Other.description)
    )


}