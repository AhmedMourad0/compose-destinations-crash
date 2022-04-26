package dev.ahmedmourad.composedestinationscrash

import com.ramcosta.composedestinations.annotation.NavGraph
import com.ramcosta.composedestinations.annotation.RootNavGraph

@NavGraph
@RootNavGraph
annotation class DiscoverNavGraph(val start: Boolean = false)

@NavGraph
@RootNavGraph
annotation class CalenderNavGraph(val start: Boolean = false)

@NavGraph
@RootNavGraph
annotation class MapNavGraph(val start: Boolean = false)

@NavGraph
@RootNavGraph
annotation class ProfileNavGraph(val start: Boolean = false)
