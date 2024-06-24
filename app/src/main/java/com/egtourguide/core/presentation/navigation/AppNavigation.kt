package com.egtourguide.core.presentation.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.egtourguide.auth.presentation.forgotPassword.ForgotPasswordScreen
import com.egtourguide.auth.presentation.login.LoginScreen
import com.egtourguide.auth.presentation.otp.OtpScreen
import com.egtourguide.auth.presentation.resetPassword.ResetPasswordScreen
import com.egtourguide.auth.presentation.signup.SignUpScreen
import com.egtourguide.auth.presentation.welcome.WelcomeScreen
import com.egtourguide.home.presentation.components.BottomBarScreens
import com.egtourguide.home.presentation.screens.artifacts_list.ArtifactsListScreen
import com.egtourguide.home.presentation.screens.expanded.ExpandedScreenRoot
import com.egtourguide.home.presentation.screens.expanded.ExpandedType
import com.egtourguide.home.presentation.screens.expanded.WebViewScreen
import com.egtourguide.home.presentation.screens.filter.FilterScreen
import com.egtourguide.home.presentation.screens.home.HomeScreen
import com.egtourguide.home.presentation.screens.landmarks_list.LandmarksListScreen
import com.egtourguide.home.presentation.screens.moreReviews.MoreReviewsScreenRoot
import com.egtourguide.home.presentation.screens.my_tours.MyToursScreen
import com.egtourguide.home.presentation.screens.review.ReviewScreen
import com.egtourguide.home.presentation.screens.saved_items.SavedScreen
import com.egtourguide.home.presentation.screens.search.SearchScreen
import com.egtourguide.home.presentation.screens.search_results.SearchResultsScreen
import com.egtourguide.home.presentation.screens.toursPlan.ToursPlanScreenRoot
import com.egtourguide.home.presentation.screens.tours_list.ToursListScreen
import com.google.gson.Gson

