package com.example.projectkeep.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.projectkeep.R

//transaction category model
data class TransactionCategoryModel(
    @StringRes
    val description: Int,
    @DrawableRes
    val icon: Int
) {

    companion object {

        val Bills = TransactionCategoryModel(
            description = R.string.bills,
            icon = R.drawable.ic_utilities
        )

        val Food = TransactionCategoryModel(
            description = R.string.food,
            icon = R.drawable.ic_food
        )

        val Education = TransactionCategoryModel(
            description = R.string.education,
            icon = R.drawable.ic_education
        )

        val Entertainment = TransactionCategoryModel(
            description = R.string.entertainment,
            icon = R.drawable.ic_entertainment
        )

        val Housing = TransactionCategoryModel(
            description = R.string.housing,
            icon = R.drawable.ic_entertainment
        )

        val Health = TransactionCategoryModel(
            description = R.string.health,
            icon = R.drawable.ic_medical
        )

        val Travel = TransactionCategoryModel(
            description = R.string.travel,
            icon = R.drawable.ic_personal
        )

        val Transportation = TransactionCategoryModel(
            description = R.string.transportation,
            icon = R.drawable.ic_transport
        )

        val Shopping = TransactionCategoryModel(
            description = R.string.shopping,
            icon = R.drawable.ic_shopping
        )

        val Salary = TransactionCategoryModel(
            description = R.string.salary,
            icon = R.drawable.ic_savings
        )



        val Other = TransactionCategoryModel(
            description = R.string.other,
            icon = R.drawable.ic_other
        )


        val TRANSACTION_CATEGORIES = arrayListOf(
            Bills,
            Food,
            Education,
            Entertainment,
            Housing,
            Travel,
            Transportation,
            Shopping,
            Salary,
            Other
        )
    }


}
