# Tecnologías utilizadas

La principal funcionalidad que debe implementar nuestra aplicación es el procesado tanto de imágenes como de texto.
Para el desarrollo vamos a utilizar las siguientes tecnologías:

## Gestión del proyecto

- **Github:** como plataforma para el control de versiones. Alojará el repositorio de todo el material y será utilizada como
  herramienta de gestión para coordinar la actividad del equipo y la distribución de las tareas. Seguiremos un sistema basado
  en issues, las cuales serán asignadas a cada integrante del grupo y revisadas por el resto.

## Desarrollo de la aplicación

- **Android Studio:** la aplicación se desarrollará de forma nativa para el sistema operativo Android usando Android Studio
  como entorno de trabajo. Será requisito necesario una versión de sistema operativo igual o superior a Android 4.0.3 
  IceCreamSandwich.

- **SQLite:** para la gestión de la base de datos local asociada a la aplicación. En esta se almacenarán los productos de los
  que dispone el usuario y se utilizará como referencia para identificar los ingredientes pendientes para elaborar las recetas.
  Al tratarse de una versión ligera es perfecta para su utilización en dispositivos móviles.
  
- **Java/C++:** Java es el principal lenguaje para el desarrollo de aplicaciones Android. En base a esto deberemos identificar
  aquellas tecnología compatibles y fácilmente integrables. En caso de necesidad, Android Studio también nos proporciona la
  posibilidad de programar parte de la aplicación en C++.
  
## Tecnologías para reconocimiento de imágenes y procesamiento del lenguaje natural
  
- **Text Recognition API de Google:** para el reconocimiento de texto en imágenes.<br/>
  https://developers.google.com/vision/android/text-overview
  
  Esta API nos permite la detección de texto a tres niveles:
  - **Bloque:** un conjunto de líneas como podrían ser un párrafo o una columna.
  - **Línea:** un conjunto de palabras contiguas localizadas en el mismo eje.
  - **Palabras:** un conjunto de caracteres alfanuméricos localizados en el mismo eje.
  
  ![texto](https://developers.google.com/vision/images/text-structure.png)
  
  Esto puede sernos de utilidad para extraer más información y facilitar la identificación de alimentos almacenados en latas,
  bricks, etc.

- **Stanford CoreNLP:** para el procesamiento de lenguaje natural.<br/>
  https://stanfordnlp.github.io/CoreNLP/ <br/>
  https://nlp.stanford.edu/software/spanish-faq.shtml
  
  Nuestro sistema debe ser capaz de, a partir de un documento, procesarlo y extraer la información sobre en qué día y momento se
  deben consumir los platos de la dieta. <br/>
  Esta API compatible con Java nos proporciona funcionalidades para etiquetado de palabras en categorías gramaticales y
  extracción de sustantivos en su versión para español.
  
  | Name  | Description | Example |
  | ------------- | ------------- | ------------- |
  | grup.z *  | Numeral group  | el **2,85 %**  |
  | grup.nom  | Noun group  | los **mercados europeos** permitieron  |
  | grup.prep *  | Preposition group  | **a partir de** hoy  |
  | grup.w *  | Date group  | desde el **19 de mayo** por  |
  | sadv  | Adverbial phrase  | **Al final** se realizó  |
  
- **OpenCV:** para análisis de imágenes y detección de objetos.<br/>
  https://opencv.org/
  
  OpenCV es una librería *open source* con soporte para Android y Java y diseñada para rendir eficientemente en aplicaciones
  en tiempo real. Para su utilización será necesaria la creación y entrenamiento de un modelo de aprendizaje para la detección
  y distinción de distintos alimentos haciendo uso de la cámara del móvil.
 
  ![texto](http://answers.opencv.org/upfiles/14285711591298042.png)
  
  ## Identificación de ingredientes
  
- **Edamam API:** para averiguar los ingredientes de cada receta utilizaremos la API buscador de recetas de Edamam.
  https://developer.edamam.com/es/api-recetas-edamam-documentacion

  Tras el procesado de la dieta se enviarán consultas HTTPS a las correspondientes URL para obtener los JSON de distintas
  recetas. Entre la información devuelta encontraríamos:
  
  - Base con más de 200,000 de recetas.
  - Base de datos de recetas normalizada.
  - Lista de ingredientes.
  - Nutrición detallada para cada receta
  - Filtros por calorías y dietas
  - Declaración de alérgenos
  
  **Ejemplo:** https://test-es.edamam.com/search?q=ensalada
