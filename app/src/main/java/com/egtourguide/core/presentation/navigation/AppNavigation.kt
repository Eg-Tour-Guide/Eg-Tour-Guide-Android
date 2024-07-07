package com.egtourguide.core.presentation.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.egtourguide.auth.presentation.screens.forgotPassword.ForgotPasswordScreen
import com.egtourguide.auth.presentation.screens.login.LoginScreen
import com.egtourguide.auth.presentation.screens.otp.OtpScreen
import com.egtourguide.auth.presentation.screens.resetPassword.ResetPasswordScreen
import com.egtourguide.auth.presentation.screens.signup.SignUpScreen
import com.egtourguide.auth.presentation.screens.welcome.WelcomeScreen
import com.egtourguide.customTours.presentation.createTour.CreateTourScreenRoot
import com.egtourguide.customTours.presentation.customExpanded.CustomExpandedScreenRoot
import com.egtourguide.home.presentation.screens.main.screens.artifactsList.ArtifactsListScreen
import com.egtourguide.expanded.presentation.screens.expanded.ExpandedScreenRoot
import com.egtourguide.expanded.presentation.screens.expanded.ExpandedType
import com.egtourguide.expanded.presentation.screens.expanded.WebViewScreen
import com.egtourguide.home.presentation.screens.filter.FilterScreen
import com.egtourguide.home.presentation.screens.main.MainScreen
import com.egtourguide.home.presentation.screens.main.screens.home.HomeScreen
import com.egtourguide.home.presentation.screens.main.screens.landmarksList.LandmarksListScreen
import com.egtourguide.expanded.presentation.screens.moreReviews.MoreReviewsScreenRoot
import com.egtourguide.customTours.presentation.myTours.MyToursScreen
import com.egtourguide.expanded.presentation.screens.review.ReviewScreenRoot
import com.egtourguide.user.presentation.savedItems.SavedScreen
import com.egtourguide.home.presentation.screens.search.SearchScreen
import com.egtourguide.home.presentation.screens.search_results.SearchResultsScreen
import com.egtourguide.expanded.presentation.screens.toursPlan.ToursPlanScreenRoot
import com.egtourguide.home.domain.model.DetectedArtifact
import com.egtourguide.home.domain.model.toReviewsList
import com.egtourguide.home.domain.model.toStringJson
import com.egtourguide.home.presentation.screens.filter.FilterScreenViewModel
import com.egtourguide.home.presentation.screens.filter.FilterType
import com.egtourguide.home.presentation.screens.main.screens.toursList.ToursListScreen
import com.egtourguide.home.presentation.screens.notifications.NotificationsScreenRoot
import com.egtourguide.user.presentation.changePassword.ChangePasswordScreenRoot
import com.egtourguide.user.presentation.editProfile.EditProfileScreenRoot
import com.egtourguide.user.presentation.settings.SettingsScreenRoot
import com.egtourguide.user.presentation.user.UserScreenRoot
import com.google.gson.Gson

@Composable
fun AppNavigation(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = {
            fadeIn(animationSpec = tween(durationMillis = 300))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(durationMillis = 300))
        },
        popEnterTransition = {
            fadeIn(animationSpec = tween(durationMillis = 300))
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(durationMillis = 300))
        }
    ) {
        authGraph(navController = navController)

        composable(route = AppGraph.Main.route) {
            MainScreen(
                navigateToExpanded = { id, expandedType ->
                    navController.navigate(
                        route = AppGraph.Expanded.route
                            .replace("{id}", id)
                            .replace("{expandedType}", expandedType)
                    )
                },
                navigateToMyTours = {
                    navController.navigate(route = AppGraph.CustomTours.route)
                }
            )
        }

        expandedGraph(
            navController = navController,
            navigateToMyTours = {
                navController.navigate(
                    route = AppGraph.CustomTours.route.replace("{isSelect}", "true")
                )
            }
        )

        customToursGraph(navController = navController)
    }
}

