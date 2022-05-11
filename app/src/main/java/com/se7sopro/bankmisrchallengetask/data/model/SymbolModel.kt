package com.se7sopro.bankmisrchallengetask.data.model

import com.se7sopro.bankmisrchallengetask.utils.listeners.SelectableItem

data class SymbolModel(override val name :String, val value : String,
                       override val id: Int?
) : SelectableItem
