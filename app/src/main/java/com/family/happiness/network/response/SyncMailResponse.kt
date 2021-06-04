package com.family.happiness.network.response

import com.family.happiness.room.mail.Mail

data class SyncMailResponse(
    val mails: List<Mail>
)