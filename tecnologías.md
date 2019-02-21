# Tecnologías utilizadas

La principal funcionalidad que debe implementar nuestra aplicación es el procesado tanto de imágenes como de texto.
Para el desarrollo vamos a utilizar las siguientes tecnologías:

- **Github:** como plataforma para el control de versiones. Alojará el repositorio de todo el material y será utilizada como
  herramienta de gestión para coordinadar la actividad del equipo y la distribución de las tareas. Seguiremos un sistema basado
  en issues, las cuales serán asignadas a cada integrante del grupo y revisadas por el resto.
  
- **Android Studio:** la aplicación se desarrollará de forma nativa para el sistema operativo Android usando Android Studio
  como entorno de trabajo. Será requisito necesario una versión de sistema oprativo igual o superior a Android 4.0.3 
  IceCreamSandwich.

- **SQLite:** para la gestión de la base de datos local asociada a la aplicación. En esta se almacenarán los productos de los que
  dispone el usuario y se utilizará como referencia para identificar los ingredientes pendientes para elaborar las recetas.
  
- **Java:** 
  
- **Text Recognition API de Google:** para el reconocimiento de texto en imágenes.<br/>
  https://developers.google.com/vision/android/text-overview
  
  Esta API nos permite la detección de texto a tres niveles:
  - Bloque: un conjunto de líneas como podrían ser un párrafo o columna.
  - Línea: un conjunto de palabras contiguas localizadas en el mismo eje.
  - Palabras: un conjunto de caracteres alfanuméricos localizados en el mismo eje.
  
  ![texto](https://developers.google.com/vision/images/text-structure.png)

- **Stanford CoreNLP**: para el procesamiento de lenguaje natural.<br/>
  https://stanfordnlp.github.io/CoreNLP/
