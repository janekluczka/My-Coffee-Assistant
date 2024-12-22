package com.luczka.mycoffee.ui.screens.brews

sealed class BrewsAction {
    data object NavigateToAssistant: BrewsAction()
    data class NavigateToBrewDetails(val brewId: Long) : BrewsAction()
    data class OnSelectedFilterChanged(val brewFilter: BrewFilterUiState) : BrewsAction()
    data class OnItemActionsExpanded(val brewId: Long) : BrewsAction()
    data class OnItemActionsCollapsed(val brewId: Long) : BrewsAction()
    data class OnDeleteClicked(val brewId: Long) : BrewsAction()
}