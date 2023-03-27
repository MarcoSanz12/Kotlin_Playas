package com.example.kotlin_playas.data.model.aemet.symbol

class SymbolProvider {

    var symbolList : List<AemetSymbol> = listOf<AemetSymbol>(
        AemetSymbol("despejado","https://www.aemet.es/imagenes_gcd/_iconos_municipios/11.png"),
        AemetSymbol("nuboso","https://www.aemet.es/imagenes_gcd/_iconos_municipios/14.png"),
        AemetSymbol("muy nuboso","https://www.aemet.es/imagenes_gcd/_iconos_municipios/15.png"),
        AemetSymbol("chubascos","https://www.aemet.es/imagenes_gcd/_iconos_municipios/43.png"),
        AemetSymbol("muy nuboso con lluvia","https://www.aemet.es/imagenes_gcd/_iconos_municipios/25.png")
    )
}