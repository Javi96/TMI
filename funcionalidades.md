# Funcionalidades
El proyecto tiene como funcionalidad principal creas listas con los ingredientes que faltan para realizar determinadas recetas en función de los que ya se posean. Para ello tenemos 2 funcionalidades básicas: 

- **Inventariado:** mediante la realización de fotos y reconocimiento de imágenes se podrán agregar nuevos ingredientes que aparecen en las fotos a la lista de ingredientes. Se permitirá realizar fotos sucesivas para incrementar gradualmente el inventario. También se permitirá al usuario agregar, modificar y eliminar manualmente dichos ingredientes. 

- **Recetario:** se permitirá el registro de nuevos platos. Para ello se podrán introducir los ingredientes requeridos de forma manual o introducir el nombre de un plato para obtener de forma automática los ingredientes recomendados para su elaboración. En este segundo caso, se le mostrarán al usuario dichos ingredientes por si quiere modificarlos. 

Combinando las 2 funcionalidades anteriores, se podrá pasar a utilizar la funcionalidad principal de la aplicación:

- **Lista de la compra:** dado el inventario de ingredientes que se poseen y la de ingredientes que se necesitan, se hará una lista de todos los ingredientes que faltan para poder elaborar las recetas indicadas. De nuevo dicha lista podrá ser modificada por el usuario y permitirá ir marcando qué ingredientes se han obtenido ya. Asimismo, el usuario podrá indicar si los ingredientes comprados se deben incluir en el inventario, si se ha comprado más de los necesarios para realizar las recetas; o no quiere que se añadan, si se ha comprado la cantidad justa para la elaboración. 

Una vez se realiza una compra, el inventario se podrá seguir utilizando tal y como haya acabado en función de los ingredientes utilizados. Asimismo se podrá vaciar opcionalmente el inventario por si se prefiere empezar de cero con el inventario vacío. 

## Funcionalidades opcionales
Las siguientes funcionalidades se llevarán a cabo según avance el desarrollo del proyecto. 

- **Cuantificación del inventario:** se proporcionarán al usuario 4 posibles categorías para los diferentes ingredientes, de forma que el inventario sea más preciso: 
  - **Masa**: ingredientes que se miden en gramos (p.e. harina) 
  - **Volumen**: ingredientes que se miden en litros (p.e. leche) 
  - **Unitarios**: ingredientes que se cuentan en unidades (p.e. huevos) 
  - **No cuantificables**: ingredientes que no se pueden medir o no se desea medir (p.e. sal o especias) 

- **Medidas nutricionales:** se ofrecerá al usuario la posibilidad de obtener la información nutricional de los ingredientes y los platos. 
