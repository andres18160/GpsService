package com.example.camilo.gpsservice.Clases;



public class util {

/* QUERY SQL PARA DETERMINAR LAS COORDENADAS MAS CERCANAS A UN PUNTO POR LA DISTANCIA */

/*
SELECT Id,Mac, Latitud, Longitud,(6371* acos(cos(radians(6.1962899)) * cos(radians(Latitud)) * cos(radians(Longitud) - radians(-75.5583944)) + sin(radians(6.1962899)) *
sin(radians(Latitud)))) AS distance FROM coordenadas GROUP BY Mac HAVING distance < 0.015 ORDER BY Id DESC
 */
}
