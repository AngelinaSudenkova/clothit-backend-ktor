package com.clothit.server.model.enums

enum class FriendApplicationStatus {
    ACCEPTED, REJECTED, PENDING, SENT;

    companion object {
        fun isAccepted(status: FriendApplicationStatus) = status == ACCEPTED
        fun isPending(status: FriendApplicationStatus) = status == PENDING
    }
}