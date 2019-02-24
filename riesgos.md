# Análisis de riesgos

En este documento se describen los principales riesgos del proyecto. Cada uno de ellos consta de una breve descripción del mismo, un plan de contingencia en caso de que ocurran y además están categorizados en base a:
- la probabilidad de que ocurran:
  - Baja - B.
  - Media - M.
  - Alta - A.
- el impacto dentro del proyecto:
  - Ligeramente dañino - LD.
  - Dañino - D.
  - Extremadamente dañino - ED.

## Clasificación de riesgos

A continuación tenemos una tabla con los distintos niveles de riesgo en base a la probabilidad y el impacto de los mismos:

<center>
<table>
  <tr>
    <th colspan="2" rowspan="2"></th>
    <th colspan="3"><b>IMPACTO</b></th>
  </tr>
  <tr>
    <td align="center"><b>LD</b></td>
    <td align="center"><b>D</b></td>
    <td align="center"><b>ED</b></td>
  </tr>
  <tr>
    <td rowspan="3"><b>PROBABILIDAD</b></td>
    <td align="center"><b>B</b></td>
    <td><img src=https://placehold.it/15/F3F781/000000?text=+> Riesgo trivial (T)</td>
    <td><img src=https://placehold.it/15/FFFF00/000000?text=+> Riesgo tolerable (TO)</td>
    <td><img src=https://placehold.it/15/FA5858/000000?text=+> Riesgo moderado (MO)</td>
  </tr>
  <tr>
    <td align="center"><b>M</b></td>
    <td><img src=https://placehold.it/15/FFFF00/000000?text=+> Riesgo tolerable (TO)</td>
    <td><img src=https://placehold.it/15/FA5858/000000?text=+> Riesgo moderado (MO)</td>
    <td><img src=https://placehold.it/15/DF0101/000000?text=+> Riesgo importante (I)</td>
  </tr>
  <tr>
    <td align="center"><b>A</b></td>
    <td><img src=https://placehold.it/15/FA5858/000000?text=+> Riesgo moderado (MO)</td>
    <td><img src=https://placehold.it/15/DF0101/000000?text=+> Riesgo importante (I)</td>
    <td><img src=https://placehold.it/15/6E6E6E/000000?text=+> Riesgo intolerable (IN)</td>
  </tr>
</table>
</center>

