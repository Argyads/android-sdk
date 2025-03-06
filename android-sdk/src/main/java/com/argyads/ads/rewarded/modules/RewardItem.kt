package com.argyads.ads.rewarded.modules

class RewardItem(private val rewardItem: com.google.android.gms.ads.rewarded.RewardItem) {

    fun getType(): String = rewardItem.type

    fun getAmount(): Int = rewardItem.amount

    override fun toString(): String {
        return "RewardItem(type=${getType()}, amount=${getAmount()})"
    }
}