fun NavGraphBuilder.authGraph(navController: NavHostController) {
    navigation(
        route = AppGraph.Auth.route,
        startDestination = AppScreen.Welcome.route
    ) {
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
                    navController.navigate(route = AppGraph.Main.route) {
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
                    navController.navigate(route = AppGraph.Main.route) {
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
                onNavigateToOTP = { code, email ->
                    navController.navigate(
                        route = AppScreen.OTP.route
                            .replace("{code}", code)
                            .replace("{email}", email)
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
    }
}

@Composable
fun MainNavGraph(
    navController: NavHostController,
    navigateToExpanded: (String, String) -> Unit,
    navigateToMyTours: () -> Unit
) {
    val toursFilterViewModel: FilterScreenViewModel = hiltViewModel(key = "tours")
    val landmarksFilterViewModel: FilterScreenViewModel = hiltViewModel(key = "landmarks")
    val artifactsFilterViewModel: FilterScreenViewModel = hiltViewModel(key = "artifacts")
    val searchFilterViewModel: FilterScreenViewModel = hiltViewModel(key = "search")

    toursFilterViewModel.setType(FilterType.TOUR)
    landmarksFilterViewModel.setType(FilterType.LANDMARK)
    artifactsFilterViewModel.setType(FilterType.ARTIFACT)
    searchFilterViewModel.setType(FilterType.SEARCH)

    NavHost(
        navController = navController,
        route = AppGraph.Main.route,
        startDestination = AppScreen.Home.route,
        enterTransition = {
            fadeIn(animationSpec = tween(durationMillis = 300))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(durationMillis = 300))
        },
        popEnterTransition = {
            fadeIn(animationSpec = tween(durationMillis = 300))
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(durationMillis = 300))
        }
    ) {
        composable(route = AppScreen.Home.route) {
            HomeScreen(
                onNavigateToSearch = {
                    navController.navigate(route = AppScreen.Search.route)
                },
                navigateToNotifications = {
                    navController.navigate(route = AppScreen.Notification.route)
                },
                onNavigateToSinglePlace = { place ->
                    navigateToExpanded(place.id, ExpandedType.LANDMARK.name)
                },
                onNavigateToDetectedArtifact = { artifact ->
                    navigateToExpanded(artifact.id, ExpandedType.ARTIFACT.name)
                },
                onNavigateToEvent = { event ->
                    navigateToExpanded(event.id, ExpandedType.EVENT.name)
                }
            )
        }

        composable(route = AppScreen.ToursList.route) {
            ToursListScreen(
                filterViewModel = toursFilterViewModel,
                onNavigateToSearch = {
                    navController.navigate(route = AppScreen.Search.route)
                },
                navigateToNotifications = {
                    navController.navigate(route = AppScreen.Notification.route)
                },
                onNavigateToFilters = {
                    navController.navigate(route = AppScreen.ToursFilter.route)
                },
                onNavigateToSingleTour = { tour ->
                    navigateToExpanded(tour.id, ExpandedType.TOUR.name)
                },
                onNavigateToDetectedArtifact = { artifact ->
                    navigateToExpanded(artifact.id, ExpandedType.ARTIFACT.name)
                }
            )
        }

        composable(route = AppScreen.ToursFilter.route) {
            FilterScreen(
                viewModel = toursFilterViewModel,
                onNavigateBack = { navController.navigateUp() }
            )
        }

        composable(route = AppScreen.LandmarksList.route) {
            LandmarksListScreen(
                filterViewModel = landmarksFilterViewModel,
                onNavigateToSearch = {
                    navController.navigate(route = AppScreen.Search.route)
                },
                navigateToNotifications = {
                    navController.navigate(route = AppScreen.Notification.route)
                },
                onNavigateToFilters = {
                    navController.navigate(route = AppScreen.LandmarksFilter.route)
                },
                onNavigateToSinglePlace = {
                    navigateToExpanded(it.id, ExpandedType.LANDMARK.name)
                },
                onNavigateToDetectedArtifact = { artifact ->
                    navigateToExpanded(artifact.id, ExpandedType.ARTIFACT.name)
                }
            )
        }

        composable(route = AppScreen.LandmarksFilter.route) {
            FilterScreen(
                viewModel = landmarksFilterViewModel,
                onNavigateBack = { navController.navigateUp() }
            )
        }

        composable(route = AppScreen.ArtifactsList.route) {
            ArtifactsListScreen(
                filterViewModel = artifactsFilterViewModel,
                onNavigateToSearch = {
                    navController.navigate(route = AppScreen.Search.route)
                },
                navigateToNotifications = {
                    navController.navigate(route = AppScreen.Notification.route)
                },
                onNavigateToFilters = {
                    navController.navigate(route = AppScreen.ArtifactsFilter.route)
                },
                onNavigateToSingleArtifact = { artifact ->
                    navigateToExpanded(artifact.id, ExpandedType.ARTIFACT.name)
                },
                onNavigateToDetectedArtifact = { artifact ->
                    navigateToExpanded(artifact.id, ExpandedType.ARTIFACT.name)
                }
            )
        }

        composable(route = AppScreen.ArtifactsFilter.route) {
            FilterScreen(
                viewModel = artifactsFilterViewModel,
                onNavigateBack = { navController.navigateUp() }
            )
        }

        userGraph(
            navController = navController,
            navigateToMyTours = navigateToMyTours,
            navigateToNotifications = {
                navController.navigate(route = AppScreen.Notification.route)
            },
            onNavigateToDetectedArtifact = { artifact ->
                navigateToExpanded(artifact.id, ExpandedType.ARTIFACT.name)
            }
        )

        composable(route = AppScreen.Search.route) {
            SearchScreen(
                onNavigateToSearchResults = { query ->
                    navController.navigate(
                        route = AppScreen.SearchResults.route.replace("{query}", query)
                    )
                }
            )
        }

        composable(route = AppScreen.SearchResults.route) { navBackStackEntry ->
            val query = navBackStackEntry.arguments?.getString("query") ?: ""

            SearchResultsScreen(
                filterViewModel = searchFilterViewModel,
                query = query,
                onNavigateToSearch = {
                    navController.navigateUp()
                },
                onNavigateToSingleItem = { item ->
                    navigateToExpanded(
                        item.id,
                        if (item.isArtifact) ExpandedType.ARTIFACT.name else ExpandedType.LANDMARK.name
                    )
                },
                onNavigateToFilters = {
                    navController.navigate(route = AppScreen.SearchFilter.route)
                }
            )
        }

        composable(route = AppScreen.SearchFilter.route) {
            FilterScreen(
                viewModel = searchFilterViewModel,
                onNavigateBack = { navController.navigateUp() }
            )
        }

        composable(route = AppScreen.Notification.route) {
            NotificationsScreenRoot(
                onBackClicked = {
                    navController.navigateUp()
                }
            )
        }
    }
}

fun NavGraphBuilder.expandedGraph(
    navController: NavHostController,
    navigateToMyTours: () -> Unit
) {
    navigation(
        route = AppGraph.Expanded.route,
        startDestination = AppScreen.Expanded.route
    ) {
        composable(route = AppScreen.Expanded.route) { entry ->
            val parentEntry = remember(entry) {
                navController.getBackStackEntry(route = AppGraph.Expanded.route)
            }

            val fromInside = entry.arguments?.getString("fromInside").toBoolean()

            val id = if (fromInside) entry.arguments?.getString("id")
                ?: "" else parentEntry.arguments?.getString("id") ?: ""

            val expandedType = if (fromInside) entry.arguments?.getString("expandedType")
                ?: "" else parentEntry.arguments?.getString("expandedType") ?: ""

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
                onSeeMoreClicked = { reviews, reviewsAverage ->
                    navController.navigate(
                        route = AppScreen.MoreReviews.route
                            .replace("{id}", id)
                            .replace(
                                "{isLandmark}",
                                (expandedType == ExpandedType.LANDMARK.name).toString()
                            )
                            .replace("{reviews}", reviews.toStringJson())
                            .replace("{reviewsAverage}", reviewsAverage.toString())
                    )
                },
                onReviewClicked = {
                    navController.navigate(
                        route = AppScreen.Review.route
                            .replace("{id}", id)
                            .replace(
                                "{isLandmark}",
                                (expandedType == ExpandedType.LANDMARK.name).toString()
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
                navigateToTours = navigateToMyTours,
                goToTourPlan = { itemId ->
                    navController.navigate(
                        route = AppScreen.ToursPlan.route.replace("{tourId}", itemId)
                    )
                },
                navigateToSingleItem = { itemId, expandedType2 ->
                    navController.navigate(
                        route = AppScreen.Expanded.route
                            .replace("{fromInside}", "true")
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

        composable(route = AppScreen.MoreReviews.route) { entry ->
            val itemId = entry.arguments?.getString("id") ?: ""
            val isLandmark = entry.arguments?.getString("isLandmark").toBoolean()
            val reviewsJson = entry.arguments?.getString("reviews") ?: ""
            val reviewsAverage = entry.arguments?.getString("reviewsAverage") ?: ""

            MoreReviewsScreenRoot(
                reviews = reviewsJson.toReviewsList(),
                reviewsAverage = reviewsAverage.toDouble(),
                onNavigateBack = { navController.navigateUp() },
                onNavigateToReview = {
                    navController.navigate(
                        route = AppScreen.Review.route
                            .replace("{id}", itemId)
                            .replace("{isLandmark}", isLandmark.toString())
                    )
                }
            )
        }

        composable(route = AppScreen.Review.route) { entry ->
            val itemId = entry.arguments?.getString("id") ?: ""
            val isLandmark = entry.arguments?.getString("isLandmark").toBoolean()

            ReviewScreenRoot(
                id = itemId,
                isLandmark = isLandmark,
                onNavigateBack = {
                    navController.navigateUp()
                },
                onSuccessReview = {
                    navController.navigate(
                        route = AppScreen.Expanded.route
                            .replace("{id}", itemId)
                            .replace(
                                "{expandedType}",
                                if (isLandmark) ExpandedType.LANDMARK.name else ExpandedType.TOUR.name
                            )
                    ) {
                        popUpTo(route = AppScreen.Expanded.route) {
                            inclusive = true
                        }
                    }
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
                        route = AppGraph.Expanded.route
                            .replace("{id}", landmarkId)
                            .replace("{expandedType}", ExpandedType.LANDMARK.name)
                    )
                }
            )
        }
    }
}

fun NavGraphBuilder.userGraph(
    navController: NavHostController,
    navigateToMyTours: () -> Unit,
    navigateToNotifications: () -> Unit,
    onNavigateToDetectedArtifact: (DetectedArtifact) -> Unit
) {
    navigation(
        route = AppGraph.User.route,
        startDestination = AppScreen.User.route
    ) {
        composable(route = AppScreen.User.route) {
            UserScreenRoot(
                navigateToNotifications = navigateToNotifications,
                navigateToEditProfile = {
                    navController.navigate(route = AppScreen.EditProfile.route)
                },
                navigateToSaved = {
                    navController.navigate(route = AppScreen.Saved.route)
                },
                navigateToMyTours = navigateToMyTours,
                navigateToChangePassword = {
                    navController.navigate(route = AppScreen.ChangePassword.route)
                },
                navigateToSettings = {
                    navController.navigate(route = AppScreen.Settings.route)
                },
                onNavigateToDetectedArtifact = onNavigateToDetectedArtifact
            )
        }

        composable(route = AppScreen.EditProfile.route) {
            EditProfileScreenRoot(
                onBackClicked = {
                    navController.navigateUp()
                }
            )
        }

        composable(route = AppScreen.ChangePassword.route) {
            ChangePasswordScreenRoot(
                onBackClicked = {
                    navController.navigateUp()
                }
            )
        }

        composable(route = AppScreen.Settings.route) {
            SettingsScreenRoot(
                onBackClicked = {
                    navController.navigateUp()
                }
            )
        }

        composable(route = AppScreen.Saved.route) { backStackEntry ->
            val filtersJson = backStackEntry.arguments?.getString("filters")
            var filters: HashMap<*, *>? = null

            filtersJson?.let {
                filters = Gson().fromJson(
                    filtersJson.substringAfter('/'),
                    HashMap::class.java
                )
            }

            SavedScreen(
                filters = filters,
                onNavigateBack = {
                    navController.navigateUp()
                },
                onNavigateToSingleItem = { _ ->
                    //TODO handel navigation here
                },
                onNavigateToFilters = {
                    // TODO: Create own filters!!
                }
            )
        }
    }
}

fun NavGraphBuilder.customToursGraph(navController: NavHostController) {
    navigation(
        route = AppGraph.CustomTours.route,
        startDestination = AppScreen.MyTours.route
    ) {
        composable(route = AppScreen.MyTours.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(route = AppGraph.CustomTours.route)
            }

            val isSelected = parentEntry.arguments?.getBoolean("isSelect") ?: false
            val filtersJson = backStackEntry.arguments?.getString("filters")
            var filters: HashMap<*, *>? = null

            filtersJson?.let {
                filters = Gson().fromJson(
                    filtersJson.substringAfter('/'),
                    HashMap::class.java
                )
            }

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
                        navController.navigate(
                            route = AppScreen.CustomExpanded.route.replace("{tourId}", tour.id)
                        )
                    }
                },
                onNavigateToFilters = {
                    // TODO: Create own filters!!
                },
                onNavigateToCreateTour = {
                    navController.navigate(
                        route = AppScreen.CreateTour.route.replace("{isCreate}", "true")
                    )
                },
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }

        composable(route = AppScreen.CreateTour.route) { entry ->
            val isCreate = entry.arguments?.getString("isCreate").toBoolean()
            val tourId = entry.arguments?.getString("tourId") ?: ""
            val name = entry.arguments?.getString("name") ?: ""
            val description = entry.arguments?.getString("description") ?: ""

            CreateTourScreenRoot(
                isCreate = isCreate,
                tourId = tourId,
                name = name,
                description = description,
                onNavigateBack = {
                    navController.navigateUp()
                },
                navigateToMyTours = {
                    navController.navigate(route = AppScreen.MyTours.route) {
                        popUpTo(route = AppScreen.MyTours.route) {
                            inclusive = true
                        }
                    }
                },
                navigateToExpanded = {
                    // TODO: Navigate to custom expanded!!
                }
            )
        }

        composable(route = AppScreen.CustomExpanded.route) { entry ->
            val tourId = entry.arguments?.getString("tourId") ?: ""

            CustomExpandedScreenRoot(
                tourId = tourId,
                onBackClicked = { navController.navigateUp() },
                onEditClicked = { name, description ->
                    navController.navigate(
                        route = AppScreen.CreateTour.route
                            .replace("{isCreate}", "false")
                            .replace("{tourId}", tourId)
                            .replace("{name}", name)
                            .replace("{description}", description)
                    )
                },
                goToTourPlan = {
                    // TODO: Navigate!!
                }
            )
        }
    }
}