- ![#F3F781](https://placehold.it/15/F3F781/000000?text=+) **Trivial (T)**: no se requiere acción específica.
- ![#FFFF00](https://placehold.it/15/FFFF00/000000?text=+) **Tolerable (TO)**: se han de solucionar aunque no suponen un gran esfuerzo.
- ![#FA5858](https://placehold.it/15/FA5858/000000?text=+) **Moderado (M)**: se ha de paliar el riesgo pero se puede continuar con el desarrollo.
- ![#DF0101](https://placehold.it/15/DF0101/000000?text=+) **Importante (I)**: no se ha de continuar el desarrollo sin antes haber minimizado el riesgo lo máximo posible.
- ![#6E6E6E](https://placehold.it/15/6E6E6E/000000?text=+) **Intolerable (IN)**: no se debe continuar con el trabajo y es necesario solucionarlo antes de continuar con el trabajo.

## Tabla de riesgos

| **Poca luz al usar la aplicación** |
| :------------- |
| No hay suficiente luz en el entorno para escanear correctamente las dietas o para reconocer los objetos de la nevera | 
| **Categoría:** ![#F3F781](https://placehold.it/15/F3F781/000000?text=+) Riesgo trivial|
| **Impacto:** Ligeramente dañino|
| **Probabilidad:** Baja |
| **Plan de contingencia:** Bastaría con cambiar el ángulo de la cámara y probar a ver si se capta mejor la imagen, aunque en caso de no ser posible bastaría con usar una fuente de luz auxiliar|

| **Las tecnologías no cumplen las espectativas** |
| :------------- |
| La integración y uso de las tecnologías es más complicada de lo que parecía en un primer momento | 
| **Categoría:** ![#6E6E6E](https://placehold.it/15/6E6E6E/000000?text=+) Riesgo intolerable|
| **Impacto:** Extremadamente dañino |
| **Probabilidad:** Alta |
| **Plan de contingencia:** Sería necesario volver a buscar nuevas tecnologías que cumpliesen con las funciones necesitadas. En caso de que se llegase a este punto se tendría que volver a hacer la planificación temporal con el consiguiente reajuste de las tareas del proyecto |

| **Falta de conocimiento técnico** |
| :------------- |
| Los desarrolladores carecen de la experiencia necesaria en Android de cara al proyecto | 
| **Categoría:** ![#F3F781](https://placehold.it/15/F3F781/000000?text=+) Riesgo trivial|
| **Impacto:** Ligeramente daniño |
| **Probabilidad:** Baja |
| **Plan de contingencia:** Al tratarse de una aplicación sencilla y al contar con un equipo de desarrollo con expeiencia en Android no se debería llevar a cabo ninguna acción si se llega a este escenario, aunque convendría recurrir a otros proyectos o recursos online (YouTube, blogs) si se diese el caso|

| **Malos tiempos de respuesta** |
| :------------- |
| La aplicación no responde con la suficiente velocidad a la hora de generar la lista de la compra | 
| **Categoría:** ![#FFFF00](https://placehold.it/15/FFFF00/000000?text=+) Riesgo tolerable |
| **Impacto:** Dañino|
| **Probabilidad:** Baja|
| **Plan de contingencia:** En este caso bastaría con reducir la carga de las vistas de la aplicación bien eliminando información innecesaria o reduciendo las conexiones con servicios externos|

| **Abandono de un miembro del equipo** |
| :------------- |
| Un miembro del equipo abandona el proyecto a mitad del desarrollo | 
| **Categoría:** ![#FA5858](https://placehold.it/15/FA5858/000000?text=+) Riesgo moderado |
| **Impacto:** Extremadamente dañino |
| **Probabilidad:** Baja |
| **Plan de contingencia:** Sería necesario reducir el ámbito de la aplicación o repartir la carga de trabajo entre los demás miembros del equipo|

| **Algún miembro del equipo no cumple con la planificación fijada** |
| :------------- |
| Uno o varios miembros del equipo no se ajustan con los plazos de las tareas o no consideran las dependencias entre ellas | 
| **Categoría:** ![#FA5858](https://placehold.it/15/FA5858/000000?text=+) Riesgo moderado |
| **Impacto:** Extremadamente dañino|
| **Probabilidad:** Baja|
| **Plan de contingencia:** Sería necesario reajustar la planificación temporal y las dependencias de las tareas para minimizar lo máximo posible el trastorno causado por los retrasos de las entregas|

| **Redundancia de información en los sistemas de gestión de datos** |
| :------------- |
| El modelo de la base de datos de la aplicación es inconsistente o hay datos incompletos | 
| **Categoría:** ![#FFFF00](https://placehold.it/15/FFFF00/000000?text=+) Riesgo tolerable |
| **Impacto:** Dañino|
| **Probabilidad:** Baja|
| **Plan de contingencia:** En este caso convendría revisar el diseño del almacenamiento de datos y eliminar la información inconsistente del sistema|

| **Planificación de tareas inconsistente** |
| :------------- |
| La planificación del desarrollo tanto a nivel de tareas como los plazos de entrega no son realistas | 
| **Categoría:** ![#DF0101](https://placehold.it/15/DF0101/000000?text=+) Riesgo importante|
| **Impacto:** Extremadamente dañino |
| **Probabilidad:** Media |
| **Plan de contingencia:** Es imperativo rehacer la planificación temporal y revisar las dependencias de tareas lo antes posible antes de seguir con el desarrollo. En caso de que no supusiese un impacto demasiado grande en el proyecto bastaría con reorganizar las tareas y retocar la planificación del mismo|