@Composable
fun AppNavigation(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(navController = navController, startDestination = startDestination) {

        /** Auth Screens */

        composable(route = AppScreen.Welcome.route) {
            WelcomeScreen(
                onNavigateToLogin = {
                    navController.navigate(AppScreen.Login.route)
                }
            )
        }

        composable(route = AppScreen.SignUp.route) {
            SignUpScreen(
                onNavigateToLogin = {
                    navController.navigate(route = AppScreen.Login.route) {
                        popUpTo(route = AppScreen.Login.route) {
                            inclusive = true
                        }
                    }
                },
                onNavigateToOTP = { code, name, email, phone, password ->
                    navController.navigate(
                        route = AppScreen.OTP.route
                            .replace("{code}", code)
                            .replace("{fromSignup}", "true")
                            .replace("{name}", name)
                            .replace("{email}", email)
                            .replace("{phone}", phone)
                            .replace("{password}", password)
                    )
                }
            )
        }

        composable(route = AppScreen.OTP.route) { entry ->
            val code = entry.arguments?.getString("code") ?: ""
            val fromSignup = entry.arguments?.getString("fromSignup").toBoolean()
            val name = entry.arguments?.getString("name") ?: ""
            val email = entry.arguments?.getString("email") ?: ""
            val phone = entry.arguments?.getString("phone") ?: ""
            val password = entry.arguments?.getString("password") ?: ""

            OtpScreen(
                code = code,
                fromSignup = fromSignup,
                name = name,
                email = email,
                phone = phone,
                password = password,
                onNavigateToResetPassword = {
                    navController.navigate(
                        route = AppScreen.ResetPassword.route.replace("{code}", code)
                    ) {
                        popUpTo(route = AppScreen.Login.route)
                    }
                },
                onNavigateToHome = {
                    navController.navigate(AppScreen.Home.route) {
                        popUpTo(route = AppScreen.Welcome.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(route = AppScreen.Login.route) {
            LoginScreen(
                onNavigateToSignUp = {
                    navController.navigate(route = AppScreen.SignUp.route)
                },
                onNavigateToForgetPassword = {
                    navController.navigate(route = AppScreen.ForgetPassword.route)
                },
                onNavigateToHome = {
                    navController.navigate(route = AppScreen.Home.route) {
                        popUpTo(route = AppScreen.Welcome.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }


        composable(route = AppScreen.ForgetPassword.route) {
            ForgotPasswordScreen(
                onNavigateUp = {
                    navController.navigateUp()
                },
                onNavigateToOTP = { code ->
                    navController.navigate(
                        route = AppScreen.OTP.route.replace("{code}", code)
                    ) {
                        popUpTo(route = AppScreen.Login.route)
                    }
                }
            )
        }

        composable(route = AppScreen.ResetPassword.route) { backStackEntry ->
            val code = backStackEntry.arguments?.getString("code")
            code?.let {
                ResetPasswordScreen(
                    code = it,
                    onNavigateToLogin = {
                        navController.navigate(route = AppScreen.Login.route) {
                            popUpTo(route = AppScreen.Login.route)
                        }
                    }
                )
            }
        }


        /** Home Screens */

        composable(route = AppScreen.Home.route) {
            HomeScreen(
                onNavigateToSearch = {
                    navController.navigate(route = AppScreen.Search.route)
                },
                onNavigateToSinglePlace = { place ->
                    navController.navigate(
                        route = AppScreen.Expanded.route
                            .replace("{id}", place.id)
                            .replace("{expandedType}", ExpandedType.LANDMARK.name)
                    )
                },
                onNavigateToDetectedArtifact = { artifact ->
                    navController.navigate(
                        route = AppScreen.Expanded.route
                            .replace("{id}", artifact.id)
                            .replace("{expandedType}", ExpandedType.ARTIFACT.name)
                    )
                },
                onNavigateToEvent = { event ->
                    navController.navigate(
                        route = AppScreen.Expanded.route
                            .replace("{id}", event.id)
                            .replace("{expandedType}", ExpandedType.EVENT.name)
                    )
                },
                onNavigateToSingleCategory = {

                },
                onNavigateToTours = {
                    navController.navigate(route = AppScreen.ToursList.route) {
                        popUpTo(route = AppScreen.Home.route)
                    }
                },
                onNavigateToLandmarks = {
                    navController.navigate(route = AppScreen.LandmarksList.route) {
                        popUpTo(route = AppScreen.Home.route)
                    }
                },
                onNavigateToArtifacts = {
                    navController.navigate(route = AppScreen.ArtifactsList.route) {
                        popUpTo(route = AppScreen.Home.route)
                    }
                },
                onNavigateToUser = {

                }
            )
        }
//        TODO here
        composable(route = AppScreen.Filter.route) { it ->
            val source = it.arguments?.getString("SOURCE")
            val query = it.arguments?.getString("QUERY")
            Log.d("````TAG````", "AppNavigation source: $source")
            FilterScreen(
                source = source!!,
                query = query ?: "",
                onNavigateToResults = { hashMap ->
                    val hashMapJson = Gson().toJson(hashMap)
                    source.let {
                        when (it) {
                            "tour" -> navController.navigate(
                                AppScreen.ToursList.route.replace(
                                    "{filters}",
                                    hashMapJson
                                )
                            ) {
                                popUpTo(AppScreen.Home.route)
                            }

                            "landmark" -> navController.navigate(
                                AppScreen.LandmarksList.route.replace(
                                    "{filters}",
                                    hashMapJson
                                )
                            ) {
                                popUpTo(AppScreen.Home.route)
                            }

                            "search" -> navController.navigate(
                                AppScreen.SearchResults.route.replace(
                                    "{filters}",
                                    hashMapJson
                                )
                            ) {
                                popUpTo(AppScreen.Search.route)
                            }

                            "saved" -> navController.navigate(
                                AppScreen.Saved.route.replace(
                                    "{filters}",
                                    hashMapJson
                                )
                            ){
                                //TODO change this to user when user screen is created
                                popUpTo(AppScreen.Home.route)
                            }

                            "my_tours" -> navController.navigate(
                                AppScreen.MyTours.route.replace(
                                    "{filters}",
                                    hashMapJson
                                )
                            ) {
                                popUpTo(AppScreen.MyTours.route)
                            }

                            "artifact" -> navController.navigate(
                                AppScreen.ArtifactsList.route.replace(
                                    "{filters}",
                                    hashMapJson
                                )
                            ) {
                                popUpTo(AppScreen.Home.route)
                            }
                        }
                    }
                },
                onNavigateBack = { navController.navigateUp() }
            )
        }

        composable(route = AppScreen.Expanded.route) { entry ->
            val id = entry.arguments?.getString("id") ?: ""
            val expandedType = entry.arguments?.getString("expandedType") ?: ""
            val tourId = entry.savedStateHandle.get<String>(key = "tourId") ?: ""
            val tourName = entry.savedStateHandle.get<String>(key = "tourName") ?: ""
            val tourImage = entry.savedStateHandle.get<String>(key = "tourImage") ?: ""

            ExpandedScreenRoot(
                id = id,
                tourId = tourId,
                tourName = tourName,
                tourImage = tourImage,
                expandedType = expandedType,
                onBackClicked = { navController.navigateUp() },
                onSeeMoreClicked = {
                    navController.navigate(route = AppScreen.MoreReviews.route)
                },
                onReviewClicked = {
                    navController.navigate(
                        route = AppScreen.Review.route
                            .replace("{id}", id)
                            .replace(
                                "{source}",
                                if (expandedType == ExpandedType.LANDMARK.name) "place" else "tour"
                            )
                    )
                },
                navigateToWebScreen = { modelUrl ->
                    navController.navigate(
                        route = AppScreen.WebView.route.replace(
                            "{modelUrl}",
                            modelUrl
                                .replace("/", "...")
                                .replace("?", "~~~")
                        )
                    )
                },
                navigateToTours = {
                    navController.navigate(
                        route = AppScreen.MyTours.route.replace("{isSelect}", "true")
                    )
                },
                goToTourPlan = { itemId ->
                    navController.navigate(
                        route = AppScreen.ToursPlan.route.replace("{tourId}", itemId)
                    )
                },
                navigateToSingleItem = { itemId, expandedType2 ->
                    navController.navigate(
                        route = AppScreen.Expanded.route
                            .replace("{id}", itemId)
                            .replace("{expandedType}", expandedType2)
                    )
                }
            )
        }

        composable(route = AppScreen.WebView.route) { entry ->
            val modelUrl = entry.arguments?.getString("modelUrl") ?: ""

            WebViewScreen(
                modelUrl = modelUrl
                    .replace("...", "/")
                    .replace("~~~", "?")
            )
        }

        composable(
            route = AppScreen.LandmarksList.route,
            arguments = listOf(
                navArgument("filters") {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) { backStackEntry ->
            val filtersJson = backStackEntry.arguments?.getString("filters")
            var filters: HashMap<*, *>? = null
            filtersJson?.let {
                Log.d("```TAG```", "AppNavigation: ${filtersJson.substringAfter('/')}")
                filters = Gson().fromJson(
                    filtersJson.substringAfter('/'),
                    HashMap::class.java
                )
            }
            LandmarksListScreen(
                filters = filters,
                onNavigateToSearch = {
                    navController.navigate(
                        route = AppScreen.Search.route.replace(
                            "{selected_bottom_bar_item}",
                            BottomBarScreens.Landmarks.name
                        )
                    )
                },
                onNavigateToFilters = {
                    navController.navigate(
                        route = AppScreen.Filter.route.replace(
                            "{SOURCE}",
                            "landmark"
                        ).replace("{QUERY}", "null")
                    )
                },
                onNavigateToSinglePlace = {
                    navController.navigate(
                        route = AppScreen.Expanded.route
                            .replace("{id}", it.id)
                            .replace("{expandedType}", ExpandedType.LANDMARK.name)
                    )
                },
                onNavigateToHome = {
                    navController.navigateUp()
                },
                onNavigateToTours = {
                    navController.navigate(route = AppScreen.ToursList.route) {
                        popUpTo(route = AppScreen.Home.route)
                    }
                },
                onNavigateToArtifacts = {
                    navController.navigate(route = AppScreen.ArtifactsList.route) {
                        popUpTo(route = AppScreen.Home.route)
                    }
                },
                onNavigateToUser = {

                }
            )
        }

        composable(route = AppScreen.ArtifactsList.route) { backStackEntry ->
            val filtersJson = backStackEntry.arguments?.getString("filters")
            var filters: HashMap<*, *>? = null
            filtersJson?.let {
                Log.d("```TAG```", "AppNavigation: ${filtersJson.substringAfter('/')}")
                filters = Gson().fromJson(
                    filtersJson.substringAfter('/'),
                    HashMap::class.java
                )
            }

            ArtifactsListScreen(
                filters = filters,
                onNavigateToSearch = {
                    navController.navigate(
                        route = AppScreen.Search.route.replace(
                            "{selected_bottom_bar_item}",
                            BottomBarScreens.Artifacts.name
                        )
                    )
                },
                onNavigateToFilters = {
                    navController.navigate(
                        route = AppScreen.Filter.route.replace(
                            "{SOURCE}",
                            "artifact"
                        ).replace("{QUERY}", "null")
                    )
                },
                onNavigateToSingleArtifact = {
                    navController.navigate(
                        route = AppScreen.Expanded.route
                            .replace("{id}", it.id)
                            .replace("{expandedType}", ExpandedType.ARTIFACT.name)
                    )
                },
                onNavigateToHome = {
                    navController.navigateUp()
                },
                onNavigateToTours = {
                    navController.navigate(route = AppScreen.ToursList.route) {
                        popUpTo(route = AppScreen.Home.route)
                    }
                },
                onNavigateToLandmarks = {
                    navController.navigate(route = AppScreen.LandmarksList.route) {
                        popUpTo(route = AppScreen.Home.route)
                    }
                },
                onNavigateToUser = {

                }
            )
        }

        composable(route = AppScreen.ToursList.route) { backStackEntry ->
            val filtersJson = backStackEntry.arguments?.getString("filters")
            var filters: HashMap<*, *>? = null
            filtersJson?.let {
                Log.d("```TAG```", "AppNavigation: ${filtersJson.substringAfter('/')}")
                filters = Gson().fromJson(
                    filtersJson.substringAfter('/'),
                    HashMap::class.java
                )
            }
            ToursListScreen(
                filters = filters,
                onNavigateToSearch = {
                    navController.navigate(
                        route = AppScreen.Search.route.replace(
                            "{selected_bottom_bar_item}",
                            BottomBarScreens.Tours.name
                        )
                    )
                },
                onNavigateToFilters = {
                    navController.navigate(
                        route = AppScreen.Filter.route.replace(
                            "{SOURCE}",
                            "tour"
                        ).replace("{QUERY}", "null")
                    )
                },
                onNavigateToSingleTour = { tour ->
                    navController.navigate(
                        route = AppScreen.Expanded.route
                            .replace("{id}", tour.id)
                            .replace("{expandedType}", ExpandedType.TOUR.name)
                    )
                },
                onNavigateToHome = {
                    navController.navigateUp()
                },
                onNavigateToArtifacts = {
                    navController.navigate(route = AppScreen.ArtifactsList.route) {
                        popUpTo(route = AppScreen.Home.route)
                    }
                },
                onNavigateToLandmarks = {
                    navController.navigate(route = AppScreen.LandmarksList.route) {
                        popUpTo(route = AppScreen.Home.route)
                    }
                },
                onNavigateToUser = {
                    navController.navigate(route = AppScreen.Saved.route)
                }
            )
        }

        composable(route = AppScreen.MoreReviews.route) {
            MoreReviewsScreenRoot(
                onNavigateBack = { navController.navigateUp() },
                onNavigateToReview = {
                    navController.navigate(route = AppScreen.Review.route)
                }
            )
        }

        composable(route = AppScreen.Review.route) { entry ->
            val itemId = entry.arguments?.getString("id") ?: ""
            val source = entry.arguments?.getString("source") ?: ""

            ReviewScreen(
                id = itemId,
                isPlace = source == "place",
                isTour = source == "tour",
                onNavigateBack = {
                    navController.navigate(
                        route = AppScreen.Expanded.route
                            .replace("{id}", itemId)
                            .replace(
                                "{expandedType}",
                                if (source == "place") ExpandedType.LANDMARK.name else ExpandedType.TOUR.name
                            )
                    ) {
                        popUpTo(route = AppScreen.Expanded.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(route = AppScreen.Search.route) { navBackStackEntry ->
            val selectedBottomBarItemString = navBackStackEntry.arguments?.getString(
                "selected_bottom_bar_item"
            )
            selectedBottomBarItemString?.let { item ->
                val selectedBottomBarItem = when (item) {
                    "Home" -> BottomBarScreens.Home
                    "Landmarks" -> BottomBarScreens.Landmarks
                    "Artifacts" -> BottomBarScreens.Artifacts
                    "Tours" -> BottomBarScreens.Tours
                    "User" -> BottomBarScreens.User
                    else -> BottomBarScreens.Home
                }
                SearchScreen(
                    bottomBarSelectedScreen = selectedBottomBarItem,
                    onNavigateToHome = {
                        navController.navigate(route = AppScreen.Home.route) {
                            popUpTo(route = AppScreen.Home.route) {
                                inclusive = true
                            }
                        }
                    },
                    onNavigateToTours = {
                        navController.navigate(route = AppScreen.ToursList.route) {
                            popUpTo(route = AppScreen.Home.route)
                        }
                    },
                    onNavigateToLandmarks = {
                        navController.navigate(route = AppScreen.LandmarksList.route) {
                            popUpTo(route = AppScreen.Home.route)
                        }
                    },
                    onNavigateToArtifacts = {
                        navController.navigate(route = AppScreen.ArtifactsList.route) {
                            popUpTo(route = AppScreen.Home.route)
                        }
                    },
                    onNavigateToUser = {

                    },
                    onNavigateToSearchResults = { query ->
                        navController.navigate(
                            route = AppScreen.SearchResults.route
                                .replace("{query}", query)
                                .replace("{selected_bottom_bar_item}", selectedBottomBarItem.name)
                        )
                    }
                )
            }
        }

        composable(route = AppScreen.SearchResults.route) { navBackStackEntry ->
            val filtersJson = navBackStackEntry.arguments?.getString("filters")
            var filters: HashMap<*, *>? = null
            Log.d("```TAG```", "json: ${filtersJson?.substringAfter('/')}")
            filtersJson?.let {
                filters = Gson().fromJson(
                    filtersJson.substringAfter('/'),
                    HashMap::class.java
                )
                Log.d("```TAG```", "test: $filters")
            }
            val query = navBackStackEntry.arguments?.getString("query")
            val selectedBottomBarItemString = navBackStackEntry.arguments?.getString(
                "selected_bottom_bar_item"
            )
            var selectedItem = BottomBarScreens.Home
            selectedBottomBarItemString?.let { item ->
                selectedItem = when (item) {
                    "Home" -> BottomBarScreens.Home
                    "Landmarks" -> BottomBarScreens.Landmarks
                    "Artifacts" -> BottomBarScreens.Artifacts
                    "Tours" -> BottomBarScreens.Tours
                    "User" -> BottomBarScreens.User
                    else -> BottomBarScreens.Home
                }
            }

            Log.d("```TAG```", "AppNavigation filters: $filters")
            SearchResultsScreen(
                query = query ?: "",
                filters = filters,
                selectedBottomBarItem = selectedItem,
                onNavigateToSearch = {
                    navController.navigateUp()
                },
                onNavigateToSingleItem = { item ->
                    navController.navigate(
                        route = AppScreen.Expanded.route
                            .replace(
                                "{id}",
                                item.id
                            ).replace(
                                "{isLandmark}",
                                "${!item.isArtifact}"
                            )
                    )
                },
                onNavigateToHome = {
                    navController.navigate(route = AppScreen.Home.route) {
                        popUpTo(route = AppScreen.Home.route) {
                            inclusive = true
                        }
                    }
                },
                onNavigateToTours = {
                    navController.navigate(route = AppScreen.ToursList.route) {
                        popUpTo(route = AppScreen.Home.route)
                    }
                },
                onNavigateToLandmarks = {
                    navController.navigate(route = AppScreen.LandmarksList.route) {
                        popUpTo(route = AppScreen.Home.route)
                    }
                },
                onNavigateToArtifacts = {
                    navController.navigate(route = AppScreen.ArtifactsList.route) {
                        popUpTo(route = AppScreen.Home.route)
                    }
                },
                onNavigateToUser = {

                },
                onNavigateToFilters = {
                    navController.navigate(
                        route = AppScreen.Filter.route.replace(
                            "{SOURCE}",
                            "search"
                        ).replace("{QUERY}", query ?: "")
                    )
                }
            )

        }

        composable(route = AppScreen.ToursPlan.route) { entry ->
            val tourID = entry.arguments?.getString("tourId") ?: ""

            ToursPlanScreenRoot(
                tourId = tourID,
                onBackClicked = {
                    navController.navigateUp()
                },
                navigateToLandmark = { landmarkId ->
                    navController.navigate(
                        route = AppScreen.Expanded.route
                            .replace("{id}", landmarkId)
                            .replace("{expandedType}", ExpandedType.LANDMARK.name)
                    )
                }
            )
        }

        composable(route = AppScreen.Saved.route) { backStackEntry ->
            val filtersJson = backStackEntry.arguments?.getString("filters")
            var filters: HashMap<*, *>? = null
            filtersJson?.let {
                Log.d("```TAG```", "AppNavigation: ${filtersJson.substringAfter('/')}")
                filters = Gson().fromJson(
                    filtersJson.substringAfter('/'),
                    HashMap::class.java
                )
            }
            Log.d("````TAG````", "saved filters: $filters")
            SavedScreen(
                filters = filters,
                onNavigateBack = {
                    navController.navigateUp()
                },
                onNavigateToSingleItem = { item ->
                    //TODO handel navigation here
                },
                onNavigateToFilters = {
                    navController.navigate(
                        route = AppScreen.Filter.route.replace(
                            "{SOURCE}",
                            "saved"
                        ).replace("{QUERY}", "null")
                    )
                }
            )
        }

        composable(route = AppScreen.MyTours.route) { backStackEntry ->
            val isSelected = backStackEntry.arguments?.getBoolean("isSelect") ?: false
            val filtersJson = backStackEntry.arguments?.getString("filters")
            var filters: HashMap<*, *>? = null
            filtersJson?.let {
                filters = Gson().fromJson(
                    filtersJson.substringAfter('/'),
                    HashMap::class.java
                )
            }
            Log.d("```TAG```", "mytours filters: $filters")
            MyToursScreen(
                filters = filters,
                onNavigateToSingleTour = { tour ->
                    if (isSelected) {
                        navController.previousBackStackEntry?.savedStateHandle?.set(
                            "tourId",
                            tour.id
                        )
                        navController.previousBackStackEntry?.savedStateHandle?.set(
                            "tourName",
                            tour.title
                        )
                        navController.previousBackStackEntry?.savedStateHandle?.set(
                            "tourImage",
                            tour.image
                        )
                        navController.navigateUp()
                    } else {
                        // TODO: Navigate!!
                    }
                },
                onNavigateToFilters = {
                    navController.navigate(
                        route = AppScreen.Filter.route
                            .replace("{SOURCE}", "my_tours")
                            .replace("{QUERY}", "null")
                    )
                },
                onNavigateToCreateTour = {
                    //TODO navigate to create tour screen
                },
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }
    }
}