package com.example.prueba_n2

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.prueba_n2.model.AppDatabase
import com.example.prueba_n2.repository.ComentarioRepository
import com.example.prueba_n2.repository.ProductoRepository
import com.example.prueba_n2.repository.UsuarioRepository
import com.example.prueba_n2.ui.screens.DetallesProducto
import com.example.prueba_n2.ui.screens.PantallaCarrito
import com.example.prueba_n2.ui.screens.PantallaDetallePublicacion
import com.example.prueba_n2.ui.screens.PantallaIngreso
import com.example.prueba_n2.ui.screens.PantallaPago
import com.example.prueba_n2.ui.screens.PantallaPerfil
import com.example.prueba_n2.ui.screens.PantallaRegistro
import com.example.prueba_n2.ui.screens.PublicarProducto
import com.example.prueba_n2.ui.theme.Prueba_n2Theme
import com.example.prueba_n2.utils.SessionManager
import com.example.prueba_n2.viewmodel.ComentarioViewModel
import com.example.prueba_n2.viewmodel.ComentarioViewModelFactory
import com.example.prueba_n2.viewmodel.LoginState
import com.example.prueba_n2.viewmodel.LoginViewModel
import com.example.prueba_n2.viewmodel.LoginViewModelFactory
import com.example.prueba_n2.viewmodel.ProductoViewModel
import com.example.prueba_n2.viewmodel.ProductoViewModelFactory

class MainActivity : ComponentActivity() {

    private val database by lazy { AppDatabase.getDatabase(this, lifecycleScope) }
    private val usuarioRepository by lazy { UsuarioRepository(database.usuarioDao()) }
    private val productoRepository by lazy { ProductoRepository(database.productoDao()) }
    private val comentarioRepository by lazy { ComentarioRepository(database.comentarioDao()) }
    private val sessionManager by lazy { SessionManager(this) }

    private val loginViewModelFactory by lazy { LoginViewModelFactory(usuarioRepository, sessionManager) }
    private val productoViewModelFactory by lazy { ProductoViewModelFactory(productoRepository) }
    private val comentarioViewModelFactory by lazy { ComentarioViewModelFactory(comentarioRepository) }

    private val loginViewModel: LoginViewModel by viewModels { loginViewModelFactory }
    private val productoViewModel: ProductoViewModel by viewModels { productoViewModelFactory }
    private val comentarioViewModel: ComentarioViewModel by viewModels { comentarioViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Prueba_n2Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(loginViewModel, productoViewModel, comentarioViewModel)
                }
            }
        }
    }
}

object AppRoutes {
    const val LOGIN = "login"
    const val REGISTRO = "registro"
    const val PRINCIPAL = "principal"
    const val PUBLICAR = "publicar"
    const val PERFIL = "perfil"
    const val CARRITO = "carrito"
    const val PAGO = "pago/{montoTotal}/{metodoPago}"
    const val DETALLE_PUBLICACION = "detalle_publicacion/{productoId}"

    fun crearRutaDetalle(productoId: String) = "detalle_publicacion/$productoId"
    fun crearRutaPago(montoTotal: String, metodoPago: String) = "pago/$montoTotal/$metodoPago"
}

@Composable
fun AppNavigation(
    loginViewModel: LoginViewModel,
    productoViewModel: ProductoViewModel,
    comentarioViewModel: ComentarioViewModel
) {
    val navController = rememberNavController()
    val currentUser by loginViewModel.currentUser.collectAsState()
    val loginState by loginViewModel.loginState.collectAsState()
    val productos by productoViewModel.productos.collectAsState()
    val productoSeleccionado by productoViewModel.productoSeleccionado.collectAsState()

    val startDestination = if (loginState is LoginState.Success) AppRoutes.PRINCIPAL else AppRoutes.LOGIN

    NavHost(navController = navController, startDestination = startDestination) {

        composable(AppRoutes.LOGIN) {
            PantallaIngreso(
                loginState = loginState,
                onLogin = loginViewModel::login,
                onNavigateToRegister = { navController.navigate(AppRoutes.REGISTRO) },
                onLoginSuccess = {
                    navController.navigate(AppRoutes.PRINCIPAL) {
                        popUpTo(AppRoutes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(AppRoutes.REGISTRO) {
            PantallaRegistro(
                onRegister = { nombre, email, pass ->
                    loginViewModel.registrarUsuario(nombre, email, pass)
                    navController.navigate(AppRoutes.PRINCIPAL) {
                        popUpTo(AppRoutes.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }

        composable(AppRoutes.PRINCIPAL) {
            DetallesProducto(
                productos = productos,
                productoViewModel = productoViewModel,
                onNavigateToPublish = { navController.navigate(AppRoutes.PUBLICAR) },
                onNavigateToProfile = { navController.navigate(AppRoutes.PERFIL) },
                onNavigateToDetail = { productoId ->
                    navController.navigate(AppRoutes.crearRutaDetalle(productoId))
                },
                onNavigateToCart = { navController.navigate(AppRoutes.CARRITO) }
            )
        }

        composable(AppRoutes.CARRITO) {
            PantallaCarrito(
                viewModel = productoViewModel,
                onBack = { navController.popBackStack() },
                onNavigateToPago = { monto, metodo ->
                     navController.navigate(AppRoutes.crearRutaPago(monto, metodo))
                }
            )
        }
        composable(
            route = AppRoutes.PAGO,
            arguments = listOf(
                navArgument("montoTotal") { type = NavType.StringType },
                navArgument("metodoPago") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val montoTotal = backStackEntry.arguments?.getString("montoTotal") ?: "0"
            val metodoPago = backStackEntry.arguments?.getString("metodoPago") ?: ""

            PantallaPago(
                montoTotal = montoTotal,
                metodoPago = metodoPago,
                onPaymentSuccess = {
                    productoViewModel.vaciarCarrito()
                    navController.navigate(AppRoutes.PRINCIPAL) {
                        popUpTo(AppRoutes.CARRITO) { inclusive = true }
                    } 
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(AppRoutes.PUBLICAR) {
            PublicarProducto(
                currentUser = currentUser,
                onProductoPublicado = { nuevoProducto ->
                    productoViewModel.addProducto(nuevoProducto)
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(AppRoutes.PERFIL) {
            PantallaPerfil(
                usuario = currentUser,
                onLogout = {
                    loginViewModel.logout()
                    navController.navigate(AppRoutes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onBack = { navController.popBackStack() },
                productoViewModel = productoViewModel,
                onNavigateToDetail = { productoId ->
                    navController.navigate(AppRoutes.crearRutaDetalle(productoId))
                }
            )
        }

        composable(
            route = AppRoutes.DETALLE_PUBLICACION,
            arguments = listOf(navArgument("productoId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productoId = backStackEntry.arguments?.getString("productoId")
            val context = LocalContext.current

            if (productoId != null) {
                LaunchedEffect(productoId) {
                    productoViewModel.getProducto(productoId)
                }
            }

            if (productoSeleccionado != null && productoSeleccionado!!.id == productoId) {
                PantallaDetallePublicacion(
                    producto = productoSeleccionado!!,
                    comentarioViewModel = comentarioViewModel,
                    usuarioActualNombre = currentUser?.nombre ?: "Anónimo",
                    onBack = { navController.popBackStack() },
                    onAddToCart = { prod, cant ->
                        productoViewModel.agregarAlCarrito(prod, cant)
                        Toast.makeText(context, "Agregado al carrito", Toast.LENGTH_SHORT).show()
                    }
                )
            } else {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}
