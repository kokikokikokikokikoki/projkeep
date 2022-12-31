package com.example.projectkeep.model

import com.google.firebase.Timestamp

class TransactionModel {
    //create transaction model for database

    //var id: Int = 0
   var id: String = ""
    var title: String? = null
    //var amount: Double = 0.0
    var amount: Long = 0
//    var date: String? = null
    var date: Timestamp? = null

    var transactionType: String? = null
    var categoryType: String? = null




//    constructor(
//        id: String,
//        title: String?,
//        amount: String,
//        date: String?,
//        transactionType: String?,
//        categoryType: String?
//    ) {
//        this.id = id
//        this.title = title
//        this.amount = amount
//        this.date = date
//        this.transactionType = transactionType
//        this.categoryType = TransactionCategoryModel(categoryType)
//    }



}
