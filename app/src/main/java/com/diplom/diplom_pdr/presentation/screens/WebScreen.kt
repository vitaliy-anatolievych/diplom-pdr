package com.diplom.diplom_pdr.presentation.screens

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.diplom.diplom_pdr.databinding.FragmentWebBinding

class WebScreen : Fragment() {

    private var _binding: FragmentWebBinding? = null
    private val binding: FragmentWebBinding
        get() = _binding ?: throw NullPointerException("FragmentWebBinding is null")

    private var pageIsLoaded = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWebBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpWebView()
        setUpProgressBar()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setUpWebView() = with(binding) {
        webMain.visibility = View.INVISIBLE

        webMain.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                val script = """
                // Виберіть тег <header>
                var header = document.querySelector('header');
            
                // Перевірте, чи тег <header> існує перед видаленням
                if (header) {
                    header.parentNode.removeChild(header);
                }
                           
                // Виберіть тег <footer>
                var footer = document.querySelector('footer');
            
                // Перевірте, чи тег <footer> існує перед видаленням
                if (footer) {
                    footer.parentNode.removeChild(footer);
                }                
            
                // Ваш JavaScript код для видалення елементів DOM
                // Наприклад, якщо ви хочете видалити всі div з класом "unwanted":
                var publicity = document.getElementsByClassName('publicity');
                while (publicity.length > 0) {
                    publicity[0].parentNode.removeChild(publicity[0]);
                }
                
                var rightSection = document.getElementsByClassName('right_section');
                while (rightSection.length > 0) {
                    rightSection[0].parentNode.removeChild(rightSection[0]);
                }

                var sectionNavigation = document.getElementsByClassName('mobile_link_a');
                while (sectionNavigation.length > 0) {
                    sectionNavigation[0].parentNode.removeChild(sectionNavigation[0]);
                } 

                var formMobile = document.getElementsByClassName('form_1 form_mobile');
                while (formMobile.length > 0) {
                    formMobile[0].parentNode.removeChild(formMobile[0]);
                } 
                                              
                var rightLink = document.getElementsByClassName('right_link');
                while (rightLink.length > 0) {
                    rightLink[0].parentNode.removeChild(rightLink[0]);
                }               
                
                var leftLink = document.getElementsByClassName('left_link');
                while (leftLink.length > 0) {
                    leftLink[0].parentNode.removeChild(leftLink[0]);
                }       
                                                                
                """.trimIndent()

                webMain.evaluateJavascript(script) {
                    pbLoadPage.visibility = View.GONE
                    webMain.visibility = View.VISIBLE
                    pageIsLoaded = true
                }

            }
        }

        with(webMain.settings) {
            blockNetworkImage = false
            blockNetworkLoads = false
            builtInZoomControls = false
            javaScriptEnabled = true
            domStorageEnabled = true
        }

        val link = WebScreenArgs.fromBundle(requireArguments()).link
        webMain.loadUrl(link)

//        registerOnBackPressedDelegation(activity, this@GameOnline.lifecycle) {
//            if (webMain.canGoBack()) {
//                webMain.goBack()
//            } else {
//                activity?.moveTaskToBack(true)
//            }
//        }
    }

    private fun setUpProgressBar() = with(binding) {
        pbLoadPage.max = 100

        webMain.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                if (newProgress < 100 && !pageIsLoaded) {
                    pbLoadPage.visibility = View.VISIBLE
                    pbLoadPage.progress = newProgress
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}