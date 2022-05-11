package com.se7sopro.bankmisrchallengetask.utils.listeners

import java.io.Serializable


interface SelectableItem : Serializable {
    val id: Int?
    val name: String?
}