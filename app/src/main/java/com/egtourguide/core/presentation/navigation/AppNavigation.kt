package com.egtourguide.core.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.egtourguide.auth.presentation.forgotPassword.ForgotPasswordScreen
import com.egtourguide.auth.presentation.login.LoginScreen
import com.egtourguide.auth.presentation.otp.OtpScreen
import com.egtourguide.auth.presentation.resetPassword.ResetPasswordScreen
import com.egtourguide.auth.presentation.signup.SignUpScreen
import com.egtourguide.auth.presentation.welcome.WelcomeScreen
import com.egtourguide.home.presentation.components.BottomBarScreens
import com.egtourguide.home.presentation.myTours.MyToursScreenRoot
import com.egtourguide.home.presentation.screens.artifacts_list.ArtifactsListScreen
import com.egtourguide.home.presentation.screens.expanded.ExpandedScreenRoot
import com.egtourguide.home.presentation.screens.expanded.WebViewScreen
import com.egtourguide.home.presentation.screens.home.HomeScreen
import com.egtourguide.home.presentation.screens.landmarks_list.LandmarksListScreen
import com.egtourguide.home.presentation.screens.moreReviews.MoreReviewsScreenRoot
import com.egtourguide.home.presentation.screens.review.ReviewScreen
import com.egtourguide.home.presentation.screens.search.SearchScreen
import com.egtourguide.home.presentation.screens.toursPlan.ToursPlanScreenRoot
import com.egtourguide.home.presentation.screens.tours_list.ToursListScreen

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

        // TODO: Change to home screen!!
        composable(route = AppScreen.Home.route) {
            HomeScreen(
                onNavigateToSearch = {
                    navController.navigate(route = AppScreen.Search.route)
                },
                onNavigateToNotification = {

                },
                onNavigateToSinglePlace = {

                },
                onNavigateToEvent = {

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

        composable(route = AppScreen.Expanded.route) { entry ->
            val id = entry.arguments?.getString("id") ?: ""
            val isLandmark = entry.arguments?.getString("isLandmark").toBoolean()
            val tourId = entry.savedStateHandle.get<String>(key = "tourId") ?: ""
            val tourName = entry.savedStateHandle.get<String>(key = "tourName") ?: ""
            val tourImage = entry.savedStateHandle.get<String>(key = "tourImage") ?:""

            ExpandedScreenRoot(
                id = id,
                tourId = tourId,
                tourName = tourName,
                tourImage = tourImage,
                isLandmark = isLandmark,
                onBackClicked = { navController.navigateUp() },
                onSeeMoreClicked = {
                    navController.navigate(route = AppScreen.MoreReviews.route)
                },
                onReviewClicked = {
                    navController.navigate(route = AppScreen.Review.route)
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

        composable(route = AppScreen.LandmarksList.route) {
            LandmarksListScreen(
                onNavigateToNotification = {

                },
                onNavigateToSearch = {

                },
                onNavigateToFilters = {

                },
                onNavigateToSinglePlace = {
                    navController.navigate(
                        route = AppScreen.Expanded.route
                            .replace("{id}", it.id)
                            .replace("{isLandmark}", "true")
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

        composable(route = AppScreen.ArtifactsList.route) {
            ArtifactsListScreen(
                onNavigateToNotification = {

                },
                onNavigateToSearch = {

                },
                onNavigateToFilters = {

                },
                onNavigateToSingleArtifact = {
                    navController.navigate(
                        route = AppScreen.Expanded.route
                            .replace("{id}", it.id)
                            .replace("{isLandmark}", "false")
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

        composable(route = AppScreen.ToursList.route) {
            ToursListScreen(
                onNavigateToNotification = {

                },
                onNavigateToSearch = {

                },
                onNavigateToFilters = {

                },
                onNavigateToSingleTour = { tour ->
                    // TODO: Remove this!!!
                    navController.navigate(
                        route = AppScreen.ToursPlan.route.replace("{tourId}", tour.id)
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

        composable(route = AppScreen.Review.route) {
            ReviewScreen(
                onNavigateBack = { navController.navigateUp() }
            )
        }

        composable(route = AppScreen.Search.route) {
            SearchScreen(
                bottomBarSelectedScreen = BottomBarScreens.Home,
                onNavigateToHome = {

                },
                onNavigateToTours = {

                },
                onNavigateToLandmarks = {

                },
                onNavigateToArtifacts = {

                },
                onNavigateToUser = {

                },
                onNavigateToSearchResults = {

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
                            .replace("{isLandmark}", "true")
                    )
                }
            )
        }

        composable(route = AppScreen.MyTours.route) {
            MyToursScreenRoot(
                onTourClicked = { tour ->
                    navController.previousBackStackEntry?.savedStateHandle?.set("tourId", tour.id)
                    navController.previousBackStackEntry?.savedStateHandle?.set(
                        "tourName",
                        tour.title
                    )
                    navController.previousBackStackEntry?.savedStateHandle?.set(
                        "tourImage",
                        tour.image
                    )
                    navController.navigateUp()
                }
            )
        }
    }
}