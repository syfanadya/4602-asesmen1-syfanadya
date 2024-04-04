package org.d3if3128.asesmenmobpro.navigation

sealed class Screen(val route: String) {
    data object Home: Screen("mainScreen")
}