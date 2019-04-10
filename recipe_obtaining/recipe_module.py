# -*- coding: utf-8 -*-



'''
    IMPORTS NECESARIOS
'''

import urllib.request
import json
import difflib
from recipeconfig import edamam_endpoint, app_id, app_key, dandelion_endpoint, dandelion_key, measures_dict

'''
    IMPLEMENTACIÓN DE LA FUNCIONALIDAD DEL SERVICIO REST.
'''

'''
    Dado un plato (en forma de texto), devuelve su receta.
    La receta consistirá en una lista de tuplas de tres elementos:
        -El primer elemento hará referencia al número de unidades que se utilicen.
        -El segundo elemento hará referencia a las unidades/métrica que se utilice (ml, kg, etc).
        -El tercer elemento será el nombre del ingrediente.
    
    -Input: string que represente el plato del cual se busque la cadena.
    -Output: lista de diccionarios con tres campos: "num", "units" y "name". Si sucede algún error, devuelve una lista vacia.
'''
#Dado un plato, devuelve su receta.
def get_recipe(plate):
    plate=parse_blank_spaces(plate)
    contents = get_edamam_recipe(plate)
    
    #contents es de tipo bytes. Es necesario pasarlo a json.
    contents = parse_bytes_to_JSON(contents)
    
    #En el caso de que encontremos alguna receta que encaje.
    if contents['count'] !=0:
        
        best_hit=get_best_hit(plate,contents)["recipe"]["ingredientLines"] #Devuelve una lista con los ingredientes sin procesar    
        result=parse_recipe(best_hit)
        #actualmente result es una lista.
        #Devolvemos el JSON correspondiente.        
        return {"state":"Success", "recipe":result}     #El parámetro del diccionario devuelto "state" indicará si la operación tuvo éxito o no.
    else:
        #Si ninguna receta encaja.
        return {"state":"Error"}




'''
    Dado un conjunto de recetas encontradas, devuelve la que encaja más con la entrada introducida por el usuario.
    La receta que encaja más con la del usuario es aquella que tiene mayor semejanza a nivel de cadenas (caracteres).

    -Input: 
        plate: cadena de caracteres que representa el plato introducido por el usuario.
        contentJSON: conjunto de matches obtenidos en la llamada a la API.
    
    -Output:
        Receta (anteriormente contenida en contentJSON) que encaja más con el plato introducido por el usuario. 
'''
def get_best_hit(plate,contentJSON):
    #Para cada posible match, buscamos el que tiene mayor similaridad.
    #La similaridad se busca mediante el encaje de cadenas. BUSCAR MEJORA.
    max=0    
    for x in contentJSON["hits"] :
        similarity=get_label_similarity(plate,x["recipe"]["label"])
        if(similarity>max):
            max=similarity
            best_hit=x
            
    return best_hit


'''
    Dada una receta, se parsea tal y como se explica abajo.

    -Input: 
        Lista de ingredientes sin parsear. Un ejemplo de ingrediente podría ser:
        
            "3 medium-sized Yukon Gold potatoes (about 1 1/2 pounds), peeled and quartered lengthwise"
    
    -Output:
        Lista de ingredientes parseados del siguiente modo:
            Para cada ingrediente se crea un diccionario con 3 elementos:
                1- Número de unidades de ese ingrediente.
                2- Unidades (mg, gr, etc) de ese ingrediente.
                3- Nombre del ingrediente
'''
def parse_recipe(input):
    result=[]
    
    #Para cada ingrediente.
    for x in input:
        d={"num":"", "units":"", "name":"" }
        
        if((x.split()[0]).isdigit()):
            d["num"]= int(x.split()[0])

        if(is_measure_unity(x.split()[1])):
            d["units"]= x.split()[1]

    
        #PONER EL VALOR QUE CORRESPONDE.
        d["name"]=get_entities_dandelion(x)

        
        if(d["name"]==None):
            print()
        else:
            result.append(d)
            
    return result


'''
    Dada una entrada (supuestamente el nombre de un plato), parsea su contenido sustituyendo los espacios
    en blanco por "%20".
    
    Esto se utiliza cuando el plato está compuesto por múltiples palabras (por ejemplo, tortilla de patata).
    En el caso de que el nombre del plato esté compuesto por una sola palabra (por ejemplo, lentejas), este método
    no hará nada.
    
    Para realizar una petición get, es necesario convertir:
        tortilla de patatas ==>  tortilla%20de%20patatas
        
    el resultado es un string parseado tal y como se indica arriba.
    
'''
def parse_blank_spaces(input):
    #Si no tenemos una sola palabra, parsea. En caso contrario, no hace nada.
    return input.replace(" ", "%20") 

'''
    Dado un objeto bytes, obtiene el json asociado.
    
    Se utiliza para formatear el resultado de las llamadas a APIs que devuelven un json.
    
    input: objeto tipo bytes a formatear.
    
    el resultado es un objeto json (diccionario)
    
'''    
def parse_bytes_to_JSON(input):
    decoded = input.decode('utf8') #Decodificamos usando utf-8. El resultado es un string con forma de json.
    return json.loads(decoded);  #Creamos el json a partir del string    


'''
    Obtiene la similaridad entre dos cadenas.
    
    Se usa para comparar las etiquetas de la receta que se solicita y las que encuentra el sistema.
    
    first: primera etiqueta a comparar.
    second: etiqueta que se comparara a first.

    el resultado es un porcentaje.

'''
def get_label_similarity(first, second):
    #Comparará las dos entradas en minuscula.
    seq = difflib.SequenceMatcher(None,first.lower(),second.lower())
    return seq.ratio()*100

'''
    Dada una palabra, comprueba si es una medida/métrica. Para ello, consulta si existe esa cadena de caracteres en el
    diccionario "measures_dict"
    
    -Input: cadena de caracteres.
    -Output: booleano que indica si es una métrica/medida.
'''

def is_measure_unity(word):
    return (word in measures_dict)


'''
    Dado el nombre de un plato, obtiene la receta a partir de la API edamam.
    
    -Input: cadena de caracteres con el nombre del plato introducido por el usuario.
        
    -Output: respuesta del API edamam a la solicitud en forma de objeto tipo bytes.
'''
def get_edamam_recipe(name):
    return  urllib.request.urlopen(edamam_endpoint+ app_id + "&app_key="+app_key+"&q=+"+name).read()



'''
    Dado un texto, obtiene las entidades que contiene. 
    
    
    TODO (improve).
'''
def get_entities_dandelion(text):
    result= urllib.request.urlopen(genera_dandelion_URL(text)).read()
    result =parse_bytes_to_JSON(result)
       
    if(len(result["annotations"])==0):
        return None
    
    if(len(result["annotations"])>1):
        return result["annotations"][1]["spot"]
    
    return result["annotations"][0]["spot"]
    
    
    
'''
    Dado un texto (input), genera la URL para realizar la petición a la API dandelion para así obtener las entidades de dicho texto.
    
    -Input: cadena de caracteres que contiene el texto del cual queremos obtener las entidades.
    -Ouput: cadena de caracteres que contendrá la URL a utilizar para realizar la petición a la API.

'''
def genera_dandelion_URL(input):
        textURL = "text="
        topEntitiesURL= "top_entities=10"
        includeURL = "include=types%2Ccategories"
    
        token = "token="+dandelion_key;
        splitInput=input.split()
        
        for x in input.split():
            textURL = textURL + x + "+"
            
        textURL = textURL + splitInput[len(splitInput)-1]
        
        return dandelion_endpoint + textURL +"&"+topEntitiesURL+"&"+includeURL+ "&"+token
