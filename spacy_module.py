# -*- coding: utf-8 -*-

'''
    Módulo encargado de realizar el procesamiento de lenguaje natural sobre las sentencias que nos devuelve la API de ingredientes.
    
    Dada una sentencia que representa un ingrediente de un plato, se encargará de eliminar los elementos superficiales y parsear dicha sentencia
    para devolver el propio ingrediente de la sentencia.
    
    Ejemplo:
        
        Entradas:
            -1 tablespoon prepared barbecue sauce
            -6 hamburger buns
            -Kosher salt, freshly ground pepper            
            
        Salidas:
            -barbecue sauce
            -hamburger buns
            -Kosher salt
'''


#Imports necesarios
import spacy
import difflib
import random 

#Diccionario de medidas. Almacenará términos que implicarán medidas que deben ignorarse.
#'spoon words' se mira de forma especial.
measures={ 'cup':None,  'x':None,  'inch':None,'tbsp':None,'oz':None,'ounce':None,'slices':None,'pinch':None,'tsp':None,'lb':None,'lbs':None }

#Modelo spacy que usa el sistema para analizar las sentencias en lenguaje natural.
nlp = spacy.load("en_core_web_sm")

'''
    Dada una cadena de caracteres, determina si alguno de estos caracteres representa un dígito
    
    Devuelve un valor booleano (True o False)
'''
def hasNumbers(inputString):
    return any(char.isdigit() for char in inputString)

'''
    Dada una cadena de caracteres, elimina todo palabra que contenga dicha cadena que esté formada por
    un único caracter.
    
    Input:
            cadena de caracteres.
    Output:
            cadena de caracteres con las palabras de un único caracter eliminadas.
'''
def delete_single_chars(inputString):
    string= inputString.split()
    result=""
    
    for x in string:
        if len(x)>1:
            result = result + " " +x
    
    return result[1:] #Eliminamos el espacio inicial que se crea de serie.


'''
    Dada una cadena de caracteres, elimina todos los términos relacionados con medidas. 
    Estos términos son los que se encuentran en el diccionario measures.
    
    Input:
        -Cadena de caracteres.
    Output:
        -Cadena de caracteres de la cual se han eliminado las apariciones de los términos que aparecen
        en el diccionario measures y las que terminan en 'spoon'.

'''

def delete_measures(inputString):
    string= inputString.split()
    
    for x in string:
       
        if (len(x)>5 and x[-5:]=='spoon' or (len(x)>6 and x[-6:]=='spoons')):
            inputString=inputString.replace(x,'')[1:] #Eliminamos la medida.
        else:
            for key in measures.keys():
                if get_label_similarity(key,x) > 90:
                    inputString=inputString.replace(x,'')[1:] #Eliminamos la medida.
                
    return inputString
    
'''
   Dada una sentencia de caracteres que representa un Noun Chunk (término que representa un nombre y 
   los términos que tiene asociado. Por ejemplo : Red friend pepper), devuelve la cadena de caracteres que 
   contiene el nombre (noun) principal y su término predecesor.
   
   Por ejemplo:
       input: 'Black Fried Pepper'
       output: 'Fried Pepper'
       
'''
def get_root(inputString):
    string= inputString.split()
    
    if len(string)>2:
        for x in  range(len(string)-2):
            inputString=inputString.replace(string[x],'',1)[1:] #Eliminamos el espacio en blanco que genera
            print(inputString)
            
    return inputString

'''
    Dadas dos cadenas de caracteres, devuelve un índice (de 0 a 100) que determina el grado de similaridad que
    tienen estas dos cadenas.
    
    Input:
        first:cadena de caracteres
        second:cadena de caracteres
        
    Output:
        double (de 0 a 100) que representa el grado de similaridad que tienen las dos cadenas proporcionadas
'''
def get_label_similarity(first, second):
    #Comparará las dos entradas en minuscula.
    seq = difflib.SequenceMatcher(None,first.lower(),second.lower())
    return seq.ratio()*100


'''
    Dada una cadena de caracteres, elimina los caracteres que se encuentran después de un paréntesis (si la 
    sentencia en lenguaje natural contiene dicho paréntesis)
    
    Ejemplo:
        -input: I like spanish omellete (and french too)
        -output: I like spanish omellete 
'''
def delete_parenthesis_text(inputString):
    
    for x in range(len(inputString)):
        if inputString[x]=='(':
            return inputString[0:x]
    
    return inputString

'''
    Dada una sentencia que representa un ingrediente de un plato, se encargará de eliminar los elementos superficiales y parsear dicha sentencia
    para devolver el propio ingrediente de la sentencia.
    
       
    Ejemplo:
        
        Entradas:
            -1 tablespoon prepared barbecue sauce
            -6 hamburger buns
            -Kosher salt, freshly ground pepper            
            
    Salidas:
            -barbecue sauce
            -hamburger buns
            -Kosher salt
        
    
    Parámetros:
        Input: cadena de caracteres que representa la sentencia en lenguaje natural que contiene el ingrediente.
        Output: cadena de caracteres que contiene el ingrediente asociado a la sentencia introducida
    
'''
def get_ingredient_name(stringInput):
    
    doc = nlp(stringInput)  #Analizamos usando Spacy 
    result=[]
    for chunk in doc.noun_chunks:
        
        #chunk text
        ct=chunk.text        
    
        #Eliminamos el primer número si lo es.
        if ct[0].isdigit():
            ct=ct[2:]
    
        #Si aún tiene números, lo desechamos.
        
        if not  hasNumbers(ct):
            ct=delete_single_chars(ct)
            ct=delete_measures(ct)
            
            ct=delete_parenthesis_text(ct)
            
            ct=get_root(ct)
            result.append(ct)
    
    if len(result)==0:
        #Si no hemos encontrado ninguna posible respuesta.
        return ""
    else:
        #Si hemos encontrado alguna posible respuesta.
        return result[random.randint(0,len(result)-1)]

#print(get_ingredient_name('1 tablespoon chopped fresh rosemary or 1 teaspoon crumbled dried rosemary'))
  
'''
print(chunk.text, chunk.root.text, chunk.root.dep_,
        chunk.root.head.text)   
'''

    

